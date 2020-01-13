package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

public class DirectionsWaypointTest extends TestUtils {

  private static final String DIRECTIONS_V5_FIXTURE = "directions_v5.json";
  private static final String DIRECTIONS_V5_PRECISION6_FIXTURE = "directions_v5_precision_6.json";
  private static final String DIRECTIONS_TRAFFIC_FIXTURE = "directions_v5_traffic.json";
  private static final String DIRECTIONS_ROTARY_FIXTURE = "directions_v5_fixtures_rotary.json";
  private static final String DIRECTIONS_V5_ANNOTATIONS_FIXTURE = "directions_annotations_v5.json";

  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
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

        try {
          String body = loadJsonFixture(resource);
          return new MockResponse().setBody(body);
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }
      }
    });
    server.start();
    mockUrl = server.url("");
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();


  @Test
  public void sanity() throws Exception {
    DirectionsWaypoint waypoint = DirectionsWaypoint.builder()
      .name("foobar")
      .build();
    assertNotNull(waypoint);
  }

  @Test
  public void location_doesGetConvertedToGeoJsonPoint() throws Exception {
    DirectionsWaypoint waypoint = DirectionsWaypoint.builder()
      .rawLocation(new double[] {1.0, 2.0})
      .build();
    assertNotNull(waypoint.location());
    assertEquals(1.0, waypoint.location().longitude(), DELTA);
    assertEquals(2.0, waypoint.location().latitude(), DELTA);
  }

  @Test
  public void testToFromJson() {

    DirectionsWaypoint waypoint = DirectionsWaypoint.builder()
      .name("Kirkjubøarvegur")
      .rawLocation(new double[]{-6.80897, 62.000075})
      .build();

    String jsonString = waypoint.toJson();
    DirectionsWaypoint waypointFromJson = DirectionsWaypoint.fromJson(jsonString);

    assertEquals(waypoint, waypointFromJson);
  }

  @Test
  public void testFromJson() {
    String waypointJsonString =
      "{\"name\": \"Kirkjubøarvegur\", " +
        "\"location\": [ -6.80897, 62.000075] }";
    DirectionsWaypoint waypoint = DirectionsWaypoint.fromJson(waypointJsonString);

    Point location = waypoint.location();
    Assert.assertEquals(-6.80897, location.longitude(), 0.0001);
    Assert.assertEquals(62.000075, location.latitude(), 0.0001);

    String jsonStr = waypoint.toJson();

    compareJson(waypointJsonString, jsonStr);
  }
}
