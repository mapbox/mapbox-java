package com.mapbox.services.api.rx.directionsmatrix.v1;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directionsmatrix.v1.models.DirectionsMatrixResponse;
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

import io.reactivex.observers.TestObserver;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;

public class MapboxDirectionsMatrixRxTest {

  public static final String DIRECTIONS_MATRIX_3X3_FIXTURE
    = "../libjava-services/src/test/fixtures/directions_matrix_3x3.json";

  private MockWebServer server;
  private HttpUrl mockUrl;

  private List<Position> positions;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String body = new String(
            Files.readAllBytes(Paths.get(DIRECTIONS_MATRIX_3X3_FIXTURE)), Charset.forName("utf-8")
          );
          return new MockResponse().setBody(body);
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }

      }
    });

    server.start();

    mockUrl = server.url("");

    positions = new ArrayList<>();
    positions.add(Position.fromCoordinates(-122.42, 37.78));
    positions.add(Position.fromCoordinates(-122.45, 37.91));
    positions.add(Position.fromCoordinates(-122.48, 37.73));
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testSanityRX() throws ServicesException {
    MapboxDirectionsMatrixRx client = new MapboxDirectionsMatrixRx.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();

    TestObserver<DirectionsMatrixResponse> testObserver = new TestObserver();
    client.getObservable().subscribe(testObserver);
    testObserver.assertComplete();
    testObserver.assertNoErrors();
    testObserver.assertValueCount(1);

    List<List<Object>> events = testObserver.getEvents();
    assertEquals(1, events.get(0).size());

    DirectionsMatrixResponse response = (DirectionsMatrixResponse) events.get(0).get(0);
    assertEquals(response.getCode(), DirectionsCriteria.RESPONSE_OK);
  }
}
