package com.mapbox.api.matrix.v1;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.core.TestUtils;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.api.matrix.v1.models.MatrixResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static com.mapbox.api.directions.v5.DirectionsCriteria.ANNOTATION_DISTANCE;
import static com.mapbox.api.directions.v5.DirectionsCriteria.ANNOTATION_DURATION;
import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_CURB;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapboxMatrixTest extends TestUtils {

  private static final String DIRECTIONS_MATRIX_WALKING_1X3_FIXTURE = "directions_matrix_1x3.json";
  private static final String DIRECTIONS_MATRIX_CYCLING_2X3_FIXTURE = "directions_matrix_2x3.json";
  private static final String DIRECTIONS_MATRIX_DRIVING_3X3_FIXTURE = "directions_matrix_3x3.json";

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
          String resource;
          if (request.getPath().contains("walking")) {
            resource = DIRECTIONS_MATRIX_WALKING_1X3_FIXTURE;

          } else if (request.getPath().contains("cycling")) {
            resource = DIRECTIONS_MATRIX_CYCLING_2X3_FIXTURE;

          } else { // driving
            resource = DIRECTIONS_MATRIX_DRIVING_3X3_FIXTURE;
          }
          String response = loadJsonFixture(resource);
          return new MockResponse().setBody(response);
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
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .coordinates(positions)
      .baseUrl(mockUrl.toString())
      .build();
    Response<MatrixResponse> response = client.executeCall();
    assertEquals(200, response.code());

    // Check the response body
    assertNotNull(response.body());
    assertEquals(3, response.body().destinations().size());
    assertNotNull(response.body().destinations().get(0).name());
    assertEquals(3, response.body().durations().size());
  }

  @Test
  public void build_noAccessTokenExceptionThrown() throws Exception {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Missing required properties: accessToken");
    MapboxMatrix.builder()
      .baseUrl(mockUrl.toString())
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .build();
  }

  @Test
  public void build_invalidAccessTokenExceptionThrown() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("Using Mapbox Services requires setting a valid access token.");
    MapboxMatrix.builder()
      .accessToken("")
      .baseUrl(mockUrl.toString())
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .coordinate(Point.fromLngLat(4.0, 4.0))
      .build();
  }

  @Test
  public void validCoordinates() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("At least two coordinates must be provided with your API request."));
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
    thrown.expectMessage(startsWith("Maximum of 25 coordinates are allowed for this API."));
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
  public void request_containsAnnotations() throws ServicesException, IOException {
    MapboxMatrix client = MapboxMatrix.builder()
            .accessToken(ACCESS_TOKEN)
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .coordinates(positions)
            .addAnnotations(ANNOTATION_DISTANCE, ANNOTATION_DURATION)
            .sources(0, 2)
            .build();

    assertTrue(client.cloneCall()
            .request().url().toString()
            .contains("annotations=distance"));
  }

  @Test
  public void request_containsApproaches() throws ServicesException, IOException {
    MapboxMatrix client = MapboxMatrix.builder()
            .accessToken(ACCESS_TOKEN)
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .coordinates(positions)
            .addApproaches(APPROACH_CURB, APPROACH_CURB, APPROACH_CURB)
            .build();

    String c = client.cloneCall().request().url().toString();


    assertTrue(c.contains("approaches=curb"));
  }

  @Test
  public void annotationsAndApproaches() throws ServicesException, IOException {
    MapboxMatrix client = MapboxMatrix.builder()
      .accessToken(ACCESS_TOKEN)
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .coordinates(positions)
      .addAnnotations(ANNOTATION_DISTANCE, ANNOTATION_DURATION)
      .addApproaches(APPROACH_CURB, APPROACH_CURB, APPROACH_CURB)
      .sources(0,2)
      .baseUrl(mockUrl.toString())
      .build();

    Response<MatrixResponse> response = client.executeCall();
    assertEquals(200, response.code());
    assertEquals(1, response.body().sources().size());
    assertEquals(1, response.body().distances().size());
    assertEquals(1, response.body().durations().size());
    assertEquals(-122.461997, response.body().sources().get(0).location().longitude(), DELTA);
    assertEquals(-122.420019, response.body().destinations().get(0).location().longitude(),  DELTA);
    assertEquals(19711.7, response.body().durations().get(0)[2], DELTA);
    assertEquals(27192.3, response.body().distances().get(0)[2], DELTA);
    assertEquals("McAllister Street", response.body().destinations().get(0).name());
  }
}
