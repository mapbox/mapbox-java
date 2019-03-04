package com.mapbox.geojson;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.mapbox.core.TestUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiPointTest extends TestUtils {

  private static final String SAMPLE_MULTIPOINT = "sample-multipoint.json";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    MultiPoint multiPoint = MultiPoint.fromLngLats(points);
    assertNotNull(multiPoint);
  }

  @Test
  public void bbox_nullWhenNotSet() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    MultiPoint multiPoint = MultiPoint.fromLngLats(points);
    assertNull(multiPoint.bbox());
  }

  @Test
  public void bbox_doesNotSerializeWhenNotPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    MultiPoint multiPoint = MultiPoint.fromLngLats(points);
    compareJson(multiPoint.toJson(),
      "{\"coordinates\":[[1,2],[2,3]],\"type\":\"MultiPoint\"}");
  }

  @Test
  public void bbox_returnsCorrectBbox() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    MultiPoint multiPoint = MultiPoint.fromLngLats(points, bbox);
    assertNotNull(multiPoint.bbox());
    assertEquals(1.0, multiPoint.bbox().west(), DELTA);
    assertEquals(2.0, multiPoint.bbox().south(), DELTA);
    assertEquals(3.0, multiPoint.bbox().east(), DELTA);
    assertEquals(4.0, multiPoint.bbox().north(), DELTA);
  }

  @Test
  public void bbox_doesSerializeWhenPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    MultiPoint multiPoint = MultiPoint.fromLngLats(points, bbox);
    compareJson(multiPoint.toJson(),
      "{\"coordinates\":[[1,2],[2,3]],\"type\":\"MultiPoint\",\"bbox\":[1.0,2.0,3.0,4.0]}");
  }

  @Test
  public void testSerializable() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    MultiPoint multiPoint = MultiPoint.fromLngLats(points, bbox);
    byte[] bytes = serialize(multiPoint);
    assertEquals(multiPoint, deserialize(bytes, MultiPoint.class));
  }

  @Test
  public void fromJson() throws IOException {
    final String json = "{ \"type\": \"MultiPoint\"," +
            "\"coordinates\": [ [100, 0], [101, 1] ] } ";
    MultiPoint geo = MultiPoint.fromJson(json);
    assertEquals(geo.type(), "MultiPoint");
    assertEquals(geo.coordinates().get(0).longitude(), 100.0, DELTA);
    assertEquals(geo.coordinates().get(0).latitude(), 0.0, DELTA);
    assertEquals(geo.coordinates().get(1).longitude(), 101.0, DELTA);
    assertEquals(geo.coordinates().get(1).latitude(), 1.0, DELTA);
    assertFalse(geo.coordinates().get(0).hasAltitude());
    assertEquals(Double.NaN, geo.coordinates().get(0).altitude(), DELTA);
  }

  @Test
  public void toJson() throws IOException {
    final String json = "{ \"type\": \"MultiPoint\"," +
            "\"coordinates\": [ [100, 0], [101, 1] ] } ";
    MultiPoint geo = MultiPoint.fromJson(json);
    compareJson(json, geo.toJson());
  }

  @Test
  public void fromJson_coordinatesPresent() throws Exception {
    thrown.expect(NullPointerException.class);
    MultiPoint.fromJson("{\"type\":\"MultiPoint\",\"coordinates\":null}");
  }
}