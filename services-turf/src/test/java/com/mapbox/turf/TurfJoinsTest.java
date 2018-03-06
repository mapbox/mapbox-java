package com.mapbox.turf;

import com.mapbox.geojson.Feature;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TurfJoinsTest extends TestUtils {

  private static final String POLY_WITH_HOLE_FIXTURE = "turf-inside/poly-with-hole.geojson";
  private static final String MULTIPOLY_WITH_HOLE_FIXTURE
    = "turf-inside/multipoly-with-hole.geojson";

  @Test
  public void testFeatureCollection() throws TurfException {
    // Test for a simple polygon
    ArrayList<Point> pointList = new ArrayList<>();
    pointList.add(Point.fromLngLat(0, 0));
    pointList.add(Point.fromLngLat(0, 100));
    pointList.add(Point.fromLngLat(100, 100));
    pointList.add(Point.fromLngLat(100, 0));
    pointList.add(Point.fromLngLat(0, 0));
    List<List<Point>> coordinates = new ArrayList<>();
    coordinates.add(pointList);
    Polygon poly = Polygon.fromLngLats(coordinates);

    Point ptIn = Point.fromLngLat(50, 50);
    Point ptOut = Point.fromLngLat(140, 150);

    assertTrue(TurfJoins.inside(ptIn, poly));
    assertFalse(TurfJoins.inside(ptOut, poly));

    // Test for a concave polygon
    pointList = new ArrayList<>();
    pointList.add(Point.fromLngLat(0, 0));
    pointList.add(Point.fromLngLat(50, 50));
    pointList.add(Point.fromLngLat(0, 100));
    pointList.add(Point.fromLngLat(100, 100));
    pointList.add(Point.fromLngLat(100, 0));
    pointList.add(Point.fromLngLat(0, 0));
    coordinates = new ArrayList<>();
    coordinates.add(pointList);
    Polygon concavePoly = Polygon.fromLngLats(coordinates);

    ptIn = Point.fromLngLat(75, 75);
    ptOut = Point.fromLngLat(25, 50);

    assertTrue(TurfJoins.inside(ptIn, concavePoly));
    assertFalse(TurfJoins.inside(ptOut, concavePoly));
  }

  @Test
  public void testPolyWithHole() throws TurfException, IOException {
    Point ptInHole = Point.fromLngLat(-86.69208526611328, 36.20373274711739);
    Point ptInPoly = Point.fromLngLat(-86.72229766845702, 36.20258997094334);
    Point ptOutsidePoly = Point.fromLngLat(-86.75079345703125, 36.18527313913089);
    Feature polyHole = Feature.fromJson(loadJsonFixture(POLY_WITH_HOLE_FIXTURE));

    assertFalse(TurfJoins.inside(ptInHole, (Polygon) polyHole.geometry()));
    assertTrue(TurfJoins.inside(ptInPoly, (Polygon) polyHole.geometry()));
    assertFalse(TurfJoins.inside(ptOutsidePoly, (Polygon) polyHole.geometry()));
  }

  @Test
  public void testMultipolygonWithHole() throws TurfException, IOException {
    Point ptInHole = Point.fromLngLat(-86.69208526611328, 36.20373274711739);
    Point ptInPoly = Point.fromLngLat(-86.72229766845702, 36.20258997094334);
    Point ptInPoly2 = Point.fromLngLat(-86.75079345703125, 36.18527313913089);
    Point ptOutsidePoly = Point.fromLngLat(-86.75302505493164, 36.23015046460186);

    Feature multiPolyHole = Feature.fromJson(loadJsonFixture(MULTIPOLY_WITH_HOLE_FIXTURE));
    assertFalse(TurfJoins.inside(ptInHole, (MultiPolygon) multiPolyHole.geometry()));
    assertTrue(TurfJoins.inside(ptInPoly, (MultiPolygon) multiPolyHole.geometry()));
    assertTrue(TurfJoins.inside(ptInPoly2, (MultiPolygon) multiPolyHole.geometry()));
    assertFalse(TurfJoins.inside(ptOutsidePoly, (MultiPolygon) multiPolyHole.geometry()));
  }

  /*
   * Custom test
   */

  @Test
  public void testInputPositions() throws IOException, TurfException {
    Point ptInPoly = Point.fromLngLat(-86.72229766845702, 36.20258997094334);
    Point ptOutsidePoly = Point.fromLngLat(-86.75079345703125, 36.18527313913089);
    Feature polyHole = Feature.fromJson(loadJsonFixture(POLY_WITH_HOLE_FIXTURE));

    Polygon polygon = (Polygon) polyHole.geometry();

    assertTrue(TurfJoins.inside(ptInPoly, polygon));
    assertFalse(TurfJoins.inside(ptOutsidePoly, polygon));
  }

  @Test
  public void testWithin() throws TurfException {
    // Test with a single point
    ArrayList<Point> pointList = new ArrayList<>();
    pointList.add(Point.fromLngLat(0, 0));
    pointList.add(Point.fromLngLat(0, 100));
    pointList.add(Point.fromLngLat(100, 100));
    pointList.add(Point.fromLngLat(100, 0));
    pointList.add(Point.fromLngLat(0, 0));
    ArrayList<List<Point>> coordinates = new ArrayList<>();
    coordinates.add(pointList);
    Polygon poly = Polygon.fromLngLats(coordinates);

    Point pt = Point.fromLngLat(50, 50);

    ArrayList<Feature> features1 = new ArrayList<>();
    features1.add(Feature.fromGeometry(poly));
    FeatureCollection polyFeatureCollection = FeatureCollection.fromFeatures(features1);

    ArrayList<Feature> features2 = new ArrayList<>();
    features2.add(Feature.fromGeometry(pt));
    FeatureCollection ptFeatureCollection = FeatureCollection.fromFeatures(features2);

    FeatureCollection counted = TurfJoins.pointsWithinPolygon(ptFeatureCollection, polyFeatureCollection);
    assertNotNull(counted);
    assertEquals(counted.features().size(), 1); // 1 point in 1 polygon

    // test with multiple points and multiple polygons
    Polygon poly1 = Polygon.fromLngLats(Arrays.asList(Arrays.asList(
      Point.fromLngLat(0, 0),
      Point.fromLngLat(10, 0),
      Point.fromLngLat(10, 10),
      Point.fromLngLat(0, 10),
      Point.fromLngLat(0, 0))));

    Polygon poly2 = Polygon.fromLngLats(Arrays.asList(Arrays.asList(
      Point.fromLngLat(10, 0),
      Point.fromLngLat(20, 10),
      Point.fromLngLat(20, 20),
      Point.fromLngLat(20, 0),
      Point.fromLngLat(10, 0))));
    
    polyFeatureCollection = FeatureCollection.fromFeatures(new Feature[] {
      Feature.fromGeometry(poly1),
      Feature.fromGeometry(poly2)});

    Point pt1 = Point.fromLngLat(1, 1);
    Point pt2 = Point.fromLngLat(1, 3);
    Point pt3 = Point.fromLngLat(14, 2);
    Point pt4 = Point.fromLngLat(13, 1);
    Point pt5 = Point.fromLngLat(19, 7);
    Point pt6 = Point.fromLngLat(100, 7);
    ptFeatureCollection = FeatureCollection.fromFeatures(new Feature[] {
      Feature.fromGeometry(pt1), Feature.fromGeometry(pt2), Feature.fromGeometry(pt3),
      Feature.fromGeometry(pt4), Feature.fromGeometry(pt5), Feature.fromGeometry(pt6)});

    counted = TurfJoins.pointsWithinPolygon(ptFeatureCollection, polyFeatureCollection);
    assertNotNull(counted);
    assertEquals(counted.features().size(), 5); // multiple points in multiple polygons
  }
}
