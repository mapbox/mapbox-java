package com.mapbox.services.api.directions.v5.models;

import java.util.List;

/**
 * A route between only two {@link DirectionsWaypoint}.
 *
 * @since 1.0.0
 */
public class RouteLeg {

  private double distance;
  private double duration;
  private String summary;
  private List<LegStep> steps;

  public RouteLeg() {
  }

  /**
   * The distance traveled from one waypoint to another.
   *
   * @return a double number with unit meters.
   * @since 1.0.0
   */
  public double getDistance() {
    return distance;
  }

  /**
   * The estimated travel time from one waypoint to another.
   *
   * @return a double number with unit seconds.
   * @since 1.0.0
   */
  public double getDuration() {
    return duration;
  }

  /**
   * A short human-readable summary of major roads traversed. Useful to distinguish alternatives.
   *
   * @return String with summary.
   * @since 1.0.0
   */
  public String getSummary() {
    return summary;
  }

  /**
   * Gives a List including all the steps to get from one waypoint to another.
   *
   * @return List of {@link LegStep}.
   * @since 1.0.0
   */
  public List<LegStep> getSteps() {
    return steps;
  }
}
