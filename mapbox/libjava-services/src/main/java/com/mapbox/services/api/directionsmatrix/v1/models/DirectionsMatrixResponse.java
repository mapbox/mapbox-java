package com.mapbox.services.api.directionsmatrix.v1.models;

import com.mapbox.services.api.directions.v5.models.DirectionsWaypoint;

import java.util.List;

/**
 * The Directions Matrix API response providing getters and setters for all response parameters.
 *
 * @since 2.1.0
 */
public class DirectionsMatrixResponse {

  private String code;
  private double[][] durations;
  private List<DirectionsWaypoint> destinations;
  private List<DirectionsWaypoint> sources;

  public DirectionsMatrixResponse() {
  }

  /**
   * String indicating the state of the response. This is a separate code than the HTTP status code. On normal valid
   * responses, the value will be Ok. See the errors section below for more information.
   *
   * @return "Ok", "NoRoute", "ProfileNotFound", or "InvalidInput".
   * @since 2.1.0
   */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Durations as array of arrays representing the matrix in row-major order. durations[i][j] gives the travel time
   * from the i-th source to the j-th destination. All values are in seconds. The duration between the same coordinate
   * is always 0. If a duration can not be found, the result is null.
   *
   * @return an array of array with each entry being a duration value given in seconds.
   * @since 2.1.0
   */
  public double[][] getDurations() {
    return durations;
  }

  public void setDurations(double[][] durations) {
    this.durations = durations;
  }

  /**
   * List of {@link DirectionsWaypoint} objects. Each waypoints is an input coordinate snapped to the road and path
   * network. The waypoints appear in the list in the order of the input coordinates, or in the order as specified in
   * the destinations query parameter.
   *
   * @return List object with each item being a {@link DirectionsWaypoint}.
   * @since 2.1.0
   */
  public List<DirectionsWaypoint> getDestinations() {
    return destinations;
  }

  public void setDestinations(List<DirectionsWaypoint> destinations) {
    this.destinations = destinations;
  }

  /**
   * List of {@link DirectionsWaypoint} objects. Each waypoints is an input coordinate snapped to the road and path
   * network. The waypoints appear in the list in the order of the input coordinates, or in the order as specified in
   * the sources query parameter.
   *
   * @return List object with each item being a {@link DirectionsWaypoint}.
   * @since 2.1.0
   */
  public List<DirectionsWaypoint> getSources() {
    return sources;
  }

  public void setSources(List<DirectionsWaypoint> sources) {
    this.sources = sources;
  }
}
