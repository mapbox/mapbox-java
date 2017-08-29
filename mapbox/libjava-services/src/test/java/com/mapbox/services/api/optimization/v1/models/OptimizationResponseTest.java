package com.mapbox.services.api.optimization.v1.models;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.services.api.BaseTest;
import com.mapbox.services.api.optimization.v1.MapboxOptimization;
import com.mapbox.services.commons.geojson.Point;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OptimizationResponseTest extends BaseTest {

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
    OptimizationResponse optimizationResponse = OptimizationResponse.builder()
      .build();
    assertNotNull(optimizationResponse);
  }

  @Test
  public void optimizationResponse_doesDeserializeFromJsonProperly() throws Exception {
    MapboxOptimization client = MapboxOptimization.builder()
      .coordinate(new Point(1.0, 1.0))
      .coordinate(new Point(2.0, 2.0))
      .accessToken("pk.XXX")
      .baseUrl(mockUrl.toString())
      .build();
    Response<OptimizationResponse> response = client.executeCall();
    assertEquals(object.get("code").getAsString(), response.body().code());

    // These objects are tested in their own class tests.
    assertNotNull(response.body().trips());
    assertNotNull(response.body().waypoints());
  }

//  @Test
//  public void testDeserializationWithMissingNullableAttribute() throws IOException {
//    String optimizationJsonString = "{\"code\":\"Ok\"}";
//    Gson gson = new Gson();
//    OptimizationResponse response = gson.fromJson(optimizationJsonString, OptimizationResponse.class);
//    Assert.assertEquals("Ok", response.code());
//    Assert.assertNull(response.waypoints());
//    Assert.assertNull(response.trips());
//  }

  @Test
  public void testSerializable() throws IOException, ClassNotFoundException {
    MapboxOptimization client = MapboxOptimization.builder()
      .coordinate(new Point(1.0, 1.0))
      .coordinate(new Point(2.0, 2.0))
      .baseUrl(mockUrl.toString())
      .accessToken("pk.XXX")
      .build();

    Response<OptimizationResponse> response = client.executeCall();
    byte[] bytes = serialize(response.body());
    Assert.assertEquals(response.body(), deserialize(bytes, OptimizationResponse.class));
  }
}
