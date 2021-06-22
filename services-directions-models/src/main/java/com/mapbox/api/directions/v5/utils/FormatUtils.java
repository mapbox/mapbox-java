package com.mapbox.api.directions.v5.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mapbox.api.directions.v5.models.Bearing;
import com.mapbox.geojson.Point;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Methods to convert models to Strings.
 */
public class FormatUtils {

  /**
   * Date-time <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a> pattern.
   */
  public static final String ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm";

  /**
   * Returns a string containing the tokens joined by delimiters.
   *
   * @param delimiter the delimiter on which to split.
   * @param tokens    A list of objects to be joined. Strings will be formed from the objects by
   *                  calling object.toString().
   *                  If a token in a list is null, an empty string is appended in its place.
   * @return {@link String} with joint values, empty string if the token list was empty,
   *   or null if the tokens list was null
   */
  @Nullable
  public static String join(@NonNull CharSequence delimiter, @Nullable List<?> tokens) {
    if (tokens == null || tokens.isEmpty()) {
      return null;
    }

    StringBuilder sb = new StringBuilder();
    boolean firstTime = true;
    for (int i = 0; i <= tokens.size() - 1; i++) {
      if (firstTime) {
        firstTime = false;
      } else {
        sb.append(delimiter);
      }
      Object token = tokens.get(i);
      if (token != null) {
        sb.append(token);
      }
    }
    return sb.toString();
  }

  /**
   * Useful to remove any trailing zeros and prevent a double being over 7 significant figures.
   *
   * @param value a double value
   * @return a formatted string.
   */
  @NonNull
  public static String formatDouble(double value) {
    DecimalFormat decimalFormat = new DecimalFormat("0.#######",
      new DecimalFormatSymbols(Locale.US));
    return String.format(Locale.US, "%s",
      decimalFormat.format(value));
  }

  /**
   * Used in various APIs to format the user provided radiuses to a String matching the APIs
   * format.
   *
   * @param radiuses a list of doubles represents the radius values
   * @return a String ready for being passed into the Retrofit call
   */
  @Nullable
  public static String formatRadiuses(@Nullable List<Double> radiuses) {
    if (radiuses == null) {
      return null;
    }

    List<String> radiusesToJoin = new ArrayList<>();
    for (Double radius : radiuses) {
      if (radius == null) {
        radiusesToJoin.add(null);
      } else if (radius == Double.POSITIVE_INFINITY) {
        radiusesToJoin.add("unlimited");
      } else {
        radiusesToJoin.add(String.format(Locale.US, "%s", formatDouble(radius)));
      }
    }
    return join(";", radiusesToJoin);
  }

  /**
   * Formats the bearing variables from the raw values to a string ready for API consumption.
   *
   * @param bearings a List of {@link Bearing} values
   * @return a string with the bearing values
   */
  @Nullable
  public static String formatBearings(@Nullable List<Bearing> bearings) {
    if (bearings == null) {
      return null;
    }

    List<String> bearingsToJoin = new ArrayList<>();
    for (Bearing bearing : bearings) {
      if (bearing == null) {
        bearingsToJoin.add(null);
      } else {
        double angle = bearing.angle();
        double tolerance = bearing.degrees();

        bearingsToJoin.add(String.format(Locale.US, "%s,%s",
          formatDouble(angle),
          formatDouble(tolerance)));
      }
    }
    return join(";", bearingsToJoin);
  }

  /**
   * Converts the list of distributions to a string ready for API consumption.
   *
   * @param distributions the list of integer arrays representing the distribution
   * @return a string with the distribution values
   */
  @Nullable
  public static String formatDistributions(@Nullable List<Integer[]> distributions) {
    if (distributions == null || distributions.isEmpty()) {
      return null;
    }

    List<String> distributionsToJoin = new ArrayList<>();
    for (Integer[] array : distributions) {
      if (array.length == 0) {
        distributionsToJoin.add(null);
      } else {
        distributionsToJoin.add(String.format(Locale.US, "%s,%s",
          formatDouble(array[0]),
          formatDouble(array[1])));
      }
    }
    return join(";", distributionsToJoin);
  }

  /**
   * Converts list of points to a string ready for API consumption.
   *
   * @param points a list representing of points
   * @return a formatted string with semicolon separated pairs of doubles
   */
  @Nullable
  public static String formatPointsList(@Nullable List<Point> points) {
    if (points == null) {
      return null;
    }

    List<String> coordinatesToJoin = new ArrayList<>();
    for (Point point : points) {
      if (point == null) {
        coordinatesToJoin.add(null);
      } else {
        coordinatesToJoin.add(String.format(Locale.US, "%s,%s",
          formatDouble(point.longitude()),
          formatDouble(point.latitude())));
      }
    }
    return join(";", coordinatesToJoin);
  }
}
