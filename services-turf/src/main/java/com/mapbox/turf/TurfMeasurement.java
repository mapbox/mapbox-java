package com.mapbox.turf;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.geojson.MultiPolygon;

import java.util.List;

/**
 * Class contains an assortment of methods used to calculate measurements such as bearing,
 * destination, midpoint, etc.
 *
 * @see <a href="http://turfjs.org/docs/">Turf documentation</a>
 * @since 1.2.0
 */
public final class TurfMeasurement {

  private TurfMeasurement() {
    // Private constructor preventing initialization of this class
  }

  /**
   * Takes two {@link Point}s and finds the geographic bearing between them.
   *
   * @param point1 first point used for calculating the bearing
   * @param point2 second point used for calculating the bearing
   * @return bearing in decimal degrees
   * @see <a href="http://turfjs.org/docs/#bearing">Turf Bearing documentation</a>
   * @since 1.3.0
   */
  public static double bearing(@NonNull Point point1, @NonNull Point point2) {
    double degrees2radians = Math.PI / 180;
    double radians2degrees = 180 / Math.PI;

    double lon1 = degrees2radians * point1.longitude();
    double lon2 = degrees2radians * point2.longitude();
    double lat1 = degrees2radians * point1.latitude();
    double lat2 = degrees2radians * point2.latitude();
    double a = Math.sin(lon2 - lon1) * Math.cos(lat2);
    double b = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
      * Math.cos(lat2) * Math.cos(lon2 - lon1);

    return radians2degrees * Math.atan2(a, b);
  }

  /**
   * Takes a Point and calculates the location of a destination point given a distance in
   * degrees, radians, miles, or kilometers; and bearing in degrees. This uses the Haversine
   * formula to account for global curvature.
   *
   * @param point    starting point used for calculating the destination
   * @param distance distance from the starting point
   * @param bearing  ranging from -180 to 180 in decimal degrees
   * @param units    one of the units found inside {@link TurfConstants.TurfUnitCriteria}
   * @return destination {@link Point} result where you specified
   * @see <a href="http://turfjs.org/docs/#destination">Turf Destination documetation</a>
   * @since 1.2.0
   */
  @NonNull
  public static Point destination(@NonNull Point point, @FloatRange(from = 0) double distance,
                                  @FloatRange(from = -180, to = 180) double bearing,
                                  @NonNull @TurfConstants.TurfUnitCriteria String units) {
    double degrees2radians = Math.PI / 180;
    double radians2degrees = 180 / Math.PI;
    double longitude1 = degrees2radians * point.longitude();
    double latitude1 = degrees2radians * point.latitude();
    double bearingRad = degrees2radians * bearing;

    double radians = TurfConversion.distanceToRadians(distance, units);

    double latitude2 = Math.asin(Math.sin(latitude1) * Math.cos(radians)
      + Math.cos(latitude1) * Math.sin(radians) * Math.cos(bearingRad));
    double longitude2 = longitude1 + Math.atan2(Math.sin(bearingRad)
        * Math.sin(radians) * Math.cos(latitude1),
      Math.cos(radians) - Math.sin(latitude1) * Math.sin(latitude2));

    return Point.fromLngLat(
      radians2degrees * longitude2, radians2degrees * latitude2);
  }

  /**
   * Calculates the distance between two points in kilometers. This uses the Haversine formula to
   * account for global curvature.
   *
   * @param point1 first point used for calculating the bearing
   * @param point2 second point used for calculating the bearing
   * @return distance between the two points in kilometers
   * @see <a href="http://turfjs.org/docs/#distance">Turf distance documentation</a>
   * @since 1.2.0
   */
  public static double distance(@NonNull Point point1, @NonNull Point point2) {
    return distance(point1, point2, TurfConstants.UNIT_DEFAULT);
  }

  /**
   * Calculates the distance between two points in degress, radians, miles, or kilometers. This
   * uses the Haversine formula to account for global curvature.
   *
   * @param point1 first point used for calculating the bearing
   * @param point2 second point used for calculating the bearing
   * @param units  one of the units found inside {@link TurfConstants.TurfUnitCriteria}
   * @return distance between the two points in kilometers
   * @see <a href="http://turfjs.org/docs/#distance">Turf distance documentation</a>
   * @since 1.2.0
   */
  public static double distance(@NonNull Point point1, @NonNull Point point2,
                                @NonNull @TurfConstants.TurfUnitCriteria String units) {
    double degrees2radians = Math.PI / 180;
    double dLat = degrees2radians * (point2.latitude() - point1.latitude());
    double dLon = degrees2radians * (point2.longitude() - point1.longitude());
    double lat1 = degrees2radians * point1.latitude();
    double lat2 = degrees2radians * point2.latitude();

    double a = Math.pow(Math.sin(dLat / 2), 2)
      + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);

    return TurfConversion.radiansToDistance(
      2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)), units);
  }

  /**
   * Takes a {@link LineString} and measures its length in the specified units.
   *
   * @param lineString geometry to measure
   * @param units      one of the units found inside {@link TurfConstants.TurfUnitCriteria}
   * @return length of the input line in the units specified
   * @see <a href="http://turfjs.org/docs/#linedistance">Turf Line Distance documentation</a>
   * @since 1.2.0
   */
  public static double lineDistance(@NonNull LineString lineString,
                                    @NonNull @TurfConstants.TurfUnitCriteria String units) {
    List<Point> coordinates = lineString.coordinates();
    return length(coordinates, units);
  }

  /**
   * Takes a {@link MultiLineString} and measures its length in the specified units.
   *
   * @param multiLineString geometry to measure
   * @param units           one of the units found inside {@link TurfConstants.TurfUnitCriteria}
   * @return length of the input lines combined, in the units specified
   * @see <a href="http://turfjs.org/docs/#linedistance">Turf Line Distance documentation</a>
   * @since 1.2.0
   */
  public static double lineDistance(@NonNull MultiLineString multiLineString,
                                    @NonNull @TurfConstants.TurfUnitCriteria String units) {
    double d = 0;
    for (List<Point> points : multiLineString.coordinates()) {
      d += length(points, units);
    }
    return d;
  }

  /**
   * Takes a {@link Polygon} and measures its perimeter in the specified units. if the polygon
   * contains holes, the perimeter will also be included.
   *
   * @param polygon geometry to measure
   * @param units   one of the units found inside {@link TurfConstants.TurfUnitCriteria}
   * @return total perimeter of the input polygon in the units specified
   * @see <a href="http://turfjs.org/docs/#linedistance">Turf Line Distance documentation</a>
   * @since 1.2.0
   */
  public static double lineDistance(@NonNull Polygon polygon,
                                    @NonNull @TurfConstants.TurfUnitCriteria String units) {
    double d = 0;
    for (List<Point> points : polygon.coordinates()) {
      d += length(points, units);
    }
    return d;
  }

  /**
   * Takes a {@link MultiPolygon} and measures each polygons perimeter in the specified units. if
   * one of the polygons contains holes, the perimeter will also be included.
   *
   * @param multiPolygon geometry to measure
   * @param units        one of the units found inside {@link TurfConstants.TurfUnitCriteria}
   * @return total perimeter of the input polygons combined, in the units specified
   * @see <a href="http://turfjs.org/docs/#linedistance">Turf Line Distance documentation</a>
   * @since 1.2.0
   */
  public static double lineDistance(@NonNull MultiPolygon multiPolygon,
                                    @NonNull @TurfConstants.TurfUnitCriteria String units) {
    double d = 0;
    List<List<List<Point>>> coordinates = multiPolygon.coordinates();
    for (List<List<Point>> coordinate : coordinates) {
      for (List<Point> aCoordinate : coordinate) {
        d += length(aCoordinate, units);
      }
    }
    return d;
  }

  /**
   * Takes two {@link Point}s and returns a point midway between them. The midpoint is calculated
   * geodesically, meaning the curvature of the earth is taken into account.
   *
   * @param from first point used for calculating the midpoint
   * @param to   second point used for calculating the midpoint
   * @return a {@link Point} midway between point1 and point2
   * @see <a href="http://turfjs.org/docs/#midpoint">Turf Midpoint documentation</a>
   * @since 1.3.0
   */
  public static Point midpoint(@NonNull Point from, @NonNull Point to) {
    double dist = distance(from, to, TurfConstants.UNIT_MILES);
    double heading = bearing(from, to);
    return destination(from, dist / 2, heading, TurfConstants.UNIT_MILES);
  }


  /**
   * Takes a line and returns a point at a specified distance along the line.
   *
   * @param line     that the point should be placed upon
   * @param distance along the linestring geometry which the point should be placed on
   * @param units    one of the units found inside {@link TurfConstants.TurfUnitCriteria}
   * @return a {@link Point} which is on the linestring provided and at the distance from the origin
   *   of that line to the end of the distance
   * @since 1.3.0
   */
  public static Point along(@NonNull LineString line, @FloatRange(from = 0) double distance,
                            @NonNull @TurfConstants.TurfUnitCriteria String units) {
    List<Point> coords = line.coordinates();

    double travelled = 0;
    for (int i = 0; i < coords.size(); i++) {
      if (distance >= travelled && i == coords.size() - 1) {
        break;
      } else if (travelled >= distance) {
        double overshot = distance - travelled;
        if (overshot == 0) {
          return coords.get(i);
        } else {
          double direction = bearing(coords.get(i), coords.get(i - 1)) - 180;
          return destination(coords.get(i), overshot, direction, units);
        }
      } else {
        travelled += distance(coords.get(i), coords.get(i + 1), units);
      }
    }

    return coords.get(coords.size() - 1);
  }

  /**
   * Takes a set of features, calculates the bbox of all input features, and returns a bounding box.
   *
   * @param point a {@link Point} object
   * @return A double array defining the bounding box in this order {@code [minX, minY, maxX, maxY]}
   * @since 2.0.0
   */
  public static double[] bbox(@NonNull Point point) {
    List<Point> resultCoords = TurfMeta.coordAll(point);
    return bboxCalculator(resultCoords);
  }

  /**
   * Takes a set of features, calculates the bbox of all input features, and returns a bounding box.
   *
   * @param lineString a {@link LineString} object
   * @return A double array defining the bounding box in this order {@code [minX, minY, maxX, maxY]}
   * @since 2.0.0
   */
  public static double[] bbox(@NonNull LineString lineString) {
    List<Point> resultCoords = TurfMeta.coordAll(lineString);
    return bboxCalculator(resultCoords);
  }

  /**
   * Takes a set of features, calculates the bbox of all input features, and returns a bounding box.
   *
   * @param multiPoint a {@link MultiPoint} object
   * @return a double array defining the bounding box in this order {@code [minX, minY, maxX, maxY]}
   * @since 2.0.0
   */
  public static double[] bbox(@NonNull MultiPoint multiPoint) {
    List<Point> resultCoords = TurfMeta.coordAll(multiPoint);
    return bboxCalculator(resultCoords);
  }

  /**
   * Takes a set of features, calculates the bbox of all input features, and returns a bounding box.
   *
   * @param polygon a {@link Polygon} object
   * @return a double array defining the bounding box in this order {@code [minX, minY, maxX, maxY]}
   * @since 2.0.0
   */
  public static double[] bbox(@NonNull Polygon polygon) {
    List<Point> resultCoords = TurfMeta.coordAll(polygon, false);
    return bboxCalculator(resultCoords);
  }

  /**
   * Takes a set of features, calculates the bbox of all input features, and returns a bounding box.
   *
   * @param multiLineString a {@link MultiLineString} object
   * @return a double array defining the bounding box in this order {@code [minX, minY, maxX, maxY]}
   * @since 2.0.0
   */
  public static double[] bbox(@NonNull MultiLineString multiLineString) {
    List<Point> resultCoords = TurfMeta.coordAll(multiLineString);
    return bboxCalculator(resultCoords);
  }

  /**
   * Takes a set of features, calculates the bbox of all input features, and returns a bounding box.
   *
   * @param multiPolygon a {@link MultiPolygon} object
   * @return a double array defining the bounding box in this order {@code [minX, minY, maxX, maxY]}
   * @since 2.0.0
   */
  public static double[] bbox(MultiPolygon multiPolygon) {
    List<Point> resultCoords = TurfMeta.coordAll(multiPolygon, false);
    return bboxCalculator(resultCoords);
  }

  private static double[] bboxCalculator(List<Point> resultCoords) {
    double[] bbox = new double[4];

    bbox[0] = Double.POSITIVE_INFINITY;
    bbox[1] = Double.POSITIVE_INFINITY;
    bbox[2] = Double.NEGATIVE_INFINITY;
    bbox[3] = Double.NEGATIVE_INFINITY;

    for (Point point : resultCoords) {
      if (bbox[0] > point.longitude()) {
        bbox[0] = point.longitude();
      }
      if (bbox[1] > point.latitude()) {
        bbox[1] = point.latitude();
      }
      if (bbox[2] < point.longitude()) {
        bbox[2] = point.longitude();
      }
      if (bbox[3] < point.latitude()) {
        bbox[3] = point.latitude();
      }
    }
    return bbox;
  }

  private static double length(List<Point> coords, String units) {
    double travelled = 0;
    Point prevCoords = coords.get(0);
    Point curCoords;
    for (int i = 1; i < coords.size(); i++) {
      curCoords = coords.get(i);
      travelled += distance(prevCoords, curCoords, units);
      prevCoords = curCoords;
    }
    return travelled;
  }
}
