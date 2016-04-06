package com.mapbox.services.commons.models;

/**
 * Represents a position defined by a longitude, latitude, and optionally, an altitude.
 */
public class Position {

    private final double longitude;
    private final double latitude;
    private final double altitude;

    /*
     * Private constructor
     */

    private Position(double longitude, double latitude, double altitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    /*
     * Getters
     */

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public double[] getCoordinates() {
        if (hasAltitude()) {
            return new double[]{getLongitude(), getLatitude(), getAltitude()};
        } else {
            return new double[]{getLongitude(), getLatitude()};
        }
    }

    /*
     * Factories
     */

    public static Position fromCoordinates(double longitude, double latitude, double altitude) {
        return new Position(longitude, latitude, altitude);
    }

    public static Position fromCoordinates(double longitude, double latitude) {
        return new Position(longitude, latitude, Double.NaN);
    }

    /*
     * Altitude related
     */

    public boolean hasAltitude() {
        return !Double.isNaN(altitude);
    }

}
