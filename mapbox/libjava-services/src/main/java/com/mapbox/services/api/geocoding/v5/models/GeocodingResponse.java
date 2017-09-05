package com.mapbox.services.api.geocoding.v5.models;

import java.util.List;

/**
 * This is the initial object which gets returned when the geocoding request receives a result.
 * Since each result is a {@link CarmenFeature}, the response simply returns a list of those
 * features.
 *
 * @since 1.0.0
 */
public class GeocodingResponse extends CarmenFeatureCollection {

  /**
   * TODO
   *
   *
   * @param features a list of Carmen Features which contains the results from the geocoding request
   * @since 1.0.0
   */
  public GeocodingResponse(List<CarmenFeature> features) {
    super(features);
  }

}
