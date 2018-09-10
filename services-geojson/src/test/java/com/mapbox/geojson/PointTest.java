package com.mapbox.geojson;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.mapbox.core.TestUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PointTest extends TestUtils {

  private static final String SAMPLE_POINT = "sample-point.json";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    Point point = Point.fromLngLat(1.0, 2.0);
    assertNotNull(point);
  }

  @Test
  public void hasAltitude_returnsFalseWhenAltitudeNotPresent() throws Exception {
    Point point = Point.fromLngLat(1.0, 2.0);
    assertFalse(point.hasAltitude());
  }

  @Test
  public void hasAltitude_returnsTrueWhenAltitudeIsPresent() throws Exception {
    Point point = Point.fromLngLat(1.0, 2.0, 5.0);
    assertTrue(point.hasAltitude());
  }

  @Test
  public void altitude_doesReturnCorrectValue() throws Exception {
    Point point = Point.fromLngLat(1.0, 2.0, 5.0);
    assertEquals(5, point.altitude(), DELTA);
  }

  @Test
  public void longitude_doesReturnCorrectValue() throws Exception {
    Point point = Point.fromLngLat(1.0, 2.0, 5.0);
    assertEquals(1, point.longitude(), DELTA);
  }

  @Test
  public void latitude_doesReturnCorrectValue() throws Exception {
    Point point = Point.fromLngLat(1.0, 2.0, 5.0);
    assertEquals(2, point.latitude(), DELTA);
  }

  @Test
  public void bbox_nullWhenNotSet() throws Exception {
    Point point = Point.fromLngLat(1.0, 2.0);
    assertNull(point.bbox());
  }

  @Test
  public void bbox_doesSerializeWhenNotPresent() throws Exception {
    Point point = Point.fromLngLat(1.0, 2.0);
    compareJson(point.toJson(),
      "{\"type\":\"Point\",\"coordinates\":[1.0, 2.0]}");
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
    final String json = loadJsonFixture(SAMPLE_POINT);
    Point geo = Point.fromJson(json);
    assertEquals(geo.type(), "Point");
    assertEquals(geo.longitude(), 100.0, DELTA);
    assertEquals(geo.latitude(), 0.0, DELTA);
    assertEquals(geo.altitude(), Double.NaN, DELTA);
    assertEquals(geo.coordinates().get(0), 100.0, DELTA);
    assertEquals(geo.coordinates().get(1), 0.0, DELTA);
    assertEquals(geo.coordinates().size(), 2);
    assertFalse(geo.hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_POINT);
    Point geo = Point.fromJson(json);
    compareJson(json, geo.toJson());
  }

  @Test
  public void fromJson_coordinatesPresent() throws Exception {
    thrown.expect(NullPointerException.class);
    Point.fromJson("{\"type\":\"Point\",\"coordinates\":null}");
  }
}