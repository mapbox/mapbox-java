package com.mapbox.api.geocoding.v6;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.geojson.GeometryAdapterFactory;

import java.util.Arrays;
import java.util.Collection;

class V6GeocodingUtils {

  @NonNull
  public static String serialize(@NonNull V6RequestOptions... requestOptions) {
    return serialize(Arrays.asList(requestOptions));
  }

  @NonNull
  public static String serialize(@NonNull Collection<? extends V6RequestOptions> requestOptions) {
    final Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(GeometryAdapterFactory.create())
      .registerTypeAdapterFactory(V6GeocodingAdapterFactory.create())
      .create();
    return gson.toJson(requestOptions);
  }
}
