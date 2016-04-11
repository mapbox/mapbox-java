package com.mapbox.services.directions.v5.models;

import java.util.List;

/**
 * Gives detailed information about an individual route such as the duration, distance and geometry.
 */
public class DirectionsRoute {

    private double distance;
    private double duration;
    private String geometry;
    private List<RouteLeg> legs;

    /**
     * The distance traveled from origin to destination.
     *
     * @return a double number with unit meters.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * The estimated travel time from origin to destination.
     *
     * @return a double number with unit seconds.
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Gives the geometry of the route. Commonly used to draw the route on the map view.
     *
     * @return either GeoJSON LineString or an encoded polyline string.
     */
    public String getGeometry() {
        return geometry;
    }

    /**
     * A Leg is a route between only two waypoints
     *
     * @return List of {@link RouteLeg} objects.
     */
    public List<RouteLeg> getLegs() {
        return legs;
    }
}
