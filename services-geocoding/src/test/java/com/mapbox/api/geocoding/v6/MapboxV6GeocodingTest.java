package com.mapbox.api.geocoding.v6;

import com.mapbox.api.geocoding.v6.models.V6Response;

import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import retrofit2.Response;

public class MapboxV6GeocodingTest extends V6GeocodingTestUtils {

  @Test
  public void sanity() throws Exception {
    server.enqueue(createMockResponse("v6/response_non_empty_valid.json"));

    final V6ForwardGeocodingRequestOptions requestOptions = V6ForwardGeocodingRequestOptions
      .builder("740 15th St NW, Washington, DC 20005")
      .build();

    final MapboxV6Geocoding geocoding = MapboxV6Geocoding
      .builder(ACCESS_TOKEN, requestOptions)
      .baseUrl(mockUrl.toString())
      .build();

    assertNotNull(geocoding);

    final Response<V6Response> response = geocoding.executeCall();
    assertEquals(200, response.code());

    final V6Response body = response.body();
    assertNotNull(body);
    assertEquals(2, body.features().size());
  }

  @Test
  public void errorMessageTest() throws Exception {
    final MockResponse mockResponse = new MockResponse()
      .setBody(loadJsonFixture("v6/error_message.json"))
      .setResponseCode(400);

    server.enqueue(mockResponse);

    final V6ForwardGeocodingRequestOptions requestOptions = V6ForwardGeocodingRequestOptions
      .builder("740 15th St NW, Washington, DC 20005")
      .build();

    final MapboxV6Geocoding geocoding = MapboxV6Geocoding
      .builder(ACCESS_TOKEN, requestOptions)
      .baseUrl(mockUrl.toString())
      .build();

    final Response<V6Response> response = geocoding.executeCall();
    assertNotNull(response);
    assertEquals(400, response.code());
  }

  @Test
  public void testForwardGeocodingRequestParameters() throws InterruptedException, IOException {
    server.enqueue(createErrorMockResponse(500));

    final V6ForwardGeocodingRequestOptions options = TEST_COMPLETE_FORWARD_OPTIONS;

    final MapboxV6Geocoding geocoding = MapboxV6Geocoding
      .builder(ACCESS_TOKEN, options)
      .permanent(true)
      .clientAppName("test-client-app")
      .baseUrl(mockUrl.toString())
      .build();

    geocoding.executeCall();

    final RecordedRequest request = server.takeRequest();

    assertEquals("GET", request.getMethod());

    assertTrue(
      Objects.requireNonNull(request.getHeader("User-Agent")).contains(
        Objects.requireNonNull(geocoding.clientAppName())
      )
    );

    final HttpUrl url = request.getRequestUrl();
    assertEquals("/search/geocode/v6/forward", Objects.requireNonNull(url).encodedPath());
    assertEquals(geocoding.accessToken(), url.queryParameter("access_token"));
    assertEquals(
      Objects.requireNonNull(geocoding.permanent()).toString(),
      url.queryParameter("permanent")
    );
    assertEquals(options.query(), url.queryParameter("q"));
    assertEquals(
      Objects.requireNonNull(options.autocomplete()).toString(),
      url.queryParameter("autocomplete")
    );
    assertEquals(options.apiFormattedBbox(), url.queryParameter("bbox"));
    assertEquals(options.country(), url.queryParameter("country"));
    assertEquals(options.language(), url.queryParameter("language"));
    assertEquals(String.valueOf(options.limit()), url.queryParameter("limit"));
    assertEquals(options.proximity(), url.queryParameter("proximity"));
    assertEquals(options.apiFormattedTypes(), url.queryParameter("types"));
    assertEquals(options.worldview(), url.queryParameter("worldview"));
  }

  @Test
  public void testForwardGeocodingDefaultRequestParameters() throws InterruptedException, IOException {
    server.enqueue(createErrorMockResponse(500));

    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(TEST_QUERY).build();

    final MapboxV6Geocoding geocoding = MapboxV6Geocoding
      .builder(ACCESS_TOKEN, options)
      .baseUrl(mockUrl.toString())
      .build();

    geocoding.executeCall();

    final RecordedRequest request = server.takeRequest();

    final HttpUrl url = request.getRequestUrl();
    assertEquals("/search/geocode/v6/forward", Objects.requireNonNull(url).encodedPath());
    assertEquals(geocoding.accessToken(), url.queryParameter("access_token"));
    assertEquals(TEST_QUERY, url.queryParameter("q"));
    assertNull(url.queryParameter("permanent"));
    assertNull(url.queryParameter("autocomplete"));
    assertNull(url.queryParameter("bbox"));
    assertNull(url.queryParameter("country"));
    assertNull(url.queryParameter("language"));
    assertNull(url.queryParameter("limit"));
    assertNull(url.queryParameter("proximity"));
    assertNull(url.queryParameter("types"));
    assertNull(url.queryParameter("worldview"));
  }

  @Test
  public void testStructuredInputRequestParameters() throws InterruptedException, IOException {
    server.enqueue(createErrorMockResponse(500));

    final V6ForwardGeocodingRequestOptions options = TEST_COMPLETE_STRUCTURED_INPUT_OPTIONS;

    final MapboxV6Geocoding geocoding = MapboxV6Geocoding
      .builder(ACCESS_TOKEN, options)
      .baseUrl(mockUrl.toString())
      .build();

    geocoding.executeCall();

    final RecordedRequest request = server.takeRequest();

    final HttpUrl url = request.getRequestUrl();
    assertEquals("/search/geocode/v6/forward", Objects.requireNonNull(url).encodedPath());
    assertEquals(options.addressLine1(), url.queryParameter("address_line1"));
    assertEquals(options.addressNumber(), url.queryParameter("address_number"));
    assertEquals(options.street(), url.queryParameter("street"));
    assertEquals(options.block(), url.queryParameter("block"));
    assertEquals(options.place(), url.queryParameter("place"));
    assertEquals(options.region(), url.queryParameter("region"));
    assertEquals(options.postcode(), url.queryParameter("postcode"));
    assertEquals(options.locality(), url.queryParameter("locality"));
    assertEquals(options.neighborhood(), url.queryParameter("neighborhood"));
  }

  @Test
  public void testDefaultStructuredInputRequestParameters() throws InterruptedException, IOException {
    server.enqueue(createErrorMockResponse(500));

    final V6StructuredInputQuery structuredInputQuery = V6StructuredInputQuery.builder()
      .addressNumber("test-address-number")
      .build();

    final V6ForwardGeocodingRequestOptions options = V6ForwardGeocodingRequestOptions
      .builder(structuredInputQuery).build();

    final MapboxV6Geocoding geocoding = MapboxV6Geocoding
      .builder(ACCESS_TOKEN, options)
      .baseUrl(mockUrl.toString())
      .build();

    geocoding.executeCall();

    final RecordedRequest request = server.takeRequest();

    final HttpUrl url = request.getRequestUrl();
    assertEquals("/search/geocode/v6/forward", Objects.requireNonNull(url).encodedPath());
    assertEquals(geocoding.accessToken(), url.queryParameter("access_token"));
    assertNull(url.queryParameter("q"));
    assertEquals(structuredInputQuery.addressNumber(), url.queryParameter("address_number"));
    assertNull(url.queryParameter("address_line1"));
    assertNull(url.queryParameter("street"));
    assertNull(url.queryParameter("block"));
    assertNull(url.queryParameter("place"));
    assertNull(url.queryParameter("region"));
    assertNull(url.queryParameter("postcode"));
    assertNull(url.queryParameter("locality"));
    assertNull(url.queryParameter("neighborhood"));
  }

  @Test
  public void testReverseGeocodingRequestParameters() throws InterruptedException, IOException {
    server.enqueue(createErrorMockResponse(500));

    final V6ReverseGeocodingRequestOptions options = TEST_COMPLETE_REVERSE_OPTIONS;

    final MapboxV6Geocoding geocoding = MapboxV6Geocoding
      .builder(ACCESS_TOKEN, options)
      .permanent(true)
      .clientAppName("test-client-app")
      .baseUrl(mockUrl.toString())
      .build();

    geocoding.executeCall();

    final RecordedRequest request = server.takeRequest();

    assertEquals("GET", request.getMethod());

    assertTrue(
      Objects.requireNonNull(request.getHeader("User-Agent")).contains(
        Objects.requireNonNull(geocoding.clientAppName())
      )
    );

    final HttpUrl url = request.getRequestUrl();
    assertEquals("/search/geocode/v6/reverse", Objects.requireNonNull(url).encodedPath());
    assertEquals(geocoding.accessToken(), url.queryParameter("access_token"));
    assertEquals(
      Objects.requireNonNull(geocoding.permanent()).toString(),
      url.queryParameter("permanent")
    );
    assertEquals(options.longitude().toString(), url.queryParameter("longitude"));
    assertEquals(options.latitude().toString(), url.queryParameter("latitude"));
    assertEquals(options.country(), url.queryParameter("country"));
    assertEquals(options.language(), url.queryParameter("language"));
    assertEquals(String.valueOf(options.limit()), url.queryParameter("limit"));
    assertEquals(options.apiFormattedTypes(), url.queryParameter("types"));
    assertEquals(options.worldview(), url.queryParameter("worldview"));
  }

  @Test
  public void testReverseGeocodingDefaultRequestParameters() throws InterruptedException, IOException {
    server.enqueue(createErrorMockResponse(500));

    final V6ReverseGeocodingRequestOptions options = V6ReverseGeocodingRequestOptions
      .builder(TEST_POINT)
      .build();

    final MapboxV6Geocoding geocoding = MapboxV6Geocoding
      .builder(ACCESS_TOKEN, options)
      .baseUrl(mockUrl.toString())
      .build();

    geocoding.executeCall();

    final RecordedRequest request = server.takeRequest();

    final HttpUrl url = request.getRequestUrl();
    assertEquals("/search/geocode/v6/reverse", Objects.requireNonNull(url).encodedPath());
    assertEquals(geocoding.accessToken(), url.queryParameter("access_token"));
    assertEquals(String.valueOf(TEST_POINT.longitude()), url.queryParameter("longitude"));
    assertEquals(String.valueOf(TEST_POINT.latitude()), url.queryParameter("latitude"));
    assertNull(url.queryParameter("permanent"));
    assertNull(url.queryParameter("country"));
    assertNull(url.queryParameter("language"));
    assertNull(url.queryParameter("limit"));
    assertNull(url.queryParameter("types"));
    assertNull(url.queryParameter("worldview"));
  }
}
