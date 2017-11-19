package com.mapbox.geojson;

import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LONGITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LONGITUDE;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.gson.BoundingBoxDeserializer;
import com.mapbox.geojson.gson.BoundingBoxSerializer;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.geojson.gson.PointSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A point represents a single geographic position and is one of the seven Geometries found in the
 * GeoJson spec.
 * <p>
 * This adheres to the RFC 7946 internet standard when serialized into JSON. When deserialized, this
 * class becomes an immutable object which should be initiated using its static factory methods.
 * </p><p>
 * Coordinates are in x, y order (easting, northing for projected coordinates), longitude, and
 * latitude for geographic coordinates), precisely in that order and using double values. Altitude
 * or elevation MAY be included as an optional third parameter while creating this object.
 * <p>
 * The size of a GeoJson text in bytes is a major interoperability consideration, and precision of
 * coordinate values has a large impact on the size of texts when serialized. For geographic
 * coordinates with units of degrees, 6 decimal places (a default common in, e.g., sprintf) amounts
 * to about 10 centimeters, a precision well within that of current GPS systems. Implementations
 * should consider the cost of using a greater precision than necessary.
 * <p>
 * Furthermore, pertaining to altitude, the WGS 84 datum is a relatively coarse approximation of the
 * geoid, with the height varying by up to 5 m (but generally between 2 and 3 meters) higher or
 * lower relative to a surface parallel to Earth's mean sea level.
 * <p>
 * A sample GeoJson Point's provided below (in it's serialized state).
 * <pre>
 * {
 *   "TYPE": "Point",
 *   "coordinates": [100.0, 0.0]
 * }
 * </pre>
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class Point implements Geometry<List<Double>>, Serializable {

  @Expose
  @SerializedName("type")
  private static final String TYPE = "Point";

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a Point object from scratch it is better to use one of the other provided static
   * factory methods such as {@link #fromLngLat(double, double)}. Longitude values should not exceed
   * the spec defined -180 to 180 range and latitude's limit of -90 to 90. While no limit is placed
   * on decimal precision, for performance reasons when serializing and deserializing it is
   * suggested to limit decimal precision to within 6 decimal places.
   *
   * @param json a formatted valid JSON string defining a GeoJson Point
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static Point fromJson(@NonNull String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer());
    return gson.create().fromJson(json, Point.class);
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
   *   method
   * @since 3.0.0
   */
  public static Point fromLngLat(
    @FloatRange(from = MIN_LONGITUDE, to = MAX_LONGITUDE) double longitude,
    @FloatRange(from = MIN_LATITUDE, to = MAX_LATITUDE) double latitude) {
    List<Double> coordinates = new ArrayList<>();
    coordinates.add(longitude);
    coordinates.add(latitude);
    return new AutoValue_Point(null, coordinates);
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
   * @param bbox      optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Point fromLngLat(
    @FloatRange(from = MIN_LONGITUDE, to = MAX_LONGITUDE) double longitude,
    @FloatRange(from = MIN_LATITUDE, to = MAX_LATITUDE) double latitude,
    @Nullable BoundingBox bbox) {
    List<Double> coordinates = new ArrayList<>();
    coordinates.add(longitude);
    coordinates.add(latitude);
    return new AutoValue_Point(bbox, coordinates);
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
   *   method
   * @since 3.0.0
   */
  public static Point fromLngLat(
    @FloatRange(from = MIN_LONGITUDE, to = MAX_LONGITUDE) double longitude,
    @FloatRange(from = MIN_LATITUDE, to = MAX_LATITUDE) double latitude,
    double altitude) {
    List<Double> coordinates = new ArrayList<>();
    coordinates.add(longitude);
    coordinates.add(latitude);
    coordinates.add(altitude);
    return new AutoValue_Point(null, coordinates);
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
   * @param bbox      optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Point fromLngLat(
    @FloatRange(from = MIN_LONGITUDE, to = MAX_LONGITUDE) double longitude,
    @FloatRange(from = MIN_LATITUDE, to = MAX_LATITUDE) double latitude,
    double altitude, @Nullable BoundingBox bbox) {
    List<Double> coordinates = new ArrayList<>();
    coordinates.add(longitude);
    coordinates.add(latitude);
    coordinates.add(altitude);
    return new AutoValue_Point(bbox, coordinates);
  }

  /**
   * This returns a double value ranging from -180 to 180 representing the x or easting position of
   * this point. ideally, this value would be restricted to 6 decimal places to correctly follow the
   * GeoJson spec.
   *
   * @return a double value ranging from -180 to 180 representing the x or easting position of this
   *   point
   * @since 3.0.0
   */
  public double longitude() {
    return coordinates().get(0);
  }

  /**
   * This returns a double value ranging from -90 to 90 representing the y or northing position of
   * this point. ideally, this value would be restricted to 6 decimal places to correctly follow the
   * GeoJson spec.
   *
   * @return a double value ranging from -90 to 90 representing the y or northing position of this
   *   point
   * @since 3.0.0
   */
  public double latitude() {
    return coordinates().get(1);
  }

  /**
   * Optionally, the coordinate spec in GeoJson allows for altitude values to be placed inside the
   * coordinate array. {@link #hasAltitude()} can be used to determine if this value was set during
   * initialization of this Point instance. This double value should only be used to represent
   * either the elevation or altitude value at this particular point.
   *
   * @return a double value ranging from negative to positive infinity
   * @since 3.0.0
   */
  public double altitude() {
    if (coordinates().size() < 3) {
      return Double.NaN;
    }
    return coordinates().get(2);
  }

  /**
   * Optionally, the coordinate spec in GeoJson allows for altitude values to be placed inside the
   * coordinate array. If an altitude value was provided while initializing this instance, this will
   * return true.
   *
   * @return true if this instance of point contains an altitude value
   * @since 3.0.0
   */
  public boolean hasAltitude() {
    return !Double.isNaN(altitude());
  }

  /**
   * This describes the TYPE of GeoJson geometry this object is, thus this will always return
   * {@link Point}.
   *
   * @return a String which describes the TYPE of geometry, for this object it will always return
   *   {@code Point}
   * @since 1.0.0
   */
  @NonNull
  @Override
  public String type() {
    return TYPE;
  }

  /**
   * A Feature Collection might have a member named {@code bbox} to include information on the
   * coordinate range for it's {@link Feature}s. The value of the bbox member MUST be a list of
   * size 2*n where n is the number of dimensions represented in the contained feature geometries,
   * with all axes of the most southwesterly point followed by all axes of the more northeasterly
   * point. The axes order of a bbox follows the axes order of geometries.
   *
   * @return a list of double coordinate values describing a bounding box
   * @since 3.0.0
   */
  @Nullable
  @Override
  public abstract BoundingBox bbox();

  /**
   * Provide a single double array containing the longitude, latitude, and optionally an
   * altitude/elevation. {@link #longitude()}, {@link #latitude()}, and {@link #altitude()} are all
   * avaliable which make getting specific coordinates more direct.
   *
   * @return a double array which holds this points coordinates
   * @since 3.0.0
   */
  @NonNull
  @Override
  public abstract List<Double> coordinates();

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this Point geometry
   * @since 1.0.0
   */
  @Override
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Point.class, new PointSerializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxSerializer());
    gson.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
    return gson.create().toJson(this);
  }

  /**
   * Gson TYPE adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<Point> typeAdapter(Gson gson) {
    return new AutoValue_Point.GsonTypeAdapter(gson);
  }
}
