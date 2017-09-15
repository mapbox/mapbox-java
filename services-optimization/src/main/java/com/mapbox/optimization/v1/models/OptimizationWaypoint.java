package com.mapbox.optimization.v1.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.Point;

import java.io.Serializable;

/**
 * This is a single input coordinated snapped to the road and path network. The full list of the
 * waypoints appear in the list in the order of the input coordinates.
 *
 * @since 2.2.0
 */
@AutoValue
public abstract class OptimizationWaypoint implements Serializable {

  /**
   * This method returns a new instance of the {@link Builder} class which provides a way to create
   * a new instance of this class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_OptimizationWaypoint.Builder();
  }

  /**
   * Index of the waypoint inside the optimization route.
   *
   * @return index value
   * @since 2.2.0
   */
  @SerializedName("waypoint_index")
  public abstract int waypointIndex();

  /**
   * Index to the trip object in the trips array that contains this waypoint.
   *
   * @return index value
   * @since 2.2.0
   */
  @SerializedName("trips_index")
  public abstract int tripsIndex();

  /**
   * Provides the way name which the waypoint's coordinate is snapped to.
   *
   * @return string with the name of the way the coordinate snapped to
   * @since 2.2.0
   */
  @Nullable
  public abstract String name();

  /**
   * A {@link Point} representing this waypoint location.
   *
   * @return GeoJson Point representing this waypoint location
   * @since 3.0.0
   */
  @NonNull
  public Point location() {
    return Point.fromLngLat(rawLocation()[0], rawLocation()[1]);
  }

  /**
   * The rawLocation as a double array, since we convert this array to a {@link Point} there's no
   * need to expose this to developers.
   * <p>
   * Note that the mutable suppress warnings used here since this isn't publicly exposed and
   * internally, we do not mess with these values.
   * </p>
   *
   * @return a double array used for creating the public {@link Point} object
   * @since 3.0.0
   */
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
  public static TypeAdapter<OptimizationWaypoint> typeAdapter(Gson gson) {
    return new AutoValue_OptimizationWaypoint.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link OptimizationWaypoint}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Index of the waypoint inside the optimization route.
     *
     * @param waypointIndex index value
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder waypointIndex(int waypointIndex);

    /**
     * Index to the trip object in the trips array that contains this waypoint.
     *
     * @param tripsIndex index value
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder tripsIndex(int tripsIndex);

    /**
     * Provides the way name which the waypoint's coordinate is snapped to.
     *
     * @param name string with the name of the way the coordinate snapped to
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder name(@Nullable String name);

    /**
     * The rawLocation as a double array. Once the {@link OptimizationWaypoint} objects created,
     * this raw location gets converted into a {@link Point} object and is public exposed as such.
     * The double array should have a length of two, index 0 being the longitude and index 1 being
     * latitude.
     *
     * @param rawLocation a double array with a length of two, index 0 being the longitude and
     *                    index 1 being latitude.

     * @since 3.0.0
     */
    public abstract Builder rawLocation(double[] rawLocation);

    /**
     * Build a new {@link OptimizationResponse} object.
     *
     * @return a new {@link OptimizationResponse} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract OptimizationWaypoint build();
  }
}
