package com.mapbox.directions.v5;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Constants and properties used to customize the directions request.
 *
 * @since 1.0.0
 */
public final class DirectionsCriteria {

  /**
   * Mapbox default username.
   *
   * @since 1.0.0
   */
  public static final String PROFILE_DEFAULT_USER = "mapbox";

  /**
   * For car and motorcycle routing. This profile factors in current and historic traffic
   * conditions to avoid slowdowns.
   *
   * @since 2.0.0
   */
  public static final String PROFILE_DRIVING_TRAFFIC = "driving-traffic";

  /**
   * For car and motorcycle routing. This profile shows the fastest routes by preferring
   * high-speed roads like highways.
   *
   * @since 1.0.0
   */
  public static final String PROFILE_DRIVING = "driving";

  /**
   * For pedestrian and hiking routing. This profile shows the shortest path by using sidewalks
   * and trails.
   *
   * @since 1.0.0
   */
  public static final String PROFILE_WALKING = "walking";

  /**
   * For bicycle routing. This profile shows routes that are short and safe for cyclist, avoiding
   * highways and preferring streets with bike lanes.
   *
   * @since 1.0.0
   */
  public static final String PROFILE_CYCLING = "cycling";

  /**
   * Retention policy for the various direction profiles.
   *
   * @since 3.0.0
   */
  @Retention(RetentionPolicy.SOURCE)
  @StringDef( {
    PROFILE_DRIVING_TRAFFIC,
    PROFILE_DRIVING,
    PROFILE_WALKING,
    PROFILE_CYCLING
  })
  public @interface ProfileCriteria {
  }

  /**
   * Format to return route geometry will be an encoded polyline.
   *
   * @since 1.0.0
   */
  public static final String GEOMETRY_POLYLINE = "polyline";

  /**
   * Format to return route geometry will be an encoded polyline with precision 6.
   *
   * @since 2.0.0
   */
  public static final String GEOMETRY_POLYLINE6 = "polyline6";

  /**
   * Retention policy for the various direction geometries.
   *
   * @since 3.0.0
   */
  @Retention(RetentionPolicy.SOURCE)
  @StringDef( {
    GEOMETRY_POLYLINE,
    GEOMETRY_POLYLINE6
  })
  public @interface GeometriesCriteria {
  }

  /**
   * A simplified version of the {@link #OVERVIEW_FULL} geometry. If not specified simplified is
   * the default.
   *
   * @since 1.0.0
   */
  public static final String OVERVIEW_SIMPLIFIED = "simplified";

  /**
   * The most detailed geometry available.
   *
   * @since 1.0.0
   */
  public static final String OVERVIEW_FULL = "full";

  /**
   * No overview geometry.
   *
   * @since 1.0.0
   */
  public static final String OVERVIEW_FALSE = "false";

  /**
   * Retention policy for the various direction overviews.
   *
   * @since 3.0.0
   */
  @Retention(RetentionPolicy.SOURCE)
  @StringDef( {
    OVERVIEW_FALSE,
    OVERVIEW_FULL,
    OVERVIEW_SIMPLIFIED
  })
  public @interface OverviewCriteria {
  }

  /**
   * The duration, in seconds, between each pair of coordinates.
   *
   * @since 2.1.0
   */
  public static final String ANNOTATION_DURATION = "duration";

  /**
   * The distance, in meters, between each pair of coordinates.
   *
   * @since 2.1.0
   */
  public static final String ANNOTATION_DISTANCE = "distance";

  /**
   * The speed, in km/h, between each pair of coordinates.
   *
   * @since 2.1.0
   */
  public static final String ANNOTATION_SPEED = "speed";

  /**
   * The congestion, provided as a String, between each pair of coordinates.
   *
   * @since 2.2.0
   */
  public static final String ANNOTATION_CONGESTION = "congestion";

  /**
   * Retention policy for the various direction annotations.
   *
   * @since 3.0.0
   */
  @Retention(RetentionPolicy.SOURCE)
  @StringDef( {
    ANNOTATION_CONGESTION,
    ANNOTATION_DISTANCE,
    ANNOTATION_DURATION,
    ANNOTATION_SPEED
  })
  public @interface AnnotationCriteria {
  }

  private DirectionsCriteria() {
    //not called
  }
}
