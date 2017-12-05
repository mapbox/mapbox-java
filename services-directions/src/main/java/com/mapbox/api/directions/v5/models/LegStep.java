package com.mapbox.api.directions.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.gson.BoundingBoxDeserializer;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.geojson.gson.GeometryDeserializer;
import com.mapbox.geojson.gson.PointDeserializer;

import java.io.Serializable;
import java.util.ArrayList;
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
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a GeoJson Directions Response
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static LegStep fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer());
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, LegStep.class);
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
  @NonNull
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
   * Any road designations associated with the road or path leading from this step’s maneuver to the
   * next step’s maneuver. Optionally included, if data is available. If multiple road designations
   * are associated with the road, they are separated by semicolons. A road designation typically
   * consists of an alphabetic network code (identifying the road type or numbering system), a space
   * or hyphen, and a route number. You should not assume that the network code is globally unique:
   * for example, a network code of “NH” may appear on a “National Highway” or “New Hampshire”.
   * Moreover, a route number may not even uniquely identify a road within a given network.
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
   * @return new {@link StepManeuver} object
   * @since 1.0.0
   */
  @NonNull
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

  /**
   * If in your request you set {@link MapboxDirections#bannerInstructions()} to true, you'll
   * receive a list of {@link BannerInstructions} which encompasses all information necessary for
   * creating a visual cue about a given {@link LegStep}.
   *
   * @return a list of {@link BannerInstructions}s which help display visual cues
   *   inside your application
   * @since 3.0.0
   */
  @Nullable
  public abstract List<BannerInstructions> bannerInstructions();

  /**
   * The legal driving side at the location for this step. Result will either be {@code left} or
   * {@code right}.
   *
   * @return a string with either a left or right value
   * @since 3.0.0
   */
  @Nullable
  @SerializedName("driving_side")
  public abstract String drivingSide();

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
    public abstract Builder distance(double distance);

    /**
     * The estimated travel time from the maneuver to the next {@link LegStep}.
     *
     * @param duration a double number with unit seconds
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder duration(double duration);

    /**
     * Gives the geometry of the leg step.
     *
     * @param geometry an encoded polyline string
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder geometry(@NonNull String geometry);

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
    public abstract Builder mode(@NonNull String mode);

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
    public abstract Builder maneuver(@NonNull StepManeuver maneuver);

    /**
     * The voice instructions object is useful for navigation sessions providing well spoken text
     * instructions along with the distance from the maneuver the instructions should be said.
     *
     * @param voiceInstructions a list of voice instructions which can be triggered on this current
     *                          step
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder voiceInstructions(@NonNull List<VoiceInstructions> voiceInstructions);

    /**
     * If in your request you set {@link MapboxDirections#bannerInstructions()} to true, you'll
     * receive a list of {@link BannerInstructions} which encompasses all information necessary for
     * creating a visual cue about a given {@link LegStep}.
     *
     * @param bannerInstructions a list of {@link BannerInstructions}s which help display visual
     *                           cues inside your application
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder bannerInstructions(
      @NonNull List<BannerInstructions> bannerInstructions);

    /**
     * The legal driving side at the location for this step. Result will either be {@code left} or
     * {@code right}.
     *
     * @param drivingSide a string with either a left or right value
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder drivingSide(@Nullable String drivingSide);

    /**
     * Specifies a decimal precision of edge weights, default value 1.
     *
     * @param weight a decimal precision double value
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder weight(double weight);

    /**
     * Provide a list of all the intersections connected to the current way the user is traveling
     * along.
     *
     * @param intersections list of {@link StepIntersection} representing all intersections along
     *                      the step
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder intersections(@NonNull List<StepIntersection> intersections);

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
