package com.mapbox.api.directions.v5;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

/**
 * Class for specifying options for use with the walking profile.
 * @since 4.7.0
 */
@AutoValue
public abstract class WalkingOptions {

  /**
   * Walking speed in kilometers per hour. Must be between 0.5 and 25 km/hr. Defaults to 5.1
   * km/hr (3.1 miles/hour).
   *
   * @return walkingSpeed in kilometers per hour
   * @since 4.7.0
   */
  @Nullable
  public abstract Double walkingSpeed();

  /**
   * A factor that modifies the cost when encountering roads or paths that do not allow
   * vehicles and are set aside for pedestrian use. Pedestrian routes generally attempt to
   * favor using these walkways and sidewalks. The default walkway_factor is 0.9, indicating a
   * slight preference.
   *
   * @return walkwayBias factor to modify the cost of roads or paths that do not allow vehicles
   * @since 4.7.0
   */
  @Nullable
  public abstract Double walkwayBias();

  /**
   * A factor that modifies (multiplies) the cost when alleys are encountered. Pedestrian
   * routes generally want to avoid alleys or narrow service roads between buildings. The
   * default alley_factor is 2.0.
   *
   * @return alleyBias factor to modify the cost when alleys are encountered
   * @since 4.7.0
   */
  @Nullable
  public abstract Double alleyBias();

  /**
   * A factor that modifies (multiplies) the cost when ferries are encountered.
   *
   * @return ferryBias factor to modify the cost when ferries are encountered
   * @since 4.7.0
   */
  @Nullable
  public abstract Double ferryBias();

  /**
   * A penalty in seconds added to each transition onto a path with steps or stairs. Higher
   * values apply larger cost penalties to avoid paths that contain flights of steps.
   *
   * @return stepPenalty in seconds added to each transistion with steps or stairs
   * @since 4.7.0
   */
  @Nullable
  public abstract Integer stepPenalty();

  /**
   * This value indicates the maximum difficulty of hiking trails that is allowed. Values
   * between 0 and 6 are allowed. The values correspond to sac_scale values within
   * OpenStreetMap. The default value is 1 which means that well cleared
   * trails that are mostly flat or slightly sloped are allowed. Higher difficulty trails can
   * be allowed by specifying a higher value.
   *
   * @return maxHikingDifficulty maximum difficulty of hiking trails that is allowed
   * @since 4.7.0
   */
  @Nullable
  public abstract Integer maxHikingDifficulty();

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a WalkingOptions object
   * @return a new instance of this class defined by the values passed inside this static factory
   *    method
   * @since 4.7.0
   */
  public static WalkingOptions fromJson(String json) {
    GsonBuilder gsonBuilder = new GsonBuilder()
      .registerTypeAdapterFactory(WalkingOptionsAdapterFactory.create());
    return gsonBuilder.create().fromJson(json, WalkingOptions.class);
  }

  /**
   * This takes the currently defined values found inside this instance and converts it to a json
   * string.
   *
   * @return a Json string which represents this WalkingOptions object
   * @since 4.7.0
   */
  public final String toJson() {
    Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(WalkingOptionsAdapterFactory.create())
      .create();
    return gson.toJson(this, WalkingOptions.class);
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 4.7.0
   */
  public static TypeAdapter<WalkingOptions> typeAdapter(Gson gson) {
    return new AutoValue_WalkingOptions.GsonTypeAdapter(gson);
  }

  /**
   * Build a new {@link WalkingOptions} object with no defaults.
   *
   * @return a {@link Builder} object for creating a {@link WalkingOptions} object
   * @since 4.7.0
   */
  public static Builder builder() {
    return new AutoValue_WalkingOptions.Builder();
  }

  /**
   * This builder is used to create a new object with specifications relating to walking directions.
   *
   * @since 4.7.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Walking speed in kilometers per hour. Must be between 0.5 and 25 km/hr. Defaults to 5.1
     * km/hr (3.1 miles/hour).
     *
     * @param walkingSpeed in kilometers per hour
     * @return this builder
     * @since 4.7.0
     */
    public abstract Builder walkingSpeed(
      @Nullable @FloatRange(from = 0.5, to = 25) Double walkingSpeed);

    /**
     * A factor that modifies the cost when encountering roads or paths that do not allow
     * vehicles and are set aside for pedestrian use. Pedestrian routes generally attempt to
     * favor using these walkways and sidewalks. The default walkway_factor is 0.9, indicating a
     * slight preference.
     *
     * @param walkwayBias factor to modify the cost of roads or paths that do not allow vehicles
     * @return this builder
     * @since 4.7.0
     */
    public abstract Builder walkwayBias(
      @Nullable @FloatRange(from = 0.1, to = 100000.0) Double walkwayBias);

    /**
     * A factor that modifies (multiplies) the cost when alleys are encountered. Pedestrian
     * routes generally want to avoid alleys or narrow service roads between buildings. The
     * default alley_factor is 2.0.
     *
     * @param alleyBias factor to modify the cost when alleys are encountered
     * @return this builder
     * @since 4.7.0
     */
    public abstract Builder alleyBias(
      @Nullable @FloatRange(from = 0.1, to = 100000.0) Double alleyBias);

    /**
     * A factor that modifies (multiplies) the cost when ferries are encountered.
     *
     * @param ferryBias factor to modify the cost when ferries are encountered
     * @return this builder
     * @since 4.7.0
     */
    public abstract Builder ferryBias(
      @Nullable @FloatRange(from = 0.1, to = 100000.0) Double ferryBias);

    /**
     * A penalty in seconds added to each transition onto a path with steps or stairs. Higher
     * values apply larger cost penalties to avoid paths that contain flights of steps.
     *
     * @param stepPenalty in seconds added to each transistion with steps or stairs
     * @return this builder
     * @since 4.7.0
     */
    public abstract Builder stepPenalty(
      @Nullable @IntRange(from = 0, to = 43200) Integer stepPenalty);

    /**
     * This value indicates the maximum difficulty of hiking trails that is allowed. Values
     * between 0 and 6 are allowed. The values correspond to sac_scale values within
     * OpenStreetMap. The default value is 1 which means that well cleared
     * trails that are mostly flat or slightly sloped are allowed. Higher difficulty trails can
     * be allowed by specifying a higher value.
     *
     * @param maxHikingDifficulty maximum difficulty of hiking trails that is allowed
     * @return this builder
     * @since 4.7.0
     */
    public abstract Builder maxHikingDifficulty(
      @Nullable @IntRange(from = 0, to = 6) Integer maxHikingDifficulty);

    /**
     * Builds a WalkingOptions object with specified configurations.
     *
     * @return WalkingOptions object
     * @since 4.7.0
     */
    public abstract WalkingOptions build();
  }
}
