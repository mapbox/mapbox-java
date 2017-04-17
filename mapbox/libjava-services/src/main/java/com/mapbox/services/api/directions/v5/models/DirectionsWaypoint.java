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

  /**
   * Empty constructor
   *
   * @since 2.0.0
   */
  public DirectionsWaypoint() {
  }

  /**
   * Provides the way name which the waypoint's coordinate is snapped to.
   *
   * @return String with the name of the way the coordinate snapped to.
   * @since 1.0.0
   */
  public String getName() {
    return name;
  }

  /**
   * Provide a way name which the waypoint's coordinate is snapped to.
   *
   * @param name a String with the name of the way the coordinate snapped to.
   * @since 2.1.0
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * an array with two double values reresenting the maneuver locations coordinate.
   *
   * @return double array of [longitude, latitude] for the snapped coordinate.
   * @since 1.0.0
   */
  public double[] getLocation() {
    return location;
  }

  /**
   * Sets double array of [longitude, latitude] for the snapped coordinate.
   *
   * @param location array with the order [longitude, latitude].
   * @since 2.1.0
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
