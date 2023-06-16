package com.mapbox.api.geocoding.v6;

import com.mapbox.api.geocoding.v6.models.V6FeatureType;
import com.mapbox.api.geocoding.v6.models.V6Worldview;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class V6ForwardGeocodingRequestOptionsTest extends V6GeocodingTestUtils {

  @Test
  public void testEmptyQuery() {
    final Exception e = assertThrows(ServicesException.class, new ThrowingRunnable() {
      @Override
      public void run() {
        V6ForwardGeocodingRequestOptions.builder("");
      }
    });

    assertTrue(e.getMessage().contains("Search query can't be empty"));
  }

  @Test
  public void testUnspecifiedParametersAreNull() {
    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY)
      .build();

    assertNull(options.autocomplete());
    assertNull(options.bbox());
    assertNull(options.country());
    assertNull(options.language());
    assertNull(options.limit());
    assertNull(options.proximity());
    assertNull(options.types());
    assertNull(options.worldview());
  }

  @Test
  public void testQuery() {
    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY)
      .build();

    assertEquals(TEST_QUERY, options.query());
  }

  @Test
  public void testAutocomplete() {
    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY)
      .autocomplete(false)
      .build();

    assertEquals(false, options.autocomplete());
  }

  @Test
  public void testBoundingBox() {
    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY)
      .boundingBox(TEST_BBOX)
      .build();

    assertEquals("10.1,20.2,30.3,50.5", options.apiFormattedBbox());
    assertEquals(Arrays.asList(10.1, 20.2, 30.3, 50.5), options.bbox());
  }

  @Test
  public void testCountry() {
    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY)
      .country(Locale.FRANCE.getCountry(), "Italy")
      .build();

    assertEquals("FR,Italy", options.country());
  }

  @Test
  public void testLanguage() {
    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY)
      .language(Locale.FRANCE.getLanguage())
      .build();

    assertEquals("fr", options.language());
  }

  @Test
  public void testLimit() {
    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY)
      .limit(7)
      .build();

    assertEquals(Integer.valueOf(7), options.limit());
  }

  @Test
  public void testProximityPoint() {
    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY)
      .proximity(Point.fromLngLat(10.1, 20.2))
      .build();

    assertEquals("10.1,20.2", options.proximity());
  }

  @Test
  public void testProximityIp() {
    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY)
      .withIpAsProximity()
      .build();

    assertEquals("ip", options.proximity());
  }

  @Test
  public void testTypes() {
    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY)
      .types(V6FeatureType.COUNTRY, V6FeatureType.PLACE)
      .build();

    assertEquals("country,place", options.apiFormattedTypes());
    assertEquals(Arrays.asList("country", "place"), options.types());
  }

  @Test
  public void testWorldview() {
    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY)
      .worldview(V6Worldview.USA)
      .build();

    assertEquals("us", options.worldview());
  }

  @Test
  public void testRequestOptionsJsonSerialization() throws IOException {
    final String json = TEST_COMPLETE_FORWARD_OPTIONS.toJson();
    assertEquals(
      loadCompressedJson("v6/forward_request_options_serialised.json"),
      json
    );
  }

  @Test
  public void testMinimalRequestOptionsJsonSerialization() throws IOException {
    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY)
      .build();

    final String json = options.toJson();
    assertEquals(
      loadCompressedJson("v6/forward_request_options_minimal_serialised.json"),
      json
    );
  }

  @Test
  public void testStructuredInputRequestOptionsJsonSerialization() throws IOException {
    final String json = TEST_COMPLETE_STRUCTURED_INPUT_OPTIONS.toJson();
    assertEquals(
      loadCompressedJson("v6/forward_structured_input_request_options_serialised.json"),
      json
    );
  }

  @Test
  public void testMinimalStructuredInputRequestOptionsJsonSerialization() throws IOException {
    final V6StructuredInputQuery query = V6StructuredInputQuery
      .builder()
      .addressNumber("test-address-number")
      .build();

    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(query)
      .build();

    final String json = options.toJson();
    assertEquals(
      loadCompressedJson("v6/forward_structured_input_request_options_minimal_serialised.json"),
      json
    );
  }
}
