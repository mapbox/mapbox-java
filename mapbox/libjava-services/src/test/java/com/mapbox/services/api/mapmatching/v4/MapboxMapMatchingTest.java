package com.mapbox.services.api.mapmatching.v4;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.mapmatching.v4.models.MapMatchingResponse;
import com.mapbox.services.commons.geojson.LineString;
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

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test the Mapbox Map Matching API
 */
public class MapboxMapMatchingTest {

  public static final String POLYLINE_FIXTURE = "src/test/fixtures/mapmatching_v5_polyline.json";
  public static final String NO_GEOMETRY_FIXTURE = "src/test/fixtures/mapmatching_v5_no_geometry.json";

  private static final String ACCESS_TOKEN = "pk.XXX";

  private MockWebServer server;
  private HttpUrl mockUrl;

  private LineString trace;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new Dispatcher() {

      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        // Switch response on geometry parameter (only false supported, so nice and simple)
        String resource = POLYLINE_FIXTURE;
        if (request.getPath().contains("geometry=false")) {
          resource = NO_GEOMETRY_FIXTURE;
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

    // From https://www.mapbox.com/api-documentation/#map-matching
    trace = LineString.fromJson("{ \"type\": \"LineString\", \"coordinates\": [ [13.418946862220764, "
      + "52.50055852688439], [13.419011235237122, 52.50113000479732], [13.419756889343262, 52.50171780290061],"
      + " [13.419885635375975, 52.50237416816131], [13.420631289482117, 52.50294888790448] ] }");
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
      .setTrace(trace)
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<MapMatchingResponse> response = client.executeCall();
    assertEquals(response.code(), 200);

    // Check the response body
    assertNotNull(response.body());
    assertEquals(1, response.body().getFeatures().size());
    assertNotNull(response.body().getFeatures().get(0).getGeometry());
  }

  /**
   * Test a basic request with polyline response
   */
  @Test
  public void testNoGeometryResponse() throws ServicesException, IOException {
    MapboxMapMatching client = new MapboxMapMatching.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(MapMatchingCriteria.PROFILE_WALKING)
      .setTrace(trace)
      .setNoGeometry()
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<MapMatchingResponse> response = client.executeCall();
    assertEquals(response.code(), 200);

    // Check the response body
    assertNotNull(response.body());
    assertEquals(1, response.body().getFeatures().size());
    assertNull(response.body().getFeatures().get(0).getGeometry());
  }

  @Test
  public void requiredAccessToken() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Services requires setting a valid access token"));
    new MapboxMapMatching.Builder().build();
  }

  @Test
  public void validGpsPrecisionLowerBounds() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Map Matching requires setting a valid GPS precision"));
    new MapboxMapMatching.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(MapMatchingCriteria.PROFILE_WALKING)
      .setGpsPrecison(0)
      .build();
  }

  @Test
  public void validGpsPrecisionUpperBounds() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Map Matching requires setting a valid GPS precision"));
    new MapboxMapMatching.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(MapMatchingCriteria.PROFILE_WALKING)
      .setGpsPrecison(11)
      .build();
  }

  @Test
  public void validProfileNonNull() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Map Matching requires setting a valid profile"));
    new MapboxMapMatching.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(null)
      .build();
  }

  @Test
  public void validProfileCorrectString() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Map Matching requires setting a valid profile"));
    new MapboxMapMatching.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile("my_own_profile")
      .build();
  }

  @Test
  public void validCoordinates() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Map Matching requires to set some coordinates"));
    new MapboxMapMatching.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(MapMatchingCriteria.PROFILE_DRIVING)
      .build();
  }

  @Test
  public void validCoordinatesTotal() throws ServicesException {
    // Fake too many positions
    ArrayList<Position> positions = new ArrayList<>();
    for (int i = 0; i < 101; i++) {
      positions.add(Position.fromCoordinates(0.0, 0.0));
    }

    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("The Map Matching API is limited to processing traces with up to 100 coordinates"));
    new MapboxMapMatching.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(MapMatchingCriteria.PROFILE_DRIVING)
      .setTrace(LineString.fromCoordinates(positions))
      .build();
  }


  @Test
  public void testUserAgent() throws ServicesException, IOException {
    MapboxMapMatching service = new MapboxMapMatching.Builder()
      .setClientAppName("APP")
      .setAccessToken("pk.XXX")
      .setProfile(MapMatchingCriteria.PROFILE_DRIVING)
      .setTrace(LineString.fromCoordinates(new ArrayList<Position>()))
      .setBaseUrl(mockUrl.toString())
      .build();
    assertTrue(service.executeCall().raw().request().header("User-Agent").contains("APP"));
  }

  //  @Test
  //  public void testPost() throws ServicesException, IOException {
  //    MapboxMapMatching client = new MapboxMapMatching.Builder()
  //      .setAccessToken("")
  //      .setProfile(MapMatchingCriteria.PROFILE_DRIVING)
  //      .setTrace(trace)
  //      .build();
  //
  //    // Enable debug
  //    client.setEnableDebug(true);
  //
  //    // All good
  //    Response<MapMatchingResponse> response = client.executeCall();
  //    assertEquals(response.code(), 200);
  //    assertEquals(response.body().getCode(), MapMatchingCriteria.RESPONSE_OK);
  //    assertEquals(response.body().getMatchedPoints().length, 5);
  //  }
}
