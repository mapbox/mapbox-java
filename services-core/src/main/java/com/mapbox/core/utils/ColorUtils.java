package com.mapbox.core.utils;

/**
 * Utils class for assisting with color transformations and other operations being done on colors.
 *
 * @since 3.0.0
 */
public final class ColorUtils {

  private ColorUtils() {
    // No Instance.
  }

  /**
   * Converts red, green, blue values to a hex string that can then be used in a URL when making API
   * request. Note that this does <b>Not</b> add the hex key before the string.
   *
   * @param red the value of the color which needs to be converted
   * @param green the value of the color which needs to be converted
   * @param blue the value of the color which needs to be converted
   * @return the hex color value as a string
   * @since 3.1.0
   */
  public static String toHexString(int red, int green, int blue) {
    String hexColor
      = String.format("%02X%02X%02X", red, green, blue);

    if (hexColor.length() < 6) {
      hexColor = "000000".substring(0, 6 - hexColor.length()) + hexColor;
    }
    return hexColor;
  }
}
