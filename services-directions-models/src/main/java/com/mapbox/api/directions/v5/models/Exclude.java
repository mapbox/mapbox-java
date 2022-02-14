package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.utils.ParseUtils;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Defines excludes for {@link RouteOptions.Builder#excludeObject(Exclude)}.
 */
@AutoValue
public abstract class Exclude {

  /**
   * Build a new instance of {@link Exclude}. Nothing is excluded by default.
   */
  @NonNull
  public static Builder builder() {
    return new AutoValue_Exclude.Builder();
  }

  /**
   * Exclude certain road types from routing. The default is to not exclude anything from the
   * profile selected. The following exclude flags are available for each profile:
   * <p>
   * {@link DirectionsCriteria#PROFILE_DRIVING}: One of {@link DirectionsCriteria#EXCLUDE_TOLL},
   * {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or {@link DirectionsCriteria#EXCLUDE_FERRY}.
   * <p>
   * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}: One of
   * {@link DirectionsCriteria#EXCLUDE_TOLL}, {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or
   * {@link DirectionsCriteria#EXCLUDE_FERRY}.
   * <p>
   * {@link DirectionsCriteria#PROFILE_WALKING}: No excludes supported
   * <p>
   * {@link DirectionsCriteria#PROFILE_CYCLING}: {@link DirectionsCriteria#EXCLUDE_FERRY}
   *
   * @return a list of strings matching one
   *   of the {@link DirectionsCriteria.ExcludeCriteria} exclusions
   */
  @Nullable
  public abstract List<String> criteria();

  /**
   * Exclude certain points from routing. The default is to not exclude anything.
   *
   * @return a list of points excluded from the routing.
   */
  @Nullable
  public abstract List<Point> points();

  /**
   * Use this builder to build an {@link Exclude} object.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Exclude certain road types from routing. The default is to not exclude anything from the
     * profile selected. The following exclude flags are available for each profile:
     * <p>
     * {@link DirectionsCriteria#PROFILE_DRIVING}: One of {@link DirectionsCriteria#EXCLUDE_TOLL},
     * {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or {@link DirectionsCriteria#EXCLUDE_FERRY}.
     * <p>
     * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}: One of
     * {@link DirectionsCriteria#EXCLUDE_TOLL}, {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or
     * {@link DirectionsCriteria#EXCLUDE_FERRY}.
     * <p>
     * {@link DirectionsCriteria#PROFILE_WALKING}: No excludes supported
     * <p>
     * {@link DirectionsCriteria#PROFILE_CYCLING}: {@link DirectionsCriteria#EXCLUDE_FERRY}
     *
     * @param criteria a list of exclude criteria
     *   matching {@link DirectionsCriteria.ExcludeCriteria}
     * @return this builder.
     */
    public abstract Builder criteria(@Nullable List<String> criteria);

    /**
     * Exclude certain points from routing. The default is to not exclude anything.
     *
     * @param points a list of points to exclude.
     * @return this builder.
     */
    public abstract Builder points(@Nullable List<Point> points);

    /**
     * Builds the object.
     *
     * @return a new instance of {@link Exclude}
     */
    public abstract Exclude build();
  }

  @Nullable
  static Exclude fromUrlQueryParameter(@Nullable String queryParameter) {
    if (queryParameter == null || Objects.equals(queryParameter, "")) {
      return null;
    }

    ArrayList<Point> points = new ArrayList<>();
    ArrayList<String> criteria = new ArrayList<>();

    List<String> excludeItems = ParseUtils.parseToStrings(queryParameter, ",");
    if (excludeItems != null) {
      for (String excludeItem : excludeItems) {
        if (excludeItem.startsWith("point(") && excludeItem.endsWith(")")) {
          Point point = parsePoint(excludeItem);
          points.add(point);
        } else if (excludeItem.contains("(") && excludeItem.contains(")")) {
          // Ignoring the unexpected type of data. Update the library to get support of new types
        } else if (excludeItem.contains("(") || excludeItem.contains(")")) {
          throw new IllegalArgumentException("Can't parse parameter " + excludeItem);
        } else {
          criteria.add(excludeItem);
        }
      }
    }

    if (criteria.size() == 0) {
      criteria = null;
    }
    if (points.size() == 0) {
      points = null;
    }
    if (criteria == null && points == null) {
      return null;
    }

    return Exclude.builder()
        .criteria(criteria)
        .points(points)
        .build();
  }

  @Nullable
  String toUrlQueryParameter() {
    if (points() == null && criteria() == null) {
      return null;
    }

    StringBuilder result = new StringBuilder();

    appendPoints(result);
    appendCriterias(result);

    return result.toString();
  }

  private void appendCriterias(StringBuilder result) {
    if (criteria() != null) {
      for (String item : criteria()) {
        if (result.length() != 0) {
          result.append(',');
        }
        result.append(item);
      }
    }
  }

  private void appendPoints(StringBuilder result) {
    if (points() != null) {
      for (Point point : points()) {
        if (result.length() != 0) {
          result.append(",");
        }
        appendPoint(result, point);
      }
    }
  }

  private static String pointParsingErrorMessage(String point) {
    return "Can't parse a point:" + point + ". Expected format: point(lon lat)";
  }

  private static Point parsePoint(String point) {
    int delimiter = point.indexOf(' ');
    if (delimiter != -1) {
      String longitude = point.substring(6, delimiter);
      String latitude = point.substring(delimiter + 1, point.length() - 1);
      try {
        Point result = Point.fromLngLat(
            Double.parseDouble(longitude),
            Double.parseDouble(latitude)
        );
        return result;
      } catch (NumberFormatException numberFormatError) {
        throw new IllegalArgumentException(
            pointParsingErrorMessage(point),
            numberFormatError
        );
      }
    } else {
      throw new IllegalArgumentException(pointParsingErrorMessage(point));
    }
  }

  private void appendPoint(StringBuilder result, Point point) {
    result.append("point(")
        .append(point.longitude())
        .append(' ')
        .append(point.latitude())
        .append(')');
  }
}
