package com.mapbox.api.directions.v5.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;

@AutoValue
public abstract class MaxSpeed implements Serializable {

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
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<IntersectionLanes> typeAdapter(Gson gson) {
    return new AutoValue_MaxSpeed.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link MaxSpeed}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Number indicating the posted speed limit.
     *
     * @param speed indicating the posted speed limit
     * @since 3.0.0
     */
    public abstract Builder speed(@Nullable Integer speed);

    /**
     * String indicating the unit of speed, either as `km/h` or `mph`.
     *
     * @param unit either as `km/h` or `mph`
     * @since 3.0.0
     */
    public abstract Builder unit(@Nullable String unit);

    /**
     * Boolean is true if the speed limit is not known, otherwise null.
     *
     * @param unknown true if speed limit is not known, otherwise null
     * @since 3.0.0
     */
    public abstract Builder unknown(@Nullable Boolean unknown);

    /**
     * Boolean is `true` if the speed limit is unlimited, otherwise null.
     *
     * @param none true if speed limit is unlimited, otherwise null
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
