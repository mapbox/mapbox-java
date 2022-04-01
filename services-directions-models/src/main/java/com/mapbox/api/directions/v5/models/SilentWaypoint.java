package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Represents an information about silent waypoint in the leg index.
 * See {@link RouteLeg#viaWaypoints()} for more details.
 */
@AutoValue
public abstract class SilentWaypoint extends DirectionsJsonObject {

  /**
   * Create a new instance of this class by using the {@link SilentWaypoint.Builder} class.
   *
   * @return {@link SilentWaypoint.Builder} for creating a new instance
   */
  @NonNull
  public static Builder builder() {
    return new AutoValue_SilentWaypoint.Builder();
  }

  /**
   * The associated waypoint index, excluding the origin (index 0) and destination.
   *
   * @return the associated waypoint index, excluding the origin (index 0) and destination.
   */
  @SerializedName("waypoint_index")
  public abstract int waypointIndex();

  /**
   * The calculated distance, in meters, from the leg origin.
   *
   * @return the calculated distance, in meters, from the leg origin.
   */
  @SerializedName("distance_from_start")
  public abstract double distanceFromStart();

  /**
   * The associated leg shape index of the via waypoint location.
   *
   * @return the associated leg shape index of the via waypoint location.
   */
  @SerializedName("geometry_index")
  public abstract int geometryIndex();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  @NonNull
  public static TypeAdapter<SilentWaypoint> typeAdapter(Gson gson) {
    return new AutoValue_SilentWaypoint.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link SilentWaypoint}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * @param waypointIndex the associated waypoint index, excluding
     *   the origin (index 0) and destination.
     * @return this builder
     */
    @NonNull
    public abstract Builder waypointIndex(int waypointIndex);

    /**
     * @param distanceFromStart the calculated distance, in meters, from the leg origin.
     * @return this builder
     */
    @NonNull
    public abstract Builder distanceFromStart(double distanceFromStart);

    /**
     * @param geometryIndex the associated leg shape index of the via waypoint location.
     * @return this builder
     */
    @NonNull
    public abstract Builder geometryIndex(int geometryIndex);

    /**
     * Build a new {@link SilentWaypoint} object.
     *
     * @return a new {@link SilentWaypoint} object
     */
    @NonNull
    public abstract SilentWaypoint build();
  }
}
