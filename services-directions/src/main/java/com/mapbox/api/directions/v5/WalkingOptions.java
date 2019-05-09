package com.mapbox.api.directions.v5;

import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

/**
 * Class for specifying options for use with the walking profile.
 * @since 4.8.0
 */
@AutoValue
public abstract class WalkingOptions {

  /**
   * Walking speed in meters per second. Must be between 0.14 and 6.94 meters per second.
   * Defaults to 1.42 meters per second
   *
   * @return walkingSpeed in meters per second
   * @since 4.8.0
   */
  @Nullable
  public abstract Double walkingSpeed();

  /**
   * A factor that modifies the cost when encountering roads or paths that do not allow
   * vehicles and are set aside for pedestrian use. Pedestrian routes generally attempt to
   * favor using these walkways and sidewalks. The default walkway_factor is 0
   *
   * @return walkwayBias factor to modify the cost of roads or paths that do not allow vehicles
   * @since 4.8.0
   */
  @Nullable
  public abstract Double walkwayBias();

  /**
   * A factor that modifies (multiplies) the cost when alleys are encountered. Pedestrian
   * routes generally want to avoid alleys or narrow service roads between buildings. The
   * default alley_factor is 0
   *
   * @return alleyBias factor to modify the cost when alleys are encountered
   * @since 4.8.0
   */
  @Nullable
  public abstract Double alleyBias();

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a WalkingOptions object
   * @return a new instance of this class defined by the values passed inside this static factory
   *    method
   * @since 4.8.0
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
   * @since 4.8.0
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
   * @since 4.8.0
   */
  public static TypeAdapter<WalkingOptions> typeAdapter(Gson gson) {
    return new AutoValue_WalkingOptions.GsonTypeAdapter(gson);
  }

  /**
   * Build a new {@link WalkingOptions} object with no defaults.
   *
   * @return a {@link Builder} object for creating a {@link WalkingOptions} object
   * @since 4.8.0
   */
  public static Builder builder() {
    return new AutoValue_WalkingOptions.Builder();
  }

  /**
   * This builder is used to create a new object with specifications relating to walking directions.
   *
   * @since 4.8.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Walking speed in meters per second. Must be between 0.14 and 6.94 meters per second.
     * Defaults to 1.42 meters per second
     *
     * @param walkingSpeed in meters per second
     * @return this builder
     * @since 4.8.0
     */
    public abstract Builder walkingSpeed(
      @Nullable @FloatRange(from = 0.14, to = 6.94) Double walkingSpeed);

    /**
     * A factor that modifies the cost when encountering roads or paths that do not allow
     * vehicles and are set aside for pedestrian use. Pedestrian routes generally attempt to
     * favor using these walkways and sidewalks. The default walkway_factor is 0.
     *
     * @param walkwayBias factor to modify the cost of roads or paths that do not allow vehicles
     * @return this builder
     * @since 4.8.0
     */
    public abstract Builder walkwayBias(
      @Nullable @FloatRange(from = -1, to = 1) Double walkwayBias);

    /**
     * A factor that modifies (multiplies) the cost when alleys are encountered. Pedestrian
     * routes generally want to avoid alleys or narrow service roads between buildings. The
     * default alley_factor is 0.
     *
     * @param alleyBias factor to modify the cost when alleys are encountered
     * @return this builder
     * @since 4.8.0
     */
    public abstract Builder alleyBias(
      @Nullable @FloatRange(from = -1, to = 1) Double alleyBias);

    /**
     * Builds a WalkingOptions object with specified configurations.
     *
     * @return WalkingOptions object
     * @since 4.8.0
     */
    public abstract WalkingOptions build();
  }
}
