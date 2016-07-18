package com.mapbox.services.directions.v5.models;

import java.util.List;

/**
 * Gives detailed information about an individual route such as the duration, distance and geometry.
 *
 * @since 1.0.0
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
     * @since 1.0.0
     */
    public double getDistance() {
        return distance;
    }

    /**
     * The estimated travel time from origin to destination.
     *
     * @return a double number with unit seconds.
     * @since 1.0.0
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Gives the geometry of the route. Commonly used to draw the route on the map view.
     *
     * @return An encoded polyline string.
     * @since 1.0.0
     */
    public String getGeometry() {
        return geometry;
    }

    /**
     * A Leg is a route between only two waypoints
     *
     * @return List of {@link RouteLeg} objects.
     * @since 1.0.0
     */
    public List<RouteLeg> getLegs() {
        return legs;
    }
}
