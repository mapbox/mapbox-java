package com.mapbox.services.api.directionsmatrix.v1;

import com.mapbox.services.api.BaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapboxDirectionsMatrixTest extends BaseTest {
//
//  private static final String DIRECTIONS_MATRIX_3X3_FIXTURE
//    = "src/test/fixtures/directions_matrix_3x3.json";
//  private static final String DIRECTIONS_MATRIX_2x3_FIXTURE
//    = "src/test/fixtures/directions_matrix_2x3.json";
//  private static final String ACCESS_TOKEN = "pk.XXX";
//
//  private MockWebServer server;
//  private HttpUrl mockUrl;
//
//  private List<Position> positions;
//
//  @Rule
//  public ExpectedException thrown = ExpectedException.none();
//
//  @Before
//  public void setUp() throws IOException {
//    server = new MockWebServer();
//
//    server.setDispatcher(new Dispatcher() {
//
//      @Override
//      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
//        try {
//          String body = new String(
//            Files.readAllBytes(Paths.get(DIRECTIONS_MATRIX_3X3_FIXTURE)), Charset.forName("utf-8")
//          );
//          return new MockResponse().setBody(body);
//        } catch (IOException ioException) {
//          throw new RuntimeException(ioException);
//        }
//      }
//    });
//    server.start();
//    mockUrl = server.url("");
//
//    positions = new ArrayList<>();
//    positions.add(Position.fromCoordinates(-122.42, 37.78));
//    positions.add(Position.fromCoordinates(-122.45, 37.91));
//    positions.add(Position.fromCoordinates(-122.48, 37.73));
//  }
//
//  @After
//  public void tearDown() throws IOException {
//    server.shutdown();
//  }
//
//  /**
//   * Test the most basic request (default response format)
//   */
//  @Test
//  public void testCallSanity() throws ServicesException, IOException {
//    MapboxMatrix client = new MapboxMatrix.Builder()
//      .setAccessToken(ACCESS_TOKEN)
//      .setProfile(DirectionsCriteria.PROFILE_WALKING)
//      .setCoordinates(positions)
//      .setBaseUrl(mockUrl.toString())
//      .build();
//    Response<MatrixResponse> response = client.executeCall();
//    assertEquals(response.code(), 200);
//
//    // Check the response body
//    assertNotNull(response.body());
//    assertEquals(3, response.body().getDestinations().size());
//    assertNotNull(response.body().getDestinations().get(0).getName());
//  }
//
//  @Test
//  public void requiredAccessToken() throws ServicesException {
//    thrown.expect(ServicesException.class);
//    thrown.expectMessage(startsWith("Using Mapbox Services requires setting a valid access token"));
//    new MapboxMatrix.Builder().build();
//  }
//
//  @Test
//  public void validProfileNonNull() throws ServicesException {
//    thrown.expect(ServicesException.class);
//    thrown.expectMessage(startsWith("A profile is required for the Directions Matrix API"));
//    new MapboxMatrix.Builder()
//      .setAccessToken(ACCESS_TOKEN)
//      .setProfile(null)
//      .build();
//  }
//
//  @Test
//  public void validProfile() throws ServicesException {
//    thrown.expect(ServicesException.class);
//    thrown.expectMessage(startsWith("Profile must be one of the values found inside the DirectionsCriteria."));
//    new MapboxMatrix.Builder()
//      .setAccessToken(ACCESS_TOKEN)
//      .setCoordinates(positions)
//      .setProfile("NOT_VALID_PROFILE")
//      .build();
//  }
//
//  @Test
//  public void validCoordinates() throws ServicesException {
//    thrown.expect(ServicesException.class);
//    thrown.expectMessage(startsWith("You should provide at least two coordinates (from/to)."));
//    new MapboxMatrix.Builder()
//      .setAccessToken(ACCESS_TOKEN)
//      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
//      .build();
//  }
//
//  @Test
//  public void validCoordinatesTotal() throws ServicesException {
//    int total = 26;
//    List<Position> positions = new ArrayList<>();
//    for (int i = 0; i < total; i++) {
//      // Fake too many positions
//      positions.add(Position.fromCoordinates(0.0, 0.0));
//    }
//
//    thrown.expect(ServicesException.class);
//    thrown.expectMessage(startsWith("All profiles allow for a maximum of 25 coordinates."));
//    new MapboxMatrix.Builder()
//      .setAccessToken(ACCESS_TOKEN)
//      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
//      .setCoordinates(positions)
//      .build();
//  }
//
//  @Test
//  public void testUserAgent() throws ServicesException, IOException {
//    MapboxMatrix service = new MapboxMatrix.Builder()
//      .setClientAppName("APP")
//      .setAccessToken("pk.XXX")
//      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
//      .setCoordinates(positions)
//      .setBaseUrl(mockUrl.toString())
//      .build();
//    assertTrue(service.executeCall().raw().request().header("User-Agent").contains("APP"));
//  }
//
//  @Test
//  public void testResponse() throws ServicesException, IOException {
//    MapboxMatrix client = new MapboxMatrix.Builder()
//      .setClientAppName("APP")
//      .setAccessToken("pk.XXX")
//      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
//      .setCoordinates(positions)
//      .setBaseUrl(mockUrl.toString())
//      .build();
//    Response<MatrixResponse> response = client.executeCall();
//
//    assertEquals(response.body().getSources().size(), 3);
//    assertEquals(response.body().getSources().get(0).getLocation()[0], -122.420019, DELTA);
//    assertEquals(response.body().getDestinations().get(0).getLocation()[0], -122.420019, DELTA);
//    assertEquals(response.body().getDurations()[0][1], 1888.3, DELTA);
//    assertEquals(response.body().getDestinations().get(0).getName(), "McAllister Street");
//  }
}
