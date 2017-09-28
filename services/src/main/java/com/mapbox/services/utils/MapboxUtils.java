package com.mapbox.services.utils;

/**
 * Misc utils around Mapbox services.
 *
 * @since 1.0.0
 */
public class MapboxUtils {

  private MapboxUtils() {
    // Empty private constructor since only static methods are found inside class.
  }

  /**
   * Checks that the provided access token is not empty or null, and that it starts with
   * the right prefixes. Note that this method does not check Mapbox servers to verify that
   * it actually belongs to an account.
   *
   * @param accessToken A Mapbox access token.
   * @return true if the provided access token is valid, false otherwise.
   * @since 1.0.0
   */
  public static boolean isAccessTokenValid(String accessToken) {
    return !TextUtils.isEmpty(accessToken) && !(!accessToken.startsWith("pk.")
      && !accessToken.startsWith("sk.") && !accessToken.startsWith("tk."));
  }
}
