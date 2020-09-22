package com.mapbox.api.directions.v5.models;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;

/**
 * An object containing information about passing rest stops along the route.
 * Only available on the {@link DirectionsCriteria#PROFILE_DRIVING} profile.
 */
@AutoValue
public abstract class RestStop extends DirectionsJsonObject {

  /**
   * The type of rest stop, either `rest_area` (includes parking only) or `service_area`
   * (includes amenities such as gas or restaurants).
   * Note that adding new possible types is not considered a breaking change.
   */
  @Nullable
  public abstract String type();

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_RestStop.Builder();
  }

  /**
   * Convert the current {@link RestStop} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link RestStop}.
   *
   * @return a {@link Builder} with the same values set to match the ones defined in this {@link
   * RestStop}
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<RestStop> typeAdapter(Gson gson) {
    return new AutoValue_RestStop.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining an Incident
   * @return a new instance of this class defined by the values passed in the method
   */
  public static RestStop fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, RestStop.class);
  }

  /**
   * This builder can be used to set the values describing the {@link RestStop}.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * The type of rest stop, either `rest_area` (includes parking only) or `service_area`
     * (includes amenities such as gas or restaurants).
     * Note that adding new possible types is not considered a breaking change.
     *
     * @param type rest stop type
     */
    public abstract Builder type(@Nullable String type);

    /**
     * Build a new {@link RestStop} object.
     *
     * @return a new {@link RestStop} using the provided values in this builder
     */
    public abstract RestStop build();
  }
}
