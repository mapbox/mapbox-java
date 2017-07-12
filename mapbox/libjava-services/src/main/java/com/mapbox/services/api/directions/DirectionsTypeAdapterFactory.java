package com.mapbox.services.api.directions;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory
public abstract class DirectionsTypeAdapterFactory implements TypeAdapterFactory {

  public static DirectionsTypeAdapterFactory create() {
    return new AutoValueGson_DirectionsTypeAdapterFactory();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    Class<? super T> rawType = type.getRawType();
    if (rawType.equals(DirectionsResponse.class)) {
      return (TypeAdapter<T>) DirectionsResponse.typeAdapter(gson);
    }
    return null;
  }
}

