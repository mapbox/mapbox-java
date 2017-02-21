package com.mapbox.services.api.navigation.v5;

import com.mapbox.services.Constants;
import com.mapbox.services.Experimental;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.api.directions.v5.models.LegStep;
import com.mapbox.services.api.directions.v5.models.RouteLeg;
import com.mapbox.services.api.utils.turf.TurfConstants;
import com.mapbox.services.api.utils.turf.TurfException;
import com.mapbox.services.api.utils.turf.TurfMeasurement;
import com.mapbox.services.commons.geojson.LineString;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code routeProgress} class contains all progress information of user along the route, leg and step.
 * <p>
 * You can use this together with MapboxNavigation to obtain this object from the AlertLevelChangeListener
 * or the ProgressChangeListener. This object will be null from when the NavigationService starts till
 * the first location change occurs. Therefore if you are using this to initialize a variable, it's
 * reasonable to check if null beforehand. Since this object is mutable, the values given can change at anytime.
 * <p>
 * This is an experimental API. Experimental APIs are quickly evolving and
 * might change or be removed in minor versions.
 *
 * @since 2.0.0
 */
@Experimental
public class RouteProgress {
  private final Logger logger = Logger.getLogger(RouteProgress.class.getSimpleName());

  private DirectionsRoute route;

  private int legIndex = 0;
  private int stepIndex;
  private double durationRemainingOnStep;
  private double distanceRemainingOnStep;
  private double distanceTraveled;
  private double userSnapToStepDistanceFromManeuver;
  private int alertUserLevel;

  /*
   * Route progress getters
   */

  public int getPreviousAlertLevel() {
    return alertUserLevel;
  }

  /**
   * Get the current {@code DirectionsRoute} object the user is traversing along during navigation.
   *
   * @return {@link DirectionsRoute} object.
   * @since 2.0.0
   */
  public DirectionsRoute getRoute() {
    return route;
  }

  /**
   * Get the total distance traveled by user along all {@link RouteLeg}s in meters.
   *
   * @since 2.0.0
   */
  public double getDistanceTraveledOnRoute() {
    return distanceTraveled;
  }

  /**
   * Number between 0 and 1 representing how far along the {@link DirectionsRoute} the user has traveled.
   *
   * @since 2.0.0
   */
  public double getFractionTraveledOnRoute() {
    logger.log(Level.FINE, "fraction %f", distanceTraveled / route.getDistance());
    return distanceTraveled / route.getDistance();
  }

  /**
   * Total distance remaining in meters along the {@link DirectionsRoute}.
   *
   * @since 2.0.0
   */
  public double getDistanceRemainingOnRoute() {
    return route.getDistance() - distanceTraveled;
  }

  /*
   * Leg progress getters
   */

  /**
   * Get the current leg index the user is on during navigation.
   *
   * @return integer representing the current leg index.
   * @since 2.0.0
   */
  public int getLegIndex() {
    return legIndex;
  }

  public RouteLeg getCurrentLeg() {
    return route.getLegs().get(getLegIndex());
  }

  /**
   * Gets the next {@link LegStep}, if the route is on it's last step, we check if the route has an additional leg
   * and if not, return the current index.
   *
   * @return A {@link LegStep} representing the next (or final) step in the route.
   * @since 2.0.0
   */
  public LegStep getUpComingStep() {
    if (stepIndex + 1 < route.getLegs().get(legIndex).getSteps().size()) {
      return route.getLegs().get(legIndex).getSteps().get(stepIndex + 1);
    } else if ((route.getLegs().size() - 1) > legIndex) {
      // User has reached the end of the route leg, we adjust our indices and return the next step in new leg.
      legIndex += 1;
      stepIndex = 0;
      return route.getLegs().get(legIndex).getSteps().get(stepIndex);
    } else {
      // The user is on the last step.
      return route.getLegs().get(legIndex).getSteps().get(stepIndex);
    }
  }

  /*
   * Step progress getters
   */

  /**
   * Get the current step index the user is on during navigation.
   *
   * @return integer representing the current step index.
   * @since 2.0.0
   */
  public int getStepIndex() {
    return stepIndex;
  }

  public LegStep getCurrentStep() {
    return route.getLegs().get(legIndex).getSteps().get(stepIndex);
  }


  /**
   * Total distance traveled in meters along current {@link LegStep}.
   *
   * @since 2.0.0
   */
  public double getDistanceTraveledOnStep() throws TurfException {
    LineString lineString = LineString.fromPolyline(getCurrentStep().getGeometry(), Constants.PRECISION_6);
    return TurfMeasurement.lineDistance(lineString, TurfConstants.UNIT_METERS) - distanceRemainingOnStep;
  }

  public double getDurationRemainingOnStep() {
    return durationRemainingOnStep;
  }

  public double getDistanceRemainingOnStep() {
    logger.log(Level.FINE, "distance %f", distanceRemainingOnStep);
    return distanceRemainingOnStep;
  }


  /*
   * Protected setters
   */

  protected void setLegIndex(int legIndex) {
    this.legIndex = legIndex;
  }

  public void setAlertUserLevel(int alertUserLevel) {
    this.alertUserLevel = alertUserLevel;
  }

  public void setStepIndex(int stepIndex) {
    this.stepIndex = stepIndex;
  }

  public void setRoute(DirectionsRoute route) {
    this.route = route;
  }

  public void setDurationRemainingOnStep(double durationRemainingOnStep) {
    this.durationRemainingOnStep = durationRemainingOnStep;
  }

  public void setDistanceRemainingOnStep(double distanceRemainingOnStep) {
    this.distanceRemainingOnStep = distanceRemainingOnStep;
  }

  protected void setDistanceTraveled(double distanceTraveled) {
    this.distanceTraveled = distanceTraveled;
  }


  public double getUserSnapToStepDistanceFromManeuver() {
    return userSnapToStepDistanceFromManeuver;
  }

  public void setUserSnapToStepDistanceFromManeuver(double userSnapToStepDistanceFromManeuver) {
    this.userSnapToStepDistanceFromManeuver = userSnapToStepDistanceFromManeuver;
  }
}
