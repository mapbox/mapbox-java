package com.mapbox.services.directions.v5.models;

import java.util.List;

/**
 * Created by antonio on 3/4/16.
 */
public class DirectionsResponse {

    private String code;
    private List<DirectionsRoute> routes;
    private List<DirectionsWaypoint> waypoints;

    /*
     * Getters
     */

    public String getCode() {
        return code;
    }

    public List<DirectionsWaypoint> getWaypoints() {
        return waypoints;
    }

    public List<DirectionsRoute> getRoutes() {
        return routes;
    }
}
