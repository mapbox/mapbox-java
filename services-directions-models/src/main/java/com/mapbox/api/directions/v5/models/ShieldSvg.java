package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;

import java.io.Serializable;

/**
 * ShieldSvg.
 */
@AutoValue
public abstract class ShieldSvg implements Serializable {

  /**
   * Create a new instance of this class by using the {@link ShieldSvg.Builder} class.
   *
   * @return {@link ShieldSvg.Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_ShieldSvg.Builder();
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a shield sprite
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  @NonNull
  public static ShieldSvg fromJson(@NonNull String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, ShieldSvg.class);
  }

  /**
   * SVG.
   */
  @NonNull
  public abstract String svg();

  /**
   * Convert the current {@link ShieldSvg} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link ShieldSvg}.
   *
   * @return a {@link ShieldSvg.Builder} with the same values set to match the ones defined
   *   in this {@link ShieldSvg}
   */
  public abstract Builder toBuilder();

  /**
   * This takes the currently defined values found inside the {@link ShieldSvg} instance and
   * converts it to a {@link ShieldSvg} string.
   *
   * @return a JSON string which represents a {@link ShieldSvg}
   */
  @NonNull
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().toJson(this);
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<ShieldSvg> typeAdapter(Gson gson) {
    return new AutoValue_ShieldSvg.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link ShieldSvg}.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * SVG.
     *
     * @param svg svg
     */
    @NonNull
    public abstract Builder svg(@NonNull String svg);

    /**
     * Build a new {@link ShieldSvg} object.
     *
     * @return a new {@link ShieldSvg} using the provided values in this builder
     */
    @NonNull
    public abstract ShieldSvg build();
  }
}
