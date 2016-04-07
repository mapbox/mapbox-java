package com.mapbox.services.directions.v5.models;

import com.google.gson.annotations.SerializedName;
import com.mapbox.services.commons.models.Position;

import java.util.Arrays;

/**
 * Created by antonio on 4/5/16.
 */
public class StepManeuver {

    private double[] location;
    @SerializedName("bearing_before") private double bearingBefore;
    @SerializedName("bearing_after") private double bearingAfter;
    private String type;
    private String modifier;
    private String instruction;
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

    public String getInstruction() {
        return instruction;
    }

    public int getExit() {
        return exit;
    }

    public Position asPosition() {
        return Position.fromCoordinates(location[0], location[1]);
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
