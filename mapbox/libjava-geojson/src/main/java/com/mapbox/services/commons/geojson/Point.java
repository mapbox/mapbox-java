package com.mapbox.services.commons.geojson;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * A point represents a single geographic position and is one of the seven Geometries found in the
 * GeoJSON spec.
 * <p>
 * This adheres to the RFC 7946 internet standard when serialized into JSON. When deserialized, this
 * class becomes an immutable object which should be initiated using its static factory methods.
 * </p><p>
 * Coordinates are in x, y order (easting, northing for projected coordinates), longitude, and
 * latitude for geographic coordinates), precisely in that order and using double values. Altitude
 * or elevation MAY be included as an optional third parameter while creating this object.
 * <p></p>
 * The size of a GeoJSON text in bytes is a major interoperability consideration, and precision of
 * coordinate values has a large impact on the size of texts when serialized. For geographic
 * coordinates with units of degrees, 6 decimal places (a default common in, e.g., sprintf) amounts
 * to about 10 centimeters, a precision well within that of current GPS systems. Implementations
 * should consider the cost of using a greater precision than necessary.
 * <p>
 * Furthermore, pertaining to altitude, the WGS 84 datum is a relatively coarse approximation of the
 * geoid, with the height varying by up to 5 m (but generally between 2 and 3 meters) higher or
 * lower relative to a surface parallel to Earth's mean sea level.
 * <p></p>
 * A sample GeoJSON Point's provided below (in it's serialized state).
 * <pre>
 * {
 *   "type": "Point",
 *   "coordinates": [100.0, 0.0]
 * }
 * </pre>
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class Point implements Geometry, Serializable {

  private static final String type = "Point";

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a Point object from scratch it is better to use one of the other provided static
   * factory methods such as {@link #fromLngLat(double, double)}. Longitude values should not exceed
   * the spec defined -180 to 180 range and latitude's limit of -90 to 90. While no limit is placed
   * on decimal precision, for performance reasons when serializing and deserializing it is
   * suggested to limit decimal precision to within 6 decimal places.
   *
   * @param json a formatted valid JSON string defining a GeoJSON Point
   * @return a new instance of this class defined by the values passed inside this static factory
   * method
   * @since 1.0.0
   */
  public static Point fromJson(@NonNull String json) {
    return new Gson().fromJson(json, Point.class);
  }

  /**
   * Create a new instance of this class defining a longitude and latitude value in that respective
   * order. Longitude values are limited to a -180 to 180 range and latitude's limited to -90 to 90
   * as the spec defines. While no limit is placed on decimal precision, for performance reasons
   * when serializing and deserializing it is suggested to limit decimal precision to within 6
   * decimal places.
   *
   * @param longitude a double value between -180 to 180 representing the x position of this point
   * @param latitude  a double value between -90 to 90 representing the y position of this point
   * @return a new instance of this class defined by the values passed inside this static factory
   * method
   * @since 3.0.0
   */
  public static Point fromLngLat(@FloatRange(from = -180, to = 180) double longitude,
                                 @FloatRange(from = -90, to = 90) double latitude) {
    return new AutoValue_Point(longitude, latitude, 0d, false);
  }

  /**
   * Create a new instance of this class defining a longitude and latitude value in that respective
   * order. Longitude values are limited to a -180 to 180 range and latitude's limited to -90 to 90
   * as the spec defines. While no limit is placed on decimal precision, for performance reasons
   * when serializing and deserializing it is suggested to limit decimal precision to within 6
   * decimal places. An optional altitude value can be passed in and can vary between negative
   * infinity and positive infinity.
   *
   * @param longitude a double value between -180 to 180 representing the x position of this point
   * @param latitude  a double value between -90 to 90 representing the y position of this point
   * @param altitude  a double value which can be negative or positive infinity representing either
   *                  elevation or altitude
   * @return a new instance of this class defined by the values passed inside this static factory
   * method
   * @since 3.0.0
   */
  public static Point fromLngLat(@FloatRange(from = -180, to = 180) double longitude,
                                 @FloatRange(from = -90, to = 90) double latitude,
                                 double altitude) {
    return new AutoValue_Point(longitude, latitude, altitude, true);
  }

  /**
   * This returns a double value ranging from -180 to 180 representing the x or easting position of
   * this point. ideally, this value would be restricted to 6 decimal places to correctly follow the
   * GeoJSON spec.
   *
   * @return a double value ranging from -180 to 180 representing the x or easting position of this
   * point
   * @since 3.0.0
   */
  public abstract double longitude();

  /**
   * This returns a double value ranging from -90 to 90 representing the y or northing position of
   * this point. ideally, this value would be restricted to 6 decimal places to correctly follow the
   * GeoJSON spec.
   *
   * @return a double value ranging from -90 to 90 representing the y or northing position of this
   * point
   * @since 3.0.0
   */
  public abstract double latitude();

  /**
   * Optionally, the coordinate spec in GeoJSON allows for altitude values to be placed inside the
   * coordinate array. {@link #hasAltitude()} can be used to determine if this value was set during
   * initialization of this Point instance. This double value should only be used to represent
   * either the elevation or altitude value at this particular point.
   *
   * @return a double value ranging from negative to positive infinity
   * @since 3.0.0
   */
  public abstract double altitude();

  /**
   * Optionally, the coordinate spec in GeoJSON allows for altitude values to be placed inside the
   * coordinate array. If an altitude value was provided while initializing this instance, this will
   * return true.
   *
   * @return true if this instance of point contains an altitude value
   * @since 3.0.0
   */
  public abstract boolean hasAltitude();

  /**
   * A bounding box representing the region which this geometry falls into. For a Point, this is
   * defined as a bounding box considering only a single coordinate. Thus the longitude values and
   * latitude values will equal each other. The axes order of a bbox follows the axes order of
   * geometries.
   * <p>
   * <p>
   * TODO define a bbox object rather than a double array west, south, east, north
   *
   * @return
   * @since 3.0.0
   */
  public double[] bbox() {
    return new double[] {longitude(), latitude(), longitude(), latitude()};
  }

  @Override
  public double[] coordinates() {
    return new double[] {longitude(), latitude()};
  }

  @Override
  public String type() {
    return type;
  }

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJSON
   * string.
   *
   * @return a JSON string which represents this Point geometry
   * @since 1.0.0
   */
  public String toJson() {
    return new Gson().toJson(this);
  }
}