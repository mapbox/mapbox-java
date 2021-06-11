package com.mapbox.api.directions.v5.models;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.geojson.Point;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static com.google.gson.JsonParser.parseString;
import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_CURB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RouteOptionsTest {

  public static final String ACCESS_TOKEN = "pk.XXX";

  // todo move to JSON file
  private static final String ROUTE_OPTIONS_JSON =
    "{\"baseUrl\":\"https://api.mapbox.com\",\"user\":\"mapbox\",\"profile\":\"driving-traffic\",\"coordinates\":[[-122.4003312,37.7736941],[-122.4187529,37.7689715],[-122.4255172,37.7775835]],\"alternatives\":false,\"language\":\"ru\",\"radiuses\":\";unlimited;100\",\"bearings\":\"0,90;90,0;\",\"continue_straight\":false,\"roundabout_exits\":false,\"geometries\":\"polyline6\",\"overview\":\"full\",\"steps\":true,\"annotations\":\"congestion,distance,duration\",\"exclude\":\"toll\",\"voice_instructions\":true,\"banner_instructions\":true,\"voice_units\":\"metric\",\"access_token\":\"token\",\"uuid\":\"12345543221\",\"approaches\":\";curb;\",\"waypoints\":\"0;1;2\",\"waypoint_names\":\";two;\",\"waypoint_targets\":\";12.2,21.2;\",\"snapping_include_closures\":\";false;true\", \"alley_bias\":0.75,\"walking_speed\":5.11,\"walkway_bias\":-0.2,\"arrive_by\":\"2021-01-01'T'01:01\",\"depart_at\":\"2021-01-01'T'01:01\"}";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void toBuilder() {
    RouteOptions routeOptions = routeOptions();

    String language = "ru";
    String url = "new_base_url";

    RouteOptions updatedOptions = routeOptions.toBuilder()
      .language(language)
      .baseUrl(url)
      .build();

    assertEquals(language, updatedOptions.language());
    assertEquals(url, updatedOptions.baseUrl());
    assertEquals(routeOptions.accessToken(), updatedOptions.accessToken());
    assertEquals(routeOptions.coordinates(), updatedOptions.coordinates());
    assertEquals(routeOptions.user(), updatedOptions.user());
    assertEquals(routeOptions.profile(), updatedOptions.profile());
    assertEquals(routeOptions.geometries(), updatedOptions.geometries());
    assertEquals(routeOptions.requestUuid(), updatedOptions.requestUuid());
  }

  @Test
  public void baseUrlIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("https://api.mapbox.com", routeOptions.baseUrl());
  }

  @Test
  public void userIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(DirectionsCriteria.PROFILE_DEFAULT_USER, routeOptions.user());
  }

  @Test
  public void profileIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("driving-traffic", routeOptions.profile());
  }

  @Test
  public void coordinatesAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(3, routeOptions.coordinates().size());
    assertEquals(Point.fromLngLat(-122.4003312, 37.7736941), routeOptions.coordinates().get(0));
    assertEquals(Point.fromLngLat(-122.4187529, 37.7689715), routeOptions.coordinates().get(1));
    assertEquals(Point.fromLngLat(-122.4255172, 37.7775835), routeOptions.coordinates().get(2));
  }

  @Test
  public void alternativesAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(false, routeOptions.alternatives());
  }

  @Test
  public void languageIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("ru", routeOptions.language());
  }

  @Test
  public void radiusesAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(";unlimited;100", routeOptions.radiuses());
  }

  @Test
  public void radiusesListIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(3, routeOptions.radiuses().size());
    assertNull(routeOptions.radiuses().get(0));
    assertEquals(Double.valueOf(Double.POSITIVE_INFINITY), routeOptions.radiuses().get(1));
    assertEquals(Double.valueOf(100.0), routeOptions.radiuses().get(2));
  }

  @Test
  public void bearingsAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("0,90;90,0;", routeOptions.bearings());
  }

  @Test
  public void bearingsListIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(3, routeOptions.bearings().size());
    assertEquals(Double.valueOf(0), routeOptions.bearings().get(0).get(0));
    assertEquals(Double.valueOf(90), routeOptions.bearings().get(0).get(1));
    assertEquals(Double.valueOf(90), routeOptions.bearings().get(1).get(0));
    assertEquals(Double.valueOf(0), routeOptions.bearings().get(1).get(1));
    assertNull(routeOptions.bearings().get(2));
  }

  @Test
  public void continueStraightIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(false, routeOptions.continueStraight());
  }

  @Test
  public void roundaboutExitsIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(false, routeOptions.continueStraight());
  }

  @Test
  public void geometriesAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("polyline6", routeOptions.geometries());
  }

  @Test
  public void stepsAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(true, routeOptions.steps());
  }

  @Test
  public void annotationsAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("congestion,distance,duration", routeOptions.annotations());
  }

  @Test
  public void annotationsListIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(3, routeOptions.annotations().size());
    assertEquals("congestion", routeOptions.annotations().get(0));
    assertEquals("distance", routeOptions.annotations().get(1));
    assertEquals("duration", routeOptions.annotations().get(2));
  }

  @Test
  public void excludeIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("toll", routeOptions.exclude());
  }

  @Test
  public void overviewIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("full", routeOptions.overview());
  }

  @Test
  public void voiceInstructionsAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(true, routeOptions.voiceInstructions());
  }

  @Test
  public void bannerInstructionsAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(true, routeOptions.bannerInstructions());
  }

  @Test
  public void voiceUnitsAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("metric", routeOptions.voiceUnits());
  }

  @Test
  public void accessTokenIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("token", routeOptions.accessToken());
  }

  @Test
  public void uuidIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("12345543221", routeOptions.requestUuid());
  }

  @Test
  public void approachesStringIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(";curb;", routeOptions.approaches());
  }

  @Test
  public void approachesListIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(3, routeOptions.approaches().size());
    assertNull(routeOptions.approaches().get(0));
    assertEquals("curb", routeOptions.approaches().get(1));
    assertNull(routeOptions.approaches().get(2));
  }

  @Test
  public void waypointIndicesStringIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("0;1;2", routeOptions.waypointIndices());
  }

  @Test
  public void waypointIndicesListIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(3, routeOptions.waypointIndices().size());
    assertEquals(Integer.valueOf(0), routeOptions.waypointIndices().get(0));
    assertEquals(Integer.valueOf(1), routeOptions.waypointIndices().get(1));
    assertEquals(Integer.valueOf(2), routeOptions.waypointIndices().get(2));
  }

  @Test
  public void waypointNamesStringIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(";two;", routeOptions.waypointNames());
  }

  @Test
  public void waypointNamesListIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(3, routeOptions.waypointNames().size());
    assertNull(routeOptions.waypointNames().get(0));
    assertEquals("two", routeOptions.waypointNames().get(1));
    assertNull(routeOptions.waypointNames().get(2));
  }

  @Test
  public void waypointTargetsStringIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(";12.2,21.2;", options.waypointTargets());
  }

  @Test
  public void waypointTargetsListIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(3, options.waypointTargets().size());
    assertNull(options.waypointTargets().get(0));
    assertEquals(Point.fromLngLat(12.2, 21.2), options.waypointTargets().get(1));
    assertNull(options.waypointTargets().get(2));
  }

  @Test
  public void snappingIncludeClosuresStringIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(";false;true", options.snappingIncludeClosures());
  }

  @Test
  public void snappingIncludeClosuresListIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    List list = options.snappingIncludeClosures();
    assertEquals(3, list.size());
    assertNull(list.get(0));
    assertEquals(false, list.get(1));
    assertEquals(true, list.get(2));
  }

  @Test
  public void routeOptions_toJson() {
    RouteOptions options = routeOptions();

    assertEquals(parseString(ROUTE_OPTIONS_JSON), parseString(options.toJson()));
  }

  @Test
  public void radiusesList_toJson() {
    List<String> radiuses = new ArrayList<>();
    radiuses.add(null);
    radiuses.add("unlimited");
    radiuses.add("100");

    RouteOptions finalOptions = RouteOptions.builder()
      .radiuses(radiuses)
      .build();

    assertEquals(parseString(ROUTE_OPTIONS_JSON), parseString(finalOptions.toJson()));
  }

  @Test
  public void bearingsList_toJson() {
    List<Double> originBearing = new ArrayList<>();
    originBearing.add(0.0);
    originBearing.add(90.0);
    List<Double> waypointBearing = new ArrayList<>();
    waypointBearing.add(90.0);
    waypointBearing.add(0.0);

    List<List<Double>> bearings = new ArrayList<>();
    bearings.add(originBearing);
    bearings.add(waypointBearing);
    bearings.add(null);

    RouteOptions finalOptions = RouteOptions.builder()
      .bearings(bearings)
      .build();

    assertEquals(parseString(ROUTE_OPTIONS_JSON), parseString(finalOptions.toJson()));
  }

  @Test
  public void annotationsList_toJson() {
    List<String> annotations = new ArrayList<>();
    annotations.add("congestion");
    annotations.add("distance");
    annotations.add("duration");

    RouteOptions finalOptions = RouteOptions.builder()
      .annotations(annotations)
      .build();

    assertEquals(parseString(ROUTE_OPTIONS_JSON), parseString(finalOptions.toJson()));
  }

  @Test
  public void approachesList_toJson() {
    List<String> approaches = new ArrayList<>();
    approaches.add(null);
    approaches.add("curb");
    approaches.add(null);

    RouteOptions finalOptions = RouteOptions.builder()
      .approaches(approaches)
      .build();

    assertEquals(parseString(ROUTE_OPTIONS_JSON), parseString(finalOptions.toJson()));
  }

  @Test
  public void waypointIndicesList_toJson() {
    List<Integer> waypoints = new ArrayList<>();
    waypoints.add(0);
    waypoints.add(1);
    waypoints.add(2);

    RouteOptions finalOptions = RouteOptions.builder()
      .waypointIndices(waypoints)
      .build();

    assertEquals(parseString(ROUTE_OPTIONS_JSON), parseString(finalOptions.toJson()));
  }

  @Test
  public void waypointNamesList_toJson() {
    List<String> waypointNames = new ArrayList<>();
    waypointNames.add(null);
    waypointNames.add("two");
    waypointNames.add(null);

    RouteOptions finalOptions = RouteOptions.builder()
      .waypointNames(waypointNames)
      .build();

    assertEquals(parseString(ROUTE_OPTIONS_JSON), parseString(finalOptions.toJson()));
  }

  @Test
  public void waypointTargetsList_toJson() {
    List<Point> waypointTargets = new ArrayList<>();
    waypointTargets.add(null);
    waypointTargets.add(Point.fromLngLat(12.2, 21.2));
    waypointTargets.add(null);

    RouteOptions finalOptions = RouteOptions.builder()
      .waypointTargets(waypointTargets)
      .build();

    assertEquals(parseString(ROUTE_OPTIONS_JSON), parseString(finalOptions.toJson()));
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
      .coordinates(coordinates)
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
      .coordinates(coordinates)
      .build();
  }

  @Test
  public void build_coordinatesSize() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "An origin and destination are required before making the directions API request."
    );
    List<Point> coordinates = new ArrayList<>();
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(coordinates)
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
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointIndices(waypointIndices)
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
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointIndices(waypointIndices)
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
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointIndices(waypointIndices)
      .build();
  }

  @Test
  public void build_waypointTargetsSize_empty() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of waypoint targets must match the number of coordinates provided."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<Point> data = new ArrayList<>();
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointTargets(data)
      .build();
  }

  @Test
  public void build_waypointTargetsSize() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of waypoint targets must match the number of coordinates provided."
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
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointTargets(data)
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
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .approaches(data)
      .build();
  }

  @Test
  public void build_approachesSize_empty() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of approach elements must match number of coordinates provided."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<String> data = new ArrayList<>();
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .approaches(data)
      .build();
  }

  @Test
  public void build_approachesSize() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of approach elements must match number of coordinates provided."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<String> data = new ArrayList<>();
    data.add(APPROACH_CURB);
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .approaches(data)
      .build();
  }

  @Test
  public void build_snappingIncludeClosuresSize_empty() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of snapping include closures elements must match number of coordinates provided."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<Boolean> data = new ArrayList<>();
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .snappingIncludeClosures(data)
      .build();
  }

  @Test
  public void build_snappingIncludeClosuresSize() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of snapping include closures elements must match number of coordinates provided."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<Boolean> data = new ArrayList<>();
    data.add(false);
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .snappingIncludeClosures(data)
      .build();
  }

  @Test
  public void build_bearingsSize_empty() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of bearings elements must match number of coordinates provided."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<List<Double>> data = new ArrayList<>();
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .bearings(data)
      .build();
  }

  @Test
  public void build_bearingsSize() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of bearings elements must match number of coordinates provided."
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
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .bearings(data)
      .build();
  }

  @Test
  public void build_waypointNamesSize_empty() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of approach elements must match number of coordinates provided."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<String> data = new ArrayList<>();
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .approaches(data)
      .build();
  }

  @Test
  public void build_waypointNamesSize() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Number of waypoint names elements must match number of coordinates provided."
    );
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.0, 0.0));
    coordinates.add(Point.fromLngLat(1.0, 1.0));
    coordinates.add(Point.fromLngLat(2.0, 3.0));
    List<String> data = new ArrayList<>();
    data.add("test");
    RouteOptions.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(coordinates)
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .waypointNames(data)
      .build();
  }

  private RouteOptions routeOptions() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-122.4003312, 37.7736941));
    coordinates.add(Point.fromLngLat(-122.4187529, 37.7689715));
    coordinates.add(Point.fromLngLat(-122.4255172, 37.7775835));

    return RouteOptions.builder()
      .baseUrl("https://api.mapbox.com")
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .coordinates(coordinates)
      .alternatives(false)
      .annotations(new ArrayList<String>() {{
        add("congestion");
        add("distance");
        add("duration");
      }})
      .bearings(new ArrayList<List<Double>>() {{
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
      .radiuses(new ArrayList<String>() {{
        add(null);
        add("unlimited");
        add("100");
      }})
      .approaches(new ArrayList<String>() {{
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
      .waypointNames(new ArrayList<String>() {{
        add(null);
        add("two");
        add(null);
      }})
      .waypointTargets(new ArrayList<Point>() {{
        add(null);
        add(Point.fromLngLat(12.2, 21.2));
        add(null);
      }})
      .waypointIndices(new ArrayList<Integer>() {{
        add(0);
        add(1);
        add(2);
      }})
      .alleyBias(0.75)
      .walkingSpeed(5.11)
      .walkwayBias(-0.2)
      .arriveBy("2021-01-01'T'01:01")
      .departAt("2021-01-01'T'01:01")
      .snappingIncludeClosures(new ArrayList<Boolean>() {{
        add(null);
        add(false);
        add(true);
      }})
      .accessToken("token")
      .user(DirectionsCriteria.PROFILE_DEFAULT_USER)
      .requestUuid("12345543221")
      .build();
  }
}
