package com.mapbox.services.directions.v5;

import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;
import com.mapbox.services.directions.v5.models.DirectionsWaypoint;
import com.mapbox.services.directions.v5.models.LegStep;
import com.mapbox.services.directions.v5.models.RouteLeg;
import com.mapbox.services.directions.v5.models.StepIntersection;
import com.mapbox.services.directions.v5.models.StepManeuver;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class MapboxDirectionsTest {

  private final static double DELTA = 1E-10;

  private MockWebServer server;
  private HttpUrl mockUrl;

  private ArrayList<Position> positions;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    byte[] content = Files.readAllBytes(Paths.get("src/test/fixtures/directions_v5.json"));
    String body = new String(content, Charset.defaultCharset());
    server.enqueue(new MockResponse().setBody(body));

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
  public void requiredCoordinates() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You should provide at least two coordinates (from/to)"));
    new MapboxDirections.Builder().setAccessToken("pk.XXX").build();
  }

  @Test
  public void testSanity() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<DirectionsResponse> response = client.executeCall();
    assertEquals(response.code(), 200);
    assertEquals(response.body().getCode(), DirectionsCriteria.RESPONSE_OK);
  }

  @Test
  public void testDirectionsResponse() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .build();
    client.setBaseUrl(mockUrl.toString());
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
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<DirectionsResponse> response = client.executeCall();

    DirectionsRoute route = response.body().getRoutes().get(0);
    assertEquals(route.getDistance(), 77274.3, DELTA);
    assertEquals(route.getDuration(), 3444.2, DELTA);
    assertTrue(route.getGeometry().startsWith("kqreFhodjVhh"));
    assertEquals(route.getLegs().size(), 1);
  }

  @Test
  public void testDirectionsWaypoint() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<DirectionsResponse> response = client.executeCall();

    DirectionsWaypoint waypoint = response.body().getWaypoints().get(0);
    assertEquals(waypoint.getName(), "Eddy Street");
    assertEquals(waypoint.asPosition().getLongitude(), -122.416685, DELTA);
    assertEquals(waypoint.asPosition().getLatitude(), 37.783424, DELTA);
  }

  @Test
  public void testRouteLeg() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<DirectionsResponse> response = client.executeCall();

    RouteLeg leg = response.body().getRoutes().get(0).getLegs().get(0);
    assertEquals(leg.getDistance(), 77274.3, DELTA);
    assertEquals(leg.getDuration(), 3444.2, DELTA);
    assertEquals(leg.getSummary(), "James Lick Freeway (US 101), Bayshore Freeway (US 101)");
    assertEquals(leg.getSteps().size(), 14);
  }

  @Test
  public void testLegStep() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<DirectionsResponse> response = client.executeCall();

    LegStep step = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(0);
    assertEquals(step.getDistance(), 223.1, DELTA);
    assertEquals(step.getDuration(), 52.0, DELTA);
    assertEquals(step.getGeometry(), "kqreFhodjVRjDh@fI");
    assertEquals(step.getName(), "Eddy Street");
    assertEquals(step.getMode(), "driving");
    assertNotEquals(step.getManeuver(), null);
    assertEquals(step.getIntersections().size(), 2);
  }

  @Test
  public void testStepIntersection() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<DirectionsResponse> response = client.executeCall();

    StepIntersection intersection = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(0).getIntersections().get(1);
    assertEquals(intersection.asPosition().getLongitude(), -122.417548, DELTA);
    assertEquals(intersection.asPosition().getLatitude(), 37.783315, DELTA);
    assertArrayEquals(intersection.getBearings(), new int[] {75, 165, 255, 345});
    assertArrayEquals(intersection.getEntry(), new boolean[] {false, false, true, true});
    assertEquals(intersection.getIn(), 0);
    assertEquals(intersection.getOut(), 2);
  }

  @Test
  public void testStepManeuver() throws ServicesException, IOException {
    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken("pk.XXX")
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<DirectionsResponse> response = client.executeCall();

    StepManeuver maneuver = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(0).getManeuver();
    assertEquals(maneuver.asPosition().getLongitude(), -122.416685, DELTA);
    assertEquals(maneuver.asPosition().getLatitude(), 37.783424, DELTA);
    assertEquals(maneuver.getBearingBefore(), 0, DELTA);
    assertEquals(maneuver.getBearingAfter(), 261, DELTA);
    assertEquals(maneuver.getType(), "depart");
    assertEquals(maneuver.getModifier(), "left");
    assertEquals(maneuver.getInstruction(), "Head west on Eddy Street");
  }

  @Test
  public void testSetCoordinates() {
    ArrayList test = new ArrayList<>();
    test.add(Position.fromCoordinates(2.1, 2.2));
    test.add(Position.fromCoordinates(3.1, 3.2));

    String coordinates = new MapboxDirections.Builder()
      .setCoordinates(test)
      .getCoordinates();
    assertEquals(coordinates, "2.100000,2.200000;3.100000,3.200000");
  }

  @Test
  public void setOriginDestination() {
    String coordinates = new MapboxDirections.Builder()
      .setOrigin(Position.fromCoordinates(2.1, 2.2))
      .setDestination(Position.fromCoordinates(3.1, 3.2))
      .getCoordinates();
    assertEquals(coordinates, "2.100000,2.200000;3.100000,3.200000");
  }

  @Test
  public void testSetCoordinatesMixed() {
    ArrayList test = new ArrayList<>();
    test.add(Position.fromCoordinates(2.1, 2.2));
    test.add(Position.fromCoordinates(3.1, 3.2));

    // Respect previously entered coordinates
    String coordinates = new MapboxDirections.Builder()
      .setCoordinates(test)
      .setOrigin(Position.fromCoordinates(1.1, 1.2))
      .setDestination(Position.fromCoordinates(4.1, 4.2))
      .getCoordinates();
    assertEquals(coordinates, "1.100000,1.200000;2.100000,2.200000;3.100000,3.200000;4.100000,4.200000");
  }

  @Test
  public void testSetCoordinatesMixedHidden() {
    ArrayList test = new ArrayList<>();
    test.add(Position.fromCoordinates(2.1, 2.2));
    test.add(Position.fromCoordinates(3.1, 3.2));

    // The order matters
    String coordinates = new MapboxDirections.Builder()
      .setOrigin(Position.fromCoordinates(1.1, 1.2))
      .setDestination(Position.fromCoordinates(4.1, 4.2))
      .setCoordinates(test)
      .getCoordinates();
    assertEquals(coordinates, "2.100000,2.200000;3.100000,3.200000");
  }

  @Test
  public void TestLocale() {
    ArrayList test = new ArrayList<>();
    test.add(Position.fromCoordinates(2.1, 2.2));
    test.add(Position.fromCoordinates(3.1, 3.2));

    // Locale shouldn't matter (#39)
    Locale.setDefault(Locale.GERMANY);
    String coordinates = new MapboxDirections.Builder()
      .setOrigin(Position.fromCoordinates(1.1, 1.2))
      .setDestination(Position.fromCoordinates(4.1, 4.2))
      .setCoordinates(test)
      .getCoordinates();
    assertEquals(coordinates, "2.100000,2.200000;3.100000,3.200000");
  }

  @Test
  public void testUserAgent() throws ServicesException, IOException {
    MapboxDirections service = new MapboxDirections.Builder()
            .setClientAppName("APP")
            .setAccessToken("pk.XXX")
            .setCoordinates(positions)
            .setProfile(DirectionsCriteria.PROFILE_DRIVING)
            .build();
    service.setBaseUrl(mockUrl.toString());
    assertTrue(service.executeCall().raw().request().header("User-Agent").contains("APP"));
  }

}
