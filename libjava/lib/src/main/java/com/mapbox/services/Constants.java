package com.mapbox.services;

public class Constants {

    /**
     * Package version
     */
    public final static String MAPBOX_JAVA_VERSION = "1.2.0-SNAPSHOT";

    /**
     * User agent for HTTP requests
     */
    public final static String HEADER_USER_AGENT = "User-Agent: MapboxAndroidServices/" + MAPBOX_JAVA_VERSION;

    public final static String BASE_API_URL = "https://api.mapbox.com";

    /*
     * For polyline encoding/decoding
     */

    public final static int GOOGLE_PRECISION = 5;

    public final static int OSRM_PRECISION_V4 = 6;
    public final static int OSRM_PRECISION_V5 = 5;

    /*
     * Mapbox default styles
     * https://www.mapbox.com/developers/api/styles/
     */

    public final static String MAPBOX_USER = "mapbox";

    public final static String MAPBOX_STYLE_STREETS = "streets-v9";
    public final static String MAPBOX_STYLE_LIGHT = "light-v9";
    public final static String MAPBOX_STYLE_DARK = "dark-v9";
    @Deprecated
    public final static String MAPBOX_STYLE_EMERALD = "emerald-v8";
    public final static String MAPBOX_OUTDOORS = "outdoors-v9";
    public final static String MAPBOX_STYLE_SATELLITE = "satellite-v9";
    public final static String MAPBOX_STYLE_SATELLITE_HYBRID = "satellite-streets-v9";

}
