package com.mapbox.services.directions.v4.models;

/**
 * Created by antonio on 11/6/15.
 */
@Deprecated
public class Waypoint {

    private double latitude;
    private double longitude;

    public Waypoint(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
