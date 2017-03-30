package com.mapbox.services.api.directions.v5;

/**
 * Constants used to customize the directions request.
 *
 * @since 1.0.0
 */
public class DirectionsCriteria {

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
   * Format to return route geometry will be geojson. Note that this isn't supported by the SDK.
   *
   * @since 1.0.0
   */
  private static final String GEOMETRY_GEOJSON = "geojson";

  /**
   * A simplified version of the {@link #OVERVIEW_FULL} geometry. If not specified simplified is the default.
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
   * Server responds with no errors.
   *
   * @since 1.0.0
   */
  public static final String RESPONSE_OK = "Ok";

  /**
   * There was no route found for the given query.
   *
   * @since 1.0.0
   */
  public static final String RESPONSE_NO_ROUTE = "NoRoute";

  /**
   * Use a valid profile as described above.
   *
   * @since 1.0.0
   */
  public static final String RESPONSE_PROFILE_NOT_FOUND = "ProfileNotFound";

  /**
   * The given request was not valid.
   *
   * @since 1.0.0
   */
  public static final String RESPONSE_INVALID_INPUT = "InvalidInput";

}
