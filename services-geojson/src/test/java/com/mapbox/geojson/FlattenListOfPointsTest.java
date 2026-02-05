package com.mapbox.geojson;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlattenListOfPointsTest extends TestUtils {

  @Test
  public void constructor_withArrays_storesDataCorrectly() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0};
    double[] altitudes = new double[]{5.0, 6.0};

    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, altitudes);

    assertNotNull(flatten);
    assertArrayEquals(lngLatArray, flatten.getFlattenLngLatArray(), DELTA);
    assertArrayEquals(altitudes, flatten.getAltitudes(), DELTA);
  }

  @Test
  public void constructor_withArrays_nullAltitudes() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0};

    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, null);

    assertNotNull(flatten);
    assertArrayEquals(lngLatArray, flatten.getFlattenLngLatArray(), DELTA);
    assertNull(flatten.getAltitudes());
  }

  @Test
  public void size_emptyFlatten_returnsZero() {
    double[] lngLatArray = new double[]{};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, null);

    assertEquals(0, flatten.size());
  }

  @Test
  public void size_singlePoint_returnsOne() {
    double[] lngLatArray = new double[]{1.0, 2.0};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, null);

    assertEquals(1, flatten.size());
  }

  @Test
  public void size_multiplePoints_returnsCorrectCount() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, null);

    assertEquals(3, flatten.size());
  }

  @Test
  public void size_fromListOfPoints_returnsCorrectCount() {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(5.0, 6.0));
    points.add(Point.fromLngLat(7.0, 8.0));

    FlattenListOfPoints flatten = new FlattenListOfPoints(points);

    assertEquals(4, flatten.size());
  }

  @Test
  public void constructor_withEmptyList_createsEmptyFlatten() {
    List<Point> points = new ArrayList<>();

    FlattenListOfPoints flatten = new FlattenListOfPoints(points);

    assertNotNull(flatten);
    assertEquals(0, flatten.getFlattenLngLatArray().length);
    assertNull(flatten.getAltitudes());
  }

  @Test
  public void constructor_withPointsNoAltitude_storesLngLatOnly() {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(5.0, 6.0));

    FlattenListOfPoints flatten = new FlattenListOfPoints(points);

    assertNotNull(flatten);
    double[] expected = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
    assertArrayEquals(expected, flatten.getFlattenLngLatArray(), DELTA);
    assertNull(flatten.getAltitudes());
  }

  @Test
  public void constructor_withPointsWithAltitude_storesAllData() {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0, 10.0));
    points.add(Point.fromLngLat(3.0, 4.0, 20.0));
    points.add(Point.fromLngLat(5.0, 6.0, 30.0));

    FlattenListOfPoints flatten = new FlattenListOfPoints(points);

    assertNotNull(flatten);
    double[] expectedLngLat = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
    double[] expectedAlt = new double[]{10.0, 20.0, 30.0};
    assertArrayEquals(expectedLngLat, flatten.getFlattenLngLatArray(), DELTA);
    assertArrayEquals(expectedAlt, flatten.getAltitudes(), DELTA);
  }

  @Test
  public void constructor_withMixedAltitudes_storesNaNForMissingAltitudes() {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));           // no altitude
    points.add(Point.fromLngLat(3.0, 4.0, 20.0));     // with altitude
    points.add(Point.fromLngLat(5.0, 6.0));           // no altitude
    points.add(Point.fromLngLat(7.0, 8.0, 40.0));     // with altitude

    FlattenListOfPoints flatten = new FlattenListOfPoints(points);

    assertNotNull(flatten);
    double[] expectedLngLat = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
    assertArrayEquals(expectedLngLat, flatten.getFlattenLngLatArray(), DELTA);

    double[] altitudes = flatten.getAltitudes();
    assertNotNull(altitudes);
    assertEquals(4, altitudes.length);
    assertTrue(Double.isNaN(altitudes[0]));
    assertEquals(20.0, altitudes[1], DELTA);
    assertTrue(Double.isNaN(altitudes[2]));
    assertEquals(40.0, altitudes[3], DELTA);
  }

  @Test
  public void constructor_withSinglePoint_worksCorrectly() {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0, 10.0));

    FlattenListOfPoints flatten = new FlattenListOfPoints(points);

    assertNotNull(flatten);
    double[] expectedLngLat = new double[]{1.0, 2.0};
    double[] expectedAlt = new double[]{10.0};
    assertArrayEquals(expectedLngLat, flatten.getFlattenLngLatArray(), DELTA);
    assertArrayEquals(expectedAlt, flatten.getAltitudes(), DELTA);
  }

  @Test
  public void points_returnsEmptyListForEmptyFlatten() {
    double[] lngLatArray = new double[]{};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, null);

    List<Point> points = flatten.points();

    assertNotNull(points);
    assertEquals(0, points.size());
  }

  @Test
  public void points_reconstructsPointsWithoutAltitude() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, null);

    List<Point> points = flatten.points();

    assertNotNull(points);
    assertEquals(2, points.size());
    assertEquals(1.0, points.get(0).longitude(), DELTA);
    assertEquals(2.0, points.get(0).latitude(), DELTA);
    assertFalse(points.get(0).hasAltitude());
    assertEquals(3.0, points.get(1).longitude(), DELTA);
    assertEquals(4.0, points.get(1).latitude(), DELTA);
    assertFalse(points.get(1).hasAltitude());
  }

  @Test
  public void points_reconstructsPointsWithAltitude() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0};
    double[] altitudes = new double[]{10.0, 20.0};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, altitudes);

    List<Point> points = flatten.points();

    assertNotNull(points);
    assertEquals(2, points.size());
    assertEquals(1.0, points.get(0).longitude(), DELTA);
    assertEquals(2.0, points.get(0).latitude(), DELTA);
    assertEquals(10.0, points.get(0).altitude(), DELTA);
    assertTrue(points.get(0).hasAltitude());
    assertEquals(3.0, points.get(1).longitude(), DELTA);
    assertEquals(4.0, points.get(1).latitude(), DELTA);
    assertEquals(20.0, points.get(1).altitude(), DELTA);
    assertTrue(points.get(1).hasAltitude());
  }

  @Test
  public void points_reconstructsPointsWithMixedAltitudes() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
    double[] altitudes = new double[]{Double.NaN, 20.0, 30.0};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, altitudes);

    List<Point> points = flatten.points();

    assertNotNull(points);
    assertEquals(3, points.size());
    assertFalse(points.get(0).hasAltitude());
    assertTrue(points.get(1).hasAltitude());
    assertEquals(20.0, points.get(1).altitude(), DELTA);
    assertTrue(points.get(2).hasAltitude());
    assertEquals(30.0, points.get(2).altitude(), DELTA);
  }

  @Test
  public void points_roundTrip_preservesData() {
    List<Point> originalPoints = new ArrayList<>();
    originalPoints.add(Point.fromLngLat(1.0, 2.0, 10.0));
    originalPoints.add(Point.fromLngLat(3.0, 4.0));
    originalPoints.add(Point.fromLngLat(5.0, 6.0, 30.0));

    FlattenListOfPoints flatten = new FlattenListOfPoints(originalPoints);
    List<Point> reconstructedPoints = flatten.points();

    assertEquals(originalPoints.size(), reconstructedPoints.size());
    for (int i = 0; i < originalPoints.size(); i++) {
      Point original = originalPoints.get(i);
      Point reconstructed = reconstructedPoints.get(i);
      assertEquals(original.longitude(), reconstructed.longitude(), DELTA);
      assertEquals(original.latitude(), reconstructed.latitude(), DELTA);
      assertEquals(original.hasAltitude(), reconstructed.hasAltitude());
      if (original.hasAltitude()) {
        assertEquals(original.altitude(), reconstructed.altitude(), DELTA);
      }
    }
  }

  @Test
  public void equals_sameFlatten_returnsTrue() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0};
    double[] altitudes = new double[]{10.0, 20.0};
    FlattenListOfPoints flatten1 = new FlattenListOfPoints(lngLatArray, altitudes);
    FlattenListOfPoints flatten2 = new FlattenListOfPoints(lngLatArray, altitudes);

    assertTrue(flatten1.equals(flatten2));
    assertTrue(flatten2.equals(flatten1));
  }

  @Test
  public void equals_differentLngLat_returnsFalse() {
    double[] lngLatArray1 = new double[]{1.0, 2.0, 3.0, 4.0};
    double[] lngLatArray2 = new double[]{1.0, 2.0, 5.0, 6.0};
    double[] altitudes = new double[]{10.0, 20.0};
    FlattenListOfPoints flatten1 = new FlattenListOfPoints(lngLatArray1, altitudes);
    FlattenListOfPoints flatten2 = new FlattenListOfPoints(lngLatArray2, altitudes);

    assertFalse(flatten1.equals(flatten2));
  }

  @Test
  public void equals_differentAltitudes_returnsFalse() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0};
    double[] altitudes1 = new double[]{10.0, 20.0};
    double[] altitudes2 = new double[]{10.0, 30.0};
    FlattenListOfPoints flatten1 = new FlattenListOfPoints(lngLatArray, altitudes1);
    FlattenListOfPoints flatten2 = new FlattenListOfPoints(lngLatArray, altitudes2);

    assertFalse(flatten1.equals(flatten2));
  }

  @Test
  public void equals_oneWithAltitudeOneWithout_returnsFalse() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0};
    double[] altitudes = new double[]{10.0, 20.0};
    FlattenListOfPoints flatten1 = new FlattenListOfPoints(lngLatArray, altitudes);
    FlattenListOfPoints flatten2 = new FlattenListOfPoints(lngLatArray, null);

    assertFalse(flatten1.equals(flatten2));
  }

  @Test
  public void equals_withNull_returnsFalse() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, null);

    assertFalse(flatten.equals(null));
  }

  @Test
  public void equals_withDifferentType_returnsFalse() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, null);

    assertFalse(flatten.equals("not a FlattenListOfPoints"));
  }

  @Test
  public void hashCode_sameData_returnsSameHashCode() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0};
    double[] altitudes = new double[]{10.0, 20.0};
    FlattenListOfPoints flatten1 = new FlattenListOfPoints(lngLatArray, altitudes);
    FlattenListOfPoints flatten2 = new FlattenListOfPoints(lngLatArray, altitudes);

    assertEquals(flatten1.hashCode(), flatten2.hashCode());
  }

  @Test
  public void hashCode_differentData_returnsDifferentHashCode() {
    double[] lngLatArray1 = new double[]{1.0, 2.0, 3.0, 4.0};
    double[] lngLatArray2 = new double[]{1.0, 2.0, 5.0, 6.0};
    double[] altitudes = new double[]{10.0, 20.0};
    FlattenListOfPoints flatten1 = new FlattenListOfPoints(lngLatArray1, altitudes);
    FlattenListOfPoints flatten2 = new FlattenListOfPoints(lngLatArray2, altitudes);

    assertNotEquals(flatten1.hashCode(), flatten2.hashCode());
  }

  @Test
  public void toString_emptyFlatten_returnsEmptyBrackets() {
    double[] lngLatArray = new double[]{};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, null);

    String result = flatten.toString();

    assertEquals("[]", result);
  }

  @Test
  public void toString_singlePointWithoutAltitude_returnsCorrectFormat() {
    double[] lngLatArray = new double[]{1.0, 2.0};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, null);

    String result = flatten.toString();

    assertEquals("[Point{type=Point, bbox=null, coordinates=[1.0, 2.0]}]", result);
  }

  @Test
  public void toString_singlePointWithAltitude_returnsCorrectFormat() {
    double[] lngLatArray = new double[]{1.0, 2.0};
    double[] altitudes = new double[]{10.0};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, altitudes);

    String result = flatten.toString();

    assertEquals("[Point{type=Point, bbox=null, coordinates=[1.0, 2.0, 10.0]}]", result);
  }

  @Test
  public void toString_multiplePointsWithoutAltitude_returnsCorrectFormat() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, null);

    String result = flatten.toString();

    String expected = "[Point{type=Point, bbox=null, coordinates=[1.0, 2.0]}, "
        + "Point{type=Point, bbox=null, coordinates=[3.0, 4.0]}]";
    assertEquals(expected, result);
  }

  @Test
  public void toString_multiplePointsWithAltitude_returnsCorrectFormat() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0};
    double[] altitudes = new double[]{10.0, 20.0};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, altitudes);

    String result = flatten.toString();

    String expected = "[Point{type=Point, bbox=null, coordinates=[1.0, 2.0, 10.0]}, "
        + "Point{type=Point, bbox=null, coordinates=[3.0, 4.0, 20.0]}]";
    assertEquals(expected, result);
  }

  @Test
  public void toString_mixedAltitudes_returnsCorrectFormat() {
    double[] lngLatArray = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
    double[] altitudes = new double[]{10.0, Double.NaN, 30.0};
    FlattenListOfPoints flatten = new FlattenListOfPoints(lngLatArray, altitudes);

    String result = flatten.toString();

    String expected = "[Point{type=Point, bbox=null, coordinates=[1.0, 2.0, 10.0]}, "
        + "Point{type=Point, bbox=null, coordinates=[3.0, 4.0]}, "
        + "Point{type=Point, bbox=null, coordinates=[5.0, 6.0, 30.0]}]";
    assertEquals(expected, result);
  }

  @Test
  public void testSerializable() throws IOException, ClassNotFoundException {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0, 10.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    points.add(Point.fromLngLat(5.0, 6.0, 30.0));

    FlattenListOfPoints flatten = new FlattenListOfPoints(points);

    byte[] bytes = serialize(flatten);
    FlattenListOfPoints deserialized = deserialize(bytes, FlattenListOfPoints.class);

    assertEquals(flatten, deserialized);
  }

  @Test
  public void constructor_withPointsWithBoundingBox_storesBoundingBoxes() {
    List<Point> points = new ArrayList<>();
    BoundingBox bbox1 = BoundingBox.fromLngLats(0.0, 0.0, 1.0, 1.0);
    BoundingBox bbox2 = BoundingBox.fromLngLats(2.0, 2.0, 3.0, 3.0);
    points.add(Point.fromLngLat(1.0, 2.0, bbox1));
    points.add(Point.fromLngLat(3.0, 4.0, bbox2));

    FlattenListOfPoints flatten = new FlattenListOfPoints(points);
    List<Point> reconstructedPoints = flatten.points();

    assertNotNull(reconstructedPoints.get(0).bbox());
    assertNotNull(reconstructedPoints.get(1).bbox());
    assertEquals(bbox1, reconstructedPoints.get(0).bbox());
    assertEquals(bbox2, reconstructedPoints.get(1).bbox());
  }

  @Test
  public void constructor_withMixedBoundingBoxes_handlesCorrectly() {
    List<Point> points = new ArrayList<>();
    BoundingBox bbox = BoundingBox.fromLngLats(0.0, 0.0, 1.0, 1.0);
    points.add(Point.fromLngLat(1.0, 2.0, bbox));
    points.add(Point.fromLngLat(3.0, 4.0));        // no bbox

    FlattenListOfPoints flatten = new FlattenListOfPoints(points);
    List<Point> reconstructedPoints = flatten.points();

    assertNotNull(reconstructedPoints.get(0).bbox());
    assertNull(reconstructedPoints.get(1).bbox());
    assertEquals(bbox, reconstructedPoints.get(0).bbox());
  }

  @Test
  public void toString_withBoundingBox_includesBoundingBoxInOutput() {
    List<Point> points = new ArrayList<>();
    BoundingBox bbox = BoundingBox.fromLngLats(0.0, 0.0, 1.0, 1.0);
    points.add(Point.fromLngLat(1.0, 2.0, bbox));

    FlattenListOfPoints flatten = new FlattenListOfPoints(points);
    String result = flatten.toString();

    assertTrue(result.contains("BoundingBox"));
    assertTrue(result.contains("1.0"));
    assertTrue(result.contains("2.0"));
  }
}
