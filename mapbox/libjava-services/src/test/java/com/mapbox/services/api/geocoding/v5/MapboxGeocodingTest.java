package com.mapbox.services.api.geocoding.v5;

import com.google.gson.JsonObject;
import com.mapbox.services.api.BaseTest;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.geocoding.v5.models.CarmenContext;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MapboxGeocodingTest extends BaseTest {

  private static final String GEOCODING_FIXTURE = "src/test/fixtures/geocoding/geocoding.json";
  private static final String GEOCODING_COUNTRY_NOT_SUPPORTED =
    "src/test/fixtures/geocoding/geocoding_country_not_supported.json";
  private static final String ACCESS_TOKEN = "pk.XXX";

  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new Dispatcher() {

      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        // Change the resource path if we are checking country not supported
        String resource = GEOCODING_FIXTURE;
        if (request.getPath().contains("country=aq")) {
          resource = GEOCODING_COUNTRY_NOT_SUPPORTED;
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
  public void testSanity() throws ServicesException, IOException {
    MapboxGeocoding client = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = client.executeCall();
    assertEquals(response.code(), 200);
  }

  @Test
  public void testBody() throws ServicesException, IOException {
    MapboxGeocoding client = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = client.executeCall();

    GeocodingResponse body = response.body();
    assertEquals(body.getType(), "FeatureCollection");

    assertEquals(body.getQuery().get(0), "1600");
    assertEquals(body.getQuery().get(1), "pennsylvania");
    assertEquals(body.getQuery().get(2), "ave");
    assertEquals(body.getQuery().get(3), "nw");

    assertEquals(body.getFeatures().size(), 5);
    assertTrue(body.getAttribution().startsWith("NOTICE"));
  }

  @Test
  public void testFeature() throws ServicesException, IOException {
    MapboxGeocoding client = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = client.executeCall();

    CarmenFeature feature = response.body().getFeatures().get(0);
    assertEquals(feature.getId(), "address.3982178573139850");
    assertEquals(feature.getType(), "Feature");
    assertEquals(feature.getText(), "Pennsylvania Ave NW");
    assertEquals(feature.getPlaceName(), "1600 Pennsylvania Ave NW, Washington, District of Columbia 20006, "
      + "United States");
    assertEquals(feature.getRelevance(), 0.99, DELTA);
    assertEquals(feature.getProperties(), new JsonObject());
    assertEquals(feature.asPosition().getLongitude(), -77.036543, DELTA);
    assertEquals(feature.asPosition().getLatitude(), 38.897702, DELTA);
    assertEquals(feature.getAddress(), "1600");
    assertEquals(feature.getContext().size(), 5);
  }

  @Test
  public void testGeometry() throws ServicesException, IOException {
    MapboxGeocoding client = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = client.executeCall();

    Point point = (Point) response.body().getFeatures().get(3).getGeometry();
    assertEquals(point.getType(), "Point");
    assertEquals(point.getCoordinates().getLongitude(), -90.313554, DELTA);
    assertEquals(point.getCoordinates().getLatitude(), 38.681546, DELTA);
  }

  @Test
  public void testContext() throws ServicesException, IOException {
    MapboxGeocoding client = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = client.executeCall();

    List<CarmenContext> contexts = response.body().getFeatures().get(0).getContext();
    assertEquals(contexts.get(4).getId(), "country.3145");
    assertEquals(contexts.get(4).getText(), "United States");
    assertEquals(contexts.get(4).getShortCode(), "us");
  }

  @Test
  public void testWikidata() throws ServicesException, IOException {
    MapboxGeocoding client = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = client.executeCall();

    CarmenContext context = response.body().getFeatures().get(0).getContext().get(2);
    assertEquals(context.getWikidata(), "Q61");
  }

  @Test
  public void testUserAgent() throws ServicesException, IOException {
    MapboxGeocoding service = new MapboxGeocoding.Builder()
      .setClientAppName("APP")
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .setBaseUrl(mockUrl.toString())
      .build();
    assertTrue(service.executeCall().raw().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void testCountryNotSupported() throws ServicesException, IOException {
    MapboxGeocoding client = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setCountry("aq")
      .setLocation("1600 pennsylvania ave nw")
      .setBaseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = client.executeCall();

    assertEquals(0, response.body().getFeatures().size());
  }

  @Test
  public void testBbox() throws IOException, ServicesException {
    MapboxGeocoding clientNeSw = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .setBbox(Position.fromCoordinates(-77.0035, 38.9115), Position.fromCoordinates(-77.0702, 38.8561))
      .setBaseUrl(mockUrl.toString())
      .build();

    MapboxGeocoding clientMinMax = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .setBbox(-77.0035, 38.9115, -77.0702, 38.8561)
      .setBaseUrl(mockUrl.toString())
      .build();

    assertTrue(
      URLDecoder.decode(clientNeSw.executeCall().raw().request().url().toString(),
        StandardCharsets.UTF_8.name()).contains("bbox=-77.0702,38.8561,-77.0035,38.9115"));
    assertTrue(
      URLDecoder.decode(clientMinMax.executeCall().raw().request().url().toString(),
        StandardCharsets.UTF_8.name()).contains("bbox=-77.0035,38.9115,-77.0702,38.8561"));
  }

  @Test
  public void testSingleLanguage() throws IOException, ServicesException {
    MapboxGeocoding clientNoLanguage = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .setBaseUrl(mockUrl.toString())
      .build();

    // By default no language is included
    assertTrue(!clientNoLanguage.getCall().request().url().toString().contains("language=en_GB"));

    MapboxGeocoding clientLanguage = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .setLanguages(Locale.UK.toString())
      .setBaseUrl(mockUrl.toString())
      .build();

    // setLanguage() will include the language query parameter
    assertTrue(clientLanguage.getCall().request().url().toString().contains("language=en_GB"));
  }

  @Test
  public void testMultipleLanguage() throws IOException, ServicesException {
    MapboxGeocoding clientLanguage = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .setLanguages(Locale.UK.toString(), Locale.US.toString(), Locale.JAPAN.toString())
      .setBaseUrl(mockUrl.toString())
      .build();

    // setLanguage() will include the languages query parameter
    assertTrue(URLDecoder.decode(clientLanguage.getCall().request().url().toString(),
      StandardCharsets.UTF_8.name()).contains("language=en_GB,en_US,ja_JP"));
  }
}
