package com.mapbox.api.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.MapboxDirections;

import java.io.Serializable;
import java.util.List;

/**
 * Includes both plain text information that can be visualized inside your navigation application
 * along with the text string broken down into {@link BannerComponents} which may or may not
 * include a image url. To receive this information, your request must have
 * {@link MapboxDirections#bannerInstructions()} set to true.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class BannerText implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_BannerText.Builder();
  }

  /**
   * Plain text with all the {@link BannerComponents} text combined.
   *
   * @return plain text with all the {@link BannerComponents} text items combined
   * @since 3.0.0
   */
  @Nullable
  public abstract String text();

  /**
   * A part or element of the {@link BannerInstructions}.
   *
   * @return a {@link BannerComponents} specific to a {@link LegStep}
   * @since 3.0.0
   */
  @Nullable
  public abstract List<BannerComponents> components();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<BannerText> typeAdapter(Gson gson) {
    return new AutoValue_BannerText.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link BannerText}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Plain text with all the {@link BannerComponents} text combined.
     *
     * @param text plain text with all the {@link BannerComponents} text items combined
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @Nullable
    public abstract Builder text(String text);

    /**
     * A part or element of the {@link BannerInstructions}.
     *
     * @param components a {@link BannerComponents} specific to a {@link LegStep}
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @Nullable
    public abstract Builder components(List<BannerComponents> components);

    /**
     * Build a new {@link BannerText} object.
     *
     * @return a new {@link BannerText} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract BannerText build();
  }
}
