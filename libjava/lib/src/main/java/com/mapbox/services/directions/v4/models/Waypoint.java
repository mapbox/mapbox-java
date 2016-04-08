package com.mapbox.services.directions.v4.models;

/**
 * A point on earth represented by a Longitude Latitude pair.
 */
@Deprecated
public class Waypoint {

    private double latitude;
    private double longitude;

    /**
     * Construct a location with a longitude latitude pair.
     */
    public Waypoint(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * The Latitude of the location.
     *
     * @return double value ranging from -90.0 to 90.0.
     */
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * The Longitude of the location.
     *
     * @return double value ranging from -180.0 to 180.0
     */
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
