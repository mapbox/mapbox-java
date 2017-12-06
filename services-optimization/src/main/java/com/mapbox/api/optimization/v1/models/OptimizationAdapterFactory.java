package com.mapbox.api.optimization.v1.models;


import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * Required so that AutoValue can generate specific type adapters when needed inside the
 * optimization packages.
 *
 * @since 3.0.0
 */
@GsonTypeAdapterFactory
public abstract class OptimizationAdapterFactory implements TypeAdapterFactory {

  /**
   * Creates a TypeAdapter that AutoValues uses to generate specific type adapters when needed
   * inside the optimization package classes.
   *
   * @return 3.0.0
   */
  public static TypeAdapterFactory create() {
    return new AutoValueGson_OptimizationAdapterFactory();
  }
}