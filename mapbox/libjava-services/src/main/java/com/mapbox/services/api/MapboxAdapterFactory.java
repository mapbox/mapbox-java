package com.mapbox.services.api;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * This is a custom type adapter factory for AutoValue and eliminates the need to manually add large
 * amounts of type adapters to our Gson builders.
 * <p>
 * For Internal Use only.
 *
 * @since 3.0.0
 */
@GsonTypeAdapterFactory
public abstract class MapboxAdapterFactory implements TypeAdapterFactory {

  /**
   * This is a custom type adapter factory for AutoValue and eliminates the need to manually add
   * large amounts of type adapters to our Gson builders.
   * <p>
   * For Internal Use only.
   *
   * @since 3.0.0
   */
  public static TypeAdapterFactory create() {
    return new AutoValueGson_MapboxAdapterFactory();
  }
}
