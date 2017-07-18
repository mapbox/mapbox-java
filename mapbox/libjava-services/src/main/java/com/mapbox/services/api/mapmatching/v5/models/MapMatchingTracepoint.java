package com.mapbox.services.api.mapmatching.v5.models;

import com.google.gson.annotations.SerializedName;
import com.mapbox.services.api.directions.v5.models.DirectionsWaypoint;

/**
 * A tracepoint object is {@link DirectionsWaypoint} object with two additional fields.
 */
public class MapMatchingTracepoint extends DirectionsWaypoint {

  @SerializedName("matchings_index")
  private int matchingsIndex;
  @SerializedName("alternatives_count")
  private int alternativesCount;
  @SerializedName("waypoint_index")
  private int waypointIndex;

  /**
   * Public constructor
   */
  public MapMatchingTracepoint() {
  }

  public MapMatchingTracepoint(int matchingsIndex, int alternativesCount, int waypointIndex) {
    this.matchingsIndex = matchingsIndex;
    this.alternativesCount = alternativesCount;
    this.waypointIndex = waypointIndex;
  }

  /**
   * Index to the match object in matchings the sub-trace was matched to.
   *
   * @return index value
   * @since 2.2.0
   */
  public int getMatchingsIndex() {
    return matchingsIndex;
  }

  /**
   * Index value
   *
   * @param matchingsIndex value
   * @since 2.2.0
   */
  public void setMatchingsIndex(int matchingsIndex) {
    this.matchingsIndex = matchingsIndex;
  }

  /**
   * Index of the waypoint inside the matched route.
   *
   * @return index value
   * @since 2.2.0
   */
  public int getWaypointIndex() {
    return waypointIndex;
  }

  /**
   * Index value
   *
   * @param waypointIndex value
   */
  public void setWaypointIndex(int waypointIndex) {
    this.waypointIndex = waypointIndex;
  }

  /**
   * Number of probable alternative matchings for this trace point. A value of zero indicates that this point was
   * matched unambiguously. Split the trace at these points for incremental map matching.
   *
   * @return an integer representing the alternatives count
   * @since 2.2.0
   */
  public int getAlternativesCount() {
    return alternativesCount;
  }

  /**
   * Set the number of probable alternative matchings for this trace point. A value of zero indicates that this point
   * was matched unambiguously. Split the trace at these points for incremental map matching.
   *
   * @param alternativesCount an integer representing the alternatives count
   * @since 2.2.0
   */
  public void setAlternativesCount(int alternativesCount) {
    this.alternativesCount = alternativesCount;
  }
}
