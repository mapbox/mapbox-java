package com.mapbox.services.api.directions.v4.models;

/**
 * Used to describe a instruction step maneuver.
 *
 * @since 1.0.0
 * @deprecated Use Directions v5 instead
 */
@Deprecated
public class StepManeuver {

  private String type;
  private ManeuverPoint location;
  private String instruction;
  private String mode;

  /**
   * A specific string describing the type of maneuver. The following types are currently used:
   * continue, bear right, turn right, sharp right, u-turn, sharp left, turn left, bear left,
   * waypoint, depart, enter roundabout, and arrive. Warning, new maneuver types may be introduced
   * in the future however, so <a href="https://www.mapbox.com/developers/api/directions/">check here</a>
   * to ensure you don't miss any.
   *
   * @return string with possibly one of the maneuvers given above.
   * @since 1.0.0
   * @deprecated Use Directions v5 instead
   */
  @Deprecated
  public String getType() {
    return type;
  }

  /**
   * @param type string with possibly one of the maneuvers given above.
   * @since 1.0.0
   * @deprecated Use Directions v5 instead
   */
  @Deprecated
  public void setType(String type) {
    this.type = type;
  }

  /**
   * {@link ManeuverPoint} object containing type and the coordinate.
   *
   * @return {@link ManeuverPoint} object.
   * @since 1.0.0
   * @deprecated Use Directions v5 instead
   */
  @Deprecated
  public ManeuverPoint getLocation() {
    return location;
  }

  /**
   * @param location {@link ManeuverPoint} object.
   * @since 1.0.0
   * @deprecated Use Directions v5 instead
   */
  @Deprecated
  public void setLocation(ManeuverPoint location) {
    this.location = location;
  }

  /**
   * Human-readable string describing the maneuver. typically combines {@link #getType()} and a
   * street name. if within {@code MapboxDirections.Builder} you setInstructions to HTML, you'll
   * relieve the instructions in HTML format.
   *
   * @return string typically the length of a sentence giving next maneuver instruction.
   * @since 1.0.0
   * @deprecated Use Directions v5 instead
   */
  @Deprecated
  public String getInstruction() {
    return instruction;
  }

  /**
   * @param instruction string typically the length of a sentence giving next maneuver instruction.
   * @since 1.0.0
   * @deprecated Use Directions v5 instead
   */
  @Deprecated
  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }

  /**
   * A string signifying the mode of transportation. Possible values can be:
   * <p>For driving: driving, ferry, movable bridge, unaccessible.
   * <p>For walking: walking, ferry, unaccessible.
   * <p>For cycling: cycling, walking, ferry, train, movable bridge, unaccessible.
   *
   * @return string with possibly one of the values given above.
   * @since 1.0.0
   * @deprecated Use Directions v5 instead
   */
  @Deprecated
  public String getMode() {
    return mode;
  }

  /**
   * @param mode string with possibly one of the values given above.
   * @since 1.0.0
   * @deprecated Use Directions v5 instead
   */
  @Deprecated
  public void setMode(String mode) {
    this.mode = mode;
  }
}
