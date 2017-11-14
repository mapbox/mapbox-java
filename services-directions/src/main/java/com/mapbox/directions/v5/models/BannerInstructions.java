package com.mapbox.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class BannerInstructions {

  public static Builder builder() {
    return new AutoValue_BannerInstructions.Builder();
  }

  public abstract double distanceAlongGeometry();

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
