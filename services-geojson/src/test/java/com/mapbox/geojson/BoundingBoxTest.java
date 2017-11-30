package com.mapbox.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.mapbox.core.TestUtils;

import org.junit.Test;

public final class BoundingBoxTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    Point southwest = Point.fromLngLat(2.0, 2.0);
    Point northeast = Point.fromLngLat(4.0, 4.0);
    BoundingBox boundingBox = BoundingBox.fromPoints(southwest, northeast);
    assertNotNull(boundingBox);
  }

  @Test
  public void southWest_doesReturnMostSouthwestCoordinate() throws Exception {
    Point southwest = Point.fromLngLat(1.0, 2.0);
    Point northeast = Point.fromLngLat(3.0, 4.0);
    BoundingBox boundingBox = BoundingBox.fromPoints(southwest, northeast);
    assertTrue(southwest.equals(boundingBox.southwest()));
  }

  @Test
  public void northEast_doesReturnMostNortheastCoordinate() throws Exception {
    Point southwest = Point.fromLngLat(1.0, 2.0);
    Point northeast = Point.fromLngLat(3.0, 4.0);
    BoundingBox boundingBox = BoundingBox.fromPoints(southwest, northeast);
    assertTrue(northeast.equals(boundingBox.northeast()));
  }

  @Test
  public void west_doesReturnMostWestCoordinate() throws Exception {
    Point southwest = Point.fromLngLat(1.0, 2.0);
    Point northeast = Point.fromLngLat(3.0, 4.0);
    BoundingBox boundingBox = BoundingBox.fromPoints(southwest, northeast);
    assertEquals(1.0, boundingBox.west(), DELTA);
  }

  @Test
  public void south_doesReturnMostSouthCoordinate() throws Exception {
    Point southwest = Point.fromLngLat(1.0, 2.0);
    Point northeast = Point.fromLngLat(3.0, 4.0);
    BoundingBox boundingBox = BoundingBox.fromPoints(southwest, northeast);
    assertEquals(2.0, boundingBox.south(), DELTA);
  }

  @Test
  public void east_doesReturnMostEastCoordinate() throws Exception {
    Point southwest = Point.fromLngLat(1.0, 2.0);
    Point northeast = Point.fromLngLat(3.0, 4.0);
    BoundingBox boundingBox = BoundingBox.fromPoints(southwest, northeast);
    assertEquals(3.0, boundingBox.east(), DELTA);
  }

  @Test
  public void north_doesReturnMostNorthCoordinate() throws Exception {
    Point southwest = Point.fromLngLat(1.0, 2.0);
    Point northeast = Point.fromLngLat(3.0, 4.0);
    BoundingBox boundingBox = BoundingBox.fromPoints(southwest, northeast);
    assertEquals(4.0, boundingBox.north(), DELTA);
  }
}
