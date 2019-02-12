package com.mapbox.api.directionsrefresh.v5.models;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * Required so that AutoValue can generate specific type adapters when needed inside the
 * directions refresh package.
 *
 * @since 4.4.0
 */
@GsonTypeAdapterFactory
public abstract class DirectionsRefreshAdapterFactory implements TypeAdapterFactory {

  /**
   * Creates a TypeAdapter that AutoValue uses to generate specific type adapters.
   *
   * @return a {@link TypeAdapterFactory} to deserialize {@link DirectionsRefreshResponse}
   * @since 4.4.0
   */
  public static TypeAdapterFactory create() {
    return new AutoValueGson_DirectionsRefreshAdapterFactory();
  }
}
