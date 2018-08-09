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
}
