package com.mapbox.services.api.mapmatching.v5;

import com.mapbox.services.api.BaseTest;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.mapmatching.v5.models.MapMatchingResponse;
import com.mapbox.services.api.mapmatching.v5.models.MapMatchingTracepoint;
import com.mapbox.services.commons.models.Position;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test the Mapbox Map Matching API
 */
public class MapboxMapMatchingTest extends BaseTest {

  private static final String POLYLINE_FIXTURE = "src/test/fixtures/mapmatching_v5_polyline.json";

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
          String body = loadJsonFixture("", POLYLINE_FIXTURE);
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

  /**
   * Test the most basic request (default response format)
   */
  @Test
  public void testCallSanity() throws ServicesException, IOException {
    MapboxMapMatching client = new MapboxMapMatching.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(MapMatchingCriteria.PROFILE_WALKING)
      .setCoordinates(coordinates)
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<MapMatchingResponse> response = client.executeCall();
    assertEquals(response.code(), 200);

    // Check the response body
    assertNotNull(response.body());
    assertEquals(1, response.body().getMatchings().size());
    assertNotNull(response.body().getMatchings().get(0).getGeometry());
  }

  @Test
  public void requiredAccessToken() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Services requires setting a valid access token"));
    new MapboxMapMatching.Builder().build();
  }

  @Test
  public void validProfileNonNull() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("A profile is required for the Map Matching API"));
    new MapboxMapMatching.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(null)
      .build();
  }

  @Test
  public void validCoordinates() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Coordinates must be specified for Map Matching to be able to work"));
    new MapboxMapMatching.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(MapMatchingCriteria.PROFILE_DRIVING)
      .build();
  }

  @Test
  public void validCoordinatesTotal() throws ServicesException {
    int total = 101;
    Position[] positions = new Position[total];
    for (int i = 0; i < total; i++) {
      // Fake too many positions
      positions[i] = Position.fromCoordinates(0.0, 0.0);
    }

    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("All profiles allows for maximum of 100 coordinates."));
    new MapboxMapMatching.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(MapMatchingCriteria.PROFILE_DRIVING)
      .setCoordinates(positions)
      .build();
  }

  @Test
  public void testUserAgent() throws ServicesException, IOException {
    MapboxMapMatching service = new MapboxMapMatching.Builder()
      .setClientAppName("APP")
      .setAccessToken("pk.XXX")
      .setProfile(MapMatchingCriteria.PROFILE_DRIVING)
      .setCoordinates(coordinates)
      .setBaseUrl(mockUrl.toString())
      .build();
    assertTrue(service.executeCall().raw().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void validConvenientMethodsFetchingMapMatchingProperties()
    throws ServicesException, IOException, ParseException {
    MapboxMapMatching client = new MapboxMapMatching.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(MapMatchingCriteria.PROFILE_DRIVING)
      .setCoordinates(coordinates)
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<MapMatchingResponse> response = client.executeCall();
    assertEquals(response.code(), 200);

    // Check the response body
    assertNotNull(response.body());
    assertEquals(1, response.body().getMatchings().size());

    assertEquals("property confidence", 0.8259225426255177,
      response.body().getMatchings().get(0).getConfidence(), 0);
    assertEquals("property distance", 289.20000000000005,
      response.body().getMatchings().get(0).getDistance(), 0);
    assertEquals("property duration", 42.9,
      response.body().getMatchings().get(0).getDuration(), 0);

    // Test indices
    List<MapMatchingTracepoint> tracepoints = response.body().getTracepoints();
    assertEquals("property indices count", 5, tracepoints.size());
    assertEquals("matchings index", tracepoints.get(0).getMatchingsIndex(), 0);
    assertEquals("waypoint index", tracepoints.get(0).getWaypointIndex(), 0);
    assertEquals("alternatives count", tracepoints.get(0).getAlternativesCount(), 0);
  }

  @Test
  public void setLanguage_urlDoesContainLanguageParam() throws IOException {
    MapboxMapMatching client = new MapboxMapMatching.Builder()
      .setAccessToken("pk.XXX")
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setCoordinates(coordinates)
      .setLanguage("sv")
      .build();

    String callUrl = client.executeCall().raw().request().url().toString();
    assertTrue(
      callUrl.contains("language=sv")
    );
  }

  @Test
  public void setLanguage_doesReturnCorrectTurnInstructionLanguage() throws IOException {
    MapboxMapMatching client = new MapboxMapMatching.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(coordinates)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setSteps(true)
      .setLanguage("sv")
      .build();

    Response<MapMatchingResponse> response = client.executeCall();
    assertTrue(response.body().getMatchings().get(0).getLegs().get(0)
      .getSteps().get(0).getManeuver().getInstruction().contains("Kör åt nordost på Adalbertstraße"));
  }

}
