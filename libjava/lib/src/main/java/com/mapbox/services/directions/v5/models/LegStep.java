package com.mapbox.services.directions.v5.models;

/**
 * Includes one {@link StepManeuver} object and travel to the following {@link LegStep}.
 */
public class LegStep {

    private double distance;
    private double duration;
    private String geometry;
    private String name;
    private String mode;
    private StepManeuver maneuver;

    /**
     * The distance traveled from the maneuver to the next {@link LegStep}.
     *
     * @return a double number with unit meters.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * The estimated travel time from the maneuver to the next {@link LegStep}.
     *
     * @return a double number with unit seconds.
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Gives the geometry of the leg step.
     *
     * @return either GeoJSON LineString or an encoded polyline string.
     */
    public String getGeometry() {
        return geometry;
    }

    /**
     * @return String with the name of the way along which the travel proceeds.
     */
    public String getName() {
        return name;
    }

    /**
     * @return String indicating the mode of transportation.
     */
    public String getMode() {
        return mode;
    }

    /**
     * @return One {@link StepManeuver} object.
     */
    public StepManeuver getManeuver() {
        return maneuver;
    }
}
