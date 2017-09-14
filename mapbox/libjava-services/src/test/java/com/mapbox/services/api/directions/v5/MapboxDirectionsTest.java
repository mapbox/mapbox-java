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
import static org.junit.Assert.assertTrue;

public class MapboxDirectionsTest {

  private static final String DIRECTIONS_V5_FIXTURE = "src/test/fixtures/directions_v5.json";
  private static final String DIRECTIONS_V5_PRECISION6_FIXTURE = "src/test/fixtures/directions_v5_precision_6.json";
  private static final String DIRECTIONS_TRAFFIC_FIXTURE = "src/test/fixtures/directions_v5_traffic.json";
  private static final String DIRECTIONS_ROTARY_FIXTURE = "src/test/fixtures/directions_v5_fixtures_rotary.json";
  private static final String DIRECTIONS_V5_ANNOTATIONS_FIXTURE = "src/test/fixtures/directions_annotations_v5.json";
  private static final String DIRECTIONS_V5_ROUNDABOUTS_FIXTURE =
      "src/test/fixtures/directions_v5_roundabout_exits.json";

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
        if (request.getPath().contains("annotations")) {
          resource = DIRECTIONS_V5_ANNOTATIONS_FIXTURE;
        }
        if (request.getPath().contains("roundabout_exits")) {
          resource = DIRECTIONS_V5_ROUNDABOUTS_FIXTURE;
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
  public void requiredAccessToken() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Services requires setting a valid access token"));
    new MapboxDirections.Builder().build();
  }

  @Test
  public void callFactoryNonNull() throws ServicesException, IOException {
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
    assertEquals(response.code(), 200);
    assertEquals(response.body().getCode(), DirectionsCriteria.RESPONSE_OK);
  }

  @Test
  public void coordinatesOverLimit() throws ServicesException {
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
  public void coordinatesTrafficOverLimit() throws ServicesException {
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
  public void noProfileProvided() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("A profile is required for the Directions API. Use one of the profiles found in"));

    ArrayList<Position> coord = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      coord.add(Position.fromCoordinates(i, i));
    }

    new MapboxDirections.Builder().setAccessToken("pk.XXX").setCoordinates(coord).build();
  }

  @Test
  public void requiredCoordinates() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You should provide at least two coordinates (from/to)"));
    new MapboxDirections.Builder().setAccessToken("pk.XXX").setProfile(DirectionsCriteria.PROFILE_DRIVING).build();
  }

  @Test
  public void noneConstantAnnotationValue() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Annotation value must be one of the constant values"));
    new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setCoordinates(positions)
      .setAnnotation("test")
      .build();
  }

  @Test
  public void testSanity() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<DirectionsResponse> response = client.executeCall();
    assertEquals(response.code(), 200);
    assertEquals(response.body().getCode(), DirectionsCriteria.RESPONSE_OK);
  }

  @Test
  public void testRadius() throws ServicesException, IOException {
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
      .contains("radiuses=100;100;100"));
  }

  @Test
  public void testRadiusWithUnlimitedDistance() throws ServicesException, IOException {
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
      .contains("radiuses=100;unlimited;100"));
  }

  @Test
  public void testRadiusesContainingNegativeDistance() throws ServicesException {
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
  public void testAnnotations() throws ServicesException, IOException {
    MapboxDirections.Builder builder = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setAnnotation(DirectionsCriteria.ANNOTATION_DURATION);

    assertTrue(builder.setAnnotation(DirectionsCriteria.ANNOTATION_DURATION).build()
      .executeCall().raw().request().url().toString().contains("annotations=duration"));
    assertTrue(builder.setAnnotation(DirectionsCriteria.ANNOTATION_DISTANCE).build()
      .executeCall().raw().request().url().toString().contains("annotations=distance"));
    assertTrue(builder.setAnnotation(DirectionsCriteria.ANNOTATION_SPEED).build()
      .executeCall().raw().request().url().toString().contains("annotations=speed"));
    assertTrue(builder.setAnnotation(DirectionsCriteria.ANNOTATION_DURATION, DirectionsCriteria.ANNOTATION_SPEED)
      .build().executeCall().raw().request().url().toString().contains("annotations=duration,speed"));
  }

  @Test
  public void testBearing() throws ServicesException, IOException {
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
      .contains("bearings=60,45;;45,45"));
  }

  @Test
  public void testBearingNotEnoughBearingsGiven() throws ServicesException {
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
  public void testBearingArrayLengthNotExactlyTwo() throws ServicesException {
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
  public void testDirectionsResponse() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<DirectionsResponse> response = client.executeCall();

    DirectionsResponse body = response.body();
    assertEquals(body.getCode(), DirectionsCriteria.RESPONSE_OK);
    assertEquals(body.getRoutes().size(), 1);
    assertEquals(body.getWaypoints().size(), 2);
  }

  @Test
  public void testDirectionsRoute() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();
    Response<DirectionsResponse> response = client.executeCall();

    DirectionsRoute route = response.body().getRoutes().get(0);
    assertEquals(route.getDistance(), 77254.7, DELTA);
    assertEquals(route.getDuration(), 3282.8, DELTA);
    assertTrue(route.getGeometry().startsWith("mqreFhodjV|q@yCxo@a`Av}AsNn"));
    assertEquals(route.getWeight(), 6524.2, DELTA);
    assertEquals(route.getWeightName(), DirectionsCriteria.WEIGHT_NAME_ROUTABILITY);
    assertEquals(route.getLegs().size(), 1);
  }

  @Test
  public void testGeometryPolyline6() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<DirectionsResponse> response = client.executeCall();

    DirectionsRoute route = response.body().getRoutes().get(0);
    assertEquals(route.getDistance(), 77254.7, DELTA);
    assertEquals(route.getDuration(), 3282.8, DELTA);
    assertTrue(route.getGeometry().startsWith("awbagAzavnhFd|"));
    assertEquals(route.getLegs().size(), 1);
  }

  @Test
  public void testDirectionsTrafficProfile() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();
    Response<DirectionsResponse> response = client.executeCall();
    DirectionsRoute route = response.body().getRoutes().get(0);
    assertEquals(route.getDistance(), 88465.5, DELTA);
    assertEquals(route.getDuration(), 4511.5, DELTA);
    assertEquals(route.getWeight(), 4510, DELTA);
    assertTrue(route.getGeometry().startsWith("mqreFhodjV|q@yCxo"));
    assertEquals(route.getLegs().size(), 1);

  }

  @Test
  public void testDirectionsWaypoint() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<DirectionsResponse> response = client.executeCall();

    DirectionsWaypoint waypoint = response.body().getWaypoints().get(0);
    assertEquals(waypoint.getName(), "Eddy Street");
    assertEquals(waypoint.asPosition().getLongitude(), -122.416686, DELTA);
    assertEquals(waypoint.asPosition().getLatitude(), 37.783425, DELTA);
  }

  @Test
  public void testRouteLeg() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();
    Response<DirectionsResponse> response = client.executeCall();

    RouteLeg leg = response.body().getRoutes().get(0).getLegs().get(0);
    assertEquals(leg.getDistance(), 77254.7, DELTA);
    assertEquals(leg.getDuration(), 3282.8, DELTA);
    assertEquals(leg.getSummary(), "Bayshore Freeway, Bayshore Freeway");
    assertEquals(leg.getSteps().size(), 15);
  }

  @Test
  public void testLegStep() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();
    Response<DirectionsResponse> response = client.executeCall();

    LegStep step = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(0);
    assertEquals(step.getDistance(), 69.7, DELTA);
    assertEquals(step.getDuration(), 23.5, DELTA);
    assertTrue(step.getGeometry().contains("mqreFhodjVQ}C"));
    assertEquals(step.getName(), "Eddy Street");
    assertEquals(step.getMode(), "driving");
    assertEquals(step.getWeight(), 42.7, DELTA);
    assertNotNull(step.getManeuver());
    assertEquals(step.getIntersections().size(), 1);
  }

  @Test
  public void testStepIntersection() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();
    Response<DirectionsResponse> response = client.executeCall();

    StepIntersection intersection = response.body().getRoutes().get(0).getLegs()
      .get(0).getSteps().get(1).getIntersections().get(0);
    assertEquals(intersection.asPosition().getLongitude(), -122.415903, DELTA);
    assertEquals(intersection.asPosition().getLatitude(), 37.783524, DELTA);
    assertArrayEquals(intersection.getBearings(), new int[] {75, 165, 255, 345});
    assertArrayEquals(intersection.getEntry(), new boolean[] {true, true, false, false});
    assertEquals(intersection.getIn(), 2);
    assertEquals(intersection.getOut(), 1);
  }

  @Test
  public void testIntersectionLanes() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();
    Response<DirectionsResponse> response = client.executeCall();

    IntersectionLanes intersectionLanes = response.body().getRoutes().get(0).getLegs()
      .get(0).getSteps().get(3).getIntersections().get(0).getLanes()[3];
    assertEquals(intersectionLanes.getValid(), true);
    assertEquals(intersectionLanes.getIndications()[0], "right");


  }

  @Test
  public void testStepManeuver() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE)
      .build();
    Response<DirectionsResponse> response = client.executeCall();

    StepManeuver maneuver = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(0).getManeuver();
    assertEquals(maneuver.asPosition().getLongitude(), -122.416686, DELTA);
    assertEquals(maneuver.asPosition().getLatitude(), 37.783425, DELTA);
    assertEquals(maneuver.getBearingBefore(), 0, DELTA);
    assertEquals(maneuver.getBearingAfter(), 81, DELTA);
    assertEquals(maneuver.getType(), "depart");
    assertEquals(maneuver.getModifier(), "right");
    assertEquals(maneuver.getInstruction(), "Head east on Eddy Street");
    assertEquals(maneuver.getExit(), null);
  }


  @Test
  public void testRotaryLegStepAndStepManeuver() throws ServicesException, IOException {
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

    StepManeuver maneuver = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(1).getManeuver();
    assertEquals(maneuver.asPosition().getLongitude(), -77.043755, DELTA);
    assertEquals(maneuver.asPosition().getLatitude(), 38.909075, DELTA);
    assertEquals(maneuver.getBearingBefore(), 84.0, DELTA);
    assertEquals(maneuver.getBearingAfter(), 111.0, DELTA);
    assertEquals(maneuver.getType(), "rotary");
    assertEquals(maneuver.getModifier(), "slight right");
    assertEquals(maneuver.getInstruction(),
      "Enter Dupont Circle Northwest and take the 3rd exit onto P Street Northwest");
    assertEquals(maneuver.getExit(), Integer.valueOf(3));


    LegStep step = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(1);
    assertEquals(step.getRotaryName(), "Dupont Circle Northwest");
    assertEquals(step.getRotaryPronunciation(), null);
    assertEquals(step.getPronunciation(), null);
  }

  @Test
  public void testSetCoordinates() {
    List<Position> test = new ArrayList<>();
    test.add(Position.fromCoordinates(2.1, 2.2));
    test.add(Position.fromCoordinates(3.1, 3.2));

    String coordinates = new MapboxDirections.Builder()
      .setCoordinates(test)
      .getCoordinates();
    assertEquals(coordinates, "2.1,2.2;3.1,3.2");
  }

  @Test
  public void setOriginDestination() {
    String coordinates = new MapboxDirections.Builder()
      .setOrigin(Position.fromCoordinates(2.1, 2.2))
      .setDestination(Position.fromCoordinates(3.1, 3.2))
      .getCoordinates();
    assertEquals(coordinates, "2.1,2.2;3.1,3.2");
  }

  @Test
  public void testSetCoordinatesMixed() {
    List<Position> test = new ArrayList<>();
    test.add(Position.fromCoordinates(2.1, 2.2));
    test.add(Position.fromCoordinates(3.1, 3.2));

    // Respect previously entered coordinates
    String coordinates = new MapboxDirections.Builder()
      .setCoordinates(test)
      .setOrigin(Position.fromCoordinates(1.1, 1.2))
      .setDestination(Position.fromCoordinates(4.1, 4.2))
      .getCoordinates();
    assertEquals(coordinates, "1.1,1.2;2.1,2.2;3.1,3.2;4.1,4.2");
  }

  @Test
  public void testLocale() {
    List<Position> test = new ArrayList<>();
    test.add(Position.fromCoordinates(2.1, 2.2));
    test.add(Position.fromCoordinates(3.1, 3.2));

    // Locale shouldn't matter (#39)
    Locale.setDefault(Locale.GERMANY);
    String coordinates = new MapboxDirections.Builder()
      .setOrigin(Position.fromCoordinates(1.1, 1.2))
      .setDestination(Position.fromCoordinates(4.1, 4.2))
      .setCoordinates(test)
      .getCoordinates();
    assertEquals(coordinates, "1.1,1.2;2.1,2.2;3.1,3.2;4.1,4.2");
  }

  @Test
  public void testUserAgent() throws ServicesException, IOException {
    MapboxDirections service = new MapboxDirections.Builder()
      .setClientAppName("APP")
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();
    assertTrue(service.executeCall().raw().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void originDestinationCoordinatesListCorrectOrder() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setOrigin(Position.fromCoordinates(-122.4313, 37.7789))
      .setCoordinates(positions)
      .setDestination(Position.fromCoordinates(-121.8001, 37.2275))
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .build();

    String callUrl = client.executeCall().raw().request().url().toString();
    assertTrue(
      callUrl.contains("-122.4313,37.7789;-122.416667,37.783333;-121.9,37.333333;-121.8001,37.2275")
    );
  }

  @Test
  public void setLanguage_urlDoesContainLangaugeParam() throws IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setLanguage("sv")
      .setAnnotation(DirectionsCriteria.ANNOTATION_DURATION)
      .build();

    String callUrl = client.executeCall().raw().request().url().toString();
    assertTrue(
      callUrl.contains("language=sv")
    );
  }

  @Test
  public void setLanguage_doesReturnCorrectTurnInstructionLanguage() throws IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setBaseUrl(mockUrl.toString())
      .setLanguage("sv")
      .setAnnotation(DirectionsCriteria.ANNOTATION_DURATION)
      .build();

    Response<DirectionsResponse> response = client.executeCall();
    assertTrue(response.body().getRoutes().get(0).getLegs().get(0)
      .getSteps().get(0).getManeuver().getInstruction().contains("Kör åt öster på Eddy Street"));
  }

  @Test
  public void testRoundaboutExitsResponse() throws IOException {
    MapboxDirections client = new MapboxDirections.Builder()
        .setAccessToken("pk.XXX")
        .setCoordinates(positions)
        .setProfile(DirectionsCriteria.PROFILE_DRIVING)
        .setBaseUrl(mockUrl.toString())
        .setRoundaboutExits(true)
        .build();

    Response<DirectionsResponse> response = client.executeCall();
    assertEquals(response.body().getRoutes().get(0).getLegs().get(0)
      .getSteps().get(2).getManeuver().getType(), "exit roundabout");
  }
}
