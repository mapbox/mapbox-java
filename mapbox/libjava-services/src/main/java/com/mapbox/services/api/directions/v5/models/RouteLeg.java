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
  private LegAnnotation annotation;

  /**
   * Empty constructor
   *
   * @since 2.0.0
   */
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
   * The distance traveled from one waypoint to another.
   *
   * @param distance a double number with unit meters.
   * @since 2.1.0
   */
  public void setDistance(double distance) {
    this.distance = distance;
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
   * The estimated travel time from one waypoint to another.
   *
   * @param duration a double number with unit seconds.
   * @since 2.1.0
   */
  public void setDuration(double duration) {
    this.duration = duration;
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
   * A short human-readable summary of major roads traversed. Useful to distinguish alternatives.
   *
   * @param summary a String with a human-readable summary.
   * @since 2.1.0
   */
  public void setSummary(String summary) {
    this.summary = summary;
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

  /**
   * Gives a List including all the steps to get from one waypoint to another.
   *
   * @param steps a List of {@link LegStep}.
   * @since 2.1.0
   */
  public void setSteps(List<LegStep> steps) {
    this.steps = steps;
  }

  /**
   * An {@link LegAnnotation} that contains additional details about each line segment along the route geometry. If
   * you'd like to receiving this, you must request it inside your Directions request before executing the call.
   *
   * @return a {@link LegAnnotation} object.
   * @since 2.1.0
   */
  public LegAnnotation getAnnotation() {
    return annotation;
  }

  /**
   * Provide An {@link LegAnnotation} that contains additional details about each line segment along the route geometry.
   *
   * @param annotation a {@link LegAnnotation} object.
   * @since 2.1.0
   */
  public void setAnnotation(LegAnnotation annotation) {
    this.annotation = annotation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RouteLeg routeLeg = (RouteLeg) o;

    if (Double.compare(routeLeg.getDistance(), getDistance()) != 0) {
      return false;
    }
    if (Double.compare(routeLeg.getDuration(), getDuration()) != 0) {
      return false;
    }
    if (getSummary() != null ? !getSummary().equals(routeLeg.getSummary()) : routeLeg.getSummary() != null) {
      return false;
    }
    if (getSteps() != null ? !getSteps().equals(routeLeg.getSteps()) : routeLeg.getSteps() != null) {
      return false;
    }
    return getAnnotation() != null
      ? getAnnotation().equals(routeLeg.getAnnotation()) : routeLeg.getAnnotation() == null;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + Double.valueOf(getDistance()).hashCode();
    result = 31 * result + Double.valueOf(getDuration()).hashCode();
    result = 31 * result + (getSummary() != null ? getSummary().hashCode() : 0);
    result = 31 * result + (getSteps() != null ? getSteps().hashCode() : 0);
    result = 31 * result + (getAnnotation() != null ? getAnnotation().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "RouteLeg{"
      + "distance=" + distance
      + ", duration=" + duration
      + ", summary='" + summary + '\''
      + ", steps=" + steps
      + ", annotation=" + annotation
      + '}';
  }
}
