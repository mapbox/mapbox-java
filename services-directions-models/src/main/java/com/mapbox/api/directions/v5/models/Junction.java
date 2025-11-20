package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

/*
 * An object containing information about routing and passing junction(s) along the route.
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
@SuppressWarnings({"checkstyle:javadoctype", "checkstyle:javadocmethod"})
public abstract class Junction extends DirectionsJsonObject {

  /*
   * The name of the junction. Optionally included if data is available.
   */
  @Nullable
  public abstract String name();

  /*
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_Junction.Builder();
  }

  /*
   * Convert the current {@link Junction} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link Junction}.
   *
   * @return a {@link Builder} with the same values set to match the ones defined
   *   in this {@link Junction}
   */
  public abstract Builder toBuilder();

  /*
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<Junction> typeAdapter(Gson gson) {
    return new AutoValue_Junction.GsonTypeAdapter(gson);
  }

  /*
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a Junction
   * @return a new instance of this class defined by the values passed in the method
   */
  public static Junction fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, Junction.class);
  }

  /*
   * This builder can be used to set the values describing the {@link Junction}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /*
     * The name of the junction. Optionally included if data is available.
     *
     * @param name junction name
     */
    @NonNull
    public abstract Builder name(@Nullable String name);

    /*
     * Build a new {@link Junction} object.
     *
     * @return a new {@link Junction} using the provided values in this builder
     */
    @NonNull
    public abstract Junction build();
  }
}
