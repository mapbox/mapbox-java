package com.mapbox.api.geocoding.v6;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mapbox.api.geocoding.v6.models.V6FeatureType;
import com.mapbox.api.geocoding.v6.models.V6Worldview;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Point;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public abstract class V6GeocodingTestUtils extends TestUtils {

  protected MockWebServer server;
  protected HttpUrl mockUrl;

  @Before
  public void setUp() throws Exception {
    server = new MockWebServer();
    server.start();
    mockUrl = server.url("");
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  public MockResponse createMockResponse(String fileName) throws IOException {
    return new MockResponse().setBody(loadJsonFixture(fileName));
  }

  public MockResponse createErrorMockResponse(int httpCode) {
    return new MockResponse().setResponseCode(httpCode);
  }

  public String loadCompressedJson(String filename) throws IOException {
    return removeJsonWhitespaces(loadJsonFixture(filename));
  }

  public static String removeJsonWhitespaces(String json) {
    final Gson gson = new GsonBuilder().create();
    final JsonElement parsed = JsonParser.parseString(json);
    return gson.toJson(parsed);
  }

  public static final String TEST_QUERY = "test query";
  public static final Point TEST_POINT = Point.fromLngLat(10.10, 20.20);

  public static final BoundingBox TEST_BBOX = BoundingBox.fromPoints(
    Point.fromLngLat(10.1, 20.2),
    Point.fromLngLat(30.3, 50.5)
  );

  public static final V6ForwardGeocodingRequestOptions TEST_COMPLETE_FORWARD_OPTIONS =
    createCompleteOptions(TEST_QUERY);

  public static final V6StructuredInputQuery TEST_COMPLETE_STRUCTURED_INPUT =
    V6StructuredInputQuery.builder()
      .addressNumber("test-address-number")
      .street("test-street")
      .block("test-block")
      .place("test-place")
      .region("test-region")
      .postcode("test-postcode")
      .locality("test-locality")
      .neighborhood("test-neighborhood")
      .build();


  public static final V6ForwardGeocodingRequestOptions TEST_COMPLETE_STRUCTURED_INPUT_OPTIONS =
    createCompleteOptions(TEST_COMPLETE_STRUCTURED_INPUT);

  public static final V6ReverseGeocodingRequestOptions TEST_COMPLETE_REVERSE_OPTIONS =
    V6ReverseGeocodingRequestOptions.builder(TEST_POINT)
      .country(Locale.FRANCE.getCountry(), Locale.ITALY.getCountry())
      .language(Locale.FRANCE.getLanguage())
      .limit(3)
      .types(V6FeatureType.COUNTRY, V6FeatureType.PLACE)
      .worldview(V6Worldview.USA)
      .build();

  private static V6ForwardGeocodingRequestOptions createCompleteOptions(V6StructuredInputQuery query) {
    final V6ForwardGeocodingRequestOptions.Builder builder = V6ForwardGeocodingRequestOptions
      .builder(query);

    return setTestParameters(builder).build();
  }

  private static V6ForwardGeocodingRequestOptions createCompleteOptions(String query) {
    final V6ForwardGeocodingRequestOptions.Builder builder = V6ForwardGeocodingRequestOptions
      .builder(query);

    return setTestParameters(builder).build();
  }

  private static V6ForwardGeocodingRequestOptions.Builder setTestParameters(
    V6ForwardGeocodingRequestOptions.Builder builder
  ) {
    return builder
      .autocomplete(false)
      .boundingBox(TEST_BBOX)
      .country(Locale.FRANCE.getCountry(), Locale.ITALY.getCountry())
      .language(Locale.FRANCE.getLanguage())
      .limit(7)
      .proximity(Point.fromLngLat(10.1, 20.2))
      .types(V6FeatureType.COUNTRY, V6FeatureType.PLACE)
      .worldview(V6Worldview.USA);
  }
}
