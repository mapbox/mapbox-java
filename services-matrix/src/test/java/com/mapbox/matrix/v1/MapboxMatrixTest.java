package com.mapbox.matrix.v1;

import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.mapbox.directions.v5.DirectionsCriteria;
import com.mapbox.geojson.Point;
import com.mapbox.matrix.v1.models.MatrixResponse;
import com.mapbox.services.exceptions.ServicesException;

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

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

public class MapboxMatrixTest extends BaseTest {

  private static final String DIRECTIONS_MATRIX_3X3_FIXTURE
    = "src/test/fixtures/directions_matrix_3x3.json";
  private static final String DIRECTIONS_MATRIX_2x3_FIXTURE
    = "src/test/fixtures/directions_matrix_2x3.json";
  private static final String ACCESS_TOKEN = "pk.XXX";

  private MockWebServer server;
  private HttpUrl mockUrl;

  private ArrayList<Point> positions;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new Dispatcher() {

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
    positions.add(Point.fromLngLat(-122.42, 37.78));
    positions.add(Point.fromLngLat(-122.45, 37.91));
    positions.add(Point.fromLngLat(-122.48, 37.73));
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  /**
   * Test the most basic request (default response format)
   */
  @Test
  public void testCallSanity() throws ServicesException, IOException {
    MapboxMatrix client = MapboxMatrix.builder()
      .accessToken(ACCESS_TOKEN)
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .coordinates(positions)
      .baseUrl(mockUrl.toString())
      .build();
    Response<MatrixResponse> response = client.executeCall();
    assertEquals(response.code(), 200);

    // Check the response body
    assertNotNull(response.body());
    assertEquals(3, response.body().destinations().size());
    assertNotNull(response.body().destinations().get(0).name());
  }

  @Test
  public void requiredAccessToken() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Services requires setting a valid access token"));
    MapboxMatrix.builder().accessToken("").baseUrl("").build();
  }

  @Test
  public void validCoordinates() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You should provide at least two coordinates (from/to)."));
    MapboxMatrix.builder()
      .accessToken(ACCESS_TOKEN)
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .build();
  }

  @Test
  public void validCoordinatesTotal() throws ServicesException {
    int total = 26;
    ArrayList<Point> positions = new ArrayList<>();
    for (int i = 0; i < total; i++) {
      // Fake too many positions
      positions.add(Point.fromLngLat(0.0, 0.0));
    }

    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("All profiles allow for a maximum of 25 coordinates."));
    MapboxMatrix.builder()
      .accessToken(ACCESS_TOKEN)
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .coordinates(positions)
      .build();
  }

  @Test
  public void testUserAgent() throws ServicesException, IOException {
    MapboxMatrix service = MapboxMatrix.builder()
      .clientAppName("APP")
      .accessToken(ACCESS_TOKEN)
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .coordinates(positions)
      .baseUrl(mockUrl.toString())
      .build();
    assertTrue(service.executeCall().raw().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void testResponse() throws ServicesException, IOException {
    MapboxMatrix client = MapboxMatrix.builder()
      .clientAppName("APP")
      .accessToken("pk.XXX")
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .coordinates(positions)
      .baseUrl(mockUrl.toString())
      .build();
    Response<MatrixResponse> response = client.executeCall();

    assertEquals(response.body().sources().size(), 3);
    assertEquals(response.body().sources().get(0).location().longitude(), -122.420019, DELTA);
    assertEquals(response.body().destinations().get(0).location().longitude(), -122.420019, DELTA);
//    assertEquals(response.body().durations().get(0), 1888.3, DELTA);
    assertEquals(response.body().destinations().get(0).name(), "McAllister Street");
  }
}
