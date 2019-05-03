package com.mapbox.api.geocoding.v5.models;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * A Geocoding type adapter factory for convenience when handling
 * serialization/deserialization. The majority of this class gets generated during compilation time.
 *
 * @since 3.0.0
 */
public abstract class GeocodingAdapterFactory implements TypeAdapterFactory {

  /**
   * Create a new instance of this Geocoding type adapter factory, this is passed into the Gson
   * Builder.
   *
   * @return a new GSON TypeAdapterFactory
   * @since 3.0.0
   */
  public static TypeAdapterFactory create() {
    return new GsonGeocodingAdapterFactory();
  }

  /**
   * @since 4.9.0
   */
  private static final class GsonGeocodingAdapterFactory extends GeocodingAdapterFactory {
    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      Class<?> rawType = type.getRawType();
      if (CarmenContext.class.isAssignableFrom(rawType)) {
        return (TypeAdapter<T>) CarmenContext.typeAdapter(gson);
      } else if (CarmenFeature.class.isAssignableFrom(rawType)) {
        return (TypeAdapter<T>) CarmenFeature.typeAdapter(gson);
      } else if (GeocodingResponse.class.isAssignableFrom(rawType)) {
        return (TypeAdapter<T>) GeocodingResponse.typeAdapter(gson);
      } else {
        return null;
      }
    }
  }

}
