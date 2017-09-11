package com.mapbox.services.commons.geojson;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PolygonTest extends BaseTest {

  private static final String SAMPLE_POLYGON = "sample-polygon.json";
  private static final String SAMPLE_POLYGON_HOLES = "sample-polygon-holes.json";

  @Test
  public void sanity() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(1.0, 2.0));
    LineString outer = LineString.fromLngLats(points);
    Polygon polygon = Polygon.fromOuterInner(outer);
    assertNotNull(polygon);
  }

  @Test
  public void bbox_nullWhenNotSet() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(1.0, 2.0));
    LineString outer = LineString.fromLngLats(points);

    List<LineString> inner = new ArrayList<>();
    inner.add(LineString.fromLngLats(points));
    inner.add(LineString.fromLngLats(points));
    Polygon polygon = Polygon.fromOuterInner(outer, inner);
    assertNull(polygon.bbox());
  }

  @Test
  public void bbox_doesNotSerializeWhenNotPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(1.0, 2.0));
    LineString outer = LineString.fromLngLats(points);

    List<LineString> inner = new ArrayList<>();
    inner.add(LineString.fromLngLats(points));
    inner.add(LineString.fromLngLats(points));
    Polygon polygon = Polygon.fromOuterInner(outer, inner);
    compareJson(polygon.toJson(),
      "{\"type\":\"Polygon\",\"coordinates\":"
        + "[[[1,2],[2,3],[3,4],[1,2]],[[1,2],[2,3],[3,4],[1,2]],[[1,2],[2,3],[3,4],[1,2]]]}");
  }

  @Test
  public void bbox_returnsCorrectBbox() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(1.0, 2.0));
    LineString outer = LineString.fromLngLats(points);

    double[] bbox = new double[] {1.0, 2.0, 3.0, 4.0};
    List<LineString> inner = new ArrayList<>();
    inner.add(LineString.fromLngLats(points));
    inner.add(LineString.fromLngLats(points));
    Polygon polygon = Polygon.fromOuterInner(outer, bbox, inner);
    assertNotNull(polygon.bbox());
    assertEquals(4, polygon.bbox().length);
    assertEquals(1.0, polygon.bbox()[0], DELTA);
    assertEquals(2.0, polygon.bbox()[1], DELTA);
    assertEquals(3.0, polygon.bbox()[2], DELTA);
    assertEquals(4.0, polygon.bbox()[3], DELTA);
  }

  @Test
  public void bbox_doesSerializeWhenPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(1.0, 2.0));
    LineString outer = LineString.fromLngLats(points);

    double[] bbox = new double[] {1.0, 2.0, 3.0, 4.0};
    List<LineString> inner = new ArrayList<>();
    inner.add(LineString.fromLngLats(points));
    inner.add(LineString.fromLngLats(points));
    Polygon polygon = Polygon.fromOuterInner(outer, bbox, inner);
    compareJson(polygon.toJson(),
      "{\"type\":\"Polygon\",\"bbox\":[1.0,2.0,3.0,4.0],\"coordinates\":"
        + "[[[1,2],[2,3],[3,4],[1,2]],[[1,2],[2,3],[3,4],[1,2]],[[1,2],[2,3],[3,4],[1,2]]]}");
  }

  @Test
  public void testSerializable() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(1.0, 2.0));
    LineString outer = LineString.fromLngLats(points);

    double[] bbox = new double[] {1.0, 2.0, 3.0, 4.0};
    List<LineString> inner = new ArrayList<>();
    inner.add(LineString.fromLngLats(points));
    inner.add(LineString.fromLngLats(points));
    Polygon polygon = Polygon.fromOuterInner(outer, bbox, inner);
    byte[] bytes = serialize(polygon);
    assertEquals(polygon, deserialize(bytes, Polygon.class));
  }

  @Test
  public void fromJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_POLYGON);
    Polygon geo = Polygon.fromJson(json);
    assertEquals(geo.type(), "Polygon");
    assertEquals(geo.coordinates().get(0).get(0).longitude(), 100.0, DELTA);
    assertEquals(geo.coordinates().get(0).get(0).latitude(), 0.0, DELTA);
    assertFalse(geo.coordinates().get(0).get(0).hasAltitude());
  }

  @Test
  public void fromJsonHoles() throws IOException {
    final String json = loadJsonFixture(SAMPLE_POLYGON_HOLES);
    Polygon geo = Polygon.fromJson(json);
    assertEquals(geo.type(), "Polygon");
    assertEquals(geo.coordinates().get(0).get(0).longitude(), 100.0, DELTA);
    assertEquals(geo.coordinates().get(0).get(0).latitude(), 0.0, DELTA);
    assertEquals(geo.coordinates().size(), 2);
    assertEquals(geo.coordinates().get(1).get(0).longitude(), 100.8, DELTA);
    assertEquals(geo.coordinates().get(1).get(0).latitude(), 0.8, DELTA);
    assertFalse(geo.coordinates().get(0).get(0).hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_POLYGON);
    Polygon geo = Polygon.fromJson(json);
    compareJson(json, geo.toJson());
  }

  @Test
  public void toJsonHoles() throws IOException {
    final String json = loadJsonFixture(SAMPLE_POLYGON_HOLES);
    Polygon geo = Polygon.fromJson(json);
    compareJson(json, geo.toJson());
  }
}
