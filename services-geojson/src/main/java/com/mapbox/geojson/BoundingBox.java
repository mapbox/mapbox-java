package com.mapbox.geojson;

import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LONGITUDE;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.geojson.constants.GeoJsonConstants;
import com.mapbox.geojson.gson.BoundingBoxDeserializer;
import com.mapbox.geojson.gson.BoundingBoxSerializer;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;

import java.io.Serializable;

/**
 * A GeoJson object MAY have a member named "bbox" to include information on the coordinate range
 * for its Geometries, Features, or FeatureCollections.
 * <p>
 * This class simplifies the build process for creating a bounding box and working with them when
 * deserialized. specific parameter naming helps define which coordinates belong where when a
 * bounding box instance is being created. Note that since GeoJson objects only have the option of
 * including a bounding box JSON element, the {@code bbox} value returned by a GeoJson object might
 * be null.
 * <p>
 * At a minimum, a bounding box will have two {@link Point}s or four coordinates which define the
 * box. A 3rd dimensional bounding box can be produced if elevation or altitude is defined.
 *
 * @since 3.0.0
 */
public class BoundingBox implements Serializable {

  private final Point southwest;

  private final Point northeast;

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a Bounding Box
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static BoundingBox fromJson(String json) {
    Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(GeoJsonAdapterFactory.create())
      .registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer())
      .create();
    return gson.fromJson(json, BoundingBox.class);
  }

  /**
   * Define a new instance of this class by passing in two {@link Point}s, representing both the
   * southwest and northwest corners of the bounding box.
   *
   * @param southwest represents the bottom left corner of the bounding box when the camera is
   *                  pointing due north
   * @param northeast represents the top right corner of the bounding box when the camera is
   *                  pointing due north
   * @return a new instance of this class defined by the provided points
   * @since 3.0.0
   */
  public static BoundingBox fromPoints(@NonNull Point southwest, @NonNull Point northeast) {
    return new BoundingBox(southwest, northeast);
  }

  /**
   * Define a new instance of this class by passing in four coordinates in the same order they would
   * appear in the serialized GeoJson form. Limits are placed on the minimum and maximum coordinate
   * values which can exist and comply with the GeoJson spec.
   *
   * @param west  the left side of the bounding box when the map is facing due north
   * @param south the bottom side of the bounding box when the map is facing due north
   * @param east  the right side of the bounding box when the map is facing due north
   * @param north the top side of the bounding box when the map is facing due north
   * @return a new instance of this class defined by the provided coordinates
   * @since 3.0.0
   * @deprecated As of 3.1.0, use {@link #fromLngLats} instead.
   */

  @Deprecated
  public static BoundingBox fromCoordinates(
    @FloatRange(from = MIN_LONGITUDE, to = GeoJsonConstants.MAX_LONGITUDE) double west,
    @FloatRange(from = MIN_LATITUDE, to = GeoJsonConstants.MAX_LATITUDE) double south,
    @FloatRange(from = MIN_LONGITUDE, to = GeoJsonConstants.MAX_LONGITUDE) double east,
    @FloatRange(from = MIN_LATITUDE, to = GeoJsonConstants.MAX_LATITUDE) double north) {
    return fromLngLats(west, south, east, north);
  }

  /**
   * Define a new instance of this class by passing in four coordinates in the same order they would
   * appear in the serialized GeoJson form. Limits are placed on the minimum and maximum coordinate
   * values which can exist and comply with the GeoJson spec.
   *
   * @param west              the left side of the bounding box when the map is facing due north
   * @param south             the bottom side of the bounding box when the map is facing due north
   * @param southwestAltitude the southwest corner altitude or elevation when the map is facing due
   *                          north
   * @param east              the right side of the bounding box when the map is facing due north
   * @param north             the top side of the bounding box when the map is facing due north
   * @param northEastAltitude the northeast corner altitude or elevation when the map is facing due
   *                          north
   * @return a new instance of this class defined by the provided coordinates
   * @since 3.0.0
   * @deprecated As of 3.1.0, use {@link #fromLngLats} instead.
   * */
  @Deprecated
  public static BoundingBox fromCoordinates(
    @FloatRange(from = MIN_LONGITUDE, to = GeoJsonConstants.MAX_LONGITUDE) double west,
    @FloatRange(from = MIN_LATITUDE, to = GeoJsonConstants.MAX_LATITUDE) double south,
    double southwestAltitude,
    @FloatRange(from = MIN_LONGITUDE, to = GeoJsonConstants.MAX_LONGITUDE) double east,
    @FloatRange(from = MIN_LATITUDE, to = GeoJsonConstants.MAX_LATITUDE) double north,
    double northEastAltitude) {
    return fromLngLats(west, south, southwestAltitude, east, north, northEastAltitude);
  }

  /**
   * Define a new instance of this class by passing in four coordinates in the same order they would
   * appear in the serialized GeoJson form. Limits are placed on the minimum and maximum coordinate
   * values which can exist and comply with the GeoJson spec.
   *
   * @param west  the left side of the bounding box when the map is facing due north
   * @param south the bottom side of the bounding box when the map is facing due north
   * @param east  the right side of the bounding box when the map is facing due north
   * @param north the top side of the bounding box when the map is facing due north
   * @return a new instance of this class defined by the provided coordinates
   * @since 3.1.0
   */
  public static BoundingBox fromLngLats(
    @FloatRange(from = MIN_LONGITUDE, to = GeoJsonConstants.MAX_LONGITUDE) double west,
    @FloatRange(from = MIN_LATITUDE, to = GeoJsonConstants.MAX_LATITUDE) double south,
    @FloatRange(from = MIN_LONGITUDE, to = GeoJsonConstants.MAX_LONGITUDE) double east,
    @FloatRange(from = MIN_LATITUDE, to = GeoJsonConstants.MAX_LATITUDE) double north) {
    return new BoundingBox(Point.fromLngLat(west, south), Point.fromLngLat(east, north));
  }

  /**
   * Define a new instance of this class by passing in four coordinates in the same order they would
   * appear in the serialized GeoJson form. Limits are placed on the minimum and maximum coordinate
   * values which can exist and comply with the GeoJson spec.
   *
   * @param west              the left side of the bounding box when the map is facing due north
   * @param south             the bottom side of the bounding box when the map is facing due north
   * @param southwestAltitude the southwest corner altitude or elevation when the map is facing due
   *                          north
   * @param east              the right side of the bounding box when the map is facing due north
   * @param north             the top side of the bounding box when the map is facing due north
   * @param northEastAltitude the northeast corner altitude or elevation when the map is facing due
   *                          north
   * @return a new instance of this class defined by the provided coordinates
   * @since 3.1.0
   */
  public static BoundingBox fromLngLats(
    @FloatRange(from = MIN_LONGITUDE, to = GeoJsonConstants.MAX_LONGITUDE) double west,
    @FloatRange(from = MIN_LATITUDE, to = GeoJsonConstants.MAX_LATITUDE) double south,
    double southwestAltitude,
    @FloatRange(from = MIN_LONGITUDE, to = GeoJsonConstants.MAX_LONGITUDE) double east,
    @FloatRange(from = MIN_LATITUDE, to = GeoJsonConstants.MAX_LATITUDE) double north,
    double northEastAltitude) {
    return new BoundingBox(
      Point.fromLngLat(west, south, southwestAltitude),
      Point.fromLngLat(east, north, northEastAltitude));
  }

  BoundingBox(Point southwest, Point northeast) {
    if (southwest == null) {
      throw new NullPointerException("Null southwest");
    }
    this.southwest = southwest;
    if (northeast == null) {
      throw new NullPointerException("Null northeast");
    }
    this.northeast = northeast;
  }
  /**
   * Provides the {@link Point} which represents the southwest corner of this bounding box when the
   * map is facing due north.
   *
   * @return a {@link Point} which defines this bounding boxes southwest corner
   * @since 3.0.0
   */
  @NonNull
  public Point southwest() {
    return southwest;
  }

  /**
   * Provides the {@link Point} which represents the northeast corner of this bounding box when the
   * map is facing due north.
   *
   * @return a {@link Point} which defines this bounding boxes northeast corner
   * @since 3.0.0
   */
  @NonNull
  public Point northeast() {
    return northeast;
  }

  /**
   * Convenience method for getting the bounding box most westerly point (longitude) as a double
   * coordinate.
   *
   * @return the most westerly coordinate inside this bounding box
   * @since 3.0.0
   */
  public final double west() {
    return southwest().longitude();
  }

  /**
   * Convenience method for getting the bounding box most southerly point (latitude) as a double
   * coordinate.
   *
   * @return the most southerly coordinate inside this bounding box
   * @since 3.0.0
   */
  public final double south() {
    return southwest().latitude();
  }

  /**
   * Convenience method for getting the bounding box most easterly point (longitude) as a double
   * coordinate.
   *
   * @return the most easterly coordinate inside this bounding box
   * @since 3.0.0
   */
  public final double east() {
    return northeast().longitude();
  }

  /**
   * Convenience method for getting the bounding box most westerly point (longitude) as a double
   * coordinate.
   *
   * @return the most westerly coordinate inside this bounding box
   * @since 3.0.0
   */
  public final double north() {
    return northeast().latitude();
  }

  /**
   * Gson TYPE adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<BoundingBox> typeAdapter(Gson gson) {
    return null; //new BoundingBox.GsonTypeAdapter(gson);
  }

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this Bounding box
   * @since 3.0.0
   */
  public final String toJson() {
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(BoundingBox.class, new BoundingBoxSerializer())
      .create();
    return gson.toJson(this, BoundingBox.class);
  }

  @Override
  public String toString() {
    return "BoundingBox{"
            + "southwest=" + southwest + ", "
            + "northeast=" + northeast
            + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof BoundingBox) {
      BoundingBox that = (BoundingBox) o;
      return (this.southwest.equals(that.southwest()))
              && (this.northeast.equals(that.northeast()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= southwest.hashCode();
    h$ *= 1000003;
    h$ ^= northeast.hashCode();
    return h$;
  }
}
