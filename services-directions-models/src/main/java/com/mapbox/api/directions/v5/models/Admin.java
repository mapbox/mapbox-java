package com.mapbox.api.directions.v5.models;

import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;

/**
 * An objects describing the administrative boundaries the route leg travels through.
 */
@AutoValue
public abstract class Admin extends DirectionsJsonObject {

  /**
   * Contains the 2 character ISO 3166-1 alpha-2 code that applies to a country boundary.
   * Example: `"US"`.
   */
  @Nullable
  @SerializedName("iso_3166_1")
  public abstract String countryCode();

  /**
   * Contains the 3 character ISO 3166-1 alpha-3 code that applies to a country boundary.
   * Example: `"USA"`.
   */
  @Nullable
  @SerializedName("iso_3166_1_alpha3")
  public abstract String countryCodeAlpha3();

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_Admin.Builder();
  }

  /**
   * Convert the current {@link Admin} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link Admin}.
   *
   * @return a {@link Builder} with the same values set to match the ones defined in this {@link
   * Admin}
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<Admin> typeAdapter(Gson gson) {
    return new AutoValue_Admin.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining an Incident
   * @return a new instance of this class defined by the values passed in the method
   */
  public static Admin fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, Admin.class);
  }

  /**
   * This builder can be used to set the values describing the {@link Admin}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * The 2 character ISO 3166-1 alpha-2 code that applies to a country boundary.
     * Example: `"US"`.
     *
     * @param countryCode 2 character ISO 3166-1 alpha-2 code
     */
    public abstract Builder countryCode(@Nullable String countryCode);

    /**
     * The 3 character ISO 3166-1 alpha-3 code that applies to a country boundary.
     * Example: `"USA"`.
     *
     * @param countryCodeAlpha3 3 character ISO 3166-1 alpha-3 code
     */
    public abstract Builder countryCodeAlpha3(@Nullable String countryCodeAlpha3);

    /**
     * Build a new {@link Admin} object.
     *
     * @return a new {@link Admin} using the provided values in this builder
     */
    public abstract Admin build();
  }
}
