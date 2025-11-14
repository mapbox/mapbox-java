package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

/**
 * Holds information about traffic codes. See {@link Incident#trafficCodes()}.
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
public abstract class TrafficCodes extends DirectionsJsonObject {

  /**
   * Jartic cause code value.
   *
   * @return jartic code cause code value
   */
  @Nullable
  @SerializedName("jartic_cause_code")
  public abstract Integer jarticCauseCode();

  /**
   * Jartic regulation code value.
   *
   * @return jartic regulation regulation code value
   */
  @Nullable
  @SerializedName("jartic_regulation_code")
  public abstract Integer jarticRegulationCode();

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this class's {@link Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_TrafficCodes.Builder();
  }

  /**
   * Convert the current {@link TrafficCodes} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link TrafficCodes}.
   *
   * @return a {@link Builder} with the same values set to match the ones defined in this
   * {@link TrafficCodes}
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<TrafficCodes> typeAdapter(Gson gson) {
    return new AutoValue_TrafficCodes.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a TrafficCodes
   * @return a new instance of this class defined by the values passed in the method
   */
  public static TrafficCodes fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, TrafficCodes.class);
  }

  /**
   * This builder can be used to set the values describing the {@link TrafficCodes}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * Sets jartic cause code value.
     *
     * @param value jartic cause code value
     */
    @NonNull
    public abstract Builder jarticCauseCode(@Nullable Integer value);

    /**
     * Sets jartic regulation code value.
     *
     * @param value jartic regulation code value
     */
    @NonNull
    public abstract Builder jarticRegulationCode(@Nullable Integer value);

    /**
     * Build a new instance of {@link TrafficCodes}.
     *
     * @return a new instance of {@link TrafficCodes}.
     */
    @NonNull
    public abstract TrafficCodes build();
  }
}
