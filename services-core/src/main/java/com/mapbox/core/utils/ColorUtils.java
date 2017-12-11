package com.mapbox.core.utils;

import java.awt.Color;

public final class ColorUtils {
  public static String toHexString(Color color) {
    String hexColour = Integer.toHexString(color.getRGB() & 0xffffff);
    if (hexColour.length() < 6) {
      hexColour = "000000".substring(0, 6 - hexColour.length()) + hexColour;
    }
    return hexColour;
  }
}
