package com.mapbox.api.directions.v5.models;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

/**
 * An object containing detailed information about the road exiting the intersection along the
 * route.
 * Only available on the {@link DirectionsCriteria#PROFILE_DRIVING} profile.
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
public abstract class MapboxStreetsV8 extends DirectionsJsonObject {

  /**
   * The road class of the road exiting the intersection as defined by the
   *<a href="https://docs.mapbox.com/vector-tiles/reference/mapbox-streets-v8/#--road---class-text">
   *   Mapbox Streets v8 road class specification</a>.
   * Valid values are the same as those supported by Mapbox Streets v8.
   * Examples include: `motorway`, `motorway_link`, `primary`, and `street`.
   * Note that adding new possible values is not considered a breaking change.
   *
   * @return class of the road.
   */
  @Nullable
  @SerializedName("class")
  public abstract String roadClass();

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_MapboxStreetsV8.Builder();
  }

  /**
   * Convert the current {@link MapboxStreetsV8} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link MapboxStreetsV8}.
   *
   * @return a {@link Builder} with the same values set to match the ones defined in this {@link
   * MapboxStreetsV8}
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<MapboxStreetsV8> typeAdapter(Gson gson) {
    return new AutoValue_MapboxStreetsV8.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining an Incident
   * @return a new instance of this class defined by the values passed in the method
   */
  public static MapboxStreetsV8 fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, MapboxStreetsV8.class);
  }

  /**
   * This builder can be used to set the values describing the {@link MapboxStreetsV8}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * Class of the road exiting the intersection.
     *
     * @param roadClass class of the road exiting the intersection.
     * @return this builder for chaining options together
     */
    public abstract Builder roadClass(@Nullable String roadClass);

    /**
     * Build a new {@link MapboxStreetsV8} object.
     *
     * @return a new {@link MapboxStreetsV8} using the provided values in this builder
     */
    public abstract MapboxStreetsV8 build();
  }
}
