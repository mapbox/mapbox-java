package com.mapbox.services.directions.v5.models;

import java.util.List;

/**
 * Created by antonio on 4/4/16.
 */
public class DirectionsRoute {

    private double distance;
    private double duration;
    private String geometry;
    private List<RouteLeg> legs;

    /*
     * Getters
     */

    public double getDistance() {
        return distance;
    }

    public double getDuration() {
        return duration;
    }

    public String getGeometry() {
        return geometry;
    }

    public List<RouteLeg> getLegs() {
        return legs;
    }
}
