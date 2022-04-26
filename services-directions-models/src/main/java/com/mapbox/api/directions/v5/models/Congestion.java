package com.mapbox.api.directions.v5.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;

/**
 * Quantitative descriptor of congestion.
 */
@AutoValue
public abstract class Congestion extends DirectionsJsonObject {

  /**
   * Quantitative descriptor of congestion. 0 to 100.
   */
  public abstract int value();

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_Congestion.Builder();
  }

  /**
   * Convert the current {@link Congestion} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link Congestion}.
   *
   * @return a {@link Builder} with the same values set to match the ones defined in this
   * {@link Congestion}
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<Congestion> typeAdapter(Gson gson) {
    return new AutoValue_Congestion.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining an Congestion
   * @return a new instance of this class defined by the values passed in the method
   */
  public static Congestion fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, Congestion.class);
  }

  /**
   * This builder can be used to set the values describing the {@link Congestion}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * Quantitative descriptor of congestion. 0 to 100.
     * @param value 0 to 100
     */
    public abstract Builder value(int value);

    /**
     * Build a new instance of {@link Congestion}.
     * @return a new instance of {@link Congestion}.
     */
    public abstract Congestion build();
  }
}
