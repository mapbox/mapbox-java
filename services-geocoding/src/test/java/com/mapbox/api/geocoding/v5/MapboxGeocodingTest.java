package com.mapbox.api.geocoding.v5;

import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Point;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapboxGeocodingTest extends GeocodingTestUtils {

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
  public void sanity_batchGeocodeSingleItemRequest() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
            .mode(GeocodingCriteria.MODE_PLACES_PERMANENT)
            .accessToken(ACCESS_TOKEN)
            .query("20001")
            .baseUrl(mockUrl.toString())
            .build();
    assertNotNull(mapboxGeocoding);
    Response<List<GeocodingResponse>> response = mapboxGeocoding.executeBatchCall();
    assertEquals(200, response.code());
  }

  @Test
  public void executeBatchCall_exceptionThrownWhenModeNotSetCorrectly() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Use getCall() for non-batch calls or set the mode to `permanent` for batch requests."));
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    mapboxGeocoding.executeBatchCall();
  }

  @Test
  public void build_noAccessTokenExceptionThrown() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Missing required properties: accessToken");
   MapboxGeocoding.builder()
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void build_invalidAccessTokenExceptionThrown() {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
      startsWith("Using Mapbox Services requires setting a valid access token."));
    MapboxGeocoding.builder()
      .accessToken("")
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
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
  public void baseUrl_doesChangeTheRequestUrl() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl("https://foobar.com")
      .query(Point.fromLngLat(-77.03655, 38.89770))
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString()
      .startsWith("https://foobar.com"));
  }

  @Test
  public void country_localeCountryConvertsCorrectly() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .country(Locale.US)
      .query(Point.fromLngLat(-77.03655, 38.89770))
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString().contains("country=US"));
  }

  @Test
  public void country_localeCountryHandlesMultipleCountriesCorrectly() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .country(Locale.US)
      .country(Locale.CANADA)
      .query(Point.fromLngLat(-77.03655, 38.89770))
      .build();
    assertEquals("US,CA",
      mapboxGeocoding.cloneCall().request().url().queryParameter("country"));
  }

  @Test
  public void proximity_doesGetAddedToUrlCorrectly() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .query("1600 pennsylvania ave nw")
      .proximity(Point.fromLngLat(-77.03655, 38.89770))
      .build();
    assertEquals("-77.03655,38.8977",
      mapboxGeocoding.cloneCall().request().url().queryParameter("proximity"));
  }

  @Test
  public void geocodingTypes_getsAddedToUrlCorrectly() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .geocodingTypes(GeocodingCriteria.TYPE_COUNTRY, GeocodingCriteria.TYPE_DISTRICT)
      .query("1600 pennsylvania ave nw")
      .build();
    assertEquals("country,district",
      mapboxGeocoding.cloneCall().request().url().queryParameter("types"));
  }

  @Test
  public void autocomplete_getsAddedToUrlCorrectly() {
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
  public void bbox_getsFormattedCorrectlyForUrl() {
    BoundingBox bbox = BoundingBox.fromLngLats(
            -77.083056, 38.908611, -76.997778, 38.959167);
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
            .accessToken(ACCESS_TOKEN)
            .baseUrl(mockUrl.toString())
            .bbox(bbox)
            .query("1600 pennsylvania ave nw")
            .build();
    assertEquals("-77.083056,38.908611,-76.997778,38.959167",
            mapboxGeocoding.cloneCall().request().url().queryParameter("bbox"));
  }

  @Test
  public void bbox_asPoints_getsFormattedCorrectlyForUrl() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .bbox(
        Point.fromLngLat(-77.083056, 38.908611),
        Point.fromLngLat(-76.997778, 38.959167)
      )
      .query("1600 pennsylvania ave nw")
      .build();
    assertEquals("-77.083056,38.908611,-76.997778,38.959167",
      mapboxGeocoding.cloneCall().request().url().queryParameter("bbox"));
  }

  @Test
  public void limit_getsAddedToUrlCorrectly() {
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
  public void language_getsConvertedToUrlCorrectly() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .languages(Locale.ENGLISH, Locale.FRANCE)
      .query("1600 pennsylvania ave nw")
      .build();
    assertEquals("en,fr",
      mapboxGeocoding.cloneCall().request().url().queryParameter("language"));
  }

  @Test
  public void clientAppName_hasAppInString() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl(mockUrl.toString())
      .clientAppName("APP")
      .languages(Locale.ENGLISH, Locale.FRANCE)
      .query("1600 pennsylvania ave nw")
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().header("User-Agent").contains("APP"));
  }

  @Test
  public void reverseMode_getsAddedToUrlCorrectly() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl("https://foobar.com")
      .query(Point.fromLngLat(-73.989,40.733))
      .reverseMode(GeocodingCriteria.REVERSE_MODE_SCORE)
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString()
      .contains("reverseMode=score"));

    mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl("https://foobar.com")
      .query(Point.fromLngLat(-73.989,40.733))
      .reverseMode(GeocodingCriteria.REVERSE_MODE_DISTANCE)
      .build();
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString()
      .contains("reverseMode=distance"));
  }

  @Test
  public void reverseMode_onlyLimit1_Allowed() {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Limit must be combined with a single type"));
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl("https://foobar.com")
      .query(Point.fromLngLat(-73.989,40.733))
      .reverseMode(GeocodingCriteria.REVERSE_MODE_SCORE)
      .limit(2)
      .build();
  }

  @Test
  public void fuzzyMatchSanity() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("wahsington")
      .fuzzyMatch(true)
      .baseUrl(mockUrl.toString())
      .build();
    assertNotNull(mapboxGeocoding);
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertEquals(200, response.code());
  }

  @Test
  public void fuzzyMatch_getsAddedToUrlCorrectly() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("wahsington")
      .fuzzyMatch(true)
      .baseUrl(mockUrl.toString())
      .build();
    assertNotNull(mapboxGeocoding);
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString()
      .contains("fuzzyMatch=true"));
  }

  @Test
  public void routingSanity() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
        .accessToken(ACCESS_TOKEN)
        .query("wahsington")
        .routing(true)
        .baseUrl(mockUrl.toString())
        .build();
    assertNotNull(mapboxGeocoding);
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertEquals(200, response.code());
  }

  @Test
  public void routing_defaultIsNotSpecified() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
        .accessToken(ACCESS_TOKEN)
        .query("wahsington")
        .baseUrl(mockUrl.toString())
        .build();
    assertNotNull(mapboxGeocoding);
    assertFalse(mapboxGeocoding.cloneCall().request().url().toString()
        .contains("routing="));
  }

  @Test
  public void routing_getsAddedToUrlCorrectly() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
        .accessToken(ACCESS_TOKEN)
        .query("wahsington")
        .routing(true)
        .baseUrl(mockUrl.toString())
        .build();
    assertNotNull(mapboxGeocoding);
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString()
        .contains("routing=true"));
  }

  @Test
  public void intersectionSearchSanity() throws IOException {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .intersectionStreets("Market Street", "Fremont Street")
      .proximity("-122.39738575285674,37.792514711136945")
      .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
      .baseUrl(mockUrl.toString())
      .build();
    assertNotNull(mapboxGeocoding);
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertEquals(200, response.code());
  }

  @Test
  public void intersectionSearchAndAddedCorrectly() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
            .accessToken(ACCESS_TOKEN)
            .intersectionStreets("Street one", "Street two")
            .proximity("-122.39738575285674,37.792514711136945")
            .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
            .baseUrl(mockUrl.toString())
            .build();
    assertNotNull(mapboxGeocoding);
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString().contains("and"));
  }

  @Test
  public void intersectionSearch_AddIntersectionToQuery() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .intersectionStreets("Market Street", "Fremont Street")
      .proximity("-122.39738575285674,37.792514711136945")
      .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
      .baseUrl(mockUrl.toString())
      .build();
    assertNotNull(mapboxGeocoding);
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString()
      .contains("Market%20Street%20and%20Fremont%20Street"));
  }

  @Test
  public void intersectionSearch_WrongMode() {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
            startsWith("Geocoding mode must be GeocodingCriteria.MODE_PLACES or GeocodingCriteria.MODE_PLACES_PERMANENT for intersection search."));
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .mode("RandomMode")
      .intersectionStreets("Market Street", "Fremont Street")
      .proximity("-122.39738575285674,37.792514711136945")
      .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void intersectionSearch_AutoSetGeocodingType() {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .intersectionStreets("Market Street", "Fremont Street")
      .proximity("-122.39738575285674,37.792514711136945")
      .baseUrl(mockUrl.toString())
      .build();
    assertNotNull(mapboxGeocoding);
    assertTrue(mapboxGeocoding.cloneCall().request().url().toString()
            .contains("types=address"));
  }

  @Test
  public void intersectionSearch_EmptyProximity() {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(
            startsWith("Geocoding proximity must be set for intersection search."));
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .intersectionStreets("Market Street", "Fremont Street")
      .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
      .baseUrl(mockUrl.toString())
      .build();
  }
}
