package com.mapbox.api.directions.v5.models;

import static com.google.gson.JsonParser.parseString;
import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_CURB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RouteOptionsTest extends TestUtils {

  public static final String ACCESS_TOKEN = "pk.XXX";

  /**
   * Always update this file when new option is introduced.
   */
  private static final String ROUTE_OPTIONS_JSON = "route_options_v5.json";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private final String optionsJson = loadJsonFixture(ROUTE_OPTIONS_JSON);

  public RouteOptionsTest() throws IOException {
  }

  @Test
  public void baseUrlIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(DirectionsCriteria.BASE_API_URL, routeOptions.baseUrl());
  }

  @Test
  public void userIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(DirectionsCriteria.PROFILE_DEFAULT_USER, routeOptions.user());
  }

  @Test
  public void profileIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC, routeOptions.profile());
  }

  @Test
  public void coordinatesAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(
      "-122.4003312,37.7736941;-122.4187529,37.7689715;-122.4255172,37.7775835",
      routeOptions.coordinates()
    );
  }

  @Test
  public void alternativesAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(false, routeOptions.alternatives());
  }

  @Test
  public void languageIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals("ru", routeOptions.language());
  }

  @Test
  public void radiusesAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(";unlimited;5.1", routeOptions.radiuses());
  }

  @Test
  public void radiusesListIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(3, routeOptions.radiusesList().size());
    assertNull(routeOptions.radiusesList().get(0));
    assertEquals(Double.valueOf(Double.POSITIVE_INFINITY), routeOptions.radiusesList().get(1));
    assertEquals(Double.valueOf(5.1), routeOptions.radiusesList().get(2));
  }

  @Test
  public void bearingsAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals("0,90;90,0;", routeOptions.bearings());
  }

  @Test
  public void bearingsListIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(3, routeOptions.bearingsList().size());
    assertEquals(Double.valueOf(0), routeOptions.bearingsList().get(0).get(0));
    assertEquals(Double.valueOf(90), routeOptions.bearingsList().get(0).get(1));
    assertEquals(Double.valueOf(90), routeOptions.bearingsList().get(1).get(0));
    assertEquals(Double.valueOf(0), routeOptions.bearingsList().get(1).get(1));
    assertNull(routeOptions.bearingsList().get(2));
  }

  @Test
  public void continueStraightIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(false, routeOptions.continueStraight());
  }

  @Test
  public void roundaboutExitsIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(false, routeOptions.continueStraight());
  }

  @Test
  public void geometriesAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(DirectionsCriteria.GEOMETRY_POLYLINE6, routeOptions.geometries());
  }

  @Test
  public void stepsAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(true, routeOptions.steps());
  }

  @Test
  public void annotationsAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals("congestion,distance,duration", routeOptions.annotations());
  }

  @Test
  public void excludeIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals("toll", routeOptions.exclude());
  }

  @Test
  public void overviewIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals("full", routeOptions.overview());
  }

  @Test
  public void voiceInstructionsAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(true, routeOptions.voiceInstructions());
  }

  @Test
  public void bannerInstructionsAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(true, routeOptions.bannerInstructions());
  }

  @Test
  public void voiceUnitsAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(DirectionsCriteria.METRIC, routeOptions.voiceUnits());
  }

  @Test
  public void accessTokenIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals("token", routeOptions.accessToken());
  }

  @Test
  public void approachesStringIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(";curb;", routeOptions.approaches());
  }

  @Test
  public void waypointIndicesStringIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals("0;1;2", routeOptions.waypointIndices());
  }

  @Test
  public void waypointNamesStringIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals(";two;", routeOptions.waypointNames());
  }

  @Test
  public void waypointTargetsStringIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(optionsJson);

    assertEquals(";12.2,21.2;", options.waypointTargets());
  }

  @Test
  public void snappingIncludeClosuresStringIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(optionsJson);

    assertEquals(";false;true", options.snappingIncludeClosures());
  }

  @Test
  public void alleyBiasIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(optionsJson);

    assertEquals(0.75, options.alleyBias(), 0.000001);
  }

  @Test
  public void walkingSpeedIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(optionsJson);

    assertEquals(5.11, options.walkingSpeed(), 0.000001);
  }

  @Test
  public void walkwayBiasIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(optionsJson);

    assertEquals(-0.2, options.walkwayBias(), 0.000001);
  }

  @Test
  public void arriveByIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(optionsJson);

    assertEquals("2021-01-01'T'01:01", options.arriveBy());
  }

  @Test
  public void departAtIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(optionsJson);

    assertEquals("2021-02-02'T'02:02", options.departAt());
  }

  @Test
  public void enableRefreshIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(optionsJson);

    assertEquals(true, options.enableRefresh());
  }

  @Test
  public void routeOptions_toJson() {
    RouteOptions options = routeOptions();

    assertEquals(parseString(optionsJson), parseString(options.toJson()));
  }

  @Test
  public void routeOptionsList_toJson() {
    RouteOptions options = routeOptionsList();

    assertEquals(parseString(optionsJson), parseString(options.toJson()));
  }

  @Test
  public void build_noCoordinatesProvided() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage(
      "Missing required properties: coordinates"
    );
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .build();
  }

  @Test
  public void build_noProvided() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage(
      "Missing required properties: accessToken"
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    RouteOptions.builder()
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .build();
  }

  @Test
  public void build_noProfileProvided() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage(
      "Missing required properties: profile"
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .build();
  }

  @Test
  public void build_coordinates_empty() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage(
      "Missing required properties: coordinates"
    );
    List<Point> coordinates = new ArrayList<>();
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .build();
  }

  @Test
  public void build_coordinatesSize() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "An origin and destination are required before making the directions API request."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(1.0, 2.0));
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .build();
  }

  @Test
  public void build_waypointIndicesOriginDest_1() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Waypoints indices must be a list of at least two indexes, origin and destination."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    List<Integer> waypointIndices = new ArrayList<>();
    waypointIndices.add(null);
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointIndicesList(waypointIndices)
      .build();
  }

  @Test
  public void build_waypointIndicesOriginDest_2() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "First and last waypoints indices must match the origin and final destination."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<Integer> waypointIndices = new ArrayList<>();
    waypointIndices.add(0);
    waypointIndices.add(1);
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointIndicesList(waypointIndices)
      .build();
  }

  @Test
  public void build_waypointIndicesOriginDest_outOfBounds() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Waypoints index out of bounds (no corresponding coordinate)."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<Integer> waypointIndices = new ArrayList<>();
    waypointIndices.add(0);
    waypointIndices.add(3);
    waypointIndices.add(2);
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointIndicesList(waypointIndices)
      .build();
  }

  @Test
  public void build_waypointTargetsSize_null() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of waypointTargets must match the number of coordinates or waypoints indices " +
        "(if present)."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<Point> data = new ArrayList<>();
    data.add(null);
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointTargetsList(data)
      .build();
  }

  @Test
  public void build_waypointTargetsSize_empty() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<Point> data = new ArrayList<>();
    RouteOptions options = RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointTargetsList(data)
      .build();

    Assert.assertNull(options.waypointTargets());
  }

  @Test
  public void build_waypointTargetsSize() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of waypointTargets must match the number of coordinates or waypoints indices " +
        "(if present)."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<Point> data = new ArrayList<>();
    data.add(Point.fromLngLat(0.0, 0.0));
    data.add(Point.fromLngLat(1.0, 1.0));
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointTargetsList(data)
      .build();
  }

  @Test
  public void build_waypointTargets_withCustomIndices() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<Point> data = new ArrayList<>();
    data.add(Point.fromLngLat(0.0, 0.0));
    data.add(Point.fromLngLat(1.0, 1.0));
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointIndices("0;2")
      .waypointTargetsList(data)
      .build();
  }

  @Test
  public void build_approachesType() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Approach should be one of unrestricted or curb"
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<String> data = new ArrayList<>();
    data.add("1");
    data.add("2");
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .approachesList(data)
      .build();
  }

  @Test
  public void build_approachesSize_null() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of approaches must match the number of coordinates."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<String> data = new ArrayList<>();
    data.add(null);
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .approachesList(data)
      .build();
  }

  @Test
  public void build_approachesSize_empty() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<String> data = new ArrayList<>();
    RouteOptions options = RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .approachesList(data)
      .build();

    Assert.assertNull(options.approachesList());
  }

  @Test
  public void build_approachesSize() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of approaches must match the number of coordinates."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<String> data = new ArrayList<>();
    data.add(APPROACH_CURB);
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .approachesList(data)
      .build();
  }

  @Test
  public void build_snappingIncludeClosuresSize_null() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of snappingIncludeClosures must match the number of coordinates."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<Boolean> data = new ArrayList<>();
    data.add(null);
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .snappingIncludeClosuresList(data)
      .build();
  }

  @Test
  public void build_snappingIncludeClosuresSize_empty() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<Boolean> data = new ArrayList<>();
    RouteOptions options = RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .snappingIncludeClosuresList(data)
      .build();

    Assert.assertNull(options.snappingIncludeClosures());
  }

  @Test
  public void build_snappingIncludeClosuresSize() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of snappingIncludeClosures must match the number of coordinates."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<Boolean> data = new ArrayList<>();
    data.add(false);
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .snappingIncludeClosuresList(data)
      .build();
  }

  @Test
  public void build_bearingsSize_null() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of bearings must match the number of coordinates."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<List<Double>> data = new ArrayList<>();
    data.add(null);
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .bearingsList(data)
      .build();
  }

  @Test
  public void build_bearingsSize_empty() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<List<Double>> data = new ArrayList<>();
    RouteOptions options = RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .bearingsList(data)
      .build();

    Assert.assertNull(options.bearingsList());
  }

  @Test
  public void build_bearingsSize() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of bearings must match the number of coordinates."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<List<Double>> data = new ArrayList<>();
    List<Double> dataNested = new ArrayList<>();
    dataNested.add(0.0);
    dataNested.add(1.0);
    data.add(dataNested);
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .bearingsList(data)
      .build();
  }

  @Test
  public void build_waypointNamesSize_null() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of waypointNames must match the number of coordinates or waypoints indices " +
        "(if present)."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<String> data = new ArrayList<>();
    data.add(null);
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointNamesList(data)
      .build();
  }

  @Test
  public void build_waypointNamesSize_empty() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<String> data = new ArrayList<>();
    RouteOptions options = RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .approachesList(data)
      .build();

    Assert.assertNull(options.approachesList());
  }

  @Test
  public void build_waypointNamesSize() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of waypointNames must match the number of coordinates or waypoints indices " +
        "(if present)."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<String> data = new ArrayList<>();
    data.add("test");
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointNamesList(data)
      .build();
  }

  @Test
  public void build_waypointNamesSize_withCustomIndices() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<String> data = new ArrayList<>();
    data.add("test");
    data.add("test2");
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinatesList(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointIndices("0;2")
      .waypointNamesList(data)
      .build();
  }

  /**
   * Fills up all the options using string variants. Values need ot be equal to the ones in {@link #optionsJson}.
   */
  private RouteOptions routeOptions() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-122.4003312, 37.7736941));
    coordinates.add(Point.fromLngLat(-122.4187529, 37.7689715));
    coordinates.add(Point.fromLngLat(-122.4255172, 37.7775835));

    return RouteOptions.builder()
      .baseUrl("https://api.mapbox.com")
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .coordinatesList(coordinates)
      .alternatives(false)
      .annotations("congestion,distance,duration")
      .bearings("0,90;90,0;")
      .continueStraight(false)
      .exclude(DirectionsCriteria.EXCLUDE_TOLL)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .overview(DirectionsCriteria.OVERVIEW_FULL)
      .radiuses(";unlimited;5.1")
      .approaches(";curb;")
      .steps(true)
      .bannerInstructions(true)
      .language("ru")
      .roundaboutExits(false)
      .voiceInstructions(true)
      .voiceUnits(DirectionsCriteria.METRIC)
      .waypointNames(";two;")
      .waypointTargets(";12.2,21.2;")
      .waypointIndices("0;1;2")
      .alleyBias(0.75)
      .walkingSpeed(5.11)
      .walkwayBias(-0.2)
      .arriveBy("2021-01-01'T'01:01")
      .departAt("2021-02-02'T'02:02")
      .snappingIncludeClosures(";false;true")
      .accessToken("token")
      .user(DirectionsCriteria.PROFILE_DEFAULT_USER)
      .enableRefresh(true)
      .build();
  }

  /**
   * Fills up all the options using list variants. Values need ot be equal to the ones in {@link #optionsJson}.
   */
  private RouteOptions routeOptionsList() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-122.4003312, 37.7736941));
    coordinates.add(Point.fromLngLat(-122.4187529, 37.7689715));
    coordinates.add(Point.fromLngLat(-122.4255172, 37.7775835));

    return RouteOptions.builder()
      .baseUrl("https://api.mapbox.com")
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .coordinatesList(coordinates)
      .alternatives(false)
      .annotationsList(new ArrayList<String>() {{
        add("congestion");
        add("distance");
        add("duration");
      }})
      .bearingsList(new ArrayList<List<Double>>() {{
        add(new ArrayList<Double>() {{
          add(0.0);
          add(90.0);
        }});
        add(new ArrayList<Double>() {{
          add(90.0);
          add(0.0);
        }});
        add(null);
      }})
      .continueStraight(false)
      .exclude(DirectionsCriteria.EXCLUDE_TOLL)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .overview(DirectionsCriteria.OVERVIEW_FULL)
      .radiusesList(new ArrayList<Double>() {{
        add(null);
        add(Double.POSITIVE_INFINITY);
        add(5.1);
      }})
      .approachesList(new ArrayList<String>() {{
        add(null);
        add("curb");
        add(null);
      }})
      .steps(true)
      .bannerInstructions(true)
      .language("ru")
      .roundaboutExits(false)
      .voiceInstructions(true)
      .voiceUnits(DirectionsCriteria.METRIC)
      .waypointNamesList(new ArrayList<String>() {{
        add(null);
        add("two");
        add(null);
      }})
      .waypointTargetsList(new ArrayList<Point>() {{
        add(null);
        add(Point.fromLngLat(12.2, 21.2));
        add(null);
      }})
      .waypointIndicesList(new ArrayList<Integer>() {{
        add(0);
        add(1);
        add(2);
      }})
      .alleyBias(0.75)
      .walkingSpeed(5.11)
      .walkwayBias(-0.2)
      .arriveBy("2021-01-01'T'01:01")
      .departAt("2021-02-02'T'02:02")
      .snappingIncludeClosuresList(new ArrayList<Boolean>() {{
        add(null);
        add(false);
        add(true);
      }})
      .accessToken("token")
      .user(DirectionsCriteria.PROFILE_DEFAULT_USER)
      .enableRefresh(true)
      .build();
  }
}
