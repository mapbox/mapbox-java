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
  @Experimental public static final String STEP_MANEUVER_TYPE_EXIT_ROUNDABOUT = "exit roundabout";
  @Experimental public static final String STEP_MANEUVER_TYPE_EXIT_ROTARY = "exit rotary";
  @Experimental public static final String STEP_MANEUVER_TYPE_NOTIFICATION = "notification";
}

