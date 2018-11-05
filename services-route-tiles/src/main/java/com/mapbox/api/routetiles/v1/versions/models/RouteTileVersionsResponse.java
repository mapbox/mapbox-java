package com.mapbox.api.routetiles.v1.versions.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mapbox.api.routetiles.v1.versions.MapboxRouteTileVersions;

import java.util.List;

/**
 * This is the root Mapbox Route Tile Versions response object, returned by
 * {@link MapboxRouteTileVersions}.
 *
 * @since 4.1.0
 */
@AutoValue
public abstract class RouteTileVersionsResponse {

  /**
   * Returns the list of available versions.
   *
   * @return list of available versions
   * @since 4.1.0
   */
  public abstract List<String> versions();

  /**
   * Build a new {@link RouteTileVersionsResponse} object.
   *
   * @param versions the versions to be included in the response
   * @return response with specified versions
   * @since 4.1.0
   */
  public RouteTileVersionsResponse create(List<String> versions) {
    return new AutoValue_RouteTileVersionsResponse(versions);
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 4.1.0
   */
  public static TypeAdapter<RouteTileVersionsResponse> typeAdapter(Gson gson) {
    return new AutoValue_RouteTileVersionsResponse.GsonTypeAdapter(gson);
  }
}
