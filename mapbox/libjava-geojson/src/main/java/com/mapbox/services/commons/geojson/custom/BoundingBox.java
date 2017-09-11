package com.mapbox.services.commons.geojson.custom;

import static com.mapbox.services.commons.geojson.custom.Constants.MAX_LATITUDE;
import static com.mapbox.services.commons.geojson.custom.Constants.MAX_LONGITUDE;
import static com.mapbox.services.commons.geojson.custom.Constants.MIN_LATITUDE;
import static com.mapbox.services.commons.geojson.custom.Constants.MIN_LONGITUDE;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.Point;

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
@AutoValue
public abstract class BoundingBox implements Serializable {

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
    return new AutoValue_BoundingBox(southwest, northeast);
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
   */
  public static BoundingBox fromCoordinates(
    @FloatRange(from = MIN_LONGITUDE, to = MAX_LONGITUDE) double west,
    @FloatRange(from = MIN_LATITUDE, to = MAX_LATITUDE) double south,
    @FloatRange(from = MIN_LONGITUDE, to = MAX_LONGITUDE) double east,
    @FloatRange(from = MIN_LATITUDE, to = MAX_LATITUDE) double north) {
    return new AutoValue_BoundingBox(Point.fromLngLat(west, south), Point.fromLngLat(east, north));
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
   */
  public static BoundingBox fromCoordinates(
    @FloatRange(from = MIN_LONGITUDE, to = MAX_LONGITUDE) double west,
    @FloatRange(from = MIN_LATITUDE, to = MAX_LATITUDE) double south,
    double southwestAltitude,
    @FloatRange(from = MIN_LONGITUDE, to = MAX_LONGITUDE) double east,
    @FloatRange(from = MIN_LATITUDE, to = MAX_LATITUDE) double north,
    double northEastAltitude) {
    return new AutoValue_BoundingBox(
      Point.fromLngLat(west, south, southwestAltitude),
      Point.fromLngLat(east, north, northEastAltitude));
  }

  /**
   * Provides the {@link Point} which represents the southwest corner of this bounding box when the
   * map is facing due north.
   *
   * @return a {@link Point} which defines this bounding boxes southwest corner
   * @since 3.0.0
   */
  @NonNull
  public abstract Point southwest();

  /**
   * Provides the {@link Point} which represents the northeast corner of this bounding box when the
   * map is facing due north.
   *
   * @return a {@link Point} which defines this bounding boxes northeast corner
   * @since 3.0.0
   */
  @NonNull
  public abstract Point northeast();

  /**
   * Convenience method for getting the bounding box most westerly point (longitude) as a double
   * coordinate.
   *
   * @return the most westerly coordinate inside this bounding box
   * @since 3.0.0
   */
  public double west() {
    return southwest().longitude();
  }

  /**
   * Convenience method for getting the bounding box most southerly point (latitude) as a double
   * coordinate.
   *
   * @return the most southerly coordinate inside this bounding box
   * @since 3.0.0
   */
  public double south() {
    return southwest().latitude();
  }

  /**
   * Convenience method for getting the bounding box most easterly point (longitude) as a double
   * coordinate.
   *
   * @return the most easterly coordinate inside this bounding box
   * @since 3.0.0
   */
  public double east() {
    return northeast().longitude();
  }

  /**
   * Convenience method for getting the bounding box most westerly point (longitude) as a double
   * coordinate.
   *
   * @return the most westerly coordinate inside this bounding box
   * @since 3.0.0
   */
  public double north() {
    return northeast().latitude();
  }

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this Bounding box
   * @since 3.0.0
   */
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(MapboxAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointSerializer());
    return gson.create().toJson(this);
  }
}
