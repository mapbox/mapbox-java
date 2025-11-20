package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.utils.ParseUtils;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Defines excludes for {@link RouteOptions.Builder#excludeObject(Exclude)}.
 *
 * This class provides type-safe way to read and build {@link RouteOptions#exclude()} parameter.
 *
 * All properties are strictly categorized after parsing. Unknown data types or flags
 * are ignored. If you want to work with exclude criteria which is not yet supported,
 * consider using raw {@link RouteOptions#exclude()} directly.
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
public abstract class Exclude extends DirectionsJsonObject {

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
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

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
          Point point = parsePointOrNull(excludeItem);
          if (point != null) {
            points.add(point);
          }
        } else if (VALID_EXCLUDE_CRITERIA.contains(excludeItem)) {
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

  @Nullable
  private static Point parsePointOrNull(String point) {
    int delimiter = point.indexOf(' ');
    if (delimiter != -1) {
      String longitude = point.substring(6, delimiter);
      String latitude = point.substring(delimiter + 1, point.length() - 1);
      try {
        return Point.fromLngLat(
          Double.parseDouble(longitude),
          Double.parseDouble(latitude)
        );
      } catch (NumberFormatException numberFormatError) {
        return null;
      }
    } else {
      return null;
    }
  }

  private void appendPoint(StringBuilder result, Point point) {
    result.append("point(")
      .append(point.longitude())
      .append(' ')
      .append(point.latitude())
      .append(')');
  }

  private static final Set<String> VALID_EXCLUDE_CRITERIA = new HashSet<String>() {
    {
      add(DirectionsCriteria.EXCLUDE_FERRY);
      add(DirectionsCriteria.EXCLUDE_MOTORWAY);
      add(DirectionsCriteria.EXCLUDE_TOLL);
      add(DirectionsCriteria.EXCLUDE_TUNNEL);
      add(DirectionsCriteria.EXCLUDE_RESTRICTED);
      add(DirectionsCriteria.EXCLUDE_CASH_ONLY_TOLLS);
      add(DirectionsCriteria.EXCLUDE_UNPAVED);
    }
  };
}
