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

public class MultiPolygonTest extends TestUtils {
  private static final String SAMPLE_MULTIPOLYGON = "sample-multipolygon.json";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(1.0, 2.0));
    LineString outer = LineString.fromLngLats(points);
    List<Polygon> polygons = new ArrayList<>();
    polygons.add(Polygon.fromOuterInner(outer));
    polygons.add(Polygon.fromOuterInner(outer));
    MultiPolygon multiPolygon = MultiPolygon.fromPolygons(polygons);
    assertNotNull(multiPolygon);
  }

  @Test
  public void bbox_nullWhenNotSet() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(1.0, 2.0));
    LineString outer = LineString.fromLngLats(points);
    List<Polygon> polygons = new ArrayList<>();
    polygons.add(Polygon.fromOuterInner(outer));
    polygons.add(Polygon.fromOuterInner(outer));
    MultiPolygon multiPolygon = MultiPolygon.fromPolygons(polygons);
    assertNull(multiPolygon.bbox());
  }

  @Test
  public void bbox_doesNotSerializeWhenNotPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(1.0, 2.0));
    LineString outer = LineString.fromLngLats(points);
    List<Polygon> polygons = new ArrayList<>();
    polygons.add(Polygon.fromOuterInner(outer));
    polygons.add(Polygon.fromOuterInner(outer));
    MultiPolygon multiPolygon = MultiPolygon.fromPolygons(polygons);
    compareJson(multiPolygon.toJson(),
      "{\"type\":\"MultiPolygon\","
        + "\"coordinates\":[[[[1,2],[2,3],[3,4],[1,2]]],[[[1,2],[2,3],[3,4],[1,2]]]]}");
  }

  @Test
  public void bbox_returnsCorrectBbox() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(1.0, 2.0));
    LineString outer = LineString.fromLngLats(points);

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    List<Polygon> polygons = new ArrayList<>();
    polygons.add(Polygon.fromOuterInner(outer));
    polygons.add(Polygon.fromOuterInner(outer));
    MultiPolygon multiPolygon = MultiPolygon.fromPolygons(polygons, bbox);
    assertNotNull(multiPolygon.bbox());
    assertEquals(1.0, multiPolygon.bbox().west(), DELTA);
    assertEquals(2.0, multiPolygon.bbox().south(), DELTA);
    assertEquals(3.0, multiPolygon.bbox().east(), DELTA);
    assertEquals(4.0, multiPolygon.bbox().north(), DELTA);
  }

  @Test
  public void passingInSinglePolygon_doesHandleCorrectly() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    List<List<Point>> pointsList = new ArrayList<>();
    pointsList.add(points);
    Polygon geometry = Polygon.fromLngLats(pointsList);
    MultiPolygon multiPolygon = MultiPolygon.fromPolygon(geometry);
    assertNotNull(multiPolygon);
    assertEquals(1, multiPolygon.polygons().size());
    assertEquals(2.0, multiPolygon.polygons().get(0).coordinates().get(0).get(0).latitude(), DELTA);
  }

  @Test
  public void bbox_doesSerializeWhenPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(1.0, 2.0));
    LineString outer = LineString.fromLngLats(points);

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    List<Polygon> polygons = new ArrayList<>();
    polygons.add(Polygon.fromOuterInner(outer));
    polygons.add(Polygon.fromOuterInner(outer));
    MultiPolygon multiPolygon = MultiPolygon.fromPolygons(polygons, bbox);
    compareJson(multiPolygon.toJson(),
      "{\"type\":\"MultiPolygon\",\"bbox\":[1.0,2.0,3.0,4.0],"
        + "\"coordinates\":[[[[1,2],[2,3],[3,4],[1,2]]],[[[1,2],[2,3],[3,4],[1,2]]]]}");
  }

  @Test
  public void testSerializable() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(1.0, 2.0));
    LineString outer = LineString.fromLngLats(points);

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    List<Polygon> polygons = new ArrayList<>();
    polygons.add(Polygon.fromOuterInner(outer));
    polygons.add(Polygon.fromOuterInner(outer));
    MultiPolygon multiPolygon = MultiPolygon.fromPolygons(polygons, bbox);
    byte[] bytes = serialize(multiPolygon);
    assertEquals(multiPolygon, deserialize(bytes, MultiPolygon.class));
  }

  @Test
  public void fromJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_MULTIPOLYGON);
    MultiPolygon geo = MultiPolygon.fromJson(json);
    assertEquals(geo.type(), "MultiPolygon");
    assertEquals(geo.coordinates().get(0).get(0).get(0).longitude(), 102.0, DELTA);
    assertEquals(geo.coordinates().get(0).get(0).get(0).latitude(), 2.0, DELTA);
    assertFalse(geo.coordinates().get(0).get(0).get(0).hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_MULTIPOLYGON);
    MultiPolygon geo = MultiPolygon.fromJson(json);
    compareJson(json, geo.toJson());
  }

  @Test
  public void fromJson_coordinatesPresent() throws Exception {
    thrown.expect(NullPointerException.class);
    MultiPolygon.fromJson("{\"type\":\"MultiPolygon\",\"coordinates\":null}");
  }
}
