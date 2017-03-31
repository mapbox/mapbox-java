package com.mapbox.services.api.directions.v5.models;

import com.mapbox.services.commons.models.Position;

/**
 * An input coordinate snapped to the roads network.
 *
 * @since 1.0.0
 */
public class DirectionsWaypoint {

  private String name;
  private double[] location;

  public DirectionsWaypoint() {
  }

  /**
   * @return String with the name of the way the coordinate snapped to.
   * @since 1.0.0
   */
  public String getName() {
    return name;
  }

  /**
   * Sets String with the name of the way the coordinate snapped to.
   *
   * @since 2.0.1
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return double array of [longitude, latitude] for the snapped coordinate.
   * @since 1.0.0
   */
  public double[] getLocation() {
    return location;
  }

  /**
   * Sets double array of [longitude, latitude] for the snapped coordinate.
   *
   * @since 2.0.1
   */
  public void setLocation(double[] location) {
    this.location = location;
  }

  /**
   * Converts double array {@link #getLocation()} to a {@link Position}. You'll typically want to
   * use this format instead of {@link #getLocation()} as it's easier to work with.
   *
   * @return {@link Position}.
   * @since 1.0.0
   */
  public Position asPosition() {
    return Position.fromCoordinates(location[0], location[1]);
  }
}
