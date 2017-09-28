package com.mapbox.optimization.v1.models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.geojson.Point;
import com.mapbox.optimization.v1.MapboxOptimization;
import com.mapbox.services.BaseTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class OptimizationWaypointTest extends BaseTest {

  private MockWebServer server;
  private JsonObject object;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws Exception {
    final String json = loadJsonFixture("optimization.json");
    object = new JsonParser().parse(json).getAsJsonObject();

    server = new MockWebServer();
    server.setDispatcher(new Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        return new MockResponse().setBody(json);
      }
    });
    server.start();
    mockUrl = server.url("");
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  public void sanityTest() throws Exception {
    OptimizationWaypoint optimizationWaypoint = OptimizationWaypoint.builder()
      .waypointIndex(0)
      .tripsIndex(0)
      .rawLocation(new double[] {1.0, 1.0})
      .build();
    assertNotNull(optimizationWaypoint);
  }

  @Test
  public void optimizationWaypoint_doesDeserializeFromJsonProperly() throws Exception {
    MapboxOptimization client = MapboxOptimization.builder()
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .baseUrl(mockUrl.toString())
      .accessToken("pk.XXX")
      .build();
    Response<OptimizationResponse> response = client.executeCall();

    // get the first waypoint in response:
    JsonObject waypointObject = object.get("waypoints").getAsJsonArray().get(0).getAsJsonObject();
    assertEquals(waypointObject.get("waypoint_index").getAsInt(),
      response.body().waypoints().get(0).waypointIndex());
    assertEquals(waypointObject.get("name").getAsString(),
      response.body().waypoints().get(0).name());
    assertEquals(waypointObject.get("trips_index").getAsInt(),
      response.body().waypoints().get(0).tripsIndex());
    assertEquals(waypointObject.get("location").getAsJsonArray().get(0).getAsDouble(),
      response.body().waypoints().get(0).rawLocation()[0], DELTA);
    assertEquals(waypointObject.get("location").getAsJsonArray().get(1).getAsDouble(),
      response.body().waypoints().get(0).rawLocation()[1], DELTA);
  }

  @Test
  public void location_doesCorrectlyConvertToPoint() throws Exception {
    MapboxOptimization client = MapboxOptimization.builder()
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .baseUrl(mockUrl.toString())
      .accessToken("pk.XXX")
      .build();
    Response<OptimizationResponse> response = client.executeCall();

    // get the first waypoint in response:
    JsonObject waypointObject = object.get("waypoints").getAsJsonArray().get(0).getAsJsonObject();

    // Check that point got created correctly
    assertEquals(waypointObject.get("location").getAsJsonArray().get(0).getAsDouble(),
      response.body().waypoints().get(0).location().longitude(), DELTA);
    assertEquals(waypointObject.get("location").getAsJsonArray().get(1).getAsDouble(),
      response.body().waypoints().get(0).location().latitude(), DELTA);
  }

  @Test
  public void testDeserializationWithMissingNullableAttribute() throws IOException {
    String optimizationJsonString
      = "{\"code\":\"Ok\",\"waypoints\":[{\"location\":[-122.420019,37.780091],\"waypoint_index\":0,\"trips_index\":0}]}";
    Gson gson = new Gson();
    OptimizationWaypoint response = gson.fromJson(optimizationJsonString, OptimizationWaypoint.class);
    assertEquals(0, response.waypointIndex());
    assertEquals(0, response.tripsIndex());
    assertNull(response.name());
  }

  @Test
  public void testSerializable() throws IOException, ClassNotFoundException {
    MapboxOptimization client = MapboxOptimization.builder()
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .baseUrl(mockUrl.toString())
      .accessToken("pk.XXX")
      .build();

    Response<OptimizationResponse> response = client.executeCall();
    byte[] bytes = serialize(response.body().waypoints().get(0));
    assertEquals(response.body().waypoints().get(0),
      deserialize(bytes, OptimizationWaypoint.class));
  }
}
