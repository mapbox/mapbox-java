package com.mapbox.services.constants;

/**
 * Includes common variables used throughout the Mapbox Service modules.
 *
 * @since 3.0.0
 */
public final class Constants {

  protected Constants() {
    // Empty constructor prevents users from initializing this class
  }

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

  public static final String MAPBOX_USER = "mapbox";
}
