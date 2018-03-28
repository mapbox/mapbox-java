package com.mapbox.api.matching.v5;

import com.mapbox.api.directions.v5.DirectionsCriteria;
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
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapboxMapMatchingTest extends TestUtils {

  private static final String MAP_MATCHING_FIXTURE = "map_matching_v5_polyline.json";
  private static final String MAP_MATCHING_ERROR_FIXTURE = "mapmatching_nosegment_v5_polyline.json";

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
          if (request.getPath().contains("0,-40;0,-20")) { // no matching segment
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
  public void build_throwsExceptionWhenCoordsOverOneHundred() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("Maximum of 100 coordinates are allowed for this API."));
    List<Point> coordinates = new ArrayList<>();
    for (int i = 0; i < 101; i++) {
      coordinates.add(Point.fromLngLat(1.0 + i, 2.0 + i));
    }
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinates(coordinates)
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
  public void build_throwsExceptionWhenNoValidAccessTokenProvided() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("Using Mapbox Services requires setting a valid access token."));
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .baseUrl("https://foobar.com")
      .build();
    mapMatching.executeCall();
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
      .profile(DirectionsCriteria.PROFILE_DRIVING)
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
      .profile(DirectionsCriteria.PROFILE_DRIVING)
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
    assertTrue(mapMatching.cloneCall().request().url().toString()
      .contains("radiuses=1.0,2.0,3.0"));
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
    assertTrue(mapMatching.cloneCall().request().url().toString()
      .contains("timestamps=1,2,3"));
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
    assertTrue(mapMatching.cloneCall().request().url().toString()
      .contains("annotations=distance,congestion"));
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


  @Test
  public void build_exceptionThrownWhenLessThanTwoWayPointsProvided() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("Waypoints must be a list of at least two indexes separated by"));
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .waypoints(0)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
  }

  @Test
  public void build_exceptionThrownWhenWaypointsDoNotStartWith0() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("Waypoints must contain indices of the first and last coordinates"));
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(3.0, 3.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .waypoints(1, 2)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
  }

  @Test
  public void build_exceptionThrownWhenWaypointDoNotEndWithLast() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("Waypoints must contain indices of the first and last coordinates"));
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(3.0, 3.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .waypoints(0, 1)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
  }

  @Test
  public void build_exceptionThrownWhenMiddleWaypointsAreWrong() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("Waypoints index too large (no corresponding coordinate)"));
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(3.0, 3.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .waypoints(0, 3, 2)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
  }

  @Test
  public void sanityWaypoints() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(3.0, 3.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .waypoints(0, 1, 2)
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

}
