package com.mapbox.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.core.TestUtils;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeatureTest extends TestUtils {

  private static final String SAMPLE_FEATURE = "sample-feature.json";
  private static final String SAMPLE_FEATURE_POINT = "sample-feature-point-all.json";

  @Test
  public void sanity() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    Feature feature = Feature.fromGeometry(lineString);
    assertNotNull(feature);
  }

  @Test
  public void bbox_nullWhenNotSet() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    Feature feature = Feature.fromGeometry(lineString);
    assertNull(feature.bbox());
  }

  @Test
  public void bbox_doesNotSerializeWhenNotPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    Feature feature = Feature.fromGeometry(lineString);
    compareJson(feature.toJson(),
      "{\"type\":\"Feature\",\"geometry\":{\"type\":"
        + "\"LineString\",\"coordinates\":[[1,2],[2,3]]}}");
  }

  @Test
  public void bbox_returnsCorrectBbox() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    Feature feature = Feature.fromGeometry(lineString, bbox);
    assertNotNull(feature.bbox());
    assertEquals(1.0, feature.bbox().west(), DELTA);
    assertEquals(2.0, feature.bbox().south(), DELTA);
    assertEquals(3.0, feature.bbox().east(), DELTA);
    assertEquals(4.0, feature.bbox().north(), DELTA);
  }

  @Test
  public void bbox_doesSerializeWhenPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    Feature feature = Feature.fromGeometry(lineString, bbox);
    String featureJsonString = feature.toJson();
    compareJson("{\"type\":\"Feature\",\"bbox\":[1.0,2.0,3.0,4.0],\"geometry\":"
        + "{\"type\":\"LineString\",\"coordinates\":[[1,2],[2,3]]}}",
      feature.toJson());
  }

  @Test
  public void test_point_feature_fromJson() throws IOException {
    final String json =  "{ \"type\": \"Feature\"," +
      "\"geometry\": { \"type\": \"Point\", \"coordinates\": [ 125.6, 10.1] }," +
      "\"properties\": {\"name\": \"Dinagat Islands\" }}";
    Feature geo = Feature.fromJson(json);
    assertEquals(geo.type(), "Feature");
    assertEquals(geo.geometry().type(), "Point");
    assertEquals(((Point)geo.geometry()).longitude(), 125.6, DELTA);
    assertEquals(((Point)geo.geometry()).latitude(), 10.1, DELTA);
    assertEquals(geo.properties().get("name").getAsString(), "Dinagat Islands");
  }

  @Test
  public void test_linestring_feature_fromJson() throws IOException {
      final String json =  "{ \"type\": \"Feature\"," +
      "\"geometry\": { \"type\": \"LineString\", "+
      " \"coordinates\": [[ 102.0, 20],[103.0, 3.0],[104.0, 4.0], [105.0, 5.0]]}," +
      "\"properties\": {\"name\": \"line name\" }}";
    Feature geo = Feature.fromJson(json);
    assertEquals(geo.type(), "Feature");
    assertEquals(geo.geometry().type(), "LineString");
    assertNotNull(geo.geometry());
    List<Point> coordinates = ((LineString) geo.geometry()).coordinates();
    assertNotNull(coordinates);
    assertEquals(4, coordinates.size());
    assertEquals(105.0,coordinates.get(3).longitude(),  DELTA);
    assertEquals(5.0,coordinates.get(3).latitude(),  DELTA);
    assertEquals("line name", geo.properties().get("name").getAsString());
  }

  @Test
  public void test_point_feature_toJson() throws IOException {
    JsonObject properties = new JsonObject();
    properties.addProperty("name", "Dinagat Islands");
    Feature geo = Feature.fromGeometry(Point.fromLngLat(125.6, 10.1),
      properties);
    String geoJsonString = geo.toJson();

    String expectedJson = "{ \"type\": \"Feature\"," +
      "\"geometry\": { \"type\": \"Point\", \"coordinates\": [ 125.6, 10.1] }," +
              "\"properties\": {\"name\": \"Dinagat Islands\" }}";

    compareJson(expectedJson, geoJsonString);
  }

  @Test
  public void testNullProperties() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.1, 2.3));
    coordinates.add(Point.fromLngLat(4.5, 6.7));
    LineString line = LineString.fromLngLats(coordinates);
    Feature feature = Feature.fromGeometry(line);
    String jsonString = feature.toJson();
    assertFalse(jsonString.contains("\"properties\":{}"));

    // Feature (empty Properties) -> Json (null Properties) -> Equavalent Feature
    Feature featureFromJson = Feature.fromJson(jsonString);
    assertEquals(featureFromJson, feature);
  }

  @Test
  public void testNonNullProperties() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.1, 2.3));
    coordinates.add(Point.fromLngLat(4.5, 6.7));
    LineString line = LineString.fromLngLats(coordinates);
    JsonObject properties = new JsonObject();
    properties.addProperty("key", "value");
    Feature feature = Feature.fromGeometry(line, properties);
    String jsonString = feature.toJson();
    assertTrue(jsonString.contains("\"properties\":{\"key\":\"value\"}"));

    // Feature (non-empty Properties) -> Json (non-empty Properties) -> Equavalent Feature
    assertEquals(Feature.fromJson(jsonString), feature);
  }

  @Test
  public void testNullPropertiesJson() {
    String jsonString = "{\"type\":\"Feature\",\"bbox\":[1.0,2.0,3.0,4.0],\"geometry\":"
      + "{\"type\":\"LineString\",\"coordinates\":[[1.0,2.0],[2.0,3.0]]}}";
    Feature feature = Feature.fromJson(jsonString);

    // Json( null Properties) -> Feature (empty Properties) -> Json(null Properties)
    String fromFeatureJsonString = feature.toJson();
    assertEquals(fromFeatureJsonString, jsonString);
  }


  @Test
  public void test_fromJson_toJson() throws IOException {
    final String jsonString = loadJsonFixture(SAMPLE_FEATURE_POINT);
    Feature featureFromJson = Feature.fromJson(jsonString);
    String jsonStringFromFeature = featureFromJson.toJson();

    compareJson(jsonString, jsonStringFromFeature);
  }
}
