package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.junit.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class DirectionsRouteTest extends TestUtils {

  private static final String DIRECTIONS_V5_VOICE_BANNER_FIXTURE =
    "directions_v5_voice_banner.json";
  private static final String DIRECTIONS_V5_VOICE_INVALID_FIXTURE =
    "directions_v5_voice_invalid.json";
  private static final int FIRST_ROUTE = 0;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    DirectionsRoute route = DirectionsRoute.builder()
      .distance(100d)
      .duration(100d)
      .build();
    assertNotNull(route);
  }

  @Test
  public void testSerializable() throws Exception {
    DirectionsRoute route = DirectionsRoute.builder().distance(100d).duration(100d).build();
    byte[] serialized = TestUtils.serialize(route);
    assertEquals(route, deserialize(serialized, DirectionsRoute.class));
  }

  @Test
  public void directionsRoute_doesReturnVoiceLocale() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_VOICE_BANNER_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);
    DirectionsRoute route = response.routes().get(FIRST_ROUTE);

    String voiceLanguage = route.voiceLanguage();

    assertEquals("en-US", voiceLanguage);
  }

  @Test
  public void directionsRouteWithInvalidLanguage_doesReturnNullVoiceLanguage() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_VOICE_INVALID_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);
    DirectionsRoute route = response.routes().get(FIRST_ROUTE);

    String voiceLanguage = route.voiceLanguage();

    assertNull(voiceLanguage);
  }

  @Test
  public void directionsRoute_doesContainOptionsAndUuid() throws Exception {
    String json = loadJsonFixture("directions_v5-with-closure_precision_6.json");
    RouteOptions options = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .coordinatesList(new ArrayList<Point>() {{
        add(Point.fromLngLat(1.0, 1.0));
        add(Point.fromLngLat(2.0, 2.0));
      }})
      .build();
    String uuid = "123";
    DirectionsRoute route = DirectionsRoute.fromJson(json, options, uuid);

    assertEquals(options, route.routeOptions());
    assertEquals(uuid, route.requestUuid());
  }

  @Test
  public void directionsRoute_json_withOptionsAndUUID_roundTripping() throws Exception {
    String json = loadJsonFixture("directions_v5-with-closure_precision_6.json");
    RouteOptions options = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .coordinatesList(new ArrayList<Point>() {{
        add(Point.fromLngLat(1.0, 1.0));
        add(Point.fromLngLat(2.0, 2.0));
      }})
      .build();
    String uuid = "123";
    DirectionsRoute route = DirectionsRoute.fromJson(json, options, uuid);

    String newRouteJson = route.toJson();

    DirectionsRoute newRoute = DirectionsRoute.fromJson(newRouteJson);

    assertEquals(route, newRoute);
  }

  @Test
  public void directionsRoute_canSetIndex() throws Exception {
    String json = loadJsonFixture("directions_v5-with-closure_precision_6.json");
    RouteOptions options = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .coordinatesList(new ArrayList<Point>() {{
        add(Point.fromLngLat(1.0, 1.0));
        add(Point.fromLngLat(2.0, 2.0));
      }})
      .build();
    String uuid = "123";
    DirectionsRoute route = DirectionsRoute.fromJson(json, options, uuid);

    DirectionsRoute newRoute = route.toBuilder().routeIndex("0").build();

    assertEquals("0", newRoute.routeIndex());
  }

  @Test
  public void directionsRoute_hasTollCosts() throws IOException {
    String json = loadJsonFixture("directions_v5_with_toll_costs.json");
    RouteOptions options = RouteOptions.builder()
            .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
            .coordinatesList(new ArrayList<Point>() {{
              add(Point.fromLngLat(1.0, 1.0));
              add(Point.fromLngLat(2.0, 2.0));
            }})
            .build();
    String uuid = "123";
    DirectionsRoute route = DirectionsRoute.fromJson(json, options, uuid);

    assertEquals(2, route.tollCosts().size());
  }

  @Test
  public void directionsRoute_noTollCosts() throws IOException {
    String json = loadJsonFixture("directions_v5-with-closure_precision_6.json");
    RouteOptions options = RouteOptions.builder()
            .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
            .coordinatesList(new ArrayList<Point>() {{
              add(Point.fromLngLat(1.0, 1.0));
              add(Point.fromLngLat(2.0, 2.0));
            }})
            .build();
    String uuid = "123";
    DirectionsRoute route = DirectionsRoute.fromJson(json, options, uuid);

    assertNull(route.tollCosts());
  }

  @Test
  public void directionsRoute_hasWaypoints() throws IOException {
    String json = loadJsonFixture("directions_v5_with_waypoints.json");
    RouteOptions options = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .coordinatesList(new ArrayList<Point>() {{
        add(Point.fromLngLat(1.0, 1.0));
        add(Point.fromLngLat(2.0, 2.0));
      }})
      .waypointsPerRoute(true)
      .build();
    String uuid = "123";
    DirectionsRoute route = DirectionsRoute.fromJson(json, options, uuid);

    assertEquals(2, route.waypoints().size());
  }

  @Test
  public void directionsRouteBuilder_hasWaypoints() {
    List<DirectionsWaypoint> waypoints = Arrays.asList(
      DirectionsWaypoint.builder()
        .name("name1")
        .rawLocation(new double[] { 5.6, 7.8 })
        .build(),
      DirectionsWaypoint.builder()
        .name("name2")
        .rawLocation(new double[] { 1.2, 3.4 })
        .build()
    );
    DirectionsRoute route = DirectionsRoute.builder()
      .duration(12.12)
      .distance(34.34)
      .waypoints(waypoints)
      .build();

    assertEquals(waypoints, route.waypoints());
  }


  @Test
  public void directionsRoute_noWaypoints() throws IOException {
    String json = loadJsonFixture("directions_v5-with-closure_precision_6.json");
    RouteOptions options = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .coordinatesList(new ArrayList<Point>() {{
        add(Point.fromLngLat(1.0, 1.0));
        add(Point.fromLngLat(2.0, 2.0));
      }})
      .waypointsPerRoute(true)
      .build();
    String uuid = "123";
    DirectionsRoute route = DirectionsRoute.fromJson(json, options, uuid);

    assertNull(route.waypoints());
  }

  @Test
  public void directionsRouteBuilder_noWaypoints() {
    DirectionsRoute route = DirectionsRoute.builder()
      .duration(12.12)
      .distance(34.34)
      .build();

    assertNull(route.waypoints());
  }
}
