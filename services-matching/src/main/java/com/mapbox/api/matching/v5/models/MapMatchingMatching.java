package com.mapbox.api.matching.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.models.RouteLeg;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import java.io.Serializable;
import java.util.List;

/**
 * A match object is a {@link DirectionsRoute} object with an
 * additional confidence field.
 *
 * @since 2.0.0
 */
@AutoValue
public abstract class MapMatchingMatching implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MapMatchingMatching.Builder();
  }

  /**
   * The distance traveled from origin to destination.
   *
   * @return a double number with unit meters
   * @since 1.0.0
   */
  public abstract double distance();

  /**
   * The estimated travel time from origin to destination.
   *
   * @return a double number with unit seconds
   * @since 1.0.0
   */
  public abstract double duration();

  /**
   * Gives the geometry of the route. Commonly used to draw the route on the map view.
   *
   * @return an encoded polyline string
   * @since 1.0.0
   */
  @Nullable
  public abstract String geometry();

  /**
   * The calculated weight of the route.
   *
   * @return the weight value provided from the API as a {@code double} value
   * @since 2.1.0
   */
  public abstract double weight();

  /**
   * The name of the weight profile used while calculating during extraction phase. The default is
   * {@code routability} which is duration based, with additional penalties for less desirable
   * maneuvers.
   *
   * @return a String representing the weight profile used while calculating the route
   * @since 2.1.0
   */
  @SerializedName("weight_name")
  public abstract String weightName();

  /**
   * A Leg is a route between only two waypoints.
   *
   * @return list of {@link RouteLeg} objects
   * @since 1.0.0
   */
  public abstract List<RouteLeg> legs();

  /**
   * A number between 0 (low) and 1 (high) indicating level of confidence in the returned match.
   *
   * @return confidence value
   * @since 2.0.0
   */
  public abstract double confidence();

  /**
   * Convert the current {@link MapMatchingMatching} to its builder holding the currently assigned
   * values. This allows you to modify a single variable and then rebuild the project resulting in
   * an updated and modifier {@link MapMatchingMatching}.
   *
   * @return a {@link MapMatchingMatching.Builder} with the same values set to match the ones
   *   defined in this {@link MapMatchingMatching}
   * @since 3.0.0
   */
  public abstract MapMatchingMatching.Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<MapMatchingMatching> typeAdapter(Gson gson) {
    return new AutoValue_MapMatchingMatching.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link MapMatchingResponse}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * The distance traveled from origin to destination.
     *
     * @param distance a double number with unit meters
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder distance(double distance);

    /**
     * The estimated travel time from origin to destination.
     *
     * @param duration a double number with unit seconds
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder duration(double duration);

    /**
     * Gives the geometry of the route. Commonly used to draw the route on the map view.
     *
     * @param geometry an encoded polyline string
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder geometry(@Nullable String geometry);

    /**
     * The calculated weight of the route.
     *
     * @param weight the weight value provided from the API as a {@code double} value
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder weight(double weight);

    /**
     * The name of the weight profile used while calculating during extraction phase. The default is
     * {@code routability} which is duration based, with additional penalties for less desirable
     * maneuvers.
     *
     * @param weightName a String representing the weight profile used while calculating the route
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder weightName(String weightName);

    /**
     * A Leg is a route between only two waypoints.
     *
     * @param legs list of {@link RouteLeg} objects
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder legs(List<RouteLeg> legs);

    /**
     * A number between 0 (low) and 1 (high) indicating level of confidence in the returned match.
     *
     * @param confidence confidence value
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder confidence(double confidence);

    /**
     * Build a new {@link MapMatchingMatching} object.
     *
     * @return a new {@link MapMatchingMatching} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract MapMatchingMatching build();
  }
}
