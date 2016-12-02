package com.mapbox.services.directions.v4.models;

import com.google.gson.annotations.SerializedName;

/**
 * The instructions for a route are broken down into steps with each step containing details like
 * heading, direction, way name, and duration.
 *
 * @since 1.0.0
 */
@Deprecated
public class RouteStep {

  private int distance;
  private int duration;
  @SerializedName("wayName")
  private String wayName;
  private String direction;
  private double heading;
  private StepManeuver maneuver;

  /**
   * The distance of travel from the maneuver to the subsequent step.
   *
   * @return integer distance given in meters.
   * @since 1.0.0
   */
  public int getDistance() {
    return distance;
  }

  /**
   * @param distance integer distance given in meters.
   * @since 1.0.0
   */
  public void setDistance(int distance) {
    this.distance = distance;
  }

  /**
   * The estimated travel time from the maneuver to the subsequent step.
   *
   * @return integer number given in seconds.
   * @since 1.0.0
   */
  public int getDuration() {
    return duration;
  }

  /**
   * @param duration integer number given in seconds.
   * @since 1.0.0
   */
  public void setDuration(int duration) {
    this.duration = duration;
  }

  /**
   * The name of the way along which travel proceeds.
   *
   * @return string containing a name.
   * @since 1.0.0
   */
  public String getWayName() {
    return wayName;
  }

  /**
   * @param wayName string containing a name.
   * @since 1.0.0
   */
  public void setWayName(String wayName) {
    this.wayName = wayName;
  }

  /**
   * The approximate cardinal direction of travel following the maneuver. Typically one of the
   * following: 'N', 'NE', 'E', 'SE', 'S', 'SW', 'W', or 'NW'.
   *
   * @return string containing abbreviated cardinal direction.
   * @since 1.0.0
   */
  public String getDirection() {
    return direction;
  }

  /**
   * @param direction string containing abbreviated cardinal direction.
   * @since 1.0.0
   */
  public void setDirection(String direction) {
    this.direction = direction;
  }

  /**
   * The clockwise angle from true north to the direction of travel immediately following the maneuver.
   *
   * @return double value ranging from 0 to 359.
   * @since 1.0.0
   */
  public double getHeading() {
    return heading;
  }

  /**
   * @param heading Double value ranging from 0 to 359.
   * @since 1.0.0
   */
  public void setHeading(double heading) {
    this.heading = heading;
  }

  /**
   * A {@link StepManeuver} object representing the step maneuver.
   *
   * @return a {@link StepManeuver} object.
   * @since 1.0.0
   */
  public StepManeuver getManeuver() {
    return maneuver;
  }

  /**
   * @param maneuver A {@link StepManeuver} object.
   * @since 1.0.0
   */
  public void setManeuver(StepManeuver maneuver) {
    this.maneuver = maneuver;
  }
}
