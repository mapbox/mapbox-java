package com.mapbox.api.geocoding.v6;

import com.google.gson.TypeAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterFactory;

/**
 * A Geocoding type adapter factory for convenience when using AutoValue and handling
 * serialization/deserialization. The majority of this class gets generated during compilation time.
 */
@GsonTypeAdapterFactory
public abstract class V6GeocodingAdapterFactory implements TypeAdapterFactory {

  /**
   * Create a new instance of this type adapter factory.
   *
   * @return a new GSON TypeAdapterFactory
   */
  public static TypeAdapterFactory create() {
    return new AutoValueGson_V6GeocodingAdapterFactory();
  }
}
