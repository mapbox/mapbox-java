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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
  public void altitude_doesReturnCorrectValueFromDoubleArray() throws Exception {
    double[] coords = new double[] {1.0, 2.0, 5.0};
    Point point = Point.fromLngLat(coords);
    assertEquals(5, point.altitude(), DELTA);
  }

  @Test
  public void point_isNullWithWrongLengthDoubleArray() throws Exception {
    double[] coords = new double[] {1.0};
    Point point = Point.fromLngLat(coords);
    assertNull(point);
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
    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    Point point = Point.fromLngLat(2.0, 2.0, bbox);
    compareJson(point.toJson(),
      "{\"coordinates\": [2,2],"
        + "\"type\":\"Point\",\"bbox\":[1.0,2.0,3.0,4.0]}");
  }

  @Test
  public void bbox_doesDeserializeWhenPresent() throws Exception {
    Point point = Point.fromJson("{\"coordinates\": [2,3],"
            + "\"type\":\"Point\",\"bbox\":[1.0,2.0,3.0,4.0]}");

    assertNotNull(point);
    assertNotNull(point.bbox());
    assertEquals(1.0, point.bbox().southwest().longitude(), DELTA);
    assertEquals(2.0, point.bbox().southwest().latitude(), DELTA);
    assertEquals(3.0, point.bbox().northeast().longitude(), DELTA);
    assertEquals(4.0, point.bbox().northeast().latitude(), DELTA);
    assertNotNull(point.coordinates());
    assertEquals(2, point.longitude(), DELTA);
    assertEquals(3, point.latitude(), DELTA);
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
    final String json =
            "{ \"type\": \"Point\", \"coordinates\": [ 100, 0] }";
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
    final String json =
      "{ \"type\": \"Point\", \"coordinates\": [ 100, 0] }";
    Point geo = Point.fromJson(json);
    compareJson(json, geo.toJson());
  }

  @Test
  public void fromJson_coordinatesPresent() throws Exception {
    thrown.expect(NullPointerException.class);
    Point.fromJson("{\"type\":\"Point\",\"coordinates\":null}");
  }
}