package com.mapbox.api.geocoding.v5.models;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.GeocodingTestUtils;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.CoordinateContainer;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

import org.junit.Assert;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CarmenFeatureTest extends GeocodingTestUtils {

  private static final String FORWARD_FEATURE_VALID = "forward_feature_valid.json";

  @Test
  public void sanity() throws Exception {
    CarmenFeature carmenFeature = CarmenFeature.builder()
      .properties(new JsonObject())
      .address("1234")
      .build();
    assertThat(carmenFeature, notNullValue());
    assertThat(carmenFeature.address(), equalTo("1234"));
  }

  @Test
  public void bbox_returnsGeoJsonBoundingBoxObject() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query(Point.fromLngLat(1.0, 2.0))
      .country(Locale.CHINESE)
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertThat(response.body().features().get(0).bbox(), notNullValue());
  }

  @Test
  public void geometry_returnsPointGeometry() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query(Point.fromLngLat(-77.0366, 38.8971))
      .baseUrl(mockUrl.toString())
      .build();

    GeocodingResponse response = mapboxGeocoding.executeCall().body();
    assertNotNull(response);
    CarmenFeature feature = response.features().get(0);
    assertThat(feature.geometry(), notNullValue());
    assertTrue(feature.geometry() instanceof Point);
    assertThat(((Point) feature.geometry()).longitude(), equalTo(106.820552));
    assertThat(((Point) feature.geometry()).latitude(), equalTo(39.458115));
  }

  @Test
  public void properties_isJsonObject() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query(Point.fromLngLat(-77.0366, 38.8971))
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    CarmenFeature feature = response.body().features().get(0);
    assertThat(feature.properties(), notNullValue());
  }

  @Test
  public void fromJson_handlesConversionCorrectly() throws Exception {
    String json = loadJsonFixture(FORWARD_FEATURE_VALID);
    CarmenFeature feature = CarmenFeature.fromJson(json);

    assertThat(feature.type(), equalTo("Feature"));
    assertEquals(5, feature.context().size());
    assertThat(feature.geometry().type(), equalTo("Point"));
    assertThat(((CoordinateContainer) feature.geometry()).coordinates().toString(),
      equalTo("[-77.036543, 38.897702]"));
    assertThat(feature.address(), equalTo("1600"));
    assertThat(feature.id(), equalTo("address.3982178573139850"));
    assertEquals(1, feature.placeType().size());
    assertThat(feature.placeType().get(0), equalTo("address"));
    assertThat(feature.relevance(), equalTo(1.0));
    assertThat(feature.placeName(), equalTo("1600 Pennsylvania Ave NW, Washington,"
      + " District of Columbia 20006, United States"));
    assertThat(feature.text(), equalTo("Pennsylvania Ave NW"));
    assertThat(feature.center().latitude(), equalTo(38.897702));
    assertThat(feature.center().longitude(), equalTo(-77.036543));
    assertThat(feature.language(), nullValue());
  }

  @Test
  public void toJson_handlesConversionCorrectly() throws IOException {
    String json = loadJsonFixture(FORWARD_FEATURE_VALID);
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    GeocodingResponse response = mapboxGeocoding.executeCall().body();
    assertNotNull(response);
    CarmenFeature feature = response.features().get(0);
    compareJson(json, feature.toJson());
  }

  @Test
  public void ForwardGeocode_withValidChineseResponse() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query(Point.fromLngLat(1.0, 2.0))
      .country(Locale.CHINESE)
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertEquals(200, response.code());
    GeocodingResponse object = response.body();
    assertNotNull(object);
    CarmenFeature feature = object.features().get(0);

    assertEquals(1, object.query().size());
    assertThat(object.query().get(0), equalTo("hainan"));
    assertThat(feature.type(), equalTo("Feature"));


    assertEquals(3, feature.context().size());
    assertThat(feature.geometry().type(), equalTo("Point"));
    assertThat(((CoordinateContainer) feature.geometry()).coordinates().toString(),
      equalTo("[106.820552, 39.458115]"));
    assertThat(feature.id(), equalTo("place.10514057239276310"));
    assertThat(feature.relevance(), equalTo(0.99));
    assertThat(feature.placeName(), equalTo("中国内蒙古乌海市海南区"));
    assertThat(feature.text(), equalTo("海南区"));
    assertThat(feature.center().latitude(), equalTo(39.458115));
    assertThat(feature.center().longitude(), equalTo(106.820552));
    assertThat(feature.language(), nullValue());
    assertTrue(feature.properties().get("wikidata").isJsonNull());
    assertThat(feature.bbox().west(), equalTo(106.733581544));
    assertThat(feature.bbox().south(), equalTo(39.308357239));
    assertThat(feature.bbox().east(), equalTo(107.025123596));
    assertThat(feature.bbox().north(), equalTo(39.6012458800001));
  }

  @Test
  public void testNullProperties() {
    CarmenFeature feature = CarmenFeature.builder()
      .geometry(Point.fromLngLat(-77, 38))
      .build();
    String jsonString = feature.toJson();
    assertFalse(jsonString.contains("\"properties\":{}"));

    // Feature (empty Properties) -> Json (null Properties) -> Equavalent Feature
    CarmenFeature featureFromJson = CarmenFeature.fromJson(jsonString);
    assertEquals(featureFromJson, feature);
  }

  @Test
  public void testNullPropertiesJson() {
    String jsonString = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-77.0,38.0]}}";
    CarmenFeature feature = CarmenFeature.fromJson(jsonString);

    // Json( null Properties) -> Feature (empty Properties) -> Json(null Properties)
    String fromFeatureJsonString = feature.toJson();
    assertEquals(fromFeatureJsonString, jsonString);
  }
}
