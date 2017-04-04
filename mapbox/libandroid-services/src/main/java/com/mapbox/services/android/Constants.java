package com.mapbox.services.android;

import com.mapbox.services.Experimental;

/**
 * Namespace to avoid constants collision.
 *
 * This is partly an experimental API. Experimental APIs are quickly evolving and
 * might change or be removed in minor versions.
 */
public class Constants {

  private static final String PACKAGE_NAME = "com.mapbox.services.android";

  /*
   * Direction constants
   */

  @Experimental public static final String STEP_MANEUVER_TYPE_TURN = "turn";
  @Experimental public static final String STEP_MANEUVER_TYPE_NEW_NAME = "new name";
  @Experimental public static final String STEP_MANEUVER_TYPE_DEPART = "depart";
  @Experimental public static final String STEP_MANEUVER_TYPE_ARRIVE = "arrive";
  @Experimental public static final String STEP_MANEUVER_TYPE_MERGE = "merge";
  @Experimental public static final String STEP_MANEUVER_TYPE_ON_RAMP = "on ramp";
  @Experimental public static final String STEP_MANEUVER_TYPE_OFF_RAMP = "off ramp";
  @Experimental public static final String STEP_MANEUVER_TYPE_FORK = "fork";
  @Experimental public static final String STEP_MANEUVER_TYPE_END_OF_ROAD = "end of road";
  @Experimental public static final String STEP_MANEUVER_TYPE_USE_LANE = "use lane";
  @Experimental public static final String STEP_MANEUVER_TYPE_CONTINUE = "continue";
  @Experimental public static final String STEP_MANEUVER_TYPE_ROUNDABOUT = "roundabout";
  @Experimental public static final String STEP_MANEUVER_TYPE_ROTARY = "rotary";
  @Experimental public static final String STEP_MANEUVER_TYPE_ROUNDABOUT_TURN = "roundabout turn";
  @Experimental public static final String STEP_MANEUVER_TYPE_NOTIFICATION = "notification";

  /*
   * Navigation constants
   */

  @Experimental public static final int NONE_ALERT_LEVEL = 0;
  @Experimental public static final int DEPART_ALERT_LEVEL = 1;
  @Experimental public static final int LOW_ALERT_LEVEL = 2;
  @Experimental public static final int MEDIUM_ALERT_LEVEL = 3;
  @Experimental public static final int HIGH_ALERT_LEVEL = 4;
  @Experimental public static final int ARRIVE_ALERT_LEVEL = 5;
  @Experimental public static final int METERS_TO_INTERSECTION = 50;
  @Experimental public static final int DEFAULT_ANGLE_TOLERANCE = 45;

  /**
   * Threshold user must be in within to count as completing a step. One of two heuristics used to know when a user
   * completes a step, see `RouteControllerManeuverZoneRadius`. The users `heading` and the `finalHeading` are
   * compared. If this number is within `RouteControllerMaximumAllowedDegreeOffsetForTurnCompletion`, the user has
   * completed the step.
   */
  @Experimental public static final int MAXIMUM_ALLOWED_DEGREE_OFFSET_FOR_TURN_COMPLETION = 30;

  /**
   * Radius in meters the user must enter to count as completing a step. One of two heuristics used to know when a user
   * completes a step, see `RouteControllerMaximumAllowedDegreeOffsetForTurnCompletion`.
   */
  @Experimental public static final int MANEUVER_ZONE_RADIUS = 40;

  /**
   * Number of seconds left on step when a `medium` alert is emitted.
   */
  @Experimental public static final int MEDIUM_ALERT_INTERVAL = 70;

  /**
   * Number of seconds left on step when a `high` alert is emitted.
   */
  @Experimental public static final int HIGH_ALERT_INTERVAL = 15;

  /**
   * Distance in meters for the minimum length of a step for giving a `high` alert.
   */
  @Experimental public static final int MINIMUM_DISTANCE_FOR_HIGH_ALERT = 100;

  /**
   * Distance in meters for the minimum length of a step for giving a `medium` alert.
   */
  @Experimental public static final int MINIMUM_DISTANCE_FOR_MEDIUM_ALERT = 400;

  /**
   * Maximum number of meters the user can travel away from step before the
   * {@link com.mapbox.services.android.navigation.v5.listeners.OffRouteListener}'s called.
   */
  @Experimental public static final double MAXIMUM_DISTANCE_BEFORE_OFF_ROUTE = 50;

  /**
   * When calculating whether or not the user is on the route, we look where the user will be given their speed and
   * this variable.
   */
  @Experimental public static final double DEAD_RECKONING_TIME_INTERVAL = 1.0;

}

