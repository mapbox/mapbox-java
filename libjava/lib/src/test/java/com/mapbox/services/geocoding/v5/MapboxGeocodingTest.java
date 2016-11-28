package com.mapbox.services.geocoding.v5;

import com.google.gson.JsonObject;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.geocoding.v5.models.CarmenContext;
import com.mapbox.services.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.geocoding.v5.models.GeocodingResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MapboxGeocodingTest {

  private final static double DELTA = 1E-10;

  private static final String GEOCODING_FIXTURE = "src/test/fixtures/geocoding.json";
  private static final String GEOCODING_COUNTRY_NOT_SUPPORTED = "src/test/fixtures/geocoding_country_not_supported.json";
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
          String body = new String(Files.readAllBytes(Paths.get(resource)), Charset.forName("utf-8"));
          return new MockResponse().setBody(body);
        } catch (IOException e) {
          throw new RuntimeException(e);
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
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<GeocodingResponse> response = client.executeCall();
    assertEquals(response.code(), 200);
  }

  @Test
  public void testBody() throws ServicesException, IOException {
    MapboxGeocoding client = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .build();
    client.setBaseUrl(mockUrl.toString());
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
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<GeocodingResponse> response = client.executeCall();

    CarmenFeature feature = response.body().getFeatures().get(0);
    assertEquals(feature.getId(), "address.3982178573139850");
    assertEquals(feature.getType(), "Feature");
    assertEquals(feature.getText(), "Pennsylvania Ave NW");
    assertEquals(feature.getPlaceName(), "1600 Pennsylvania Ave NW, Washington, District of Columbia 20006, United States");
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
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<GeocodingResponse> response = client.executeCall();

    Point point = (Point) response.body().getFeatures().get(3).getGeometry();
    assertEquals(point.getType(), "Point");
    assertEquals(point.getCoordinates().getLongitude(), -77.045, DELTA);
    assertEquals(point.getCoordinates().getLatitude(), 38.8946, DELTA);
  }

  @Test
  public void testContext() throws ServicesException, IOException {
    MapboxGeocoding client = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<GeocodingResponse> response = client.executeCall();

    List<CarmenContext> contexts = response.body().getFeatures().get(0).getContext();
    assertEquals(contexts.get(4).getId(), "country.12862386939497690");
    assertEquals(contexts.get(4).getText(), "United States");
    assertEquals(contexts.get(4).getShortCode(), "us");
  }

  @Test
  public void testWikidata() throws ServicesException, IOException {
    MapboxGeocoding client = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<GeocodingResponse> response = client.executeCall();

    CarmenContext context = response.body().getFeatures().get(0).getContext().get(1);
    assertEquals(context.getWikidata(), "Q61");
  }

  @Test
  public void testUserAgent() throws ServicesException, IOException {
    MapboxGeocoding service = new MapboxGeocoding.Builder()
      .setClientAppName("APP")
      .setAccessToken(ACCESS_TOKEN)
      .setLocation("1600 pennsylvania ave nw")
      .build();
    service.setBaseUrl(mockUrl.toString());
    assertTrue(service.executeCall().raw().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void testCountryNotSupported() throws ServicesException, IOException {
    MapboxGeocoding client = new MapboxGeocoding.Builder()
      .setAccessToken(ACCESS_TOKEN)
      .setCountry("aq")
      .setLocation("1600 pennsylvania ave nw")
      .build();
    client.setBaseUrl(mockUrl.toString());
    Response<GeocodingResponse> response = client.executeCall();

    assertEquals(0, response.body().getFeatures().size());
  }
}
