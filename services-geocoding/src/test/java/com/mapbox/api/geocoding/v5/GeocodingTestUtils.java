package com.mapbox.api.geocoding.v5;

import com.mapbox.core.TestUtils;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.hamcrest.junit.ExpectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.io.IOException;

public class GeocodingTestUtils extends TestUtils {

  protected static final String FORWARD_VALID = "forward_valid.json";
  private static final String FORWARD_GEOCODING = "geocoding.json";
  private static final String FORWARD_INVALID = "forward_invalid.json";
  private static final String FORWARD_VALID_ZH = "forward_valid_zh.json";
  private static final String FORWARD_BATCH_GEOCODING = "geocoding_batch.json";
  protected static final String REVERSE_GEOCODING_MULTILANG = "geocoding_reverse_ru_fr.json";

  private MockWebServer server;
  protected HttpUrl mockUrl;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    server = new MockWebServer();
    server.setDispatcher(new Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String response;
          String requestPath = request.getPath();
          if (requestPath.contains(GeocodingCriteria.MODE_PLACES_PERMANENT)) {
            response = loadJsonFixture(FORWARD_BATCH_GEOCODING);
          } else if (requestPath.contains("1600") && !requestPath.contains("nw")) {
            response = loadJsonFixture(FORWARD_VALID);
          } else if (requestPath.contains("nw")) {
            response = loadJsonFixture(FORWARD_GEOCODING);
          } else if (requestPath.contains("sandy")) {
            response = loadJsonFixture(FORWARD_INVALID);
          } else if (requestPath.contains("ru") && requestPath.contains("fr")) {
            response = loadJsonFixture(REVERSE_GEOCODING_MULTILANG);
          } else {
            response = loadJsonFixture(FORWARD_VALID_ZH);
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
}
