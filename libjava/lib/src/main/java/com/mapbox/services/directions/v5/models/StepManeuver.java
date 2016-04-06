package com.mapbox.services.directions.v5.models;

import java.util.Arrays;

/**
 * Created by antonio on 4/5/16.
 */
public class StepManeuver {

    private double[] location;
    private double bearing_before;
    private double bearing_after;
    private String type;
    private String modifier;
    private int exit;

    public double[] getLocation() {
        return location;
    }

    public double getBearing_before() {
        return bearing_before;
    }

    public double getBearing_after() {
        return bearing_after;
    }

    public String getType() {
        return type;
    }

    public String getModifier() {
        return modifier;
    }

    public int getExit() {
        return exit;
    }

    @Override
    public String toString() {
        return "StepManeuver{" +
                "location=" + Arrays.toString(location) +
                ", bearing_before=" + bearing_before +
                ", bearing_after=" + bearing_after +
                ", type='" + type + '\'' +
                ", modifier='" + modifier + '\'' +
                ", exit=" + exit +
                '}';
    }
}
