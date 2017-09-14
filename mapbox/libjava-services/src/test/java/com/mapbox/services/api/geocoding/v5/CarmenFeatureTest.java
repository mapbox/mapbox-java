package com.mapbox.services.api.geocoding.v5;

import com.google.gson.JsonObject;
import com.mapbox.services.api.BaseTest;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.custom.BoundingBox;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.hamcrest.junit.ExpectedException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CarmenFeatureTest extends BaseTest {

  private static final String ACCESS_TOKEN = "pk.XXX";

  private static final String GEOCODING_FIXTURE = "geocoding/geocoding.json";
  private static final String GEOCODING_BATCH_FIXTURE = "geocoding/geocoding_batch.json";
  private static final String REVERSE_GEOCODE_FIXTURE = "geocoding/geocoding_reverse.json";
  private static final String GEOCODE_WITH_BBOX_FIXTURE = "geocoding/bbox_geocoding_result.json";
  private static final String GEOCODE_LANGUAGE_FIXTURE = "geocoding/language_geocoding_result.json";

  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws Exception {
    server = new MockWebServer();
    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String response;
          if (request.getPath().contains(GeocodingCriteria.MODE_PLACES_PERMANENT)) {
            response = loadJsonFixture(GEOCODING_BATCH_FIXTURE);
          } else if (request.getPath().contains("-77.0366,38.8971")) {
            response = loadJsonFixture(REVERSE_GEOCODE_FIXTURE);
          } else if (request.getPath().contains("texas")) {
            response = loadJsonFixture(GEOCODE_WITH_BBOX_FIXTURE);
          } else if (request.getPath().contains("language")) {
            response = loadJsonFixture(GEOCODE_LANGUAGE_FIXTURE);
          } else {
            response = loadJsonFixture(GEOCODING_FIXTURE);
          }
          return new MockResponse().setBody(response);
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
    CarmenFeature carmenFeature = CarmenFeature.builder()
      .properties(new JsonObject())
      .address("1234")
      .build();
    assertNotNull(carmenFeature);
    assertTrue(carmenFeature.address().equals("1234"));
  }

  @Test
  public void center_returnsPointRepresentingCenter() throws Exception {
    Point centerPoint = Point.fromLngLat(-77.036491, 38.897291);
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query(Point.fromLngLat(-77.0366, 38.8971))
      .baseUrl(mockUrl.toString())
      .build();
    assertNotNull(mapboxGeocoding);
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertTrue(response.body().features().get(0).center().equals(centerPoint));
  }

  @Test
  public void type_returnsFeatureString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertTrue(response.body().features().get(0).type().equals("Feature"));
  }

  @Test
  public void bbox_returnsGeoJsonBoundingBoxObject() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("texas")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertNotNull(response.body().features().get(0).bbox());
  }

  @Test
  public void id_doesReturnCorrectId() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query(Point.fromLngLat(-77.0366, 38.8971))
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertTrue(response.body().features().get(0).id().equals("poi.7298394581225630"));
  }

  @Test
  public void geometry_returnsPointGeometry() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query(Point.fromLngLat(-77.0366, 38.8971))
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    Assert.assertNotNull(response.body().features().get(0).geometry());
    assertTrue(response.body().features().get(0).geometry() instanceof Point);
    assertEquals(-77.036491,
      ((Point) response.body().features().get(0).geometry()).longitude(), DELTA);
    assertEquals(38.897291,
      ((Point) response.body().features().get(0).geometry()).latitude(), DELTA);
  }

  @Test
  public void properties_isJsonObject() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query(Point.fromLngLat(-77.0366, 38.8971))
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    Assert.assertNotNull(response.body().features().get(0).properties());
    assertTrue(response.body().features().get(0).properties().get("address")
      .getAsString().equals("1600 Pennsylvania Ave NW"));
    assertTrue(response.body().features().get(0).properties().get("category")
      .getAsString().equals("restaurant"));
    assertTrue(response.body().features().get(0).properties().get("landmark")
      .getAsBoolean());
    assertTrue(response.body().features().get(0).properties().get("maki")
      .getAsString().equals("restaurant"));
  }

  @Test
  public void text_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("texas")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertTrue(response.body().features().get(0).text().equals("Texas"));
  }

  @Test
  public void placeName_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query(Point.fromLngLat(-77.0366, 38.8971))
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertTrue(response.body().features().get(0).placeName().equals("Harry S. Truman Bowling Alley,"
      + " 1600 Pennsylvania Ave NW, Washington, District of Columbia 20006, United States"));
  }

  @Test
  public void placeType_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query(Point.fromLngLat(-77.0366, 38.8971))
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertEquals(1, response.body().features().get(0).placeType().size());
    assertTrue(response.body().features().get(0).placeType().get(0).equals("poi"));
  }

  @Test
  public void address_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertTrue(response.body().features().get(0).address().equals("1600"));
  }

  @Test
  public void context_returnsListOfContext() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertEquals(5, response.body().features().get(0).context().size());
  }

  @Test
  public void relevance_returnsAccurateValue() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertEquals(1.0, response.body().features().get(0).relevance(), DELTA);
  }

  @Test
  public void matchingText_returnsCorrectString() throws Exception {
    // TODO complete test with fixture
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    System.out.println(response.body().features().get(0).matchingText());
  }

  @Test
  public void matchingPlaceName_returnsCorrectString() throws Exception {
    // TODO complete test with fixture
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    System.out.println(response.body().features().get(0).matchingPlaceName());
  }

  @Test
  public void language_returnCorrectString() throws Exception {
    // TODO complete test for language
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("foobar")
      .baseUrl(mockUrl.toString())
      .languages(Locale.FRANCE)
      .build();
    System.out.println(mapboxGeocoding.cloneCall().request().url());
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    System.out.println(response.body().features().get(0).language());
  }

  @Test
  public void carmenFeatureBuilder_sanity() throws Exception {
    JsonObject properties = new JsonObject();
    properties.addProperty("key", "value");
    Point geometry = Point.fromLngLat(2.0, 2.0);
    BoundingBox bbox = BoundingBox.fromCoordinates(1.0, 2.0, 3.0, 4.0);

    CarmenFeature carmenFeature = CarmenFeature.builder()
      .address("1000")
      .bbox(bbox)
      .context(null)
      .geometry(geometry)
      .id("poi.123456789")
      .language("fr")
      .matchingPlaceName("matchingPlaceName")
      .matchingText("matchingText")
      .placeName("placeName")
      .placeType(null)
      .properties(properties)
      .relevance(0.5)
      .text("text")
      .build();

    assertTrue(carmenFeature.address().equals("1000"));
    assertTrue(carmenFeature.bbox().equals(bbox));
    assertNull(carmenFeature.context());
    assertTrue(carmenFeature.geometry().equals(geometry));
    assertTrue(carmenFeature.id().equals("poi.123456789"));
    assertTrue(carmenFeature.language().equals("fr"));
    assertTrue(carmenFeature.matchingPlaceName().equals("matchingPlaceName"));
    assertTrue(carmenFeature.matchingText().equals("matchingText"));
    assertTrue(carmenFeature.placeName().equals("placeName"));
    assertNull(carmenFeature.placeType());
    assertTrue(carmenFeature.properties().get("key").getAsString().equals("value"));
    assertEquals(0.5, carmenFeature.relevance(), DELTA);
    assertTrue(carmenFeature.text().equals("text"));
  }

  // TODO fix to and from JSON
//  @Test
//  public void toJson_convertsCarmenFeatureToJsonCorrectly() throws Exception {
//    CarmenFeature carmenFeature = CarmenFeature.fromJson(GEOCODING_FIXTURE);
//    System.out.println(carmenFeature);
//  }


}
