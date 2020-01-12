package com.mapbox.api.directions.v5.models;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * Required so that AutoValue can generate specific type adapters when needed inside the direction
 * packages.
 *
 * @since 4.8.0
 */
@GsonTypeAdapterFactory
public abstract class WalkingOptionsAdapterFactory implements TypeAdapterFactory {

  /**
   * Create a new instance of this WalkingOptions type adapter factory. This is passed into the Gson
   * Builder.
   *
   * @return a new GSON TypeAdapterFactory
   * @since 4.8.0
   */
  public static TypeAdapterFactory create() {
    return new AutoValueGson_WalkingOptionsAdapterFactory();
  }
}
