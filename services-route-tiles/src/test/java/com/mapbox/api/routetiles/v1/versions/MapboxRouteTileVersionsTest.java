package com.mapbox.api.routetiles.v1.versions;

import com.mapbox.api.routetiles.v1.versions.models.RouteTileVersionsResponse;
import com.mapbox.geojson.TestUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;

public class MapboxRouteTileVersionsTest extends TestUtils {
  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String body = loadJsonFixture("versions.json");
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
    MapboxRouteTileVersions mapboxRouteTileVersions = getBasicMapboxRouteTileVersions();

    Assert.assertNotNull(mapboxRouteTileVersions);
  }

  @Test
  public void responseIsOk() throws Exception {
    MapboxRouteTileVersions mapboxRouteTileVersions = getBasicMapboxRouteTileVersions();

    mapboxRouteTileVersions.setCallFactory(null);
    Response<RouteTileVersionsResponse> response = mapboxRouteTileVersions.executeCall();

    assertEquals(200, response.code());
  }

  @Test
  public void responseContainsAvailableVersion() throws Exception {
    MapboxRouteTileVersions mapboxRouteTileVersions = getBasicMapboxRouteTileVersions();
    mapboxRouteTileVersions.setCallFactory(null);

    Response<RouteTileVersionsResponse> response = mapboxRouteTileVersions.executeCall();

    assertEquals("2018-10-16", response.body().availableVersions().get(0));
  }

  private MapboxRouteTileVersions getBasicMapboxRouteTileVersions() {
    return MapboxRouteTileVersions.builder()
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .build();
  }
}
