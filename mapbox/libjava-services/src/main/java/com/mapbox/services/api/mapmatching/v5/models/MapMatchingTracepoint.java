package com.mapbox.services.api.mapmatching.v5.models;

import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.services.api.directions.v5.models.DirectionsWaypoint;
import com.mapbox.services.commons.geojson.Point;

import java.io.Serializable;

/**
 * A tracepoint object is {@link DirectionsWaypoint} object with two additional fields.
 *
 * @since 2.0.0
 */
@AutoValue
public abstract class MapMatchingTracepoint implements Serializable {

  /**
   * This method returns a new instance of the {@link Builder} class which provides a way to create
   * a new instance of this class.
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
  @SerializedName("matchings_index")
  public abstract int matchingsIndex();

  /**
   * Number of probable alternative matchings for this trace point. A value of zero indicates that
   * this point was matched unambiguously. Split the trace at these points for incremental map
   * matching.
   *
   * @return an integer representing the alternatives count
   * @since 2.2.0
   */
  @SerializedName("alternatives_count")
  public abstract int alternativesCount();

  /**
   * Index of the waypoint inside the matched route.
   *
   * @return index value
   * @since 2.2.0
   */
  @SerializedName("waypoint_index")
  public abstract int waypointIndex();

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
     * @return index value
     * @since 3.0.0
     */
    public abstract Builder matchingsIndex(int matchingsIndex);

    /**
     * Number of probable alternative matchings for this trace point. A value of zero indicates that
     * this point was matched unambiguously. Split the trace at these points for incremental map
     * matching.
     *
     * @return an integer representing the alternatives count
     * @since 3.0.0
     */
    public abstract Builder alternativesCount(int alternativesCount);

    /**
     * Index of the waypoint inside the matched route.
     *
     * @return index value
     * @since 3.0.0
     */
    public abstract Builder waypointIndex(int waypointIndex);

    /**
     * Build a new {@link MapMatchingTracepoint} object.
     *
     * @return a new {@link MapMatchingTracepoint} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract MapMatchingTracepoint build();
  }
}
