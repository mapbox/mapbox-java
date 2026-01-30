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
  public void fromFlattenListOfPoints() throws Exception {
    double[] flattenLngLatPoints= new double[]{1.0, 2.0, 4.0, 8.0};
    LineString lineString = LineString.fromFlattenArrayOfPoints(flattenLngLatPoints, null);
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

    double[] coordinatesPrimitive = lineString.flattenCoordinates().getFlattenLngLatArray();
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

    double[] coordinates = geo.flattenCoordinates().getFlattenLngLatArray();
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
  public void fromJsonWithExtraValuesAreIgnored() throws IOException {
    final String json = "{\"type\": \"LineString\"," +
            "  \"coordinates\": [[ 100, 0, 1000, 2, 3], [101, 1]]} ";
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

    double[] coordinates = geo.flattenCoordinates().getFlattenLngLatArray();
    assertEquals(4, geo.flattenCoordinates().getFlattenLngLatArray().length);
    double[] altitudes = geo.flattenCoordinates().getAltitudes();
    assertEquals(100.0, coordinates[0], 0.0);
    assertEquals(0.0, coordinates[1], 0.0);
    assertNotNull(altitudes);
    assertEquals(1000.0, altitudes[0], 0.0);

    // Second point
    assertEquals(101.0, coordinates[2], 0.0);
    assertEquals(1.0, coordinates[3], 0.0);
    assertEquals(Double.NaN, altitudes[1], 0.0);
  }

  /**
   * Test to trigger reading a JSON that needs to extend the array capacity while parsing.
   * {@link FlattenListOfPointsTypeAdapter#INITIAL_CAPACITY}.
   *
   * @throws IOException
   */
  @SuppressWarnings("JavadocReference")
  @Test
  public void readJsonWithMoreThan_INITIAL_CAPACITY() throws IOException {
    final StringBuilder json = new StringBuilder("{\"type\": \"LineString\"," +
            "  \"coordinates\": [");
    int totalPoints = 100 * 2;
    for (int i = 0; i < totalPoints; i++) {
      json.append("[ ");
      double lng = 100 + i;
      double lat = i;
      json.append(lng).append(",");
      json.append(lat);
      // Only add altitude for half of the points
      if (i >= 100) {
        double alt = 1000 + i;
        json.append(",").append(alt);
      }
      json.append("], ");
    }
    // Trim the last `, `
    json.deleteCharAt(json.length() - 2);
    json.append("]}");

    LineString geo = LineString.fromJson(json.toString());
    assertEquals("LineString", geo.type());
    List<Point> points = geo.coordinates();
    assertEquals(totalPoints, points.size());

    // Verify the list of points contents
    for (int i = 0; i < totalPoints; i++) {
      Point point = points.get(i);
      double expectedLng = 100 + i;
      double expectedLat = i;
      assertEquals(expectedLng, point.longitude(), DELTA);
      assertEquals(expectedLat, point.latitude(), DELTA);

      if (i >= 100) {
        // Second half should have altitude
        assertTrue(point.hasAltitude());
        double expectedAlt = 1000 + i;
        assertEquals(expectedAlt, point.altitude(), DELTA);
      } else {
        // First half should not have altitude
        assertFalse(point.hasAltitude());
      }
    }

    FlattenListOfPoints flattenListOfPoints = geo.flattenCoordinates();
    assertEquals(totalPoints, flattenListOfPoints.size());
    double[] flattenLngLatArray = flattenListOfPoints.getFlattenLngLatArray();
    assertEquals(totalPoints * 2, flattenLngLatArray.length);
    double[] altitudes = flattenListOfPoints.getAltitudes();
    assertNotNull(altitudes);
    assertEquals(totalPoints, altitudes.length);

    // Verify the contents of flattenLngLatArray and altitudes
    for (int i = 0; i < totalPoints; i++) {
      double expectedLng = 100 + i;
      double expectedLat = i;
      assertEquals(expectedLng, flattenLngLatArray[i * 2], DELTA);
      assertEquals(expectedLat, flattenLngLatArray[i * 2 + 1], DELTA);

      if (i >= 100) {
        // Second half should have altitude
        double expectedAlt = 1000 + i;
        assertEquals(expectedAlt, altitudes[i], DELTA);
      } else {
        // First half should have NaN for altitude
        assertTrue(Double.isNaN(altitudes[i]));
      }
    }
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
  public void toJsonEmpty() throws IOException {
    final String json = "{\"type\": \"LineString\",  \"coordinates\": [ ]} ";
    LineString geo = LineString.fromLngLats(new ArrayList<>());
    String geoJsonString = geo.toJson();
    compareJson(geoJsonString, json);
  }

  @Test
  public void fromEmptyJson() throws IOException {
    final String json = "{\"type\": \"LineString\",  \"coordinates\": [ ]} ";
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