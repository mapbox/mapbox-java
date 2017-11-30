package com.mapbox.api.geocoding.v5;

import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.TestUtils;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;

import org.hamcrest.junit.ExpectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapboxGeocodingTest extends TestUtils {

  private static final String GEOCODING_FIXTURE = "geocoding.json";
  private static final String GEOCODING_BATCH_FIXTURE = "geocoding_batch.json";

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
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    assertNotNull(mapboxGeocoding);
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertEquals(200, response.code());
  }

  @Test
  public void sanity_batchGeocodeRequest() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .mode(GeocodingCriteria.MODE_PLACES_PERMANENT)
      .accessToken(ACCESS_TOKEN)
      .query("20001;20009;22209")
      .baseUrl(mockUrl.toString())
      .build();
    assertNotNull(mapboxGeocoding);
    Response<List<GeocodingResponse>> response = mapboxGeocoding.executeBatchCall();
    assertEquals(200, response.code());
  }

  @Test
  public void executeBatchCall_exceptionThrownWhenModeNotSetCorrectly() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Use getCall() for non-batch calls."));
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    mapboxGeocoding.executeBatchCall();
  }

  @Test
  public void build_accessTokenNotValidException() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("Using Mapbox Services requires setting a valid access token."));
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    mapboxGeocoding.executeCall();
  }

  @Test
  public void build_emptyQueryNotAllowedException() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("A query with at least one character or digit is required."));
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("")
      .baseUrl(mockUrl.toString())
      .build();
    mapboxGeocoding.executeCall();
  }

  @Test
  public void query_acceptsPointsCorrectly() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query(Point.fromLngLat(-77.03655, 38.89770))
      .baseUrl(mockUrl.toString())
      .build();
    assertTrue(mapboxGeocoding.query().equals("-77.03655,38.8977"));
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertEquals(200, response.code());
  }

  @Test
  public void baseUrl_doesChangeTheRequestUrl() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl("https://foobar.com")
      .query(Point.fromLngLat(-77.03655, 38.89770))
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString()
      .startsWith("https://foobar.com"));
  }

  @Test
  public void country_localeCountryConvertsCorrectly() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .country(Locale.US)
      .query(Point.fromLngLat(-77.03655, 38.89770))
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString().contains("country=US"));
  }

  @Test
  public void country_localeCountryHandlesMultipleCountriesCorrectly() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .country(Locale.US)
      .country(Locale.CANADA)
      .query(Point.fromLngLat(-77.03655, 38.89770))
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString().contains("country=US,CA"));
  }

  @Test
  public void proximity_doesGetAddedToUrlCorrectly() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .query("1600 pennsylvania ave nw")
      .proximity(Point.fromLngLat(-77.03655, 38.89770))
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString()
      .contains("proximity=-77.03655,38.8977"));
  }

  @Test
  public void geocodingTypes_getsAddedToUrlCorrectly() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .geocodingTypes(GeocodingCriteria.TYPE_COUNTRY, GeocodingCriteria.TYPE_DISTRICT)
      .query("1600 pennsylvania ave nw")
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString()
      .contains("types=country,district"));
  }

  @Test
  public void autocomplete_getsAddedToUrlCorrectly() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .autocomplete(true)
      .query("1600 pennsylvania ave nw")
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString()
      .contains("autocomplete=true"));
  }

  @Test
  public void bbox_getsFormattedCorrectlyForUrl() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .bbox(
        Point.fromLngLat(-77.083056, 38.908611),
        Point.fromLngLat(-76.997778, 38.959167)
      )
      .query("1600 pennsylvania ave nw")
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString()
      .contains("bbox=-77.083056,38.908611,-76.997778,38.959167"));
  }

  @Test
  public void limit_getsAddedToUrlCorrectly() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .limit(2)
      .query("1600 pennsylvania ave nw")
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString()
      .contains("limit=2"));
  }

  @Test
  public void language_getsConvertedToUrlCorrectly() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .languages(Locale.ENGLISH, Locale.FRANCE)
      .query("1600 pennsylvania ave nw")
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString().contains("language=en,fr"));
  }

  @Test
  public void clientAppName_hasAppInString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .clientAppName("APP")
      .languages(Locale.ENGLISH, Locale.FRANCE)
      .query("1600 pennsylvania ave nw")
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().header("User-Agent").contains("APP"));
  }
}
