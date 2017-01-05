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

  public void setRoutes(List<DirectionsRoute> routes) {
    this.routes = routes;
  }
}
