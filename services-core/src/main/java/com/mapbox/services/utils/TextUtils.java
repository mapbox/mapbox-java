package com.mapbox.services.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

/**
 * We avoid including a full library like org.apache.commons:commons-lang3 to avoid an unnecessary
 * large number of methods, which is inconvenient to Android devs.
 *
 * @see <a href="https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/text/TextUtils.java">Some code came from this source.</a>
 * @since 1.0.0
 */
public final class TextUtils {

  private TextUtils() {
    // Empty private constructor preventing class from getting initialized
  }

  /**
   * Returns true if the string is null or 0-length.
   *
   * @param str the string to be examined
   * @return true if str is null or zero length
   * @since 1.0.0
   */
  public static boolean isEmpty(CharSequence str) {
    return str == null || str.length() == 0;
  }

  /**
   * Returns a string containing the tokens joined by delimiters.
   *
   * @param delimiter the delimeter on which to split.
   * @param tokens    An array objects to be joined. Strings will be formed from the objects by
   *                  calling object.toString().
   * @return {@link String}
   * @since 1.0.0
   */
  public static String join(CharSequence delimiter, Object[] tokens) {
    if (tokens == null || tokens.length < 1) {
      return null;
    }

    StringBuilder sb = new StringBuilder();
    boolean firstTime = true;
    for (Object token : tokens) {
      if (firstTime) {
        firstTime = false;
      } else {
        sb.append(delimiter);
      }
      sb.append(token);
    }
    return sb.toString();
  }

  /**
   * Useful to remove any trailing zeros and prevent a coordinate being over 7 significant figures.
   *
   * @param coordinate a double value representing a coordinate.
   * @return a formatted string.
   * @since 2.1.0
   */
  public static String formatCoordinate(double coordinate) {
    DecimalFormat decimalFormat = new DecimalFormat("0.######",
      new DecimalFormatSymbols(Locale.US));
    return String.format(Locale.US, "%s",
      decimalFormat.format(coordinate));
  }

  /**
   * Allows the specific adjusting of a coordinates precision.
   *
   * @param coordinate a double value representing a coordinate.
   * @param precision  an integer value you'd like the precision to be at.
   * @return a formatted string.
   * @since 2.1.0
   */
  public static String formatCoordinate(double coordinate, int precision) {
    String pattern = "0." + new String(new char[precision]).replace("\0", "0");
    DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
    df.applyPattern(pattern);
    df.setRoundingMode(RoundingMode.FLOOR);
    return df.format(coordinate);
  }

  /**
   * Used in various APIs to format the user provided radiuses to a String matching the APIs format.
   *
   * @param radiuses a double array which represents the radius values
   * @return a String ready for being passed into the Retrofit call
   * @since 3.0.0
   */
  public static String formatRadiuses(double[] radiuses) {
    if (radiuses == null || radiuses.length == 0) {
      return null;
    }

    String[] radiusesFormatted = new String[radiuses.length];
    for (int i = 0; i < radiuses.length; i++) {
      if (radiuses[i] == Double.POSITIVE_INFINITY) {
        radiusesFormatted[i] = "unlimited";
      } else {
        radiusesFormatted[i] = String.format(Locale.US, "%s",
          TextUtils.formatCoordinate(radiuses[i]));
      }
    }
    return join(";", radiusesFormatted);
  }

  /**
   * Formats the bearing variables from the raw values to a string which can than be used for the
   * request URL.
   *
   * @param bearings a List of doubles representing bearing values
   * @return a string with the bearing values
   * @since 3.0.0
   */
  public static String formatBearing(List<Double[]> bearings) {
    if (bearings.isEmpty()) {
      return null;
    }

    String[] bearingFormatted = new String[bearings.size()];
    for (int i = 0; i < bearings.size(); i++) {
      if (bearings.get(i).length == 0) {
        bearingFormatted[i] = "";
      } else {
        bearingFormatted[i] = String.format(Locale.US, "%s,%s",
          TextUtils.formatCoordinate(bearings.get(i)[0]),
          TextUtils.formatCoordinate(bearings.get(i)[1]));
      }
    }
    return TextUtils.join(";", bearingFormatted);
  }

  /**
   * converts the list of integer arrays to a string ready for API consumption.
   *
   * @param distributions the list of integer arrays representing the distribution
   * @return a string with the distribution values
   * @since 3.0.0
   */
  public static String formatDistributions(List<Integer[]> distributions) {
    if (distributions.isEmpty()) {
      return null;
    }

    String[] distributionsFormatted = new String[distributions.size()];
    for (int i = 0; i < distributions.size(); i++) {
      if (distributions.get(i).length == 0) {
        distributionsFormatted[i] = "";
      } else {
        distributionsFormatted[i] = String.format(Locale.US, "%s,%s",
          TextUtils.formatCoordinate(distributions.get(i)[0]),
          TextUtils.formatCoordinate(distributions.get(i)[1]));
      }
    }
    return TextUtils.join(";", distributionsFormatted);
  }
}
