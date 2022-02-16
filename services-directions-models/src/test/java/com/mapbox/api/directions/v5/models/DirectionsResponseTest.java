package com.mapbox.api.directions.v5.models;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DirectionsResponseTest extends TestUtils {

  private static final String DIRECTIONS_V5_PRECISION6_FIXTURE = "directions_v5_precision_6.json";
  private static final String DIRECTIONS_V5_MULTIPLE_ROUTES = "directions_v5_multiple_routes.json";
  private static final String DIRECTIONS_V5_MULTIPLE_ROUTES_WITH_OPTIONS =
    "directions_v5_multiple_routes_with_options.json";
  private static final String DIRECTIONS_V5_SILENT_WAYPOINT = "directions_v5_silent_waypoints.json";

  @Test
  public void sanity() throws Exception {
    DirectionsResponse response = DirectionsResponse.builder()
      .code("100")
      .routes(new ArrayList<DirectionsRoute>())
      .build();
    assertNotNull(response);
  }

  @Test
  public void fromJson_correctlyBuildsFromJson() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_PRECISION6_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);
    assertNotNull(response);
    assertEquals(1, response.routes().size());
  }

  @Test
  public void testToFromJson() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_PRECISION6_FIXTURE);
    DirectionsResponse responseFromJson1 = DirectionsResponse.fromJson(json);

    String jsonString = responseFromJson1.toJson();
    DirectionsResponse responseFromJson2 = DirectionsResponse.fromJson(jsonString);

    Assert.assertEquals(responseFromJson1, responseFromJson2);
    Assert.assertEquals(responseFromJson2, responseFromJson1);
  }

  @Test
  public void fromJson_correctlyBuildsFromJsonWithOptionsAndUuid() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_PRECISION6_FIXTURE);
    RouteOptions options = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .coordinatesList(new ArrayList<Point>() {{
        add(Point.fromLngLat(1.0, 1.0));
        add(Point.fromLngLat(2.0, 2.0));
      }})
      .build();
    String uuid = "123";
    DirectionsResponse response = DirectionsResponse.fromJson(json, options, uuid);

    assertEquals(options, response.routes().get(0).routeOptions());
    assertEquals(uuid, response.routes().get(0).requestUuid());
  }

  @Test
  public void fromJson_correctlyBuildsFromJsonWithOptions() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_PRECISION6_FIXTURE);
    RouteOptions options = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .coordinatesList(new ArrayList<Point>() {{
        add(Point.fromLngLat(1.0, 1.0));
        add(Point.fromLngLat(2.0, 2.0));
      }})
      .build();
    DirectionsResponse response = DirectionsResponse.fromJson(json, options);

    assertEquals(options, response.routes().get(0).routeOptions());
    assertEquals("cjhk3ov9e1voc3vp58hcgit34", response.routes().get(0).requestUuid());
  }

  @Test
  public void fromJson_correctlyBuildsFromJsonWithUuid() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_PRECISION6_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);

    assertEquals("cjhk3ov9e1voc3vp58hcgit34", response.routes().get(0).requestUuid());
  }

  @Test
  public void fromJson_multipleRoutesHaveCorrectIndices() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_MULTIPLE_ROUTES);

    List<DirectionsRoute> routes = DirectionsResponse.fromJson(json).routes();

    assertEquals("0", routes.get(0).routeIndex());
    assertEquals("1", routes.get(1).routeIndex());
  }

  @Test
  public void fromJson_deserializesOptions() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_MULTIPLE_ROUTES_WITH_OPTIONS);

    List<DirectionsRoute> routes = DirectionsResponse.fromJson(json).routes();

    assertNotNull(routes.get(0).routeOptions());
    assertNotNull(routes.get(1).routeOptions());
  }

  @Test
  public void fromJson_deserializeWiaWaypoints() throws IOException {
    String json = loadJsonFixture(DIRECTIONS_V5_SILENT_WAYPOINT);
    DirectionsRoute route = DirectionsResponse.fromJson(json).routes().get(0);
    List<SilentWaypoint> viaWaypoints = route.legs().get(0).viaWaypoints();
    assertNotNull(viaWaypoints);
    assertEquals(1, viaWaypoints.size());
    SilentWaypoint waypoint = viaWaypoints.get(0);
    assertEquals(1, waypoint.waypointIndex());
    assertEquals(616.839, waypoint.distanceFromStart(), 0.001);
    assertEquals(58, waypoint.geometryIndex());
  }

  @Test
  public void testFromToJsonForRouteWithSilentWaypoints() throws IOException {
    String json = loadJsonFixture(DIRECTIONS_V5_SILENT_WAYPOINT);
    DirectionsResponse initial = DirectionsResponse.fromJson(json);
    String serialized = initial.toJson();
    DirectionsResponse deserialized = DirectionsResponse.fromJson(serialized);
    assertEquals(initial, deserialized);
  }
}
