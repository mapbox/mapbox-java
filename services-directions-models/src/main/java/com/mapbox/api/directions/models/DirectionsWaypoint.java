package com.mapbox.api.directions.models;

import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.Point;

/**
 * An input coordinate snapped to the roads network.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class DirectionsWaypoint extends DirectionsJsonObject {

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
   * Convert the current {@link DirectionsWaypoint} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link DirectionsWaypoint}.
   *
   * @return a {@link DirectionsWaypoint.Builder} with the same values set to match the ones defined
   *   in this {@link DirectionsWaypoint}
   * @since 3.1.0
   */
  public abstract Builder toBuilder();

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
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a DirectionsWaypoint
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.4.0
   */
  public static DirectionsWaypoint fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, DirectionsWaypoint.class);
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
