package com.mapbox.services;

import java.util.Locale;

public class Constants {

  public static final double MIN_LONGITUDE = -180;

  public static final double MAX_LONGITUDE = 180;

  public static final double MIN_LATITUDE = -90;

  public static final double MAX_LATITUDE = 90;

  /**
   * Default locale
   */
  public static final Locale DEFAULT_LOCALE = Locale.US;

  /**
   * User agent for HTTP requests
   */
  public static final String HEADER_USER_AGENT =
    String.format(Locale.US, "MapboxJava/%s (%s)", BuildConfig.VERSION, BuildConfig.GIT_REVISION);

  /**
   * Base URL for all API calls, not hardcoded to enable testing
   */
  public static final String BASE_API_URL = "https://api.mapbox.com";

  /**
   * Constants for polyline encoding/decoding
   */

  // Use these if you want to be explicit about the vendor
  public static final int GOOGLE_PRECISION = 5;
  public static final int OSRM_PRECISION_V4 = 6;
  public static final int OSRM_PRECISION_V5 = 5;

  // Use these if you want to be explicit about the precision
  public static final int PRECISION_5 = 5;
  public static final int PRECISION_6 = 6;

  /**
   * Default user for services
   */
  public static final String MAPBOX_USER = "mapbox";

  /**
   * Mapbox default styles
   * https://www.mapbox.com/developers/api/styles/
   */
  public static final String MAPBOX_STYLE_STREETS = "streets-v10";
  public static final String MAPBOX_STYLE_LIGHT = "light-v9";
  public static final String MAPBOX_STYLE_DARK = "dark-v9";
  public static final String MAPBOX_STYLE_OUTDOORS = "outdoors-v10";
  public static final String MAPBOX_STYLE_SATELLITE = "satellite-v9";
  public static final String MAPBOX_STYLE_SATELLITE_HYBRID = "satellite-streets-v10";
  public static final String MAPBOX_STYLE_TRAFFIC_DAY = "traffic-day-v2";
  public static final String MAPBOX_STYLE_TRAFFIC_NIGHT = "traffic-night-v2";

  public static final String PIN_SMALL = "pin-s";
  public static final String PIN_MEDIUM = "pin-m";
  public static final String PIN_LARGE = "pin-l";

}
