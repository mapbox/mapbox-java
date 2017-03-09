package com.mapbox.services.api.directions.v5;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.api.directions.v5.models.DirectionsWaypoint;
import com.mapbox.services.api.directions.v5.models.IntersectionLanes;
import com.mapbox.services.api.directions.v5.models.LegStep;
import com.mapbox.services.api.directions.v5.models.RouteLeg;
import com.mapbox.services.api.directions.v5.models.StepIntersection;
import com.mapbox.services.api.directions.v5.models.StepManeuver;
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
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MapboxDirectionsTest {

  private static final String DIRECTIONS_V5_FIXTURE = "src/test/fixtures/directions_v5.json";
  private static final String DIRECTIONS_V5_PRECISION6_FIXTURE = "src/test/fixtures/directions_v5_precision_6.json";
  private static final String DIRECTIONS_TRAFFIC_FIXTURE = "src/test/fixtures/directions_v5_traffic.json";
  private static final String DIRECTIONS_ROTARY_FIXTURE = "src/test/fixtures/directions_v5_fixtures_rotary.json";
  private static final double DELTA = 1E-10;

  private MockWebServer server;
  private HttpUrl mockUrl;

  private ArrayList<Position> positions;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new Dispatcher() {

      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

        // Switch response on geometry parameter (only false supported, so nice and simple)
        String resource = DIRECTIONS_V5_FIXTURE;
        if (request.getPath().contains("geometries=polyline6")) {
          resource = DIRECTIONS_V5_PRECISION6_FIXTURE;
        }
        if (request.getPath().contains("driving-traffic")) {
          resource = DIRECTIONS_TRAFFIC_FIXTURE;
        }
        if (request.getPath().contains("-77.04430")) {
          resource = DIRECTIONS_ROTARY_FIXTURE;
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
  public void checksAccessTokenIsRequired() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Services requires setting a valid access token"));

    new MapboxDirections.Builder().build();
  }

  @Test
  public void checksCallFactoryCouldBeNull() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();

    // Setting a null call factory doesn't make the request fail
    // (the default OkHttp client is used)
    client.setCallFactory(null);
    Response<DirectionsResponse> response = client.executeCall();

    assertEquals(200, response.code());
    assertEquals(DirectionsCriteria.RESPONSE_OK, response.body().getCode());
  }

  @Test
  public void checksCoordinatesOverLimit() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("All profiles (except driving-traffic) allows for maximum of 25 coordinates."));

    ArrayList<Position> coord = new ArrayList<>();
    for (int i = 0; i < 26; i++) {
      coord.add(Position.fromCoordinates(i, i));
    }

    new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setCoordinates(coord).build();
  }

  @Test
  public void checksTrafficCoordinatesOverLimit() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using the driving-traffic profile allows for maximum of 3 coordinates."));

    ArrayList<Position> coord = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      coord.add(Position.fromCoordinates(i, i));
    }

    new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setProfile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .setCoordinates(coord).build();
  }

  @Test
  public void checksProfileIsRequired() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("A profile is required for the Directions API. Use one of the profiles found in"));

    ArrayList<Position> coord = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      coord.add(Position.fromCoordinates(i, i));
    }

    new MapboxDirections.Builder().setAccessToken("pk.XXX").setCoordinates(coord).build();
  }

  @Test
  public void checksCoordinatesAreRequired() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You should provide at least two coordinates (from/to)"));

    new MapboxDirections.Builder().setAccessToken("pk.XXX").setProfile(DirectionsCriteria.PROFILE_DRIVING).build();
  }

  @Test
  public void checksSanity() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();

    Response<DirectionsResponse> response = client.executeCall();

    assertEquals(200, response.code());
    assertEquals(DirectionsCriteria.RESPONSE_OK, response.body().getCode());
  }

  @Test
  public void checksRadius() throws ServicesException, IOException {
    List<Position> coordinates = new ArrayList<>();
    coordinates.add(Position.fromCoordinates(13.4301, 52.5109));
    coordinates.add(Position.fromCoordinates(13.4265, 52.5080));
    coordinates.add(Position.fromCoordinates(13.4316, 52.5021));

    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(coordinates)
      .setRadiuses(new double[] {100, 100, 100})
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();

    assertTrue(client.executeCall().raw().request().url().toString()
      .contains("radiuses=100.000000;100.000000;100.000000"));
  }

  @Test
  public void checksRadiusWithUnlimitedDistance() throws ServicesException, IOException {
    List<Position> coordinates = new ArrayList<>();
    coordinates.add(Position.fromCoordinates(13.4301, 52.5109));
    coordinates.add(Position.fromCoordinates(13.4265, 52.5080));
    coordinates.add(Position.fromCoordinates(13.4316, 52.5021));

    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(coordinates)
      .setRadiuses(new double[] {100, Double.POSITIVE_INFINITY, 100})
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();

    assertTrue(client.executeCall().raw().request().url().toString()
      .contains("radiuses=100.000000;unlimited;100.000000"));
  }

  @Test
  public void checksNegativeRadiusesNotAllowed() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Radius values need to be greater than zero."));

    new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setRadiuses(new double[] {-1, -2})
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void checksBearings() throws ServicesException, IOException {
    List<Position> coordinates = new ArrayList<>();
    coordinates.add(Position.fromCoordinates(13.4301, 52.5109));
    coordinates.add(Position.fromCoordinates(13.4265, 52.5080));
    coordinates.add(Position.fromCoordinates(13.4316, 52.5021));

    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(coordinates)
      .setBearings(new double[] {60, 45}, new double[] {}, new double[] {45, 45})
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();

    assertTrue(client.executeCall().raw().request().url().toString()
      .contains("bearings=60.000000,45.000000;;45.000000,45.000000"));
  }

  @Test
  public void checksLessBearingsThanCoordinatesNotAllowed() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("There must be as many bearings as there are coordinates."));

    new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setBearings(new double[] {})
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void checksTwoBearingsNotAllowed() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Requesting a route which includes bearings requires"));

    new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setBearings(new double[] {0, 0}, new double[] {0})
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void checksDirectionsResponse() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();

    Response<DirectionsResponse> response = client.executeCall();
    DirectionsResponse body = response.body();

    assertEquals(DirectionsCriteria.RESPONSE_OK, body.getCode());
    assertEquals(1, body.getRoutes().size());
    assertEquals(2, body.getWaypoints().size());
  }

  @Test
  public void checksDirectionsRoute() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();

    Response<DirectionsResponse> response = client.executeCall();
    DirectionsRoute route = response.body().getRoutes().get(0);

    assertEquals(77274.3, route.getDistance(), DELTA);
    assertEquals(3441.8, route.getDuration(), DELTA);
    assertTrue(route.getGeometry().startsWith("kqreFhodjV"));
    assertEquals(1, route.getLegs().size());
  }

  @Test
  public void checksGeometryPolyline6() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();

    Response<DirectionsResponse> response = client.executeCall();

    DirectionsRoute route = response.body().getRoutes().get(0);
    assertEquals(77255.1, route.getDistance(), DELTA);
    assertEquals(3935, route.getDuration(), DELTA);
    assertTrue(route.getGeometry().startsWith("_wbagAxavn"));
    assertEquals(1, route.getLegs().size());
  }

  @Test
  public void checksDirectionsTrafficProfile() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();

    Response<DirectionsResponse> response = client.executeCall();
    DirectionsRoute route = response.body().getRoutes().get(0);

    assertEquals(88549, route.getDistance(), DELTA);
    assertEquals(3520.8, route.getDuration(), DELTA);
    assertTrue(route.getGeometry().startsWith("kqreFhodjV"));
    assertEquals(1, route.getLegs().size());
  }

  @Test
  public void checksDirectionsWaypoint() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();

    Response<DirectionsResponse> response = client.executeCall();
    DirectionsWaypoint waypoint = response.body().getWaypoints().get(0);

    assertEquals("Eddy Street", waypoint.getName());
    assertEquals(-122.416685, waypoint.asPosition().getLongitude(), DELTA);
    assertEquals(37.783424, waypoint.asPosition().getLatitude(), DELTA);
  }

  @Test
  public void checksRouteLeg() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();

    Response<DirectionsResponse> response = client.executeCall();
    RouteLeg leg = response.body().getRoutes().get(0).getLegs().get(0);

    assertEquals(77274.3, leg.getDistance(), DELTA);
    assertEquals(3441.8, leg.getDuration(), DELTA);
    assertEquals("Bayshore Freeway, Bayshore Freeway", leg.getSummary());
    assertEquals(13, leg.getSteps().size());
  }

  @Test
  public void checksLegStep() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();

    Response<DirectionsResponse> response = client.executeCall();
    LegStep step = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(0);

    assertEquals(223.1, step.getDistance(), DELTA);
    assertEquals(52.0, step.getDuration(), DELTA);
    assertEquals("kqreFhodjVRjDh@fI", step.getGeometry());
    assertEquals("Eddy Street", step.getName());
    assertEquals("driving", step.getMode());
    assertNotNull(step.getManeuver());
    assertEquals(2, step.getIntersections().size());
  }

  @Test
  public void checksStepIntersection() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();

    Response<DirectionsResponse> response = client.executeCall();
    StepIntersection intersection = response.body().getRoutes().get(0).getLegs()
      .get(0).getSteps().get(0).getIntersections().get(1);

    assertEquals(-122.417548, intersection.asPosition().getLongitude(), DELTA);
    assertEquals(37.783315, intersection.asPosition().getLatitude(), DELTA);
    assertArrayEquals(new int[] {75, 165, 255, 345}, intersection.getBearings());
    assertArrayEquals(new boolean[] {false, false, true, true}, intersection.getEntry());
    assertEquals(0, intersection.getIn());
    assertEquals(2, intersection.getOut());
  }

  @Test
  public void checksIntersectionLanes() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();

    Response<DirectionsResponse> response = client.executeCall();
    IntersectionLanes intersectionLanes = response.body().getRoutes().get(0).getLegs()
      .get(0).getSteps().get(1).getIntersections().get(8).getLanes()[0];

    assertTrue(intersectionLanes.getValid());
    assertEquals("none", intersectionLanes.getIndications()[0]);
  }

  @Test
  public void checksStepManeuver() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();

    Response<DirectionsResponse> response = client.executeCall();
    StepManeuver maneuver = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(0).getManeuver();

    assertEquals(-122.416685, maneuver.asPosition().getLongitude(), DELTA);
    assertEquals(37.783424, maneuver.asPosition().getLatitude(), DELTA);
    assertEquals(0, maneuver.getBearingBefore(), DELTA);
    assertEquals(261, maneuver.getBearingAfter(), DELTA);
    assertEquals("depart", maneuver.getType());
    assertEquals("left", maneuver.getModifier());
    assertEquals("Head west on Eddy Street", maneuver.getInstruction());
    assertNull(maneuver.getExit());
  }


  @Test
  public void checksRotaryLegStepAndStepManeuver() throws ServicesException, IOException {
    List<Position> positionsExit = new ArrayList<>();
    positionsExit.add(Position.fromCoordinates(-77.04430818557739, 38.908650612656864));
    positionsExit.add(Position.fromCoordinates(-77.04192638397217, 38.90963574367117));
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positionsExit)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();

    Response<DirectionsResponse> response = client.executeCall();
    LegStep step = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(1);
    StepManeuver maneuver = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(1).getManeuver();

    assertEquals("Dupont Circle Northwest", step.getRotaryName());
    assertNull(step.getRotaryPronunciation());
    assertNull(step.getPronunciation());

    assertEquals(-77.043755, maneuver.asPosition().getLongitude(), DELTA);
    assertEquals(38.909075, maneuver.asPosition().getLatitude(), DELTA);
    assertEquals(84.0, maneuver.getBearingBefore(), DELTA);
    assertEquals(111.0, maneuver.getBearingAfter(), DELTA);
    assertEquals("rotary", maneuver.getType());
    assertEquals("slight right", maneuver.getModifier());
    assertEquals("Enter Dupont Circle Northwest and take the 3rd exit onto P Street Northwest",
      maneuver.getInstruction());
    assertEquals(Integer.valueOf(3), maneuver.getExit());
  }

  @Test
  public void checksSetCoordinates() {
    ArrayList aCoupleOfCoordinates = new ArrayList<>();
    aCoupleOfCoordinates.add(Position.fromCoordinates(2.1, 2.2));
    aCoupleOfCoordinates.add(Position.fromCoordinates(3.1, 3.2));

    String coordinates = new MapboxDirections.Builder()
      .setCoordinates(aCoupleOfCoordinates)
      .getCoordinates();

    assertEquals("2.100000,2.200000;3.100000,3.200000", coordinates);
  }

  @Test
  public void checksSetOriginDestination() {
    String coordinates = new MapboxDirections.Builder()
      .setOrigin(Position.fromCoordinates(2.1, 2.2))
      .setDestination(Position.fromCoordinates(3.1, 3.2))
      .getCoordinates();

    assertEquals("2.100000,2.200000;3.100000,3.200000", coordinates);
  }

  @Test
  public void checksSetCoordinatesMixed() {
    ArrayList moreThanTwoCoordinates = new ArrayList<>();
    moreThanTwoCoordinates.add(Position.fromCoordinates(2.1, 2.2));
    moreThanTwoCoordinates.add(Position.fromCoordinates(3.1, 3.2));
    moreThanTwoCoordinates.add(Position.fromCoordinates(4.1, 4.2));

    // Respect previously entered coordinates
    String coordinates = new MapboxDirections.Builder()
      .setCoordinates(moreThanTwoCoordinates)
      .setOrigin(Position.fromCoordinates(1.1, 1.2))
      .setDestination(Position.fromCoordinates(5.1, 5.2))
      .getCoordinates();

    assertEquals("1.100000,1.200000;2.100000,2.200000;3.100000,3.200000;4.100000,4.200000;5.100000,5.200000",
      coordinates);
  }

  @Test
  public void checksSetCoordinatesMixedHidden() {
    ArrayList moreThanTwoCoordinates = new ArrayList<>();
    moreThanTwoCoordinates.add(Position.fromCoordinates(2.1, 2.2));
    moreThanTwoCoordinates.add(Position.fromCoordinates(3.1, 3.2));
    moreThanTwoCoordinates.add(Position.fromCoordinates(4.1, 4.2));

    // The order matters
    String coordinates = new MapboxDirections.Builder()
      .setOrigin(Position.fromCoordinates(1.1, 1.2))
      .setDestination(Position.fromCoordinates(5.1, 5.2))
      .setCoordinates(moreThanTwoCoordinates)
      .getCoordinates();

    assertEquals("2.100000,2.200000;3.100000,3.200000;4.100000,4.200000", coordinates);
  }

  @Test
  public void checksLocale() {
    ArrayList aCoupleOfCoordinates = new ArrayList<>();
    aCoupleOfCoordinates.add(Position.fromCoordinates(2.1, 2.2));
    aCoupleOfCoordinates.add(Position.fromCoordinates(3.1, 3.2));
    // Locale shouldn't matter (#39)
    Locale.setDefault(Locale.GERMANY);

    String coordinates = new MapboxDirections.Builder()
      .setOrigin(Position.fromCoordinates(1.1, 1.2))
      .setDestination(Position.fromCoordinates(4.1, 4.2))
      .setCoordinates(aCoupleOfCoordinates)
      .getCoordinates();

    assertEquals("2.100000,2.200000;3.100000,3.200000", coordinates);
  }

  @Test
  public void checksUserAgent() throws ServicesException, IOException {
    MapboxDirections service = new MapboxDirections.Builder()
      .setClientAppName("APP")
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();

    assertTrue(service.executeCall().raw().request().header("User-Agent").contains("APP"));
  }
}
