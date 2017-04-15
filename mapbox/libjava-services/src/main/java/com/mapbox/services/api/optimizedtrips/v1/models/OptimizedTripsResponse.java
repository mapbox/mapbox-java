package com.mapbox.services.api.optimizedtrips.v1.models;

import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.api.directions.v5.models.DirectionsWaypoint;

import java.util.List;

public class OptimizedTripsResponse {

  private String code;
  private List<DirectionsWaypoint> waypoints;
  private List<DirectionsRoute> trips;

  /**
   * Empty constructor
   *
   * @since 2.1.0
   */
  public OptimizedTripsResponse() {
  }

  /**
   * Constructor taking in a list of {@link DirectionsRoute}s along with a list of {@link DirectionsWaypoint}s.
   *
   * @param trips     a list of {@link DirectionsRoute}s used for building this object.
   * @param waypoints a list of {@link DirectionsWaypoint}s used for building this object. In typical cases, you'll
   *                  want the waypoints to match the ones making up the directions route.
   * @since 2.1.0
   */
  public OptimizedTripsResponse(List<DirectionsRoute> trips, List<DirectionsWaypoint> waypoints) {
    this.trips = trips;
    this.waypoints = waypoints;
  }

  /**
   * String indicating the state of the response. This is a separate code than the HTTP status code. On normal valid
   * responses, the value will be {@code Ok}.
   * <p>
   * On error, the server responds with different HTTP status codes. For responses with HTTP status codes lower than
   * 500, the JSON response body includes the code property, which may be used by client programs to manage control
   * flow. The response body may also include a message property, with a human-readable explanation of the error. If
   * a server error occurs, the HTTP status code will be 500 or higher and the response will not include a code
   * property. Possible errors include:
   * <ul>
   * <li><strong>Ok</strong>: {@code 200} Normal success case</li>
   * <li><strong>NoTrips</strong>: {@code 200} For one coordinate no route to other coordinates could be found. Check
   * for impossible routes (e.g. routes over oceans without ferry connections).</li>
   * <li><strong>NotImplemented</strong>: {@code 200} For the given combination of {@code source}, {@code destination}
   * and {@code roundtrip}, this request is not supported.</li>
   * <li><strong>ProfileNotFound</strong>: {@code 404} Use a valid profile</li>
   * <li><strong>InvalidInput</strong>: {@code 422} The given request was not valid. The message key of the response
   * will hold an explanation of the invalid input.</li>
   * </ul>
   *
   * @return String containing the response code. In normal conditions this will return {@code OK}.
   * @since 2.1.0
   */
  public String getCode() {
    return code;
  }

  /**
   * A {@code String} representing the response code the API typically returns. See
   * {@link OptimizedTripsResponse#getCode()} for a list of responses the API provides.
   *
   * @param code a {@code String} representing the response code the API typically returns.
   * @since 2.1.0
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * List of {@link DirectionsWaypoint} objects. Each waypoint is an input coordinate snapped to the road and path
   * network. The waypoints appear in the list in the order of the input coordinates.
   *
   * @return a list of {@link DirectionsWaypoint}s in the order of the input coordinates.
   * @since 2.1.0
   */
  public List<DirectionsWaypoint> getWaypoints() {
    return waypoints;
  }

  /**
   * List of {@link DirectionsWaypoint} objects. Each waypoint should be an input coordinate snapped to the road and
   * path network. The waypoints should appear in the list in the order of the input coordinates.
   *
   * @param waypoints a list of {@link DirectionsWaypoint}s in the order of the input coordinates.
   * @since 2.1.0
   */
  public void setWaypoints(List<DirectionsWaypoint> waypoints) {
    this.waypoints = waypoints;
  }

  /**
   * List of trip {@link DirectionsRoute} objects. Will have zero or one trip.
   *
   * @return list of {@link DirectionsRoute} either having a size zero or one.
   * @since 2.1.0
   */
  public List<DirectionsRoute> getTrips() {
    return trips;
  }

  /**
   * List of trip {@link DirectionsRoute} objects. Should have either zero or one trip.
   *
   * @param trips list of {@link DirectionsRoute} either having a size zero or one.
   * @since 2.1.0
   */
  public void setTrips(List<DirectionsRoute> trips) {
    this.trips = trips;
  }
}
