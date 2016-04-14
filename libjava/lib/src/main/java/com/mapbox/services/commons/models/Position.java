package com.mapbox.services.commons.models;

/**
 * Represents a position defined by a longitude, latitude, and optionally, an altitude.
 */
public class Position {

    private final double longitude;
    private final double latitude;
    private final double altitude;


    /**
     * Private Constructor
     *
     * @param longitude double value with positions longitude.
     * @param latitude  double value with positions latitude.
     * @param altitude  double value with positions altitude.
     */
    private Position(double longitude, double latitude, double altitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    /*
     * Getters
     */

    /**
     * get the positions longitude.
     *
     * @return double value with positions longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * get the positions latitude.
     *
     * @return double value with positions latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * get the positions altitude.
     *
     * @return double value with positions altitude.
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * get the position coordinates in a double array. Altitude will be included within array if it
     * isn't null.
     *
     * @return double array with longitude, latitude, and altitude (if it isn't null).
     */
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

    /**
     * Build a {@link Position} from a double longitude, latitude and a altitude.
     *
     * @param longitude double longitude value.
     * @param latitude  double latitude value.
     * @param altitude  double altitude value.
     */
    public static Position fromCoordinates(double longitude, double latitude, double altitude) {
        return new Position(longitude, latitude, altitude);
    }

    /**
     * Build a {@link Position} from a double longitude and latitude.
     *
     * @param longitude double longitude value.
     * @param latitude  double latitude value.
     */
    public static Position fromCoordinates(double longitude, double latitude) {
        return new Position(longitude, latitude, Double.NaN);
    }

    /**
     * Check if a position has altitude value.
     *
     * @return true if position contains altitude data.
     */
    public boolean hasAltitude() {
        return !Double.isNaN(altitude);
    }

}
