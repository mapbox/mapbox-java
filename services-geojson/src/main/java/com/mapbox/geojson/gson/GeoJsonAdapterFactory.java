package com.mapbox.geojson.gson;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory
public abstract class GeoJsonAdapterFactory implements TypeAdapterFactory {

  public static TypeAdapterFactory create() {
    return new AutoValueGson_GeoJsonAdapterFactory();
  }
}
