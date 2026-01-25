package com.mapbox.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LineStringTest extends TestUtils {

  private static final String SAMPLE_LINESTRING_FIXTURE = "sample-linestring.json";

  private static final int PRECISION_6 = 6;
  private static final int PRECISION_5 = 5;

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
    assertEquals("_gayB_c`|@_wemJ_kbvD", lineString.toPolyline(PRECISION_6));
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
    String lineStringJson = lineString.toJson();
    compareJson("{\"coordinates\":[[1,1],[2,2],[3,3]],"
        + "\"type\":\"LineString\",\"bbox\":[1.0,2.0,3.0,4.0]}",
            lineStringJson);
  }

  @Test
  public void bbox_doesDeserializeWhenPresent() throws Exception {
    LineString lineString = LineString.fromJson("{\"coordinates\":[[1,2],[2,3],[3,4]],"
            + "\"type\":\"LineString\",\"bbox\":[1.0,2.0,3.0,4.0]}");

    assertNotNull(lineString);
    assertNotNull(lineString.bbox());
    assertEquals(1.0, lineString.bbox().southwest().longitude(), DELTA);
    assertEquals(2.0, lineString.bbox().southwest().latitude(), DELTA);
    assertEquals(3.0, lineString.bbox().northeast().longitude(), DELTA);
    assertEquals(4.0, lineString.bbox().northeast().latitude(), DELTA);
    List<Point> coordinates = lineString.coordinates();
    assertNotNull(coordinates);
    assertEquals(1, coordinates.get(0).longitude(), DELTA);
    assertEquals(2, coordinates.get(0).latitude(), DELTA);
    assertEquals(2, coordinates.get(1).longitude(), DELTA);
    assertEquals(3, coordinates.get(1).latitude(), DELTA);
    assertEquals(3, coordinates.get(2).longitude(), DELTA);
    assertEquals(4, coordinates.get(2).latitude(), DELTA);

    double[] coordinatesPrimitive = lineString.flattenCoordinates().getFlattenLatLngArray();
    assertEquals(1, coordinatesPrimitive[0], DELTA);
    assertEquals(2, coordinatesPrimitive[1], DELTA);
    assertEquals(2, coordinatesPrimitive[2], DELTA);
    assertEquals(3, coordinatesPrimitive[3], DELTA);
    assertEquals(3, coordinatesPrimitive[4], DELTA);
    assertEquals(4, coordinatesPrimitive[5], DELTA);
  }

  @Test
  public void testSerializable() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 1.0, 1.0));
    points.add(Point.fromLngLat(2.0, 2.0));
    points.add(Point.fromLngLat(3.0, 3.0));
    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    LineString lineString = LineString.fromLngLats(points, bbox);
    byte[] bytes = serialize(lineString);
    LineString deserialize = deserialize(bytes, LineString.class);
    assertEquals(lineString, deserialize);
  }

  @Test
  public void fromJson() throws IOException {
    final String json = "{\"type\": \"LineString\"," +
            "  \"coordinates\": [[ 100, 0, 1000], [101, 1]]} ";
    LineString geo = LineString.fromJson(json);
    assertEquals("LineString", geo.type());
    List<Point> points = geo.coordinates();
    Point firstPoint = points.get(0);
    assertEquals(100.0, firstPoint.longitude(), 0.0);
    assertEquals(0.0, firstPoint.latitude(), 0.0);
    assertTrue(firstPoint.hasAltitude());
    assertEquals(1000.0, firstPoint.altitude(), 0.0);

    Point secondPoint = points.get(1);
    assertEquals(101.0, secondPoint.longitude(), 0.0);
    assertEquals(1.0, secondPoint.latitude(), 0.0);
    assertFalse(secondPoint.hasAltitude());

    double[] coordinates = geo.flattenCoordinates().getFlattenLatLngArray();
    double[] altitudes = geo.flattenCoordinates().getAltitudes();
    assertEquals(100.0, coordinates[0], 0.0);
    assertEquals(0.0, coordinates[1], 0.0);
    assertNotNull(altitudes);
    assertEquals(1000.0, altitudes[0], 0.0);
    assertEquals(101.0, coordinates[2], 0.0);
    assertEquals(1.0, coordinates[3], 0.0);
    assertEquals(Double.NaN, altitudes[1], 0.0);
  }

  @Test
  public void toJson() throws IOException {
    final String json = "{\"type\": \"LineString\"," +
            "  \"coordinates\": [[ 100, 0, 1], [101, 1]]} ";
    LineString geo = LineString.fromJson(json);
    String geoJsonString = geo.toJson();
    compareJson(geoJsonString, json);
  }

  @Test
  public void fromJson_coordinatesPresent() throws Exception {
    thrown.expect(NullPointerException.class);
    LineString.fromJson("{\"type\":\"LineString\",\"coordinates\":null}");
  }
}