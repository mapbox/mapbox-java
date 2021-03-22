package com.mapbox.api.directions.v5.models;

import com.mapbox.geojson.Point;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import static com.mapbox.api.directions.v5.DirectionsCriteria.ANNOTATION_CONGESTION;
import static com.mapbox.api.directions.v5.DirectionsCriteria.ANNOTATION_DISTANCE;
import static com.mapbox.api.directions.v5.DirectionsCriteria.ANNOTATION_DURATION;
import static com.mapbox.api.directions.v5.DirectionsCriteria.ANNOTATION_MAXSPEED;
import static com.mapbox.api.directions.v5.DirectionsCriteria.ANNOTATION_SPEED;
import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_CURB;
import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_UNRESTRICTED;
import static org.junit.Assert.assertEquals;

public class RouteOptionsTest {

  private static final String ROUTE_OPTIONS_JSON =
      "{\"baseUrl\":\"https://api.mapbox.com\",\"user\":\"mapbox\",\"profile\":\"driving-traffic\",\"coordinates\":[[-122.4003312,37.7736941],[-122.4187529,37.7689715],[-122.4255172,37.7775835]],\"alternatives\":false,\"language\":\"ru\",\"radiuses\":\";unlimited;100\",\"bearings\":\"0,90;90,0;\",\"continue_straight\":false,\"roundabout_exits\":false,\"geometries\":\"polyline6\",\"overview\":\"full\",\"steps\":true,\"annotations\":\"congestion,distance,duration\",\"exclude\":\"toll\",\"voice_instructions\":true,\"banner_instructions\":true,\"voice_units\":\"metric\",\"access_token\":\"token\",\"uuid\":\"12345543221\",\"approaches\":\";curb;\",\"waypoints\":\"0;1;2\",\"waypoint_names\":\";two;\",\"waypoint_targets\":\";12.2,21.2;\",\"snapping_closures\":\";false;true\"}";

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
  public void radiusesString() {
    String radiusesStr = ";5.1;;7.4;;";

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .radiuses(radiusesStr)
        .build();

    assertEquals(radiusesStr, routeOptions.radiuses());

    List<Double> radiuses = routeOptions.radiusesList();
    assertEquals(6, radiuses.size());
    assertEquals(null, radiuses.get(0));
    assertEquals(Double.valueOf(5.1), radiuses.get(1));
    assertEquals(null, radiuses.get(2));
    assertEquals(Double.valueOf(7.4), radiuses.get(3));
    assertEquals(null, radiuses.get(4));
    assertEquals(null, radiuses.get(5));
  }

  @Test
  public void radiusesList() {
    List<Double> radiuses = new ArrayList<>();
    radiuses.add(null);
    radiuses.add(null);
    radiuses.add(5.7);
    radiuses.add(null);
    radiuses.add(4.4);
    radiuses.add(9.9);
    radiuses.add(null);
    radiuses.add(null);

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .radiusesList(radiuses)
        .build();

    assertEquals(radiuses, routeOptions.radiusesList());
    assertEquals(";;5.7;;4.4;9.9;;", routeOptions.radiuses());
  }

  @Test
  public void bearingsString() {
    String bearingsString = ";5.1,7.4;;";

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .bearings(bearingsString)
        .build();

    List<Double> bearing = new ArrayList<>();
    bearing.add(5.1);
    bearing.add(7.4);

    assertEquals(";5.1,7.4;;", routeOptions.bearings());

    List<List<Double>> bearings = routeOptions.bearingsList();
    assertEquals(4, bearings.size());
    assertEquals(null, bearings.get(0));
    assertEquals(bearing, bearings.get(1));
    assertEquals(null, bearings.get(2));
    assertEquals(null, bearings.get(3));
  }

  @Test
  public void bearingsList() {
    List<Double> bearing1 = new ArrayList<>();
    bearing1.add(1.1);
    bearing1.add(2.2);

    List<Double> bearing2 = new ArrayList<>();
    bearing2.add(7.7);
    bearing2.add(8.8);

    List<List<Double>> bearings = new ArrayList<>();
    bearings.add(null);
    bearings.add(null);
    bearings.add(bearing1);
    bearings.add(bearing2);
    bearings.add(null);

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .bearingsList(bearings)
        .build();

    assertEquals(bearings, routeOptions.bearingsList());
    assertEquals(";;1.1,2.2;7.7,8.8;", routeOptions.bearings());
  }

  @Test
  public void approachesString() {
    String approachesStr = ";" + APPROACH_CURB + ";" + ";" + APPROACH_UNRESTRICTED + ";";

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .approaches(approachesStr)
        .build();

    assertEquals(approachesStr, routeOptions.approaches());

    List<String> approaches = routeOptions.approachesList();
    assertEquals(5, approaches.size());
    assertEquals(null, approaches.get(0));
    assertEquals(APPROACH_CURB, approaches.get(1));
    assertEquals(null, approaches.get(2));
    assertEquals(APPROACH_UNRESTRICTED, approaches.get(3));
    assertEquals(null, approaches.get(4));
  }

  @Test
  public void approachesList() {
    List<String> approaches = new ArrayList<>();
    approaches.add(APPROACH_CURB);
    approaches.add(null);
    approaches.add(null);
    approaches.add(APPROACH_UNRESTRICTED);
    approaches.add(APPROACH_UNRESTRICTED);
    approaches.add(null);
    approaches.add(null);
    approaches.add(APPROACH_CURB);
    approaches.add(null);

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .approachesList(approaches)
        .build();

    assertEquals(approaches, routeOptions.approachesList());
    assertEquals("curb;;;unrestricted;unrestricted;;;curb;", routeOptions.approaches());
  }

  @Test
  public void waypointIndicesString() {
    String indicesStr = "1;4;5;7;8";

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .waypointIndices(indicesStr)
        .build();

    assertEquals("1;4;5;7;8", routeOptions.waypointIndices());

    List<Integer> indices = routeOptions.waypointIndicesList();
    assertEquals(5, indices.size());
    assertEquals(Integer.valueOf(1), indices.get(0));
    assertEquals(Integer.valueOf(4), indices.get(1));
    assertEquals(Integer.valueOf(5), indices.get(2));
    assertEquals(Integer.valueOf(7), indices.get(3));
    assertEquals(Integer.valueOf(8), indices.get(4));
  }

  @Test
  public void waypointIndicesList() {
    List<Integer> indices = new ArrayList<>();
    indices.add(1);
    indices.add(5);
    indices.add(7);

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .waypointIndicesList(indices)
        .build();

    assertEquals(indices, routeOptions.waypointIndicesList());
    assertEquals("1;5;7", routeOptions.waypointIndices());
  }

  @Test
  public void waypointNamesString() {
    String namesStr = "ab;;;cd;ef;;;gh;ij";

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .waypointNames(namesStr)
        .build();

    assertEquals(namesStr, routeOptions.waypointNames());

    List<String> names = routeOptions.waypointNamesList();
    assertEquals("ab", names.get(0));
    assertEquals(null, names.get(1));
    assertEquals(null, names.get(2));
    assertEquals("cd", names.get(3));
    assertEquals("ef", names.get(4));
    assertEquals(null, names.get(5));
    assertEquals(null, names.get(6));
    assertEquals("gh", names.get(7));
    assertEquals("ij", names.get(8));
  }

  @Test
  public void waypointNamesList() {
    List<String> names = new ArrayList<>();
    names.add(null);
    names.add(null);
    names.add("abc");
    names.add(null);
    names.add("def");
    names.add(null);
    names.add(null);

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .waypointNamesList(names)
        .build();

    assertEquals(names, routeOptions.waypointNamesList());
    assertEquals(";;abc;;def;;", routeOptions.waypointNames());
  }

  @Test
  public void waypointTargetsString() {
    String targetsStr = "1.2,3.4;;;5.65,7.123;;;";

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .waypointTargets(targetsStr)
        .build();

    assertEquals("1.2,3.4;;;5.65,7.123;;;", routeOptions.waypointTargets());

    List<Point> targets = routeOptions.waypointTargetsList();
    assertEquals(7, targets.size());
    assertEquals(Point.fromLngLat(1.2, 3.4), targets.get(0));
    assertEquals(null, targets.get(1));
    assertEquals(null, targets.get(2));
    assertEquals(Point.fromLngLat(5.65, 7.123), targets.get(3));
    assertEquals(null, targets.get(4));
    assertEquals(null, targets.get(5));
    assertEquals(null, targets.get(6));
  }

  @Test
  public void waypointTargetsList() {
    List<Point> targets = new ArrayList<>();
    targets.add(null);
    targets.add(null);
    targets.add(Point.fromLngLat(5.55, 7.77));
    targets.add(Point.fromLngLat(1.22, 3.44));
    targets.add(null);
    targets.add(Point.fromLngLat(1.55, 8.99));

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .waypointTargetsList(targets)
        .build();

    assertEquals(targets, routeOptions.waypointTargetsList());
    assertEquals(";;5.55,7.77;1.22,3.44;;1.55,8.99", routeOptions.waypointTargets());
  }

  @Test
  public void annotationsString() {
    String annotationsStr = ANNOTATION_MAXSPEED + "," + ANNOTATION_DURATION;

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .annotations(annotationsStr)
        .build();

    assertEquals(annotationsStr, routeOptions.annotations());

    List<String> annotations = routeOptions.annotationsList();
    assertEquals(2, annotations.size());
    assertEquals(ANNOTATION_MAXSPEED, annotations.get(0));
    assertEquals(ANNOTATION_DURATION, annotations.get(1));
  }

  @Test
  public void annotationsList() {
    List<String> annotations = new ArrayList<>();
    annotations.add(ANNOTATION_CONGESTION);
    annotations.add(ANNOTATION_DISTANCE);
    annotations.add(ANNOTATION_MAXSPEED);
    annotations.add(ANNOTATION_SPEED);

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .annotationsList(annotations)
        .build();

    assertEquals(annotations, routeOptions.annotationsList());
    assertEquals("congestion,distance,maxspeed,speed", routeOptions.annotations());
  }

  @Test
  public void snappingClosuresString() {
    String snappingClosuresString = "true;;;false;false;true;;;;" ;

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .snappingClosures(snappingClosuresString)
        .build();

    assertEquals(snappingClosuresString, routeOptions.snappingClosures());

    List<Boolean> snappingClosures = routeOptions.snappingClosuresList();
    assertEquals(10, snappingClosures.size());
    assertEquals(true, snappingClosures.get(0));
    assertEquals(null, snappingClosures.get(1));
    assertEquals(null, snappingClosures.get(2));
    assertEquals(false, snappingClosures.get(3));
    assertEquals(false, snappingClosures.get(4));
    assertEquals(true, snappingClosures.get(5));
    assertEquals(null, snappingClosures.get(6));
    assertEquals(null, snappingClosures.get(7));
    assertEquals(null, snappingClosures.get(8));
    assertEquals(null, snappingClosures.get(9));
  }

  @Test
  public void snappingClosuresEmptyString() {
    String snappingClosuresString = "" ;

    RouteOptions routeOptions = routeOptions()
            .toBuilder()
            .snappingClosures(snappingClosuresString)
            .build();

    assertEquals(snappingClosuresString, routeOptions.snappingClosures());

    List<Boolean> snappingClosures = routeOptions.snappingClosuresList();
    assertTrue(snappingClosures.isEmpty());
  }

  @Test
  public void snappingClosuresList() {
    List<Boolean> snappingClosures = new ArrayList<>();
    snappingClosures.add(false);
    snappingClosures.add(false);
    snappingClosures.add(null);
    snappingClosures.add(true);
    snappingClosures.add(false);
    snappingClosures.add(null);
    snappingClosures.add(null);

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .snappingClosures(snappingClosures)
        .build();

    assertEquals(snappingClosures, routeOptions.snappingClosuresList());
    assertEquals("false;false;;true;false;;", routeOptions.snappingClosures());
  }

  @Test
  public void snappingClosuresEmptyList() {
    List<Boolean> snappingClosures = new ArrayList<>();

    RouteOptions routeOptions = routeOptions()
            .toBuilder()
            .snappingClosures(snappingClosures)
            .build();

    assertEquals(snappingClosures, routeOptions.snappingClosuresList());
    assertTrue(routeOptions.snappingClosures().isEmpty());
  }

  @Test
  public void baseUrlIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("https://api.mapbox.com", routeOptions.baseUrl());
  }

  @Test
  public void userIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("mapbox", routeOptions.user());
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

    assertEquals(3, routeOptions.radiusesList().size());
    assertEquals(null, routeOptions.radiusesList().get(0));
    assertEquals(Double.valueOf(Double.POSITIVE_INFINITY), routeOptions.radiusesList().get(1));
    assertEquals(Double.valueOf(100.0), routeOptions.radiusesList().get(2));
  }

  @Test
  public void bearingsAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("0,90;90,0;", routeOptions.bearings());
  }

  @Test
  public void bearingsListIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(3, routeOptions.bearingsList().size());
    assertEquals(Double.valueOf(0), routeOptions.bearingsList().get(0).get(0));
    assertEquals(Double.valueOf(90), routeOptions.bearingsList().get(0).get(1));
    assertEquals(Double.valueOf(90), routeOptions.bearingsList().get(1).get(0));
    assertEquals(Double.valueOf(0), routeOptions.bearingsList().get(1).get(1));
    assertEquals(null, routeOptions.bearingsList().get(2));
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

    assertEquals(3, routeOptions.annotationsList().size());
    assertEquals("congestion", routeOptions.annotationsList().get(0));
    assertEquals("distance", routeOptions.annotationsList().get(1));
    assertEquals("duration", routeOptions.annotationsList().get(2));
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

    assertEquals(3, routeOptions.approachesList().size());
    assertEquals(null, routeOptions.approachesList().get(0));
    assertEquals("curb", routeOptions.approachesList().get(1));
    assertEquals(null, routeOptions.approachesList().get(2));
  }

  @Test
  public void waypointIndicesStringIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals("0;1;2", routeOptions.waypointIndices());
  }

  @Test
  public void waypointIndicesListIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(3, routeOptions.waypointIndicesList().size());
    assertEquals(Integer.valueOf(0), routeOptions.waypointIndicesList().get(0));
    assertEquals(Integer.valueOf(1), routeOptions.waypointIndicesList().get(1));
    assertEquals(Integer.valueOf(2), routeOptions.waypointIndicesList().get(2));
  }

  @Test
  public void waypointNamesStringIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(";two;", routeOptions.waypointNames());
  }

  @Test
  public void waypointNamesListIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(3, routeOptions.waypointNamesList().size());
    assertEquals(null, routeOptions.waypointNamesList().get(0));
    assertEquals("two", routeOptions.waypointNamesList().get(1));
    assertEquals(null, routeOptions.waypointNamesList().get(2));
  }

  @Test
  public void waypointTargetsStringIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(";12.2,21.2;", options.waypointTargets());
  }

  @Test
  public void waypointTargetsListIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(3, options.waypointTargetsList().size());
    assertEquals(null, options.waypointTargetsList().get(0));
    assertEquals(Point.fromLngLat(12.2, 21.2), options.waypointTargetsList().get(1));
    assertEquals(null, options.waypointTargetsList().get(2));
  }

  @Test
  public void snappingIncludeClosuresStringIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    assertEquals(";false;true", options.snappingClosures());
  }

  @Test
  public void snappingIncludeClosuresListIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(ROUTE_OPTIONS_JSON);

    List list = options.snappingClosuresList();
    assertEquals(3, list.size());
    assertEquals(null, list.get(0));
    assertEquals(false, list.get(1));
    assertEquals(true, list.get(2));
  }

  @Test
  public void routeOptions_toJson() {
    RouteOptions options = routeOptions();

    assertEquals(ROUTE_OPTIONS_JSON, options.toJson());
  }

  @Test
  public void radiusesList_toJson() {
    RouteOptions options = routeOptions().toBuilder()
        .radiuses("")
        .build();

    List<Double> radiuses = new ArrayList<>();
    radiuses.add(null);
    radiuses.add(Double.POSITIVE_INFINITY);
    radiuses.add(100.0);

    RouteOptions finalOptions = options.toBuilder()
        .radiusesList(radiuses)
        .build();

    assertEquals(ROUTE_OPTIONS_JSON, finalOptions.toJson());
  }

  @Test
  public void bearingsList_toJson() {
    RouteOptions options = routeOptions().toBuilder()
        .bearings("")
        .build();

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

    RouteOptions finalOptions = options.toBuilder()
        .bearingsList(bearings)
        .build();

    assertEquals(ROUTE_OPTIONS_JSON, finalOptions.toJson());
  }

  @Test
  public void annotationsList_toJson() {
    RouteOptions options = routeOptions().toBuilder()
        .annotations("")
        .build();

    List<String> annotations = new ArrayList<>();
    annotations.add("congestion");
    annotations.add("distance");
    annotations.add("duration");

    RouteOptions finalOptions = options.toBuilder()
        .annotationsList(annotations)
        .build();

    assertEquals(ROUTE_OPTIONS_JSON, finalOptions.toJson());
  }

  @Test
  public void approachesList_toJson() {
    RouteOptions options = routeOptions().toBuilder()
        .approaches("")
        .build();

    List<String> approaches = new ArrayList<>();
    approaches.add(null);
    approaches.add("curb");
    approaches.add(null);

    RouteOptions finalOptions = options.toBuilder()
        .approachesList(approaches)
        .build();

    assertEquals(ROUTE_OPTIONS_JSON, finalOptions.toJson());
  }

  @Test
  public void waypointIndicesList_toJson() {
    RouteOptions options = routeOptions().toBuilder()
        .waypointIndices("")
        .build();

    List<Integer> waypoints = new ArrayList<>();
    waypoints.add(0);
    waypoints.add(1);
    waypoints.add(2);

    RouteOptions finalOptions = options.toBuilder()
        .waypointIndicesList(waypoints)
        .build();

    assertEquals(ROUTE_OPTIONS_JSON, finalOptions.toJson());
  }

  @Test
  public void waypointNamesList_toJson() {
    RouteOptions options = routeOptions().toBuilder()
        .waypointNames("")
        .build();

    List<String> waypointNames = new ArrayList<>();
    waypointNames.add(null);
    waypointNames.add("two");
    waypointNames.add(null);

    RouteOptions finalOptions = options.toBuilder()
        .waypointNamesList(waypointNames)
        .build();

    assertEquals(ROUTE_OPTIONS_JSON, finalOptions.toJson());
  }

  @Test
  public void waypointTargetsList_toJson() {
    RouteOptions options = routeOptions().toBuilder()
        .waypointTargets("")
        .build();

    List<Point> waypointTargets = new ArrayList<>();
    waypointTargets.add(null);
    waypointTargets.add(Point.fromLngLat(12.2, 21.2));
    waypointTargets.add(null);

    RouteOptions finalOptions = options.toBuilder()
        .waypointTargetsList(waypointTargets)
        .build();

    assertEquals(ROUTE_OPTIONS_JSON, finalOptions.toJson());
  }

  private RouteOptions routeOptions() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-122.4003312, 37.7736941));
    coordinates.add(Point.fromLngLat(-122.4187529, 37.7689715));
    coordinates.add(Point.fromLngLat(-122.4255172, 37.7775835));

    return RouteOptions.builder()
        .baseUrl("https://api.mapbox.com")
        .user("mapbox")
        .profile("driving-traffic")
        .coordinates(coordinates)
        .alternatives(false)
        .language("ru")
        .radiuses(";unlimited;100")
        .bearings("0,90;90,0;")
        .continueStraight(false)
        .roundaboutExits(false)
        .geometries("polyline6")
        .overview("full")
        .steps(true)
        .annotations("congestion,distance,duration")
        .exclude("toll")
        .voiceInstructions(true)
        .bannerInstructions(true)
        .voiceUnits("metric")
        .accessToken("token")
        .requestUuid("12345543221")
        .approaches(";curb;")
        .waypointIndices("0;1;2")
        .waypointNames(";two;")
        .waypointTargets(";12.2,21.2;")
        .snappingClosures(";false;true")
        .build();
  }
}
