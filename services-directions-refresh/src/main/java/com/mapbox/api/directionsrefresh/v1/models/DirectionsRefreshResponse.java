package com.mapbox.api.directionsrefresh.v1.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import java.io.Serializable;

@AutoValue
public abstract class DirectionsRefreshResponse implements Serializable {

  public abstract String code();

  public abstract String message();

  public abstract DirectionsRoute route();

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

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder code(String code);

    public abstract Builder message(String message);

    public abstract Builder route(DirectionsRoute directionsRoute);

    public abstract DirectionsRefreshResponse build();
  }
}
