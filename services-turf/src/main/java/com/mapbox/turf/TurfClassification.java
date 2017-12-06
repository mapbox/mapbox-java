package com.mapbox.turf;

import android.support.annotation.NonNull;

import com.mapbox.geojson.Point;

import java.util.List;

/**
 * Methods found in this class are meant to consume a set of information and classify it according
 * to a shared quality or characteristic.
 *
 * @since 3.0.0
 */
public class TurfClassification {

  private TurfClassification() {
    // Private constructor preventing initialization of this class
  }

  /**
   * Takes a reference point and a list of {@link Point} geometries and returns the point from the
   * set point list closest to the reference. This calculation is geodesic.
   *
   * @param targetPoint the reference point
   * @param points      set list of points to run against the input point
   * @return the closest point in the set to the reference point
   * @since 3.0.0
   */
  @NonNull
  public static Point nearest(@NonNull Point targetPoint, @NonNull List<Point> points) {
    if (points.isEmpty()) {
      return targetPoint;
    }
    Point nearestPoint = points.get(0);
    double minDist = Double.POSITIVE_INFINITY;
    for (Point point : points) {
      double distanceToPoint = TurfMeasurement.distance(targetPoint, point);
      if (distanceToPoint < minDist) {
        nearestPoint = point;
        minDist = distanceToPoint;
      }
    }
    return nearestPoint;
  }
}
