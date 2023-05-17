package com.mapbox.api.directions.v5.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mapbox.api.directions.v5.utils.MutateJsonUtil.mutateJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DirectionsResponseTest extends TestUtils {

  private static final String DIRECTIONS_V5_PRECISION6_FIXTURE = "directions_v5_precision_6.json";
  private static final String DIRECTIONS_V5_PRECISION6_FIXTURE_ARTIFICIAL_FIELDS = "directions_v5_precision_6_with_artificial_fields.json";
  private static final String DIRECTIONS_V5_MULTIPLE_ROUTES = "directions_v5_multiple_routes.json";
  private static final String DIRECTIONS_V5_MULTIPLE_ROUTES_WITH_OPTIONS =
    "directions_v5_multiple_routes_with_options.json";
  private static final String DIRECTIONS_V5_SILENT_WAYPOINT = "directions_v5_silent_waypoints.json";

  @Test
  public void sanity() throws Exception {
    Metadata metadata = Metadata.builder().infoMap(Collections.singletonMap("aaa", "bbb")).build();
    List<DirectionsWaypoint> waypoints = Arrays.asList(
      DirectionsWaypoint
        .builder()
        .rawLocation(new double[0])
        .distance(9.8)
        .name("aaa")
        .build()
    );
    List<DirectionsRoute> routes = Arrays.asList(
      DirectionsRoute
        .builder()
        .distance(1.2)
        .duration(7.8)
        .build()
    );
    Map<String, JsonElement> unrecognizedProperties = Collections.singletonMap("ccc", new JsonPrimitive("ddd"));
    DirectionsResponse response = DirectionsResponse.builder()
      .code("100")
      .routes(routes)
      .message("Message")
      .metadata(metadata)
      .uuid("uuid")
      .waypoints(waypoints)
      .unrecognizedJsonProperties(unrecognizedProperties)
      .build();

    assertNotNull(response);
   assertEquals("100", response.code());
   assertEquals("Message", response.message());
   assertEquals(1, response.routes().size());
   assertEquals(metadata, response.metadata());
   assertEquals("uuid", response.uuid());
   assertEquals(waypoints, response.waypoints());
   assertEquals(unrecognizedProperties, response.getUnrecognizedJsonProperties());
  }

  @Test
  public void fromJson_correctlyBuildsFromJson() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_PRECISION6_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);
    assertNotNull(response);
    assertEquals(1, response.routes().size());
  }

  @Test
  public void deserialization_from_reader() throws Exception {
    String testJsonFileName = DIRECTIONS_V5_PRECISION6_FIXTURE;
    InputStream jsonStream = getResourceInputSteam(testJsonFileName);
    String jsonText = loadJsonFixture(testJsonFileName);
    InputStreamReader reader = new InputStreamReader(jsonStream);

    DirectionsResponse responseFromReader = DirectionsResponse.fromJson(reader);
    DirectionsResponse responseFromText = DirectionsResponse.fromJson(jsonText);

    System.out.println("vadzim-test: " + responseFromText.routes().get(0).legs().get(0).steps().get(6).pronunciation());
    assertEquals(
      responseFromText.routes().get(0).legs().get(0).steps().get(6).pronunciation(),
      responseFromReader.routes().get(0).legs().get(0).steps().get(6).pronunciation()
    );
    assertEquals(responseFromText, responseFromReader);
  }

  @Test
  public void deserialization_from_reader_with_route_options() throws Exception {
    RouteOptions testRouteOptions = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .coordinatesList(new ArrayList<Point>() {{
        add(Point.fromLngLat(1.0, 1.0));
        add(Point.fromLngLat(2.0, 2.0));
      }})
      .build();
    String testJsonFileName = DIRECTIONS_V5_PRECISION6_FIXTURE;
    InputStream jsonStream = getResourceInputSteam(testJsonFileName);
    String jsonText = loadJsonFixture(testJsonFileName);
    InputStreamReader reader = new InputStreamReader(jsonStream);

    DirectionsResponse responseFromReader = DirectionsResponse.fromJson(reader, testRouteOptions);
    DirectionsResponse responseFromText = DirectionsResponse.fromJson(jsonText, testRouteOptions);

    assertEquals(responseFromText, responseFromReader);
  }

  @Test
  public void testToFromJsonWithRealResponse() throws Exception {
    Gson gson = new GsonBuilder().create();
    String originalJson = loadJsonFixture(DIRECTIONS_V5_PRECISION6_FIXTURE_ARTIFICIAL_FIELDS);
    JsonObject originalJsonObject = gson.fromJson(originalJson, JsonObject.class);

    DirectionsResponse model = DirectionsResponse.fromJson(originalJson);
    JsonObject jsonFromObject = gson.fromJson(model.toJson(), JsonObject.class);

    assertEquals(originalJsonObject, jsonFromObject);
  }

  @Test
  public void testToFromJsonWithMutatedResponse() throws Exception {
    Gson gson = new GsonBuilder().create();
    JsonObject mutatedJson = gson.fromJson(loadJsonFixture(DIRECTIONS_V5_PRECISION6_FIXTURE_ARTIFICIAL_FIELDS), JsonObject.class);
    mutateJson(mutatedJson);

    DirectionsResponse responseFromJson1 = DirectionsResponse.fromJson(mutatedJson.toString());
    JsonObject jsonFromObject = gson.fromJson(responseFromJson1.toJson(), JsonObject.class);

    assertEquals(mutatedJson, jsonFromObject);
  }

  @Test
  public void accessUnrecognizedProperties() throws Exception {
    JsonObject directionsResponseJson = readJsonObject(DIRECTIONS_V5_PRECISION6_FIXTURE_ARTIFICIAL_FIELDS);
    String unrecognizedPropertyName = "testUnrecognizedProperty";
    String unrecognizedPropertyValue = "test";
    directionsResponseJson.add(unrecognizedPropertyName, new JsonPrimitive(unrecognizedPropertyValue));
    DirectionsResponse response = DirectionsResponse.fromJson(directionsResponseJson.toString());

    String value = response.getUnrecognizedProperty(unrecognizedPropertyName).getAsString();
    JsonElement notExistingProperty = response.getUnrecognizedProperty("notExisting");

    assertEquals(unrecognizedPropertyValue, value);
    assertNull(notExistingProperty);
  }

  @Test
  public void accessUnrecognizedJsonProperties() throws Exception {
    JsonObject directionsResponseJson = readJsonObject(DIRECTIONS_V5_PRECISION6_FIXTURE_ARTIFICIAL_FIELDS);
    String unrecognizedPropertyName = "testUnrecognizedProperty";
    String unrecognizedPropertyValue = "test";
    directionsResponseJson.add(unrecognizedPropertyName, new JsonPrimitive(unrecognizedPropertyValue));
    Map<String, JsonElement> expected = new HashMap<>();
    expected.put(unrecognizedPropertyName, new JsonPrimitive(unrecognizedPropertyValue));
    DirectionsResponse response = DirectionsResponse.fromJson(directionsResponseJson.toString());

    Map<String, JsonElement> actual = response.getUnrecognizedJsonProperties();

    assertEquals(expected, actual);
  }

  @Test
  public void noUnrecognizedProperties() throws Exception {
    JsonObject directionsResponseJson = readJsonObject(DIRECTIONS_V5_PRECISION6_FIXTURE_ARTIFICIAL_FIELDS);
    DirectionsResponse response = DirectionsResponse.fromJson(directionsResponseJson.toString());

    JsonElement value = response.getUnrecognizedProperty("");
    Set<String> propertiesNames = response.getUnrecognizedPropertiesNames();
    Map<String, JsonElement> jsonProperties = response.getUnrecognizedJsonProperties();

    assertNull(value);
    assertEquals(0, propertiesNames.size());
    assertNull(jsonProperties);
  }

  @Test
  public void getUnrecognizedPropertiesNames() throws Exception {
    JsonObject directionsResponseJson = readJsonObject(DIRECTIONS_V5_PRECISION6_FIXTURE_ARTIFICIAL_FIELDS);
    directionsResponseJson.add("1", new JsonPrimitive(1));
    directionsResponseJson.add("2", new JsonPrimitive(2));
    directionsResponseJson.add("3", new JsonPrimitive(3));
    DirectionsResponse response = DirectionsResponse.fromJson(directionsResponseJson.toString());

    Set<String> properties = response.getUnrecognizedPropertiesNames();

    assertEquals(3, properties.size());
    assertTrue(properties.contains("1"));
    assertTrue(properties.contains("2"));
    assertTrue(properties.contains("3"));
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
  public void fromToJsonForRouteWithSilentWaypoints() throws IOException {
    String json = loadJsonFixture(DIRECTIONS_V5_SILENT_WAYPOINT);

    DirectionsResponse initial = DirectionsResponse.fromJson(json);
    String serialized = initial.toJson();
    DirectionsResponse deserialized = DirectionsResponse.fromJson(serialized);

    assertEquals(initial, deserialized);
  }

  private JsonObject readJsonObject(String file) throws IOException {
    Gson gson = new GsonBuilder().create();
    return gson.fromJson(loadJsonFixture(file), JsonObject.class);
  }
}
