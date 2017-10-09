package com.mapbox.matching.v5.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.Point;

import java.io.Serializable;

/**
 * A tracepoint object is {@link com.mapbox.directions.v5.models.DirectionsWaypoint} object with two
 * additional fields.
 *
 * @since 2.0.0
 */
@AutoValue
public abstract class MapMatchingTracepoint implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MapMatchingTracepoint.Builder();
  }

  /**
   * Index to the match object in matchings the sub-trace was matched to.
   *
   * @return index value
   * @since 2.2.0
   */
  @Nullable
  @SerializedName("matchings_index")
  public abstract Integer matchingsIndex();

  /**
   * Number of probable alternative matchings for this trace point. A value of zero indicates that
   * this point was matched unambiguously. Split the trace at these points for incremental map
   * matching.
   *
   * @return an integer representing the alternatives count
   * @since 2.2.0
   */
  @Nullable
  @SerializedName("alternatives_count")
  public abstract Integer alternativesCount();

  /**
   * Index of the waypoint inside the matched route.
   *
   * @return index value
   * @since 2.2.0
   */
  @Nullable
  @SerializedName("waypoint_index")
  public abstract Integer waypointIndex();

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
   * Provides the way name which the waypoint's coordinate is snapped to.
   *
   * @return string with the name of the way the coordinate snapped to
   * @since 1.0.0
   */
  @Nullable
  public abstract String name();

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
  public static TypeAdapter<MapMatchingTracepoint> typeAdapter(Gson gson) {
    return new AutoValue_MapMatchingTracepoint.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link MapMatchingTracepoint}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * The rawLocation as a double array. Once the {@link MapMatchingTracepoint} object's created,
     * this raw location gets converted into a {@link Point} object and is public exposed as such.
     * The double array should have a length of two, index 0 being the longitude and index 1 being
     * latitude.
     *
     * @param rawLocation a double array with a length of two, index 0 being the longitude and
     *                    index 1 being latitude.
     * @return a double array used for creating the public {@link Point} object
     * @since 3.0.0
     */
    public abstract Builder rawLocation(double[] rawLocation);

    /**
     * Index to the match object in matchings the sub-trace was matched to.
     *
     * @param matchingsIndex index value
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder matchingsIndex(@Nullable Integer matchingsIndex);

    /**
     * Number of probable alternative matchings for this trace point. A value of zero indicates that
     * this point was matched unambiguously. Split the trace at these points for incremental map
     * matching.
     *
     * @param alternativesCount an integer representing the alternatives count
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder alternativesCount(@Nullable Integer alternativesCount);

    /**
     * Index of the waypoint inside the matched route.
     *
     * @param waypointIndex index value
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder waypointIndex(@Nullable Integer waypointIndex);

    /**
     * Provides the way name which the waypoint's coordinate is snapped to.
     *
     * @param name string with the name of the way the coordinate snapped to
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder name(@Nullable String name);

    /**
     * Build a new {@link MapMatchingTracepoint} object.
     *
     * @return a new {@link MapMatchingTracepoint} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract MapMatchingTracepoint build();
  }
}
