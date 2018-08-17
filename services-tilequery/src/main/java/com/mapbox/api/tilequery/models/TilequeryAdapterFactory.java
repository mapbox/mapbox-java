package com.mapbox.api.tilequery.models;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * A Tilequery type adapter factory for convenience when using AutoValue and handling
 * serialization/deserialization. The majority of this class gets generated during compilation time.
 *
 * @since 3.5.0
 */
@GsonTypeAdapterFactory
public abstract class TilequeryAdapterFactory implements TypeAdapterFactory {

  /**
   * Create a new instance of this Tilequery type adapter factory, this is passed into the Gson
   * Builder.
   *
   * @return a new GSON TypeAdapterFactory
   * @since 3.5.0
   */
  public static TypeAdapterFactory create() {
    return new AutoValueGson_TilequeryAdapterFactory();
  }
}
