package com.mapbox.services.api.directions.v5.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

/**
 * The response to a directions request.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class DirectionsResponse {

  /**
   * Constructor taking in both a list of {@link DirectionsRoute} and a list of {@link DirectionsWaypoint}s.
   *
   * @param code      {@code String} that indicates the status of the response
   * @param routes    list of routes you can pass in while building this object
   * @param waypoints list of waypoints you can pass in while building this object. Ideally these should match what was
   *                  used to crate the route
   * @since 2.0.0
   */
  public static DirectionsResponse create(String code, List<DirectionsRoute> routes,
                                          List<DirectionsWaypoint> waypoints) {
    return new AutoValue_DirectionsResponse(code, routes, waypoints);
  }

  public static TypeAdapter<DirectionsResponse> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsResponse.GsonTypeAdapter(gson);
  }

  /**
   * String indicating the state of the response. This is a separate code than the HTTP
   * status code.
   *
   * @return "Ok", "NoRoute", "ProfileNotFound", or "InvalidInput".
   * @since 1.0.0
   */
  public abstract String getCode();

  /**
   * List containing all the different route options. It's ordered by descending recommendation
   * rank. In other words, object 0 in the List is the highest recommended route. if you don't
   * setAlternatives to true (default is false) in your builder this should always be a List of
   * size 1.
   *
   * @return List of {@link DirectionsRoute} objects.
   * @since 1.0.0
   */
  public abstract List<DirectionsRoute> getRoutes();

  /**
   * List with waypoints of locations snapped to the road and path network and appear in the List
   * in the order of the input coordinates.
   *
   * @return List of {@link DirectionsWaypoint} objects.
   * @since 1.0.0
   */
  public abstract List<DirectionsWaypoint> getWaypoints();
}
