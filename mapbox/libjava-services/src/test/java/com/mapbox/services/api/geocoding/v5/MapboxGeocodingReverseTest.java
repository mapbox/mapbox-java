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
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MapboxGeocodingReverseTest extends BaseTest {
//
//  private static final String REVERSE_GEOCODE_FIXTURE = "src/test/fixtures/geocoding/geocoding_reverse.json";
//
//  private MockWebServer server;
//  private HttpUrl mockUrl;
//
//  @Before
//  public void setUp() throws IOException {
//    server = new MockWebServer();
//
//    String body = loadJsonFixture(REVERSE_GEOCODE_FIXTURE);
//    server.enqueue(new MockResponse().setBody(body));
//
//    server.start();
//
//    mockUrl = server.url("");
//  }
//
//  @After
//  public void tearDown() throws IOException {
//    server.shutdown();
//  }
//
//  @Rule
//  public ExpectedException thrown = ExpectedException.none();
//
//  @Test
//  public void testSanity() throws ServicesException, IOException {
//    MapboxGeocoding client = new MapboxGeocoding.Builder()
//      .setAccessToken("pk.XXX")
//      .setCoordinates(Position.fromCoordinates(-77.0366, 38.8971))
//      .setBaseUrl(mockUrl.toString())
//      .build();
//    Response<GeocodingResponse> response = client.executeCall();
//    assertEquals(response.code(), 200);
//  }
//
//  @Test
//  public void testBody() throws ServicesException, IOException {
//    MapboxGeocoding client = new MapboxGeocoding.Builder()
//      .setAccessToken("pk.XXX")
//      .setLocation("1600 pennsylvania ave nw")
//      .setBaseUrl(mockUrl.toString())
//      .build();
//    Response<GeocodingResponse> response = client.executeCall();
//
//    GeocodingResponse body = response.body();
//    assertEquals(body.getType(), "FeatureCollection");
//
//    assertEquals(body.getQuery().get(0), String.valueOf(-77.0366));
//    assertEquals(body.getQuery().get(1), String.valueOf(38.8971));
//
//    assertEquals(body.getFeatures().size(), 6);
//    assertTrue(body.getAttribution().startsWith("NOTICE"));
//  }
//
//  @Test
//  public void testFeature() throws ServicesException, IOException {
//    MapboxGeocoding client = new MapboxGeocoding.Builder()
//      .setAccessToken("pk.XXX")
//      .setLocation("1600 pennsylvania ave nw")
//      .setBaseUrl(mockUrl.toString())
//      .build();
//    Response<GeocodingResponse> response = client.executeCall();
//
//    CarmenFeature feature = response.body().getFeatures().get(1);
//    assertEquals(feature.getId(), "neighborhood.291451");
//    assertEquals(feature.getType(), "Feature");
//    assertEquals(feature.getText(), "Downtown");
//    assertEquals(feature.getPlaceName(), "Downtown, Washington, District of Columbia 20006, United States");
//    assertEquals(feature.getRelevance(), 1, DELTA);
//    assertEquals(feature.getProperties(), new JsonObject());
//    assertEquals(feature.getBbox()[0], -77.048808, DELTA);
//    assertEquals(feature.getBbox()[1], 38.891915, DELTA);
//    assertEquals(feature.getBbox()[2], -77.016764, DELTA);
//    assertEquals(feature.getBbox()[3], 38.907241, DELTA);
//    assertEquals(feature.asPosition().getLongitude(), -77.03, DELTA);
//    assertEquals(feature.asPosition().getLatitude(), 38.9, DELTA);
//    assertEquals(feature.getContext().size(), 4);
//  }
//
//  @Test
//  public void testGeometry() throws ServicesException, IOException {
//    MapboxGeocoding client = new MapboxGeocoding.Builder()
//      .setAccessToken("pk.XXX")
//      .setLocation("1600 pennsylvania ave nw")
//      .setBaseUrl(mockUrl.toString())
//      .build();
//    Response<GeocodingResponse> response = client.executeCall();
//
//    Point point = (Point) response.body().getFeatures().get(1).getGeometry();
//    assertEquals(point.getType(), "Point");
//    assertEquals(point.getCoordinates().getLongitude(), -77.03, DELTA);
//    assertEquals(point.getCoordinates().getLatitude(), 38.9, DELTA);
//  }
//
//  @Test
//  public void testContext() throws ServicesException, IOException {
//    MapboxGeocoding client = new MapboxGeocoding.Builder()
//      .setAccessToken("pk.XXX")
//      .setLocation("1600 pennsylvania ave nw")
//      .setBaseUrl(mockUrl.toString())
//      .build();
//    Response<GeocodingResponse> response = client.executeCall();
//
//    List<CarmenContext> contexts = response.body().getFeatures().get(1).getContext();
//    assertEquals(contexts.get(3).getId(), "country.3145");
//    assertEquals(contexts.get(3).getText(), "United States");
//    assertEquals(contexts.get(3).getShortCode(), "us");
//  }
}
