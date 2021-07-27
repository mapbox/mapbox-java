package com.mapbox.api.directions.v5.models;

import static com.google.gson.JsonParser.parseString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class RouteOptionsTest extends TestUtils {

  /**
   * Always update this file when new option is introduced.
   */
  private static final String ROUTE_OPTIONS_JSON = "route_options_v5.json";

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
    assertEquals(0.0, routeOptions.bearingsList().get(0).angle(), 0.00001);
    assertEquals(90.0, routeOptions.bearingsList().get(0).degrees(), 0.00001);
    assertEquals(90.0, routeOptions.bearingsList().get(1).angle(), 0.00001);
    assertEquals(0.0, routeOptions.bearingsList().get(1).degrees(), 0.00001);
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
      .bearingsList(new ArrayList<Bearing>() {{
        add(Bearing.builder().angle(0.0).degrees(90.0).build());
        add(Bearing.builder().angle(90.0).degrees(0.0).build());
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
      .user(DirectionsCriteria.PROFILE_DEFAULT_USER)
      .enableRefresh(true)
      .build();
  }
}
