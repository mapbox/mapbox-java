package com.mapbox.api.geocoding.v5.models;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.geocoding.v5.RoutingDestinationTypeAdapter;
import com.mapbox.geojson.Point;

import java.util.Arrays;
import java.util.List;

/**
 * A latitude/longitude that's a recommended location to use a destination when
 * navigating to a certain place of interest (POI). This class is part of a
 * {@link CarmenFeature} within the Mapbox Geocoding API {@link GeocodingResponse}.
 *
 * @since 4.10.0
 */
public class RoutableDestination {

  @NonNull
  private final List<Double> coordinates;


  RoutableDestination(List<Double> coordinates) {
    if (coordinates == null || coordinates.size() == 0) {
      throw new NullPointerException("Null coordinates for RoutableDestination");
    }
    this.coordinates = coordinates;
  }

  /**
   * Create a {@link RoutingInfo} object with a list of {@link Point}s.
   *
   * @param singlePoint the {@link Point} whose coordinates will be
   *                    used to create a RoutableDestination object.
   * @return a RoutingInfo object.
   * @since 4.10.0
   */
  public static RoutableDestination fromPoint(@NonNull Point singlePoint) {
    return new RoutableDestination(Arrays.asList(singlePoint.longitude(), singlePoint.latitude()));
  }

  /**
   * Provides a single double array containing the longitude and latitude.
   * {@link #longitude()} and {@link #latitude()} are all available which
   * make getting specific coordinates more direct.
   *
   * @return a double array which holds this Point's coordinates
   * @since 4.10.0
   */
  @NonNull
  public List<Double> coordinates()  {
    return coordinates;
  }

  /**
   * This returns a double value ranging from -180 to 180 representing the x or
   * easting position of this point. ideally, this value would be restricted to 6
   * decimal places to correctly follow the GeoJson spec.
   *
   * @return a double value ranging from -180 to 180 representing the x or easting\
    position of this point.
   * @since 4.10.0
   */
  public double longitude() {
    return coordinates().get(0);
  }

  /**
   * This returns a double value ranging from -90 to 90 representing the y or
   * northing position of this point. ideally, this value would be restricted to
   * 6 decimal places to correctly follow the GeoJson spec.
   *
   * @return a double value ranging from -90 to 90 representing the y or northing
    position of this point.
   * @since 4.10.0
   */
  public double latitude() {
    return coordinates().get(1);
  }

  /**
   * Convenience method to convert this {@link RoutableDestination} object's
   * coordinates into a {@link Point} for
   * easier usage with the rest of the Maps SDK for Android.
   *
   * @return a {@link Point} object that has the same coordinates as the
   * {@link RoutableDestination}.
   */
  public Point toPoint() {
    return Point.fromLngLat(longitude(), latitude());
  }

  @Override
  public String toString() {
    return "RoutableDestination [coordinates = " + coordinates.toString() + "]";
  }

  /**
   * Gson TYPE adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   * @since 4.10.0
   */
  public static TypeAdapter<RoutableDestination> typeAdapter(Gson gson) {
    return new RoutingDestinationTypeAdapter();
  }

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this Bounding box
   * @since 4.10.0
   */
  public final String toJson() {
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(RoutableDestination.class, new RoutingDestinationTypeAdapter())
      .create();
    return gson.toJson(this, RoutableDestination.class);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a Bounding Box
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static RoutableDestination fromJson(String json) {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(RoutableDestination.class, new RoutingDestinationTypeAdapter())
        .create();
    return gson.fromJson(json, RoutableDestination.class);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof RoutableDestination) {
      RoutableDestination that = (RoutableDestination) obj;
      return (this.longitude() == (that.longitude()) && (this.latitude() == (that.latitude())));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode *= 1000003;
    hashCode ^= coordinates.hashCode();
    return hashCode;
  }
}
