package com.mapbox.services.api.directions.v5.models;

/**
 * An annotations object that contains additional details about each line segment along the route geometry. Each entry
 * in an annotations field corresponds to a coordinate along the route geometry.
 *
 * @since 2.1.0
 */
public class LegAnnotation {

  private double[] distance;
  private double[] duration;
  private double[] speed;

  public LegAnnotation() {
  }

  /**
   * The distance, in meters, between each pair of coordinates.
   *
   * @return a double array with each entry being a distance value between two of the routeLeg geometry coordinates.
   * @since 2.1.0
   */
  public double[] getDistance() {
    return distance;
  }

  /**
   * The duration, in seconds, between each pair of coordinates.
   *
   * @return a double array with each entry being a duration value between two of the routeLeg geometry coordinates.
   * @since 2.1.0
   */
  public double[] getDuration() {
    return duration;
  }

  /**
   * The speed, in km/h, between each pair of coordinates.
   *
   * @return a double array with each entry being a speed value between two of the routeLeg geometry coordinates.
   * @since 2.1.0
   */
  public double[] getSpeed() {
    return speed;
  }
}
