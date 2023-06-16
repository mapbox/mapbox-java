package com.mapbox.api.geocoding.v6.models;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.geocoding.v6.V6GeocodingAdapterFactory;
import com.mapbox.geojson.GeometryAdapterFactory;

import java.io.Serializable;
import java.util.List;

/**
 * This is the initial object which gets returned when the geocoding request receives a result.
 */
@AutoValue
public abstract class V6Response implements Serializable {

  /**
   * "FeatureCollection",
   * a GeoJSON type from the <a href="https://tools.ietf.org/html/rfc7946">GeoJSON specification</a>.
   *
   * @return the type of GeoJSON
   */
  @NonNull
  public abstract String type();

  /**
   * A list of feature objects.
   * Forward geocodes: Returned features are ordered by relevance.
   * Reverse geocodes: Returned features are ordered by index hierarchy,
   * from most specific features to least specific features that overlap the queried coordinates.
   * <p>
   * Read the
   * <a href="https://docs.mapbox.com/help/getting-started/geocoding/#search-result-prioritization">Search result prioritization</a>
   * guide to learn more about how returned features are organized in the Geocoding API response.
   *
   * @return a list of {@link V6Feature}s
   */
  @NonNull
  public abstract List<V6Feature> features();

  /**
   * Attributes the results of the Mapbox Geocoding API to Mapbox.
   *
   * @return information about Mapbox's terms of service and the data sources
   */
  @NonNull
  public abstract String attribution();

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a GeoJson Geocoding Response
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  @NonNull
  public static V6Response fromJson(@NonNull String json) {
    final Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(GeometryAdapterFactory.create())
      .registerTypeAdapterFactory(V6GeocodingAdapterFactory.create())
      .create();
    return gson.fromJson(json, V6Response.class);
  }

  /**
   * Gson TYPE adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   */
  public static TypeAdapter<V6Response> typeAdapter(Gson gson) {
    return new AutoValue_V6Response.GsonTypeAdapter(gson);
  }
}
