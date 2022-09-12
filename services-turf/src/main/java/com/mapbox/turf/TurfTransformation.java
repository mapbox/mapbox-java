package com.mapbox.turf;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.mapbox.geojson.Polygon;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Methods in this class consume one GeoJSON object and output a new object with the defined
 * parameters provided.
 *
 * @since 3.0.0
 */
public final class TurfTransformation {

  private static final int DEFAULT_STEPS = 64;

  // 1 by default (in the same metric as the point coordinates)
  private static final double SIMPLIFY_DEFAULT_TOLERANCE = 1;

  // False by default (excludes distance-based preprocessing step which leads to highest quality
  // simplification but runs slower)
  private static final boolean SIMPLIFY_DEFAULT_HIGH_QUALITY = false;

  private TurfTransformation() {
    // Empty constructor to prevent class initialization
  }

  /**
   * Takes a {@link Point} and calculates the circle polygon given a radius in degrees, radians,
   * miles, or kilometers; and steps for precision. This uses the {@link #DEFAULT_STEPS} and
   * {@link TurfConstants#UNIT_DEFAULT} values.
   *
   * @param center a {@link Point} which the circle will center around
   * @param radius the radius of the circle
   * @return a {@link Polygon} which represents the newly created circle
   * @since 3.0.0
   */
  public static Polygon circle(@NonNull Point center, double radius) {
    return circle(center, radius, 64, TurfConstants.UNIT_DEFAULT);
  }

  /**
   * Takes a {@link Point} and calculates the circle polygon given a radius in the
   * provided {@link TurfConstants.TurfUnitCriteria}; and steps for precision. This
   * method uses the {@link #DEFAULT_STEPS}.
   *
   * @param center a {@link Point} which the circle will center around
   * @param radius the radius of the circle
   * @param units  one of the units found inside {@link TurfConstants.TurfUnitCriteria}
   * @return a {@link Polygon} which represents the newly created circle
   * @since 3.0.0
   */
  public static Polygon circle(@NonNull Point center, double radius,
                               @TurfConstants.TurfUnitCriteria String units) {
    return circle(center, radius, DEFAULT_STEPS, units);
  }

  /**
   * Takes a {@link Point} and calculates the circle polygon given a radius in the
   * provided {@link TurfConstants.TurfUnitCriteria}; and steps for precision.
   *
   * @param center a {@link Point} which the circle will center around
   * @param radius the radius of the circle
   * @param steps  number of steps which make up the circle parameter
   * @param units  one of the units found inside {@link TurfConstants.TurfUnitCriteria}
   * @return a {@link Polygon} which represents the newly created circle
   * @since 3.0.0
   */
  public static Polygon circle(@NonNull Point center, double radius, @IntRange(from = 1) int steps,
                               @TurfConstants.TurfUnitCriteria String units) {
    List<Point> coordinates = new ArrayList<>();
    for (int i = 0; i < steps; i++) {
      coordinates.add(TurfMeasurement.destination(center, radius, i * 360d / steps, units));
    }

    if (coordinates.size() > 0) {
      coordinates.add(coordinates.get(0));
    }
    List<List<Point>> coordinate = new ArrayList<>();
    coordinate.add(coordinates);
    return Polygon.fromLngLats(coordinate);
  }

  /**
   * Simplifies provided list of coordinates points into a shorter list of coordinates.
   *
   * @param original    the coordinates to simplify
   * @return simplified coordinates
   */
  @NonNull
  public static List<Point> simplify(@NonNull List<Point> original) {
    return simplify(original, SIMPLIFY_DEFAULT_TOLERANCE, SIMPLIFY_DEFAULT_HIGH_QUALITY);
  }

  /**
   * Simplifies provided list of coordinates points into a shorter list of coordinates.
   *
   * @param original    the coordinates to simplify
   * @param tolerance   tolerance in the same measurement as the point coordinates
   * @return simplified coordinates
   */
  @NonNull
  public static List<Point> simplify(@NonNull List<Point> original, double tolerance) {
    return simplify(original, tolerance, SIMPLIFY_DEFAULT_HIGH_QUALITY);
  }

  /**
   * Simplifies provided list of coordinates points into a shorter list of coordinates.
   *
   * @param original    the coordinates to simplify
   * @param highQuality true for using Douglas-Peucker, false for Radial-Distance algorithm
   * @return simplified coordinates
   */
  @NonNull
  public static List<Point> simplify(@NonNull List<Point> original, boolean highQuality) {
    return simplify(original, SIMPLIFY_DEFAULT_TOLERANCE, highQuality);
  }

  /**
   * Simplifies provided list of coordinates points into a shorter list of coordinates.
   *
   * @param original    the coordinates to simplify
   * @param tolerance   tolerance in the same measurement as the point coordinates
   * @param highQuality true for using Douglas-Peucker, false for Radial-Distance algorithm
   * @return simplified coordinates
   */
  @NonNull
  public static List<Point> simplify(
    @NonNull List<Point> original,
    double tolerance,
    boolean highQuality
  ) {
    if (original.size() <= 2) {
      return original;
    }
    double squaredTolerance = tolerance * tolerance;
    List<Point> dpInput = highQuality ? original : simplifyRadial(original, squaredTolerance);
    return simplifyDouglasPeucker(dpInput, squaredTolerance);
  }

  private static List<Point> simplifyRadial(
    @NonNull List<Point> original,
    double squaredTolerance
  ) {
    if (original.size() <= 2) {
      return original;
    }
    Point prevCoordinate = original.get(0);
    List<Point> newCoordinates = new ArrayList<>();
    newCoordinates.add(prevCoordinate);
    Point coordinate = original.get(1);

    for (int i = 1; i < original.size(); i++) {
      coordinate = original.get(i);
      if (squaredDistance(coordinate, prevCoordinate) > squaredTolerance) {
        newCoordinates.add(coordinate);
        prevCoordinate = coordinate;
      }
    }

    if (prevCoordinate != coordinate) {
      newCoordinates.add(coordinate);
    }

    return newCoordinates;
  }

  private static double squaredDistance(@NonNull Point p1, @NonNull Point p2) {
    double dx = p2.longitude() - p1.longitude();
    double dy = p2.latitude() - p1.latitude();
    return dx * dx + dy * dy;
  }

  private static List<Point> simplifyDouglasPeucker(
    @NonNull List<Point> original,
    double tolerance
  ) {
    if (original.size() <= 2) {
      return original;
    }

    int lastPointIndex = original.size() - 1;
    List<Point> result = new ArrayList<>();
    result.add(original.get(0));
    simplifyDouglasPeuckerStep(original, 0, lastPointIndex, tolerance, result);
    result.add(original.get(lastPointIndex));
    return result;
  }

  private static void simplifyDouglasPeuckerStep(
    @NonNull List<Point> original,
    int startIndex,
    int endIndex,
    double tolerance,
    @NonNull List<Point> simplified
  ) {
    double maxSquaredDistance = tolerance;
    int index = 0;

    for (int i = startIndex + 1; i < endIndex; i++) {
      double squaredDistance = squaredSegmentDistance(
        original.get(i),
        original.get(startIndex),
        original.get(endIndex)
      );
      if (squaredDistance > maxSquaredDistance) {
        index = i;
        maxSquaredDistance = squaredDistance;
      }
    }

    if (maxSquaredDistance > tolerance) {
      if (index - startIndex > 1) {
        simplifyDouglasPeuckerStep(original, startIndex, index, tolerance, simplified);
      }
      simplified.add(original.get(index));
      if (endIndex - index > 1) {
        simplifyDouglasPeuckerStep(original, index, endIndex, tolerance, simplified);
      }
    }
  }

  private static double squaredSegmentDistance(
    @NonNull Point point,
    @NonNull Point segmentStart,
    @NonNull Point segmentEnd
  ) {
    double x = segmentStart.latitude();
    double y = segmentStart.longitude();
    double dx = segmentEnd.latitude() - x;
    double dy = segmentEnd.longitude() - y;

    if (dx != 0 || dy != 0) {
      double t = ((point.latitude() - x) * dx + (point.longitude() - y) * dy) / (dx * dx + dy * dy);
      if (t > 1) {
        x = segmentEnd.latitude();
        y = segmentEnd.longitude();
      } else if (t > 0) {
        x += dx * t;
        y += dy * t;
      }
    }

    dx = point.latitude() - x;
    dy = point.longitude() - y;

    return dx * dx + dy * dy;
  }
}
