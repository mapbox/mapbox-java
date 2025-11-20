package com.mapbox.api.directions.v5.models;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

import java.util.Map;

/**
 * Object representing metadata associated with a route request.
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
public abstract class Metadata extends DirectionsJsonObject {

  /**
   * Create a new instance of this class by using the {@link Metadata.Builder} class.
   *
   * @return {@link Metadata.Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_Metadata.Builder();
  }

  /**
   * Map containing a key/value pair of all the properties inside `map` json element including
   * map sources, version information etc. Metadata map is generic and can give access to many
   * different key/value pairs other than the ones specified.
   * @return map of key/value pairs
   */
  @Nullable
  @SerializedName("map")
  public abstract Map<String, String> infoMap();

  /**
   * Convert the current {@link Metadata} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link Metadata}.
   *
   * @return a {@link Metadata.Builder} with the same values set to match the ones defined
   *   in this {@link Metadata}
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<Metadata> typeAdapter(Gson gson) {
    return new AutoValue_Metadata.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a Metadata
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  public static Metadata fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, Metadata.class);
  }

  /**
   * This builder can be used to set the values describing the {@link Metadata}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * Map indicating the key/value pairs.
     *
     * @param mapInfo indicating the key/value pairs
     * @return a {@link Metadata.Builder} object
     */
    public abstract Builder infoMap(Map<String, String> mapInfo);

    /**
     * Build a new {@link Metadata} object.
     *
     * @return a new {@link Metadata} using the provided values in this builder
     */
    public abstract Metadata build();
  }
}
