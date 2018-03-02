package com.mapbox.api.directions.v5.models;


import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.hamcrest.junit.ExpectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class BannerTextTest extends TestUtils {

  private static final String DIRECTIONS_V5_FIXTURE = "directions_v5_banner_text.json";

  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String body = loadJsonFixture(DIRECTIONS_V5_FIXTURE);
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

  @Test
  public void sanity() throws Exception {
    BannerText bannerText = BannerText.builder().text("test").build();
    assertNotNull(bannerText);
  }

  @Test
  public void testSerializable() throws Exception {
    BannerText bannerText = BannerText.builder().text("test").build();
    byte[] serialized = TestUtils.serialize(bannerText);
    assertEquals(bannerText, deserialize(serialized, BannerText.class));
  }

  @Test
  public void requestBannerInstructions() throws Exception {
    Response<DirectionsResponse> response = MapboxDirections.builder()
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .origin(Point.fromLngLat(-122.03067988107114, 37.331808179989494))
      .destination(Point.fromLngLat(-122.03178702099605, 37.3302383113533))
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .voiceUnits("imperial")
      .roundaboutExits(true)
      .geometries("polyline")
      .overview("full")
      .steps(true)
      .voiceInstructions(true)
      .bannerInstructions(true)
      .build().executeCall();

    BannerText bannerText = response.body()
      .routes().get(0)
      .legs().get(0)
      .steps().get(0)
      .bannerInstructions().get(0)
      .primary();

    assertNotNull(bannerText.modifier());
    assertNotNull(bannerText.type());
  }
}
