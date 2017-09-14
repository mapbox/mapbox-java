package com.mapbox.services.commons.geojson.utils;

import com.mapbox.services.commons.geojson.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Polyline utils class contains method that can decode/encode a polyline, simplify a line, and more.
 *
 * @since 1.0.0
 */
public class PolylineUtils {

  // 1 by default (in the same metric as the point coordinates)
  private static final double SIMPLIFY_DEFAULT_TOLERANCE = 1;

  // False by default (excludes distance-based preprocessing step which leads to highest quality
  // simplification but runs slower)
  private static final boolean SIMPLIFY_DEFAULT_HIGHEST_QUALITY = false;

  /**
   * Decodes an encoded path string into a sequence of Points.
   *
   * @param encodedPath {@link String} representing a path string.
   * @param precision   OSRMv4 uses 6, OSRMv5 and Google uses 5.
   * @return List of {@link Point} making up the line.
   * @see <a href="https://github.com/mapbox/polyline/blob/master/src/polyline.js">Part of algorithm came from this source</a>
   * @see <a href="https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/PolyUtil.java">Part of algorithm came from this source.</a>
   * @since 1.0.0
   */
  public static List<Point> decode(final String encodedPath, int precision) {
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
      int b;
      do {
        b = encodedPath.charAt(index++) - 63 - 1;
        result += b << shift;
        shift += 5;
      }
      while (b >= 0x1f);
      lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

      result = 1;
      shift = 0;
      do {
        b = encodedPath.charAt(index++) - 63 - 1;
        result += b << shift;
        shift += 5;
      }
      while (b >= 0x1f);
      lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

      path.add(Point.fromLngLat(lng / factor, lat / factor));
    }

    return path;
  }

  /**
   * Encodes a sequence of Points into an encoded path string.
   *
   * @param path      List of {@link Point} making up the line.
   * @param precision OSRMv4 uses 6, OSRMv5 and Google uses 5.
   * @return {@link String} representing a path string.
   * @since 1.0.0
   */
  public static String encode(final List<Point> path, int precision) {
    long lastLat = 0;
    long lastLng = 0;

    final StringBuffer result = new StringBuffer();

    // OSRM uses precision=6, the default Polyline spec divides by 1E5, capping at precision=5
    double factor = Math.pow(10, precision);

    for (final Point point : path) {
      long lat = Math.round(point.latitude() * factor);
      long lng = Math.round(point.longitude() * factor);

      long dLat = lat - lastLat;
      long dLng = lng - lastLng;

      encode(dLat, result);
      encode(dLng, result);

      lastLat = lat;
      lastLng = lng;
    }
    return result.toString();
  }

  private static void encode(long variable, StringBuffer result) {
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
   * Square distance between 2 points
   */
  private static double getSqDist(Point p1, Point p2) {
    double dx = p1.longitude() - p2.longitude();
    double dy = p1.latitude() - p2.latitude();
    return dx * dx + dy * dy;
  }

  /**
   * Square distance from a point to a segment
   */
  private static double getSqSegDist(Point point, Point p1, Point p2) {
    double x = p1.longitude();
    double y = p1.latitude();
    double dx = p2.longitude() - x;
    double dy = p2.latitude() - y;

    if (dx != 0 || dy != 0) {
      double t = ((point.longitude() - x) * dx + (point.latitude() - y) * dy) / (dx * dx + dy * dy);
      if (t > 1) {
        x = p2.longitude();
        y = p2.latitude();

      } else if (t > 0) {
        x += dx * t;
        y += dy * t;
      }
    }

    dx = point.longitude() - x;
    dy = point.latitude() - y;

    return dx * dx + dy * dy;
  }

  /**
   * Basic distance-based simplification
   */
  private static Point[] simplifyRadialDist(Point[] points, double sqTolerance) {
    Point prevPoint = points[0];
    ArrayList<Point> newPoints = new ArrayList<>();
    newPoints.add(prevPoint);
    Point point = null;

    for (int i = 1, len = points.length; i < len; i++) {
      point = points[i];

      if (getSqDist(point, prevPoint) > sqTolerance) {
        newPoints.add(point);
        prevPoint = point;
      }
    }

    if (prevPoint != point) {
      newPoints.add(point);
    }

    return newPoints.toArray(new Point[newPoints.size()]);
  }

  private static List<Point> simplifyDpStep(
    Point[] points, int first, int last, double sqTolerance, List<Point> simplified) {
    double maxSqDist = sqTolerance;
    int index = 0;

    ArrayList<Point> stepList = new ArrayList<>();

    for (int i = first + 1; i < last; i++) {
      double sqDist = getSqSegDist(points[i], points[first], points[last]);
      if (sqDist > maxSqDist) {
        index = i;
        maxSqDist = sqDist;
      }
    }

    if (maxSqDist > sqTolerance) {
      if (index - first > 1) {
        stepList.addAll(simplifyDpStep(points, first, index, sqTolerance, simplified));
      }

      stepList.add(points[index]);

      if (last - index > 1) {
        stepList.addAll(simplifyDpStep(points, index, last, sqTolerance, simplified));
      }
    }

    return stepList;
  }

  /**
   * Simplification using Ramer-Douglas-Peucker algorithm
   */
  private static Point[] simplifyDouglasPeucker(Point[] points, double sqTolerance) {
    int last = points.length - 1;

    ArrayList<Point> simplified = new ArrayList<>();
    simplified.add(points[0]);
    simplified.addAll(simplifyDpStep(points, 0, last, sqTolerance, simplified));
    simplified.add(points[last]);
    return simplified.toArray(new Point[simplified.size()]);
  }

  /**
   * Reduces the number of points in a polyline while retaining its shape, giving a performance
   * boost when processing it and also reducing visual noise
   *
   * @param points an array of points
   * @return an array of simplified points
   * @see <a href="http://mourner.github.io/simplify-js/">JavaScript implementation</a>
   * @since 1.2.0
   */
  public static Point[] simplify(Point[] points) {
    return simplify(points, SIMPLIFY_DEFAULT_TOLERANCE, SIMPLIFY_DEFAULT_HIGHEST_QUALITY);
  }

  /**
   * Reduces the number of points in a polyline while retaining its shape, giving a performance
   * boost when processing it and also reducing visual noise.
   *
   * @param points    an array of points
   * @param tolerance affects the amount of simplification (in the same metric as the point coordinates)
   * @return an array of simplified points
   * @see <a href="http://mourner.github.io/simplify-js/">JavaScript implementation</a>
   * @since 1.2.0
   */
  public static Point[] simplify(Point[] points, double tolerance) {
    return simplify(points, tolerance, SIMPLIFY_DEFAULT_HIGHEST_QUALITY);
  }

  /**
   * Reduces the number of points in a polyline while retaining its shape, giving a performance
   * boost when processing it and also reducing visual noise.
   *
   * @param points         an array of points
   * @param highestQuality excludes distance-based preprocessing step which leads to highest quality simplification
   * @return an array of simplified points
   * @see <a href="http://mourner.github.io/simplify-js/">JavaScript implementation</a>
   * @since 1.2.0
   */
  public static Point[] simplify(Point[] points, boolean highestQuality) {
    return simplify(points, SIMPLIFY_DEFAULT_TOLERANCE, highestQuality);
  }

  /**
   * Reduces the number of points in a polyline while retaining its shape, giving a performance
   * boost when processing it and also reducing visual noise.
   *
   * @param points         an array of points
   * @param tolerance      affects the amount of simplification (in the same metric as the point coordinates)
   * @param highestQuality excludes distance-based preprocessing step which leads to highest quality simplification
   * @return an array of simplified points
   * @see <a href="http://mourner.github.io/simplify-js/">JavaScript implementation</a>
   * @since 1.2.0
   */
  public static Point[] simplify(Point[] points, double tolerance, boolean highestQuality) {
    if (points.length <= 2) {
      return points;
    }

    double sqTolerance = tolerance * tolerance;

    points = highestQuality ? points : simplifyRadialDist(points, sqTolerance);
    points = simplifyDouglasPeucker(points, sqTolerance);

    return points;
  }
}
