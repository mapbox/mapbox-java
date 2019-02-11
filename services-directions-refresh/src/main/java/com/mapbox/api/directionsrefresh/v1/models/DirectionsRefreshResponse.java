package com.mapbox.api.directionsrefresh.v1.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.models.DirectionsJsonObject;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

@AutoValue
public abstract class DirectionsRefreshResponse extends DirectionsJsonObject {

  @Nullable //todo change to @NonNull
  public abstract String code();

  @Nullable
  public abstract String message();

  @Nullable
  public abstract DirectionsRoute route();

  @NonNull
  public static Builder builder() {
    return new AutoValue_DirectionsRefreshResponse.Builder();
  }

  public static TypeAdapter<DirectionsRefreshResponse> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsRefreshResponse.GsonTypeAdapter(gson);
  }

  public static DirectionsRefreshResponse fromJson(String json) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapterFactory(DirectionsRefreshAdapterFactory.create())
      .registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gsonBuilder.create().fromJson(json, DirectionsRefreshResponse.class);
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder code(String code);

    public abstract Builder message(String message);

    public abstract Builder route(DirectionsRoute directionsRoute);

    public abstract DirectionsRefreshResponse build();
  }
}
