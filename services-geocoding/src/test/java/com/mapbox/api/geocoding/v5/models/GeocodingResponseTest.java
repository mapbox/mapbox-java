package com.mapbox.api.geocoding.v5.models;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.core.TestUtils;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;

public class GeocodingResponseTest extends TestUtils {

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
    assert object != null;

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
}