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
     * @param longitude double value with position's longitude.
     * @param latitude  double value with position's latitude.
     * @param altitude  double value with position's altitude.
     */
    private Position(double longitude, double latitude, double altitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    /**
     * Gets the position's longitude.
     *
     * @return double value with positions longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Gets the position's latitude.
     *
     * @return double value with positions latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the position's altitude.
     *
     * @return double value with positions altitude.
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * Gets the position coordinates in a double array. Altitude will be included in the array
     * if a value is present.
     *
     * @return double[] array with longitude, latitude, and altitude (if present).
     */
    public double[] getCoordinates() {
        if (hasAltitude()) {
            return new double[]{getLongitude(), getLatitude(), getAltitude()};
        } else {
            return new double[]{getLongitude(), getLatitude()};
        }
    }

    /**
     * Builds a {@link Position} from a double longitude, latitude and an altitude.
     *
     * @param longitude double longitude value.
     * @param latitude  double latitude value.
     * @param altitude  double altitude value.
     */
    public static Position fromCoordinates(double longitude, double latitude, double altitude) {
        return new Position(longitude, latitude, altitude);
    }

    /**
     * Builds a {@link Position} from a double longitude and latitude.
     *
     * @param longitude double longitude value.
     * @param latitude  double latitude value.
     */
    public static Position fromCoordinates(double longitude, double latitude) {
        return new Position(longitude, latitude, Double.NaN);
    }

    /**
     * Checks if a position has an altitude value.
     *
     * @return true if position contains altitude data.
     */
    public boolean hasAltitude() {
        return !Double.isNaN(altitude);
    }

}
