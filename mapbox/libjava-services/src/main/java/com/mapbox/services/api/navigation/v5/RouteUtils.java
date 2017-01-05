package com.mapbox.services.api.navigation.v5;

import com.mapbox.services.Constants;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.api.utils.turf.TurfConstants;
import com.mapbox.services.api.utils.turf.TurfException;
import com.mapbox.services.api.utils.turf.TurfMeasurement;
import com.mapbox.services.api.utils.turf.TurfMisc;
import com.mapbox.services.commons.utils.PolylineUtils;
import com.mapbox.services.api.directions.v5.models.LegStep;
import com.mapbox.services.api.directions.v5.models.RouteLeg;

import java.util.List;
import java.util.Locale;

/**
 * A few utilities to work with RouteLeg objects.
 *
 * @since 1.3.0
 */
public class RouteUtils {

  // Default threshold for the user to be considered to be off-route (100 meters)
  public static final double DEFAULT_OFF_ROUTE_THRESHOLD_KM = 0.1;

  private double offRouteThresholdKm;

  /**
   * RouteUtils constructor using default threshold of 100 meters.
   *
   * @since 1.3.0
   */
  public RouteUtils() {
    this.offRouteThresholdKm = DEFAULT_OFF_ROUTE_THRESHOLD_KM;
  }

  /**
   * RouteUtils constructor allowing you to pass in a threshold value.
   *
   * @param offRouteThresholdKm Double value using unit kilometers. This value determines the
   *                            distance till you are notified.
   * @since 1.3.0
   */
  public RouteUtils(double offRouteThresholdKm) {
    this.offRouteThresholdKm = offRouteThresholdKm;
  }

  /**
   * @return the RouteUtils threshold as a double value; defaults 100 meters.
   * @since 1.3.0
   */
  public double getOffRouteThresholdKm() {
    return offRouteThresholdKm;
  }

  /**
   * Compute the distance between the position and the step line (the closest point), and checks
   * if it's within the off-route threshold.
   *
   * @param position  you want to verify is on or near the route step. If using for navigation,
   *                  this would typically be the users current location.
   * @param route     a directions route.
   * @param stepIndex integer index for step in route.
   * @return true if the position is outside the OffRoute threshold.
   * @throws ServicesException if error occurs Mapbox API related.
   * @throws TurfException     signals that a Turf exception of some sort has occurred.
   * @since 1.3.0
   */
  public boolean isInStep(Position position, RouteLeg route, int stepIndex) throws ServicesException, TurfException {
    double distance = getDistanceToStep(position, route, stepIndex);
    return (distance <= offRouteThresholdKm);
  }

  /**
   * Computes the distance between the position and the closest point in route step.
   *
   * @param position  you want to measure distance to from route. If using for navigation, this
   *                  would typically be the users current location.
   * @param route     a directions route.
   * @param stepIndex integer index for step in route.
   * @return double value giving distance in kilometers.
   * @throws ServicesException if error occurs Mapbox API related.
   * @throws TurfException     signals that a Turf exception of some sort has occurred.
   * @since 1.3.0
   */
  public double getDistanceToStep(Position position, RouteLeg route, int stepIndex) throws ServicesException,
    TurfException {
    Position closestPoint = getSnapToRoute(position, route, stepIndex);
    return TurfMeasurement.distance(
      Point.fromCoordinates(position),
      Point.fromCoordinates(closestPoint),
      TurfConstants.UNIT_DEFAULT
    );
  }

  /**
   * Snaps given position to a {@link RouteLeg} step.
   *
   * @param position  that you want to snap to route. If using for navigation, this would
   *                  typically be the users current location.
   * @param route     that you want to snap position to.
   * @param stepIndex integer index for step in route.
   * @return your position snapped to the route.
   * @throws ServicesException if error occurs Mapbox API related.
   * @throws TurfException     signals that a Turf exception of some sort has occurred.
   * @since 1.3.0
   */
  public Position getSnapToRoute(Position position, RouteLeg route, int stepIndex) throws ServicesException, TurfException {
    LegStep step = validateStep(route, stepIndex);

    // Decode the geometry
    List<Position> coords = PolylineUtils.decode(step.getGeometry(), Constants.OSRM_PRECISION_V5);

    // No need to do the math if the step has one coordinate only
    if (coords.size() == 1) {
      return coords.get(0);
    }

    // Uses Turf's pointOnLine, which takes a Point and a LineString to calculate the closest
    // Point on the LineString.
    Feature point = TurfMisc.pointOnLine(Point.fromCoordinates(position), coords);
    return ((Point) point.getGeometry()).getCoordinates();
  }

  /**
   * Method to check whether the position given is outside the route using the threshold.
   *
   * @param position you want to verify is on or near the route. If using for navigation, this
   *                 would typically be the users current location.
   * @param route    a directions route.
   * @return true if the position is beyond the threshold limit from the route.
   * @throws ServicesException if error occurs Mapbox API related.
   * @throws TurfException     signals that a Turf exception of some sort has occurred.
   * @since 1.3.0
   */
  public boolean isOffRoute(Position position, RouteLeg route) throws ServicesException, TurfException {
    for (int stepIndex = 0; stepIndex < route.getSteps().size(); stepIndex++) {
      if (isInStep(position, route, stepIndex)) {
        // We aren't off-route if we're close to at least one route step
        return false;
      }
    }

    return true;
  }

  /**
   * Get the closest route step to the given position. Ties will go to the furthest step in the
   * route leg.
   *
   * @param position that you want to get closest route step to.
   * @param route    a directions route.
   * @return integer step index in route leg.
   * @throws ServicesException if error occurs Mapbox API related.
   * @throws TurfException     signals that a Turf exception of some sort has occurred.
   * @since 1.3.0
   */
  public int getClosestStep(Position position, RouteLeg route) throws ServicesException, TurfException {
    double minDistance = Double.MAX_VALUE;
    int closestIndex = 0;

    double distance;
    for (int stepIndex = 0; stepIndex < route.getSteps().size(); stepIndex++) {
      distance = getDistanceToStep(position, route, stepIndex);
      if (distance <= minDistance) {
        minDistance = distance;
        closestIndex = stepIndex;
      }
    }

    return closestIndex;
  }

  private LegStep validateStep(RouteLeg route, int stepIndex) throws ServicesException {
    if (route == null) {
      throw new ServicesException("The provided route is empty.");
    } else if (route.getSteps() == null || route.getSteps().size() == 0) {
      throw new ServicesException("The provided route has an empty set of steps.");
    } else if (stepIndex >= route.getSteps().size()) {
      throw new ServicesException(String.format(
        Locale.US, "The provided route doesn't have so many steps (%d).", stepIndex));
    }

    return route.getSteps().get(stepIndex);
  }
}
