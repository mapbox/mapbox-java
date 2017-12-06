package com.mapbox.api.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.MapboxDirections;

import java.io.Serializable;

/**
 * A part of the {@link BannerText} which includes a snippet of the full banner text instruction. In
 * cases where data is avaliable, an image url will be provided to visually include a road shield.
 * To receive this information, your request must have {@link MapboxDirections#bannerInstructions()}
 * set to true.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class BannerComponents implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_BannerComponents.Builder();
  }

  /**
   * A snippet of the full {@link BannerText#text()} which can be used for visually altering parts
   * of the full string.
   *
   * @return a single snippet of the full text instruction
   * @since 3.0.0
   */
  @Nullable
  public abstract String text();

  /**
   * In some cases when the {@link LegStep} is a highway or major roadway, there might be a shield
   * icon that's included to better identify to your user to roadway. Note that this doesn't
   * return the image itself but rather the url which can be used to download the file.
   *
   * @return the url which can be used to download the shield icon if one is avaliable
   * @since 3.0.0
   */
  @Nullable
  @SerializedName("imageBaseURL")
  public abstract String imageBaseUrl();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<BannerComponents> typeAdapter(Gson gson) {
    return new AutoValue_BannerComponents.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link BannerComponents}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * A snippet of the full {@link BannerText#text()} which can be used for visually altering parts
     * of the full string.
     *
     * @param text a single snippet of the full text instruction
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder text(@Nullable String text);

    /**
     * In some cases when the {@link LegStep} is a highway or major roadway, there might be a shield
     * icon that's included to better identify to your user to roadway. Note that this doesn't
     * return the image itself but rather the url which can be used to download the file.
     *
     * @param imageBaseUrl the url which can be used to download the shield icon if one is avaliable
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder imageBaseUrl(@Nullable String imageBaseUrl);

    /**
     * Build a new {@link BannerComponents} object.
     *
     * @return a new {@link BannerComponents} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract BannerComponents build();
  }
}
