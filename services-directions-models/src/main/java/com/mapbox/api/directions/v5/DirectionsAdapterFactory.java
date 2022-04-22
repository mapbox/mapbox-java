package com.mapbox.api.directions.v5;

import com.google.gson.TypeAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterFactory;

/**
 * Required so that AutoValue can generate specific type adapters when needed inside the direction
 * packages.
 *
 * @since 3.0.0
 */
@GsonTypeAdapterFactory
public abstract class DirectionsAdapterFactory implements TypeAdapterFactory {

  /**
   * Creates a TypeAdapter that AutoValues uses to generate specific type adapters when needed
   * inside the direction package classes.
   *
   * @return 3.0.0
   */
  public static TypeAdapterFactory create() {
    return new AutoValueGson_DirectionsAdapterFactory();
  }
}
