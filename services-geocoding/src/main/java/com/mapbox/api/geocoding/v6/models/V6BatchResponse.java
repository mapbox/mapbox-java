package com.mapbox.api.geocoding.v6.models;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.geocoding.v6.V6GeocodingAdapterFactory;
import com.mapbox.geojson.GeometryAdapterFactory;

import java.io.Serializable;
import java.util.List;

/**
 * This is the initial object which gets returned when the batch geocoding request
 * receives a result.
 */
@AutoValue
public abstract class V6BatchResponse implements Serializable {

  /**
   * A list of {@link V6Response}, one {@link V6Response} for each of
   * {@link com.mapbox.api.geocoding.v6.V6RequestOptions}.
   *
   * @return a list of {@link V6Response}s
   */
  @SerializedName("batch")
  @NonNull
  public abstract List<V6Response> responses();

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a GeoJson Geocoding Response
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  @NonNull
  public static V6BatchResponse fromJson(@NonNull String json) {
    final Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(GeometryAdapterFactory.create())
      .registerTypeAdapterFactory(V6GeocodingAdapterFactory.create())
      .create();
    return gson.fromJson(json, V6BatchResponse.class);
  }

  /**
   * Gson TYPE adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   */
  public static TypeAdapter<V6BatchResponse> typeAdapter(Gson gson) {
    return new AutoValue_V6BatchResponse.GsonTypeAdapter(gson);
  }
}
