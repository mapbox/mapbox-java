package com.mapbox.services.api.directions.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Includes one {@link StepManeuver} object and travel to the following {@link LegStep}.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class LegStep implements Serializable {

  public static AutoValue_LegStep.Builder builder() {
    return new AutoValue_LegStep.Builder();
  }

  /**
   * The distance traveled from the maneuver to the next {@link LegStep}.
   *
   * @return a double number with unit meters
   * @since 1.0.0
   */
  public abstract double distance();

  /**
   * The estimated travel time from the maneuver to the next {@link LegStep}.
   *
   * @return a double number with unit seconds
   * @since 1.0.0
   */
  public abstract double duration();

  /**
   * Gives the geometry of the leg step.
   *
   * @return an encoded polyline string
   * @since 1.0.0
   */
  public abstract String geometry();

  /**
   * String with the name of the way along which the travel proceeds.
   *
   * @return a {@code String} representing the way along which the travel proceeds
   * @since 1.0.0
   */
  @Nullable
  public abstract String name();

  /**
   * String with reference number or code of the way along which the travel proceeds.
   *
   * @return String with reference number or code of the way along which the travel proceeds.
   * Optionally included, if data is available.
   * @since 2.0.0
   */
  @Nullable
  public abstract String ref();

  /**
   * String with the destinations of the way along which the travel proceeds.
   *
   * @return String with the destinations of the way along which the travel proceeds. Optionally
   * included, if data is available
   * @since 2.0.0
   */
  @Nullable
  public abstract String destinations();

  /**
   * indicates the mode of transportation in the step.
   *
   * @return String indicating the mode of transportation.
   * @since 1.0.0
   */
  @NonNull
  public abstract String mode();

  /**
   * The pronunciation hint of the way name. Will be undefined if no pronunciation is hit.
   *
   * @return String with the pronunciation
   * @since 2.0.0
   */
  @Nullable
  public abstract String pronunciation();

  /**
   * An optional string indicating the name of the rotary. This will only be a nonnull when the
   * maneuver type equals {@code rotary}.
   *
   * @return String with the rotary name
   * @since 2.0.0
   */
  @Nullable
  @SerializedName("rotary_name")
  public abstract String rotaryName();

  /**
   * An optional string indicating the pronunciation of the name of the rotary. This will only be a
   * nonnull when the maneuver type equals {@code rotary}.
   *
   * @return String in IPA with the rotary name's pronunciation.
   * @since 2.0.0
   */
  @Nullable
  @SerializedName("rotary_pronunciation")
  public abstract String rotaryPronunciation();

  /**
   * A {@link StepManeuver} object that typically represents the first coordinate making up the
   * {@link LegStep#geometry()}.
   *
   * @return ne {@link StepManeuver} object
   * @since 1.0.0
   */
  @NonNull
  public abstract StepManeuver maneuver();

  /**
   * Specifies a decimal precision of edge weights, default value 1.
   *
   * @return a decimal precision double value
   * @since 2.1.0
   */
  public abstract double weight();

  /**
   * Provides a list of all the intersections connected to the current way the user is traveling
   * along.
   *
   * @return list of {@link StepIntersection} representing all intersections along the step
   * @since 1.3.0
   */
  @Nullable
  public abstract List<StepIntersection> intersections();

  /**
   * String with the exit numbers or names of the way. Optionally included, if data is available.
   *
   * @return a String identifying the exit number or name
   * @since 3.0.0
   */
  @Nullable
  public abstract String exits();

  public static TypeAdapter<LegStep> typeAdapter(Gson gson) {
    return new AutoValue_LegStep.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder distance(double distance);

    public abstract Builder duration(double duration);

    public abstract Builder geometry(String geometry);

    public abstract Builder name(@Nullable String name);

    public abstract Builder ref(@Nullable String ref);

    public abstract Builder destinations(@Nullable String destinations);

    public abstract Builder mode(@NonNull String mode);

    public abstract Builder pronunciation(@Nullable String pronunciation);

    public abstract Builder rotaryName(@Nullable String rotaryName);

    public abstract Builder rotaryPronunciation(@Nullable String rotaryPronunciation);

    public abstract Builder maneuver(@NonNull StepManeuver maneuver);

    public abstract Builder weight(double weight);

    public abstract Builder intersections(@Nullable List<StepIntersection> intersections);

    public abstract Builder exits(@Nullable String exits);

    public abstract LegStep build();
  }
}
