package com.mapbox.api.tilequery.models;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory
public abstract class TilequeryAdapterFactory implements TypeAdapterFactory {
  public static TypeAdapterFactory create() {
    return AutoValueGson_TilequeryAdapterFactory.create();
  }
}
