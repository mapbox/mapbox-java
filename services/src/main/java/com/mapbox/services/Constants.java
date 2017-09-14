package com.mapbox.services;

import java.util.Locale;

/**
 * Includes common variables used throughout the Mapbox Service modules.
 *
 * @since 3.0.0
 */
public class Constants {

  public static final double MIN_LONGITUDE = -180;

  public static final double MAX_LONGITUDE = 180;

  public static final double MIN_LATITUDE = -90;

  public static final double MAX_LATITUDE = 90;

  /**
   * User agent for HTTP requests.
   *
   * @since 1.0.0
   */
  public static final String HEADER_USER_AGENT = "TODO"; // TODO
//    String.format(Locale.US, "MapboxJava/%s (%s)", BuildConfig.VERSION, BuildConfig.GIT_REVISION);

  /**
   * Base URL for all API calls, not hardcoded to enable testing
   *
   * @since 1.0.0
   */
  public static final String BASE_API_URL = "https://api.mapbox.com";

}
