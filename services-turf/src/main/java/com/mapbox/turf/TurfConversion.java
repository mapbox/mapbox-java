package com.mapbox.turf;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonObject;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.turf.TurfConstants.TurfUnitCriteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is made up of methods that take in an object, convert it, and then return the object
 * in the desired units or object.
 *
 * @see <a href="http://turfjs.org/docs/">Turfjs documentation</a>
 * @since 1.2.0
 */
public final class TurfConversion {

  private static final Map<String, Double> FACTORS;

  static {
    FACTORS = new HashMap<>();
    FACTORS.put(TurfConstants.UNIT_MILES, 3960d);
    FACTORS.put(TurfConstants.UNIT_NAUTICAL_MILES, 3441.145d);
    FACTORS.put(TurfConstants.UNIT_DEGREES, 57.2957795d);
    FACTORS.put(TurfConstants.UNIT_RADIANS, 1d);
    FACTORS.put(TurfConstants.UNIT_INCHES, 250905600d);
    FACTORS.put(TurfConstants.UNIT_YARDS, 6969600d);
    FACTORS.put(TurfConstants.UNIT_METERS, 6373000d);
    FACTORS.put(TurfConstants.UNIT_CENTIMETERS, 6.373e+8d);
    FACTORS.put(TurfConstants.UNIT_KILOMETERS, 6373d);
    FACTORS.put(TurfConstants.UNIT_FEET, 20908792.65d);
    FACTORS.put(TurfConstants.UNIT_CENTIMETRES, 6.373e+8d);
    FACTORS.put(TurfConstants.UNIT_METRES, 6373000d);
    FACTORS.put(TurfConstants.UNIT_KILOMETRES, 6373d);
  }

  private TurfConversion() {
    // Private constructor preventing initialization of this class
  }

  /**
   * Convert a distance measurement (assuming a spherical Earth) from a real-world unit into degrees
   * Valid units: miles, nauticalmiles, inches, yards, meters, metres, centimeters, kilometres,
   * feet.
   *
   * @param distance in real units
   * @param units    can be degrees, radians, miles, or kilometers inches, yards, metres, meters,
   *                 kilometres, kilometers.
   * @return a double value representing the distance in degrees
   * @since 3.0.0
   */
  public static double lengthToDegrees(double distance, @TurfUnitCriteria String units) {
    return radiansToDegrees(lengthToRadians(distance, units));
  }

  /**
   * Converts an angle in degrees to radians.
   *
   * @param degrees angle between 0 and 360 degrees
   * @return angle in radians
   * @since 3.1.0
   */
  public static double degreesToRadians(double degrees) {
    double radians = degrees % 360;
    return radians * Math.PI / 180;
  }

  /**
   * Converts an angle in radians to degrees.
   *
   * @param radians angle in radians
   * @return degrees between 0 and 360 degrees
   * @since 3.0.0
   */
  public static double radiansToDegrees(double radians) {
    double degrees = radians % (2 * Math.PI);
    return degrees * 180 / Math.PI;
  }

  /**
   * Convert a distance measurement (assuming a spherical Earth) from radians to a more friendly
   * unit. The units used here equals the default.
   *
   * @param radians a double using unit radian
   * @return converted radian to distance value
   * @since 1.2.0
   */
  public static double radiansToLength(double radians) {
    return radiansToLength(radians, TurfConstants.UNIT_DEFAULT);
  }

  /**
   * Convert a distance measurement (assuming a spherical Earth) from radians to a more friendly
   * unit.
   *
   * @param radians a double using unit radian
   * @param units   pass in one of the units defined in {@link TurfUnitCriteria}
   * @return converted radian to distance value
   * @since 1.2.0
   */
  public static double radiansToLength(double radians, @NonNull @TurfUnitCriteria String units) {
    return radians * FACTORS.get(units);
  }

  /**
   * Convert a distance measurement (assuming a spherical Earth) from a real-world unit into
   * radians.
   *
   * @param distance double representing a distance value assuming the distance units is in
   *                 kilometers
   * @return converted distance to radians value
   * @since 1.2.0
   */
  public static double lengthToRadians(double distance) {
    return lengthToRadians(distance, TurfConstants.UNIT_DEFAULT);
  }

  /**
   * Convert a distance measurement (assuming a spherical Earth) from a real-world unit into
   * radians.
   *
   * @param distance double representing a distance value
   * @param units    pass in one of the units defined in {@link TurfUnitCriteria}
   * @return converted distance to radians value
   * @since 1.2.0
   */
  public static double lengthToRadians(double distance, @NonNull @TurfUnitCriteria String units) {
    return distance / FACTORS.get(units);
  }

  /**
   * Converts a distance to the default units. Use
   * {@link TurfConversion#convertLength(double, String, String)} to specify a unit to convert to.
   *
   * @param distance     double representing a distance value
   * @param originalUnit of the distance, must be one of the units defined in
   *                     {@link TurfUnitCriteria}
   * @return converted distance in the default unit
   * @since 2.2.0
   */
  public static double convertLength(@FloatRange(from = 0) double distance,
                                     @NonNull @TurfUnitCriteria String originalUnit) {
    return convertLength(distance, originalUnit, TurfConstants.UNIT_DEFAULT);
  }

  /**
   * Converts a distance to a different unit specified.
   *
   * @param distance     the distance to be converted
   * @param originalUnit of the distance, must be one of the units defined in
   *                     {@link TurfUnitCriteria}
   * @param finalUnit    returned unit, {@link TurfConstants#UNIT_DEFAULT} if not specified
   * @return the converted distance
   * @since 2.2.0
   */
  public static double convertLength(@FloatRange(from = 0) double distance,
                                     @NonNull @TurfUnitCriteria String originalUnit,
                                     @Nullable @TurfUnitCriteria String finalUnit) {
    if (finalUnit == null) {
      finalUnit = TurfConstants.UNIT_DEFAULT;
    }
    return radiansToLength(lengthToRadians(distance, originalUnit), finalUnit);
  }

  /**
   * Takes a {@link FeatureCollection} and
   * returns all positions as {@link Point} objects.
   *
   * @param featureCollection a {@link FeatureCollection} object
   * @return a new {@link FeatureCollection} object with {@link Point} objects
   * @since 4.8.0
   */
  public static FeatureCollection explode(@NonNull FeatureCollection featureCollection) {
    List<Feature> finalFeatureList = new ArrayList<>();
    for (Point singlePoint : TurfMeta.coordAll(featureCollection, true)) {
      finalFeatureList.add(Feature.fromGeometry(singlePoint));
    }
    return FeatureCollection.fromFeatures(finalFeatureList);
  }

  /**
   * Takes a {@link Feature}  and
   * returns its position as a {@link Point} objects.
   *
   * @param feature a {@link Feature} object
   * @return a new {@link FeatureCollection} object with {@link Point} objects
   * @since 4.8.0
   */
  public static FeatureCollection explode(@NonNull Feature feature) {
    List<Feature> finalFeatureList = new ArrayList<>();
    for (Point singlePoint : TurfMeta.coordAll(feature, true)) {
      finalFeatureList.add(Feature.fromGeometry(singlePoint));
    }
    return FeatureCollection.fromFeatures(finalFeatureList);
  }

  /**
   * Takes a {@link Feature} that contains {@link Polygon} and
   * covert it to a {@link Feature} that contains {@link LineString} or {@link MultiLineString}.
   *
   * @param feature a {@link Feature} object that contains {@link Polygon}
   * @return  a {@link Feature} object that contains {@link LineString} or {@link MultiLineString}
   * @since 4.9.0
   */
  public static Feature polygonToLine(@NonNull Feature feature) {
    return polygonToLine(feature, null);
  }

  /**
   * Takes a {@link Feature} that contains {@link Polygon} and a properties {@link JsonObject} and
   * covert it to a {@link Feature} that contains {@link LineString} or {@link MultiLineString}.
   *
   * @param feature a {@link Feature} object that contains {@link Polygon}
   * @param properties a {@link JsonObject} that represents a feature's properties
   * @return  a {@link Feature} object that contains {@link LineString} or {@link MultiLineString}
   * @since 4.9.0
   */
  public static Feature polygonToLine(@NonNull Feature feature, @Nullable JsonObject properties) {
    Geometry geometry = feature.geometry();
    if (geometry instanceof Polygon) {
      return polygonToLine((Polygon) geometry,properties != null ? properties :
              feature.type().equals("Feature") ? feature.properties() : new JsonObject());
    }
    throw new TurfException("Feature's geometry must be Polygon");
  }

  /**
   * Takes a {@link Polygon} and
   * covert it to a {@link Feature} that contains {@link LineString} or {@link MultiLineString}.
   *
   * @param polygon a {@link Polygon} object
   * @return  a {@link Feature} object that contains {@link LineString} or {@link MultiLineString}
   * @since 4.9.0
   */
  public static Feature polygonToLine(@NonNull Polygon polygon) {
    return polygonToLine(polygon, null);
  }

  /**
   * Takes a {@link MultiPolygon} and
   * covert it to a {@link FeatureCollection} that contains list
   * of {@link Feature} of {@link LineString} or {@link MultiLineString}.
   *
   * @param multiPolygon a {@link MultiPolygon} object
   * @return  a {@link FeatureCollection} object that contains
   *   list of {@link Feature} of {@link LineString} or {@link MultiLineString}
   * @since 4.9.0
   */
  public static FeatureCollection polygonToLine(@NonNull MultiPolygon multiPolygon) {
    return polygonToLine(multiPolygon, null);
  }

  /**
   * Takes a {@link Polygon} and a properties {@link JsonObject} and
   * covert it to a {@link Feature} that contains {@link LineString} or {@link MultiLineString}.
   *
   * @param polygon a {@link Polygon} object
   * @param properties a {@link JsonObject} that represents a feature's properties
   * @return  a {@link Feature} object that contains {@link LineString} or {@link MultiLineString}
   * @since 4.9.0
   */
  public static Feature polygonToLine(@NonNull Polygon polygon, @Nullable JsonObject properties) {
    return coordsToLine(polygon.coordinates(), properties);
  }

  /**
   * Takes a {@link MultiPolygon} and a properties {@link JsonObject} and
   * covert it to a {@link FeatureCollection} that contains list
   * of {@link Feature} of {@link LineString} or {@link MultiLineString}.
   *
   * @param multiPolygon a {@link MultiPolygon} object
   * @param properties a {@link JsonObject} that represents a feature's properties
   * @return  a {@link FeatureCollection} object that contains
   *   list of {@link Feature} of {@link LineString} or {@link MultiLineString}
   * @since 4.9.0
   */
  public static FeatureCollection polygonToLine(@NonNull MultiPolygon multiPolygon,
                                                @Nullable JsonObject properties) {
    List<List<List<Point>>> coordinates = multiPolygon.coordinates();
    List<Feature> finalFeatureList = new ArrayList<>();
    for (List<List<Point>> polygonCoordinates : coordinates) {
      finalFeatureList.add(coordsToLine(polygonCoordinates, properties));
    }
    return FeatureCollection.fromFeatures(finalFeatureList);
  }

  /**
   * Takes a {@link Feature} that contains {@link MultiPolygon} and
   * covert it to a {@link FeatureCollection} that contains list of {@link Feature}
   * of {@link LineString} or {@link MultiLineString}.
   *
   * @param feature a {@link Feature} object that contains {@link Polygon}
   * @return  a {@link FeatureCollection} object that contains list of {@link Feature}
   *   of {@link LineString} or {@link MultiLineString}
   * @since 4.9.0
   */
  public static FeatureCollection multiPolygonToLine(@NonNull Feature feature) {
    return multiPolygonToLine(feature, null);
  }

  /**
   * Takes a {@link Feature} that contains {@link MultiPolygon} and a
   * properties {@link JsonObject} and
   * covert it to a {@link FeatureCollection} that contains
   * list of {@link Feature} of {@link LineString} or {@link MultiLineString}.
   *
   * @param feature a {@link Feature} object that contains {@link MultiPolygon}
   * @param properties a {@link JsonObject} that represents a feature's properties
   * @return  a {@link FeatureCollection} object that contains
   *   list of {@link Feature} of {@link LineString} or {@link MultiLineString}
   * @since 4.9.0
   */
  public static FeatureCollection multiPolygonToLine(@NonNull Feature feature,
                                                     @Nullable JsonObject properties) {
    Geometry geometry = feature.geometry();
    if (geometry instanceof MultiPolygon) {
      return polygonToLine((MultiPolygon) geometry, properties != null ? properties :
              feature.type().equals("Feature") ? feature.properties() : new JsonObject());
    }
    throw new TurfException("Feature's geometry must be MultiPolygon");
  }

  @Nullable
  private static Feature coordsToLine(@NonNull List<List<Point>> coordinates,
                                      @Nullable JsonObject properties) {
    if (coordinates.size() > 1) {
      return Feature.fromGeometry(MultiLineString.fromLngLats(coordinates), properties);
    } else if (coordinates.size() == 1) {
      LineString lineString = LineString.fromLngLats(coordinates.get(0));
      return Feature.fromGeometry(lineString, properties);
    }
    return null;
  }

  /**
   * Combines a FeatureCollection of geometries and returns
   * a {@link FeatureCollection} with "Multi-" geometries in it.
   * If the original FeatureCollection parameter has {@link Point}(s)
   * and/or {@link MultiPoint}s), the returned
   * FeatureCollection will include a {@link MultiPoint} object.
   *
   * If the original FeatureCollection parameter has
   * {@link LineString}(s) and/or {@link MultiLineString}s), the returned
   * FeatureCollection will include a {@link MultiLineString} object.
   *
   * If the original FeatureCollection parameter has
   * {@link Polygon}(s) and/or {@link MultiPolygon}s), the returned
   * FeatureCollection will include a {@link MultiPolygon} object.
   *
   * @param originalFeatureCollection a {@link FeatureCollection}
   *
   * @return a {@link FeatureCollection} with a "Multi-" geometry
   *    or "Multi-" geometries.
   *
   * @since 4.10.0
   **/
  public static FeatureCollection combine(@NonNull FeatureCollection originalFeatureCollection) {
    if (originalFeatureCollection.features() == null) {
      throw new TurfException("Your FeatureCollection is null.");
    } else if (originalFeatureCollection.features().size() == 0) {
      throw new TurfException("Your FeatureCollection doesn't have any Feature objects in it.");
    }
    List<Point> pointList = new ArrayList<>(0);
    List<LineString> lineStringList = new ArrayList<>(0);
    List<Polygon> polygonList = new ArrayList<>(0);
    for (Feature singleFeature : originalFeatureCollection.features()) {
      Geometry singleFeatureGeometry = singleFeature.geometry();
      if (singleFeatureGeometry instanceof Point || singleFeatureGeometry instanceof MultiPoint) {
        if (singleFeatureGeometry instanceof Point) {
          pointList.add((Point) singleFeatureGeometry);
        } else {
          pointList.addAll(((MultiPoint) singleFeatureGeometry).coordinates());
        }
      } else if (singleFeatureGeometry instanceof LineString || singleFeatureGeometry
        instanceof MultiLineString) {
        if (singleFeatureGeometry instanceof LineString) {
          lineStringList.add((LineString) singleFeatureGeometry);
        } else {
          lineStringList.addAll(((MultiLineString) singleFeatureGeometry).lineStrings());
        }
      } else if (singleFeatureGeometry instanceof Polygon || singleFeatureGeometry
        instanceof MultiPolygon) {
        if (singleFeatureGeometry instanceof Polygon) {
          polygonList.add((Polygon) singleFeatureGeometry);
        } else {
          polygonList.addAll(((MultiPolygon) singleFeatureGeometry).polygons());
        }
      }
    }
    List<Feature> finalFeatureList = new ArrayList<>(0);
    if (!pointList.isEmpty()) {
      finalFeatureList.add(Feature.fromGeometry(MultiPoint.fromLngLats(pointList)));
    }
    if (!lineStringList.isEmpty()) {
      finalFeatureList.add(Feature.fromGeometry(MultiLineString.fromLineStrings(lineStringList)));
    }
    if (!polygonList.isEmpty()) {
      finalFeatureList.add(Feature.fromGeometry(MultiPolygon.fromPolygons(polygonList)));
    }
    return finalFeatureList.isEmpty() ? originalFeatureCollection
      : FeatureCollection.fromFeatures(finalFeatureList);
  }
}
