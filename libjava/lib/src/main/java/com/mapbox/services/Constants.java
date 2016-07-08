package com.mapbox.services;

import java.util.Locale;

public class Constants {

    /**
     * Package version
     */
    public final static String MAPBOX_JAVA_VERSION = "2.0.0-SNAPSHOT";

    /**
     * User agent for HTTP requests
     */
    public final static String HEADER_USER_AGENT =
            String.format(Locale.US, "MapboxJava/%s", MAPBOX_JAVA_VERSION);

    /**
     * Base URL for all API calls, not hardcoded to enable testing
     */
    public final static String BASE_API_URL = "https://api.mapbox.com";

    /**
     * Constants for polyline encoding/decoding
     */
    public final static int GOOGLE_PRECISION = 5;
    public final static int OSRM_PRECISION_V4 = 6;
    public final static int OSRM_PRECISION_V5 = 5;

    /**
     * Default user for services
     */
    public final static String MAPBOX_USER = "mapbox";

    /**
     * Mapbox default styles
     * https://www.mapbox.com/developers/api/styles/
     */
    public final static String MAPBOX_STYLE_STREETS = "streets-v9";
    public final static String MAPBOX_STYLE_LIGHT = "light-v9";
    public final static String MAPBOX_STYLE_DARK = "dark-v9";
    public final static String MAPBOX_STYLE_OUTDOORS = "outdoors-v9";
    public final static String MAPBOX_STYLE_SATELLITE = "satellite-v9";
    public final static String MAPBOX_STYLE_SATELLITE_HYBRID = "satellite-streets-v9";

}
