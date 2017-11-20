package com.mapbox.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;

@AutoValue
public abstract class BannerComponents implements Serializable {

  public static Builder builder() {
    return new AutoValue_BannerComponents.Builder();
  }

  @Nullable
  public abstract String text();

  public static TypeAdapter<BannerComponents> typeAdapter(Gson gson) {
    return new AutoValue_BannerComponents.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder text(@Nullable String text);

    public abstract BannerComponents build();
  }
}
