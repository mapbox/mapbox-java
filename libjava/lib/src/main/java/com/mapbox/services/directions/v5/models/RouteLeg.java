package com.mapbox.services.directions.v5.models;

import java.util.List;

/**
 * A route between only two {@link DirectionsWaypoint}.
 */
public class RouteLeg {

    private double distance;
    private double duration;
    private String summary;
    private List<LegStep> steps;

    /**
     * The distance traveled from one waypoint to another.
     *
     * @return a double number with unit meters.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * The estimated travel time from one waypoint to another.
     *
     * @return a double number with unit seconds.
     */
    public double getDuration() {
        return duration;
    }

    /**
     * A short human-readable summary of major roads traversed. Useful to distinguish alternatives.
     *
     * @return String with summary.
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Gives a List including all the steps to get from one waypoint to another.
     *
     * @return List of {@link LegStep}.
     */
    public List<LegStep> getSteps() {
        return steps;
    }
}
