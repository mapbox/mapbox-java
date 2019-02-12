package com.mapbox.api.directionsrefresh.v5;

import com.mapbox.api.directionsrefresh.v5.models.DirectionsRefreshResponse;
import com.mapbox.core.TestUtils;

import org.hamcrest.junit.ExpectedException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapboxDirectionsRefreshTest extends TestUtils {

  private static final String DIRECTIONS_REFRESH_V5_FIXTURE = "directions_refresh_v5.json";

  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

        String resource = DIRECTIONS_REFRESH_V5_FIXTURE;

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
    MapboxDirectionsRefresh mapboxDirectionsRefresh = MapboxDirectionsRefresh.builder()
      .accessToken(ACCESS_TOKEN)
      .requestId("")
      .build();

    Assert.assertNotNull(mapboxDirectionsRefresh);
  }

  @Test
  public void testResponse() throws IOException {
    MapboxDirectionsRefresh mapboxDirectionsRefresh = MapboxDirectionsRefresh.builder()
      .accessToken(ACCESS_TOKEN)
      .requestId("")
      .baseUrl(mockUrl.toString())
      .build();

    Response<DirectionsRefreshResponse> response = mapboxDirectionsRefresh.executeCall();
    DirectionsRefreshResponse directionsRefreshResponse = response.body();

    assertEquals(200, response.code());
    assertEquals("Ok", directionsRefreshResponse.code());
    assertNotNull(directionsRefreshResponse.route().legs());
    assertNotNull(directionsRefreshResponse.route().legs().get(0).annotation());
  }

  @Test
  public void testRoute() throws IOException {
    MapboxDirectionsRefresh mapboxDirectionsRefresh = MapboxDirectionsRefresh.builder()
      .accessToken(ACCESS_TOKEN)
      .requestId("")
      .baseUrl(mockUrl.toString())
      .build();

    Response<DirectionsRefreshResponse> response = mapboxDirectionsRefresh.executeCall();
    DirectionsRefreshResponse directionsRefreshResponse = response.body();

    assertNotNull(directionsRefreshResponse.route().legs());
    assertNotNull(directionsRefreshResponse.route().legs().get(0).annotation());
  }
}
