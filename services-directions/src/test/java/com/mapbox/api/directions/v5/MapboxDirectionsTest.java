package com.mapbox.api.directions.v5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.api.directions.v5.models.BannerComponents;
import com.mapbox.api.directions.v5.models.BannerInstructions;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.Incident;
import com.mapbox.api.directions.v5.models.LegAnnotation;
import com.mapbox.api.directions.v5.models.Metadata;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.core.MapboxService;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Callback;
import retrofit2.Response;

public class MapboxDirectionsTest extends TestUtils {

  private static final String DIRECTIONS_V5_FIXTURE = "directions_v5.json";
  private static final String DIRECTIONS_ALTERNATIVES_V5_FIXTURE =
    "directions_alternatives_v5.json";
  private static final String DIRECTIONS_V5_PRECISION6_FIXTURE = "directions_v5_precision_6.json";
  private static final String DIRECTIONS_TRAFFIC_FIXTURE = "directions_v5_traffic.json";
  private static final String DIRECTIONS_ROTARY_FIXTURE = "directions_v5_fixtures_rotary.json";
  private static final String DIRECTIONS_V5_ANNOTATIONS_FIXTURE = "directions_annotations_v5.json";
  private static final String DIRECTIONS_V5_NO_ROUTE = "directions_v5_no_route.json";
  private static final String DIRECTIONS_V5_SPEED_LIMIT = "directions_v5_speedlimit.json";
  private static final String DIRECTIONS_V5_MAX_SPEED_ANNOTATION =
    "directions_v5_max_speed_annotation.json";
  private static final String DIRECTIONS_V5_BANNER_INSTRUCTIONS =
    "directions_v5_banner_instructions.json";
  private static final String DIRECTIONS_V5_APPROACHES_REQUEST = "directions_v5_approaches.json";
  private static final String DIRECTIONS_V5_WAYPOINT_NAMES_FIXTURE =
    "directions_v5_waypoint_names.json";
  private static final String DIRECTIONS_V5_WAYPOINT_TARGETS_FIXTURE =
    "directions_v5_waypoint_targets.json";
  private static final String DIRECTIONS_V5_POST = "directions_v5_post.json";
  private static final String ROUTE_OPTIONS_V5 = "route_options_v5.json";
  private static final String ROUTE_OPTIONS_ALTERNATIVES_V5 = "route_options_alternatives_v5.json";

  private final TestDispatcher testDispatcher = new TestDispatcher();
  private MockWebServer server;
  private HttpUrl mockUrl;

  private final RouteOptions routeOptions =
    RouteOptions.fromJson(loadJsonFixture(ROUTE_OPTIONS_V5));

  public MapboxDirectionsTest() throws IOException {
  }

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    testDispatcher.currentResource = DIRECTIONS_V5_FIXTURE;
    server.setDispatcher(testDispatcher);
    server.start();
    mockUrl = server.url("");
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  public void testRouteOptionsFromUrl() throws IOException {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertEquals(
      routeOptions,
      RouteOptions.fromUrl(new URL(directions.cloneCall().request().url().toString()))
    );
  }

  @Test
  public void testRouteOptionsFromUrl_alreadyDecoded() throws IOException {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertEquals(
      routeOptions,
      RouteOptions.fromUrl(
        new URL(URLDecoder.decode(directions.cloneCall().request().url().toString(), "UTF-8"))
      )
    );
  }

  @Test
  public void sanity() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertNotNull(mapboxDirections);
  }


  @Test
  public void build_coordinatesListCreatedInCorrectOrder() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString()
      .contains("-122.4003312,37.7736941;-122.4187529,37.7689715;-122.4255172,37.7775835"));
  }

  @Test
  public void build_walkingWalkingSpeedOptions() {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("walking_speed=5.11"));
  }

  @Test
  public void build_metadataOptions() {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("metadata=true"));
  }

  @Test
  public void build_walkingWalkwayBiasOptions() {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("walkway_bias=-0.2"));
  }

  @Test
  public void build_walkingAlleyBiasOptions() {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("alley_bias=0.75"));
  }

  @Test
  public void user_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("/mapbox/"));
  }

  @Test
  public void profile_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("/driving/"));
  }

  @Test
  public void waypointList_doesGetFormattedInUrlCorrectly() {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();

    assertEquals("0;1;2", directions.cloneCall().request().url().queryParameter("waypoints"));
  }

  @Test
  public void alternatives_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("alternatives=false"));
  }

  @Test
  public void geometries_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("geometries=polyline6"));
  }

  @Test
  public void overview_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("overview=full"));
  }

  @Test
  public void steps_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("steps=true"));
  }

  @Test
  public void continueStraight_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString()
      .contains("continue_straight=false"));
  }

  @Test
  public void language_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("language=ru"));
  }

  @Test
  public void annotations_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertEquals("congestion,distance,duration",
      directions.cloneCall().request().url().queryParameter("annotations"));
  }

  @Test
  public void addBearing_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertEquals("0,90;90,0;",
      directions.cloneCall().request().url().queryParameter("bearings"));
  }

  @Test
  public void radiuses_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertEquals(";unlimited;5.1",
      directions.cloneCall().request().url().queryParameter("radiuses"));
  }

  @Test
  public void clientAppName_doesGetAddedToRequestHeader1() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .clientAppName("APP")
      .build();
    assertTrue(directions.cloneCall().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void clientAppName_doesGetAddedToRequestHeader2() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .clientAppName("APP")
      .build();
    assertTrue(directions.executeCall().raw().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void accessToken_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(directions.cloneCall().request().url().toString().contains("access_token=token"));
  }

  @Test
  public void baseUrl_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertTrue(
      directions.cloneCall().request().url().toString().startsWith("https://api.mapbox.com"));
  }

  @Test
  public void voiceUnits_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();

    assertTrue(directions.cloneCall().request().url().toString().contains("voice_units=metric"));
  }

  @Test
  public void bannerInstructions_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();

    assertTrue(
      directions.cloneCall().request().url().toString().contains("banner_instructions=true"));
  }

  @Test
  public void voiceInstructions_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();

    assertTrue(
      directions.cloneCall().request().url().toString().contains("voice_instructions=true"));
  }

  @Test
  public void exclude_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();

    assertTrue(directions.cloneCall().request().url().toString().contains("exclude=toll"));
  }

  @Test
  public void include_doesGetFormattedInUrlCorrectly() throws Exception {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();

    assertTrue(directions.cloneCall().request().url().toString().contains("include=hot"));
  }

  @Test
  public void callFactoryNonNull() throws IOException {
    MapboxDirections client = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .build();

    // Setting a null call factory doesn't make the request fail
    // (the default OkHttp client is used)
    client.setCallFactory(null);
    Response<DirectionsResponse> response = client.executeCall();
    assertEquals(200, response.code());
    assertEquals("Ok", response.body().code());
  }

  @Test
  public void radiusWithUnlimitedDistance() throws IOException {
    MapboxDirections client = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .build();

    assertEquals(";unlimited;5.1",
      client.cloneCall().request().url().queryParameter("radiuses"));
  }

  @Test
  public void noValidRouteTest() throws Exception {
    testDispatcher.currentResource = DIRECTIONS_V5_NO_ROUTE;
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .build();

    Response<DirectionsResponse> response = mapboxDirections.executeCall();
    assertTrue(response.body().message().contains("No route found"));
    assertTrue(response.body().code().contains("NoRoute"));
  }

  @Test
  public void setCoordinates_localeShouldNotMatter() {
    Locale.setDefault(Locale.GERMANY);
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .build();
    assertTrue(directions.cloneCall().request().url().toString()
      .contains("-122.4003312,37.7736941;-122.4187529,37.7689715;-122.4255172,37.7775835"));
  }

  @Test
  public void congestionAnnotation_doesGetCreatedInResponse() throws IOException {
    Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(DirectionsAdapterFactory.create()).create();
    String body = loadJsonFixture(DIRECTIONS_V5_MAX_SPEED_ANNOTATION);
    DirectionsResponse response = gson.fromJson(body, DirectionsResponse.class);
    DirectionsRoute maxSpeedRoute = response.routes().get(0);
    LegAnnotation maxSpeedAnnotation = maxSpeedRoute.legs().get(0).annotation();

    assertNotNull(maxSpeedAnnotation.maxspeed());
  }

  @Test
  public void speedLimit_doesUnitGetCreatedInResponse() throws IOException {
    Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(DirectionsAdapterFactory.create()).create();
    String body = loadJsonFixture(DIRECTIONS_V5_SPEED_LIMIT);
    DirectionsResponse response = gson.fromJson(body, DirectionsResponse.class);
    DirectionsRoute speedLimitRoute = response.routes().get(0);
    String speedLimitUnit = speedLimitRoute.legs().get(0).steps().get(0).speedLimitUnit();

    assertEquals("mph", speedLimitUnit);
  }

  @Test
  public void speedLimit_doesSignGetCreatedInResponse() throws IOException {
    Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(DirectionsAdapterFactory.create()).create();
    String body = loadJsonFixture(DIRECTIONS_V5_SPEED_LIMIT);
    DirectionsResponse response = gson.fromJson(body, DirectionsResponse.class);
    DirectionsRoute speedLimitRoute = response.routes().get(0);
    String speedLimitSign = speedLimitRoute.legs().get(0).steps().get(0).speedLimitSign();

    assertEquals("mutcd", speedLimitSign);
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
    testDispatcher.currentResource = DIRECTIONS_V5_BANNER_INSTRUCTIONS;

    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
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
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();

    assertEquals(";curb;",
      mapboxDirections.cloneCall().request().url().queryParameter("approaches"));
  }

  @Test
  public void sanityWaypointNamesInstructions() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();
    assertNotNull(mapboxDirections);
    assertEquals(";two;",
      mapboxDirections.cloneCall().request().url().queryParameter("waypoint_names"));
  }

  @Test
  public void withWaypointTargets() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions)
      .build();

    assertNotNull(mapboxDirections);
    assertEquals(
      ";12.2,21.2;",
      mapboxDirections.cloneCall().request().url().queryParameter("waypoint_targets")
    );
  }

  @Test
  public void withInterceptor() throws Exception {
    Interceptor interceptor = new Interceptor() {
      @Override
      public okhttp3.Response intercept(Chain chain) throws IOException {
        return null;
      }
    };
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .interceptor(interceptor)
      .build();

    assertEquals(interceptor, mapboxDirections.interceptor());
  }

  @Test
  public void withNetworkInterceptor() throws Exception {
    Interceptor interceptor = new Interceptor() {
      @Override
      public okhttp3.Response intercept(Chain chain) throws IOException {
        return null;
      }
    };

    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .networkInterceptor(interceptor)
      .build();

    assertEquals(interceptor, mapboxDirections.networkInterceptor());
  }

  @Test
  public void withEventListener() throws Exception {
    EventListener eventListener = new EventListener() {
      @Override
      public void callStart(Call call) {
        super.callStart(call);
      }
    };

    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .eventListener(eventListener)
      .build();

    assertEquals(eventListener, mapboxDirections.eventListener());
  }

  @Test
  public void post() throws IOException {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .usePostMethod(true)
      .build();

    Response<DirectionsResponse> response = mapboxDirections.executeCall();
    assertEquals(200, response.code());
    assertEquals("Ok", response.body().code());

    assertNotNull(response.body().routes());
    assertEquals(1, response.body().routes().size());
  }

  @Test
  public void callForUrlLength_longUrl() {
    Random random = new Random();
    List<Point> fakeCoordinates = new ArrayList<>();
    // dividing by 5 to account for 4 digits and a comma
    for (int i = 0; i < MapboxService.MAX_URL_SIZE / 5; i++) {
      fakeCoordinates.add(Point.fromLngLat(random.nextInt(50), random.nextInt(50)));
    }
    MapboxDirections.Builder builder = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(
        RouteOptions.builder()
          .baseUrl(mockUrl.toString())
          .coordinatesList(fakeCoordinates)
          .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
          .build()
      );

    retrofit2.Call<DirectionsResponse> call = builder.build().initializeCall();

    assertEquals("POST", call.request().method());
  }

  @Test
  public void callForUrlLength_shortUrl() {
    MapboxDirections.Builder builder = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build());

    retrofit2.Call<DirectionsResponse> call = builder.build().initializeCall();

    assertEquals("GET", call.request().method());
  }

  @Test
  public void postIsUsed() {
    MapboxDirections.Builder builder = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .usePostMethod(true);

    retrofit2.Call<DirectionsResponse> call = builder.build().initializeCall();

    assertEquals("POST", call.request().method());
  }

  @Test
  public void getIsUsed() {
    MapboxDirections.Builder builder = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .usePostMethod(false);

    retrofit2.Call<DirectionsResponse> call = builder.build().initializeCall();

    assertEquals("GET", call.request().method());
  }

  @Test
  public void getIsUsedByDefault() {
    MapboxDirections.Builder builder = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build());

    retrofit2.Call<DirectionsResponse> call = builder.build().initializeCall();

    assertEquals("GET", call.request().method());
  }

  @Test
  public void withIncidents() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .build();

    Response<DirectionsResponse> response = mapboxDirections.executeCall();
    assertEquals(200, response.code());
    assertEquals("Ok", response.body().code());
    assertEquals(1, response.body().routes().size());

    List<Incident> incidents = response.body().routes().get(0).legs().get(0).incidents();
    assertEquals(1, incidents.size());

    List<Integer> alertcCodes = new ArrayList<>();
    alertcCodes.add(501);
    alertcCodes.add(803);

    Incident incident = incidents.get(0);
    assertEquals("15985415522454461962", incident.id());
    assertEquals("construction", incident.type());
    assertEquals(true, incident.closed());
    assertEquals(55, incident.congestion().value());
    assertEquals("Zwischen Eching", incident.description());
    assertEquals("Zwischen Eching und Oberpfaffenhofen", incident.longDescription());
    assertEquals("minor", incident.impact());
    assertEquals("CONSTRUCTION", incident.subType());
    assertEquals("construction description", incident.subTypeDescription());
    assertEquals(alertcCodes, incident.alertcCodes());
    assertEquals(805, (int) incident.geometryIndexStart());
    assertEquals(896, (int) incident.geometryIndexEnd());
    assertEquals("2020-10-08T11:34:14Z", incident.creationTime());
    assertEquals("2020-10-06T12:52:02Z", incident.startTime());
    assertEquals("2020-11-27T16:00:00Z", incident.endTime());
  }

  @Test
  public void snappingIncludeClosures() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .build();

    assertEquals(";false;true",
      mapboxDirections.cloneCall().request().url().queryParameter("snapping_include_closures"));
  }

  @Test
  public void arrive_by() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .build();

    assertEquals("2021-01-01'T'01:01",
      mapboxDirections.cloneCall().request().url().queryParameter("arrive_by"));
  }

  @Test
  public void depart_at() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .build();

    assertEquals("2021-02-02'T'02:02",
      mapboxDirections.cloneCall().request().url().queryParameter("depart_at"));
  }

  @Test
  public void max_height() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .build();

    assertEquals("1.5",
      mapboxDirections.cloneCall().request().url().queryParameter("max_height"));
  }

  @Test
  public void max_width() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .build();

    assertEquals("1.4",
      mapboxDirections.cloneCall().request().url().queryParameter("max_width"));
  }

  @Test
  public void enable_refresh() throws Exception {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .build();

    assertEquals("true",
      mapboxDirections.cloneCall().request().url().queryParameter("enable_refresh"));
  }

  @Test(expected = Exception.class)
  public void token_required() throws Exception {
    MapboxDirections.builder()
      .routeOptions(routeOptions.toBuilder().baseUrl(mockUrl.toString()).build())
      .build();
  }

  @Test
  public void metadata_doesGetCreatedInResponse() throws IOException {
    Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(DirectionsAdapterFactory.create()).create();
    String body = loadJsonFixture(DIRECTIONS_V5_FIXTURE);
    DirectionsResponse response = gson.fromJson(body, DirectionsResponse.class);

    Metadata metadata = response.metadata();
    assertNotNull(metadata);
    Map<String, String> infoMap = metadata.infoMap();
    assertNotNull(infoMap);

    assertEquals("2021_07_14-03_00_00", infoMap.get("tileset_version"));
    assertEquals("and its value", infoMap.get("some_other_property"));
  }

  @Test
  public void responseFactory_routesUpdatedWithRequestData() throws IOException {
    testDispatcher.currentResource = DIRECTIONS_ALTERNATIVES_V5_FIXTURE;
    RouteOptions altRouteOptions =
      RouteOptions.fromJson(loadJsonFixture(ROUTE_OPTIONS_ALTERNATIVES_V5))
        .toBuilder()
        .baseUrl(mockUrl.toString())
        .build();

    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(altRouteOptions)
      .build();

    Response<DirectionsResponse> response = mapboxDirections.executeCall();

    DirectionsRoute route1 = response.body().routes().get(0);
    DirectionsRoute route2 = response.body().routes().get(1);

    assertEquals("0", route1.routeIndex());
    assertEquals("1", route2.routeIndex());
    assertEquals(
      "qIutV-I6xWhBmE1SgzowNSiujLyTETZVGewuHL3E74foqlBbvC8S0A==",
      route1.requestUuid()
    );
    assertEquals(
      "qIutV-I6xWhBmE1SgzowNSiujLyTETZVGewuHL3E74foqlBbvC8S0A==",
      route2.requestUuid()
    );
    assertEquals(altRouteOptions, route1.routeOptions());
    assertEquals(altRouteOptions, route2.routeOptions());
  }

  @Test
  public void responseFactory_routesUpdatedWithRequestData_async() throws IOException {
    testDispatcher.currentResource = DIRECTIONS_ALTERNATIVES_V5_FIXTURE;
    final RouteOptions altRouteOptions =
      RouteOptions.fromJson(loadJsonFixture(ROUTE_OPTIONS_ALTERNATIVES_V5))
        .toBuilder()
        .baseUrl(mockUrl.toString())
        .build();

    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("token")
      .routeOptions(altRouteOptions)
      .build();

    final CountDownLatch latch = new CountDownLatch(1);
    mapboxDirections.enqueueCall(
      new Callback<DirectionsResponse>() {
        @Override
        public void onResponse(retrofit2.Call<DirectionsResponse> call,
                               Response<DirectionsResponse> response) {
          DirectionsRoute route1 = response.body().routes().get(0);
          DirectionsRoute route2 = response.body().routes().get(1);

          assertEquals("0", route1.routeIndex());
          assertEquals("1", route2.routeIndex());
          assertEquals(
            "qIutV-I6xWhBmE1SgzowNSiujLyTETZVGewuHL3E74foqlBbvC8S0A==",
            route1.requestUuid()
          );
          assertEquals(
            "qIutV-I6xWhBmE1SgzowNSiujLyTETZVGewuHL3E74foqlBbvC8S0A==",
            route2.requestUuid()
          );
          assertEquals(altRouteOptions, route1.routeOptions());
          assertEquals(altRouteOptions, route2.routeOptions());
          latch.countDown();
        }

        @Override
        public void onFailure(retrofit2.Call<DirectionsResponse> call, Throwable t) {
          Assert.fail();
        }
      }
    );
    try {
      if (!latch.await(5, TimeUnit.SECONDS)) {
        Assert.fail();
      }
    } catch (InterruptedException e) {
      Assert.fail();
    }
  }

  class TestDispatcher extends okhttp3.mockwebserver.Dispatcher {

    String currentResource;

    @Override
    public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
      try {
        String body = loadJsonFixture(currentResource);
        return new MockResponse().setBody(body);
      } catch (IOException ioException) {
        throw new RuntimeException(ioException);
      }
    }
  }
}
