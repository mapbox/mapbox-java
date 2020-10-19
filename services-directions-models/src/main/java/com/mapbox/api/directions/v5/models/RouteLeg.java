package com.mapbox.api.directions.v5.models;

import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;

import java.util.List;

/**
 * A route between only two {@link DirectionsWaypoint}.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class RouteLeg extends DirectionsJsonObject {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_RouteLeg.Builder();
  }

  /**
   * The distance traveled from one waypoint to another.
   *
   * @return a double number with unit meters
   * @since 1.0.0
   */
  @Nullable
  public abstract Double distance();

  /**
   * The estimated travel time from one waypoint to another.
   *
   * @return a double number with unit seconds
   * @since 1.0.0
   */
  @Nullable
  public abstract Double duration();

  /**
   * The typical travel time for traversing this RouteLeg. There's a delay along the RouteLeg
   * if you subtract this durationTypical() value from the RouteLeg duration() value and
   * the resulting difference is greater than 0. The delay is because of any number
   * of real-world situations (road repair, traffic jam, etc).
   *
   * @return a double number with unit seconds
   * @since 5.5.0
   */
  @Nullable
  @SerializedName("duration_typical")
  public abstract Double durationTypical();

  /**
   * A short human-readable summary of major roads traversed. Useful to distinguish alternatives.
   *
   * @return String with summary
   * @since 1.0.0
   */
  @Nullable
  public abstract String summary();

  /**
   * An array of objects describing the administrative boundaries the route leg travels through.
   * Use {@link StepIntersection#adminIndex()} on the intersection object
   * to look up the admin for each intersection in this array.
   */
  @Nullable
  public abstract List<Admin> admins();

  /**
   * Gives a List including all the steps to get from one waypoint to another.
   *
   * @return List of {@link LegStep}
   * @since 1.0.0
   */
  @Nullable
  public abstract List<LegStep> steps();

  /**
   * A list of incidents that occur on this leg.
   *
   * @return a list of {@link Incident}
   */
  @Nullable
  public abstract List<Incident> incidents();

  /**
   * A {@link LegAnnotation} that contains additional details about each line segment along the
   * route geometry. If you'd like to receiving this, you must request it inside your Directions
   * request before executing the call.
   *
   * @return a {@link LegAnnotation} object
   * @since 2.1.0
   */
  @Nullable
  public abstract LegAnnotation annotation();

  /**
   * Convert the current {@link RouteLeg} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link RouteLeg}.
   *
   * @return a {@link RouteLeg.Builder} with the same values set to match the ones defined
   *   in this {@link RouteLeg}
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
  public static TypeAdapter<RouteLeg> typeAdapter(Gson gson) {
    return new AutoValue_RouteLeg.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a RouteLeg
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.4.0
   */
  public static RouteLeg fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, RouteLeg.class);
  }

  /**
   * This builder can be used to set the values describing the {@link RouteLeg}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * The distance traveled from one waypoint to another.
     *
     * @param distance a double number with unit meters
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder distance(@Nullable Double distance);

    /**
     * The estimated travel time from one waypoint to another.
     *
     * @param duration a double number with unit seconds
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder duration(@Nullable Double duration);

    /**
     * The typical travel time for traversing this RouteLeg. There's a delay along the RouteLeg
     * if you subtract this durationTypical() value from the RouteLeg duration() value and
     * the resulting difference is greater than 0. The delay is because of any number
     * of real-world situations (road repair, traffic jam, etc).
     *
     * @param durationTypical a double number with unit seconds
     * @return this builder for chaining options together
     * @since 5.5.0
     */
    public abstract Builder durationTypical(@Nullable Double durationTypical);

    /**
     * A short human-readable summary of major roads traversed. Useful to distinguish alternatives.
     *
     * @param summary String with summary
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder summary(@Nullable String summary);

    /**
     * An array of objects describing the administrative boundaries the route leg travels through.
     * Use {@link StepIntersection#adminIndex()} on the intersection object
     * to look up the admin for each intersection in this array.
     *
     * @param admins Array with admins
     * @return this builder for chaining options together
     */
    public abstract Builder admins(@Nullable List<Admin> admins);

    /**
     * Gives a List including all the steps to get from one waypoint to another.
     *
     * @param steps List of {@link LegStep}
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder steps(@Nullable List<LegStep> steps);

    /**
     * A list of incidents that occur on this leg.
     *
     * @param incidents a list of {@link Incident}
     * @return this builder for chaining options together
     */
    public abstract Builder incidents(@Nullable List<Incident> incidents);

    /**
     * A {@link LegAnnotation} that contains additional details about each line segment along the
     * route geometry. If you'd like to receiving this, you must request it inside your Directions
     * request before executing the call.
     *
     * @param annotation a {@link LegAnnotation} object
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder annotation(@Nullable LegAnnotation annotation);

    /**
     * Build a new {@link RouteLeg} object.
     *
     * @return a new {@link RouteLeg} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract RouteLeg build();
  }
}
