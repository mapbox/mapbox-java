package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;

/**
 * Visual instruction information related to a particular {@link LegStep} useful for making UI
 * elements inside your application such as banners. To receive this information, your request must
 * <tt>MapboxDirections.Builder#bannerInstructions()</tt> have set to true.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class BannerInstructions extends DirectionsJsonObject {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_BannerInstructions.Builder();
  }

  /**
   * Distance in meters from the beginning of the step at which the visual instruction should be
   * visible.
   *
   * @return double value representing the length from the steps first point to where the banner
   *   instruction should be displayed
   * @since 3.0.0
   */
  public abstract double distanceAlongGeometry();

  /**
   * A plain text representation stored inside a {@link BannerText} object.
   *
   * @return a {@link BannerText} object which includes text for visually displaying current step
   *   information to the user
   * @since 3.0.0
   */
  @NonNull
  public abstract BannerText primary();

  /**
   * Ancillary visual information about the {@link LegStep}.
   *
   * @return {@link BannerText} representing the secondary visual information
   * @since 3.0.0
   */
  @Nullable
  public abstract BannerText secondary();


  /**
   * Additional information that is included if we feel the driver needs a heads up about something.
   * Can include information about the next maneuver (the one after the upcoming one),
   * if the step is short - can be null, or can be lane information.
   * If we have lane information, that trumps information about the next maneuver.
   *
   * @return {@link BannerText} representing the sub visual information
   * @since 3.2.0
   */
  @Nullable
  public abstract BannerText sub();

  /**
   * Optional image to display for an upcoming maneuver. Used to provide a visual
   * for complex junctions and maneuver. If the step is short the image should be displayed
   * for the duration of the step, otherwise it is shown as you approach the maneuver.
   *
   * @return {@link BannerView} representing the secondary visual information
   * @since 5.0.0
   */
  @Nullable
  public abstract BannerView view();

  /**
   * Convert the current {@link BannerInstructions} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link BannerInstructions}.
   *
   * @return a {@link BannerInstructions.Builder} with the same values set to match the ones defined
   *   in this {@link BannerInstructions}
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
  public static TypeAdapter<BannerInstructions> typeAdapter(Gson gson) {
    return new AutoValue_BannerInstructions.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a BannerInstructions
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.4.0
   */
  public static BannerInstructions fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, BannerInstructions.class);
  }

  /**
   * This builder can be used to set the values describing the {@link BannerInstructions}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Distance in meters from the beginning of the step at which the visual instruction should be
     * visible.
     *
     * @param distanceAlongGeometry double value representing the length from the steps first point
     *                              to where the banner instruction should be displayed
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder distanceAlongGeometry(double distanceAlongGeometry);

    /**
     * Main visual information about the {@link LegStep}.
     *
     * @param primary {@link BannerText} representing the primary visual information
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder primary(@NonNull BannerText primary);

    /**
     * Ancillary visual information about the {@link LegStep}.
     *
     * @param secondary {@link BannerText} representing the secondary visual information
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder secondary(@Nullable BannerText secondary);

    /**
     * Additional information that is included
     * if we feel the driver needs a heads up about something.
     * Can include information about the next maneuver (the one after the upcoming one),
     * if the step is short - can be null, or can be lane information.
     * If we have lane information, that trumps information about the next maneuver.
     *
     * @param sub {@link BannerText} representing the sub visual information
     * @return {@link BannerText} representing the sub visual information
     * @since 3.2.0
     */
    public abstract Builder sub(@Nullable BannerText sub);

    /**
     * Optional image to display for an upcoming maneuver. Used to provide a visual
     * for complex junctions and maneuver. If the step is short the image should be displayed
     * for the duration of the step, otherwise it is shown as you approach the maneuver.
     *
     * @param view {@link BannerView} representing the sub visual information
     * @return this builder for chaining options together
     * @since 5.0.0
     */
    public abstract Builder view(@Nullable BannerView view);

    /**
     * Build a new {@link BannerInstructions} object.
     *
     * @return a new {@link BannerInstructions} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract BannerInstructions build();
  }
}
