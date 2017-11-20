package com.mapbox.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;

@AutoValue
public abstract class BannerInstructions implements Serializable {

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

  @Nullable
  public abstract BannerText secondary();

  @Nullable
  public abstract String turnIcon();

  public static TypeAdapter<BannerInstructions> typeAdapter(Gson gson) {
    return new AutoValue_BannerInstructions.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder distanceAlongGeometry(double distanceAlongGeometry);

    public abstract Builder primary(@Nullable BannerText primary);

    public abstract Builder secondary(@Nullable BannerText secondary);

    public abstract Builder turnIcon(@Nullable String turnIcon);

    public abstract BannerInstructions build();
  }
}
