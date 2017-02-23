package com.mapbox.services.api.rx.geocoding.v5;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import io.reactivex.observers.TestObserver;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test Rx support on the Mapbox Geocoding API
 */

public class MapboxGeocodingRxTest {

  private static final double DELTA = 1E-10;

  private static final String GEOCODING_FIXTURE = "../libjava-services/src/test/fixtures/geocoding.json";
  private static final String ACCESS_TOKEN = "pk.XXX";

  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new Dispatcher() {

      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String body = new String(Files.readAllBytes(Paths.get(GEOCODING_FIXTURE)), Charset.forName("utf-8"));
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
  public void testSanityRX() throws ServicesException {
    MapboxGeocodingRx client = new MapboxGeocodingRx.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .setBaseUrl(mockUrl.toString())
      .build();

    TestObserver<GeocodingResponse> testObserver = new TestObserver<>();
    client.getObservable().subscribe(testObserver);

    testObserver.assertComplete();
    testObserver.assertNoErrors();
    testObserver.assertValueCount(1);

    List<List<Object>> events = testObserver.getEvents();
    assertEquals(1, events.get(0).size());

    GeocodingResponse response = (GeocodingResponse) events.get(0).get(0);
    assertNotNull(response);
  }

}
