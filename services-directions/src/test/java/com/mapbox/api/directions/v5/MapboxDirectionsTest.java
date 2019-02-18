package com.mapbox.api.directions.v5;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.api.directions.v5.models.BannerComponents;
import com.mapbox.api.directions.v5.models.BannerInstructions;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.LegAnnotation;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.core.TestUtils;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;

import org.hamcrest.core.StringStartsWith;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_CURB;
import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_UNRESTRICTED;
import static com.mapbox.api.directions.v5.DirectionsCriteria.GEOMETRY_POLYLINE;
import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_CYCLING;
import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_DRIVING;
import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_DRIVING_TRAFFIC;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapboxDirectionsTest extends TestUtils {

  private static final String DIRECTIONS_V5_FIXTURE = "directions_v5.json";
  private static final String DIRECTIONS_V5_PRECISION6_FIXTURE = "directions_v5_precision_6.json";
  private static final String DIRECTIONS_TRAFFIC_FIXTURE = "directions_v5_traffic.json";
  private static final String DIRECTIONS_ROTARY_FIXTURE = "directions_v5_fixtures_rotary.json";
  private static final String DIRECTIONS_V5_ANNOTATIONS_FIXTURE = "directions_annotations_v5.json";
  private static final String DIRECTIONS_V5_NO_ROUTE = "directions_v5_no_route.json";
  private static final String DIRECTIONS_V5_MAX_SPEED_ANNOTATION = "directions_v5_max_speed_annotation.json";
  private static final String DIRECTIONS_V5_BANNER_INSTRUCTIONS = "directions_v5_banner_instructions.json";
  private static final String DIRECTIONS_V5_APPROACHES_REQUEST = "directions_v5_approaches.json";
  private static final String DIRECTIONS_V5_WAYPOINT_NAMES_FIXTURE = "directions_v5_waypoint_names.json";
  private static final String DIRECTIONS_V5_WAYPOINT_TARGETS_FIXTURE = "directions_v5_waypoint_targets.json";
  private static final String DIRECTIONS_V5_POST = "directions_v5_post.json";

  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

        // Switch response on geometry parameter (only false supported, so nice and simple)
        String resource = DIRECTIONS_V5_FIXTURE;
        if (request.getPath().contains("-77.04430")) {
          resource = DIRECTIONS_ROTARY_FIXTURE;
        } else if (request.getPath().contains("annotations")) {
          resource = DIRECTIONS_V5_ANNOTATIONS_FIXTURE;
        } else if (request.getPath().contains("waypoint_names")) {
          resource = DIRECTIONS_V5_WAYPOINT_NAMES_FIXTURE;
        } else if (request.getPath().contains("waypoint_targets")) {
            resource = DIRECTIONS_V5_WAYPOINT_TARGETS_FIXTURE;
        }else if (request.getPath().contains("approaches")) {
          resource = DIRECTIONS_V5_APPROACHES_REQUEST;
        } else if (request.getPath().contains("-151.2302")) {
          resource = DIRECTIONS_V5_NO_ROUTE;
        } else if (request.getPath().contains("-122.403561,37.777689")) {
          resource = DIRECTIONS_V5_BANNER_INSTRUCTIONS;
        } else if (request.getPath().contains("driving-traffic")) {
          resource = DIRECTIONS_TRAFFIC_FIXTURE;
        } else if (request.getPath().contains("geometries=polyline6")) {
          resource = DIRECTIONS_V5_PRECISION6_FIXTURE;
        } else if (request.getMethod().equals("POST")) {
          resource = DIRECTIONS_V5_POST;
        }

        try {
          String body = loadJsonFixture(resource);
          return new MockResponse().setBody(body);
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }
      }
    });
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
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken(ACCESS_TOKEN)
      .origin(Point.fromLngLat(1.0, 1.0))
      .destination(Point.fromLngLat(2.0, 2.0))
      .build();
    Assert.assertNotNull(mapboxDirections);
  }

  @Test
  public void build_noAccessTokenExceptionThrown() throws Exception {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Missing required properties: accessToken");
    MapboxDirections.builder()
      .origin(Point.fromLngLat(1.0, 1.0))
      .destination(Point.fromLngLat(2.0, 2.0))
      .build();
  }

  @Test
  public void build_invalidAccessTokenExceptionThrown() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(StringStartsWith.startsWith("Using Mapbox Services requires setting a valid access token"));
    MapboxDirections.builder()
      .accessToken("")
      .origin(Point.fromLngLat(1.0, 1.0))
      .destination(Point.fromLngLat(2.0, 2.0))
      .build();
  }

  @Test
  public void build_noCoordinatesHaveBeenProvided() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("An origin and destination are required before making the");
    MapboxDirections.builder().accessToken(ACCESS_TOKEN).build().executeCall();
  }

  @Test
  public void build_coordinatesListCreatedInCorrectOrder() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .origin(Point.fromLngLat(1.234, 2.345))
      .destination(Point.fromLngLat(13.4930, 9.958))
      .addWaypoint(Point.fromLngLat(5.29838, 4.42189))
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString()
      .contains("1.234,2.345;5.29838,4.42189;13.493,9.958"));
  }

  @Test
  public void build_originDestinationGetAddedToListCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString()
      .contains("1.234,2.345;13.493,9.958"));
  }

  @Test
  public void user_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .user("foobar")
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("/foobar/"));
  }

  @Test
  public void profile_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .profile(DirectionsCriteria.PROFILE_CYCLING)
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("/cycling/"));
  }

  @Test
  public void origin_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("1.234,2.345;"));
  }

  @Test
  public void destination_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString()
      .contains("1.234,2.345;13.493,9.958"));
  }

  @Test
  public void addWaypoint_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .addWaypoint(Point.fromLngLat(90.01, 50.23))
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString()
      .contains("1.234,2.345;90.01,50.23;13.493,9.958"));
  }

  @Test
  public void waypoints_doesGetFormattedInUrlCorrectly() {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .addWaypoint(Point.fromLngLat(4.56, 7.89))
      .origin(Point.fromLngLat(1.234, 2.345))
      .addWaypointIndices(0, 2)
      .accessToken(ACCESS_TOKEN)
      .build();
    String semicolon = "%3B";
    assertTrue(directions.cloneCall().request().url().toString().contains("waypoints=0" + semicolon + "2"));
  }

  @Test
  public void alternatives_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .alternatives(true)
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("alternatives=true"));
  }

  @Test
  public void geometries_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("geometries=polyline6"));
  }

  @Test
  public void overview_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .overview(DirectionsCriteria.OVERVIEW_FULL)
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("overview=full"));
  }

  @Test
  public void steps_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .steps(true)
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("steps=true"));
  }

  @Test
  public void continueStraight_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .continueStraight(true)
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString()
      .contains("continue_straight=true"));
  }

  @Test
  public void language_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .language(Locale.FRANCE)
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("language=fr"));
  }

  @Test
  public void annotations_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .annotations(DirectionsCriteria.ANNOTATION_CONGESTION, DirectionsCriteria.ANNOTATION_DURATION)
      .overview(DirectionsCriteria.OVERVIEW_FULL)
      .accessToken(ACCESS_TOKEN)
      .build();
    assertEquals("congestion,duration",
      directions.cloneCall().request().url().queryParameter("annotations"));
  }

  @Test
  public void addBearing_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .addBearing(45d, 90d)
      .addBearing(2d, 90d)
      .accessToken(ACCESS_TOKEN)
      .build();
    assertEquals("45,90;2,90",
      directions.cloneCall().request().url().queryParameter("bearings"));
  }

  @Test
  public void radiuses_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .radiuses(23, 30)
      .accessToken(ACCESS_TOKEN)
      .build();
    assertEquals("23;30",
      directions.cloneCall().request().url().queryParameter("radiuses"));
  }

  @Test
  public void clientAppName_doesGetAddedToRequestHeader1() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .accessToken(ACCESS_TOKEN)
      .clientAppName("APP")
      .build();
    assertTrue(directions.cloneCall().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void clientAppName_doesGetAddedToRequestHeader2() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .accessToken(ACCESS_TOKEN)
      .clientAppName("APP")
      .build();
    assertTrue(directions.executeCall().raw().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void accessToken_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .accessToken(ACCESS_TOKEN)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("access_token=pk.XXX"));
  }

  @Test
  public void baseUrl_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .accessToken(ACCESS_TOKEN)
      .baseUrl("https://foobar.com")
      .build();
    assertTrue(directions.cloneCall().request().url().toString().startsWith("https://foobar.com"));
  }

  @Test
  public void voiceUnits_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .accessToken(ACCESS_TOKEN)
      .baseUrl("https://foobar.com")
      .voiceUnits(DirectionsCriteria.METRIC)
      .build();

    assertThat(directions.cloneCall().request().url().toString(),
      containsString("voice_units=metric"));
  }

  @Test
  public void bannerInstructions_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .accessToken(ACCESS_TOKEN)
      .baseUrl("https://foobar.com")
      .bannerInstructions(true)
      .build();

    assertThat(directions.cloneCall().request().url().toString(),
      containsString("banner_instructions=true"));
  }

  @Test
  public void voiceInstructions_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .accessToken(ACCESS_TOKEN)
      .baseUrl("https://foobar.com")
      .voiceInstructions(true)
      .build();

    assertThat(directions.cloneCall().request().url().toString(),
      containsString("voice_instructions=true"));
  }

  @Test
  public void exclude_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .destination(Point.fromLngLat(13.4930, 9.958))
      .origin(Point.fromLngLat(1.234, 2.345))
      .accessToken(ACCESS_TOKEN)
      .baseUrl("https://foobar.com")
      .exclude(DirectionsCriteria.EXCLUDE_MOTORWAY)
      .build();

    assertThat(directions.cloneCall().request().url().toString(),
      containsString("exclude=motorway"));
  }

  @Test
  public void callFactoryNonNull() throws IOException {
    MapboxDirections client = MapboxDirections.builder()
      .accessToken(ACCESS_TOKEN)
      .origin(Point.fromLngLat(1.0, 2.0))
      .destination(Point.fromLngLat(5.0, 6.0))
      .baseUrl(mockUrl.toString())
      .build();

    // Setting a null call factory doesn't make the request fail
    // (the default OkHttp client is used)
    client.setCallFactory(null);
    Response<DirectionsResponse> response = client.executeCall();
    assertEquals(200, response.code());
    assertEquals("Ok", response.body().code());
  }

  @Test
  public void testRadiusWithUnlimitedDistance() throws IOException {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(13.4301, 52.5109));
    coordinates.add(Point.fromLngLat(13.4265, 52.5080));
    coordinates.add(Point.fromLngLat(13.4316, 52.5021));

    MapboxDirections client = MapboxDirections.builder()
      .accessToken(ACCESS_TOKEN)
      .origin(coordinates.get(0))
      .addWaypoint(coordinates.get(1))
      .destination(coordinates.get(2))
      .baseUrl("https://foobar.com")
      .radiuses(100, Double.POSITIVE_INFINITY, 100)
      .build();

    assertEquals("100;unlimited;100",
      client.cloneCall().request().url().queryParameter("radiuses"));
  }

  @Test
  public void noValidRouteTest() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .origin(Point.fromLngLat(-151.2302, -33.9283))
      .destination(Point.fromLngLat(-174.7654, -36.8641))
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .steps(true)
      .build();

    Response<DirectionsResponse> response = mapboxDirections.executeCall();
    assertThat(response.body().message(), containsString("No route found"));
    assertThat(response.body().code(), containsString("NoRoute"));
  }

  @Test
  public void setCoordinates_localeShouldNotMatter() {
    Locale.setDefault(Locale.GERMANY);
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken(ACCESS_TOKEN)
      .origin(Point.fromLngLat(1.234,2.345))
      .addWaypoint(Point.fromLngLat(13.493,9.958))
      .destination(Point.fromLngLat(5.29838,4.42189))
      .build();
    assertThat(directions.cloneCall().request().url().toString(),
      containsString("1.234,2.345;13.493,9.958;5.29838,4.42189"));
  }

  @Test
  public void maxSpeedAnnotation_doesGetFormattedInUrlCorrectly() {
    Locale.setDefault(Locale.GERMANY);
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken(ACCESS_TOKEN)
      .origin(Point.fromLngLat(1.234,2.345))
      .addWaypoint(Point.fromLngLat(13.493,9.958))
      .destination(Point.fromLngLat(5.29838,4.42189))
      .annotations(DirectionsCriteria.ANNOTATION_MAXSPEED)
      .build();

    assertThat(directions.cloneCall().request().url().toString(),
      containsString("annotations=maxspeed"));
  }

  @Test
  public void maxSpeedAnnotation_doesGetCreatedInResponse() throws IOException {
    Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(DirectionsAdapterFactory.create()).create();
    String body = loadJsonFixture(DIRECTIONS_V5_MAX_SPEED_ANNOTATION);
    DirectionsResponse response = gson.fromJson(body, DirectionsResponse.class);
    DirectionsRoute maxSpeedRoute = response.routes().get(0);
    LegAnnotation maxSpeedAnnotation = maxSpeedRoute.legs().get(0).annotation();

    assertNotNull(maxSpeedAnnotation.maxspeed());
  }

  @Test
  public void subBannerInstructions() throws Exception {
    Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(DirectionsAdapterFactory.create()).create();
    String body = loadJsonFixture(DIRECTIONS_V5_BANNER_INSTRUCTIONS);
    DirectionsResponse response = gson.fromJson(body, DirectionsResponse.class);

    BannerInstructions bannerInstructions =
      response.routes().get(0).legs().get(0).steps().get(0).bannerInstructions().get(1);

    assertNotNull(bannerInstructions.sub());
    assertNotNull(bannerInstructions.sub().components());

    BannerComponents component = bannerInstructions.sub().components().get(1);
    assertNotNull(component.active());
    assertNotNull(component.directions());
    assertEquals(2, component.directions().size());
  }

  @Test
  public void subBannerInstructionsFromJson() throws Exception {

    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .origin(Point.fromLngLat(-122.403561,37.777689))
      .destination(Point.fromLngLat(-122.405786,37.770369))
      .accessToken(ACCESS_TOKEN)
      .profile(PROFILE_DRIVING)
      .steps(true)
      .geometries(GEOMETRY_POLYLINE)
      .bannerInstructions(true)
      .baseUrl(mockUrl.toString())
      .build();

    Response<DirectionsResponse> response = mapboxDirections.executeCall();

    BannerInstructions bannerInstructions =
      response.body().routes().get(0).legs().get(0).steps().get(0).bannerInstructions().get(1);

    assertNotNull(bannerInstructions.sub());
    assertNotNull(bannerInstructions.sub().components());

    BannerComponents component = bannerInstructions.sub().components().get(1);
    assertNotNull(component.active());
    assertNotNull(component.directions());
    assertEquals(2, component.directions().size());
  }

  @Test
  public void sanityApproachesInstructions() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .origin(Point.fromLngLat(1.0, 1.0))
      .addWaypoint(Point.fromLngLat(2.0, 2.0))
      .addWaypoint(Point.fromLngLat(3.0, 3.0))
      .destination(Point.fromLngLat(4.0, 4.0))
      .addApproaches(APPROACH_UNRESTRICTED, null, "", APPROACH_CURB)
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(mapboxDirections);

    assertEquals("unrestricted;;;curb",
      mapboxDirections.cloneCall().request().url().queryParameter("approaches"));
  }

  @Test
  public void build_exceptionThrownWhenNumApproachesDoesNotMatchCoordinates() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("Number of approach elements must match"));
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .origin(Point.fromLngLat(2.0, 2.0))
      .destination(Point.fromLngLat(4.0, 4.0))
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
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .origin(Point.fromLngLat(2.0, 2.0))
      .destination(Point.fromLngLat(4.0, 4.0))
      .addApproaches(APPROACH_UNRESTRICTED, "restricted")
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
  }

  @Test
  public void testApproaches() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .profile(PROFILE_DRIVING)
      .origin(Point.fromLngLat(13.4301,52.5109))
      .destination(Point.fromLngLat(13.432508,52.501725))
      .addApproaches(APPROACH_UNRESTRICTED, APPROACH_CURB)
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .build();

    mapboxDirections.setCallFactory(null);
    Response<DirectionsResponse> response = mapboxDirections.executeCall();
    assertEquals(200, response.code());
    assertEquals("Ok", response.body().code());
  }

  @Test
  public void testRouteOptionsApproaches() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .profile(PROFILE_DRIVING)
      .origin(Point.fromLngLat(13.4301,52.5109))
      .destination(Point.fromLngLat(13.432508,52.501725))
      .addApproaches(APPROACH_UNRESTRICTED, APPROACH_CURB)
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .build();

    mapboxDirections.setCallFactory(null);
    Response<DirectionsResponse> response = mapboxDirections.executeCall();
    RouteOptions routeOptions = response.body().routes().get(0).routeOptions();


    String approaches = routeOptions.approaches();
    assertEquals("unrestricted;curb", approaches);
  }

  @Test(expected = ServicesException.class)
  public void build_exceptionThrownWhenLessThanTwoWaypointsProvided() {
    MapboxDirections.builder()
            .origin(Point.fromLngLat(2.0, 2.0))
            .destination(Point.fromLngLat(4.0, 4.0))
            .addWaypointIndices(0)
            .baseUrl("https://foobar.com")
            .accessToken(ACCESS_TOKEN)
            .build();
  }

  @Test(expected = ServicesException.class)
  public void build_exceptionThrownWhenWaypointsDoNotStartWith0() {
    MapboxDirections.builder()
            .origin(Point.fromLngLat(2.0, 2.0))
            .addWaypoint(Point.fromLngLat(3.0, 3.0))
            .destination(Point.fromLngLat(4.0, 4.0))
            .addWaypointIndices(1, 2)
            .baseUrl("https://foobar.com")
            .accessToken(ACCESS_TOKEN)
            .build();
  }

  @Test(expected = ServicesException.class)
  public void build_exceptionThrownWhenWaypointDoNotEndWithLast() {
    MapboxDirections.builder()
            .origin(Point.fromLngLat(2.0, 2.0))
            .addWaypoint(Point.fromLngLat(3.0, 3.0))
            .destination(Point.fromLngLat(4.0, 4.0))
            .addWaypointIndices(0, 1)
            .baseUrl("https://foobar.com")
            .accessToken(ACCESS_TOKEN)
            .build();
  }

  @Test(expected = ServicesException.class)
  public void build_exceptionThrownWhenMiddleWaypointsAreWrong() {
    MapboxDirections.builder()
            .origin(Point.fromLngLat(2.0, 2.0))
            .addWaypoint(Point.fromLngLat(3.0, 3.0))
            .destination(Point.fromLngLat(4.0, 4.0))
            .addWaypointIndices(0, 3, 2)
            .baseUrl("https://foobar.com")
            .accessToken(ACCESS_TOKEN)
            .build();
  }

  @Test
  public void sanityWaypointNamesInstructions() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .origin(Point.fromLngLat(1.0, 1.0))
      .addWaypoint(Point.fromLngLat(2.0, 2.0))
      .destination(Point.fromLngLat(4.0, 4.0))
      .addWaypointNames("Home", "Store", "Work")
      .baseUrl("https://foobar.com")
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(mapboxDirections);
    assertEquals("Home;Store;Work",
      mapboxDirections.cloneCall().request().url().queryParameter("waypoint_names"));
  }

  @Test
  public void testWithWaypointNames() throws Exception {

    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .profile(PROFILE_CYCLING)
      .origin(Point.fromLngLat(-122.42,37.78))
      .destination(Point.fromLngLat(-77.03,38.91))
      .steps(true)
      .voiceInstructions(true)
      .voiceUnits(DirectionsCriteria.IMPERIAL)
      .addWaypointNames("Home", "Work")
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .build();

    mapboxDirections.setCallFactory(null);
    Response<DirectionsResponse> response = mapboxDirections.executeCall();
    assertEquals(200, response.code());
    assertEquals("Ok", response.body().code());
  }

  @Test
  public void testWithWaypointTargets() throws Exception {

    MapboxDirections mapboxDirections = MapboxDirections.builder()
            .profile(PROFILE_DRIVING_TRAFFIC)
            .origin(Point.fromLngLat(-6.80904429026134,62.00015328799685))
            .destination(Point.fromLngLat(-6.800065040588378,62.00012400993553))
            .steps(true)
            .addWaypointTargets(null, Point.fromLngLat(-6.799936294555664,61.99987216574813))
            .accessToken(ACCESS_TOKEN)
            .baseUrl(mockUrl.toString())
            .build();

    assertNotNull(mapboxDirections);
    assertNotNull(mapboxDirections.cloneCall().request().url().queryParameter("waypoint_targets"));

    Response<DirectionsResponse> response = mapboxDirections.executeCall();
    assertEquals(200, response.code());
    assertEquals("Ok", response.body().code());
    assertEquals("left",
            response.body().routes().get(0).legs().get(0).steps().get(0).maneuver().modifier());

  }

  @Test
  public void testWithInterceptor() throws Exception {
    Interceptor interceptor = new Interceptor() {
      @Override
      public okhttp3.Response intercept(Chain chain) throws IOException {
        return null;
      }
    };
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .profile(PROFILE_CYCLING)
      .origin(Point.fromLngLat(-122.42,37.78))
      .destination(Point.fromLngLat(-77.03,38.91))
      .steps(true)
      .voiceInstructions(true)
      .voiceUnits(DirectionsCriteria.IMPERIAL)
      .addWaypointNames("Home", "Work")
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .interceptor(interceptor)
      .build();

    assertEquals(interceptor, mapboxDirections.interceptor());
  }

  @Test
  public void testWithEventListener() throws Exception {
    EventListener eventListener = new EventListener() {
      @Override
      public void callStart(Call call) {
        super.callStart(call);
      }
    };
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .profile(PROFILE_CYCLING)
      .origin(Point.fromLngLat(-122.42,37.78))
      .destination(Point.fromLngLat(-77.03,38.91))
      .steps(true)
      .voiceInstructions(true)
      .voiceUnits(DirectionsCriteria.IMPERIAL)
      .addWaypointNames("Home", "Work")
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .eventListener(eventListener)
      .build();


    assertEquals(eventListener, mapboxDirections.eventListener());
  }

  @Test
  public void testPost() throws IOException {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .profile(PROFILE_CYCLING)
      .origin(Point.fromLngLat(-122.42,37.78))
      .destination(Point.fromLngLat(-77.03,38.91))
      .steps(true)
      .voiceInstructions(true)
      .voiceUnits(DirectionsCriteria.IMPERIAL)
      .addWaypointNames("Home", "Work")
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .post()
      .build();

    Response<DirectionsResponse> response = mapboxDirections.executeCall();
    System.out.print(response.raw().request().url());
    assertEquals(200, response.code());
    assertEquals("Ok", response.body().code());

    assertNotNull(response.body().routes());
    assertEquals(1, response.body().routes().size());
  }
}
