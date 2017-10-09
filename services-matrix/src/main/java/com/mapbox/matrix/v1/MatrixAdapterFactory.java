package com.mapbox.matrix.v1;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * Required so that AutoValue can generate specific type adapters when needed inside the matrix
 * packages.
 *
 * @since 3.0.0
 */
@GsonTypeAdapterFactory
public abstract class MatrixAdapterFactory implements TypeAdapterFactory {

  /**
   * Creates a TypeAdapter that AutoValues uses to generate specific type adapters when needed
   * inside the matrix package classes.
   *
   * @return 3.0.0
   */
  public static TypeAdapterFactory create() {
    return new AutoValueGson_MatrixAdapterFactory();
  }
}