package com.mapbox.api.directions.v5.models;

import com.mapbox.geojson.Point;
import java.util.ArrayList;
import java.util.List;
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

    assertEquals(";5.1;;7.4", routeOptions.radiuses());

    List<Double> radiuses = routeOptions.radiusesList();
    assertEquals(4, radiuses.size());
    assertEquals(null, radiuses.get(0));
    assertEquals(Double.valueOf(5.1), radiuses.get(1));
    assertEquals(null, radiuses.get(2));
    assertEquals(Double.valueOf(7.4), radiuses.get(3));
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

    assertEquals(";" + APPROACH_CURB + ";" + ";" + APPROACH_UNRESTRICTED,
        routeOptions.approaches());

    List<String> approaches = routeOptions.approachesList();
    assertEquals(4, approaches.size());
    assertEquals(null, approaches.get(0));
    assertEquals(APPROACH_CURB, approaches.get(1));
    assertEquals(null, approaches.get(2));
    assertEquals(APPROACH_UNRESTRICTED, approaches.get(3));
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

    assertEquals("1.2,3.4;;;5.65,7.123", routeOptions.waypointTargets());

    List<Point> targets = routeOptions.waypointTargetsList();
    assertEquals(4, targets.size());
    assertEquals(Point.fromLngLat(1.2, 3.4), targets.get(0));
    assertEquals(null, targets.get(1));
    assertEquals(null, targets.get(2));
    assertEquals(Point.fromLngLat(5.65, 7.123), targets.get(3));
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
    String annotationsStr = ANNOTATION_MAXSPEED + ";" + ANNOTATION_DURATION + ";" + ";";

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .annotations(annotationsStr)
        .build();

    assertEquals(ANNOTATION_MAXSPEED + ";" + ANNOTATION_DURATION, routeOptions.annotations());

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
    annotations.add(null);
    annotations.add(null);

    RouteOptions routeOptions = routeOptions()
        .toBuilder()
        .annotationsList(annotations)
        .build();

    assertEquals(annotations, routeOptions.annotationsList());
    assertEquals("congestion;distance;maxspeed;speed", routeOptions.annotations());
  }

  private RouteOptions routeOptions() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(1.0, 2.0));
    coordinates.add(Point.fromLngLat(3.0, 4.0));

    return RouteOptions.builder()
        .accessToken("token")
        .baseUrl("base_url")
        .coordinates(coordinates)
        .user("user")
        .profile("profile")
        .requestUuid("requestUuid")
        .build();
  }
}
