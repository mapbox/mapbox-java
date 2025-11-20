package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

/**
 * An object indicating the geometry indexes defining a road closure.
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
public abstract class Closure extends DirectionsJsonObject {

  /**
   * Closure's leg-wise geometry index start point.
   */
  @NonNull
  @SerializedName("geometry_index_start")
  public abstract Integer geometryIndexStart();

  /**
   * Closure's leg-wise geometry index end point.
   */
  @NonNull
  @SerializedName("geometry_index_end")
  public abstract Integer geometryIndexEnd();

  /**
   * Create a new instance of this class by using the {@link Closure.Builder} class.
   *
   * @return this classes {@link Closure.Builder} for creating a new instance
   */
  public static Closure.Builder builder() {
    return new AutoValue_Closure.Builder();
  }

  /**
   * Convert the current {@link Closure} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link Closure}.
   *
   * @return a {@link Closure.Builder} with the same values set to match the ones
   *    defined in this {@link Closure}
   */
  public abstract Closure.Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<Closure> typeAdapter(Gson gson) {
    return new AutoValue_Closure.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining an Incident
   * @return a new instance of this class defined by the values passed in the method
   */
  public static Closure fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, Closure.class);
  }

  /**
   * This builder can be used to set the values describing the {@link Closure}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * Closure's leg-wise geometry index start point.
     *
     * @param geometryIndexStart start index
     */
    public abstract Builder geometryIndexStart(@NonNull Integer geometryIndexStart);

    /**
     * Closure's leg-wise geometry index end point.
     *
     * @param geometryIndexEnd end index
     */
    public abstract Builder geometryIndexEnd(@NonNull Integer geometryIndexEnd);

    /**
     * Build a new {@link Closure} object.
     *
     * @return a new {@link Closure} using the provided values in this builder
     */
    public abstract Closure build();
  }
}
