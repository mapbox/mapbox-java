package com.mapbox.services.api.directions.v5.models;

import com.mapbox.services.commons.models.Position;

import java.util.Arrays;

/**
 * Object representing an intersection along the step.
 *
 * @since 1.3.0
 */
public class StepIntersection {

  private double[] location;
  private int[] bearings;
  private boolean[] entry;
  private int in;
  private int out;
  private IntersectionLanes[] lanes;

  /**
   * Empty constructor
   *
   * @since 2.0.0
   */
  public StepIntersection() {
  }

  /**
   * Constructor allowing the setting of the {@link IntersectionLanes}.
   *
   * @param lanes an Array of {@link IntersectionLanes}.
   * @since 2.0.0
   */
  public StepIntersection(IntersectionLanes[] lanes) {
    this.lanes = lanes;
  }

  /**
   * A double array of length 2, first position being the longitude and the other being latitude.
   *
   * @return A [longitude, latitude] pair describing the location of the turn.
   * @since 1.3.0
   */
  public double[] getLocation() {
    return location;
  }

  /**
   * A double array of length 2, first position being the longitude and the other being latitude.
   *
   * @param location array with the order [longitude, latitude].
   * @since 2.1.0
   */
  public void setLocation(double[] location) {
    this.location = location;
  }


  /**
   * An integer array of bearing values available at the step intersection.
   *
   * @return An array of bearing values (for example [0,90,180,270]) that are available at the
   * intersection. The bearings describe all available roads at the intersection.
   * @since 1.3.0
   */
  public int[] getBearings() {
    return bearings;
  }

  /**
   * An integer array of bearing values available at the step intersection.
   *
   * @param bearings an array of bearing values (for example [0,90,180,270]) that are available at the
   *                 intersection. The bearings describe all available roads at the intersection.
   * @since 2.1.0
   */
  public void setBearings(int[] bearings) {
    this.bearings = bearings;
  }

  /**
   * An array of entry flags, corresponding in a 1:1 relationship to the bearings.
   *
   * @return An array of entry flags, corresponding in a 1:1 relationship to the bearings. A value
   * of true indicates that the respective road could be entered on a valid route. false
   * indicates that the turn onto the respective road would violate a restriction.
   * @since 1.3.0
   */
  public boolean[] getEntry() {
    return entry;
  }

  /**
   * An array of entry flags, corresponding in a 1:1 relationship to the bearings.
   *
   * @param entry an array of entry flags, corresponding in a 1:1 relationship to the bearings. A value
   *              of true indicates that the respective road could be entered on a valid route. false
   *              indicates that the turn onto the respective road would violate a restriction.
   * @since 2.1.0
   */
  public void setEntry(boolean[] entry) {
    this.entry = entry;
  }

  /**
   * Index into bearings/entry array.
   *
   * @return Index into bearings/entry array. Used to calculate the bearing before the turn.
   * Namely, the clockwise angle from true north to the direction of travel before the
   * maneuver/passing the intersection. To get the bearing in the direction of driving,
   * the bearing has to be rotated by a value of 180. The value is not supplied for departure
   * maneuvers.
   * @since 1.3.0
   */
  public int getIn() {
    return in;
  }

  /**
   * Index into bearings/entry array.
   *
   * @param in index into bearings/entry array. Used to calculate the bearing before the turn.
   *           Namely, the clockwise angle from true north to the direction of travel before the
   *           maneuver/passing the intersection. To get the bearing in the direction of driving,
   *           the bearing has to be rotated by a value of 180. The value is not supplied for departure
   *           maneuvers.
   * @since 2.1.0
   */
  public void setIn(int in) {
    this.in = in;
  }

  /**
   * Index out of the bearings/entry array.
   *
   * @return index out of the bearings/entry array. Used to extract the bearing after the turn.
   * Namely, The clockwise angle from true north to the direction of travel after the
   * maneuver/passing the intersection. The value is not supplied for arrive maneuvers.
   * @since 1.3.0
   */
  public int getOut() {
    return out;
  }

  /**
   * Index out of the bearings/entry array.
   *
   * @param out index out of the bearings/entry array. Used to extract the bearing after the turn.
   *            Namely, The clockwise angle from true north to the direction of travel after the
   *            maneuver/passing the intersection. The value is not supplied for arrive maneuvers.
   * @since 2.1.0
   */
  public void setOut(int out) {
    this.out = out;
  }

  /**
   * Converts double array {@link #getLocation()} to a {@link Position}. You'll typically want to
   * use this format instead of {@link #getLocation()} as it's easier to work with.
   *
   * @return {@link Position}.
   * @since 1.3.0
   */
  public Position asPosition() {
    return Position.fromCoordinates(location[0], location[1]);
  }

  /**
   * Array of lane objects that represent the available turn lanes at the intersection.
   *
   * @return array of lane objects that represent the available turn lanes at the intersection. If
   * no lane information is available for an intersection, the lanes property will not be present.
   * Lanes are provided in their order on the street, from left to right.
   * @since 2.0.0
   */
  public IntersectionLanes[] getLanes() {
    return lanes;
  }

  /**
   * Array of lane objects that represent the available turn lanes at the intersection.
   *
   * @param lanes an array of lane objects that represent the available turn lanes at the intersection. If
   *              no lane information is available for an intersection, the lanes property will not be present.
   *              Lanes are provided in their order on the street, from left to right.
   * @since 2.1.0
   */
  public void setLanes(IntersectionLanes[] lanes) {
    this.lanes = lanes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    StepIntersection stepIntersection = (StepIntersection) o;

    if (getLocation() != null ? !Arrays.equals(getLocation(),
      stepIntersection.getLocation()) : stepIntersection.getLocation() != null) {
      return false;
    }
    if (getBearings() != null ? !Arrays.equals(getBearings(),
      stepIntersection.getBearings()) : stepIntersection.getBearings() != null) {
      return false;
    }
    if (getEntry() != null ? !Arrays.equals(getEntry(),
      stepIntersection.getEntry()) : stepIntersection.getEntry() != null) {
      return false;
    }
    if (getIn() != stepIntersection.getIn()) {
      return false;
    }
    if (getOut() != stepIntersection.getOut()) {
      return false;
    }
    return !(getLanes() != null ? !Arrays.equals(getLanes(),
      stepIntersection.getLanes()) : stepIntersection.getLanes() != null);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (getLocation() != null ? Arrays.hashCode(getLocation()) : 0);
    result = 31 * result + (getBearings() != null ? Arrays.hashCode(getBearings()) : 0);
    result = 31 * result + (getEntry() != null ? Arrays.hashCode(getEntry()) : 0);
    result = 31 * result + getIn();
    result = 31 * result + getOut();
    result = 31 * result + (getLanes() != null ? Arrays.hashCode(getLanes()) : 0);
    return result;
  }

  @Override
  public String toString() {
    return "StepIntersection{"
      + "location=" + Arrays.toString(location)
      + ", bearings=" + Arrays.toString(bearings)
      + ", entry=" + Arrays.toString(entry)
      + ", in=" + in
      + ", out=" + out
      + ", lanes=" + Arrays.toString(lanes)
      + '}';
  }
}
