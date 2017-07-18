package com.mapbox.services.api.optimizedtrips.v1.models;

import com.google.gson.annotations.SerializedName;
import com.mapbox.services.api.directions.v5.models.DirectionsWaypoint;

public class OptimizationWaypoint extends DirectionsWaypoint {

  @SerializedName("waypoint_index")
  private int waypointIndex;
  @SerializedName("trips_index")
  private int tripsIndex;

  /**
   * Empty constructor
   *
   * @since 2.2.0
   */
  public OptimizationWaypoint() {
  }

  /**
   * Construct an OptimizationWaypoint object.
   *
   * @param waypointIndex index of position of the waypoint within the trip
   * @param tripsIndex    index to the trip object in the trips array that contains this waypoint
   * @since 2.2.0
   */
  public OptimizationWaypoint(int waypointIndex, int tripsIndex) {
    this.waypointIndex = waypointIndex;
    this.tripsIndex = tripsIndex;
  }

  /**
   * Index of the waypoint inside the optimization route.
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
   * @since 2.2.0
   */
  public void setWaypointIndex(int waypointIndex) {
    this.waypointIndex = waypointIndex;
  }

  /**
   * Index to the trip object in the trips array that contains this waypoint.
   *
   * @return index value
   * @since 2.2.0
   */
  public int getTripsIndex() {
    return tripsIndex;
  }

  /**
   * Index to the trip object in the trips array that contains this waypoint.
   *
   * @param tripsIndex index value
   * @since 2.2.0
   */
  public void setTripsIndex(int tripsIndex) {
    this.tripsIndex = tripsIndex;
  }
}
