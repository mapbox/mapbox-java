package com.mapbox.services.commons.turf.models;

/**
 * Created by antonio on 5/13/16.
 */
public class LineIntersectsResult {

    private Double x;
    private Double y;
    private boolean onLine1 = false;
    private boolean onLine2 = false;

    public LineIntersectsResult() {
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public boolean isOnLine1() {
        return onLine1;
    }

    public void setOnLine1(boolean onLine1) {
        this.onLine1 = onLine1;
    }

    public boolean isOnLine2() {
        return onLine2;
    }

    public void setOnLine2(boolean onLine2) {
        this.onLine2 = onLine2;
    }
}
