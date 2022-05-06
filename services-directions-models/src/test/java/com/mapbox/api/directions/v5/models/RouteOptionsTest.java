package com.mapbox.api.directions.v5.models;

import static com.google.gson.JsonParser.parseString;
import static com.mapbox.api.directions.v5.utils.Asserts.assertContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.utils.MutateJsonUtil;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class RouteOptionsTest extends TestUtils {
  /**
   * Always update this file when new option is introduced.
   */
  private static final String ROUTE_OPTIONS_JSON = "route_options_v5.json";
  private static final String ROUTE_OPTIONS_URL =
    "https://api.mapbox.com/directions/v5/mapbox/driving/-122.4003312,37.7736941;-122.4187529,37.7689715;-122.4255172,37.7775835?access_token=pk.token&geometries=polyline6&alternatives=false&overview=full&radiuses=%3Bunlimited%3B5.1&steps=true&avoid_maneuver_radius=200.0&bearings=0%2C90%3B90%2C0%3B&layers=-42%3B%3B0&continue_straight=false&annotations=congestion%2Cdistance%2Cduration&language=ru&roundabout_exits=false&voice_instructions=true&banner_instructions=true&voice_units=metric&exclude=toll%2Cferry%2Cpoint%2811.0+-22.0%29&include=hot%2Chov2&approaches=%3Bcurb%3B&waypoints=0%3B1%3B2&waypoint_names=%3BSerangoon+Garden+Market+%26+Food+Centre%3BFunky+%26nAmE*&waypoint_targets=%3B12.2%2C21.2%3B&enable_refresh=true&walking_speed=5.11&walkway_bias=-0.2&alley_bias=0.75&snapping_include_closures=%3Bfalse%3Btrue&arrive_by=2021-01-01%27T%2701%3A01&depart_at=2021-02-02%27T%2702%3A02&max_height=1.5&max_width=1.4&metadata=true";
  private static final String ACCESS_TOKEN = "pk.token";

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

    assertEquals(DirectionsCriteria.PROFILE_DRIVING, routeOptions.profile());
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
  public void layersAreValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals("-42;;0", routeOptions.layers());
  }

  @Test
  public void layersListIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    List<Integer> expected = new ArrayList<Integer>();
    Collections.addAll(expected, -42, null, 0);

    assertEquals(routeOptions.layersList().size(), expected.size());
    for (int i = 0; i < expected.size(); ++i) {
      if (expected.get(i) != null) {
        assertEquals(expected.get(i), routeOptions.layersList().get(i));
      } else {
        assertNull(routeOptions.layersList().get(i));
      }
    }
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

    assertEquals("toll,ferry,point(11.0 -22.0)", routeOptions.exclude());
  }

  @Test
  public void includeIsValid_fromJson() {
    RouteOptions routeOptions = RouteOptions.fromJson(optionsJson);

    assertEquals("hot,hov2", routeOptions.include());
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

    assertEquals(
      ";Serangoon Garden Market & Food Centre;Funky &nAmE*",
      routeOptions.waypointNames()
    );
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
  public void maxHeightIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(optionsJson);

    assertEquals(1.5, options.maxHeight(), 0.000001);
  }

  @Test
  public void maxWidthIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(optionsJson);

    assertEquals(1.4, options.maxWidth(), 0.000001);
  }

  @Test
  public void enableRefreshIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(optionsJson);

    assertEquals(true, options.enableRefresh());
  }

  @Test
  public void metadataIsValid_fromJson() {
    RouteOptions options = RouteOptions.fromJson(optionsJson);

    assertEquals(true, options.metadata());
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
  public void routeOptions_toUrl_fromUrl() {
    RouteOptions options = routeOptions();
    URL url = options.toUrl(ACCESS_TOKEN);
    RouteOptions optionsFromUrl = RouteOptions.fromUrl(url);

    assertEquals(options, optionsFromUrl);
  }

  @Test
  public void routeOptionsList_toUrl_fromUrl() {
    RouteOptions options = routeOptionsList();
    URL url = options.toUrl(ACCESS_TOKEN);
    RouteOptions optionsFromUrl = RouteOptions.fromUrl(url);

    assertEquals(options, optionsFromUrl);
  }

  @Test
  public void routeOptions_toUrl() {
    RouteOptions options = routeOptions();
    URL url = options.toUrl(ACCESS_TOKEN);

    assertEquals(ROUTE_OPTIONS_URL, url.toString());
  }

  @Test
  public void routeOptionsWithDefaults_toUrl() {
    String expectedUrl =
      "https://api.mapbox.com/directions/v5/mapbox/driving/-122.4003312,37.7736941;-122.4187529,37.7689715?access_token=pk.token&geometries=polyline6";
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-122.4003312, 37.7736941));
    coordinates.add(Point.fromLngLat(-122.4187529, 37.7689715));

    RouteOptions options = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .coordinatesList(coordinates)
      .build();

    URL url = options.toUrl(ACCESS_TOKEN);

    assertEquals(expectedUrl, url.toString());
  }

  @Test
  public void routeOptionsWithPort_roundtripping() {
    String expectedUrl =
      "https://api.mapbox.com:12345/directions/v5/mapbox/driving/-122.4003312,37.7736941;-122.4187529,37.7689715?access_token=pk.token&geometries=polyline6";
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-122.4003312, 37.7736941));
    coordinates.add(Point.fromLngLat(-122.4187529, 37.7689715));

    RouteOptions options = RouteOptions.builder()
      .baseUrl("https://api.mapbox.com:12345")
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .coordinatesList(coordinates)
      .build();

    URL url = options.toUrl(ACCESS_TOKEN);
    assertEquals(expectedUrl, url.toString());

    RouteOptions recreatedOptions = RouteOptions.fromUrl(url);
    assertEquals(options, recreatedOptions);
  }

  @Test
  public void baseUrlWithLastSlash() {
    String expectedUrl =
      "https://mapbox.com/directions/v5/mapbox/driving/-122.4003312,37.7736941;-122.4187529,37.7689715?access_token=pk.token&geometries=polyline6";
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-122.4003312, 37.7736941));
    coordinates.add(Point.fromLngLat(-122.4187529, 37.7689715));

    RouteOptions options = RouteOptions.builder()
      .baseUrl("https://mapbox.com/")
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .coordinatesList(coordinates)
      .build();

    URL url = options.toUrl(ACCESS_TOKEN);

    assertEquals(expectedUrl, url.toString());
  }

  @Test
  public void routeOptionsWithDecodedChars_toUrlWithEncodedChars() {
    String expectedEncodedUrl =
      "https://api.mapbox.com/directions/v5/mapbox/driving/-122.4003312,37.7736941;-122.4187529,37.7689715?access_token=pk.token&geometries=polyline6&waypoint_names=%3BFunky+%26nAmE*";
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-122.4003312, 37.7736941));
    coordinates.add(Point.fromLngLat(-122.4187529, 37.7689715));

    RouteOptions options = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .coordinatesList(coordinates)
      .waypointNames(";Funky &nAmE*")
      .build();

    URL url = options.toUrl(ACCESS_TOKEN);

    assertEquals(expectedEncodedUrl, url.toString());
  }

  @Test
  public void routeOptions_toUrl_fromUrl_withEncodedChars() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-122.4003312, 37.7736941));
    coordinates.add(Point.fromLngLat(-122.4187529, 37.7689715));

    RouteOptions expectedOptions = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .coordinatesList(coordinates)
      .waypointNames("Funky &nAmE*;my destination")
      .build();

    URL url = expectedOptions.toUrl(ACCESS_TOKEN);

    RouteOptions resultingOptions = RouteOptions.fromUrl(url);

    assertEquals(expectedOptions, resultingOptions);
  }

  @Test
  public void routeOptions_fromUrl_withEncodedCoordinates() throws MalformedURLException {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-122.4003312, 37.7736941));
    coordinates.add(Point.fromLngLat(-122.4187529, 37.7689715));

    RouteOptions expectedOptions = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .coordinatesList(coordinates)
      .waypointNames("my starting position;my destination")
      .build();

    String url =
      "https://api.mapbox.com/directions/v5/mapbox/driving/-122.4003312%2C37.7736941;-122.4187529%2C37.7689715?access_token=pk.token&geometries=polyline6&waypoint_names=my%20starting%20position;my%20destination";

    RouteOptions resultingOptions = RouteOptions.fromUrl(new URL(url));

    assertEquals(expectedOptions, resultingOptions);
  }

  @Test
  public void routeOptionsWithUTF8Chars_toUrlWithEncodedChars() {
    String expectedEncodedUrl =
      "https://api.mapbox.com/directions/v5/mapbox/driving/-122.4003312,37.7736941;-122.4187529,37.7689715?access_token=pk.token&geometries=polyline6&waypoint_names=%3B%D0%A3%D0%BB%D0%B8%D1%86%D0%B0+%D0%AF%D0%BD%D0%B0+%D0%A7%D0%B5%D1%87%D0%BE%D1%82%D0%B0+7%2C+Minsk+220045%2C+Belarus";
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-122.4003312, 37.7736941));
    coordinates.add(Point.fromLngLat(-122.4187529, 37.7689715));

    RouteOptions options = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .coordinatesList(coordinates)
      .waypointNames(";Улица Яна Чечота 7, Minsk 220045, Belarus")
      .build();

    URL url = options.toUrl(ACCESS_TOKEN);

    assertEquals(expectedEncodedUrl, url.toString());
  }

  @Test
  public void routeOptionWithExcludedPoint() {
    RouteOptions options = RouteOptions.builder()
      .coordinates("1.0,1.0;2.0,2.0")
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .excludeObject(
        Exclude.builder()
          .points(Arrays.asList(Point.fromLngLat(1.0, 2.0)))
          .build()
      )
      .build();

    URL url = options.toUrl("testToken");

    List<String> queryParameters = Arrays.asList(url.getQuery().split("&"));
    assertTrue(
      "url doesn't contain excluded point: " + url,
      queryParameters.contains("exclude=point%281.0+2.0%29")
    );
  }

  @Test
  public void routeOptionWithTwoExcludedPoints() {
    RouteOptions options = RouteOptions.builder()
      .coordinates("1.0,1.0;2.0,2.0")
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .excludeObject(
        Exclude.builder()
          .points(
            Arrays.asList(
              Point.fromLngLat(1.0, 2.0),
              Point.fromLngLat(6.03, 8.07)
            )
          )
          .build()
      )
      .build();

    URL url = options.toUrl("testToken");

    List<String> queryParameters = Arrays.asList(url.getQuery().split("&"));
    assertTrue(
      "url doesn't contain excluded point: " + url,
      queryParameters.contains("exclude=point%281.0+2.0%29%2Cpoint%286.03+8.07%29")
    );
  }

  @Test
  public void routeOptionsWithExcludePointAndCriteriaFromUrl() throws MalformedURLException {
    URL testUrl = new URL(
      "https://api.mapbox.com/directions/v5/mapbox/driving-traffic/18.60289583391497,54.41121871390118;18.59400217318438,54.40983705017376.json?access_token=testToken&alternatives=true&annotations=distance%2Cduration%2Ccongestion%2Cspeed&geometries=geojson&language=en&overview=full&steps=true&exclude=point(18.595875353791087%2054.41000119108463),toll");

    RouteOptions routeOptions = RouteOptions.fromUrl(testUrl);

    List<Point> excludedPoints = routeOptions.excludeObject().points();
    assertNotNull(excludedPoints);
    assertEquals(1, excludedPoints.size());
    Point excludedPoint = excludedPoints.get(0);
    assertEquals(18.595875353791087, excludedPoint.longitude(), 0.001);
    assertEquals(54.41000119108463, excludedPoint.latitude(), 0.001);
    assertEquals(
      Arrays.asList(DirectionsCriteria.EXCLUDE_TOLL),
      routeOptions.excludeObject().criteria()
    );
  }

  @Test
  public void routeOptionsWithExcludePointToJson() {
    RouteOptions options = RouteOptions.builder()
      .coordinates("1.0,1.0;2.0,2.0")
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .excludeObject(
        Exclude.builder()
          .points(Arrays.asList(
            Point.fromLngLat(11.0, 22.0)
            )
          ).build()
      )
      .build();

    String json = options.toJson();

    String exclude = JsonParser.parseString(json)
      .getAsJsonObject()
      .get("exclude")
      .getAsString();
    assertEquals("point(11.0 22.0)", exclude);
  }

  @Test
  public void routeOptionsWithMixedExcludePointsAndExcludeList() {
    Exclude excludeObject = Exclude.builder()
      .criteria(
        Arrays.asList(
          DirectionsCriteria.EXCLUDE_FERRY
        )
      )
      .points(
        Arrays.asList(
          Point.fromLngLat(19.4567, -55.5677)
        )
      )
      .build();
    RouteOptions options = RouteOptions.builder()
      .coordinates("1.0,1.0;2.0,2.0")
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .excludeObject(excludeObject)
      .build();

    String rawExclude = options.exclude();

    assertTrue(
      "exclude doesn't contain point. exclude: " + rawExclude,
      rawExclude.contains("point(19.4567 -55.5677)")
    );
    assertTrue(
      "exclude doesn't contain criteria. exclude: " + rawExclude,
      rawExclude.contains(DirectionsCriteria.EXCLUDE_FERRY)
    );
    assertEquals(excludeObject, options.excludeObject());
  }

  @Test
  public void cleanExcludes() {
    RouteOptions routeOptions = routeOptions().toBuilder()
      .excludeObject(null)
      .build();

    assertNull(routeOptions.exclude());
  }

  @Test
  public void putCustomRouteOptionsParamsToUrl() {
    RouteOptions routeOptions = routeOptions().toBuilder()
      .unrecognizedProperties(new HashMap<String, String>() {{
        put("testName", "testValue");
        put("testName2", "true");
      }})
      .build();

    URL url = routeOptions.toUrl("test");
    String query = url.getQuery();

    assertContains(query, "testName=testValue");
    assertContains(query, "testName2=true");
  }

  @Test
  public void nullCustomRouteOptions() {
    RouteOptions routeOptions = routeOptions().toBuilder()
      .unrecognizedProperties(null)
      .build();
    assertEquals(0, routeOptions.getUnrecognizedPropertiesNames().size());
  }

  @Test
  public void readCustomRouteOptionsParamsFromUrl() throws MalformedURLException {
    String url = ROUTE_OPTIONS_URL + "&testString=test&testNumber=4.9&testBoolean=true";

    RouteOptions routeOptions = RouteOptions.fromUrl(new URL(url));

    Set<String> unrecognizedProperties = routeOptions.getUnrecognizedPropertiesNames();
    assertContains(unrecognizedProperties, "testString");
    assertEquals("test", routeOptions.getUnrecognizedProperty("testString").getAsString());
    assertContains(unrecognizedProperties, "testNumber");
    assertEquals(4.9, routeOptions.getUnrecognizedProperty("testNumber").getAsDouble(), 0);
    assertContains(unrecognizedProperties, "testBoolean");
    assertTrue(routeOptions.getUnrecognizedProperty("testBoolean").getAsBoolean());
  }

  @Test
  public void unrecognizedOptionsFromJsonToUrl() {
    RouteOptions sourceRouteOptions = routeOptions().toBuilder()
      .unrecognizedProperties(new LinkedHashMap<String, String>() {{
        put("test1", "1");
        put("test2", "2");
      }})
      .build();

    RouteOptions routeOptions = RouteOptions.fromUrl(sourceRouteOptions.toUrl("testToken"));

    assertEquals(sourceRouteOptions, routeOptions);
  }

  @Test
  public void allUnrecognizedPropertiesStaysAfterTransformingModelToJsonAndBack()
    throws IOException {
    Gson gson = new GsonBuilder().create();
    JsonObject mutatedRouteOptionsJson =
      gson.fromJson(loadJsonFixture(ROUTE_OPTIONS_JSON), JsonObject.class);
    MutateJsonUtil.mutateJson(mutatedRouteOptionsJson);

    RouteOptions routeOptions = RouteOptions.fromJson(mutatedRouteOptionsJson.toString());
    JsonObject deserializedRouteOptions = gson.fromJson(routeOptions.toJson(), JsonObject.class);

    assertEquals(mutatedRouteOptionsJson, deserializedRouteOptions);
  }

  @Test(expected = IllegalStateException.class)
  public void routeOptionsWithUnrecognizedObjectsAndArrays() throws IOException {
    Gson gson = new GsonBuilder().create();
    JsonObject mutatedRouteOptionsJson =
      gson.fromJson(loadJsonFixture(ROUTE_OPTIONS_JSON), JsonObject.class);
    MutateJsonUtil.mutateJson(mutatedRouteOptionsJson);

    RouteOptions.fromJson(mutatedRouteOptionsJson.toString()).toUrl("testToken");
  }

  @Test
  public void emptyExcludeObjectsCleansUpExcludes() {
    RouteOptions routeOptions = routeOptions().toBuilder()
      .excludeObject(
        Exclude.builder().build()
      )
      .build();

    assertNull(routeOptions.exclude());
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
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .coordinatesList(coordinates)
      .alternatives(false)
      .annotations("congestion,distance,duration")
      .bearings("0,90;90,0;")
      .avoidManeuverRadius(200.0)
      .layers("-42;;0")
      .continueStraight(false)
      .exclude(DirectionsCriteria.EXCLUDE_TOLL + "," + DirectionsCriteria.EXCLUDE_FERRY +
        ",point(11.0 -22.0)")
      .include(DirectionsCriteria.INCLUDE_HOT + "," + DirectionsCriteria.INCLUDE_HOV2)
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
      .waypointNames(";Serangoon Garden Market & Food Centre;Funky &nAmE*")
      .waypointTargets(";12.2,21.2;")
      .waypointIndices("0;1;2")
      .alleyBias(0.75)
      .walkingSpeed(5.11)
      .walkwayBias(-0.2)
      .arriveBy("2021-01-01'T'01:01")
      .departAt("2021-02-02'T'02:02")
      .maxHeight(1.5)
      .maxWidth(1.4)
      .snappingIncludeClosures(";false;true")
      .user(DirectionsCriteria.PROFILE_DEFAULT_USER)
      .enableRefresh(true)
      .metadata(true)
      .build();
  }

  /**
   * Fills up all the options using list variants. Values need to be equal to the ones in {@link #optionsJson}.
   */
  private RouteOptions routeOptionsList() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-122.4003312, 37.7736941));
    coordinates.add(Point.fromLngLat(-122.4187529, 37.7689715));
    coordinates.add(Point.fromLngLat(-122.4255172, 37.7775835));

    return RouteOptions.builder()
      .baseUrl("https://api.mapbox.com")
      .profile(DirectionsCriteria.PROFILE_DRIVING)
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
      .avoidManeuverRadius(200.0)
      .layersList(new ArrayList<Integer>() {{
        add(-42);
        add(null);
        add(0);
      }})
      .continueStraight(false)
      .excludeList(new ArrayList<String>() {{
        add(DirectionsCriteria.EXCLUDE_TOLL);
        add(DirectionsCriteria.EXCLUDE_FERRY);
        add("point(11.0 -22.0)");
      }})
      .includeList(new ArrayList<String>() {{
        add(DirectionsCriteria.INCLUDE_HOT);
        add(DirectionsCriteria.INCLUDE_HOV2);
      }})
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
        add("Serangoon Garden Market & Food Centre");
        add("Funky &nAmE*");
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
      .maxHeight(1.5)
      .maxWidth(1.4)
      .snappingIncludeClosuresList(new ArrayList<Boolean>() {{
        add(null);
        add(false);
        add(true);
      }})
      .user(DirectionsCriteria.PROFILE_DEFAULT_USER)
      .enableRefresh(true)
      .metadata(true)
      .build();
  }
}
