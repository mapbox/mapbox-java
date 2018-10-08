package com.mapbox.api.directions.v5.models;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.core.TestUtils;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Point;

import org.hamcrest.junit.ExpectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static com.mapbox.api.directions.v5.DirectionsCriteria.ROAD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RouteOptionsTest extends TestUtils {

  private static final String DIRECTIONS_V5_FIXTURE = "directions_v5.json";

  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String body = loadJsonFixture(DIRECTIONS_V5_FIXTURE);
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
    List<Point> pointList = new ArrayList<>();
    pointList.add(Point.fromLngLat(1.0, 2.0));
    pointList.add(Point.fromLngLat(3.0, 4.0));
    RouteOptions routeOptions = RouteOptions.builder()
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .profile("hello")
      .user("user")
      .coordinates(pointList)
      .accessToken(ACCESS_TOKEN)
      .requestUuid("uuid")
      .build();
    assertNotNull(routeOptions);
    assertEquals("hello", routeOptions.profile());
  }

  @Test
  public void directionsRequestResult_doesContainTheOriginalRequestData() throws Exception {
    Response<DirectionsResponse> response = MapboxDirections.builder()
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .origin(Point.fromLngLat(1.0, 1.0))
      .destination(Point.fromLngLat(5.0, 5.0))
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .continueStraight(false)
      .language(Locale.CANADA)
      .alternatives(true).build().executeCall();
    DirectionsRoute route = response.body().routes().get(0);
    assertEquals(Locale.CANADA.getLanguage(), route.routeOptions().language());
    assertEquals(DirectionsCriteria.PROFILE_WALKING, route.routeOptions().profile());
    assertEquals(Constants.MAPBOX_USER, route.routeOptions().user());
    assertEquals(false, route.routeOptions().continueStraight());

    // Never set values
    assertNull(route.routeOptions().annotations());
    assertNull(route.routeOptions().bearings());
  }

  @Test
  public void directionsRequestResult_doesContainBaseUrl() throws Exception {
    Response<DirectionsResponse> response = MapboxDirections.builder()
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .origin(Point.fromLngLat(1.0, 1.0))
      .destination(Point.fromLngLat(5.0, 5.0))
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .continueStraight(false)
      .language(Locale.CANADA)
      .alternatives(true).build().executeCall();
    DirectionsRoute route = response.body().routes().get(0);

    assertEquals(mockUrl.toString(), route.routeOptions().baseUrl());
  }

  @Test
  public void directionsRequestResult_doesContainRoundaboutExits() throws Exception {
    Response<DirectionsResponse> response = MapboxDirections.builder()
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .origin(Point.fromLngLat(1.0, 1.0))
      .destination(Point.fromLngLat(5.0, 5.0))
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .continueStraight(false)
      .language(Locale.CANADA)
      .roundaboutExits(true)
      .alternatives(true).build().executeCall();
    DirectionsRoute route = response.body().routes().get(0);

    assertEquals(true, route.routeOptions().roundaboutExits());
  }

  @Test
  public void directionsRequestResult_doesContainSteps() throws Exception {
    Response<DirectionsResponse> response = MapboxDirections.builder()
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .origin(Point.fromLngLat(1.0, 1.0))
      .destination(Point.fromLngLat(5.0, 5.0))
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .continueStraight(false)
      .language(Locale.CANADA)
      .steps(true)
      .alternatives(true).build().executeCall();
    DirectionsRoute route = response.body().routes().get(0);

    assertEquals(true, route.routeOptions().steps());
  }

  @Test
  public void directionsRequestResult_doesContainGeometries() throws Exception {
    Response<DirectionsResponse> response = MapboxDirections.builder()
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .origin(Point.fromLngLat(1.0, 1.0))
      .destination(Point.fromLngLat(5.0, 5.0))
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .continueStraight(false)
      .language(Locale.CANADA)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE)
      .alternatives(true).build().executeCall();
    DirectionsRoute route = response.body().routes().get(0);

    assertEquals(DirectionsCriteria.GEOMETRY_POLYLINE, route.routeOptions().geometries());
  }

  @Test
  public void directionsRequestResult_doesContainOverview() throws Exception {
    Response<DirectionsResponse> response = MapboxDirections.builder()
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .origin(Point.fromLngLat(1.0, 1.0))
      .destination(Point.fromLngLat(5.0, 5.0))
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .continueStraight(false)
      .language(Locale.CANADA)
      .overview(DirectionsCriteria.OVERVIEW_SIMPLIFIED)
      .alternatives(true).build().executeCall();
    DirectionsRoute route = response.body().routes().get(0);

    assertEquals(DirectionsCriteria.OVERVIEW_SIMPLIFIED, route.routeOptions().overview());
  }

  @Test
  public void directionsRequestResult_doesContainBicycleType() throws Exception {
    Response<DirectionsResponse> response = MapboxDirections.builder()
            .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
            .baseUrl(mockUrl.toString())
            .accessToken(ACCESS_TOKEN)
            .origin(Point.fromLngLat(1.0, 1.0))
            .destination(Point.fromLngLat(5.0, 5.0))
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .continueStraight(false)
            .language(Locale.CANADA)
            .bicycleType(ROAD)
            .alternatives(true).build().executeCall();
    DirectionsRoute route = response.body().routes().get(0);

    assertEquals(ROAD, route.routeOptions().bicycleType());
  }

  @Test
  public void directionsRequestResult_doesContainCyclingSpeed() throws Exception {
    Float cyclingSpeed = 10f;
    Response<DirectionsResponse> response = MapboxDirections.builder()
            .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
            .baseUrl(mockUrl.toString())
            .accessToken(ACCESS_TOKEN)
            .origin(Point.fromLngLat(1.0, 1.0))
            .destination(Point.fromLngLat(5.0, 5.0))
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .continueStraight(false)
            .language(Locale.CANADA)
            .cyclingSpeed(cyclingSpeed)
            .alternatives(true).build().executeCall();
    DirectionsRoute route = response.body().routes().get(0);

    assertEquals(cyclingSpeed, route.routeOptions().cyclingSpeed());
  }

  @Test
  public void directionsRequestResult_doesContainUseRoads() throws Exception {
    Float useRoads = .5f;
    Response<DirectionsResponse> response = MapboxDirections.builder()
            .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
            .baseUrl(mockUrl.toString())
            .accessToken(ACCESS_TOKEN)
            .origin(Point.fromLngLat(1.0, 1.0))
            .destination(Point.fromLngLat(5.0, 5.0))
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .continueStraight(false)
            .language(Locale.CANADA)
            .useRoads(useRoads)
            .alternatives(true).build().executeCall();
    DirectionsRoute route = response.body().routes().get(0);

    assertEquals(useRoads, route.routeOptions().useRoads());
  }

  @Test
  public void directionsRequestResult_doesContainUseHills() throws Exception {
    Float useHills = .5f;
    Response<DirectionsResponse> response = MapboxDirections.builder()
            .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
            .baseUrl(mockUrl.toString())
            .accessToken(ACCESS_TOKEN)
            .origin(Point.fromLngLat(1.0, 1.0))
            .destination(Point.fromLngLat(5.0, 5.0))
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .continueStraight(false)
            .language(Locale.CANADA)
            .useHills(useHills)
            .alternatives(true).build().executeCall();
    DirectionsRoute route = response.body().routes().get(0);

    assertEquals(useHills, route.routeOptions().useHills());
  }

  @Test
  public void directionsRequestResult_doesContainUseFerry() throws Exception {
    Float useFerry = .5f;
    Response<DirectionsResponse> response = MapboxDirections.builder()
            .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
            .baseUrl(mockUrl.toString())
            .accessToken(ACCESS_TOKEN)
            .origin(Point.fromLngLat(1.0, 1.0))
            .destination(Point.fromLngLat(5.0, 5.0))
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .continueStraight(false)
            .language(Locale.CANADA)
            .useFerry(useFerry)
            .alternatives(true).build().executeCall();
    DirectionsRoute route = response.body().routes().get(0);

    assertEquals(useFerry, route.routeOptions().useFerry());
  }

  @Test
  public void directionsRequestResult_doesContainAvoidBadSurfaces() throws Exception {
    Float avoidBadSurfaces = .5f;
    Response<DirectionsResponse> response = MapboxDirections.builder()
            .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
            .baseUrl(mockUrl.toString())
            .accessToken(ACCESS_TOKEN)
            .origin(Point.fromLngLat(1.0, 1.0))
            .destination(Point.fromLngLat(5.0, 5.0))
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .continueStraight(false)
            .language(Locale.CANADA)
            .avoidBadSurfaces(avoidBadSurfaces)
            .alternatives(true).build().executeCall();
    DirectionsRoute route = response.body().routes().get(0);

    assertEquals(avoidBadSurfaces, route.routeOptions().avoidBadSurfaces());
  }

  @Test
  public void toJson_fromJson() throws Exception {
    Response<DirectionsResponse> response = MapboxDirections.builder()
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .origin(Point.fromLngLat(1.0, 1.0))
      .destination(Point.fromLngLat(5.0, 5.0))
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .continueStraight(false)
      .language(Locale.CANADA)
      .overview(DirectionsCriteria.OVERVIEW_SIMPLIFIED)
      .alternatives(true)
      .build()
      .executeCall();

    RouteOptions routeOptions = response.body().routes().get(0).routeOptions();

    String jsonString = routeOptions.toJson();
    RouteOptions routeOptionsFromJson = RouteOptions.fromJson(jsonString);

    assertEquals(routeOptions, routeOptionsFromJson);
  }

  @Test
  public void fromJson() {
    String jsonString = "{" +
      "\"profile\": \"auto\"," +
      "\"user\": \"mapbox\"," +
      "\"baseUrl\": \"https://api.mapbox.com\"," +
      "\"coordinates\": [[-3.707788,40.395039],[-3.712179,40.401819]]," +
      "\"access_token\": \"ACCESS_TOKEN\"," +
      "\"geometries\": \"polyline6\"," +
      "\"overview\": \"full\"," +
      "\"steps\": true," +
      "\"bearings\": \";\"," +
      "\"continue_straight\": true," +
      "\"annotations\": \"congestion,distance\"," +
      "\"language\": \"en\"," +
      "\"roundabout_exits\": true," +
      "\"voice_instructions\": true," +
      "\"banner_instructions\": true," +
      "\"voice_units\": \"imperial\"," +
      "\"uuid\": \"uuid1\"" +
      "}";

    RouteOptions routeOptions = RouteOptions.fromJson(jsonString);

    assertEquals("auto", routeOptions.profile());
    assertEquals("mapbox", routeOptions.user());
    assertEquals("https://api.mapbox.com", routeOptions.baseUrl());
    assertEquals(2, routeOptions.coordinates().size());
    assertEquals("ACCESS_TOKEN", routeOptions.accessToken());
    assertEquals("polyline6", routeOptions.geometries());
    assertEquals("full", routeOptions.overview());
    assertEquals(true, routeOptions.steps());
    assertEquals(";", routeOptions.bearings());
    assertEquals(true, routeOptions.continueStraight());
    assertEquals("congestion,distance", routeOptions.annotations());
    assertEquals("en", routeOptions.language());
    assertEquals(true, routeOptions.roundaboutExits());
    assertEquals(true, routeOptions.voiceInstructions());
    assertEquals(true, routeOptions.bannerInstructions());
    assertEquals("imperial", routeOptions.voiceUnits());
    assertEquals("uuid1", routeOptions.requestUuid());
  }

  @Test
  public void toJson() {
    RouteOptions routeOptions = RouteOptions.builder()
      .profile("auto")
      .user("mapbox")
      .coordinates(Arrays.asList(Point.fromLngLat(-3.707788, 40.395039),
        Point.fromLngLat(-3.712179, 40.401819)))
      .accessToken("ACCESS_TOKEN")
      .baseUrl("https://api.mapbox.com")
      .geometries("polyline6")
      .overview("full")
      .steps(true)
      .bearings(";")
      .continueStraight(true)
      .annotations("congestion,distance")
      .language("en")
      .roundaboutExits(true)
      .voiceInstructions(true)
      .bannerInstructions(true)
      .voiceUnits("imperial")
      .requestUuid("uuid1")
      .build();

    String jsonString = routeOptions.toJson();

    String expectedJsonString = "{" +
      "\"profile\": \"auto\"," +
      "\"user\": \"mapbox\"," +
      "\"baseUrl\": \"https://api.mapbox.com\"," +
      "\"coordinates\": [[-3.707788,40.395039],[-3.712179,40.401819]]," +
      "\"access_token\": \"ACCESS_TOKEN\"," +
      "\"geometries\": \"polyline6\"," +
      "\"overview\": \"full\"," +
      "\"steps\": true," +
      "\"bearings\": \";\"," +
      "\"continue_straight\": true," +
      "\"annotations\": \"congestion,distance\"," +
      "\"language\": \"en\"," +
      "\"roundabout_exits\": true," +
      "\"voice_instructions\": true," +
      "\"banner_instructions\": true," +
      "\"voice_units\": \"imperial\"," +
      "\"uuid\": \"uuid1\"}";
    compareJson(expectedJsonString, jsonString);
  }
}
