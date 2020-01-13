package com.mapbox.api.matching.v5;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.api.matching.v5.models.MapMatchingResponse;
import com.mapbox.core.TestUtils;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_CURB;
import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_UNRESTRICTED;
import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_CYCLING;
import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_DRIVING;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapboxMapMatchingTest extends TestUtils {

  private static final String MAP_MATCHING_FIXTURE = "map_matching_v5_polyline.json";
  private static final String MAP_MATCHING_ERROR_FIXTURE = "mapmatching_nosegment_v5_polyline.json";
  private static final String MAP_MATCHING_APPROACHES = "mapmatching_v5_approaches.json";
  private static final String MAP_MATCHING_WAYPOINT_NAMES_FIXTURE = "mapmatching_v5_waypoint_names.json";


  private MockWebServer server;
  private HttpUrl mockUrl;
  private List<Point> coordinates;

  @Before
  public void setUp() throws Exception {
    server = new MockWebServer();
    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
          String resource = MAP_MATCHING_FIXTURE;
          if (request.getPath().contains("approaches")) {
            resource = MAP_MATCHING_APPROACHES;
          } else if (request.getPath().contains("waypoint_names")) {
            resource = MAP_MATCHING_WAYPOINT_NAMES_FIXTURE;
          } else if (request.getPath().contains("0,-40;0,-20")) { // no matching segment
            resource = MAP_MATCHING_ERROR_FIXTURE;
          }
            try {
          String response = loadJsonFixture(resource);
          return new MockResponse().setBody(response);
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }
      }
    });

    coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(13.418946862220764, 52.50055852688439));
    coordinates.add(Point.fromLngLat(13.419011235237122, 52.50113000479732));
    coordinates.add(Point.fromLngLat(13.419756889343262, 52.50171780290061));
    coordinates.add(Point.fromLngLat(13.419885635375975, 52.50237416816131));
    coordinates.add(Point.fromLngLat(13.420631289482117, 52.50294888790448));

    server.start();
    mockUrl = server.url("");
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(mapMatching);
  }

  @Test
  public void build_exceptionThrownWhenLessThanTwoCoordsProvided() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("At least two coordinates must be provided with your API request."));
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
    mapMatching.executeCall();
  }

  @Test
  public void build_throwsExceptionWhenNotMatchingRadiusesForEachCoord() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("There must be as many radiuses as there are coordinates."));
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .baseUrl("https://foobar.com")
      .radiuses(2d, 3d, 4d)
      .accessToken(ACCESS_TOKEN)
      .build();
    mapMatching.executeCall();
  }

  @Test
  public void build_throwsExceptionWhenNotMatchingTimestampsForEachCoord() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("There must be as many timestamps as there are coordinates."));
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .baseUrl("https://foobar.com")
      .timestamps("1", "1", "2")
      .accessToken(ACCESS_TOKEN)
      .build();
    mapMatching.executeCall();
  }

  @Test
  public void build_noAccessTokenExceptionThrown() throws Exception {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Missing required properties: accessToken");
    MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .build();
  }

  @Test
  public void build_invalidAccessTokenExceptionThrown() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("Using Mapbox Services requires setting a valid access token.");
    MapboxMapMatching.builder()
      .accessToken("")
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .build();
  }

  @Test
  public void clientAppName_doesSetInHeaderCorrectly1() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .baseUrl(mockUrl.toString())
      .clientAppName("APP")
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(mapMatching.cloneCall().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void clientAppName_doesSetInHeaderCorrectly2() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .baseUrl(mockUrl.toString())
      .clientAppName("APP")
      .accessToken(ACCESS_TOKEN)
      .build();

    assertTrue(mapMatching.executeCall().raw().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void mapMatchingToDirectionsRoute() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(mapMatching.executeCall().body().matchings().get(0).toDirectionRoute());
  }


  @Test
  public void accessToken_doesGetPlacedInUrlCorrectly() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(mapMatching.cloneCall().request().url().toString().contains("access_token=pk.XXX"));
  }

  @Test
  public void tidy_doesShowInUrlCorrectly() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .baseUrl("https://foobar.com")
      .tidy(true)
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(mapMatching.cloneCall().request().url().toString().contains("tidy=true"));
  }

  @Test
  public void user_doesShowInUrlCorrectly() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .baseUrl("https://foobar.com")
      .user("userString")
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(mapMatching.cloneCall().request().url().toString().contains("/userString/"));
  }

  @Test
  public void profile_doesShowInUrlCorrectly() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .baseUrl("https://foobar.com")
      .profile(PROFILE_DRIVING)
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(mapMatching.cloneCall().request().url().toString().contains("/driving/"));
  }

  @Test
  public void coordinates_doesShowInUrlCorrectly() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.1234, 3.3456))
      .coordinate(Point.fromLngLat(90.10293, 7.10293))
      .coordinate(Point.fromLngLat(100.10203, 84.039))
      .baseUrl("https://foobar.com")
      .profile(PROFILE_DRIVING)
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(mapMatching.cloneCall().request().url().toString()
      .contains("2.1234,3.3456;90.10293,7.10293;100.10203,84.039"));
  }


  @Test
  public void geometries_doesShowInUrlCorrectly() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.1234, 3.3456))
      .coordinate(Point.fromLngLat(90.10293, 7.10293))
      .coordinate(Point.fromLngLat(100.10203, 84.039))
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();
    assertTrue(mapMatching.cloneCall().request().url().toString()
      .contains("geometries=polyline"));
  }

  @Test
  public void radiuses_doesShowInUrlCorrectly() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.1234, 3.3456))
      .coordinate(Point.fromLngLat(90.10293, 7.10293))
      .coordinate(Point.fromLngLat(100.10203, 84.039))
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .radiuses(1d, 2d, 3d)
      .build();
    assertEquals("1.0;2.0;3.0",
      mapMatching.cloneCall().request().url().queryParameter("radiuses"));
  }

  @Test
  public void steps_doesShowInUrlCorrectly() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.1234, 3.3456))
      .coordinate(Point.fromLngLat(90.10293, 7.10293))
      .coordinate(Point.fromLngLat(100.10203, 84.039))
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .steps(true)
      .build();
    assertTrue(mapMatching.cloneCall().request().url().toString()
      .contains("steps=true"));
  }

  @Test
  public void overview_doesShowInUrlCorrectly() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.1234, 3.3456))
      .coordinate(Point.fromLngLat(90.10293, 7.10293))
      .coordinate(Point.fromLngLat(100.10203, 84.039))
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .overview(DirectionsCriteria.OVERVIEW_FULL)
      .build();
    assertTrue(mapMatching.cloneCall().request().url().toString()
      .contains("overview=full"));
  }

  @Test
  public void timestamps_doesShowInUrlCorrectly() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.1234, 3.3456))
      .coordinate(Point.fromLngLat(90.10293, 7.10293))
      .coordinate(Point.fromLngLat(100.10203, 84.039))
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .timestamps("1", "2", "3")
      .build();
    assertEquals("1;2;3",
      mapMatching.cloneCall().request().url().queryParameter("timestamps"));
  }

  @Test
  public void annotations_doesShowInUrlCorrectly() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.1234, 3.3456))
      .coordinate(Point.fromLngLat(90.10293, 7.10293))
      .coordinate(Point.fromLngLat(100.10203, 84.039))
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .annotations(
        DirectionsCriteria.ANNOTATION_DISTANCE,
        DirectionsCriteria.ANNOTATION_CONGESTION
      ).build();
    assertEquals("distance,congestion",
      mapMatching.cloneCall().request().url().queryParameter("annotations"));
  }

  @Test
  public void language_doesShowInUrlCorrectly() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.1234, 3.3456))
      .coordinate(Point.fromLngLat(90.10293, 7.10293))
      .coordinate(Point.fromLngLat(100.10203, 84.039))
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .language(Locale.US)
      .build();
    assertTrue(mapMatching.cloneCall().request().url().toString()
      .contains("language=en"));
  }

  @Test
  public void baseUrl_doesShowInUrlCorrectly() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.1234, 3.3456))
      .coordinate(Point.fromLngLat(90.10293, 7.10293))
      .coordinate(Point.fromLngLat(100.10203, 84.039))
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(mapMatching.cloneCall().request().url().toString()
      .startsWith("https://foobar.com"));
  }


  @Test(expected = ServicesException.class)
  public void build_exceptionThrownWhenLessThanTwoSeparatesLegsProvided() {
    MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .waypointIndices(0)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
  }

  @Test(expected = ServicesException.class)
  public void build_exceptionThrownWhenSeparatesLegsDoNotStartWith0() {
    MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(3.0, 3.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .waypointIndices(1, 2)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
  }

  @Test(expected = ServicesException.class)
  public void build_exceptionThrownWhenSeparatesLegsDoNotEndWithLast() {
    MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(3.0, 3.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .waypointIndices(0, 1)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
  }

  @Test(expected = ServicesException.class)
  public void build_exceptionThrownWhenMiddleSeparatesLegsAreWrong() {
    MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(3.0, 3.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .waypointIndices(0, 3, 2)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
  }

  @Test
  public void sanitySeparatesLegs() {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(3.0, 3.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .waypointIndices(0, 1, 2)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(mapMatching);
  }

  @Test
  public void sanityVoiceInstructions() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .voiceInstructions(true)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(mapMatching);
    assertTrue(mapMatching.cloneCall().request().url().toString()
      .contains("voice_instructions=true"));
  }

  @Test
  public void sanityVoiceUnits() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .voiceInstructions(true)
      .voiceUnits(DirectionsCriteria.METRIC)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(mapMatching);
    assertTrue(mapMatching.cloneCall().request().url().toString()
      .contains("voice_units=metric"));
  }

  @Test
  public void sanityBannerInstructions() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .bannerInstructions(true)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(mapMatching);
    assertTrue(mapMatching.cloneCall().request().url().toString()
      .contains("banner_instructions=true"));
  }

  @Test
  public void sanityRoundExtsInstructions() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .roundaboutExits(true)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(mapMatching);
    assertTrue(mapMatching.cloneCall().request().url().toString()
      .contains("roundabout_exits=true"));
  }

  @Test
  public void noValidMatchTest() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(0, -40))
      .coordinate(Point.fromLngLat(0, -20 ))
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .build();

    Response<MapMatchingResponse> response = mapMatching.executeCall();
    assertThat(response.body().message(), containsString("Could not find"));
    assertThat(response.body().code(), containsString("NoSegment"));
  }

  @Test
  public void sanityApproachesInstructions() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(3.0, 3.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .addApproaches(APPROACH_UNRESTRICTED, null, "", APPROACH_CURB)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(mapMatching);
    assertEquals("unrestricted;;;curb",
      mapMatching.cloneCall().request().url().queryParameter("approaches"));
  }

  @Test
  public void sanityApproachesOptional() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(3.0, 3.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(mapMatching);
    assertFalse(mapMatching.cloneCall().request().url().toString()
      .contains("approaches"));
  }

  @Test
  public void build_exceptionThrownWhenNumApproachesDoesNotMatchCoordinates() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("Number of approach elements must match"));
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .addApproaches(APPROACH_UNRESTRICTED)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
  }

  @Test
  public void build_exceptionThrownWhenInvalidApproaches() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("All approaches values must be one of curb, unrestricted"));
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .addApproaches(APPROACH_UNRESTRICTED, "restricted")
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
  }

  @Test
  public void testApproaches() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .profile(PROFILE_DRIVING)
      .coordinate(Point.fromLngLat(-117.1728265285492,32.71204416018209))
      .coordinate(Point.fromLngLat(-117.17334151268004,32.71254065549407))
      .addApproaches(APPROACH_UNRESTRICTED, APPROACH_CURB)
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .build();

    Response<MapMatchingResponse> response = mapMatching.executeCall();
    assertEquals(200, response.code());
    assertEquals("Ok", response.body().code());
  }

  @Test
  public void testRouteOptionsApproaches() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .profile(PROFILE_DRIVING)
      .coordinate(Point.fromLngLat(-117.1728265285492,32.71204416018209))
      .coordinate(Point.fromLngLat(-117.17334151268004,32.71254065549407))
      .addApproaches(APPROACH_UNRESTRICTED, APPROACH_CURB)
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .build();

    Response<MapMatchingResponse> response = mapMatching.executeCall();
    RouteOptions routeOptions = response.body().matchings().get(0).routeOptions();

    assertEquals("unrestricted;curb", routeOptions.approaches());
  }

  @Test
  public void sanityWaypointNamesInstructions() {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .waypointIndices(0, 1 ,2)
      .addWaypointNames("Home", "Store", "Work")
      .build();
    assertNotNull(mapMatching);
    assertEquals("Home;Store;Work",
      mapMatching.cloneCall().request().url().queryParameter("waypoint_names"));
  }

  @Test
  public void testWithWaypointNames() throws Exception {

    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .profile(PROFILE_DRIVING)
      .coordinates(Arrays.asList(
        Point.fromLngLat(2.344003915786743,48.85805170891599),
        Point.fromLngLat(2.346750497817993,48.85727523615161),
        Point.fromLngLat(2.348681688308716,48.85936462637049),
        Point.fromLngLat(2.349550724029541,48.86084691113991),
        Point.fromLngLat(2.349550724029541,48.8608892614883),
        Point.fromLngLat(2.349625825881958,48.86102337068847),
        Point.fromLngLat(2.34982967376709,48.86125629633996)
        ))
      .steps(true)
      .tidy(true)
      .bannerInstructions(true)
      .waypointIndices(0, 6)
      .addWaypointNames("Home", "Work")
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .build();

    mapMatching.setCallFactory(null);
    Response<MapMatchingResponse> response = mapMatching.executeCall();
    assertEquals(200, response.code());
    assertEquals("Ok", response.body().code());
  }

  @Test
  public void testUsePostMethod() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
        .accessToken(ACCESS_TOKEN)
        .baseUrl(mockUrl.toString())
        .profile(PROFILE_DRIVING)
        .steps(true)
        .tidy(true)
        .waypointIndices("0;6")
        .coordinates(Arrays.asList(
            Point.fromLngLat(2.344003915786743,48.85805170891599),
            Point.fromLngLat(2.346750497817993,48.85727523615161),
            Point.fromLngLat(2.348681688308716,48.85936462637049),
            Point.fromLngLat(2.349550724029541,48.86084691113991),
            Point.fromLngLat(2.349550724029541,48.8608892614883),
            Point.fromLngLat(2.349625825881958,48.86102337068847),
            Point.fromLngLat(2.34982967376709,48.86125629633996)))
        .post()
        .build();
    Response<MapMatchingResponse> response = mapMatching.executeCall();
    assertEquals(200, response.code());
    assertEquals("Ok", response.body().code());

    assertNotNull(response.body().matchings());
    assertEquals(1, response.body().matchings().size());
  }

  @Test
  public void testCallForUrlLength_longUrl() {
    MapboxMapMatching.Builder builder = MapboxMapMatching.builder()
      .profile(PROFILE_CYCLING)
      .steps(true)
      .coordinate(Point.fromLngLat(-122.42,37.78))
      .coordinate(Point.fromLngLat(-77.03,38.91))
      .voiceInstructions(true)
      .voiceUnits(DirectionsCriteria.IMPERIAL)
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString());
    addWaypoints(builder, 400);

    retrofit2.Call<MapMatchingResponse> call = builder.build().initializeCall();

    assertEquals("POST", call.request().method());
  }

  @Test
  public void testCallForUrlLength_shortUrl() {
    MapboxMapMatching.Builder builder = MapboxMapMatching.builder()
      .profile(PROFILE_CYCLING)
      .steps(true)
      .coordinate(Point.fromLngLat(-122.42,37.78))
      .coordinate(Point.fromLngLat(-77.03,38.91))
      .voiceInstructions(true)
      .voiceUnits(DirectionsCriteria.IMPERIAL)
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString());
    addWaypoints(builder, 10);

    retrofit2.Call<MapMatchingResponse> call = builder.build().initializeCall();

    assertEquals("GET", call.request().method());
  }

  @Test
  public void testPostIsUsed() {
    MapboxMapMatching.Builder builder = MapboxMapMatching.builder()
      .profile(PROFILE_CYCLING)
      .steps(true)
      .coordinate(Point.fromLngLat(-122.42,37.78))
      .coordinate(Point.fromLngLat(-77.03,38.91))
      .voiceInstructions(true)
      .voiceUnits(DirectionsCriteria.IMPERIAL)
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .post();

    retrofit2.Call<MapMatchingResponse> call = builder.build().initializeCall();

    assertEquals("POST", call.request().method());
  }

  @Test
  public void testGetIsUsed() {
    MapboxMapMatching.Builder builder = MapboxMapMatching.builder()
      .profile(PROFILE_CYCLING)
      .steps(true)
      .coordinate(Point.fromLngLat(-122.42,37.78))
      .coordinate(Point.fromLngLat(-77.03,38.91))
      .voiceInstructions(true)
      .voiceUnits(DirectionsCriteria.IMPERIAL)
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .get();

    retrofit2.Call<MapMatchingResponse> call = builder.build().initializeCall();

    assertEquals("GET", call.request().method());
  }

  private void addWaypoints(MapboxMapMatching.Builder builder, int number) {
    for (int i = 0; i < number; i++) {
      builder.coordinate(Point.fromLngLat(getRandomLng(), getRandomLat()));
    }
  }

  private double getRandomLng() {
    Random random = new Random();
    double lng = random.nextDouble() % 360;
    return lng - 180;
  }

  private double getRandomLat() {
    Random random = new Random();
    double lat = random.nextDouble() % 180;
    return lat - 90;
  }
}
