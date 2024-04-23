package com.mapbox.api.geocoding.v6;

import com.mapbox.api.geocoding.v6.models.V6FeatureType;
import com.mapbox.api.geocoding.v6.models.V6Worldview;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class V6ReverseGeocodingRequestOptionsTest extends V6GeocodingTestUtils {

  @Test
  public void testUnspecifiedParametersAreNull() {
    final V6ReverseGeocodingRequestOptions options = V6ReverseGeocodingRequestOptions
      .builder(TEST_POINT)
      .build();

    assertNull(options.country());
    assertNull(options.language());
    assertNull(options.limit());
    assertNull(options.types());
    assertNull(options.worldview());
  }

  @Test
  public void testPoint() {
    final V6ReverseGeocodingRequestOptions options = V6ReverseGeocodingRequestOptions
      .builder(TEST_POINT)
      .build();

    assertEquals(TEST_POINT.longitude(), options.longitude(), DELTA);
    assertEquals(TEST_POINT.latitude(), options.latitude(), DELTA);
  }

  @Test
  public void testCountry() {
    final V6ReverseGeocodingRequestOptions options = V6ReverseGeocodingRequestOptions
      .builder(TEST_POINT)
      .country(Locale.FRANCE.getCountry(), Locale.ITALY.getCountry())
      .build();

    assertEquals("FR,IT", options.country());
  }

  @Test
  public void testLanguage() {
    final V6ReverseGeocodingRequestOptions options = V6ReverseGeocodingRequestOptions
      .builder(TEST_POINT)
      .language(Locale.FRANCE.getLanguage())
      .build();

    assertEquals("fr", options.language());
  }

  @Test
  public void testLimit() {
    final V6ReverseGeocodingRequestOptions options = V6ReverseGeocodingRequestOptions
      .builder(TEST_POINT)
      .limit(3)
      .build();

    assertEquals(Integer.valueOf(3), options.limit());
  }

  @Test
  public void testTypes() {
    final V6ReverseGeocodingRequestOptions options = V6ReverseGeocodingRequestOptions
      .builder(TEST_POINT)
      .types(V6FeatureType.COUNTRY, V6FeatureType.PLACE)
      .build();

    assertEquals("country,place", options.apiFormattedTypes());
    assertEquals(Arrays.asList("country", "place"), options.types());
  }

  @Test
  public void testWorldview() {
    final V6ReverseGeocodingRequestOptions options = V6ReverseGeocodingRequestOptions
      .builder(TEST_POINT)
      .worldview(V6Worldview.USA)
      .build();

    assertEquals("us", options.worldview());
  }

  @Test
  public void testCompleteReverseOptionsJsonSerialization() throws IOException {
    final String json = TEST_COMPLETE_REVERSE_OPTIONS.toJson();
    assertEquals(
      loadCompressedJson("v6/reverse_request_options_serialised.json"),
      json
    );
  }

  @Test
  public void testMinimalReverseOptionsJsonSerialization() throws IOException {
    final V6ReverseGeocodingRequestOptions options = V6ReverseGeocodingRequestOptions
      .builder(TEST_POINT)
      .build();

    final String json = options.toJson();
    assertEquals(
      loadCompressedJson("v6/reverse_request_options_minimal_serialised.json"),
      json
    );
  }
}
