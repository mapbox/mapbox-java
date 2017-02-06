package com.mapbox.services.api.rx.directions.v5;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.commons.models.Position;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

/**
 * Test Rx support on the Mapbox Directions API
 */

public class MapboxDirectionsRxTest {

  private static final double DELTA = 1E-10;

  public static final String DIRECTIONS_FIXTURE = "../libjava-services/src/test/fixtures/directions_v5.json";

  private MockWebServer server;
  private HttpUrl mockUrl;

  private ArrayList<Position> positions;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String body = new String(Files.readAllBytes(Paths.get(DIRECTIONS_FIXTURE)), Charset.forName("utf-8"));
          return new MockResponse().setBody(body);
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }

      }
    });

    server.start();

    mockUrl = server.url("");

    positions = new ArrayList<>();
    positions.add(Position.fromCoordinates(-122.416667, 37.783333)); // SF
    positions.add(Position.fromCoordinates(-121.9, 37.333333)); // SJ
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testSanityRX() throws ServicesException {
    MapboxDirectionsRx client = new MapboxDirectionsRx.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();

    TestSubscriber<DirectionsResponse> testSubscriber = new TestSubscriber<>();
    client.getObservable().subscribe(testSubscriber);

    testSubscriber.assertCompleted();
    testSubscriber.assertNoErrors();
    testSubscriber.assertValueCount(1);

    List<DirectionsResponse> events = testSubscriber.getOnNextEvents();
    assertEquals(1, events.size());

    DirectionsResponse response = events.get(0);
    assertEquals(response.getCode(), DirectionsCriteria.RESPONSE_OK);
  }

}
