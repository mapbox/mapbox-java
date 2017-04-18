package com.mapbox.services.api.rx.optimizedtrips.v1;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.optimizedtrips.v1.models.OptimizedTripsResponse;
import com.mapbox.services.commons.models.Position;

import org.hamcrest.junit.ExpectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.TestObserver;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;

public class MapboxOptimizedTripsRxTest {

  public static final String OPTIMIZED_TRIP_FIXTURE = "../libjava-services/src/test/fixtures/optimized_trip.json";

  private static final String ACCESS_TOKEN = "pk.XXX";

  private MockWebServer server;
  private HttpUrl mockUrl;

  private List<Position> coordinates;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new Dispatcher() {

      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String body = new String(Files.readAllBytes(Paths.get(OPTIMIZED_TRIP_FIXTURE)), Charset.forName("utf-8"));
          return new MockResponse().setBody(body);
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }
      }
    });

    server.start();
    mockUrl = server.url("");

    coordinates = new ArrayList<>();
    coordinates.add(Position.fromCoordinates(13.418946862220764, 52.50055852688439));
    coordinates.add(Position.fromCoordinates(13.419011235237122, 52.50113000479732));
    coordinates.add(Position.fromCoordinates(13.419756889343262, 52.50171780290061));
    coordinates.add(Position.fromCoordinates(13.419885635375975, 52.50237416816131));
    coordinates.add(Position.fromCoordinates(13.420631289482117, 52.50294888790448));
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  public void testSanityRX() throws ServicesException {
    MapboxOptimizedTripsRx client = new MapboxOptimizedTripsRx.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setCoordinates(coordinates)
      .setBaseUrl(mockUrl.toString())
      .build();

    TestObserver<OptimizedTripsResponse> testObserver = new TestObserver<>();
    client.getObservable().subscribe(testObserver);

    testObserver.assertComplete();
    testObserver.assertNoErrors();
    testObserver.assertValueCount(1);

    List<List<Object>> events = testObserver.getEvents();
    assertEquals(1, events.get(0).size());

    OptimizedTripsResponse response = (OptimizedTripsResponse) events.get(0).get(0);
    assertEquals(response.getCode(), DirectionsCriteria.RESPONSE_OK);
  }
}