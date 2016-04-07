package com.mapbox.services.directions.v5.models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by antonio on 4/5/16.
 */
public class StepManeuver {

    private double[] location;
    @SerializedName("bearingBefore") private double bearingBefore;
    @SerializedName("bearingAfter") private double bearingAfter;
    private String type;
    private String modifier;
    private int exit;

    public double[] getLocation() {
        return location;
    }

    public double getBearingBefore() {
        return bearingBefore;
    }

    public double getBearingAfter() {
        return bearingAfter;
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
                ", bearingBefore=" + bearingBefore +
                ", bearingAfter=" + bearingAfter +
                ", type='" + type + '\'' +
                ", modifier='" + modifier + '\'' +
                ", exit=" + exit +
                '}';
    }
}
