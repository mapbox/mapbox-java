package com.mapbox.api.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.MapboxDirections;

import java.io.Serializable;

/**
 * Visual instruction information related to a particular {@link LegStep} useful for making UI
 * elements inside your application such as banners. To receive this information, your request must
 * have {@link MapboxDirections#bannerInstructions()} set to true.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class BannerInstructions implements Serializable {

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
  @Nullable
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
    public abstract Builder primary(@Nullable BannerText primary);

    /**
     * Ancillary visual information about the {@link LegStep}.
     *
     * @param secondary {@link BannerText} representing the secondary visual information
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder secondary(@Nullable BannerText secondary);

    /**
     * Build a new {@link BannerInstructions} object.
     *
     * @return a new {@link BannerInstructions} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract BannerInstructions build();
  }
}
