package com.mapbox.api.directions.models;

import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import org.junit.After;
import org.junit.Before;
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

  @Test
  public void testToFromJson1() {

    List<BannerComponents> bannerComponents =
      Arrays.asList(BannerComponents.builder()
        .text("You will arrive at your destination")
        .type("text")
        .build()
      );

    BannerText bannerText = BannerText.builder()
      .text("You will arrive at your destination")
      .components(bannerComponents)
      .type("arrive")
      .modifier("straight")
      .build();

    String jsonString = bannerText.toJson();
    BannerText bannerTextFromJson = BannerText.fromJson(jsonString);

    assertEquals(bannerText, bannerTextFromJson);
  }

  @Test
  public void testToFromJson2() {

    List<BannerComponents> bannerComponents =
      Arrays.asList(
        BannerComponents.builder()
          .text("")
          .type("lane")
          .directions(Arrays.asList("left"))
          .active(true)
          .build(),
        BannerComponents.builder()
          .text("")
          .type("lane")
          .directions(Arrays.asList("left", "straight"))
          .active(true)
          .build(),
        BannerComponents.builder()
          .text("")
          .type("lane")
          .directions(Arrays.asList("right"))
          .active(false)
          .build());

    BannerText bannerText = BannerText.builder()
      .text("")
      .components(bannerComponents)
      .build();

    String jsonString = bannerText.toJson();
    BannerText bannerTextFromJson = BannerText.fromJson(jsonString);

    assertEquals(bannerText, bannerTextFromJson);
  }

  @Test
  public void testToFromJson3() {

    List<BannerComponents> bannerComponents =
      Arrays.asList(
        BannerComponents.builder()
          .text("Baltimore")
          .type("text")
          .build(),
        BannerComponents.builder()
          .text("/")
          .type("text")
          .directions(Arrays.asList("left", "straight"))
          .active(true)
          .build(),
        BannerComponents.builder()
          .text("Northern Virginia")
          .type("text")
          .build());

    BannerText bannerText = BannerText.builder()
      .text("Baltimore / Northern Virginia")
      .type("turn")
      .modifier("left")
      .components(bannerComponents)
      .build();

    String jsonString = bannerText.toJson();
    BannerText bannerTextFromJson = BannerText.fromJson(jsonString);

    assertEquals(bannerText, bannerTextFromJson);
  }


  @Test
  public void testToFromJson4() {

    List<BannerComponents> bannerComponents =
      Arrays.asList(
        BannerComponents.builder()
          .text("I 495")
          .imageBaseUrl("https://s3.amazonaws.com/mapbox/shields/v3/i-495")
          .type("icon")
          .build(),
        BannerComponents.builder()
          .text("North")
          .type("text")
          .abbreviation("N")
          .abbreviationPriority(0)
          .build(),
        BannerComponents.builder()
          .text("/")
          .type("delimeter")
          .build(),
        BannerComponents.builder()
          .text("I 95")
          .imageBaseUrl("https://s3.amazonaws.com/mapbox/shields/v3/i-95")
          .type("icon")
          .build());

    BannerText bannerText = BannerText.builder()
      .text("I 495 North / I 95")
      .type("turn")
      .modifier("left")
      .components(bannerComponents)
      .build();

    String jsonString = bannerText.toJson();
    BannerText bannerTextFromJson = BannerText.fromJson(jsonString);

    assertEquals(bannerText, bannerTextFromJson);
  }
}
