package com.mapbox.services.api.mapmatching.v5.models;

import com.mapbox.services.api.directions.v5.models.DirectionsRoute;

/**
 * A match object is a {@link DirectionsRoute} object with an additional confidence field.
 */
public class MapMatchingMatching extends DirectionsRoute {

  private double confidence;

  /**
   * A number between 0 (low) and 1 (high) indicating level of confidence in the returned match
   *
   * @return confidence value
   */
  public double getConfidence() {
    return confidence;
  }

  /**
   * Set the confidence value
   *
   * @param confidence value
   */
  public void setConfidence(double confidence) {
    this.confidence = confidence;
  }
}
