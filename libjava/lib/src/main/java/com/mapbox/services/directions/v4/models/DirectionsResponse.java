package com.mapbox.services.directions.v4.models;

import java.util.ArrayList;
import java.util.List;

/**
 * The response to a directions request.
 */
public class DirectionsResponse {

    private DirectionsFeature origin;
    private DirectionsFeature destination;
    private List<DirectionsFeature> waypoints;
    private List<DirectionsRoute> routes;

    public DirectionsResponse() {
        waypoints = new ArrayList<>();
        routes = new ArrayList<>();
    }

    /**
     * Gives details about the origin of the route.
     *
     * @return {@link DirectionsFeature} object.
     */
    public DirectionsFeature getOrigin() {
        return origin;
    }

    public void setOrigin(DirectionsFeature origin) {
        this.origin = origin;
    }

    /**
     * Gives details about the destination of the route.
     *
     * @return {@link DirectionsFeature} object.
     */
    public DirectionsFeature getDestination() {
        return destination;
    }

    public void setDestination(DirectionsFeature destination) {
        this.destination = destination;
    }

    /**
     * A List of {@link DirectionsFeature} objects representing intermediate waypoints along route,
     * if any exist. If you request directions with more then two waypoints (origin and destination)
     * addition information about those intermediate points will be given here in a List object.
     *
     * @return List of {@link DirectionsFeature} objects.
     */
    public List<DirectionsFeature> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<DirectionsFeature> waypoints) {
        this.waypoints = waypoints;
    }

    /**
     * List containing all the different route options. It's ordered by descending recommendation
     * rank. In other words, object 0 in the List is the highest recommended route. if you
     * setAlternatives to false (default is true) in your builder this should always be a List of
     * size 1.
     *
     * @return List of {@link DirectionsRoute} objects.
     */
    public List<DirectionsRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<DirectionsRoute> routes) {
        this.routes = routes;
    }
}
