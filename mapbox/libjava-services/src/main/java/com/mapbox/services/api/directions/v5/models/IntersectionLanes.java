package com.mapbox.services.api.directions.v5.models;

/**
 * Object representing lanes in an intersection.
 *
 * @since 2.0.0
 */
public class IntersectionLanes {

  private boolean valid;
  private String[] indications;

  public IntersectionLanes() {
  }

  public IntersectionLanes(boolean valid) {
    this.valid = valid;
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
   * Sets Boolean value for whether this lane can be taken to complete the maneuver. For
   * instance, if the lane array has four objects and the first two are marked as valid, then the
   * driver can take either of the left lanes and stay on the route.
   *
   * @since 2.1.0
   */
  public void setValid(boolean valid) {
    this.valid = valid;
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

  /**
   * Sets Array of signs for each turn lane. There can be multiple signs. For example, a turning
   * lane can have a sign with an arrow pointing left and another sign with an arrow pointing
   * straight.
   *
   * @since 2.1.0
   */
  public void setIndications(String[] indications) {
    this.indications = indications;
  }
}
