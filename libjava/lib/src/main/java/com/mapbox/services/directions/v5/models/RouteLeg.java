package com.mapbox.services.directions.v5.models;

import java.util.List;

/**
 * Created by antonio on 4/5/16.
 */
public class RouteLeg {

    private double distance;
    private double duration;
    private String summary;
    private List<LegStep> steps;

    /*
     * Getters
     */

    public double getDistance() {
        return distance;
    }

    public double getDuration() {
        return duration;
    }

    public String getSummary() {
        return summary;
    }

    public List<LegStep> getSteps() {
        return steps;
    }
}
