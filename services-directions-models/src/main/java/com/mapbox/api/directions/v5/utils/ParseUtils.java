package com.mapbox.api.directions.v5.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mapbox.geojson.Point;
import java.util.ArrayList;
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

  private interface ValueParser<T> {
    @NonNull
    T parse(@NonNull String element);
  }

  private static final ValueParser<Integer> INTEGER_PARSER = new ValueParser<Integer>() {
    @NonNull
    @Override
    public Integer parse(@NonNull String element) {
      return Integer.valueOf(element);
    }
  };

  private static final ValueParser<String> STRING_PARSER = new ValueParser<String>() {
    @NonNull
    @Override
    public String parse(@NonNull String element) {
      return element;
    }
  };

  private static final ValueParser<Point> POINT_PARSER = new ValueParser<Point>() {
    @NonNull
    @Override
    public Point parse(@NonNull String element) {
      String[] pointArray = element.split(COMMA);
      if (pointArray.length != 2) {
        throw new RuntimeException(
          "Point list should have exactly 2 values, longitude and latitude."
        );
      }
      return Point.fromLngLat(Double.parseDouble(pointArray[0]), Double.parseDouble(pointArray[1]));
    }
  };

  private static final ValueParser<Double> DOUBLE_PARSER = new ValueParser<Double>() {
    @NonNull
    @Override
    public Double parse(@NonNull String element) {
      if (element.equals(UNLIMITED)) {
        return Double.POSITIVE_INFINITY;
      } else {
        return Double.valueOf(element);
      }
    }
  };

  private static final ValueParser<Boolean> BOOLEAN_PARSER = new ValueParser<Boolean>() {
    @NonNull
    @Override
    public Boolean parse(@NonNull String element) {
      if (element.equalsIgnoreCase(TRUE)) {
        return true;
      } else if (element.equalsIgnoreCase(FALSE)) {
        return false;
      } else {
        throw new RuntimeException(
          "Boolean value should be either true or false string but is " + element
        );
      }
    }
  };

  private static final ValueParser<List<Double>> DOUBLE_LIST_PARSER =
    new ValueParser<List<Double>>() {
      @NonNull
      @Override
      public List<Double> parse(@NonNull String element) {
        String[] values = element.split(COMMA);
        List<Double> doubles = new ArrayList<>();
        for (String value : values) {
          doubles.add(Double.valueOf(value));
        }
        return doubles;
      }
    };

  /**
   * Parse a String to a list of values separated by the separator.
   * If separation finds empty strings, those will be added as nulls to the resulting list.
   *
   * @param original  an original String.
   * @param separator separator to split the original string by
   * @param parser    parser
   * @return List of values or null if the original string is null
   */
  @Nullable
  private static <T> List<T> parseToList(
    @NonNull String separator, @Nullable String original, @NonNull ValueParser<T> parser) {
    if (original == null) {
      return null;
    }

    List<T> result = new ArrayList<>();
    String[] elements = original.split(separator, -1);
    for (String element : elements) {
      if (element.isEmpty()) {
        result.add(null);
      } else {
        result.add(parser.parse(element));
      }
    }

    return result;
  }

  /**
   * Parse a semicolon separated String to a list of Integers.
   * If separation finds empty strings, those will be added as nulls to the resulting list.
   *
   * @param original an original String.
   * @return List of Integers or null if the original string is null
   */
  @Nullable
  public static List<Integer> parseToIntegers(@Nullable String original) {
    return parseToList(SEMICOLON, original, INTEGER_PARSER);
  }

  /**
   * Parse a semicolon separated String to a list of Strings.
   * If separation finds empty strings, those will be added as nulls to the resulting list.
   *
   * @param original an original String.
   * @return List of Strings or null if the original string is null
   */
  @Nullable
  public static List<String> parseToStrings(@Nullable String original) {
    return parseToList(SEMICOLON, original, STRING_PARSER);
  }

  /**
   * Parse a String to a list of Strings using the provided separator.
   * If separation finds empty strings, those will be added as nulls to the resulting list.
   *
   * @param original  an original String.
   * @param separator a String used as a separator.
   * @return List of Strings or null if the original string is null
   */
  @Nullable
  public static List<String> parseToStrings(@Nullable String original, @NonNull String separator) {
    return parseToList(separator, original, STRING_PARSER);
  }

  /**
   * Parse a semicolon separated String to a list of Points.
   * If separation finds empty strings, those will be added as nulls to the resulting list.
   * <p>
   * Elements of the string should have 2 comma separated double values,
   * longitude and latitude, for example:
   * "10.1,47.3;33.09,79.111"
   *
   * @param original an original String.
   * @return List of Points or null if the original string is null
   */
  @Nullable
  public static List<Point> parseToPoints(@Nullable String original) {
    return parseToList(SEMICOLON, original, POINT_PARSER);
  }

  /**
   * Parse a semicolon separated String to a list of Doubles.
   * If separation finds empty strings, those will be added as nulls to the resulting list.
   *
   * @param original an original String.
   * @return List of Doubles or null if the original string is null
   */
  @Nullable
  public static List<Double> parseToDoubles(@Nullable String original) {
    return parseToList(SEMICOLON, original, DOUBLE_PARSER);
  }

  /**
   * Parse a semicolon separated String to a list of lists of doubles.
   * If separation finds empty strings, those will be added as nulls to the resulting list.
   * <p>
   * Elements of the string should have a comma separated double values, for example:
   * ";;10.1,47.3,33.09,79.111;84.45,45.4;"
   *
   * @param original an original String.
   * @return List of List of Doubles or null if the original string is null
   */
  @Nullable
  public static List<List<Double>> parseToListOfListOfDoubles(@Nullable String original) {
    return parseToList(SEMICOLON, original, DOUBLE_LIST_PARSER);
  }

  /**
   * Parse a semicolon separated String to a list of Booleans.
   * If separation finds empty strings, those will be added as nulls to the resulting list.
   *
   * @param original an original String.
   * @return List of Booleans or null if the original string is null
   */
  @Nullable
  public static List<Boolean> parseToBooleans(@Nullable String original) {
    return parseToList(SEMICOLON, original, BOOLEAN_PARSER);
  }
}
