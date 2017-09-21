package com.mapbox.services.api.directions.v5.models;

import java.util.List;

/**
 * The response to a directions request.
 *
 * @since 1.0.0
 */
public class DirectionsResponse {

  private String code;
  private List<DirectionsRoute> routes;
  private List<DirectionsWaypoint> waypoints;

  /**
   * Empty constructor
   *
   * @since 2.1.0
   */
  public DirectionsResponse() {
  }

  /**
   * Constructor taking in both a list of {@link DirectionsRoute} and a list of {@link DirectionsWaypoint}s.
   *
   * @param routes    list of routes you can pass in while building this object.
   * @param waypoints list of waypoints you can pass in while building this object. Ideally these should match what was
   *                  used to crate the route.
   * @since 2.0.0
   */
  public DirectionsResponse(List<DirectionsRoute> routes, List<DirectionsWaypoint> waypoints) {
    this.routes = routes;
    this.waypoints = waypoints;
  }

  /**
   * String indicating the state of the response. This is a separate code than the HTTP
   * status code.
   *
   * @return "Ok", "NoRoute", "ProfileNotFound", or "InvalidInput".
   * @since 1.0.0
   */
  public String getCode() {
    return code;
  }

  /**
   * String indicating the state of the response. This is a separate code than the HTTP
   * status code.
   *
   * @param code "Ok", "NoRoute", "ProfileNotFound", or "InvalidInput".
   * @since 2.1.0
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * List with Waypoints of locations snapped to the road and path network and appear in the List
   * in the order of the input coordinates.
   *
   * @return List of {@link DirectionsWaypoint} objects.
   * @since 1.0.0
   */
  public List<DirectionsWaypoint> getWaypoints() {
    return waypoints;
  }

  /**
   * List with Waypoints of locations snapped to the road and path network and should appear in the List
   * in the order of the input coordinates.
   *
   * @param waypoints List of {@link DirectionsWaypoint} objects.
   * @since 2.1.0
   */
  public void setWaypoints(List<DirectionsWaypoint> waypoints) {
    this.waypoints = waypoints;
  }

  /**
   * List containing all the different route options. It's ordered by descending recommendation
   * rank. In other words, object 0 in the List is the highest recommended route. if you don't
   * setAlternatives to true (default is false) in your builder this should always be a List of
   * size 1.
   *
   * @return List of {@link DirectionsRoute} objects.
   * @since 1.0.0
   */
  public List<DirectionsRoute> getRoutes() {
    return routes;
  }

  /**
   * List containing all the different route options. It should be ordered by descending recommendation
   * rank. In other words, object 0 in the List is the highest recommended route. if you don't
   * setAlternatives to true (default is false) in your builder this should always be a List of
   * size 1.
   *
   * @param routes List of {@link DirectionsRoute} objects.
   * @since 2.1.0
   */
  public void setRoutes(List<DirectionsRoute> routes) {
    this.routes = routes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DirectionsResponse response = (DirectionsResponse) o;

    if (getCode() != null ? !getCode().equals(response.getCode()) : response.getCode() != null) {
      return false;
    }
    if (getRoutes() != null ? !getRoutes().equals(response.getRoutes()) : response.getRoutes() != null) {
      return false;
    }
    return getWaypoints() != null ? getWaypoints().equals(response.getWaypoints()) : response.getWaypoints() == null;

  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
    result = 31 * result + (getRoutes() != null ? getRoutes().hashCode() : 0);
    result = 31 * result + (getWaypoints() != null ? getWaypoints().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DirectionsResponse{"
      + "code='" + code + '\''
      + ", routes=" + routes
      + ", waypoints=" + waypoints
      + '}';
  }
}
