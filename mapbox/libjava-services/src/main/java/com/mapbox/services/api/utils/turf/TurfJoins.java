package com.mapbox.services.api.utils.turf;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.MultiPolygon;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Class contains methods that can determine if points lie within a polygon or not.
 *
 * @see <a href="http://turfjs.org/docs/">Turf documentation</a>
 * @since 1.3.0
 */
public class TurfJoins {

  /**
   * Takes a {@link Point} and a {@link Polygon} and determines if the point resides inside the
   * polygon. The polygon can be convex or concave. The function accounts for holes.
   *
   * @param point   which you'd like to check if inside the polygon
   * @param polygon which you'd like to check if the points inside
   * @return true if the Point is inside the Polygon; false if the Point is not inside the Polygon
   * @see <a href="http://turfjs.org/docs/#inside">Turf Inside documentation</a>
   * @since 1.3.0
   */
  public static boolean inside(Point point, Polygon polygon) {
    // This API needs to get better
    List<List<Point>> coordinates = polygon.coordinates();
    List<List<List<Point>>> multiCoordinates = new ArrayList<>();
    multiCoordinates.add(coordinates);
    return inside(point, MultiPolygon.fromLngLats(multiCoordinates));
  }

  /**
   * Takes a {@link Point} and a {@link MultiPolygon} and determines if the point resides inside
   * the polygon. The polygon can be convex or concave. The function accounts for holes.
   *
   * @param point        which you'd like to check if inside the polygon
   * @param multiPolygon which you'd like to check if the points inside
   * @return true if the Point is inside the MultiPolygon; false if the Point is not inside the
   * MultiPolygon
   * @see <a href="http://turfjs.org/docs/#inside">Turf Inside documentation</a>
   * @since 1.3.0
   */
  public static boolean inside(Point point, MultiPolygon multiPolygon) {
    List<List<List<Point>>> polys = multiPolygon.coordinates();

    boolean insidePoly = false;
    for (int i = 0; i < polys.size() && !insidePoly; i++) {
      // check if it is in the outer ring first
      if (inRing(point, polys.get(i).get(0))) {
        boolean inHole = false;
        int k = 1;
        // check for the point in any of the holes
        while (k < polys.get(i).size() && !inHole) {
          if (inRing(point, polys.get(i).get(k))) {
            inHole = true;
          }
          k++;
        }
        if (!inHole) {
          insidePoly = true;
        }
      }
    }
    return insidePoly;
  }

  /**
   * Takes a {@link FeatureCollection} of {@link Point} and a {@link FeatureCollection} of
   * {@link Polygon} and returns the points that fall within the polygons.
   *
   * @param points   input points.
   * @param polygons input polygons.
   * @return points that land within at least one polygon.
   * @since 1.3.0
   */
  public static FeatureCollection within(FeatureCollection points, FeatureCollection polygons) {
    ArrayList<Feature> features = new ArrayList<>();
    for (int i = 0; i < polygons.getFeatures().size(); i++) {
      for (int j = 0; j < points.getFeatures().size(); j++) {
        Point point = (Point) points.getFeatures().get(j).getGeometry();
        boolean isInside
          = TurfJoins.inside(point, (Polygon) polygons.getFeatures().get(i).getGeometry());
        if (isInside) {
          features.add(Feature.fromGeometry(point));
        }
      }
    }
    return FeatureCollection.fromFeatures(features);
  }

  // pt is [x,y] and ring is [[x,y], [x,y],..]
  private static boolean inRing(Point pt, List<Point> ring) {
    boolean isInside = false;

    for (int i = 0, j = ring.size() - 1; i < ring.size(); j = i++) {
      double xi = ring.get(i).longitude();
      double yi = ring.get(i).latitude();
      double xj = ring.get(j).longitude();
      double yj = ring.get(j).latitude();
      boolean intersect = (
        (yi > pt.latitude()) != (yj > pt.latitude()))
        && (pt.longitude() < (xj - xi) * (pt.latitude() - yi) / (yj - yi) + xi);
      if (intersect) {
        isInside = !isInside;
      }
    }
    return isInside;
  }
}
