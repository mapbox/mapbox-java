package com.mapbox.services.mapmatching.v4.models;

/**
 * Constants used to customize the MapMatching request and interpret the response
 * <p/>
 * Created by ivo on 18/05/16.
 */
public final class MapMatchingCriteria {

    /**
     * For car and motorcycle routing.
     */
    public static final String PROFILE_DRIVING = "driving";

    /**
     * For pedestrian and hiking routing.
     */
    public static final String PROFILE_WALKING = "walking";

    /**
     * For bicycle routing.
     */
    public static final String PROFILE_CYCLING = "cycling";

    /**
     * Format to return geometry will be encoded polyline.
     */
    public final static String GEOMETRY_POLYLINE = "polyline";


    /**
     * Format to return no geometry
     */
    public final static String GEOMETRY_NONE = "false";

    /**
     * Server responds with no errors.
     */
    public final static String RESPONSE_OK = "Ok";

}
