package com.mapbox.geojson;

import com.google.gson.JsonObject;
import com.mapbox.services.BaseTest;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FeatureTest extends BaseTest {

  private static final String SAMPLE_FEATURE = "sample-feature.json";

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
        + "\"LineString\",\"coordinates\":[[1,2],[2,3]]},\"properties\":{}}");
  }

  @Test
  public void bbox_returnsCorrectBbox() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);

    BoundingBox bbox = BoundingBox.fromCoordinates(1.0, 2.0, 3.0, 4.0);
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

    BoundingBox bbox = BoundingBox.fromCoordinates(1.0, 2.0, 3.0, 4.0);
    Feature feature = Feature.fromGeometry(lineString, bbox);
    compareJson(feature.toJson(),
      "{\"type\":\"Feature\",\"bbox\":[1.0,2.0,3.0,4.0],\"geometry\":"
        + "{\"type\":\"LineString\",\"coordinates\":[[1,2],[2,3]]},\"properties\":{}}");
  }

  @Test
  public void fromJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_FEATURE);
    Feature geo = Feature.fromJson(json);
    assertEquals(geo.type(), "Feature");
    assertEquals(geo.geometry().type(), "Point");
    assertEquals(geo.properties().get("name").getAsString(), "Dinagat Islands");
  }

  @Test
  public void toJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_FEATURE);
    Feature geo = Feature.fromJson(json);
    compareJson(json, geo.toJson());
  }

  @Test
  public void testNullProperties() {
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0.1, 2.3));
    coordinates.add(Point.fromLngLat(4.5, 6.7));
    LineString line = LineString.fromLngLats(coordinates);
    Feature feature = Feature.fromGeometry(line);
    assertTrue(feature.toJson().contains("\"properties\":{}"));
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
    assertTrue(feature.toJson().contains("\"properties\":{\"key\":\"value\"}"));
  }
}
