package com.mapbox.geojson.gson;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * A GeoJson type adapter factory for convenience when using AutoValue and handling
 * serialization/deserialization. The majority of this class gets generated during compilation time.
 *
 * @since 3.0.0
 */
@GsonTypeAdapterFactory
public abstract class GeoJsonAdapterFactory implements TypeAdapterFactory {

  /**
   * Create a new instance of this GeoJson type adapter factory, this is passed into the Gson
   * Builder.
   *
   * @return a new GSON TypeAdapterFactory
   * @since 3.0.0
   */
  public static TypeAdapterFactory create() {
    return new AutoValueGson_GeoJsonAdapterFactory();
  }
}
