package com.mapbox.services.directions.v5;

/**
 * Constants used to customize the directions requested
 */
public class DirectionsCriteria {

    public static final String PROFILE_DEFAULT_USER = "mapbox";

    /**
     * For car and motorcycle routing. This profile shows the fastest routes by preferring
     * high-speed roads like highways.
     */
    public static final String PROFILE_DRIVING = "driving";

    /**
     * For pedestrian and hiking routing. This profile shows the shortest path by using sidewalks
     * and trails.
     */
    public static final String PROFILE_WALKING = "walking";

    /**
     * For bicycle routing. This profile shows routes that are short and safe for cyclist, avoiding
     * highways and preferring streets with bike lanes.
     */
    public static final String PROFILE_CYCLING = "cycling";

    /**
     * Format to return route geometry will be encoded polyline.
     */
    public final static String GEOMETRY_POLYLINE = "polyline";

    /**
     * Format to return route geometry will be geojson. Note that this isn't supported by the SDK.
     */
    private final static String GEOMETRY_GEOJSON = "geojson";

    /**
     * A simplified version of the {@link #OVERVIEW_FULL} geometry. If not specified simplified is the default.
     */
    public final static String OVERVIEW_SIMPLIFIED = "simplified";

    /**
     * The most detailed geometry available.
     */
    public final static String OVERVIEW_FULL = "full";

    /**
     * No overview geometry.
     */
    public final static String OVERVIEW_FALSE = "false";

    /**
     * Server responds with no errors.
     */
    public final static String RESPONSE_OK = "Ok";

    /**
     * There was no route found for the given query.
     */
    public final static String RESPONSE_NO_ROUTE = "NoRoute";

    /**
     * Use a valid profile as described above.
     */
    public final static String RESPONSE_PROFILE_NOT_FOUND = "ProfileNotFound";

    /**
     * The given request was not valid.
     */
    public final static String RESPONSE_INVALID_INPUT = "InvalidInput";

}
