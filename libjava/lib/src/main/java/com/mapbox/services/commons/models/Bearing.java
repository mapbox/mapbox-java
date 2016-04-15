package com.mapbox.services.commons.models;

/**
 * Bearings indicate the allowed direction of travel through a coordinate.
 */
public class Bearing {

    private int direction;
    private int range;

    /*
     * Constructor
     */

    public Bearing(int direction, int range) {
        this.direction = direction;
        this.range = range;
    }

    /*
     * Getters and setters
     */

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
