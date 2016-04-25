package com.mapbox.services.directions.v4.models;

/**
 * A point on earth represented by a longitude/latitude pair.
 */
@Deprecated
public class Waypoint {

    private double latitude;
    private double longitude;

    /**
     * Construct a location with a longitude latitude pair.
     *
     * @param longitude double value ranging from -180.0 to 180.0.
     * @param latitude  double value ranging from -90.0 to 90.0.
     */
    public Waypoint(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * The latitude of the location.
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
     * The longitude of the location.
     *
     * @return double value ranging from -180.0 to 180.0.
     */
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
