package com.mapbox.services.api.directions.v5.models;

import com.google.gson.annotations.SerializedName;

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
  private double weight;
  @SerializedName("weight_name")
  private String weightName;
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
   *
   * @param distance a double number with unit meters.
   * @since 2.1.0
   */
  public void setDistance(double distance) {
    this.distance = distance;
  }

  /**
   * The name of the weight profile used while calculating during extraction phase. In many cases, this will return
   * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#WEIGHT_NAME_ROUTABILITY},
   * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#WEIGHT_NAME_DURATION}, or
   * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#WEIGHT_NAME_DISTANCE}.
   *
   * @return a String representing the weight profile used while calculating the route.
   * @since 2.1.0
   */
  public String getWeightName() {
    return weightName;
  }

  /**
   * The name of the weight profile used while calculating during extraction phase. In many cases, this would be
   * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#WEIGHT_NAME_ROUTABILITY},
   * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#WEIGHT_NAME_DURATION}, or
   * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#WEIGHT_NAME_DISTANCE}.
   *
   * @param weightName a String representing the weight profile used while calculating the route.
   * @since 2.1.0
   */
  public void setWeightName(String weightName) {
    this.weightName = weightName;
  }

  /**
   * The calculated weight of the route.
   *
   * @return the weight value provided from the API as a {@code double} value.
   * @since 2.1.0
   */
  public double getWeight() {
    return weight;
  }

  /**
   * The calculated weight of the route.
   *
   * @param weight the weight value as a {@code double} value.
   * @since 2.1.0
   */
  public void setWeight(double weight) {
    this.weight = weight;
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
   *
   * @param duration a double number with unit seconds.
   * @since 2.1.0
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
   *
   * @param geometry an encoded polyline string.
   * @since 2.1.0
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
   *
   * @param legs list of {@link RouteLeg} objects.
   * @since 2.1.0
   */
  public void setLegs(List<RouteLeg> legs) {
    this.legs = legs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DirectionsRoute that = (DirectionsRoute) o;

    if (Double.compare(that.getDistance(), getDistance()) != 0) {
      return false;
    }
    if (Double.compare(that.getDuration(), getDuration()) != 0) {
      return false;
    }
    if (Double.compare(that.getWeight(), getWeight()) != 0) {
      return false;
    }
    if (!getGeometry().equals(that.getGeometry())) {
      return false;
    }
    if (!getWeightName().equals(that.getWeightName())) {
      return false;
    }
    return getLegs().equals(that.getLegs());
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + Double.valueOf(getDistance()).hashCode();
    result = 31 * result + Double.valueOf(getDuration()).hashCode();
    result = 31 * result + getGeometry().hashCode();
    result = 31 * result + Double.valueOf(getWeight()).hashCode();
    result = 31 * result + getWeightName().hashCode();
    result = 31 * result + getLegs().hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "DirectionsRoute{"
      + "distance=" + distance
      + ", duration=" + duration
      + ", geometry='" + geometry + '\''
      + ", weight=" + weight
      + ", weightName='" + weightName + '\''
      + ", legs=" + legs
      + '}';
  }
}
