package com.mapbox.services.directions.v5.models;

/**
 * Includes one {@link StepManeuver} object and travel to the following {@link LegStep}.
 *
 * @since 1.0.0
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
     * @since 1.0.0
     */
    public double getDistance() {
        return distance;
    }

    /**
     * The estimated travel time from the maneuver to the next {@link LegStep}.
     *
     * @return a double number with unit seconds.
     * @since 1.0.0
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Gives the geometry of the leg step.
     *
     * @return An encoded polyline string.
     * @since 1.0.0
     */
    public String getGeometry() {
        return geometry;
    }

    /**
     * @return String with the name of the way along which the travel proceeds.
     * @since 1.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * @return String indicating the mode of transportation.
     * @since 1.0.0
     */
    public String getMode() {
        return mode;
    }

    /**
     * @return One {@link StepManeuver} object.
     * @since 1.0.0
     */
    public StepManeuver getManeuver() {
        return maneuver;
    }
}
