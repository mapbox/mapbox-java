package com.mapbox.services.directions.v5.models;

import com.google.gson.annotations.SerializedName;
import com.mapbox.services.commons.models.Position;

import java.util.Arrays;

/**
 * Gives maneuver information about one {@link LegStep}.
 */
public class StepManeuver {

    private double[] location;
    @SerializedName("bearing_before") private double bearingBefore;
    @SerializedName("bearing_after") private double bearingAfter;
    private String type;
    private String modifier;
    private String instruction;
    private int exit;

    /**
     * @return double array of [longitude, latitude] for the snapped coordinate.
     */
    public double[] getLocation() {
        return location;
    }

    /**
     * Number between 0 and 360 indicating the clockwise angle from true north to the direction of
     * travel right before the maneuver.
     *
     * @return double with value from 0 to 360.
     */
    public double getBearingBefore() {
        return bearingBefore;
    }

    /**
     * Number between 0 and 360 indicating the clockwise angle from true north to the direction of
     * travel right after the maneuver.
     *
     * @return double with value from 0 to 360.
     */
    public double getBearingAfter() {
        return bearingAfter;
    }

    /**
     * This indicating the type of maneuver. It can be any of these listed:
     * <p/>
     * <ul>
     * <li>turn - a basic turn into direction of the modifier</li>
     * <li>new name - the road name changes (after a mandatory turn)</li>
     * <li>depart - indicates departure from a leg</li>
     * <li>arrive - indicates arrival to a destination of a leg</li>
     * <li>merge - merge onto a street</li>
     * <li>ramp - take a ramp</li>
     * <li>fork</li>
     * <li>end of road - road ends in a T intersection</li>
     * <li>continue - continue on a street after a turn</li>
     * <li>roundabout - traverse roundabout, has additional field {@link #getExit()}</li>
     * </ul>
     *
     * @return String with type of maneuver.
     */
    public String getType() {
        return type;
    }

    /**
     * This indicating the mode of the maneuver.  If type is of turn, the modifier indicates the
     * change in direction accomplished through the turn. If the type is of depart/arrive, the
     * modifier indicates the position of waypoint from the current direction of travel.
     *
     * @return String with modifier.
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * A human-readable instruction of how to execute the returned maneuver.
     *
     * @return String with instruction.
     */
    public String getInstruction() {
        return instruction;
    }

    /**
     * Roundabout's will have an additional parameter.
     *
     * @return int value
     */
    public int getExit() {
        return exit;
    }

    /**
     * converts double array {@link #getLocation()} to a {@link Position}. You'll typically want to
     * use this format instead of {@link #getLocation()} as it's easier to work with.
     *
     * @return {@link Position}.
     */
    public Position asPosition() {
        return Position.fromCoordinates(location[0], location[1]);
    }

    /**
     * @return String with all {@link StepManeuver} information within.
     */
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
