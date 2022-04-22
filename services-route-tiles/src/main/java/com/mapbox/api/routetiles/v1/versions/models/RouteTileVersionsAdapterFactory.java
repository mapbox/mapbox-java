package com.mapbox.api.routetiles.v1.versions.models;

import com.google.gson.TypeAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterFactory;

/**
 * Required so that AutoValue can generate specific type adapters when needed inside the direction
 * packages.
 *
 * @since 4.1.0
 */
@GsonTypeAdapterFactory
public abstract class RouteTileVersionsAdapterFactory implements TypeAdapterFactory {

  /**
   * Creates a TypeAdapter that AutoValues uses to generate specific type adapters when needed
   * inside the direction package classes.
   *
   * @return autovalue type adapter factory
   * @since 4.1.0
   */
  public static TypeAdapterFactory create() {
    return new AutoValueGson_RouteTileVersionsAdapterFactory();
  }
}
