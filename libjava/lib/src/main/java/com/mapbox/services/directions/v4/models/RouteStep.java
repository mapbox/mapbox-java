package com.mapbox.services.directions.v4.models;

import com.google.gson.annotations.SerializedName;

/**
 * The instructions for a route are broken down into steps with each step containing details like
 * heading, direction, way name, and duration.
 */
@Deprecated
public class RouteStep {

    private int distance;
    private int duration;
    @SerializedName("wayName") private String wayName;
    private String direction;
    private double heading;
    private StepManeuver maneuver;

    /**
     * The distance of travel from the maneuver to the subsequent step.
     *
     * @return integer distance given in meters.
     */
    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * The estimated travel time from the maneuver to the subsequent step.
     *
     * @return integer number given in seconds.
     */
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * The name of the way along which travel proceeds.
     *
     * @return string containing a name.
     */
    public String getWayName() {
        return wayName;
    }

    public void setWayName(String wayName) {
        this.wayName = wayName;
    }

    /**
     * The approximate cardinal direction of travel following the maneuver. Typically one of the
     * following: 'N', 'NE', 'E', 'SE', 'S', 'SW', 'W', or 'NW'.
     *
     * @return string containing abbreviated cardinal direction.
     */
    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * The clockwise angle from true north to the direction of travel immediately following the maneuver.
     *
     * @return double value ranging from 0 to 359.
     */
    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    /**
     * A {@link StepManeuver} object representing the step maneuver.
     *
     * @return a {@link StepManeuver} object.
     */
    public StepManeuver getManeuver() {
        return maneuver;
    }

    public void setManeuver(StepManeuver maneuver) {
        this.maneuver = maneuver;
    }
}
