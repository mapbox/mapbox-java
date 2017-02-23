package com.mapbox.services.api.directions.v5.models;

/**
 * Object representing lanes in an intersection.
 */
public class IntersectionLanes {

  private boolean valid;
  private String[] indications;

  public IntersectionLanes() {
  }

  /**
   * @return Boolean value for whether this lane can be taken to complete the maneuver. For
   * instance, if the lane array has four objects and the first two are marked as valid, then the
   * driver can take either of the left lanes and stay on the route.
   * @since 2.0.0
   */
  public boolean getValid() {
    return valid;
  }

  /**
   * @return Array of signs for each turn lane. There can be multiple signs. For example, a turning
   * lane can have a sign with an arrow pointing left and another sign with an arrow pointing
   * straight.
   * @since 2.0.0
   */
  public String[] getIndications() {
    return indications;
  }

}
