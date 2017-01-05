package com.mapbox.services.api.mapmatching.v4;

/**
 * Constant used for the MapMatching request.
 *
 * @since 2.0.0
 */
public class MapMatchingCriteria {
  /**
   * Matches trace to the road network.
   *
   * @since 2.0.0
   */
  public static final String PROFILE_DRIVING = "mapbox.driving";

  /**
   * Matches trace to pedestrian and hiking routing.
   *
   * @since 2.0.0
   */
  public static final String PROFILE_WALKING = "mapbox.walking";

  /**
   * Matches trace to bicycle routing.
   *
   * @since 2.0.0
   */
  public static final String PROFILE_CYCLING = "mapbox.cycling";

  /**
   * The geometry returned will be in the GeoJSON format.
   *
   * @since 2.0.0
   */
  public static final String GEOMETRY_GEOJSON = "geojson";

  /**
   * The geometry returned will be an encoded polyline with precision 6.
   *
   * @since 2.0.0
   */
  public static final String GEOMETRY_POLYLINE = "polyline";

  /**
   * Use false to omit geometry from response.
   *
   * @since 2.0.0
   */
  public static final String GEOMETRY_FALSE = "false";

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