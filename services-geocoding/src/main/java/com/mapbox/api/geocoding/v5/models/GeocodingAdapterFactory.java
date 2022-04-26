package com.mapbox.api.geocoding.v5.models;

import com.google.gson.TypeAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterFactory;

/**
 * A Geocoding type adapter factory for convenience when using AutoValue and handling
 * serialization/deserialization. The majority of this class gets generated during compilation time.
 *
 * @since 3.0.0
 */
@GsonTypeAdapterFactory
public abstract class GeocodingAdapterFactory implements TypeAdapterFactory {

  /**
   * Create a new instance of this Geocoding type adapter factory, this is passed into the Gson
   * Builder.
   *
   * @return a new GSON TypeAdapterFactory
   * @since 3.0.0
   */
  public static TypeAdapterFactory create() {
    return new AutoValueGson_GeocodingAdapterFactory();
  }
}
