package com.mapbox.services.api.rx.mapmatching.v5;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.mapmatching.v5.MapMatchingCriteria;
import com.mapbox.services.api.mapmatching.v5.models.MapMatchingResponse;
import com.mapbox.services.api.rx.BaseTest;
import com.mapbox.services.commons.models.Position;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.List;

import io.reactivex.observers.TestObserver;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;

/**
 * Test Rx support on the Mapbox Map Matching API
 */
public class MapboxMapMatchingRxTest extends BaseTest {

  private static final String POLYLINE_FIXTURE = "../libjava-services/src/test/fixtures/mapmatching_v5_polyline.json";

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
          String body = loadJsonFixture(POLYLINE_FIXTURE);
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

    TestObserver<MapMatchingResponse> testObserver = new TestObserver<>();
    client.getObservable().subscribe(testObserver);

    testObserver.assertComplete();
    testObserver.assertNoErrors();
    testObserver.assertValueCount(1);

    List<List<Object>> events = testObserver.getEvents();
    assertEquals(1, events.get(0).size());

    MapMatchingResponse response = (MapMatchingResponse) events.get(0).get(0);
    assertEquals(response.getCode(), MapMatchingCriteria.RESPONSE_OK);
  }

}
