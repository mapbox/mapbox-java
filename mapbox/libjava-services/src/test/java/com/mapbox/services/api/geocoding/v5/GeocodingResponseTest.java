package com.mapbox.services.api.geocoding.v5;

import com.mapbox.services.api.BaseTest;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.geojson.Point;
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

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GeocodingResponseTest extends BaseTest {

  private static final String ACCESS_TOKEN = "pk.XXX";

  private static final String GEOCODING_FIXTURE = "geocoding/geocoding.json";
  private static final String GEOCODING_BATCH_FIXTURE = "geocoding/geocoding_batch.json";
  private static final String REVERSE_GEOCODE_FIXTURE = "geocoding/geocoding_reverse.json";
  private static final String GEOCODE_WITH_BBOX_FIXTURE = "geocoding/bbox_geocoding_result.json";

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
          } else if (request.getPath().contains("")) {
            response = loadJsonFixture(REVERSE_GEOCODE_FIXTURE);
          } else if (request.getPath().contains("texas")) {
            response = loadJsonFixture(GEOCODE_WITH_BBOX_FIXTURE);
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
    GeocodingResponse response = GeocodingResponse.builder()
      .build();
    assertNotNull(response);
  }

  @Test
  public void reverseGeocoding_responseSuccessfully() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query(Point.fromLngLat(-77.0366, 38.8971))
      .baseUrl(mockUrl.toString())
      .build();
    assertNotNull(mapboxGeocoding);
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertEquals(200, response.code());
    assertNotNull(response.body());
  }
}
