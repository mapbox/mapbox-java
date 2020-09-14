package com.mapbox.api.directions.v5.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mapbox.geojson.Point;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
   * Returns a string containing the tokens joined by delimiters. Doesn't remove trailing nulls.
   *
   * @param delimiter the delimiter on which to split.
   * @param tokens A list of objects to be joined. Strings will be formed from the objects by
   *               calling object.toString().
   * @return {@link String}
   */
  @Nullable
  public static String join(@NonNull CharSequence delimiter, @Nullable List<?> tokens) {
    return join(delimiter, tokens, false);
  }

  /**
   * Returns a string containing the tokens joined by delimiters.
   *
   * @param delimiter the delimiter on which to split.
   * @param tokens A list of objects to be joined. Strings will be formed from the objects by
   *               calling object.toString().
   * @param removeTrailingNulls true if trailing nulls should be removed.
   * @return {@link String}
   */
  @Nullable
  public static String join(@NonNull CharSequence delimiter, @Nullable List<?> tokens,
      boolean removeTrailingNulls) {
    if (tokens == null || tokens.size() < 1) {
      return null;
    }

    int lastNonNullToken = tokens.size() - 1;
    if (removeTrailingNulls) {
      for (int i = tokens.size() - 1; i >= 0; i--) {
        Object token = tokens.get(i);
        if (token != null) {
          break;
        } else {
          lastNonNullToken--;
        }
      }
    }

    StringBuilder sb = new StringBuilder();
    boolean firstTime = true;
    for (int i = 0; i <= lastNonNullToken; i++) {
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
   * Useful to remove any trailing zeros and prevent a coordinate being over 7 significant figures.
   *
   * @param coordinate a double value representing a coordinate.
   * @return a formatted string.
   */
  @NonNull
  public static String formatCoordinate(double coordinate) {
    DecimalFormat decimalFormat = new DecimalFormat("0.######",
        new DecimalFormatSymbols(Locale.US));
    return String.format(Locale.US, "%s",
        decimalFormat.format(coordinate));
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
    if (radiuses == null || radiuses.size() == 0) {
      return null;
    }

    List<String> radiusesToJoin = new ArrayList<>();
    for (Double radius : radiuses) {
      if (radius == null) {
        radiusesToJoin.add(null);
      } else if (radius == Double.POSITIVE_INFINITY) {
        radiusesToJoin.add("unlimited");
      } else {
        radiusesToJoin.add(String.format(Locale.US, "%s", formatCoordinate(radius)));
      }
    }
    return join(";", radiusesToJoin);
  }

  /**
   * Formats the bearing variables from the raw values to a string which can than be used for the
   * request URL.
   *
   * @param bearings a List of list of doubles representing bearing values
   * @return a string with the bearing values
   */
  @Nullable
  public static String formatBearings(@Nullable List<List<Double>> bearings) {
    if (bearings == null || bearings.isEmpty()) {
      return null;
    }

    List<String> bearingsToJoin = new ArrayList<>();
    for (List<Double> bearing : bearings) {
      if (bearing == null) {
        bearingsToJoin.add(null);
      } else {
        if (bearing.size() != 2) {
          throw new RuntimeException("Bearing size should be 2.");
        }

        Double angle = bearing.get(0);
        Double tolerance = bearing.get(1);
        if (angle == null || tolerance == null) {
          bearingsToJoin.add(null);
        } else {
          if (angle < 0 || angle > 360 || tolerance < 0 || tolerance > 360) {
            throw new RuntimeException("Angle and tolerance have to be from 0 to 360.");
          }

          bearingsToJoin.add(String.format(Locale.US, "%s,%s",
              formatCoordinate(angle),
              formatCoordinate(tolerance)));
        }
      }
    }
    return join(";", bearingsToJoin);
  }

  /**
   * Converts the list of integer arrays to a string ready for API consumption.
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
            formatCoordinate(array[0]),
            formatCoordinate(array[1])));
      }
    }
    return join(";", distributionsToJoin);
  }

  /**
   * Converts String list with approaches values to a string ready for API consumption. An approach
   * could be unrestricted, curb or null.
   *
   * @param approaches a list representing approaches to each coordinate.
   * @return a formatted string.
   */
  @Nullable
  public static String formatApproaches(@Nullable List<String> approaches) {
    if (approaches == null || approaches.isEmpty()) {
      return null;
    }

    for (String approach : approaches) {
      if (approach != null && !approach.equals("unrestricted") && !approach.equals("curb")
          && !approach.isEmpty()) {
        return null;
      }
    }
    return join(";", approaches);
  }

  /**
   * Converts String list with waypoint_names values to a string ready for API consumption.
   *
   * @param waypointNames a string representing approaches to each coordinate.
   * @return a formatted string.
   */
  @Nullable
  public static String formatWaypointNames(@Nullable List<String> waypointNames) {
    if (waypointNames == null || waypointNames.isEmpty()) {
      return null;
    }

    return join(";", waypointNames);
  }

  /**
   * Converts a list of Points to String.
   *
   * @param coordinates a list of coordinates.
   * @return a formatted string.
   */
  @Nullable
  public static String formatCoordinates(@NonNull List<Point> coordinates) {
    List<String> coordinatesToJoin = new ArrayList<>();
    for (Point point : coordinates) {
      coordinatesToJoin.add(String.format(Locale.US, "%s,%s",
          formatCoordinate(point.longitude()),
          formatCoordinate(point.latitude())));
    }

    return join(";", coordinatesToJoin);
  }

  /**
   * Converts array of Points with waypoint_targets values to a string ready for API consumption.
   *
   * @param points a list representing approaches to each coordinate.
   * @return a formatted string.
   */
  @Nullable
  public static String formatPointsList(@Nullable List<Point> points) {
    if (points == null || points.isEmpty()) {
      return null;
    }

    List<String> coordinatesToJoin = new ArrayList<>();
    for (Point point : points) {
      if (point == null) {
        coordinatesToJoin.add(null);
      } else {
        coordinatesToJoin.add(String.format(Locale.US, "%s,%s",
            formatCoordinate(point.longitude()),
            formatCoordinate(point.latitude())));
      }
    }
    return join(";", coordinatesToJoin);
  }

  /**
   * Format {@link Date} to <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a>
   * Date-Time format.
   *
   * @param date that has to be formatted
   * @see #ISO_8601_PATTERN
   */
  @Nullable
  public static String formatDateToIso8601(@Nullable Date date) {
    if (date == null) {
      return null;
    } else {
      return new SimpleDateFormat(ISO_8601_PATTERN).format(date);
    }
  }
}
