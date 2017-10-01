package com.mapbox.matching.v5.models;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory
public abstract class MapMatchingAdapterFactory implements TypeAdapterFactory {

  public static TypeAdapterFactory create() {
    return new AutoValueGson_MapMatchingAdapterFactory();
  }
}