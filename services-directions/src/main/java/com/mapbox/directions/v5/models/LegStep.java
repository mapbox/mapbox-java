package com.mapbox.directions.v5.models;

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

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_LegStep.Builder();
  }

  /**
   * The distance traveled from the maneuver to the next {@link LegStep}.
   *
   * @return a double number with unit meters
   * @since 1.0.0
   */
  @Nullable
  public abstract Double distance();

  /**
   * The estimated travel time from the maneuver to the next {@link LegStep}.
   *
   * @return a double number with unit seconds
   * @since 1.0.0
   */
  @Nullable
  public abstract Double duration();

  /**
   * Gives the geometry of the leg step.
   *
   * @return an encoded polyline string
   * @since 1.0.0
   */
  @Nullable
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
   *   Optionally included, if data is available.
   * @since 2.0.0
   */
  @Nullable
  public abstract String ref();

  /**
   * String with the destinations of the way along which the travel proceeds.
   *
   * @return String with the destinations of the way along which the travel proceeds. Optionally
   *   included, if data is available
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
  @Nullable
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
   * @return new {@link StepManeuver} object
   * @since 1.0.0
   */
  @Nullable
  public abstract StepManeuver maneuver();

  /**
   * The voice instructions object is useful for navigation sessions providing well spoken text
   * instructions along with the distance from the maneuver the instructions should be said.
   *
   * @return a list of voice instructions which can be triggered on this current step
   * @since 3.0.0
   */
  @Nullable
  public abstract List<VoiceInstructions> voiceInstructions();

  @Nullable
  public abstract List<BannerInstructions> bannerInstructions();

  /**
   * Specifies a decimal precision of edge weights, default value 1.
   *
   * @return a decimal precision double value
   * @since 2.1.0
   */
  @Nullable
  public abstract Double weight();

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

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<LegStep> typeAdapter(Gson gson) {
    return new AutoValue_LegStep.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link LegStep}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * The distance traveled from the maneuver to the next {@link LegStep}.
     *
     * @param distance a double number with unit meters
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder distance(@Nullable Double distance);

    /**
     * The estimated travel time from the maneuver to the next {@link LegStep}.
     *
     * @param duration a double number with unit seconds
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder duration(@Nullable Double duration);

    /**
     * Gives the geometry of the leg step.
     *
     * @param geometry an encoded polyline string
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder geometry(@Nullable String geometry);

    /**
     * String with the name of the way along which the travel proceeds.
     *
     * @param name a {@code String} representing the way along which the travel proceeds
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder name(@Nullable String name);

    /**
     * String with reference number or code of the way along which the travel proceeds.
     *
     * @param ref String with reference number or code of the way along which the travel proceeds.
     *            Optionally included, if data is available
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder ref(@Nullable String ref);

    /**
     * String with the destinations of the way along which the travel proceeds.
     *
     * @param destinations String with the destinations of the way along which the travel proceeds.
     *                     Optionally included, if data is available
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder destinations(@Nullable String destinations);

    /**
     * Indicates the mode of transportation in the step.
     *
     * @param mode String indicating the mode of transportation
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder mode(@Nullable String mode);

    /**
     * The pronunciation hint of the way name. Will be undefined if no pronunciation is hit.
     *
     * @param pronunciation String with the pronunciation
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder pronunciation(@Nullable String pronunciation);

    /**
     * An optional string indicating the name of the rotary. This will only be a nonnull when the
     * maneuver type equals {@code rotary}.
     *
     * @param rotaryName String with the rotary name
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder rotaryName(@Nullable String rotaryName);

    /**
     * An optional string indicating the pronunciation of the name of the rotary. This will only be
     * a nonnull when the maneuver type equals {@code rotary}.
     *
     * @param rotaryPronunciation String in IPA with the rotary name's pronunciation.
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder rotaryPronunciation(@Nullable String rotaryPronunciation);

    /**
     * A {@link StepManeuver} object that typically represents the first coordinate making up the
     * {@link LegStep#geometry()}.
     *
     * @param maneuver new {@link StepManeuver} object
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder maneuver(@Nullable StepManeuver maneuver);

    /**
     * The voice instructions object is useful for navigation sessions providing well spoken text
     * instructions along with the distance from the maneuver the instructions should be said.
     *
     * @param voiceInstructions a list of voice instructions which can be triggered on this current
     *                          step
     * @since 3.0.0
     */
    public abstract Builder voiceInstructions(@Nullable List<VoiceInstructions> voiceInstructions);

    public abstract Builder bannerInstructions(
      @Nullable List<BannerInstructions> bannerInstructions);

    /**
     * Specifies a decimal precision of edge weights, default value 1.
     *
     * @param weight a decimal precision double value
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder weight(@Nullable Double weight);

    /**
     * Provide a list of all the intersections connected to the current way the user is traveling
     * along.
     *
     * @param intersections list of {@link StepIntersection} representing all intersections along
     *                      the step
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder intersections(@Nullable List<StepIntersection> intersections);

    /**
     * String with the exit numbers or names of the way. Optionally included, if data is available.
     *
     * @param exits a String identifying the exit number or name
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder exits(@Nullable String exits);

    /**
     * Build a new {@link LegStep} object.
     *
     * @return a new {@link LegStep} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract LegStep build();
  }
}
