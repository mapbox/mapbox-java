package com.mapbox.api.directions.v5.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mapbox.geojson.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Methods to convert Strings to Lists of objects.
 */
public class ParseUtils {
  private static final String SEMICOLON = ";";
  private static final String COMMA = ",";
  private static final String UNLIMITED = "unlimited";
  private static final String TRUE = "true";
  private static final String FALSE = "false";

  /**
   * Parse a String to a list of Integers.
   *
   * @param original an original String.
   * @return List of Integers
   */
  @Nullable
  public static List<Integer> parseToIntegers(@Nullable String original) {
    if (original == null) {
      return null;
    }

    List<Integer> integers = new ArrayList<>();
    String[] strings = original.split(SEMICOLON);
    for (String index : strings) {
      if (index != null) {
        if (index.isEmpty()) {
          integers.add(null);
        } else {
          integers.add(Integer.valueOf(index));
        }
      }
    }

    return integers;
  }

  /**
   * Parse a String to a list of Strings using ";" as a separator.
   *
   * @param original an original String.
   * @return List of Strings
   */
  @Nullable
  public static List<String> parseToStrings(@Nullable String original) {
    return parseToStrings(original, SEMICOLON);
  }

  /**
   * Parse a String to a list of Strings.
   *
   * @param original an original String.
   * @param separator a String used as a separator.
   * @return List of Strings
   */
  @Nullable
  public static List<String> parseToStrings(@Nullable String original, @NonNull String separator) {
    if (original == null) {
      return null;
    }

    List<String> result = new ArrayList<>();
    String[] strings = original.split(separator, -1);
    for (String str : strings) {
      if (str != null) {
        if (str.isEmpty()) {
          result.add(null);
        } else {
          result.add(str);
        }
      }
    }

    return result;
  }

  /**
   * Parse a String to a list of Points.
   *
   * @param original an original String.
   * @return List of Points
   */
  @Nullable
  public static List<Point> parseToPoints(@Nullable String original) {
    if (original == null) {
      return null;
    }

    List<Point> points = new ArrayList<>();
    String[] targets = original.split(SEMICOLON, -1);
    for (String target : targets) {
      if (target != null) {
        if (target.isEmpty()) {
          points.add(null);
        } else {
          String[] point = target.split(COMMA);
          points.add(Point.fromLngLat(Double.valueOf(point[0]), Double.valueOf(point[1])));
        }
      }
    }

    return points;
  }

  /**
   * Parse a String to a list of Points.
   *
   * @param original an original String.
   * @return List of Doubles
   */
  @Nullable
  public static List<Double> parseToDoubles(@Nullable String original) {
    if (original == null) {
      return null;
    }

    List<Double> doubles = new ArrayList<>();
    String[] strings = original.split(SEMICOLON, -1);
    for (String str : strings) {
      if (str != null) {
        if (str.isEmpty()) {
          doubles.add(null);
        } else if (str.equals(UNLIMITED)) {
          doubles.add(Double.POSITIVE_INFINITY);
        } else {
          doubles.add(Double.valueOf(str));
        }
      }
    }

    return doubles;
  }

  /**
   * Parse a String to a list of list of Doubles.
   *
   * @param original an original String.
   * @return List of List of Doubles
   */
  @Nullable
  public static List<List<Double>> parseToListOfListOfDoubles(@Nullable String original) {
    if (original == null) {
      return null;
    }

    List<List<Double>> result = new ArrayList<>();
    String[] pairs = original.split(SEMICOLON, -1);
    for (String pair : pairs) {
      if (pair.isEmpty()) {
        result.add(null);
      } else {
        String[] values = pair.split(COMMA);
        if (values.length == 2) {
          result.add(Arrays.asList(Double.valueOf(values[0]), Double.valueOf(values[1])));
        }
      }
    }

    return result;
  }

  /**
   * Parse a String to a list of Boolean.
   *
   * @param original an original String.
   * @return List of Booleans
   */
  @Nullable
  public static List<Boolean> parseToBooleans(@Nullable String original) {
    if (original == null) {
      return null;
    }

    List<Boolean> booleans = new ArrayList<>();
    if (original.isEmpty()) {
      return booleans;
    }

    String[] strings = original.split(SEMICOLON, -1);
    for (String str : strings) {
      if (str != null) {
        if (str.isEmpty()) {
          booleans.add(null);
        } else if (str.equalsIgnoreCase(TRUE)) {
          booleans.add(true);
        } else if (str.equalsIgnoreCase(FALSE)) {
          booleans.add(false);
        } else {
          booleans.add(null);
        }
      }
    }

    return booleans;
  }
}
