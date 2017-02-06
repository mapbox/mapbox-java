package com.mapbox.services.api.rx.distance.v1;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.distance.v1.models.DistanceResponse;
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
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test Rx support on the Mapbox Distance API
 */

public class MapboxDistanceRxTest {

  private static final String DISTANCE_FIXTURE = "../libjava-services/src/test/fixtures/distance_v1.json";

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
          String body = new String(Files.readAllBytes(Paths.get(DISTANCE_FIXTURE)), Charset.forName("utf-8"));
          return new MockResponse().setBody(body);
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }
      }
    });

    server.start();
    mockUrl = server.url("");

    coordinates = new ArrayList<>();
    coordinates.add(Position.fromCoordinates(13.41894, 52.50055));
    coordinates.add(Position.fromCoordinates(14.10293, 52.50055));
    coordinates.add(Position.fromCoordinates(13.50116, 53.10293));
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  public void testSanityRX() throws ServicesException {
    MapboxDistanceRx client = new MapboxDistanceRx.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(DirectionsCriteria.PROFILE_WALKING)
      .setCoordinates(coordinates)
      .setBaseUrl(mockUrl.toString())
      .build();

    TestSubscriber<DistanceResponse> testSubscriber = new TestSubscriber<>();
    client.getObservable().subscribe(testSubscriber);

    testSubscriber.assertCompleted();
    testSubscriber.assertNoErrors();
    testSubscriber.assertValueCount(1);

    List<DistanceResponse> events = testSubscriber.getOnNextEvents();
    assertEquals(1, events.size());

    DistanceResponse response = events.get(0);
    assertNotNull(response);
    assertEquals(3, response.getDurations().length);
    assertNotNull(response.getDurations()[0][0]);
  }

}
