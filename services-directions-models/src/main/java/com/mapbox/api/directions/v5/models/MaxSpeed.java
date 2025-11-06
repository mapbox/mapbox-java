package com.mapbox.api.directions.v5.models;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;

/**
 * Object representing max speeds along a route.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class MaxSpeed extends DirectionsJsonObject {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MaxSpeed.Builder();
  }

  /**
   * Number indicating the posted speed limit.
   *
   * @return number indicating the posted speed limit
   * @since 3.0.0
   */
  @Nullable
  public abstract Integer speed();

  /**
   * String indicating the unit of speed, either as `km/h` or `mph`.
   *
   * @return String unit either as `km/h` or `mph`
   * @since 3.0.0
   */
  @Nullable
  @SpeedLimit.Unit
  public abstract String unit();

  /**
   * Boolean is true if the speed limit is not known, otherwise null.
   *
   * @return Boolean true if speed limit is not known, otherwise null
   * @since 3.0.0
   */
  @Nullable
  public abstract Boolean unknown();

  /**
   * Boolean is `true` if the speed limit is unlimited, otherwise null.
   *
   * @return Boolean true if speed limit is unlimited, otherwise null
   * @since 3.0.0
   */
  @Nullable
  public abstract Boolean none();

  /**
   * Convert the current {@link MaxSpeed} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link MaxSpeed}.
   *
   * @return a {@link MaxSpeed.Builder} with the same values set to match the ones defined
   *   in this {@link MaxSpeed}
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
  public static TypeAdapter<MaxSpeed> typeAdapter(Gson gson) {
    return new MaxSpeedGsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a MaxSpeed
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.4.0
   */
  public static MaxSpeed fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, MaxSpeed.class);
  }

  /**
   * This builder can be used to set the values describing the {@link MaxSpeed}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * Number indicating the posted speed limit.
     *
     * @param speed indicating the posted speed limit
     * @return a {@link Builder} object
     * @since 3.0.0
     */
    public abstract Builder speed(@Nullable Integer speed);

    /**
     * String indicating the unit of speed, either as `km/h` or `mph`.
     *
     * @param unit either as `km/h` or `mph`
     * @return a {@link Builder} object
     * @since 3.0.0
     */
    public abstract Builder unit(@Nullable @SpeedLimit.Unit String unit);

    /**
     * Boolean is true if the speed limit is not known, otherwise null.
     *
     * @param unknown true if speed limit is not known, otherwise null
     * @return a {@link Builder} object
     * @since 3.0.0
     */
    public abstract Builder unknown(@Nullable Boolean unknown);

    /**
     * Boolean is `true` if the speed limit is unlimited, otherwise null.
     *
     * @param none true if speed limit is unlimited, otherwise null
     * @return a {@link Builder} object
     * @since 3.0.0
     */
    public abstract Builder none(@Nullable Boolean none);

    /**
     * Build a new {@link MaxSpeed} object.
     *
     * @return a new {@link MaxSpeed} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract MaxSpeed build();
  }
}
