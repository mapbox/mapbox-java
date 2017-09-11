package com.mapbox.services.api.turf;

import com.mapbox.services.api.utils.turf.TurfException;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TurfGridsTest {

  @Test
  public void testWithin() throws TurfException {
    // Test with a single point
    ArrayList<Position> pointList = new ArrayList<>();
    pointList.add(Position.fromCoordinates(0, 0));
    pointList.add(Position.fromCoordinates(0, 100));
    pointList.add(Position.fromCoordinates(100, 100));
    pointList.add(Position.fromCoordinates(100, 0));
    pointList.add(Position.fromCoordinates(0, 0));
    ArrayList<List<Position>> coordinates = new ArrayList<>();
    coordinates.add(pointList);
    Polygon poly = Polygon.fromCoordinates(coordinates);

    Point pt = Point.fromCoordinates(Position.fromCoordinates(50, 50));

    ArrayList<Feature> features1 = new ArrayList<>();
    features1.add(Feature.fromGeometry(poly));
    FeatureCollection polyFeatureCollection = FeatureCollection.fromFeatures(features1);

    ArrayList<Feature> features2 = new ArrayList<>();
    features2.add(Feature.fromGeometry(pt));
    FeatureCollection ptFeatureCollection = FeatureCollection.fromFeatures(features2);

    FeatureCollection counted = TurfGrids.within(ptFeatureCollection, polyFeatureCollection);
    assertNotNull(counted);
    assertEquals(counted.getFeatures().size(), 1); // 1 point in 1 polygon

    // test with multiple points and multiple polygons
    Polygon poly1 = Polygon.fromCoordinates(new double[][][] {{{0, 0}, {10, 0}, {10, 10}, {0, 10}, {0, 0}}});
    Polygon poly2 = Polygon.fromCoordinates(new double[][][] {{{10, 0}, {20, 10}, {20, 20}, {20, 0}, {10, 0}}});
    polyFeatureCollection = FeatureCollection.fromFeatures(new Feature[] {
      Feature.fromGeometry(poly1),
      Feature.fromGeometry(poly2)});

    Point pt1 = Point.fromCoordinates(Position.fromCoordinates(1, 1));
    Point pt2 = Point.fromCoordinates(Position.fromCoordinates(1, 3));
    Point pt3 = Point.fromCoordinates(Position.fromCoordinates(14, 2));
    Point pt4 = Point.fromCoordinates(Position.fromCoordinates(13, 1));
    Point pt5 = Point.fromCoordinates(Position.fromCoordinates(19, 7));
    Point pt6 = Point.fromCoordinates(Position.fromCoordinates(100, 7));
    ptFeatureCollection = FeatureCollection.fromFeatures(new Feature[] {
      Feature.fromGeometry(pt1), Feature.fromGeometry(pt2), Feature.fromGeometry(pt3),
      Feature.fromGeometry(pt4), Feature.fromGeometry(pt5), Feature.fromGeometry(pt6)});

    counted = TurfGrids.within(ptFeatureCollection, polyFeatureCollection);
    assertNotNull(counted);
    assertEquals(counted.getFeatures().size(), 5); // multiple points in multiple polygons
  }
}
