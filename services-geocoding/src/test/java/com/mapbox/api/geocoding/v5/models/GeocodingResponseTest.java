package com.mapbox.api.geocoding.v5.models;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.mapbox.api.geocoding.v5.GeocodingTestUtils;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import junit.framework.TestCase;

import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GeocodingResponseTest extends GeocodingTestUtils {

  @Test
  public void sanity() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertEquals(200, response.code());
  }

  @Test
  public void builder_doesSuccessfullyBuildGeocodingResponse() throws Exception {
    GeocodingResponse response = GeocodingResponse.builder()
      .attribution("attribution")
      .features(new ArrayList<CarmenFeature>())
      .query(new ArrayList<String>())
      .build();
    assertThat(response, notNullValue());
    assertThat(response.attribution(), equalTo("attribution"));
    assertThat(response.type(), equalTo("FeatureCollection"));
    assertEquals(0, response.features().size());
    assertEquals(0, response.query().size());
  }

  @Test
  public void fromJson_handlesConversionCorrectly() throws Exception {
    String json = loadJsonFixture(FORWARD_VALID);
    GeocodingResponse response = GeocodingResponse.fromJson(json);

    assertThat(response.attribution(), equalTo("NOTICE: © 2016 Mapbox and its "
      + "suppliers. All rights reserved. Use of this data is subject to the Mapbox Terms of Service"
      + " (https://www.mapbox.com/about/maps/). This response and the information it contains may "
      + "not be retained."));
    assertThat(response.type(), equalTo("FeatureCollection"));
    assertThat(response.query().get(0), equalTo("1600"));
    assertThat(response.query().get(1), equalTo("pennsylvania"));
    assertThat(response.query().get(2), equalTo("ave"));
    assertEquals(3, response.query().size());

    // response carmen features are checked in separate test class.
    assertEquals(4, response.features().size());
  }

  @Test
  public void toJson_handlesConversionCorrectly() throws IOException {
    String json = loadJsonFixture(FORWARD_VALID);
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertEquals(200, response.code());
    compareJson(json, response.body().toJson());
  }

  @Test
  public void forwardRequest_invalidQuery() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("sandy")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertEquals(200, response.code());
    GeocodingResponse object = response.body();
    assertNotNull(object);

    assertEquals(4, object.query().size());
    assertThat(object.query().get(0), equalTo("sandy"));
    assertThat(object.query().get(1), equalTo("island"));
    assertThat(object.query().get(2), equalTo("new"));
    assertThat(object.query().get(3), equalTo("caledonia"));
    assertThat(object.attribution(), equalTo("NOTICE: © 2016 Mapbox and its "
      + "suppliers. All rights reserved. Use of this data is subject to the Mapbox Terms of Service"
      + " (https://www.mapbox.com/about/maps/). This response and the information it contains may "
      + "not be retained."));
    assertThat(object.type(), equalTo("FeatureCollection"));
    assertThat(object.features(), notNullValue());
    assertEquals(0, object.features().size());
  }

  @Test
  public void toFromJson() {

    Map<String, String> texts = new HashMap<>();
    texts.put("ru", "округ Колумбия");
    texts.put("fr", "district de Columbia");

    CarmenContext carmenContext = CarmenContext.builder()
            .id("123")
            .shortCode("shortCode")
            .text(texts)
            .text(texts.get("ru"))
            .languages(Arrays.asList("ru", "fr"))
            .language("ru")
            .build();

    texts = new HashMap<>();
    texts.put("ru", "Соединённые Штаты Америки");
    texts.put("fr", "États-Unis");

    Map<String, String> placeNames = new HashMap<>();
    placeNames.put("ru", "Соединённые Штаты Америки");
    placeNames.put("fr", "États-Unis");

    CarmenFeature carmenFeature = CarmenFeature.builder()
            .id("id")
            .placeType(Arrays.asList("country"))
            .relevance(1.0)
            .texts(texts)
            .text(texts.get("ru"))
            .placeNames(placeNames)
            .placeName(placeNames.get("ru"))
            .languages(Arrays.asList("ru", "fr"))
            .language("ru")
            .context(Arrays.asList(carmenContext))
            .build();

    GeocodingResponse geocodingResponse = GeocodingResponse.builder()
            .attribution("attribution")
            .features(Arrays.asList(carmenFeature))
            .query(Arrays.asList("query"))
            .type("Feature")
            .build();

    String jsonString = geocodingResponse.toJson();
    GeocodingResponse geocodingResponseFromJson = GeocodingResponse.fromJson(jsonString);
    TestCase.assertEquals(geocodingResponse, geocodingResponseFromJson);
  }
}