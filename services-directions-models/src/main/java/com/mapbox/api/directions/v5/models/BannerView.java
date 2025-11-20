package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

import java.util.List;

/**
 * Includes both plain text information that can be visualized inside your navigation application
 * along with the text string broken down into {@link BannerComponents} which may or may not
 * include a image url. To receive this information, your request must have
 * <tt>MapboxDirections.Builder#bannerInstructions()</tt> set to true.
 *
 * @since 5.0.0
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
public abstract class BannerView extends DirectionsJsonObject {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 5.0.0
   */
  public static Builder builder() {
    return new AutoValue_BannerView.Builder();
  }

  /**
   * Plain text with all the {@link BannerComponents} text combined.
   *
   * @return plain text with all the {@link BannerComponents} text items combined
   * @since 5.0.0
   */
  @NonNull
  public abstract String text();

  /**
   * A part or element of the {@link BannerInstructions}.
   *
   * @return a {@link BannerComponents} specific to a {@link LegStep}
   * @since 5.0.0
   */
  @Nullable
  public abstract List<BannerComponents> components();

  /**
   * This indicates the type of maneuver.
   *
   * @return String with type of maneuver
   * @see StepManeuver.StepManeuverType
   * @since 5.0.0
   */
  @Nullable
  @StepManeuver.StepManeuverType
  public abstract String type();

  /**
   * This indicates the mode of the maneuver. If type is of turn, the modifier indicates the
   * change in direction accomplished through the turn. If the type is of depart/arrive, the
   * modifier indicates the position of waypoint from the current direction of travel.
   *
   * @return String with modifier
   * @since 5.0.0
   */
  @Nullable
  @ManeuverModifier.Type
  public abstract String modifier();

  /**
   * Convert the current {@link BannerView} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link BannerView}.
   *
   * @return a {@link BannerView.Builder} with the same values set to match the ones defined
   *   in this {@link BannerView}
   * @since 5.0.0
   */
  public abstract BannerView.Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 5.0.0
   */
  public static TypeAdapter<BannerView> typeAdapter(Gson gson) {
    return new AutoValue_BannerView.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a BannerText
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 5.0.0
   */
  public static BannerView fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, BannerView.class);
  }

  /**
   * This builder can be used to set the values describing the {@link BannerView}.
   *
   * @since 5.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * Plain text with all the {@link BannerComponents} text combined.
     *
     * @param text plain text with all the {@link BannerComponents} text items combined
     * @return this builder for chaining options together
     * @since 5.0.0
     */
    public abstract Builder text(@NonNull String text);

    /**
     * A part or element of the {@link BannerInstructions}.
     *
     * @param components a {@link BannerComponents} specific to a {@link LegStep}
     * @return this builder for chaining options together
     * @since 5.0.0
     */
    public abstract Builder components(List<BannerComponents> components);

    /**
     * This indicates the type of maneuver. See {@link BannerView#type()} for a full list of
     * options.
     *
     * @param type String with type of maneuver
     * @return this builder for chaining options together
     * @see StepManeuver.StepManeuverType
     * @since 5.0.0
     */
    public abstract Builder type(@Nullable @StepManeuver.StepManeuverType  String type);

    /**
     * This indicates the mode of the maneuver. If type is of turn, the modifier indicates the
     * change in direction accomplished through the turn. If the type is of depart/arrive, the
     * modifier indicates the position of waypoint from the current direction of travel.
     *
     * @param modifier String with modifier
     * @return this builder for chaining options together
     * @since 5.0.0
     */
    public abstract Builder modifier(@Nullable String modifier);

    /**
     * Build a new {@link BannerView} object.
     *
     * @return a new {@link BannerView} using the provided values in this builder
     * @since 5.0.0
     */
    public abstract BannerView build();
  }
}
