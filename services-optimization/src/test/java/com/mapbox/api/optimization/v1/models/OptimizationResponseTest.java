package com.mapbox.api.optimization.v1.models;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;
import com.mapbox.api.optimization.v1.MapboxOptimization;

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

public class OptimizationResponseTest extends TestUtils {

  private static final String OPTIMIZATION_FIXTURE = "optimization.json";

  private MockWebServer server;
  private JsonObject object;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws Exception {
    final String json = loadJsonFixture(OPTIMIZATION_FIXTURE);
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
      .accessToken(ACCESS_TOKEN)
      .coordinate(Point.fromLngLat(-122.42, 37.78))
      .coordinate(Point.fromLngLat(-122.45, 37.91))
      .coordinate(Point.fromLngLat(-122.48, 37.73))
      .baseUrl(mockUrl.toString())
      .build();
    Response<OptimizationResponse> response = client.executeCall();
    assertEquals(object.get("code").getAsString(), response.body().code());

    // These objects are tested in their own class tests.
    assertNotNull(response.body().trips());
    assertNotNull(response.body().waypoints());
  }

  @Test
  public void testSerializable() throws IOException, ClassNotFoundException {
    MapboxOptimization client = MapboxOptimization.builder()
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .build();

    Response<OptimizationResponse> response = client.executeCall();
    byte[] bytes = serialize(response.body());
    assertEquals(response.body(), deserialize(bytes, OptimizationResponse.class));
  }
}
