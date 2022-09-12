package com.mapbox.geojson.utils;

import static com.mapbox.geojson.utils.PolylineUtils.decode;
import static com.mapbox.geojson.utils.PolylineUtils.encode;
import static com.mapbox.geojson.utils.PolylineUtils.simplify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.TestUtils;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolylineUtilsTest extends TestUtils {

  private static final int PRECISION_6 = 6;
  private static final int PRECISION_5 = 5;

  // Delta for Coordinates comparison
  private static final double DELTA = 0.000001;
  
  private static final String SIMPLIFICATION_INPUT = "simplification-input";
  private static final String SIMPLIFICATION_EXPECTED_OUTPUT = "simplification-expected-output";

  private static final String TEST_LINE
    = "_cqeFf~cjVf@p@fA}AtAoB`ArAx@hA`GbIvDiFv@gAh@t@X\\|@z@`@Z\\Xf@Vf@VpA\\tATJ@NBBkC";

  private static final  String TEST_LINE6 =
    "qn_iHgp}LzCy@xCsAsC}PoEeD_@{A@uD_@Sg@Je@a@I_@FcAoFyGcCqFgQ{L{CmD";

  @Test
  public void testDecodePath() {
    List<Point> latLngs = decode(TEST_LINE, PRECISION_5);

    int expectedLength = 21;
    assertEquals("Wrong length.", expectedLength, latLngs.size());

    Point lastPoint = latLngs.get(expectedLength - 1);
    expectNearNumber(37.76953, lastPoint.latitude(), 1e-6);
    expectNearNumber(-122.41488, lastPoint.longitude(), 1e-6);
  }

  @Test
  public void testEncodePath5() {
    List<Point> path = decode(TEST_LINE, PRECISION_5);
    String encoded = encode(path, PRECISION_5);
    assertEquals(TEST_LINE, encoded);
  }

  @Test
  public void testDecodeEncodePath6() {
    List<Point> path = decode(TEST_LINE6, PRECISION_6);
    String encoded = encode(path, PRECISION_6);
    assertEquals(TEST_LINE6, encoded);
  }

  @Test
  public void testFromPolyline6() {

    List<Point> originalPath = Arrays.asList(
      Point.fromLngLat(2.2862036, 48.8267868),
      Point.fromLngLat(2.4, 48.9)
    );
    String encoded = encode(originalPath, PRECISION_6);
    List<Point> path = LineString.fromPolyline(encoded, PRECISION_6).coordinates();

    assertEquals(originalPath.size(), path.size());
    for (int i = 0; i < originalPath.size(); i++) {
      assertEquals(originalPath.get(i).latitude(), path.get(i).latitude(), DELTA);
      assertEquals(originalPath.get(i).longitude(), path.get(i).longitude(), DELTA);
    }
  }

  @Test
  public void testFromPolylineAndDecode() {

    List<Point> path1 = LineString.fromPolyline(TEST_LINE6, PRECISION_6).coordinates();
    List<Point> path2 = decode(TEST_LINE6, PRECISION_6);

    assertEquals(path1.size(), path2.size());
    for (int i = 0; i < path1.size(); i++) {
      assertEquals(path1.get(i), path2.get(i));
    }
  }


  @Test
  public void testEncodeDecodePath6() {
    List<Point> originalPath = Arrays.asList(
      Point.fromLngLat(2.2862036, 48.8267868),
      Point.fromLngLat(2.4, 48.9)
    );

    String encoded = encode(originalPath, PRECISION_6);
    List<Point> path =  decode(encoded, PRECISION_6);
    assertEquals(originalPath.size(), path.size());

    for (int i = 0; i < originalPath.size(); i++) {
      assertEquals(originalPath.get(i).latitude(), path.get(i).latitude(), DELTA);
      assertEquals(originalPath.get(i).longitude(), path.get(i).longitude(), DELTA);
    }
  }


  @Test
  public void decode_neverReturnsNullButRatherAnEmptyList() throws Exception {
    List<Point> path = decode("", PRECISION_5);
    assertNotNull(path);
    assertEquals(0, path.size());
  }

  @Test
  public void encode_neverReturnsNull() throws Exception {
    String encodedString = encode(new ArrayList<Point>(), PRECISION_6);
    assertNotNull(encodedString);
  }

  @Test
  @Deprecated
  public void simplify_neverReturnsNullButRatherAnEmptyList() throws Exception {
    List<Point> simplifiedPath = simplify(new ArrayList<Point>(), PRECISION_6);
    assertNotNull(simplifiedPath);
  }

  @Test
  @Deprecated
  public void simplify_returnSameListWhenListSizeIsLessThanOrEqualToTwo(){
    final List<Point> path = new ArrayList<>();
    path.add(Point.fromLngLat(0, 0));
    path.add(Point.fromLngLat(10, 0));
    List<Point> simplifiedPath = simplify(path, PRECISION_6, true);
    assertTrue("Returned list is different from input list", path == simplifiedPath);
  }

  @Test
  @Deprecated
  public void simplify_withHighestQuality() throws IOException{
    List<Point> path = createPointListFromResourceFile(SIMPLIFICATION_INPUT);
    List<Point> simplifiedPath = simplify(path, PRECISION_5, true);
    List<Point> expectedSimplifiedPath = createPointListFromResourceFile(SIMPLIFICATION_EXPECTED_OUTPUT);
    assertTrue("Wrong number of points retained",simplifiedPath.size() == expectedSimplifiedPath.size());
    int counter = 0;
    for(Point retainedPoint:simplifiedPath){
      Point expectedPoint = expectedSimplifiedPath.get(counter);
      assertTrue("Wrong point retained by simplification algorithm",retainedPoint.equals(expectedPoint));
      ++counter;
    }
  }

  private List<Point> createPointListFromResourceFile(String fileName) throws IOException {
    String inputPoints = loadJsonFixture(fileName);
    String[] coords = inputPoints.split(",", -1);

    final List<Point> pointList = new ArrayList<>();
    for(int i= 0;i <= coords.length-2;i = i+2) {
      double x = Double.parseDouble(coords[i]);
      double y = Double.parseDouble(coords[i+1]);
      pointList.add(Point.fromLngLat(x, y));
    }

    return pointList;
  }
}
