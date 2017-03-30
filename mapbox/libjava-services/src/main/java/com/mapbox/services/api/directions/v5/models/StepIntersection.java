package com.mapbox.services.api.directions.v5.models;

import com.mapbox.services.commons.models.Position;

/**
 * Object representing an intersection along the step.
 */
public class StepIntersection {

  private double[] location;
  private int[] bearings;
  private boolean[] entry;
  private int in;
  private int out;
  private IntersectionLanes[] lanes;

  public StepIntersection() {
  }

  public StepIntersection(IntersectionLanes[] lanes) {
    this.lanes = lanes;
  }

  /**
   * @return A [longitude, latitude] pair describing the location of the turn.
   * @since 1.3.0
   */
  public double[] getLocation() {
    return location;
  }

  /**
   * Sets A [longitude, latitude] pair describing the location of the turn.
   *
   * @since 2.0.1
   */
  public void setLocation(double[] location) {
    this.location = location;
  }


  /**
   * @return A list of bearing values (for example [0,90,180,270]) that are available at the
   * intersection. The bearings describe all available roads at the intersection.
   * @since 1.3.0
   */
  public int[] getBearings() {
    return bearings;
  }

  /**
   * Sets A list of bearing values (for example [0,90,180,270]) that are available at the
   * intersection. The bearings describe all available roads at the intersection.
   *
   * @since 2.0.1
   */
  public void setBearings(int[] bearings) {
    this.bearings = bearings;
  }

  /**
   * @return A list of entry flags, corresponding in a 1:1 relationship to the bearings. A value
   * of true indicates that the respective road could be entered on a valid route. false
   * indicates that the turn onto the respective road would violate a restriction.
   * @since 1.3.0
   */
  public boolean[] getEntry() {
    return entry;
  }

  /**
   * Sets A list of entry flags, corresponding in a 1:1 relationship to the bearings. A value
   * of true indicates that the respective road could be entered on a valid route. false
   * indicates that the turn onto the respective road would violate a restriction.
   *
   * @since 2.0.1
   */
  public void setEntry(boolean[] entry) {
    this.entry = entry;
  }

  /**
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
   * Sets Index into bearings/entry array. Used to calculate the bearing before the turn.
   * Namely, the clockwise angle from true north to the direction of travel before the
   * maneuver/passing the intersection. To get the bearing in the direction of driving,
   * the bearing has to be rotated by a value of 180. The value is not supplied for departure
   * maneuvers.
   *
   * @since 2.0.1
   */
  public void setIn(int in) {
    this.in = in;
  }

  /**
   * @return Index into the bearings/entry array. Used to extract the bearing after the turn.
   * Namely, The clockwise angle from true north to the direction of travel after the
   * maneuver/passing the intersection. The value is not supplied for arrive maneuvers.
   * @since 1.3.0
   */
  public int getOut() {
    return out;
  }

  /**
   * Sets Index into the bearings/entry array. Used to extract the bearing after the turn.
   * Namely, The clockwise angle from true north to the direction of travel after the
   * maneuver/passing the intersection. The value is not supplied for arrive maneuvers.
   *
   * @since 2.0.1
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
   * @return Array of lane objects that represent the available turn lanes at the intersection. If
   * no lane information is available for an intersection, the lanes property will not be present.
   * Lanes are provided in their order on the street, from left to right.
   * @since 2.0.0
   */
  public IntersectionLanes[] getLanes() {
    return lanes;
  }

  /**
   * Sets Array of lane objects that represent the available turn lanes at the intersection. If
   * no lane information is available for an intersection, the lanes property will not be present.
   * Lanes are provided in their order on the street, from left to right.
   *
   * @since 2.0.1
   */
  public void setLanes(IntersectionLanes[] lanes) {
    this.lanes = lanes;
  }
}
