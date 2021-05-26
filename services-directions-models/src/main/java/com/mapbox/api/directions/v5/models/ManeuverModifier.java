package com.mapbox.api.directions.v5.models;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Constants for the {@link StepManeuver#modifier()}.
 *
 * @since 5.2.0
 */
public final class ManeuverModifier {

  /**
   * Indicates "uturn" maneuver modifier.
   *
   * @since 5.2.0
   */
  public static final String UTURN = "uturn";

  /**
   * Indicates "sharp right" maneuver modifier.
   *
   * @since 5.2.0
   */
  public static final String SHARP_RIGHT = "sharp right";

  /**
   * Indicates "right" maneuver modifier.
   *
   * @since 5.2.0
   */
  public static final String RIGHT = "right";

  /**
   * Indicates "slight right" maneuver modifier.
   *
   * @since 5.2.0
   */
  public static final String SLIGHT_RIGHT = "slight right";

  /**
   * Indicates "straight" maneuver modifier.
   *
   * @since 5.2.0
   */
  public static final String STRAIGHT = "straight";

  /**
   * Indicates "slight left" maneuver modifier.
   *
   * @since 5.2.0
   */
  public static final String SLIGHT_LEFT = "slight left";

  /**
   * Indicates "left" maneuver modifier.
   *
   * @since 5.2.0
   */
  public static final String LEFT = "left";

  /**
   * Indicates "sharp left" maneuver modifier.
   *
   * @since 5.2.0
   */
  public static final String SHARP_LEFT = "sharp left";

  /**
   * Representation of ManeuverModifier in form of logical types.
   *
   * @since 5.2.1
   */
  @Retention(RetentionPolicy.CLASS)
  @StringDef({
          ManeuverModifier.UTURN,
          ManeuverModifier.SHARP_RIGHT,
          ManeuverModifier.RIGHT,
          ManeuverModifier.SLIGHT_RIGHT,
          ManeuverModifier.STRAIGHT,
          ManeuverModifier.SLIGHT_LEFT,
          ManeuverModifier.LEFT,
          ManeuverModifier.SHARP_LEFT
  })
  public @interface Type {
  }
}
