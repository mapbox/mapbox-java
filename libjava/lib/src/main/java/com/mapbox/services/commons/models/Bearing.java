package com.mapbox.services.commons.models;

/**
 * Bearings indicate the allowed direction of travel through a coordinate.
 */
public class Bearing {

    private int direction;
    private int range;


    /**
     * Constructor
     *
     * @param direction integer value between 0 and 360 indicating the clockwise angle from true north
     * @param range     TODO
     */
    public Bearing(int direction, int range) {
        this.direction = direction;
        this.range = range;
    }

    // ************************* Getters and Setters *************************

    /**
     * get the bearing direction integer value between 0 and 360 indicating the clockwise angle from
     * true north
     *
     * @return integer value between 0 and 360.
     */
    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
