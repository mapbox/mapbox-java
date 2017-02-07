package com.mapbox.services.api.mapmatching.v5.models;

import com.google.gson.annotations.SerializedName;
import com.mapbox.services.api.directions.v5.models.DirectionsWaypoint;

/**
 * A tracepoint object is {@link DirectionsWaypoint} object with two additional fields.
 */
public class MapMatchingTracepoint extends DirectionsWaypoint {

  @SerializedName("matchings_index")
  private int matchingsIndex;

  @SerializedName("waypoint_index")
  private int waypointIndex;

  /**
   * Index to the match object in matchings the sub-trace was matched to.
   *
   * @return index value
   */
  public int getMatchingsIndex() {
    return matchingsIndex;
  }

  /**
   * Index value
   *
   * @param matchingsIndex value
   */
  public void setMatchingsIndex(int matchingsIndex) {
    this.matchingsIndex = matchingsIndex;
  }

  /**
   * Index of the waypoint inside the matched route.
   *
   * @return index value
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
}
