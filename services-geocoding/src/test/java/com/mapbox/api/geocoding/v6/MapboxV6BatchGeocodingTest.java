package com.mapbox.api.geocoding.v6;

import com.mapbox.api.geocoding.v6.models.V6BatchResponse;
import com.mapbox.api.geocoding.v6.models.V6Response;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import retrofit2.Response;

public class MapboxV6BatchGeocodingTest extends V6GeocodingTestUtils {

  @Test
  public void sanity() throws Exception {
    server.enqueue(createMockResponse("v6/batch_response_non_empty_valid.json"));

    final MapboxV6BatchGeocoding geocoding = MapboxV6BatchGeocoding
      .builder(ACCESS_TOKEN, TEST_COMPLETE_FORWARD_OPTIONS, TEST_COMPLETE_REVERSE_OPTIONS)
      .baseUrl(mockUrl.toString())
      .build();

    assertNotNull(geocoding);

    final Response<V6BatchResponse> response = geocoding.executeCall();
    assertEquals(200, response.code());

    final V6BatchResponse body = response.body();
    assertNotNull(body);

    final List<V6Response> responses = body.responses();
    assertNotNull(responses);
    assertEquals(3, responses.size());

    assertEquals(2, responses.get(0).features().size());
    assertEquals(1, responses.get(1).features().size());
    assertEquals(0, responses.get(2).features().size());
  }

  @Test
  public void errorMessageTest() throws Exception {
    final MockResponse mockResponse = new MockResponse()
      .setBody(loadJsonFixture("v6/error_message.json"))
      .setResponseCode(400);

    server.enqueue(mockResponse);

    final MapboxV6BatchGeocoding geocoding = MapboxV6BatchGeocoding
      .builder(ACCESS_TOKEN, TEST_COMPLETE_FORWARD_OPTIONS, TEST_COMPLETE_REVERSE_OPTIONS)
      .baseUrl(mockUrl.toString())
      .build();

    final Response<V6BatchResponse> response = geocoding.executeCall();
    assertNotNull(response);
    assertEquals(400, response.code());
  }

  @Test
  public void testRequestParameters() throws InterruptedException, IOException {
    server.enqueue(createErrorMockResponse(500));

    final MapboxV6BatchGeocoding geocoding = MapboxV6BatchGeocoding
      .builder(ACCESS_TOKEN, TEST_COMPLETE_FORWARD_OPTIONS)
      .permanent(true)
      .clientAppName("test-client-app")
      .baseUrl(mockUrl.toString())
      .build();

    geocoding.executeCall();

    final RecordedRequest request = server.takeRequest();

    assertEquals("POST", request.getMethod());

    assertTrue(
      Objects.requireNonNull(request.getHeader("User-Agent")).contains(
        Objects.requireNonNull(geocoding.clientAppName())
      )
    );

    final HttpUrl url = request.getRequestUrl();
    assertEquals("/search/geocode/v6/batch", Objects.requireNonNull(url).encodedPath());
    assertEquals(geocoding.accessToken(), url.queryParameter("access_token"));
    assertEquals(
      Objects.requireNonNull(geocoding.permanent()).toString(),
      url.queryParameter("permanent")
    );

    final Buffer body = request.getBody();
    assertNotNull(body);

    final String json = new String(body.readByteArray(), Charset.defaultCharset());

    assertEquals(
      V6GeocodingUtils.serialize(TEST_COMPLETE_FORWARD_OPTIONS),
      json
    );
  }

  @Test
  public void testDefaultRequestParameters() throws InterruptedException, IOException {
    server.enqueue(createErrorMockResponse(500));

    final MapboxV6BatchGeocoding geocoding = MapboxV6BatchGeocoding
      .builder(ACCESS_TOKEN, TEST_COMPLETE_FORWARD_OPTIONS)
      .baseUrl(mockUrl.toString())
      .build();

    geocoding.executeCall();

    final RecordedRequest request = server.takeRequest();
    final HttpUrl url = request.getRequestUrl();
    assertNull(Objects.requireNonNull(url).queryParameter("permanent"));
  }
}
