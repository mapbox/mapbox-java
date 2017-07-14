package com.mapbox.services.api.optimizedtrips.v1;

import com.mapbox.services.api.BaseTest;
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

public class MapboxOptimizedTripsTest extends BaseTest {

  private static final String OPTIMIZED_TRIP_FIXTURE = "src/test/fixtures/optimized_trip.json";
  private static final String OPTIMIZED_TRIP_STEP_FIXTURE = "src/test/fixtures/optimized_trip_steps.json";
  private static final String OPTIMIZED_TRIP_DISTRIBUTIONS_FIXTURE
    = "src/test/fixtures/optimized_trip_distributions.json";

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

        String resource = OPTIMIZED_TRIP_FIXTURE;
        if (request.getPath().contains("language=sv")) {
          resource = OPTIMIZED_TRIP_STEP_FIXTURE;
        } else if (request.getPath().contains("distributions")) {
          resource = OPTIMIZED_TRIP_DISTRIBUTIONS_FIXTURE;
        }

        try {
          String body = new String(Files.readAllBytes(Paths.get(resource)), Charset.forName("utf-8"));
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
  public void testCallSanity() throws IOException {
    MapboxOptimizedTrips client = new MapboxOptimizedTrips.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setCoordinates(coordinates)
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<OptimizedTripsResponse> response = client.executeCall();
    assertEquals(response.code(), 200);

    // Check the response body
    assertNotNull(response.body());
    assertEquals(1, response.body().getTrips().size());
    assertNotNull(response.body().getTrips().get(0).getGeometry());
  }

  @Test
  public void requiredAccessToken() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Services requires setting a valid access token"));
    new MapboxOptimizedTrips.Builder().build();
  }

  @Test
  public void validProfileNonNull() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("A profile is required for the Optimized Trips API."));
    new MapboxOptimizedTrips.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(null)
      .build();
  }

  @Test
  public void validCoordinates() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("At least two coordinates must be provided with your API request."));
    new MapboxOptimizedTrips.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .build();
  }

  @Test
  public void validCoordinatesTotal() throws ServicesException {
    int total = 13;
    List<Position> positions = new ArrayList<>();
    for (int i = 0; i < total; i++) {
      // Fake too many positions
      positions.add(Position.fromCoordinates(0.0, 0.0));
    }

    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Maximum of 12 coordinates are allowed for this API."));
    new MapboxOptimizedTrips.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setCoordinates(positions)
      .build();
  }

  @Test
  public void testUserAgent() throws ServicesException, IOException {
    MapboxOptimizedTrips client = new MapboxOptimizedTrips.Builder()
      .setClientAppName("APP")
      .setAccessToken("pk.XXX")
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setCoordinates(coordinates)
      .setBaseUrl(mockUrl.toString())
      .build();
    assertTrue(client.executeCall().raw().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void simpleOptimizedTripCalled() throws IOException {
    List<Position> coords = new ArrayList<>();
    coords.add(Position.fromCoordinates(-122.42, 37.78));
    coords.add(Position.fromCoordinates(-122.45, 37.91));
    coords.add(Position.fromCoordinates(-122.48, 37.73));

    MapboxOptimizedTrips client = new MapboxOptimizedTrips.Builder()
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setAccessToken("pk.XXX")
      .setBaseUrl(mockUrl.toString())
      .setCoordinates(coords)
      .build();

    Response<OptimizedTripsResponse> response = client.executeCall();
    OptimizedTripsResponse trip = response.body();
    assertEquals(trip.getTrips().size(), 1);

    // Test entire route
    assertEquals(trip.getTrips().get(0).getDistance(), 70717.79999999999, DELTA);
    assertEquals(trip.getTrips().get(0).getDuration(), 5933.799999999999, DELTA);
    assertTrue(trip.getTrips().get(0).getGeometry().contains("q|qeFbdejV}cCzZrNv|Bi[nj@nIb_AeGvk@s_@pn@gm"));

    // Test leg returned
    assertEquals(trip.getTrips().get(0).getLegs().get(0).getDistance(), 25785.8, DELTA);
    assertEquals(trip.getTrips().get(0).getLegs().get(0).getDuration(), 2206.1, DELTA);

    assertEquals(trip.getWaypoints().size(), 3);
    assertTrue(trip.getWaypoints().get(0).getName().contains("McAllister Street"));
  }

  @Test
  public void setLanguage_urlDoesContainLangaugeParam() throws IOException {
    List<Position> coords = new ArrayList<>();
    coords.add(Position.fromCoordinates(-122.42, 37.78));
    coords.add(Position.fromCoordinates(-122.45, 37.91));
    coords.add(Position.fromCoordinates(-122.48, 37.73));

    MapboxOptimizedTrips client = new MapboxOptimizedTrips.Builder()
      .setAccessToken("pk.XXX")
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setCoordinates(coords)
      .setLanguage("sv")
      .setAnnotations(DirectionsCriteria.ANNOTATION_DURATION)
      .build();

    String callUrl = client.executeCall().raw().request().url().toString();
    assertTrue(
      callUrl.contains("language=sv")
    );
  }

  @Test
  public void setLanguage_doesReturnCorrectTurnInstructionLanguage() throws IOException {
    List<Position> coords = new ArrayList<>();
    coords.add(Position.fromCoordinates(-122.42, 37.78));
    coords.add(Position.fromCoordinates(-122.45, 37.91));
    coords.add(Position.fromCoordinates(-122.48, 37.73));

    MapboxOptimizedTrips client = new MapboxOptimizedTrips.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(coords)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setLanguage("sv")
      .setAnnotations(DirectionsCriteria.ANNOTATION_DURATION)
      .build();

    Response<OptimizedTripsResponse> response = client.executeCall();
    assertTrue(response.body().getTrips().get(0).getLegs().get(0)
      .getSteps().get(0).getManeuver().getInstruction().contains("Kör åt väster på McAllister Street"));
  }

  @Test
  public void setDistribution_urlDoesContainDistributionParam() throws IOException {
    List<Position> coords = new ArrayList<>();
    coords.add(Position.fromCoordinates(13.388860, 52.517037));
    coords.add(Position.fromCoordinates(13.397634, 52.529407));
    coords.add(Position.fromCoordinates(13.428555, 52.523219));
    coords.add(Position.fromCoordinates(13.418555, 52.523215));

    MapboxOptimizedTrips client = new MapboxOptimizedTrips.Builder()
      .setAccessToken("pk.XXX")
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setCoordinates(coords)
      .setRoundTrip(true)
      .setDistributions(new double[] {3, 1})
      .build();

    String callUrl = client.executeCall().raw().request().url().toString();
    assertTrue(
      callUrl.contains("distributions=3,1")
    );
  }

  @Test
  public void optimizationResult_doesContainWaypointIndex() throws IOException {
    List<Position> coords = new ArrayList<>();
    coords.add(Position.fromCoordinates(13.388860, 52.517037));
    coords.add(Position.fromCoordinates(13.397634, 52.529407));
    coords.add(Position.fromCoordinates(13.428555, 52.523219));
    coords.add(Position.fromCoordinates(13.418555, 52.523215));

    MapboxOptimizedTrips client = new MapboxOptimizedTrips.Builder()
      .setAccessToken("pk.XXX")
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setCoordinates(coords)
      .build();

    Response<OptimizedTripsResponse> response = client.executeCall();
    assertEquals(response.body().getWaypoints().get(1).getWaypointIndex(), 1);
  }
}
