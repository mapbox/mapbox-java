package com.mapbox.core.utils;

import java.awt.Color;

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
   * Converts a {@link Color} object to a hex string that can then be used in a URL when making API
   * request. Note that this does <b>Not</b> add the hex key before the string.
   *
   * @param color the color which needs to be converted
   * @return the hex color value as a string
   * @since 3.0.0
   */
  public static String toHexString(Color color) {
    String hexColor
      = String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());

    if (hexColor.length() < 6) {
      hexColor = "000000".substring(0, 6 - hexColor.length()) + hexColor;
    }
    return hexColor;
  }
}
