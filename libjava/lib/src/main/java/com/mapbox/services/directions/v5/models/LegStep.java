package com.mapbox.services.directions.v5.models;

/**
 * Created by antonio on 4/5/16.
 */
public class LegStep {

    private double distance;
    private double duration;
    private String geometry;
    private String name;
    private String mode;
    private StepManeuver maneuver;

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

    public String getName() {
        return name;
    }

    public String getMode() {
        return mode;
    }

    public StepManeuver getManeuver() {
        return maneuver;
    }
}
