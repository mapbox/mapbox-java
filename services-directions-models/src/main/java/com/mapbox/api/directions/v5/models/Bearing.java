package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import com.google.auto.value.AutoValue;

/**
 * A value pair of an {@link #angle} which is a clockwise value from true north between 0 and 360,
 * and a {@link #degrees()} which is the range of degrees by which the angle can deviate.
 */
@AutoValue
public abstract class Bearing {

  /**
   * Build a new instance of this builder.
   *
   * @return {@link Builder}
   */
  public static Bearing.Builder builder() {
    return new AutoValue_Bearing.Builder()
      .angle(45.0)
      .degrees(90.0);
  }

  /**
   * Clockwise value from true north between 0 and 360.
   * <p>
   * Defaults to 45°.
   *
   * @return value
   */
  public abstract double angle();

  /**
   * Range of degrees by which the angle can deviate.
   * <p>
   * Defaults to 90°.
   *
   * @return value
   */
  public abstract double degrees();

  /**
   * Convert the current {@link Bearing} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link Bearing}.
   *
   * @return a {@link Bearing.Builder} with the same values set to match the ones defined
   *   in this {@link Bearing}
   */
  @NonNull
  public abstract Builder toBuilder();

  /**
   * This builder can be used to set the values describing the {@link Bearing}.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Clockwise value from true north between 0 and 360.
     *
     * @param angle value
     * @return this builder
     */
    @NonNull
    public abstract Builder angle(double angle);

    /**
     * Range of degrees by which the angle can deviate.
     *
     * @param degrees value
     * @return this builder
     */
    @NonNull
    public abstract Builder degrees(double degrees);

    /**
     * This uses the provided parameters set using the {@link Builder}.
     *
     * @return a new instance of {@link Bearing}
     */
    @NonNull
    public abstract Bearing build();
  }
}
