package com.mapbox.services.directions.v4;

/**
 * Constants used to customize the directions requested
 */
@Deprecated
public final class DirectionsCriteria {

    /**
     * For car and motorcycle routing. This profile shows the fastest routes by preferring
     * high-speed roads like highways.
     */
    public static final String PROFILE_DRIVING = "mapbox.driving";

    /**
     * For pedestrian and hiking routing. This profile shows the shortest path by using sidewalks
     * and trails.
     */
    public static final String PROFILE_WALKING = "mapbox.walking";

    /**
     * For bicycle routing. This profile shows routes that are short and safe for cyclist, avoiding
     * highways and preferring streets with bike lanes.
     */
    public static final String PROFILE_CYCLING = "mapbox.cycling";

    /**
     * Format to return route instructions will be text.
     */
    public static final String INSTRUCTIONS_TEXT = "text";

    /**
     * Format to return route instructions will be html.
     */
    public static final String INSTRUCTIONS_HTML = "html";

    /**
     * Format to return route geometry will be geojson.
     */
    public static final String GEOMETRY_GEOJSON = "geojson";

    /**
     * Format to return route geometry will be encoded polyline.
     */
    public static final String GEOMETRY_POLYLINE = "polyline";

    /**
     * Use false to omit geometry from response.
     */
    public static final String GEOMETRY_FALSE = "false";

}
