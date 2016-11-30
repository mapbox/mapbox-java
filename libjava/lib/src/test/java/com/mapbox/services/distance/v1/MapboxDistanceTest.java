package com.mapbox.services.distance.v1;

import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.distance.v1.models.DistanceResponse;

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

public class MapboxDistanceTest {

  private static final String DISTANCE_FIXTURE = "src/test/fixtures/distance_v1.json";

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
        try {
          String body = new String(Files.readAllBytes(Paths.get(DISTANCE_FIXTURE)), Charset.forName("utf-8"));
          return new MockResponse().setBody(body);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });

    server.start();
    mockUrl = server.url("");

    coordinates = new ArrayList<>();
    coordinates.add(Position.fromCoordinates(13.41894, 52.50055));
    coordinates.add(Position.fromCoordinates(14.10293, 52.50055));
    coordinates.add(Position.fromCoordinates(13.50116, 53.10293));
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
    MapboxDistance client = new MapboxDistance.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(DirectionsCriteria.PROFILE_WALKING)
      .setCoordinates(coordinates)
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<DistanceResponse> response = client.executeCall();
    assertEquals(response.code(), 200);

    // Check the response body
    assertNotNull(response.body());
    assertEquals(3, response.body().getDurations().length);
    assertNotNull(response.body().getDurations()[0][0]);
  }

  @Test
  public void requiredAccessToken() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Services requires setting a valid access token"));
    new MapboxDirections.Builder().build();
  }

  @Test
  public void validCoordinates() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Distance API requires to set some coordinates."));
    new MapboxDistance.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .build();
  }

  @Test
  public void validCoordinatesTotal() throws ServicesException {
    // Fake too many positions
    List<Position> positions = new ArrayList<>();
    for (int i = 0; i < 101; i++) {
      positions.add(Position.fromCoordinates(0.0, 0.0));
    }

    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("The Mapbox Distance API is limited to processing up to 100 coordinate pairs."));
    new MapboxDistance.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setCoordinates(positions)
      .build();
  }

  @Test
  public void validProfileNonNull() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Distance API requires setting a valid profile."));
    new MapboxDistance.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile(null)
      .build();
  }

  @Test
  public void validProfileCorrectString() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Distance API requires setting a valid profile."));
    new MapboxDistance.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setProfile("my_own_profile")
      .build();
  }

  @Test
  public void testUserAgent() throws ServicesException, IOException {
    MapboxDistance service = new MapboxDistance.Builder()
      .setClientAppName("APP")
      .setAccessToken("pk.XXX")
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setCoordinates(coordinates)
      .build();
    service.setBaseUrl(mockUrl.toString());
    assertTrue(service.executeCall().raw().request().header("User-Agent").contains("APP"));
  }
}
