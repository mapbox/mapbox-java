package com.mapbox.api.geocoding.v6;

import com.mapbox.api.geocoding.v6.models.V6FeatureType;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Point;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class V6GeocodingUtilsTest extends V6GeocodingTestUtils {

  @Test
  public void testEmptyListSerialization() {
    assertEquals(
      "[]",
      V6GeocodingUtils.serialize(Collections.<V6RequestOptions>emptyList())
    );
  }

  /**
   * In this test we combine all predefined request options from tests
   * {@link V6ForwardGeocodingRequestOptionsTest} and
   * {@link V6ReverseGeocodingRequestOptionsTest}
   */
  @Test
  public void testOptionsSerialization() throws IOException {
    final Map<V6RequestOptions, String> testData = new LinkedHashMap<>();

    final V6ForwardGeocodingRequestOptions minimalStructuredInputOptions =
      V6ForwardGeocodingRequestOptions
        .builder(V6StructuredInputQuery.builder().addressNumber("test-address-number").build())
        .build();

    testData.put(
      minimalStructuredInputOptions,
      loadCompressedJson("v6/forward_structured_input_request_options_minimal_serialised.json")
    );

    testData.put(
      TEST_COMPLETE_STRUCTURED_INPUT_OPTIONS,
      loadCompressedJson("v6/forward_structured_input_request_options_serialised.json")
    );

    final V6ForwardGeocodingRequestOptions minimalForwardOptions = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY)
      .build();

    testData.put(
      minimalForwardOptions,
      loadCompressedJson("v6/forward_request_options_minimal_serialised.json")
    );

    testData.put(
      TEST_COMPLETE_FORWARD_OPTIONS,
      loadCompressedJson("v6/forward_request_options_serialised.json")
    );

    final V6ReverseGeocodingRequestOptions minimalReverseOptions = V6ReverseGeocodingRequestOptions
      .builder(TEST_POINT)
      .build();

    testData.put(
      minimalReverseOptions,
      loadCompressedJson("v6/reverse_request_options_minimal_serialised.json")
    );

    testData.put(
      TEST_COMPLETE_REVERSE_OPTIONS,
      loadCompressedJson("v6/reverse_request_options_serialised.json")
    );

    final String expectedJson = String.format(
      Locale.US,
      "[%s,%s,%s,%s,%s,%s]",
      testData.values().toArray()
    );

    assertEquals(
      removeJsonWhitespaces(expectedJson),
      V6GeocodingUtils.serialize(testData.keySet())
    );
  }

  /**
   * Test case from docs https://docs.mapbox.com/api/search/geocoding-v6/#example-request-batch-geocoding
   */
  @Test
  public void testArbitraryOptionsSerialization() throws IOException {
    final V6ForwardGeocodingRequestOptions forwardOptions = V6ForwardGeocodingRequestOptions
      .builder("1600 Pennsylvania Avenue NW, Washington, DC 20500, United States")
      .types(V6FeatureType.ADDRESS)
      .limit(1)
      .boundingBox(BoundingBox.fromLngLats(-80, 35, -70, 40))
      .build();

    final V6StructuredInputQuery structuredInputQuery = V6StructuredInputQuery.builder()
      .addressNumber("1600")
      .street("Pennsylvania Avenue NW")
      .postcode("20500")
      .place("Washington, DC")
      .build();

    final V6ForwardGeocodingRequestOptions structuredInputOptions = V6ForwardGeocodingRequestOptions
      .builder(structuredInputQuery)
      .country("us")
      .build();

    final V6ReverseGeocodingRequestOptions reverseOptions = V6ReverseGeocodingRequestOptions
      .builder(Point.fromLngLat(-73.986136, 40.748895))
      .types(V6FeatureType.ADDRESS)
      .build();

    assertEquals(
      removeJsonWhitespaces(loadCompressedJson("v6/batch_request_options.json")),
      V6GeocodingUtils.serialize(
        Arrays.asList(forwardOptions, structuredInputOptions, reverseOptions)
      )
    );
  }
}
