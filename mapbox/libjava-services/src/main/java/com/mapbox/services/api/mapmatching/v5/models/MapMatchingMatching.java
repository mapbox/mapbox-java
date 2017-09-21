package com.mapbox.services.api.mapmatching.v5.models;

import com.mapbox.services.api.directions.v5.models.DirectionsRoute;

/**
 * A match object is a {@link DirectionsRoute} object with an additional confidence field.
 */
public class MapMatchingMatching extends DirectionsRoute {

  private double confidence;

  public MapMatchingMatching() {
  }

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    MapMatchingMatching that = (MapMatchingMatching) o;

    return Double.compare(that.getConfidence(), getConfidence()) == 0;

  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + Double.valueOf(getConfidence()).hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "MapMatchingMatching{"
      + "confidence=" + confidence
      + '}';
  }
}
