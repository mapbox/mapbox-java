package com.mapbox.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;
import java.util.List;

@AutoValue
public abstract class BannerText implements Serializable {

  public static Builder builder() {
    return new AutoValue_BannerText.Builder();
  }

  @Nullable
  public abstract String text();

  @Nullable
  public abstract List<BannerComponents> components();

  public static TypeAdapter<BannerText> typeAdapter(Gson gson) {
    return new AutoValue_BannerText.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    @Nullable
    public abstract Builder text(String text);

    @Nullable
    public abstract Builder components(List<BannerComponents> components);

    public abstract BannerText build();
  }
}
