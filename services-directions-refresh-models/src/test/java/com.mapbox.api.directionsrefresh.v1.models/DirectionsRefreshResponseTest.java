package com.mapbox.api.directionsrefresh.v1.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DirectionsRefreshResponseTest extends TestUtils {

  private static final String DIRECTIONS_REFRESH_V1 = "directions_refresh_v1.json";
  private static final String DIRECTIONS_REFRESH_V1_WAYPOINTS = "directions_refresh_v1_waypoints.json";

  @Test
  public void sanity() {
    DirectionsRouteRefresh route = DirectionsRouteRefresh.builder()
      .legs(Collections.<RouteLegRefresh>emptyList())
      .build();
    Map<String, JsonElement> unrecognizedProperties = Collections
      .singletonMap("key1", new JsonPrimitive("value1"));
    DirectionsRefreshResponse response = DirectionsRefreshResponse.builder()
      .code("200")
      .message("Message")
      .route(route)
      .unrecognizedJsonProperties(unrecognizedProperties)
      .build();

    assertEquals("200", response.code());
    assertEquals("Message", response.message());
    assertEquals(route, response.route());
    assertEquals(unrecognizedProperties, response.getUnrecognizedJsonProperties());
  }

  @Test
  public void testSerialization() throws IOException {
    DirectionsRefreshResponse directionsRefreshResponse =
      DirectionsRefreshResponse.fromJson(loadJsonFixture(DIRECTIONS_REFRESH_V1_WAYPOINTS));

    assertEquals(directionsRefreshResponse.code(), "Ok");
    assertNotNull(directionsRefreshResponse.route());
    assertNotNull(directionsRefreshResponse.route().legs());
    assertNotNull(directionsRefreshResponse.route().legs().get(0));
    assertNotNull(directionsRefreshResponse.route().legs().get(0).annotation());
    assertTrue(directionsRefreshResponse.route().legs().get(0).annotation().congestion().size() > 0);
    assertTrue(directionsRefreshResponse.route().legs().get(0).annotation().trafficTendency().size() > 0);
    assertTrue(directionsRefreshResponse.route().legs().get(0).incidents().size() > 0);
    assertTrue(directionsRefreshResponse.route().legs().get(0).closures().size() > 0);
  }
  @Test
  public void testSerializationDeserialization() throws IOException {
    String json = loadJsonFixture(DIRECTIONS_REFRESH_V1_WAYPOINTS);
    DirectionsRefreshResponse fromJson1 = DirectionsRefreshResponse.fromJson(json);

    String jsonFromObj = fromJson1.toJson();
    DirectionsRefreshResponse fromJson2 = DirectionsRefreshResponse.fromJson(jsonFromObj);

    assertEquals(fromJson1, fromJson2);
  }

  @Test
  public void accessUnrecognizedProperties() throws Exception {
    Map<String, JsonElement> expected = new HashMap<>();
    JsonObject directionsRefreshResponseJson = readJsonObject(DIRECTIONS_REFRESH_V1);
    String unrecognizedPropertyName = "testUnrecognizedProperty";
    String unrecognizedPropertyValue = "test";
    directionsRefreshResponseJson.add(unrecognizedPropertyName, new JsonPrimitive(unrecognizedPropertyValue));
    expected.put(unrecognizedPropertyName, new JsonPrimitive(unrecognizedPropertyValue));
    DirectionsRefreshResponse response = DirectionsRefreshResponse.fromJson(directionsRefreshResponseJson.toString());

    Map<String, JsonElement> actual = response.getUnrecognizedJsonProperties();

    assertEquals(expected, actual);
  }

  @Test
  public void noUnrecognizedProperties() throws Exception {
    JsonObject directionsRefreshResponseJson = readJsonObject(DIRECTIONS_REFRESH_V1);
    DirectionsRefreshResponse response = DirectionsRefreshResponse.fromJson(directionsRefreshResponseJson.toString());

    Map<String, JsonElement> actual = response.getUnrecognizedJsonProperties();

    assertNull(actual);
  }

  private JsonObject readJsonObject(String file) throws IOException {
    Gson gson = new GsonBuilder().create();
    return gson.fromJson(loadJsonFixture(file), JsonObject.class);
  }
}
