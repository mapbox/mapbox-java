package com.mapbox.api.optimization.v1;

import com.mapbox.api.optimization.v1.models.OptimizationResponse;
import com.mapbox.core.TestUtils;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.hamcrest.junit.ExpectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapboxOptimizationTest extends TestUtils {

  private static final String OPTIMIZATION_DISTRIBUTION = "optimized_trip_distributions.json";
  private static final String OPTIMIZATION_FIXTURE = "optimization.json";
  private static final String OPTIMIZATION_STEPS = "optimized_trip_steps.json";

  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

        String resource = OPTIMIZATION_FIXTURE;

        try {
          String body = loadJsonFixture(resource);
          return new MockResponse().setBody(body);
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }
      }
    });
    server.start();
    mockUrl = server.url("");
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    MapboxOptimization mapboxOptimization = MapboxOptimization.builder()
      .coordinate(Point.fromLngLat(1.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(mapboxOptimization);
  }

  @Test
  public void build_doesThrowTwoCoordinateMinException() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("At least two coordinates must be provided with your"));
    MapboxOptimization.builder().build();
  }

  @Test
  public void build_noAccessTokenExceptionThrown() throws Exception {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Missing required properties: accessToken");
    MapboxOptimization.builder()
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .build();
  }

  @Test
  public void build_invalidAccessTokenExceptionThrown() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("Using Mapbox Services requires setting a valid access token.");
    MapboxOptimization.builder()
      .accessToken("")
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .build();
  }

  @Test
  public void build_doesAddCoordinatesToUrl() throws Exception {
    MapboxOptimization client = MapboxOptimization.builder()
      .coordinate(Point.fromLngLat(1.23456, 1.23456))
      .coordinate(Point.fromLngLat(20.9876, 20.9876))
      .accessToken(ACCESS_TOKEN)
      .build();

    assertTrue(client.cloneCall().request().toString().contains("1.23456,1.23456;20.9876,20.9876"));
  }

  @Test
  public void testUserAgent() throws ServicesException, IOException {
    MapboxOptimization client = MapboxOptimization.builder()
      .clientAppName("APP")
      .accessToken(ACCESS_TOKEN)
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .build();
    assertTrue(client.executeCall().raw().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void executeCall_modifiesResponse() throws IOException {
    final Response<OptimizationResponse> modifiedResponse = mock(Response.class);
    final MockedConstruction.MockInitializer<OptimizationResponseFactory> mockInitializer = (mock, context) ->
      when(mock.generate(any())).thenReturn(modifiedResponse);
    try (MockedConstruction ignored = Mockito.mockConstruction(OptimizationResponseFactory.class, mockInitializer)) {
      MapboxOptimization client = MapboxOptimization.builder()
        .clientAppName("APP")
        .accessToken(ACCESS_TOKEN)
        .coordinate(Point.fromLngLat(1.0, 1.0))
        .coordinate(Point.fromLngLat(1.0, 1.0))
        .baseUrl(mockUrl.url().toString())
        .build();
      assertEquals(modifiedResponse, client.executeCall());
    }
  }

  @Test
  public void enqueueCall_onSuccess_modifiesResponse() throws Exception {
    final CountDownLatch latch = new CountDownLatch(1);
    MapboxOptimization client = MapboxOptimization.builder()
      .clientAppName("APP")
      .accessToken(ACCESS_TOKEN)
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .baseUrl(mockUrl.url().toString())
      .build();
    List<Response<OptimizationResponse>> actualResponses = new ArrayList<>();
    client.enqueueCall(new Callback<OptimizationResponse>() {
      @Override
      public void onResponse(Call<OptimizationResponse> call, Response<OptimizationResponse> response) {
        actualResponses.add(response);
        latch.countDown();
      }

      @Override
      public void onFailure(Call<OptimizationResponse> call, Throwable t) {
        latch.countDown();
      }
    });
    latch.await(5, TimeUnit.SECONDS);
    assertEquals(1, actualResponses.size());
    assertEquals("0", actualResponses.get(0).body().trips().get(0).routeIndex());
  }

  @Test
  public void enqueueCall_onFailure() throws InterruptedException {
    final CountDownLatch latch = new CountDownLatch(1);
    MapboxOptimization client = MapboxOptimization.builder()
      .clientAppName("APP")
      .accessToken(ACCESS_TOKEN)
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .baseUrl(mockUrl.url().toString())
      .build();
    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) {
        return new MockResponse().setResponseCode(500).setStatus("Error");
      }
    });
    List<Throwable> actualExceptions = new ArrayList<>();
    client.enqueueCall(new Callback<OptimizationResponse>() {
      @Override
      public void onResponse(Call<OptimizationResponse> call, Response<OptimizationResponse> response) {
        latch.countDown();
      }

      @Override
      public void onFailure(Call<OptimizationResponse> call, Throwable t) {
        actualExceptions.add(t);
        latch.countDown();
      }
    });
    latch.await(5, TimeUnit.SECONDS);
    assertEquals(1, actualExceptions.size());
  }
}
