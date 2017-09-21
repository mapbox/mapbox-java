package com.mapbox.services.api.directions.v5.models;

import com.google.gson.annotations.SerializedName;
import com.mapbox.services.commons.models.Position;

import java.util.Arrays;

/**
 * Gives maneuver information about one {@link LegStep}.
 *
 * @since 1.0.0
 */
public class StepManeuver {

  private double[] location;
  @SerializedName("bearing_before")
  private double bearingBefore;
  @SerializedName("bearing_after")
  private double bearingAfter;
  private String type;
  private String modifier;
  private String instruction;
  private Integer exit;

  /**
   * Empty constructor.
   *
   * @since 2.0.0
   */
  public StepManeuver() {
  }

  /**
   * Constructor taking in a {@code type}, {@code modifier}, and an integer {@code exit} value.
   *
   * @param type     indicates the type of maneuver. It can be any of these listed:
   * @param modifier indicates the mode of the maneuver.
   * @param exit     integer indicating number of the exit to take.
   * @since 2.0.0
   */
  public StepManeuver(String type, String modifier, Integer exit) {
    this.type = type;
    this.modifier = modifier;
    this.exit = exit;
  }

  /**
   * an array with two double values reresenting the maneuver locations coordinate.
   *
   * @return double array of [longitude, latitude] for the snapped coordinate.
   * @since 1.0.0
   */
  public double[] getLocation() {
    return location;
  }

  /**
   * Sets double array of [longitude, latitude] for the snapped coordinate.
   *
   * @param location array with the order [longitude, latitude].
   * @since 2.1.0
   */
  public void setLocation(double[] location) {
    this.location = location;
  }

  /**
   * Number between 0 and 360 indicating the clockwise angle from true north to the direction of
   * travel right before the maneuver.
   *
   * @return double with value from 0 to 360.
   * @since 1.0.0
   */
  public double getBearingBefore() {
    return bearingBefore;
  }

  /**
   * Number between 0 and 360 indicating the clockwise angle from true north to the direction of
   * travel right before the maneuver.
   *
   * @param bearingBefore a double with value from 0 to 360 representing the bearing before the maneuver.
   * @since 2.1.0
   */
  public void setBearingBefore(double bearingBefore) {
    this.bearingBefore = bearingBefore;
  }

  /**
   * Number between 0 and 360 indicating the clockwise angle from true north to the direction of
   * travel right after the maneuver.
   *
   * @return double with value from 0 to 360.
   * @since 1.0.0
   */
  public double getBearingAfter() {
    return bearingAfter;
  }

  /**
   * Number between 0 and 360 indicating the clockwise angle from true north to the direction of
   * travel right after the maneuver.
   *
   * @param bearingAfter a double with value from 0 to 360 representing the bearing after the maneuver.
   * @since 2.1.0
   */
  public void setBearingAfter(double bearingAfter) {
    this.bearingAfter = bearingAfter;
  }

  /**
   * This indicates the type of maneuver. It can be any of these listed:
   * <br>
   * <ul>
   * <li>turn - a basic turn into direction of the modifier</li>
   * <li>new name - the road name changes (after a mandatory turn)</li>
   * <li>depart - indicates departure from a leg</li>
   * <li>arrive - indicates arrival to a destination of a leg</li>
   * <li>merge - merge onto a street</li>
   * <li>ramp - take a ramp</li>
   * <li>fork</li>
   * <li>end of road - road ends in a T intersection</li>
   * <li>continue - continue on a street after a turn</li>
   * <li>roundabout - traverse roundabout</li>
   * </ul>
   *
   * @return String with type of maneuver.
   * @since 1.0.0
   */
  public String getType() {
    return type;
  }

  /**
   * This indicates the type of maneuver. See the full list of types in {@link StepManeuver#getType()}.
   *
   * @param type a String with type of maneuver.
   * @since 2.1.0
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * An optional integer indicating number of the exit to take.
   * If exit is undefined the destination is on the roundabout.
   * The property exists for the following type properties:
   * <p>
   * else - indicates the number of intersections passed until the turn.
   * roundabout - traverse roundabout
   * rotary - a traffic circle
   *
   * @return String of an integer indicating number of the exit to take.
   * @since 2.0.0
   */
  public Integer getExit() {
    return exit;
  }

  /**
   * An optional integer indicating number of the exit to take.
   * If exit is undefined the destination is on the roundabout.
   * The property exists for the following type properties:
   * <p>
   * else - indicates the number of intersections passed until the turn.
   * roundabout - traverse roundabout
   * rotary - a traffic circle
   * <p>
   *
   * @param exit a String of an integer indicating number of the exit to take.
   * @since 2.1.0
   */
  public void setExit(Integer exit) {
    this.exit = exit;
  }

  /**
   * This indicates the mode of the maneuver.  If type is of turn, the modifier indicates the
   * change in direction accomplished through the turn. If the type is of depart/arrive, the
   * modifier indicates the position of waypoint from the current direction of travel.
   *
   * @return String with modifier.
   * @since 1.0.0
   */
  public String getModifier() {
    return modifier;
  }


  /**
   * This indicates the mode of the maneuver.  If type is of turn, the modifier indicates the
   * change in direction accomplished through the turn. If the type is of depart/arrive, the
   * modifier indicates the position of waypoint from the current direction of travel.
   *
   * @param modifier a String with the modifier.
   * @since 2.1.0
   */
  public void setModifier(String modifier) {
    this.modifier = modifier;
  }

  /**
   * A human-readable instruction of how to execute the returned maneuver.
   *
   * @return String with instruction.
   * @since 1.0.0
   */
  public String getInstruction() {
    return instruction;
  }

  /**
   * A human-readable instruction of how to execute the returned maneuver.
   *
   * @param instruction a String with instruction representing how to execute the maneuver.
   * @since 2.1.0
   */
  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }

  /**
   * Converts double array {@link #getLocation()} to a {@link Position}. You'll typically want to
   * use this format instead of {@link #getLocation()} as it's easier to work with.
   *
   * @return {@link Position}.
   * @since 1.0.0
   */
  public Position asPosition() {
    return Position.fromCoordinates(location[0], location[1]);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    StepManeuver that = (StepManeuver) o;

    if (Double.compare(that.getBearingBefore(), getBearingBefore()) != 0) {
      return false;
    }
    if (Double.compare(that.getBearingAfter(), getBearingAfter()) != 0) {
      return false;
    }
    if (!Arrays.equals(getLocation(), that.getLocation())) {
      return false;
    }
    if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) {
      return false;
    }
    if (getModifier() != null ? !getModifier().equals(that.getModifier()) : that.getModifier() != null) {
      return false;
    }
    if (getInstruction() != null ? !getInstruction().equals(that.getInstruction()) : that.getInstruction() != null) {
      return false;
    }
    return getExit() != null ? getExit().equals(that.getExit()) : that.getExit() == null;

  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + Arrays.hashCode(getLocation());
    result = 31 * result + Double.valueOf(getBearingBefore()).hashCode();
    result = 31 * result + Double.valueOf(getBearingAfter()).hashCode();
    result = 31 * result + (getType() != null ? getType().hashCode() : 0);
    result = 31 * result + (getModifier() != null ? getModifier().hashCode() : 0);
    result = 31 * result + (getInstruction() != null ? getInstruction().hashCode() : 0);
    result = 31 * result + (getExit() != null ? getExit().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "StepManeuver{"
      + "location=" + Arrays.toString(location)
      + ", bearingBefore=" + bearingBefore
      + ", bearingAfter=" + bearingAfter
      + ", type='" + type + '\''
      + ", modifier='" + modifier + '\''
      + ", instruction='" + instruction + '\''
      + ", exit=" + exit
      + '}';
  }
}
