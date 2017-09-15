package com.mapbox.geocoding.v5.models;

import com.mapbox.geocoding.v5.BaseTest;
import com.mapbox.geocoding.v5.GeocodingCriteria;
import com.mapbox.geocoding.v5.MapboxGeocoding;

import org.hamcrest.junit.ExpectedException;
import org.junit.After;
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
import static org.junit.Assert.assertTrue;

public class CarmenContextTest extends BaseTest {

  private static final String GEOCODING_FIXTURE = "geocoding.json";
  private static final String GEOCODING_BATCH_FIXTURE = "geocoding_batch.json";
  private static final String REVERSE_GEOCODE_FIXTURE = "geocoding_reverse.json";
  private static final String GEOCODE_WITH_BBOX_FIXTURE = "bbox_geocoding_result.json";
  private static final String GEOCODE_LANGUAGE_FIXTURE = "language_geocoding_result.json";

  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws Exception {
    server = new MockWebServer();
    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String response;
          if (request.getPath().contains(GeocodingCriteria.MODE_PLACES_PERMANENT)) {
            response = loadJsonFixture(GEOCODING_BATCH_FIXTURE);
          } else if (request.getPath().contains("-77.0366,38.8971")) {
            response = loadJsonFixture(REVERSE_GEOCODE_FIXTURE);
          } else if (request.getPath().contains("texas")) {
            response = loadJsonFixture(GEOCODE_WITH_BBOX_FIXTURE);
          } else if (request.getPath().contains("language")) {
            response = loadJsonFixture(GEOCODE_LANGUAGE_FIXTURE);
          } else {
            response = loadJsonFixture(GEOCODING_FIXTURE);
          }
          return new MockResponse().setBody(response);
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
    CarmenContext carmenContext = CarmenContext.builder().build();
    assertNotNull(carmenContext);
  }

  @Test
  public void id_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertTrue(response.body().features().get(0).context().get(0).id()
      .equals("neighborhood.291451"));
  }

  @Test
  public void text_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertTrue(response.body().features().get(0).context().get(0).text()
      .equals("Downtown"));
  }

  @Test
  public void shortCode_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("texas")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertTrue(response.body().features().get(0).context().get(0).shortCode()
      .equals("us"));
  }

  @Test
  public void wikidata_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("texas")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    System.out.println(response.body().features().get(0).context().get(0).wikidata());
    assertTrue(response.body().features().get(0).context().get(0).wikidata()
      .equals("Q30"));
  }

//  @Test
//  public void category_returnsCorrectString() throws Exception {
  // TODO find a fixture with category
//    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
//      .accessToken(ACCESS_TOKEN)
//      .query("1600 pennsylvania ave nw")
//      .baseUrl(mockUrl.toString())
//      .build();
//    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
//    System.out.println(response.body().features().get(0).context().get(0).category());
//    assertTrue(response.body().features().get(0).context().get(0).category()
//      .equals("Q30"));
//  }

//  @Test
//  public void maki_returnsCorrectString() throws Exception {
//    // TODO find a fixture with category
//    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
//      .accessToken(ACCESS_TOKEN)
//      .query("texas")
//      .baseUrl(mockUrl.toString())
//      .build();
//    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
//    System.out.println(response.body().features().get(0).context().get(0).maki());
//    assertTrue(response.body().features().get(0).context().get(0).maki()
//      .equals("Q30"));
//  }

  @Test
  public void testSerializable() throws Exception {
    CarmenContext carmenContext = CarmenContext.builder()
      .id("123")
      .shortCode("shortCode")
      .build();
    byte[] bytes = serialize(carmenContext);
    assertEquals(carmenContext, deserialize(bytes, CarmenContext.class));
  }
}
