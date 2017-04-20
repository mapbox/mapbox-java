package com.mapbox.services.api.directionsmatrix.v1.models;

import com.mapbox.services.api.directions.v5.models.DirectionsWaypoint;

import java.util.Arrays;
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

  /**
   * Empty constructor
   *
   * @since 2.1.0
   */
  public DirectionsMatrixResponse() {
  }

  /**
   * String indicating the state of the response. This is a separate code than the HTTP status code. On normal valid
   * responses, the value will be {@code Ok}.
   *
   * @return "Ok", "NoRoute", "ProfileNotFound", or "InvalidInput".
   * @since 2.1.0
   */
  public String getCode() {
    return code;
  }

  /**
   * String indicating the state of the response. This is a separate code than the HTTP status code. On normal valid
   * responses, the value will be {@code Ok}.
   *
   * @param code "Ok", "NoRoute", "ProfileNotFound", or "InvalidInput".
   * @since 2.1.0
   */
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

  /**
   * Durations as array of arrays representing the matrix in row-major order. durations[i][j] gives the travel time
   * from the i-th source to the j-th destination. All values are in seconds. The duration between the same coordinate
   * should always 0.
   *
   * @param durations an array of array with each entry being a duration value given in seconds.
   * @since 2.1.0
   */
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

  /**
   * List of {@link DirectionsWaypoint} objects. Each waypoints should be an input coordinate snapped to the road and
   * path network. The waypoints appear in the list in the order of the input coordinates, or in the order as specified
   * in the destinations query parameter.
   *
   * @param destinations List object with each item being a {@link DirectionsWaypoint}.
   * @since 2.1.0
   */
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

  /**
   * List of {@link DirectionsWaypoint} objects. Each waypoints should be an input coordinate snapped to the road and
   * path network. The waypoints appear in the list in the order of the input coordinates, or in the order as specified
   * in the sources query parameter.
   *
   * @param sources List object with each item being a {@link DirectionsWaypoint}.
   * @since 2.1.0
   */
  public void setSources(List<DirectionsWaypoint> sources) {
    this.sources = sources;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DirectionsMatrixResponse that = (DirectionsMatrixResponse) o;

    if (getCode() != null ? !getCode().equals(that.getCode()) : that.getCode() != null) {
      return false;
    }
    if (!Arrays.deepEquals(getDurations(), that.getDurations())) {
      return false;
    }
    if (getDestinations() != null
      ? !getDestinations().equals(that.getDestinations()) : that.getDestinations() != null) {
      return false;
    }
    return getSources() != null ? getSources().equals(that.getSources()) : that.getSources() == null;

  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
    result = 31 * result + Arrays.deepHashCode(getDurations());
    result = 31 * result + (getDestinations() != null ? getDestinations().hashCode() : 0);
    result = 31 * result + (getSources() != null ? getSources().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DirectionsMatrixResponse{"
      + "code='" + code + '\''
      + ", durations=" + Arrays.toString(durations)
      + ", destinations=" + destinations
      + ", sources=" + sources
      + '}';
  }
}
