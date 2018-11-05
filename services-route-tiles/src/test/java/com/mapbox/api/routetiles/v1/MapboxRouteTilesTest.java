package com.mapbox.api.routetiles.v1;

import com.mapbox.core.TestUtils;
import com.mapbox.geojson.BoundingBox;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Okio;

public class MapboxRouteTilesTest extends TestUtils {
  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        okio.Buffer buffer = new okio.Buffer();
        try {
          buffer.writeAll(Okio.source(new File("2018-10-31T15_28_22.155Z.tar")));
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }
        return new MockResponse().setBody(buffer);
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
    BoundingBox boundingBox = BoundingBox.fromLngLats(1, 1, 1, 1);
    MapboxRouteTiles mapboxRouteTiles = MapboxRouteTiles.builder()
      .version("")
      .accessToken(ACCESS_TOKEN)
      .boundingBox(boundingBox)
      .build();

    Assert.assertNotNull(mapboxRouteTiles);
  }
}
