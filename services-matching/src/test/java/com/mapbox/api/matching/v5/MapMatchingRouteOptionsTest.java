package com.mapbox.api.matching.v5;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.models.RouteOptions;
import com.mapbox.api.matching.v5.models.MapMatchingMatching;
import com.mapbox.api.matching.v5.models.MapMatchingResponse;
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
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MapMatchingRouteOptionsTest extends TestUtils {

  private static final String MAP_MATCHING_FIXTURE = "map_matching_v5_polyline.json";

  private MockWebServer server;
  private List<Point> coordinates;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String body = loadJsonFixture(MAP_MATCHING_FIXTURE);
          return new MockResponse().setBody(body);
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
  public void mapMatchingRequestResult_doesContainTheOriginalRequestData() throws Exception {
    Response<MapMatchingResponse> response = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .language(Locale.CANADA).build().executeCall();
    MapMatchingMatching matching = response.body().matchings().get(0);

    assertEquals(Locale.CANADA.getLanguage(), matching.routeOptions().language());
    assertEquals(DirectionsCriteria.PROFILE_WALKING, matching.routeOptions().profile());
    assertEquals(Constants.MAPBOX_USER, matching.routeOptions().user());

    // Never set values
    assertNull(matching.routeOptions().annotations());
    assertNull(matching.routeOptions().bearings());
  }

  @Test
  public void mapMatchingRequestResult_doesContainBaseUrl() throws Exception {
    Response<MapMatchingResponse> response = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .language(Locale.CANADA)
      .build().executeCall();
    MapMatchingMatching matching = response.body().matchings().get(0);

    assertEquals(mockUrl.toString(), matching.routeOptions().baseUrl());
  }

  @Test
  public void mapMatchingRequestResult_doesContainRoundaboutExits() throws Exception {
    Response<MapMatchingResponse> response = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .language(Locale.CANADA)
      .roundaboutExits(true)
      .build().executeCall();
    MapMatchingMatching matching = response.body().matchings().get(0);

    assertEquals(true, matching.routeOptions().roundaboutExits());
  }

  @Test
  public void mapMatchingRequestResult_doesContainSteps() throws Exception {
    Response<MapMatchingResponse> response = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .language(Locale.CANADA)
      .steps(true)
      .build().executeCall();
    MapMatchingMatching matching = response.body().matchings().get(0);

    assertEquals(true, matching.routeOptions().steps());
  }

  @Test
  public void mapMatchingRequestResult_doesContainGeometries() throws Exception {
    Response<MapMatchingResponse> response = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .language(Locale.CANADA)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build().executeCall();
    MapMatchingMatching matching = response.body().matchings().get(0);

    assertEquals(DirectionsCriteria.GEOMETRY_POLYLINE, matching.routeOptions().geometries());
  }

  @Test
  public void mapMatchingRequestResult_doesContainOverview() throws Exception {
    Response<MapMatchingResponse> response = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .language(Locale.CANADA)
      .overview(DirectionsCriteria.OVERVIEW_SIMPLIFIED)
      .build()
      .executeCall();
    MapMatchingMatching matching = response.body().matchings().get(0);

    assertEquals(DirectionsCriteria.OVERVIEW_SIMPLIFIED, matching.routeOptions().overview());
  }
}
