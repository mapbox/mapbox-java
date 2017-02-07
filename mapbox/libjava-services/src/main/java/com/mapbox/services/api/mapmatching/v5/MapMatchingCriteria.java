package com.mapbox.services.api.mapmatching.v5;

/**
 * Constant used for the MapMatching request.
 *
 * @since 2.0.0
 */
public class MapMatchingCriteria {

  public static final String PROFILE_DEFAULT_USER = "mapbox";

  /**
   * Matches trace to the road network.
   *
   * @since 2.0.0
   */
  public static final String PROFILE_DRIVING = "driving";

  /**
   * Matches trace to pedestrian and hiking routing.
   *
   * @since 2.0.0
   */
  public static final String PROFILE_WALKING = "walking";

  /**
   * Matches trace to bicycle routing.
   *
   * @since 2.0.0
   */
  public static final String PROFILE_CYCLING = "cycling";

  /**
   * The geometry returned will be in the GeoJSON format.
   *
   * @since 2.0.0
   */
  public static final String GEOMETRY_GEOJSON = "geojson";

  /**
   * The geometry returned will be an encoded polyline with precision 5.
   *
   * @since 2.0.0
   */
  public static final String GEOMETRY_POLYLINE = "polyline";

  /**
   * The geometry returned will be an encoded polyline with precision 6.
   *
   * @since 2.0.0
   */
  public static final String GEOMETRY_POLYLINE_6 = "polyline6";

  /**
   * A simplified version of the {@link #OVERVIEW_FULL} geometry. If not specified simplified is the default.
   *
   * @since 2.0.0
   */
  public static final String OVERVIEW_SIMPLIFIED = "simplified";

  /**
   * The most detailed geometry available.
   *
   * @since 2.0.0
   */
  public static final String OVERVIEW_FULL = "full";

  /**
   * No overview geometry.
   *
   * @since 2.0.0
   */
  public static final String OVERVIEW_FALSE = "false";

  /**
   * Normal case
   *
   * @since 2.0.0
   */
  public static final String RESPONSE_OK = "Ok";

  /**
   * The input did not produce any matches. Features will be an empty array.
   *
   * @since 2.0.0
   */
  public static final String RESPONSE_NO_MATCH = "NoMatch";

  /**
   * There are more than 100 points in the request.
   *
   * @since 2.0.0
   */
  public static final String RESPONSE_TOO_MANY_COORDINATES = "TooManyCoordinates";

  /**
   * message will hold an explanation of the invalid input.
   *
   * @since 2.0.0
   */
  public static final String RESPONSE_INVALID_INPUT = "InvalidInput";

  /**
   * Profile should be  mapbox.driving, mapbox.walking, or mapbox.cycling.
   *
   * @since 2.0.0
   */
  public static final String RESPONSE_PROFILE_NOT_FOUND = "ProfileNotFound";

}