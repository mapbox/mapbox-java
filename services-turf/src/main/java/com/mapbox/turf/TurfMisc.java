package com.mapbox.turf;

import android.support.annotation.NonNull;
import com.mapbox.turf.models.LineIntersectsResult;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Class contains all the miscellaneous methods that Turf can perform.
 *
 * @see <a href="http://turfjs.org/docs/">Turf documentation</a>
 * @since 1.2.0
 */
public class TurfMisc {

  /**
   * Takes a line, a start {@link Point}, and a stop point and returns the line in between those
   * points.
   *
   * @param startPt Starting point.
   * @param stopPt  Stopping point.
   * @param line    Line to slice.
   * @return Sliced line.
   * @throws TurfException signals that a Turf exception of some sort has occurred.
   * @see <a href="http://turfjs.org/docs/#lineslice">Turf Line slice documentation</a>
   * @since 1.2.0
   */
  public static LineString lineSlice(Point startPt, Point stopPt, Feature line) throws TurfException {
    if (!line.geometry().type().equals("LineString")) {
      throw new TurfException("input must be a LineString Feature or Geometry");
    }

    return lineSlice(startPt, stopPt, (LineString) line.geometry());
  }

  /**
   * Takes a line, a start {@link Point}, and a stop point and returns the line in between those
   * points.
   *
   * @param startPt used for calculating the lineSlice
   * @param stopPt  used for calculating the lineSlice
   * @param line    geometry that should be sliced
   * @return a sliced {@link LineString}
   * @see <a href="http://turfjs.org/docs/#lineslice">Turf Line slice documentation</a>
   * @since 1.2.0
   */
  public static LineString lineSlice(@NonNull Point startPt, @NonNull Point stopPt,
                                     @NonNull LineString line) {
    List<Point> coords = line.coordinates();

    if (coords.size() < 2) {
      throw new TurfException("Turf lineSlice requires a LineString made up of at least 2 "
        + "coordinates.");
    } else if (startPt.equals(stopPt)) {
      throw new TurfException("Start and stop points in Turf lineSlice cannot equal each other.");
    }

    Feature startVertex = pointOnLine(startPt, coords);
    Feature stopVertex = pointOnLine(stopPt, coords);
    List<Feature> ends = new ArrayList<>();
    if ((int) startVertex.getNumberProperty("index")
      <= (int) stopVertex.getNumberProperty("index")) {
      ends.add(startVertex);
      ends.add(stopVertex);
    } else {
      ends.add(stopVertex);
      ends.add(startVertex);
    }
    List<Point> points = new ArrayList<>();
    points.add((Point) ends.get(0).geometry());
    for (int i = (int) ends.get(0).getNumberProperty("index") + 1;
         i < (int) ends.get(1).getNumberProperty("index") + 1; i++) {
      points.add(coords.get(i));
    }
    points.add((Point) ends.get(1).geometry());
    return LineString.fromLngLats(points);
  }

  /**
   * Takes a {@link Point} and a {@link LineString} and calculates the closest Point on the
   * LineString.
   *
   * @param pt     point to snap from
   * @param coords line to snap to
   * @return closest point on the line to point
   * @since 1.3.0
   */
  public static Feature pointOnLine(@NonNull Point pt, @NonNull List<Point> coords) {
    Feature closestPt = Feature.fromGeometry(
      Point.fromLngLat(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    closestPt.addNumberProperty("dist", Double.POSITIVE_INFINITY);

    for (int i = 0; i < coords.size() - 1; i++) {
      Feature start = Feature.fromGeometry(coords.get(i));
      Feature stop = Feature.fromGeometry(coords.get(i + 1));
      //start
      start.addNumberProperty("dist", TurfMeasurement.distance(
        pt, (Point) start.geometry(), TurfConstants.UNIT_MILES));
      //stop
      stop.addNumberProperty("dist", TurfMeasurement.distance(
        pt, (Point) stop.geometry(), TurfConstants.UNIT_MILES));
      //perpendicular
      double heightDistance = Math.max(
        start.properties().get("dist").getAsDouble(),
        stop.properties().get("dist").getAsDouble()
      );
      double direction = TurfMeasurement.bearing((Point) start.geometry(),
        (Point) stop.geometry());
      Feature perpendicularPt1 = Feature.fromGeometry(
        TurfMeasurement.destination(pt, heightDistance, direction + 90,
          TurfConstants.UNIT_MILES));
      Feature perpendicularPt2 = Feature.fromGeometry(
        TurfMeasurement.destination(pt, heightDistance, direction - 90,
          TurfConstants.UNIT_MILES));
      LineIntersectsResult intersect = lineIntersects(
        ((Point) perpendicularPt1.geometry()).longitude(),
        ((Point) perpendicularPt1.geometry()).latitude(),
        ((Point) perpendicularPt2.geometry()).longitude(),
        ((Point) perpendicularPt2.geometry()).latitude(),
        ((Point) start.geometry()).longitude(),
        ((Point) start.geometry()).latitude(),
        ((Point) stop.geometry()).longitude(),
        ((Point) stop.geometry()).latitude()
      );

      Feature intersectPt = null;
      if (intersect != null) {
        intersectPt = Feature.fromGeometry(
          Point.fromLngLat(intersect.getX(), intersect.getY()));
        intersectPt.addNumberProperty("dist", TurfMeasurement.distance(pt,
          (Point) intersectPt.geometry(), TurfConstants.UNIT_MILES));
      }

      if ((double) start.getNumberProperty("dist")
        < (double) closestPt.getNumberProperty("dist")) {
        closestPt = start;
        closestPt.addNumberProperty("index", i);
      }
      if ((double) stop.getNumberProperty("dist")
        < (double) closestPt.getNumberProperty("dist")) {
        closestPt = stop;
        closestPt.addNumberProperty("index", i);
      }
      if (intersectPt != null
        && (double) intersectPt.getNumberProperty("dist")
        < (double) closestPt.getNumberProperty("dist")) {
        closestPt = intersectPt;
        closestPt.addNumberProperty("index", i);
      }
    }

    return closestPt;
  }

  private static LineIntersectsResult lineIntersects(double line1StartX, double line1StartY,
                                                     double line1EndX, double line1EndY,
                                                     double line2StartX, double line2StartY,
                                                     double line2EndX, double line2EndY) {
    // If the lines intersect, the result contains the x and y of the intersection
    // (treating the lines as infinite) and booleans for whether line segment 1 or line
    // segment 2 contain the point
    double denominator;
    double a;
    double b;
    double numerator1;
    double numerator2;
    LineIntersectsResult result = new LineIntersectsResult();

    denominator = ((line2EndY - line2StartY) * (line1EndX - line1StartX))
      - ((line2EndX - line2StartX) * (line1EndY - line1StartY));
    if (denominator == 0) {
      if (result.getX() != null && result.getY() != null) {
        return result;
      } else {
        return null;
      }
    }
    a = line1StartY - line2StartY;
    b = line1StartX - line2StartX;
    numerator1 = ((line2EndX - line2StartX) * a) - ((line2EndY - line2StartY) * b);
    numerator2 = ((line1EndX - line1StartX) * a) - ((line1EndY - line1StartY) * b);
    a = numerator1 / denominator;
    b = numerator2 / denominator;

    // if we cast these lines infinitely in both directions, they intersect here:
    result.setX(line1StartX + (a * (line1EndX - line1StartX)));
    result.setY(line1StartY + (a * (line1EndY - line1StartY)));

    // if line1 is a segment and line2 is infinite, they intersect if:
    if (a > 0 && a < 1) {
      result.setOnLine1(true);
    }
    // if line2 is a segment and line1 is infinite, they intersect if:
    if (b > 0 && b < 1) {
      result.setOnLine2(true);
    }
    // if line1 and line2 are segments, they intersect if both of the above are true
    if (result.isOnLine1() && result.isOnLine2()) {
      return result;
    } else {
      return null;
    }
  }
}
