package com.mapbox.directions.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.Point;

import java.io.Serializable;

/**
 * An input coordinate snapped to the roads network.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class DirectionsWaypoint implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_DirectionsWaypoint.Builder();
  }

  /**
   * Provides the way name which the waypoint's coordinate is snapped to.
   *
   * @return string with the name of the way the coordinate snapped to
   * @since 1.0.0
   */
  @Nullable
  public abstract String name();

  /**
   * A {@link Point} representing this waypoint location.
   *
   * @return GeoJson Point representing this waypoint location
   * @since 3.0.0
   */
  @Nullable
  public Point location() {
    return Point.fromLngLat(rawLocation()[0], rawLocation()[1]);
  }

  /**
   * A {@link Point} representing this waypoint location.
   *
   * @return GeoJson Point representing this waypoint location
   * @since 3.0.0
   */
  @Nullable
  @SerializedName("location")
  @SuppressWarnings("mutable")
  abstract double[] rawLocation();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<DirectionsWaypoint> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsWaypoint.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link DirectionsWaypoint}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Provides the way name which the waypoint's coordinate is snapped to.
     *
     * @param name string with the name of the way the coordinate snapped to
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder name(@Nullable String name);

    /**
     * The rawLocation as a double array. Once the {@link DirectionsWaypoint} objects created,
     * this raw location gets converted into a {@link Point} object and is public exposed as such.
     * The double array should have a length of two, index 0 being the longitude and index 1 being
     * latitude.
     *
     * @param rawLocation a double array with a length of two, index 0 being the longitude and
     *                    index 1 being latitude.
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder rawLocation(@Nullable double[] rawLocation);

    /**
     * Build a new {@link DirectionsWaypoint} object.
     *
     * @return a new {@link DirectionsWaypoint} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract DirectionsWaypoint build();

  }
}
