package com.mapbox.services.directions.v5;

/**
 * Created by antonio on 3/4/16.
 */
public class DirectionsCriteria {

    /**
     The profile parameter denotes the type of routing. Currently supported are:
     - mapbox/driving for car routing
     - mapbox/walking for pedestrian and hiking routing
     - mapbox/cycling for bicycle routing
     */
    public static final String PROFILE_DEFAULT_USER = "mapbox";
    public static final String PROFILE_DRIVING = "driving";
    public static final String PROFILE_WALKING = "walking";
    public static final String PROFILE_CYCLING = "cycling";

    /**
     * Format in which geometries are returned
     */
    public final static String GEOMETRY_POLYLINE = "polyline";
    private final static String GEOMETRY_GEOJSON = "geojson"; // Unsupported

    /**
     * Add overview geometry either full, simplified to the highest zoom level it could be
     * display on, or not at all
     */
    public final static String OVERVIEW_SIMPLIFIED = "simplified";
    public final static String OVERVIEW_FULL = "full";
    public final static String OVERVIEW_FALSE = "false";

    /**
     * On error, the server responds with an HTTP status code denoting the error, as explained in
     * the table below. The response body may include a JSON object with a message property,
     * explaining the error.
     */
    public final static String RESPONSE_OK = "Ok";
    public final static String RESPONSE_NO_ROUTE = "NoRoute";
    public final static String RESPONSE_PROFILE_NOT_FOUND = "ProfileNotFound";
    public final static String RESPONSE_INVALID_INPUT = "InvalidInput";

}
