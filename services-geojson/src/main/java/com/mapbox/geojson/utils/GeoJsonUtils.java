package com.mapbox.geojson.utils;

/**
 * GeoJson utils class contains method that can be used throughout geojson package.
 *
 * @since 4.3.0
 */
public class GeoJsonUtils {

  private static double ROUND_PRECISION = 10000000.0;
  private static long MAX_DOUBLE_TO_ROUND = (long) (Long.MAX_VALUE / ROUND_PRECISION);

  /**
   * Trims a double value to have only 7 digits after period.
   *
   * @param value to be trimed
   * @return trimmed value
   */
  public static double trim(double value) {
    if (value > MAX_DOUBLE_TO_ROUND || value < -MAX_DOUBLE_TO_ROUND) {
      return value;
    }
    return Math.round(value * ROUND_PRECISION) / ROUND_PRECISION;
  }
}
