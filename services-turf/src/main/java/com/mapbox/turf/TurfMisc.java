package com.mapbox.turf;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.turf.models.LineIntersectsResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Class contains all the miscellaneous methods that Turf can perform.
 *
 * @see <a href="http://turfjs.org/docs/">Turf documentation</a>
 * @since 1.2.0
 */
public final class TurfMisc {

  private static final String INDEX_KEY = "index";

  private TurfMisc() {
    throw new AssertionError("No Instances.");
  }

  /**
   * Takes lines {@link LineString} and returns intersect points. The time complexity is O(nm)
   * that is not as efficient as Turf.js implementation.
   *
   * @param line1 LineString 1
   * @param line2 LineString 2
   * @return Intersect points
   * @see <a href="https://turfjs.org/docs/#lineIntersect">Turf Line intersect documentation</a>
   * @since 6.2.0
   */
  @NonNull
  public static List<Point> lineIntersect(@NonNull LineString line1, @NonNull LineString line2) {
    List<Point> result = new ArrayList<>();
    Point[] line1Points = line1.coordinates().toArray(new Point[line1.coordinates().size()]);
    Point[] line2Points = line2.coordinates().toArray(new Point[line2.coordinates().size()]);

    for (int i = 0; i < line1Points.length - 1; i++) {
      for (int j = 0; j < line2Points.length - 1; j++) {
        LineIntersectsResult intersects = lineIntersects(
          line1Points[i].longitude(),
          line1Points[i].latitude(),
          line1Points[i + 1].longitude(),
          line1Points[i + 1].latitude(),
          line2Points[j].longitude(),
          line2Points[j].latitude(),
          line2Points[j + 1].longitude(),
          line2Points[j + 1].latitude());

        if (intersects != null) {
          result.add(Point.fromLngLat(
            intersects.horizontalIntersection(),
            intersects.verticalIntersection()));
        }
      }
    }
    return result;
  }

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
  @NonNull
  public static LineString lineSlice(@NonNull Point startPt, @NonNull Point stopPt,
                                     @NonNull Feature line) {
    if (line.geometry() == null) {
      throw new NullPointerException("Feature.geometry() == null");
    }
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
  @NonNull
  public static LineString lineSlice(@NonNull Point startPt, @NonNull Point stopPt,
                                     @NonNull LineString line) {

    List<Point> coords = line.coordinates();

    if (coords.size() < 2) {
      throw new TurfException("Turf lineSlice requires a LineString made up of at least 2 "
        + "coordinates.");
    } else if (startPt.equals(stopPt)) {
      throw new TurfException("Start and stop points in Turf lineSlice cannot equal each other.");
    }

    Feature startVertex = nearestPointOnLine(startPt, coords);
    Feature stopVertex = nearestPointOnLine(stopPt, coords);
    List<Feature> ends = new ArrayList<>();
    if ((int) startVertex.getNumberProperty(INDEX_KEY)
      <= (int) stopVertex.getNumberProperty(INDEX_KEY)) {
      ends.add(startVertex);
      ends.add(stopVertex);
    } else {
      ends.add(stopVertex);
      ends.add(startVertex);
    }
    List<Point> points = new ArrayList<>();
    points.add((Point) ends.get(0).geometry());
    for (int i = (int) ends.get(0).getNumberProperty(INDEX_KEY) + 1;
         i < (int) ends.get(1).getNumberProperty(INDEX_KEY) + 1; i++) {
      points.add(coords.get(i));
    }
    points.add((Point) ends.get(1).geometry());
    return LineString.fromLngLats(points);
  }

  /**
   * Takes a {@link LineString}, a specified distance along the line to a start {@link Point},
   * and a specified distance along the line to a stop point
   * and returns a subsection of the line in-between those points.
   *
   * This can be useful for extracting only the part of a route between two distances.
   *
   * @param line input line
   * @param startDist distance along the line to starting point
   * @param stopDist distance along the line to ending point
   * @param units one of the units found inside {@link TurfConstants.TurfUnitCriteria}
   *              can be degrees, radians, miles, or kilometers
   * @return sliced line
   * @throws TurfException signals that a Turf exception of some sort has occurred.
   * @see <a href="http://turfjs.org/docs/#lineslicealong">Turf Line slice documentation</a>
   * @since 3.1.0
   */
  @NonNull
  public static LineString lineSliceAlong(@NonNull Feature line,
                                          @FloatRange(from = 0) double startDist,
                                          @FloatRange(from = 0) double stopDist,
                                          @NonNull @TurfConstants.TurfUnitCriteria String units) {
    if (line.geometry() == null) {
      throw new NullPointerException("Feature.geometry() == null");
    }
    if (!line.geometry().type().equals("LineString")) {
      throw new TurfException("input must be a LineString Feature or Geometry");
    }

    return lineSliceAlong((LineString)line.geometry(), startDist, stopDist, units);
  }

  /**
   * Takes a {@link LineString}, a specified distance along the line to a start {@link Point},
   * and a specified distance along the line to a stop point,
   * returns a subsection of the line in-between those points.
   * NOTE: use {@link TurfMisc#lineSliceAlong(LineString, List, double, double, String)}
   * instead of this method if you invoke the method often enough with the same `line` parameter.
   * This way you can calculate and cache the distances which might boost the performance.
   *
   * This can be useful for extracting only the part of a route between two distances.
   *
   * @param line input line
   * @param startDist distance along the line to starting point
   * @param stopDist distance along the line to ending point
   * @param units one of the units found inside {@link TurfConstants.TurfUnitCriteria}
   *              can be degrees, radians, miles, or kilometers
   * @return sliced line
   * @throws TurfException signals that a Turf exception of some sort has occurred.
   * @see <a href="http://turfjs.org/docs/#lineslicealong">Turf Line slice documentation</a>
   * @since 3.1.0
   */
  @NonNull
  public static LineString lineSliceAlong(@NonNull LineString line,
                                          @FloatRange(from = 0) double startDist,
                                          @FloatRange(from = 0) double stopDist,
                                          @NonNull @TurfConstants.TurfUnitCriteria String units) {
    List<Point> coords = line.coordinates();
    List<DistanceProvider> distanceProviders = new ArrayList<>();
    for (int i = 0; i < coords.size() - 1; i++) {
      final int index = i;
      distanceProviders.add(new DistanceProvider() {
        @Override
        public double get() {
          return TurfMeasurement.distance(coords.get(index), coords.get(index + 1), units);
        }
      });
    }
    return lineSliceAlong(line, distanceProviders, startDist, stopDist, units);
  }

  /**
   * Takes a {@link LineString}, a specified distance along the line to a start {@link Point},
   * and a specified distance along the line to a stop point,
   * returns a subsection of the line in-between those points.
   * NOTE: use this method instead of
   * {@link TurfMisc#lineSliceAlong(LineString, double, double, String)} if you invoke the method
   * often enough with the same `line` parameter.
   * This way you can calculate and cache the distances which might boost the performance.
   *
   * This can be useful for extracting only the part of a route between two distances.
   *
   * @param line input line
   * @param distanceProviders list of {@link DistanceProvider}
   * @param startDist distance along the line to starting point
   * @param stopDist distance along the line to ending point
   * @param units one of the units found inside {@link TurfConstants.TurfUnitCriteria}
   *              can be degrees, radians, miles, or kilometers
   * @return sliced line
   * @throws TurfException signals that a Turf exception of some sort has occurred.
   * @see <a href="http://turfjs.org/docs/#lineslicealong">Turf Line slice documentation</a>
   * @since 3.1.0
   */
  @NonNull
  public static LineString lineSliceAlong(@NonNull LineString line,
                                          @NonNull List<DistanceProvider> distanceProviders,
                                          @FloatRange(from = 0) double startDist,
                                          @FloatRange(from = 0) double stopDist,
                                          @NonNull @TurfConstants.TurfUnitCriteria String units) {

    List<Point> coords = line.coordinates();

    if (coords.size() < 2) {
      throw new TurfException("Turf lineSlice requires a LineString Geometry made up of "
        + "at least 2 coordinates. The LineString passed in only contains " + coords.size() + ".");
    } else if (startDist == stopDist) {
      throw new TurfException("Start and stop distance in Turf lineSliceAlong "
        + "cannot equal each other.");
    } else if (distanceProviders.size() != coords.size() - 1) {
      throw new TurfException("distanceProviders in Turf lineSliceAlong should be of size "
        + "coordinates - 1. Expected: " + (coords.size() - 1)
        + ", actual: " + distanceProviders.size());
    }

    List<Point> slice = new ArrayList<>(2);

    double travelled = 0;
    for (int i = 0; i < coords.size(); i++) {

      if (startDist >= travelled && i == coords.size() - 1) {
        break;

      } else if (travelled > startDist && slice.size() == 0) {
        double overshot = startDist - travelled;
        if (overshot == 0) {
          slice.add(coords.get(i));
          return LineString.fromLngLats(slice);
        }
        double direction = TurfMeasurement.bearing(coords.get(i), coords.get(i - 1)) - 180;
        Point interpolated = TurfMeasurement.destination(coords.get(i), overshot, direction, units);
        slice.add(interpolated);
      }

      if (travelled >= stopDist) {
        double overshot = stopDist - travelled;
        if (overshot == 0) {
          slice.add(coords.get(i));
          return LineString.fromLngLats(slice);
        }
        double direction = TurfMeasurement.bearing(coords.get(i), coords.get(i - 1)) - 180;
        Point interpolated = TurfMeasurement.destination(coords.get(i), overshot, direction, units);
        slice.add(interpolated);
        return LineString.fromLngLats(slice);
      }

      if (travelled >= startDist) {
        slice.add(coords.get(i));
      }

      if (i == coords.size() - 1) {
        return LineString.fromLngLats(slice);
      }

      travelled += distanceProviders.get(i).get();
    }

    if (travelled < startDist) {
      throw new TurfException("Start position is beyond line");
    }

    return LineString.fromLngLats(slice);
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
  @NonNull
  public static Feature nearestPointOnLine(@NonNull Point pt, @NonNull List<Point> coords) {
    return nearestPointOnLine(pt, coords, null);
  }

  /**
   * Takes a {@link Point} and a {@link LineString} and calculates the closest Point on the
   * LineString.
   *
   * @param pt point to snap from
   * @param coords line to snap to
   * @param units one of the units found inside {@link TurfConstants.TurfUnitCriteria}
   *              can be degrees, radians, miles, or kilometers
   * @return closest point on the line to point
   * @since 4.9.0
   */
  @NonNull
  public static Feature nearestPointOnLine(@NonNull Point pt, @NonNull List<Point> coords,
                                           @Nullable @TurfConstants.TurfUnitCriteria String units) {

    if (coords.size() < 2) {
      throw new TurfException("Turf nearestPointOnLine requires a List of Points "
          + "made up of at least 2 coordinates.");
    }

    if (units == null) {
      units = TurfConstants.UNIT_KILOMETERS;
    }

    Feature closestPt = Feature.fromGeometry(
        Point.fromLngLat(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    closestPt.addNumberProperty("dist", Double.POSITIVE_INFINITY);

    for (int i = 0; i < coords.size() - 1; i++) {
      Feature start = Feature.fromGeometry(coords.get(i));
      Feature stop = Feature.fromGeometry(coords.get(i + 1));
      //start
      start.addNumberProperty("dist", TurfMeasurement.distance(
        pt, (Point) start.geometry(), units));
      //stop
      stop.addNumberProperty("dist", TurfMeasurement.distance(
        pt, (Point) stop.geometry(), units));
      //perpendicular
      double heightDistance = Math.max(
        start.properties().get("dist").getAsDouble(),
        stop.properties().get("dist").getAsDouble()
      );
      double direction = TurfMeasurement.bearing((Point) start.geometry(),
        (Point) stop.geometry());
      Feature perpendicularPt1 = Feature.fromGeometry(
        TurfMeasurement.destination(pt, heightDistance, direction + 90, units));
      Feature perpendicularPt2 = Feature.fromGeometry(
        TurfMeasurement.destination(pt, heightDistance, direction - 90, units));
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
          Point.fromLngLat(intersect.horizontalIntersection(), intersect.verticalIntersection()));
        intersectPt.addNumberProperty("dist", TurfMeasurement.distance(pt,
          (Point) intersectPt.geometry(), units));
      }

      if ((double) start.getNumberProperty("dist")
        < (double) closestPt.getNumberProperty("dist")) {
        closestPt = start;
        closestPt.addNumberProperty(INDEX_KEY, i);
      }
      if ((double) stop.getNumberProperty("dist")
        < (double) closestPt.getNumberProperty("dist")) {
        closestPt = stop;
        closestPt.addNumberProperty(INDEX_KEY, i);
      }
      if (intersectPt != null
        && (double) intersectPt.getNumberProperty("dist")
        < (double) closestPt.getNumberProperty("dist")) {
        closestPt = intersectPt;
        closestPt.addNumberProperty(INDEX_KEY, i);
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
    LineIntersectsResult result = LineIntersectsResult.builder()
      .onLine1(false)
      .onLine2(false)
      .build();

    double denominator = ((line2EndY - line2StartY) * (line1EndX - line1StartX))
      - ((line2EndX - line2StartX) * (line1EndY - line1StartY));
    if (denominator == 0) {
      if (result.horizontalIntersection() != null && result.verticalIntersection() != null) {
        return result;
      } else {
        return null;
      }
    }
    double varA = line1StartY - line2StartY;
    double varB = line1StartX - line2StartX;
    double numerator1 = ((line2EndX - line2StartX) * varA) - ((line2EndY - line2StartY) * varB);
    double numerator2 = ((line1EndX - line1StartX) * varA) - ((line1EndY - line1StartY) * varB);
    varA = numerator1 / denominator;
    varB = numerator2 / denominator;

    // if we cast these lines infinitely in both directions, they intersect here:
    result = result.toBuilder().horizontalIntersection(line1StartX
      + (varA * (line1EndX - line1StartX))).build();
    result = result.toBuilder().verticalIntersection(line1StartY
      + (varA * (line1EndY - line1StartY))).build();

    // if line1 is a segment and line2 is infinite, they intersect if:
    if (varA >= 0 && varA <= 1) {
      result = result.toBuilder().onLine1(true).build();
    }
    // if line2 is a segment and line1 is infinite, they intersect if:
    if (varB >= 0 && varB <= 1) {
      result = result.toBuilder().onLine2(true).build();
    }
    // if line1 and line2 are segments, they intersect if both of the above are true
    if (result.onLine1() && result.onLine2()) {
      return result;
    } else {
      return null;
    }
  }
}
