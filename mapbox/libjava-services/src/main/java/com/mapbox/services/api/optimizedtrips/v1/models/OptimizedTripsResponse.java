package com.mapbox.services.api.optimizedtrips.v1.models;

import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.api.directions.v5.models.DirectionsWaypoint;

import java.util.List;

public class OptimizedTripsResponse {

  private String code;
  private List<DirectionsWaypoint> waypoints;
  private List<DirectionsRoute> trips;

  public OptimizedTripsResponse() {
  }

  public OptimizedTripsResponse(List<DirectionsRoute> trips, List<DirectionsWaypoint> waypoints) {
    this.trips = trips;
    this.waypoints = waypoints;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public List<DirectionsWaypoint> getWaypoints() {
    return waypoints;
  }

  public void setWaypoints(List<DirectionsWaypoint> waypoints) {
    this.waypoints = waypoints;
  }

  public List<DirectionsRoute> getTrips() {
    return trips;
  }

  public void setTrips(List<DirectionsRoute> trips) {
    this.trips = trips;
  }
}
