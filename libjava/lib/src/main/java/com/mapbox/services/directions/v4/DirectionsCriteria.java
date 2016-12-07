package com.mapbox.services.directions.v4;

/**
 * Constants used to customize the directions request. These constansts are shared by the
 * {@link com.mapbox.services.mapmatching.v4.MapboxMapMatching} object.
 *
 * @since 1.0.0
 * @deprecated Use Directions v5 instead
 */
@Deprecated
public final class DirectionsCriteria {

  /**
   * For car and motorcycle routing. This profile shows the fastest routes by preferring
   * high-speed roads like highways.
   *
   * @since 1.0.0
   * @deprecated Use Directions v5 instead or if MapMatching use
   * {@link com.mapbox.services.mapmatching.v4.MapMatchingCriteria}
   */
  @Deprecated
  public static final String PROFILE_DRIVING = "mapbox.driving";

  /**
   * For pedestrian and hiking routing. This profile shows the shortest path by using sidewalks
   * and trails.
   *
   * @since 1.0.0
   * @deprecated Use Directions v5 instead or if MapMatching use
   * {@link com.mapbox.services.mapmatching.v4.MapMatchingCriteria}
   */
  @Deprecated
  public static final String PROFILE_WALKING = "mapbox.walking";

  /**
   * For bicycle routing. This profile shows routes that are short and safe for cyclist, avoiding
   * highways and preferring streets with bike lanes.
   *
   * @since 1.0.0
   * @deprecated Use Directions v5 instead or if MapMatching use
   * {@link com.mapbox.services.mapmatching.v4.MapMatchingCriteria}
   */
  @Deprecated
  public static final String PROFILE_CYCLING = "mapbox.cycling";

  /**
   * Format to return route instructions will be text.
   *
   * @since 1.0.0
   * @deprecated Use Directions v5 instead or if MapMatching use
   * {@link com.mapbox.services.mapmatching.v4.MapMatchingCriteria}
   */
  @Deprecated
  public static final String INSTRUCTIONS_TEXT = "text";

  /**
   * Format to return route instructions will be html.
   *
   * @since 1.0.0
   * @deprecated Use Directions v5 instead or if MapMatching use
   * {@link com.mapbox.services.mapmatching.v4.MapMatchingCriteria}
   */
  @Deprecated
  public static final String INSTRUCTIONS_HTML = "html";

  /**
   * Format to return route geometry will be geojson.
   *
   * @since 1.0.0
   * @deprecated Use Directions v5 instead or if MapMatching use
   * {@link com.mapbox.services.mapmatching.v4.MapMatchingCriteria}
   */
  @Deprecated
  public static final String GEOMETRY_GEOJSON = "geojson";

  /**
   * Format to return route geometry will be encoded polyline.
   *
   * @since 1.0.0
   * @deprecated Use Directions v5 instead or if MapMatching use
   * {@link com.mapbox.services.mapmatching.v4.MapMatchingCriteria}
   */
  @Deprecated
  public static final String GEOMETRY_POLYLINE = "polyline";

  /**
   * Use false to omit geometry from response.
   *
   * @since 1.0.0
   * @deprecated Use Directions v5 instead or if MapMatching use
   * {@link com.mapbox.services.mapmatching.v4.MapMatchingCriteria}
   */
  @Deprecated
  public static final String GEOMETRY_FALSE = "false";

  /**
   * Normal case
   *
   * @since 1.0.0
   * @deprecated Use Directions v5 instead or if MapMatching use
   * {@link com.mapbox.services.mapmatching.v4.MapMatchingCriteria}
   */
  @Deprecated
  public static final String RESPONSE_OK = "Ok";

  /**
   * The input did not produce any matches. Features will be an empty array.
   *
   * @since 1.0.0
   * @deprecated Use Directions v5 instead or if MapMatching use
   * {@link com.mapbox.services.mapmatching.v4.MapMatchingCriteria}
   */
  @Deprecated
  public static final String RESPONSE_NO_MATCH = "NoMatch";

  /**
   * There are more than 100 points in the request.
   *
   * @since 1.0.0
   * @deprecated Use Directions v5 instead or if MapMatching use
   * {@link com.mapbox.services.mapmatching.v4.MapMatchingCriteria}
   */
  @Deprecated
  public static final String RESPONSE_TOO_MANY_COORDINATES = "TooManyCoordinates";

  /**
   * message will hold an explanation of the invalid input.
   *
   * @since 1.0.0
   * @deprecated Use Directions v5 instead or if MapMatching use
   * {@link com.mapbox.services.mapmatching.v4.MapMatchingCriteria}
   */
  @Deprecated
  public static final String RESPONSE_INVALID_INPUT = "InvalidInput";

  /**
   * Profile should be  mapbox.driving, mapbox.walking, or mapbox.cycling.
   *
   * @since 1.0.0
   * @deprecated Use Directions v5 instead or if MapMatching use
   * {@link com.mapbox.services.mapmatching.v4.MapMatchingCriteria}
   */
  @Deprecated
  public static final String RESPONSE_PROFILE_NOT_FOUND = "ProfileNotFound";

}
