package com.mapbox.services.api.navigation.v5;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.api.directions.v5.models.LegStep;
import com.mapbox.services.api.directions.v5.models.RouteLeg;
import com.mapbox.services.api.utils.turf.TurfConstants;
import com.mapbox.services.api.utils.turf.TurfException;
import com.mapbox.services.api.utils.turf.TurfMeasurement;
import com.mapbox.services.api.utils.turf.TurfMisc;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.PolylineUtils;

import java.util.List;
import java.util.Locale;

import static com.mapbox.services.Constants.PRECISION_6;

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
   * @param position  you want to verify is on or near the routeLeg step. If using for navigation,
   *                  this would typically be the users current location.
   * @param routeLeg  a directions routeLeg.
   * @param stepIndex integer index for step in routeLeg.
   * @return true if the position is outside the OffRoute threshold.
   * @throws ServicesException if error occurs Mapbox API related.
   * @throws TurfException     signals that a Turf exception of some sort has occurred.
   * @since 1.3.0
   */
  public boolean isInStep(Position position, RouteLeg routeLeg, int stepIndex) throws ServicesException, TurfException {
    double distance = getDistanceToStep(position, routeLeg, stepIndex);
    return (distance <= offRouteThresholdKm);
  }

  /**
   * Computes the distance between the position and the closest point in routeLeg step.
   *
   * @param position  you want to measure distance to from route. If using for navigation, this
   *                  would typically be the users current location.
   * @param routeLeg  a directions routeLeg.
   * @param stepIndex integer index for step in routeLeg.
   * @return double value giving distance in kilometers.
   * @throws ServicesException if error occurs Mapbox API related.
   * @throws TurfException     signals that a Turf exception of some sort has occurred.
   * @since 1.3.0
   */
  public double getDistanceToStep(Position position, RouteLeg routeLeg, int stepIndex) throws ServicesException,
    TurfException {
    Position closestPoint = getSnapToRoute(position, routeLeg, stepIndex);
    return TurfMeasurement.distance(
      Point.fromCoordinates(position),
      Point.fromCoordinates(closestPoint),
      TurfConstants.UNIT_DEFAULT
    );
  }

  /**
   * Measures the distance from a position to the end of the route step. The position provided is snapped to the route
   * before distance is calculated.
   *
   * @param position  you want to measure distance to from route. If using for navigation, this would typically be the
   *                  users current location.
   * @param routeLeg  a directions route.
   * @param stepIndex integer index for step in route.
   * @return double value giving distance in kilometers.
   * @throws ServicesException if error occurs Mapbox API related.
   * @throws TurfException     signals that a Turf exception of some sort has occurred.
   * @since 2.0.0
   */
  public static double getDistanceToNextStep(Position position, RouteLeg routeLeg, int stepIndex)
    throws ServicesException,
    TurfException {
    return getDistanceToNextStep(position, routeLeg, stepIndex, TurfConstants.UNIT_DEFAULT);
  }

  /**
   * Measures the distance from a position to the end of the route step. The position provided is snapped to the route
   * before distance is calculated.
   *
   * @param position  you want to measure distance to from route. If using for navigation, this would typically be the
   *                  users current location.
   * @param routeLeg  a directions route.
   * @param stepIndex integer index for step in route.
   * @return double value giving distance in kilometers.
   * @throws ServicesException if error occurs Mapbox API related.
   * @throws TurfException     signals that a Turf exception of some sort has occurred.
   * @since 2.0.0
   */
  public static double getDistanceToNextStep(Position position, RouteLeg routeLeg, int stepIndex, String units)
    throws ServicesException,
    TurfException {
    LegStep step = validateStep(routeLeg, stepIndex);

    // Decode the geometry
    List<Position> coords = PolylineUtils.decode(step.getGeometry(), PRECISION_6);

    LineString slicedLine = TurfMisc.lineSlice(
      Point.fromCoordinates(position),
      Point.fromCoordinates(coords.get(coords.size() - 1)),
      LineString.fromCoordinates(coords)
    );
    return TurfMeasurement.lineDistance(slicedLine, units);
  }

  /**
   * @param position you want to measure distance to from route. If using for navigation, this would typically be the
   *                 users current location.
   * @param route    a {@link DirectionsRoute}.
   * @return double value giving distance in kilometers.
   * @throws TurfException signals that a Turf exception of some sort has occurred.
   * @since 2.0.0
   */
  public static double getDistanceToEndOfRoute(Position position, DirectionsRoute route) throws TurfException {
    // Decode the geometry
    List<Position> coords = PolylineUtils.decode(route.getGeometry(), PRECISION_6);

    LineString slicedLine = TurfMisc.lineSlice(
      Point.fromCoordinates(position),
      Point.fromCoordinates(coords.get(coords.size() - 1)),
      LineString.fromCoordinates(coords)
    );
    return TurfMeasurement.lineDistance(slicedLine, TurfConstants.UNIT_DEFAULT);
  }

  /**
   * Snaps given position to a {@link RouteLeg} step.
   *
   * @param position  that you want to snap to routeLeg. If using for navigation, this would
   *                  typically be the users current location.
   * @param routeLeg  that you want to snap position to.
   * @param stepIndex integer index for step in routeLeg.
   * @return your position snapped to the route.
   * @throws ServicesException if error occurs Mapbox API related.
   * @throws TurfException     signals that a Turf exception of some sort has occurred.
   * @since 1.3.0
   */
  public static Position getSnapToRoute(Position position, RouteLeg routeLeg, int stepIndex)
    throws ServicesException, TurfException {
    LegStep step = validateStep(routeLeg, stepIndex);

    // Decode the geometry
    List<Position> coords = PolylineUtils.decode(step.getGeometry(), PRECISION_6);

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
   * Method to check whether the position given is outside the routeLeg using the threshold.
   *
   * @param position you want to verify is on or near the routeLeg. If using for navigation, this
   *                 would typically be the users current location.
   * @param routeLeg a directions routeLeg.
   * @return true if the position is beyond the threshold limit from the routeLeg.
   * @throws ServicesException if error occurs Mapbox API related.
   * @throws TurfException     signals that a Turf exception of some sort has occurred.
   * @since 1.3.0
   */
  public boolean isOffRoute(Position position, RouteLeg routeLeg) throws ServicesException, TurfException {
    for (int stepIndex = 0; stepIndex < routeLeg.getSteps().size(); stepIndex++) {
      if (isInStep(position, routeLeg, stepIndex)) {
        // We aren't off-routeLeg if we're close to at least one routeLeg step
        return false;
      }
    }

    return true;
  }

  /**
   * Get the closest routeLeg step to the given position. Ties will go to the furthest step in the
   * routeLeg.
   *
   * @param position that you want to get closest routeLeg step to.
   * @param routeLeg a directions routeLeg.
   * @return integer step index in routeLeg.
   * @throws ServicesException if error occurs Mapbox API related.
   * @throws TurfException     signals that a Turf exception of some sort has occurred.
   * @since 1.3.0
   */
  public int getClosestStep(Position position, RouteLeg routeLeg) throws TurfException, ServicesException {
    double minDistance = Double.MAX_VALUE;
    int closestIndex = 0;

    double distance;
    for (int stepIndex = 0; stepIndex < routeLeg.getSteps().size(); stepIndex++) {
      distance = getDistanceToStep(position, routeLeg, stepIndex);
      if (distance <= minDistance) {
        minDistance = distance;
        closestIndex = stepIndex;
      }
    }

    return closestIndex;
  }

  /**
   * Get the remaining route geometry from the position provided to the end of the directions route. This is useful
   * when the user location is traversing along the route and you don't want the past route geometry to show on the map.
   *
   * @param position the new starting postion of the route geometry. If using for navigation, this would typically be
   *                 the users current location.
   * @param route    a Directions route.
   * @return the {@link LineString} representing the new route geometry.
   * @throws TurfException signals that a Turf exception of some sort has occurred.
   * @since 2.0.0
   */
  public static LineString getGeometryRemainingOnRoute(Position position, DirectionsRoute route) throws TurfException {
    int lastLegIndex = route.getLegs().size() - 1;
    int lastStepIndex = route.getLegs().get(lastLegIndex).getSteps().size() - 1;

    String polyline = route.getLegs().get(lastLegIndex).getSteps().get(lastStepIndex).getGeometry();
    LineString lastStepInLastLegLineString = LineString.fromPolyline(polyline, PRECISION_6);
    int lastStepLastIndex = lastStepInLastLegLineString.getCoordinates().size() - 1;
    Position lastStepInLastLegPostion = lastStepInLastLegLineString.getCoordinates().get(lastStepLastIndex);

    return TurfMisc.lineSlice(
      Point.fromCoordinates(position),
      Point.fromCoordinates(lastStepInLastLegPostion),
      LineString.fromPolyline(route.getGeometry(), PRECISION_6)
    );
  }

  private static LegStep validateStep(RouteLeg route, int stepIndex) throws ServicesException {
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
