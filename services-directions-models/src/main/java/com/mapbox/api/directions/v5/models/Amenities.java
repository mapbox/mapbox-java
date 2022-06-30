package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;

/**
 * An object containing information about amenities available at rest stops along the route.
 * Only available on the {@link DirectionsCriteria#PROFILE_DRIVING} profile.
 */
@AutoValue
public abstract class Amenities extends DirectionsJsonObject {

  /**
   * The type of amenities such as gas, restaurants, shopping, bank, atm etc.
   * Note that adding new possible types is not considered a breaking change.
   */
  @NonNull
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
   * Create a new instance of this class by using the {@link Amenities.Builder} class.
   *
   * @return this classes {@link Amenities.Builder} for creating a new instance
   */
  public static Amenities.Builder builder() {
    return new AutoValue_Amenities.Builder();
  }

  /**
   * Convert the current {@link Amenities} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link Amenities}.
   *
   * @return a {@link Amenities.Builder} with the same values set to match the ones
   *   defined in this {@link Amenities}
   */
  public abstract Amenities.Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<Amenities> typeAdapter(Gson gson) {
    return new AutoValue_Amenities.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining an Incident
   * @return a new instance of this class defined by the values passed in the method
   */
  public static Amenities fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, Amenities.class);
  }

  /**
   * This builder can be used to set the values describing the {@link Amenities}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Amenities.Builder> {

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
     * Build a new {@link Amenities} object.
     *
     * @return a new {@link Amenities} using the provided values in this builder
     */
    @NonNull
    public abstract Amenities build();
  }
}
