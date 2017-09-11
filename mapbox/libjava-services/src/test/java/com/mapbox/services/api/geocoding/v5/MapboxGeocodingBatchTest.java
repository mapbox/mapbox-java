package com.mapbox.services.api.geocoding.v5;

import com.mapbox.services.api.BaseTest;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;

public class MapboxGeocodingBatchTest extends BaseTest {
//
//  private static final String GEOCODING_FIXTURE = "src/test/fixtures/geocoding/geocoding_batch.json";
//  private static final String ACCESS_TOKEN = "pk.XXX";
//
//  private MockWebServer server;
//  private HttpUrl mockUrl;
//
//  @Rule
//  public ExpectedException thrown = ExpectedException.none();
//
//  @Before
//  public void setUp() throws IOException {
//    server = new MockWebServer();
//
//    server.setDispatcher(new Dispatcher() {
//
//      @Override
//      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
//        try {
//
//          String body = loadJsonFixture(GEOCODING_FIXTURE);
//          return new MockResponse().setBody(body);
//        } catch (IOException ioException) {
//          throw new RuntimeException(ioException);
//        }
//      }
//    });
//    server.start();
//
//    mockUrl = server.url("");
//  }
//
//  @After
//  public void tearDown() throws IOException {
//    server.shutdown();
//  }
//
//  @Test
//  public void testSanity() throws ServicesException, IOException {
//    MapboxGeocoding client = new MapboxGeocoding.Builder()
//      .setAccessToken(ACCESS_TOKEN)
//      .setMode(GeocodingCriteria.MODE_PLACES_PERMANENT)
//      .setLocation("20001;20009;22209")
//      .setBaseUrl(mockUrl.toString())
//      .build();
//    Response<List<GeocodingResponse>> response = client.executeBatchCall();
//    assertEquals(response.code(), 200);
//    assertEquals(response.body().size(), 3); // 3 zip codes
//  }
//
//  @Test
//  public void testContent() throws ServicesException, IOException {
//    MapboxGeocoding client = new MapboxGeocoding.Builder()
//      .setAccessToken(ACCESS_TOKEN)
//      .setMode(GeocodingCriteria.MODE_PLACES_PERMANENT)
//      .setLocation("20001;20009;22209")
//      .setBaseUrl(mockUrl.toString())
//      .build();
//    Response<List<GeocodingResponse>> response = client.executeBatchCall();
//    assertEquals(response.code(), 200);
//
//    // Queries
//    assertEquals(response.body().get(0).getQuery().get(0), "20001");
//    assertEquals(response.body().get(1).getQuery().get(0), "20009");
//    assertEquals(response.body().get(2).getQuery().get(0), "22209");
//
//    // Features
//    assertEquals(response.body().get(0).getFeatures().get(0).getPlaceName(),
//      "20001, District of Columbia, United States");
//    assertEquals(response.body().get(1).getFeatures().get(0).getPlaceName(),
//      "20009, District of Columbia, United States");
//    assertEquals(response.body().get(2).getFeatures().get(0).getPlaceName(),
//      "22209, Virginia, United States");
//  }
//
//  @Test
//  public void testBatchVsNonBatch() throws ServicesException, IOException {
//    // Non-batch call on multiple locations
//    MapboxGeocoding client = new MapboxGeocoding.Builder()
//      .setAccessToken(ACCESS_TOKEN)
//      .setMode(GeocodingCriteria.MODE_PLACES_PERMANENT)
//      .setLocation("20001;20009;22209")
//      .setBaseUrl(mockUrl.toString())
//      .build();
//    thrown.expect(IllegalArgumentException.class);
//    client.executeCall();
//
//    // Batch call on one location
//    MapboxGeocoding clientBatch = new MapboxGeocoding.Builder()
//      .setAccessToken(ACCESS_TOKEN)
//      .setMode(GeocodingCriteria.MODE_PLACES_PERMANENT)
//      .setLocation("20001")
//      .setBaseUrl(mockUrl.toString())
//      .build();
//    thrown.expect(IllegalArgumentException.class);
//    clientBatch.executeBatchCall();
//  }

}
