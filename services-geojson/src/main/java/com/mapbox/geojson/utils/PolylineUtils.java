package com.mapbox.geojson.utils;

import android.support.annotation.NonNull;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Polyline utils class contains method that can decode/encode a polyline, simplify a line, and
 * more.
 *
 * @since 1.0.0
 */
public final class PolylineUtils {

  private PolylineUtils() {
    // Prevent initialization of this class
  }

  // 1 by default (in the same metric as the point coordinates)
  private static final double SIMPLIFY_DEFAULT_TOLERANCE = 1;

  // False by default (excludes distance-based preprocessing step which leads to highest quality
  // simplification but runs slower)
  private static final boolean SIMPLIFY_DEFAULT_HIGHEST_QUALITY = false;

  /**
   * Decodes an encoded path string into a sequence of {@link Point}.
   *
   * @param encodedPath a String representing an encoded path string
   * @param precision   OSRMv4 uses 6, OSRMv5 and Google uses 5
   * @return list of {@link Point} making up the line
   * @see <a href="https://github.com/mapbox/polyline/blob/master/src/polyline.js">Part of algorithm came from this source</a>
   * @see <a href="https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/PolyUtil.java">Part of algorithm came from this source.</a>
   * @since 1.0.0
   */
  @NonNull
  public static List<Point> decode(@NonNull final String encodedPath, int precision) {
    int len = encodedPath.length();

    // OSRM uses precision=6, the default Polyline spec divides by 1E5, capping at precision=5
    double factor = Math.pow(10, precision);

    // For speed we preallocate to an upper bound on the final length, then
    // truncate the array before returning.
    final List<Point> path = new ArrayList<>();
    int index = 0;
    int lat = 0;
    int lng = 0;

    while (index < len) {
      int result = 1;
      int shift = 0;
      int temp;
      do {
        temp = encodedPath.charAt(index++) - 63 - 1;
        result += temp << shift;
        shift += 5;
      }
      while (temp >= 0x1f);
      lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

      result = 1;
      shift = 0;
      do {
        temp = encodedPath.charAt(index++) - 63 - 1;
        result += temp << shift;
        shift += 5;
      }
      while (temp >= 0x1f);
      lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

      path.add(Point.fromLngLat(lng / factor, lat / factor));
    }

    return path;
  }

  /**
   * Encodes a sequence of Points into an encoded path string.
   *
   * @param path      list of {@link Point}s making up the line
   * @param precision OSRMv4 uses 6, OSRMv5 and Google uses 5
   * @return a String representing a path string
   * @since 1.0.0
   */
  @NonNull
  public static String encode(@NonNull final List<Point> path, int precision) {
    long lastLat = 0;
    long lastLng = 0;

    final StringBuilder result = new StringBuilder();

    // OSRM uses precision=6, the default Polyline spec divides by 1E5, capping at precision=5
    double factor = Math.pow(10, precision);

    for (final Point point : path) {
      long lat = Math.round(point.latitude() * factor);
      long lng = Math.round(point.longitude() * factor);

      long varLat = lat - lastLat;
      long varLng = lng - lastLng;

      encode(varLat, result);
      encode(varLng, result);

      lastLat = lat;
      lastLng = lng;
    }
    return result.toString();
  }

  private static void encode(long variable, StringBuilder result) {
    variable = variable < 0 ? ~(variable << 1) : variable << 1;
    while (variable >= 0x20) {
      result.append(Character.toChars((int) ((0x20 | (variable & 0x1f)) + 63)));
      variable >>= 5;
    }
    result.append(Character.toChars((int) (variable + 63)));
  }

  /*
   * Polyline simplification method. It's a direct port of simplify.js to Java.
   * See: https://github.com/mourner/simplify-js/blob/master/simplify.js
   */

  /**
   * Reduces the number of points in a polyline while retaining its shape, giving a performance
   * boost when processing it and also reducing visual noise.
   *
   * @param points an array of points
   * @return an array of simplified points
   * @see <a href="http://mourner.github.io/simplify-js/">JavaScript implementation</a>
   * @since 1.2.0
   */
  @NonNull
  public static List<Point> simplify(@NonNull List<Point> points) {
    return simplify(points, SIMPLIFY_DEFAULT_TOLERANCE, SIMPLIFY_DEFAULT_HIGHEST_QUALITY);
  }

  /**
   * Reduces the number of points in a polyline while retaining its shape, giving a performance
   * boost when processing it and also reducing visual noise.
   *
   * @param points    an array of points
   * @param tolerance affects the amount of simplification (in the same metric as the point
   *                  coordinates)
   * @return an array of simplified points
   * @see <a href="http://mourner.github.io/simplify-js/">JavaScript implementation</a>
   * @since 1.2.0
   */
  @NonNull
  public static List<Point> simplify(@NonNull List<Point> points, double tolerance) {
    return simplify(points, tolerance, SIMPLIFY_DEFAULT_HIGHEST_QUALITY);
  }

  /**
   * Reduces the number of points in a polyline while retaining its shape, giving a performance
   * boost when processing it and also reducing visual noise.
   *
   * @param points         an array of points
   * @param highestQuality excludes distance-based preprocessing step which leads to highest quality
   *                       simplification
   * @return an array of simplified points
   * @see <a href="http://mourner.github.io/simplify-js/">JavaScript implementation</a>
   * @since 1.2.0
   */
  @NonNull
  public static List<Point> simplify(@NonNull List<Point> points, boolean highestQuality) {
    return simplify(points, SIMPLIFY_DEFAULT_TOLERANCE, highestQuality);
  }

  /**
   * Reduces the number of points in a polyline while retaining its shape, giving a performance
   * boost when processing it and also reducing visual noise.
   *
   * @param points         an array of points
   * @param tolerance      affects the amount of simplification (in the same metric as the point
   *                       coordinates)
   * @param highestQuality excludes distance-based preprocessing step which leads to highest quality
   *                       simplification
   * @return an array of simplified points
   * @see <a href="http://mourner.github.io/simplify-js/">JavaScript implementation</a>
   * @since 1.2.0
   */
  @NonNull
  public static List<Point> simplify(@NonNull List<Point> points, double tolerance,
                                     boolean highestQuality) {
    if (points.size() <= 2) {
      return points;
    }

    double sqTolerance = tolerance * tolerance;

    points = highestQuality ? points : simplifyRadialDist(points, sqTolerance);
    points = simplifyDouglasPeucker(points, sqTolerance);

    return points;
  }

  /**
   * Square distance between 2 points.
   *
   * @param p1 first {@link Point}
   * @param p2 second Point
   * @return square of the distance between two input points
   */
  private static double getSqDist(Point p1, Point p2) {
    double dx = p1.longitude() - p2.longitude();
    double dy = p1.latitude() - p2.latitude();
    return dx * dx + dy * dy;
  }

  /**
   * Square distance from a point to a segment.
   *
   * @param point {@link Point} whose distance from segment needs to be determined
   * @param start {@link Point} of the line segment
   * @param end {@link Point} of the line segment
   * @return square of the distance between first input point and segment defined by other two input points
   */
  private static double getSqSegDist(Point point, Point start, Point end) {
    double horizontal = start.longitude();
    double vertical = start.latitude();
    double diffHorizontal = end.longitude() - horizontal;
    double diffVertical = end.latitude() - vertical;

    if (diffHorizontal != 0 || diffVertical != 0) {
      double total = ((point.longitude() - horizontal) * diffHorizontal + (point.latitude()
        - vertical) * diffVertical) / (diffHorizontal * diffHorizontal + diffVertical
        * diffVertical);
      if (total > 1) {
        horizontal = end.longitude();
        vertical = end.latitude();

      } else if (total > 0) {
        horizontal += diffHorizontal * total;
        vertical += diffVertical * total;
      }
    }

    diffHorizontal = point.longitude() - horizontal;
    diffVertical = point.latitude() - vertical;

    return diffHorizontal * diffHorizontal + diffVertical * diffVertical;
  }

  /**
   * Basic distance-based simplification.
   *
   * @param points a list of points to be simplified
   * @param sqTolerance square of amount of simplification
   * @return a list of simplified points
   */
  private static List<Point> simplifyRadialDist(List<Point> points, double sqTolerance) {
    Point prevPoint = points.get(0);
    ArrayList<Point> newPoints = new ArrayList<>();
    newPoints.add(prevPoint);
    Point point = null;

    for (int i = 1, len = points.size(); i < len; i++) {
      point = points.get(i);

      if (getSqDist(point, prevPoint) > sqTolerance) {
        newPoints.add(point);
        prevPoint = point;
      }
    }

    if (!prevPoint.equals(point)) {
      newPoints.add(point);
    }
    return newPoints;
  }

  private static List<Point> simplifyDpStep(
    List<Point> points, int first, int last, double sqTolerance, List<Point> simplified) {
    double maxSqDist = sqTolerance;
    int index = 0;

    ArrayList<Point> stepList = new ArrayList<>();

    for (int i = first + 1; i < last; i++) {
      double sqDist = getSqSegDist(points.get(i), points.get(first), points.get(last));
      if (sqDist > maxSqDist) {
        index = i;
        maxSqDist = sqDist;
      }
    }

    if (maxSqDist > sqTolerance) {
      if (index - first > 1) {
        stepList.addAll(simplifyDpStep(points, first, index, sqTolerance, simplified));
      }

      stepList.add(points.get(index));

      if (last - index > 1) {
        stepList.addAll(simplifyDpStep(points, index, last, sqTolerance, simplified));
      }
    }

    return stepList;
  }

  /**
   * Simplification using Ramer-Douglas-Peucker algorithm.
   *
   * @param points a list of points to be simplified
   * @param sqTolerance square of amount of simplification
   * @return a list of simplified points
   */
  private static List<Point> simplifyDouglasPeucker(List<Point> points, double sqTolerance) {
    int last = points.size() - 1;
    ArrayList<Point> simplified = new ArrayList<>();
    simplified.add(points.get(0));
    simplified.addAll(simplifyDpStep(points, 0, last, sqTolerance, simplified));
    simplified.add(points.get(last));
    return simplified;
  }
}
