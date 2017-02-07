package com.mapbox.services.api.rx.mapmatching.v5;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.mapmatching.v5.MapMatchingCriteria;
import com.mapbox.services.api.mapmatching.v5.models.MapMatchingResponse;
import com.mapbox.services.commons.geojson.LineString;
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
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

/**
 * Test Rx support on the Mapbox Map Matching API
 */

public class MapboxMapMatchingRxTest {

  public static final String POLYLINE_FIXTURE =
    "../libjava-services/src/test/fixtures/mapmatching_v5_polyline.json";

  private static final String ACCESS_TOKEN = "pk.XXX";

  private MockWebServer server;
  private HttpUrl mockUrl;

  private Position[] coordinates;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new Dispatcher() {

      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String body = new String(Files.readAllBytes(Paths.get(POLYLINE_FIXTURE)), Charset.forName("utf-8"));
          return new MockResponse().setBody(body);
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }
      }
    });

    server.start();
    mockUrl = server.url("");

    coordinates = new Position[] {
      Position.fromCoordinates(13.418946862220764, 52.50055852688439),
      Position.fromCoordinates(13.419011235237122, 52.50113000479732),
      Position.fromCoordinates(13.419756889343262, 52.50171780290061),
      Position.fromCoordinates(13.419885635375975, 52.50237416816131),
      Position.fromCoordinates(13.420631289482117, 52.50294888790448)
    };
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  public void testSanityRX() throws ServicesException {
    MapboxMapMatchingRx client = new MapboxMapMatchingRx.Builder()
      .setBaseUrl(mockUrl.toString())
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(MapMatchingCriteria.PROFILE_WALKING)
      .setCoordinates(coordinates)
      .build();

    TestSubscriber<MapMatchingResponse> testSubscriber = new TestSubscriber<>();
    client.getObservable().subscribe(testSubscriber);

    testSubscriber.assertCompleted();
    testSubscriber.assertNoErrors();
    testSubscriber.assertValueCount(1);

    List<MapMatchingResponse> events = testSubscriber.getOnNextEvents();
    assertEquals(1, events.size());

    MapMatchingResponse response = events.get(0);
    assertEquals(response.getCode(), MapMatchingCriteria.RESPONSE_OK);
  }

}
