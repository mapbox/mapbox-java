package com.mapbox.services.services.directions.v5;

import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;
import com.mapbox.services.directions.v5.models.DirectionsWaypoint;
import com.mapbox.services.directions.v5.models.LegStep;
import com.mapbox.services.directions.v5.models.RouteLeg;
import com.mapbox.services.directions.v5.models.StepManeuver;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by antonio on 4/7/16.
 */
public class MapboxDirectionsTest {

    private final static double DELTA = 1E-10;


    private MockWebServer server;
    private HttpUrl mockUrl;

    private Position[] positions;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();

        byte[] content = Files.readAllBytes(Paths.get("src/test/java/com/mapbox/services/services/directions/v5/sample_response.json"));
        String body = new String(content, StandardCharsets.UTF_8);
        server.enqueue(new MockResponse().setBody(body));

        server.start();

        mockUrl = server.url("");

        positions = new Position[2];
        positions[0] = Position.fromCoordinates(-122.416667, 37.783333); // SF
        positions[1] = Position.fromCoordinates(-121.9, 37.333333); // SJ
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
        assertEquals(response.body().getCode(), DirectionsCriteria.RESPONSE_OK.toLowerCase());
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
        assertEquals(body.getCode(), DirectionsCriteria.RESPONSE_OK.toLowerCase());
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
        assertEquals(route.getDistance(), 9728.041736092327, DELTA);
        assertEquals(route.getDuration(), 1024.3, DELTA);
        assertTrue(route.getGeometry().startsWith("orneFh~mjVaENwGPwADGoEI"));
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
        assertEquals(waypoint.getName(), "8th Ave");
        assertEquals(waypoint.asPosition().getLongitude(), -122.465173, DELTA);
        assertEquals(waypoint.asPosition().getLatitude(), 37.76312, DELTA);
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
        assertEquals(leg.getDistance(), 9728.041736092327, DELTA);
        assertEquals(leg.getDuration(), 1024.3, DELTA);
        assertEquals(leg.getSummary(), "Golden Gate Ave, Leavenworth St");
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
        assertEquals(step.getDistance(), 312.82453411511926, DELTA);
        assertEquals(step.getDuration(), 44.599999999999994, DELTA);
        assertEquals(step.getGeometry(), "orneFh~mjVaENwGPwAD");
        assertEquals(step.getName(), "8th Ave");
        assertEquals(step.getMode(), "driving");
        assertNotEquals(step.getManeuver(), null);
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
        assertEquals(maneuver.asPosition().getLongitude(), -122.465173, DELTA);
        assertEquals(maneuver.asPosition().getLatitude(), 37.76312, DELTA);
        assertEquals(maneuver.getBearingBefore(), 316.5266871733792, DELTA);
        assertEquals(maneuver.getBearingAfter(), 0, DELTA);
        assertEquals(maneuver.getType(), "depart");
        assertEquals(maneuver.getModifier(), "left");
        assertEquals(maneuver.getInstruction(), "Head left on 8th Ave towards Lincoln Way");
        assertEquals(maneuver.getExit(), 1);
    }

}
