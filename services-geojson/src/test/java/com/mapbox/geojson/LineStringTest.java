package com.mapbox.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.mapbox.core.TestUtils;
import com.mapbox.core.constants.Constants;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LineStringTest extends TestUtils {

  private static final String SAMPLE_LINESTRING_FIXTURE = "sample-linestring.json";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 1.0));
    points.add(Point.fromLngLat(2.0, 2.0));
    points.add(Point.fromLngLat(3.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    assertNotNull(lineString);
  }

  @Test
  public void fromLngLats_generatedFromMultipoint() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0,2.0));
    points.add(Point.fromLngLat(4.0,8.0));
    MultiPoint multiPoint = MultiPoint.fromLngLats(points);
    LineString lineString = LineString.fromLngLats(multiPoint);
    assertEquals("_gayB_c`|@_wemJ_kbvD", lineString.toPolyline(Constants.PRECISION_6));
  }

  @Test
  public void bbox_nullWhenNotSet() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 1.0));
    points.add(Point.fromLngLat(2.0, 2.0));
    points.add(Point.fromLngLat(3.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    assertNull(lineString.bbox());
  }

  @Test
  public void bbox_doesNotSerializeWhenNotPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 1.0));
    points.add(Point.fromLngLat(2.0, 2.0));
    points.add(Point.fromLngLat(3.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    compareJson(lineString.toJson(),
      "{\"coordinates\":[[1,1],[2,2],[3,3]],\"type\":\"LineString\"}");
  }

  @Test
  public void bbox_returnsCorrectBbox() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 1.0));
    points.add(Point.fromLngLat(2.0, 2.0));
    points.add(Point.fromLngLat(3.0, 3.0));
    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    LineString lineString = LineString.fromLngLats(points, bbox);
    assertNotNull(lineString.bbox());
    assertEquals(1.0, lineString.bbox().west(), DELTA);
    assertEquals(2.0, lineString.bbox().south(), DELTA);
    assertEquals(3.0, lineString.bbox().east(), DELTA);
    assertEquals(4.0, lineString.bbox().north(), DELTA);
  }

  @Test
  public void bbox_doesSerializeWhenPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 1.0));
    points.add(Point.fromLngLat(2.0, 2.0));
    points.add(Point.fromLngLat(3.0, 3.0));
    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    LineString lineString = LineString.fromLngLats(points, bbox);
    compareJson(lineString.toJson(),
      "{\"coordinates\":[[1,1],[2,2],[3,3]],"
        + "\"type\":\"LineString\",\"bbox\":[1.0,2.0,3.0,4.0]}");
  }

  @Test
  public void testSerializable() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 1.0));
    points.add(Point.fromLngLat(2.0, 2.0));
    points.add(Point.fromLngLat(3.0, 3.0));
    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    LineString lineString = LineString.fromLngLats(points, bbox);
    byte[] bytes = serialize(lineString);
    assertEquals(lineString, deserialize(bytes, LineString.class));
  }

  @Test
  public void fromJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_LINESTRING_FIXTURE);
    LineString geo = LineString.fromJson(json);
    assertEquals(geo.type(), "LineString");
    assertEquals(geo.coordinates().get(0).longitude(), 100.0, 0.0);
    assertEquals(geo.coordinates().get(0).latitude(), 0.0, 0.0);
    assertFalse(geo.coordinates().get(0).hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_LINESTRING_FIXTURE);
    LineString geo = LineString.fromJson(json);
    compareJson(json, geo.toJson());
  }

  @Test
  public void fromJson_coordinatesPresent() throws Exception {
    thrown.expect(NullPointerException.class);
    LineString.fromJson("{\"type\":\"LineString\",\"coordinates\":null}");
  }
}