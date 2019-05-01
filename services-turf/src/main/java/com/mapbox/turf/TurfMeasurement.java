package com.mapbox.turf;

import static com.mapbox.turf.TurfConversion.degreesToRadians;
import static com.mapbox.turf.TurfConversion.radiansToDegrees;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.GeometryCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    throw new AssertionError("No Instances.");
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

    double lon1 = degreesToRadians(point1.longitude());
    double lon2 = degreesToRadians(point2.longitude());
    double lat1 = degreesToRadians(point1.latitude());
    double lat2 = degreesToRadians(point2.latitude());
    double value1 = Math.sin(lon2 - lon1) * Math.cos(lat2);
    double value2 = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
      * Math.cos(lat2) * Math.cos(lon2 - lon1);

    return radiansToDegrees(Math.atan2(value1, value2));
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

    double longitude1 = degreesToRadians(point.longitude());
    double latitude1 = degreesToRadians(point.latitude());
    double bearingRad = degreesToRadians(bearing);

    double radians = TurfConversion.lengthToRadians(distance, units);

    double latitude2 = Math.asin(Math.sin(latitude1) * Math.cos(radians)
      + Math.cos(latitude1) * Math.sin(radians) * Math.cos(bearingRad));
    double longitude2 = longitude1 + Math.atan2(Math.sin(bearingRad)
        * Math.sin(radians) * Math.cos(latitude1),
      Math.cos(radians) - Math.sin(latitude1) * Math.sin(latitude2));

    return Point.fromLngLat(
      radiansToDegrees(longitude2), radiansToDegrees(latitude2));
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
    double difLat = degreesToRadians((point2.latitude() - point1.latitude()));
    double difLon = degreesToRadians((point2.longitude() - point1.longitude()));
    double lat1 = degreesToRadians(point1.latitude());
    double lat2 = degreesToRadians(point2.latitude());

    double value = Math.pow(Math.sin(difLat / 2), 2)
      + Math.pow(Math.sin(difLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);

    return TurfConversion.radiansToLength(
      2 * Math.atan2(Math.sqrt(value), Math.sqrt(1 - value)), units);
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
  public static double length(@NonNull LineString lineString,
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
  public static double length(@NonNull MultiLineString multiLineString,
                              @NonNull @TurfConstants.TurfUnitCriteria String units) {
    double len = 0;
    for (List<Point> points : multiLineString.coordinates()) {
      len += length(points, units);
    }
    return len;
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
  public static double length(@NonNull Polygon polygon,
                              @NonNull @TurfConstants.TurfUnitCriteria String units) {
    double len = 0;
    for (List<Point> points : polygon.coordinates()) {
      len += length(points, units);
    }
    return len;
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
  public static double length(@NonNull MultiPolygon multiPolygon,
                              @NonNull @TurfConstants.TurfUnitCriteria String units) {
    double len = 0;
    List<List<List<Point>>> coordinates = multiPolygon.coordinates();
    for (List<List<Point>> coordinate : coordinates) {
      for (List<Point> theCoordinate : coordinate) {
        len += length(theCoordinate, units);
      }
    }
    return len;
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
   * @return a {@link Point} which is on the linestring provided and at the distance from
   *         the origin of that line to the end of the distance
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

  /**
   * Takes a set of features, calculates the bbox of all input features, and returns a bounding box.
   *
   * @param geoJson a {@link GeoJson} object
   * @return a double array defining the bounding box in this order {@code [minX, minY, maxX, maxY]}
   * @since 4.8.0
   */
  public static double[] bbox(GeoJson geoJson) {
    BoundingBox boundingBox = geoJson.bbox();
    if (boundingBox != null) {
      return new double[] {
        boundingBox.west(),
        boundingBox.south(),
        boundingBox.east(),
        boundingBox.north()
      };
    }

    if (geoJson instanceof Geometry) {
      return bbox((Geometry) geoJson);
    } else if (geoJson instanceof FeatureCollection) {
      return bbox((FeatureCollection) geoJson);
    } else if (geoJson instanceof Feature) {
      return bbox((Feature) geoJson);
    } else {
      throw new UnsupportedOperationException("bbox type not supported for GeoJson instance");
    }
  }

  /**
   * Takes a set of features, calculates the bbox of all input features, and returns a bounding box.
   *
   * @param featureCollection a {@link FeatureCollection} object
   * @return a double array defining the bounding box in this order {@code [minX, minY, maxX, maxY]}
   * @since 4.8.0
   */
  public static double[] bbox(FeatureCollection featureCollection) {
    return bboxCalculator(TurfMeta.coordAll(featureCollection, false));
  }

  /**
   * Takes a set of features, calculates the bbox of all input features, and returns a bounding box.
   *
   * @param feature a {@link Feature} object
   * @return a double array defining the bounding box in this order {@code [minX, minY, maxX, maxY]}
   * @since 4.8.0
   */
  public static double[] bbox(Feature feature) {
    return bboxCalculator(TurfMeta.coordAll(feature, false));
  }

  /**
   * Takes an arbitrary {@link Geometry} and calculates a bounding box.
   *
   * @param geometry a {@link Geometry} object
   * @return a double array defining the bounding box in this order {@code [minX, minY, maxX, maxY]}
   * @since 2.0.0
   */
  public static double[] bbox(Geometry geometry) {
    if (geometry instanceof Point) {
      return bbox((Point) geometry);
    } else if (geometry instanceof MultiPoint) {
      return bbox((MultiPoint) geometry);
    } else if (geometry instanceof LineString) {
      return bbox((LineString) geometry);
    } else if (geometry instanceof MultiLineString) {
      return bbox((MultiLineString) geometry);
    } else if (geometry instanceof Polygon) {
      return bbox((Polygon) geometry);
    } else if (geometry instanceof MultiPolygon) {
      return bbox((MultiPolygon) geometry);
    } else if (geometry instanceof GeometryCollection) {
      List<Point> points = new ArrayList<>();
      for (Geometry geo : ((GeometryCollection) geometry).geometries()) {
        // recursive
        double[] bbox = bbox(geo);
        points.add(Point.fromLngLat(bbox[0], bbox[1]));
        points.add(Point.fromLngLat(bbox[2], bbox[1]));
        points.add(Point.fromLngLat(bbox[2], bbox[3]));
        points.add(Point.fromLngLat(bbox[0], bbox[3]));
      }
      return TurfMeasurement.bbox(MultiPoint.fromLngLats(points));
    } else {
      throw new RuntimeException(("Unknown geometry class: " + geometry.getClass()));
    }
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

  /**
   * Takes a {@link BoundingBox} and uses its coordinates to create a {@link Polygon}
   * geometry.
   *
   * @param boundingBox a {@link BoundingBox} object to calculate with
   * @return a {@link Polygon} object
   * @see <a href="http://turfjs.org/docs/#bboxPolygon">Turf BoundingBox Polygon documentation</a>
   * @since 4.7.0
   */
  public static Polygon bboxPolygon(@NonNull BoundingBox boundingBox) {
    return Polygon.fromLngLats(
      Collections.singletonList(
        Arrays.asList(
          Point.fromLngLat(boundingBox.west(), boundingBox.south()),
          Point.fromLngLat(boundingBox.east(), boundingBox.south()),
          Point.fromLngLat(boundingBox.east(), boundingBox.north()),
          Point.fromLngLat(boundingBox.west(), boundingBox.north()),
          Point.fromLngLat(boundingBox.west(), boundingBox.south()))));
  }

  /**
   * Takes a bbox and uses its coordinates to create a {@link Polygon} geometry.
   *
   * @param bbox a double[] object to calculate with
   * @return a {@link Polygon} object
   * @see <a href="http://turfjs.org/docs/#bboxPolygon">Turf BoundingBox Polygon documentation</a>
   * @since 4.9.0
   */
  public static Polygon bboxPolygon(@NonNull double[] bbox) {
    return Polygon.fromLngLats(
      Collections.singletonList(
        Arrays.asList(
          Point.fromLngLat(bbox[0], bbox[1]),
          Point.fromLngLat(bbox[2], bbox[1]),
          Point.fromLngLat(bbox[2], bbox[3]),
          Point.fromLngLat(bbox[0], bbox[3]),
          Point.fromLngLat(bbox[0], bbox[1]))));
  }

  /**
   * Takes any number of features and returns a rectangular Polygon that encompasses all vertices.
   *
   * @param geoJson input features
   * @return a rectangular Polygon feature that encompasses all vertices
   * @since 4.9.0
   */
  public static Polygon envelope(GeoJson geoJson) {
    return bboxPolygon(bbox(geoJson));
  }
}
