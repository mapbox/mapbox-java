package com.mapbox.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.mapbox.core.TestUtils;
import com.mapbox.geojson.exception.GeoJsonException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PolygonTest extends TestUtils {

  private static final String SAMPLE_POLYGON = "sample-polygon.json";
  private static final String SAMPLE_POLYGON_HOLES = "sample-polygon-holes.json";

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
    Polygon polygon = Polygon.fromOuterInner(outer);
    assertNotNull(polygon);
  }

  @Test
  public void fromLngLats_tripleDoubleArray() throws Exception {
    double[][][] coordinates = new double[][][] {
      {{100.0, 0.0},
        {101.0, 0.0},
        {101.0, 1.0},
        {100.0, 1.0},
        {100.0, 0.0}}
    };
    Polygon polygon = Polygon.fromLngLats(coordinates);
    assertEquals(0, polygon.inner().size());
    assertEquals(Point.fromLngLat(100.0, 0.0), polygon.coordinates().get(0).get(0));
  }

  @Test
  public void fromOuterInner_throwsNotLinearRingException() throws Exception {
    thrown.expect(GeoJsonException.class);
    thrown.expectMessage("LinearRings need to be made up of 4 or more coordinates.");
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(10.0, 2.0));
    points.add(Point.fromLngLat(5.0, 2.0));
    points.add(Point.fromLngLat(3.0, 2.0));
    LineString lineString = LineString.fromLngLats(points);
    Polygon polygon = Polygon.fromOuterInner(lineString);
  }

  @Test
  public void fromOuterInner_throwsNotConnectedLinearRingException() throws Exception {
    thrown.expect(GeoJsonException.class);
    thrown.expectMessage("LinearRings require first and last coordinate to be identical.");
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(10.0, 2.0));
    points.add(Point.fromLngLat(5.0, 2.0));
    points.add(Point.fromLngLat(3.0, 2.0));
    points.add(Point.fromLngLat(5.0, 2.0));
    LineString lineString = LineString.fromLngLats(points);
    Polygon polygon = Polygon.fromOuterInner(lineString);
  }

  @Test
  public void fromOuterInner_handlesSingleLineStringCorrectly() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(10.0, 2.0));
    points.add(Point.fromLngLat(5.0, 2.0));
    points.add(Point.fromLngLat(3.0, 2.0));
    points.add(Point.fromLngLat(10.0, 2.0));
    LineString lineString = LineString.fromLngLats(points);

    Polygon polygon = Polygon.fromOuterInner(lineString);
    assertEquals(Point.fromLngLat(10.0, 2.0), polygon.coordinates().get(0).get(0));
  }

  @Test
  public void fromOuterInner_handlesOuterAndInnerLineStringCorrectly() throws Exception {
    List<Point> outer = new ArrayList<>();
    outer.add(Point.fromLngLat(10.0, 2.0));
    outer.add(Point.fromLngLat(5.0, 2.0));
    outer.add(Point.fromLngLat(3.0, 2.0));
    outer.add(Point.fromLngLat(10.0, 2.0));
    LineString outerLineString = LineString.fromLngLats(outer);

    List<Point> inner = new ArrayList<>();
    inner.add(Point.fromLngLat(5.0, 2.0));
    inner.add(Point.fromLngLat(2.5, 2.0));
    inner.add(Point.fromLngLat(1.5, 2.0));
    inner.add(Point.fromLngLat(5.0, 2.0));
    LineString innerLineString = LineString.fromLngLats(inner);

    Polygon polygon = Polygon.fromOuterInner(outerLineString, innerLineString);
    assertEquals(Point.fromLngLat(10.0, 2.0), polygon.coordinates().get(0).get(0));
    assertEquals(outerLineString, polygon.outer());
    assertEquals(1, polygon.inner().size());
    assertEquals(innerLineString, polygon.inner().get(0));
  }

  @Test
  public void fromOuterInner_withABoundingBox() throws Exception {
    List<Point> outer = new ArrayList<>();
    outer.add(Point.fromLngLat(10.0, 2.0));
    outer.add(Point.fromLngLat(5.0, 2.0));
    outer.add(Point.fromLngLat(3.0, 2.0));
    outer.add(Point.fromLngLat(10.0, 2.0));
    LineString outerLineString = LineString.fromLngLats(outer);

    List<Point> inner = new ArrayList<>();
    inner.add(Point.fromLngLat(5.0, 2.0));
    inner.add(Point.fromLngLat(2.5, 2.0));
    inner.add(Point.fromLngLat(1.5, 2.0));
    inner.add(Point.fromLngLat(5.0, 2.0));
    LineString innerLineString = LineString.fromLngLats(inner);

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    Polygon polygon = Polygon.fromOuterInner(outerLineString, bbox, innerLineString);

    assertEquals(bbox, polygon.bbox());
    assertEquals(outerLineString, polygon.outer());
    assertEquals(1, polygon.inner().size());
    assertEquals(innerLineString, polygon.inner().get(0));
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

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    List<LineString> inner = new ArrayList<>();
    inner.add(LineString.fromLngLats(points));
    inner.add(LineString.fromLngLats(points));
    Polygon polygon = Polygon.fromOuterInner(outer, bbox, inner);
    assertNotNull(polygon.bbox());
    assertEquals(1.0, polygon.bbox().west(), DELTA);
    assertEquals(2.0, polygon.bbox().south(), DELTA);
    assertEquals(3.0, polygon.bbox().east(), DELTA);
    assertEquals(4.0, polygon.bbox().north(), DELTA);
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

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    List<LineString> inner = new ArrayList<>();
    inner.add(LineString.fromLngLats(points));
    inner.add(LineString.fromLngLats(points));
    Polygon polygon = Polygon.fromOuterInner(outer, bbox, inner);
    byte[] bytes = serialize(polygon);
    assertEquals(polygon, deserialize(bytes, Polygon.class));
  }

  @Test
  public void fromJson() throws IOException {
    final String json = "{\"type\": \"Polygon\", " +
            "\"coordinates\": [[[100, 0], [101, 0], [101, 1], [100, 1],[100, 0]]]}";
    Polygon geo = Polygon.fromJson(json);
    assertEquals("Polygon", geo.type());
    assertEquals(100.0, geo.coordinates().get(0).get(0).longitude(), DELTA);
    assertEquals(0.0, geo.coordinates().get(0).get(0).latitude(), DELTA);
    assertFalse(geo.coordinates().get(0).get(0).hasAltitude());
  }

  @Test
  public void fromJsonHoles() throws IOException {
    final String json = "{\"type\": \"Polygon\", " +
            "\"coordinates\": [[[100, 0], [101, 0], [101, 1], [100, 1],[100, 0]], " +
            " [[100.8, 0.8],[100.8, 0.2],[100.2, 0.2],[100.2, 0.8],[100.8, 0.8]]]}";
    Polygon geo = Polygon.fromJson(json);
    assertEquals("Polygon", geo.type());
    assertEquals(100.0, geo.coordinates().get(0).get(0).longitude(), DELTA);
    assertEquals(0.0, geo.coordinates().get(0).get(0).latitude(), DELTA);
    assertEquals(2, geo.coordinates().size());
    assertEquals(100.8, geo.coordinates().get(1).get(0).longitude(), DELTA);
    assertEquals(0.8, geo.coordinates().get(1).get(0).latitude(), DELTA);
    assertFalse(geo.coordinates().get(0).get(0).hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    final String json = "{\"type\": \"Polygon\", " +
            "\"coordinates\": [[[100, 0], [101, 0], [101, 1], [100, 1],[100, 0]]]}";
    Polygon geo = Polygon.fromJson(json);
    compareJson(json, geo.toJson());
  }

  @Test
  public void toJsonHoles() throws IOException {
    final String json = "{\"type\": \"Polygon\", " +
            "\"coordinates\": [[[100, 0], [101, 0], [101, 1], [100, 1],[100, 0]], " +
            " [[100.8, 0.8],[100.8, 0.2],[100.2, 0.2],[100.2, 0.8],[100.8, 0.8]]]}";
    Polygon geo = Polygon.fromJson(json);
    compareJson(json, geo.toJson());
  }

  @Test
  public void fromJson_coordinatesPresent() throws Exception {
    thrown.expect(NullPointerException.class);
    Polygon.fromJson("{\"type\":\"Polygon\",\"coordinates\":null}");
  }
}
