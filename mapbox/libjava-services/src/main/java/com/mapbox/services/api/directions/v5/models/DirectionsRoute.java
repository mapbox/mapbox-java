package com.mapbox.services.api.directions.v5.models;

import java.util.List;

/**
 * Detailed information about an individual route such as the duration, distance and geometry.
 *
 * @since 1.0.0
 */
public class DirectionsRoute {

  private double distance;
  private double duration;
  private String geometry;
  private List<RouteLeg> legs;

  public DirectionsRoute() {
  }

  /**
   * The distance traveled from origin to destination.
   *
   * @return a double number with unit meters.
   * @since 1.0.0
   */
  public double getDistance() {
    return distance;
  }

  /**
   * The distance traveled from origin to destination.
   * <p>
   * Sets a double number with unit meters.
   *
   * @since 2.0.1
   */
  public void setDistance(double distance) {
    this.distance = distance;
  }

  /**
   * The estimated travel time from origin to destination.
   *
   * @return a double number with unit seconds.
   * @since 1.0.0
   */
  public double getDuration() {
    return duration;
  }

  /**
   * The estimated travel time from origin to destination.
   * <p>
   * Sets a double number with unit seconds.
   *
   * @since 2.0.1
   */
  public void setDuration(double duration) {
    this.duration = duration;
  }

  /**
   * Gives the geometry of the route. Commonly used to draw the route on the map view.
   *
   * @return An encoded polyline string.
   * @since 1.0.0
   */
  public String getGeometry() {
    return geometry;
  }

  /**
   * Sets the geometry of the route. Commonly used to draw the route on the map view.
   * <p>
   * Sets An encoded polyline string.
   *
   * @since 2.0.1
   */
  public void setGeometry(String geometry) {
    this.geometry = geometry;
  }


  /**
   * A Leg is a route between only two waypoints
   *
   * @return List of {@link RouteLeg} objects.
   * @since 1.0.0
   */
  public List<RouteLeg> getLegs() {
    return legs;
  }


  /**
   * A Leg is a route between only two waypoints
   * <p>
   * Sets List of {@link RouteLeg} objects.
   *
   * @since 2.0.1
   */
  public void setLegs(List<RouteLeg> legs) {
    this.legs = legs;
  }


}
