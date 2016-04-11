package com.mapbox.services.directions.v5.models;

import java.util.List;

/**
 * The response to a directions request.
 */
public class DirectionsResponse {

    private String code;
    private List<DirectionsRoute> routes;
    private List<DirectionsWaypoint> waypoints;

    /**
     * String indicating the state of the response. This is a separate code than the HTTP status code.
     *
     * @return "Ok", "NoRoute", "ProfileNotFound", or "InvalidInput".
     */
    public String getCode() {
        return code;
    }

    /**
     * List with Waypoints of locations snapped to the road and path network and appear in the List
     * in the order of the input coordinates.
     *
     * @return List of {@link DirectionsWaypoint} objects.
     */
    public List<DirectionsWaypoint> getWaypoints() {
        return waypoints;
    }

    /**
     * List containing all the different route options. It's ordered by descending recommendation
     * rank. In other words, object 0 in the List is the highest recommended route. if you don't
     * setAlternatives to true (default is false) in your builder this should always be a List of
     * size 1.
     *
     * @return List of {@link DirectionsRoute} objects.
     */
    public List<DirectionsRoute> getRoutes() {
        return routes;
    }
}
