package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

/**
 * An object containing information about amenities available at rest stops along the route.
 * Only available on the {@link DirectionsCriteria#PROFILE_DRIVING} profile.
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
public abstract class Amenity extends DirectionsJsonObject {

  /**
   * The type of amenities such as gas, restaurants, shopping, bank, atm etc.
   * For a list of supported amenity types
   * see {@link com.mapbox.api.directions.v5.DirectionsCriteria.AmenityTypeCriteria}.
   */
  @NonNull
  @DirectionsCriteria.AmenityTypeCriteria
  public abstract String type();

  /**
   * The name of the amenity. Optionally included if data is available.
   */
  @Nullable
  public abstract String name();

  /**
   * The brand name of the amenity. Optionally included if data is available.
   */
  @Nullable
  public abstract String brand();

  /**
   * Create a new instance of this class by using the {@link Amenity.Builder} class.
   *
   * @return this classes {@link Amenity.Builder} for creating a new instance
   */
  public static Amenity.Builder builder() {
    return new AutoValue_Amenity.Builder();
  }

  /**
   * Convert the current {@link Amenity} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link Amenity}.
   *
   * @return a {@link Amenity.Builder} with the same values set to match the ones
   *   defined in this {@link Amenity}
   */
  public abstract Amenity.Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<Amenity> typeAdapter(Gson gson) {
    return new AutoValue_Amenity.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining an Incident
   * @return a new instance of this class defined by the values passed in the method
   */
  public static Amenity fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, Amenity.class);
  }

  /**
   * This builder can be used to set the values describing the {@link Amenity}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Amenity.Builder> {

    /**
     * The type of amenity, includes amenities such as gas, restaurants etc.
     * Note that adding new possible types is not considered a breaking change.
     *
     * @param type amenity type
     */
    @NonNull
    public abstract Builder type(@NonNull String type);

    /**
     * The name of the amenity. Optionally included if data is available.
     *
     * @param name amenity name
     */
    @NonNull
    public abstract Builder name(@Nullable String name);

    /**
     * The brand name of the amenity. Optionally included if data is available.
     *
     * @param brand amenity brand name
     */
    @NonNull
    public abstract Builder brand(@Nullable String brand);

    /**
     * Build a new {@link Amenity} object.
     *
     * @return a new {@link Amenity} using the provided values in this builder
     */
    @NonNull
    public abstract Amenity build();
  }
}
