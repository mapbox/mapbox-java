package com.mapbox.services.directions.v4.models;

import com.mapbox.services.commons.geojson.LineString;

import java.util.ArrayList;
import java.util.List;

/**
 * Gives detailed information about an individual route such as the duration, distance and geometry.
 *
 * @since 1.0.0
 */
public class DirectionsRoute {

  private int distance;
  private int duration;
  private String summary;
  private String geometry;
  private List<RouteStep> steps;

  public DirectionsRoute() {
    steps = new ArrayList<>();
  }

  /**
   * The distance traveled from origin to destination.
   *
   * @return An integer number with unit meters.
   * @since 1.0.0
   */
  public int getDistance() {
    return distance;
  }

  /**
   * @param distance The distance traveled from origin to destination.
   * @since 1.0.0
   */
  public void setDistance(int distance) {
    this.distance = distance;
  }

  /**
   * The estimated travel time from origin to destination.
   *
   * @return an integer number with unit seconds.
   * @since 1.0.0
   */
  public int getDuration() {
    return duration;
  }

  /**
   * @param duration The estimated travel time from origin to destination.
   * @since 1.0.0
   */
  public void setDuration(int duration) {
    this.duration = duration;
  }

  /**
   * A short, human-readable summary of the route (e.g., major roads traversed).
   *
   * @return a string briefly describing route.
   * @since 1.0.0
   */
  public String getSummary() {
    return summary;
  }

  /**
   * @param summary A short, human-readable summary of the route (e.g., major roads traversed).
   * @since 1.0.0
   */
  public void setSummary(String summary) {
    this.summary = summary;
  }

  /**
   * Gives the geometry of the route. Commonly used to draw the route on the map view.
   *
   * @return encoded polyline with precision 6.
   * @since 1.0.0
   */
  public String getGeometry() {
    return geometry;
  }

  /**
   * @param geometry The geometry of the route.
   * @since 1.0.0
   */
  public void setGeometry(String geometry) {
    this.geometry = geometry;
  }

  /**
   * Steps gives you the route instructions.
   *
   * @return List of {@link RouteStep} objects.
   * @since 1.0.0
   */
  public List<RouteStep> getSteps() {
    return steps;
  }

  /**
   * @param steps The route instructions.
   * @since 1.0.0
   */
  public void setSteps(List<RouteStep> steps) {
    this.steps = steps;
  }

  /**
   * Gets a GeoJSON LineString which can be used to get route coordinates useful for
   * drawing on a map view.
   *
   * @param precision of encoded polyline.
   * @return GeoJSON LineString.
   * @since 1.0.0
   */
  public LineString asLineString(int precision) {
    return LineString.fromPolyline(getGeometry(), precision);
  }
}
