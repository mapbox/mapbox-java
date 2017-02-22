package com.mapbox.services.android.navigation.v5;

import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.mapbox.services.Experimental;
import com.mapbox.services.android.Constants;
import com.mapbox.services.android.telemetry.utils.MathUtils;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.models.StepIntersection;
import com.mapbox.services.api.navigation.v5.RouteProgress;
import com.mapbox.services.api.navigation.v5.RouteUtils;
import com.mapbox.services.api.utils.turf.TurfConstants;
import com.mapbox.services.api.utils.turf.TurfException;
import com.mapbox.services.api.utils.turf.TurfMeasurement;
import com.mapbox.services.commons.models.Position;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * This is an experimental API. Experimental APIs are quickly evolving and
 * might change or be removed in minor versions.
 */
@Experimental
public class LocationUpdatedThread extends HandlerThread {

  private static final String TAG = "LocationUpdatedThread";
  private static final int MESSAGE_LOCATION_UPDATED = 0;

  // Handlers
  private Handler requestHandler;
  private Handler responseHandler;

  // Navigation Variables
  private AlertLevelChangeListener alertLevelChangeListener;
  private ProgressChangeListener progressChangeListener;
  private double userDistanceToManeuverLocation;
  private boolean snapToRoute;
  private Location location;


  LocationUpdatedThread(Handler responseHandler) {
    super(TAG);
    this.responseHandler = responseHandler;
    // By default we snap to route when possible.
    snapToRoute = true;
  }

  @Override
  protected void onLooperPrepared() {
    requestHandler = new RequestHandler(this);
  }

  void updateLocation(RouteProgress target, Location location) {
    Timber.d("updateLocation called");
    this.location = location;

    requestHandler.obtainMessage(MESSAGE_LOCATION_UPDATED, target).sendToTarget();
  }

  private void handleRequest(final RouteProgress target, Location location) {
    if (location == null) {
      return;
    }

    try {
      // Convert the location object to both the true position and a snapped (to the route) position.
      Position truePosition = Position.fromCoordinates(location.getLongitude(), location.getLatitude());
      Position snappedPosition = RouteUtils.getSnapToRoute(
        truePosition,
        target.getRoute().getLegs().get((target).getLegIndex()),
        target.getStepIndex()
      );

      // Even if the user isn't listening in to the alert listener, we need to run monitorStepProgress inorder to
      // update the routeProgress object
      final int alertLevel = monitorStepProgress(target, location, truePosition, snappedPosition);

      List<StepIntersection> intersections = getNextIntersections(target, snappedPosition);

      // Test the closest intersection to the user only.
      boolean userStillOnRoute = true;
      if (intersections.size() > 0) {
        userStillOnRoute = isUserStillOnRoute(intersections.get(0), location.getBearing());
      }
      if (snapToRoute && userStillOnRoute) {
        // Pass in the snapped location with all the other location data remaining intact for their use.
        location.setLatitude(snappedPosition.getLatitude());
        location.setLongitude(snappedPosition.getLongitude());
      }

      this.location = location;

      // Post back to the UI Thread.
      responseHandler.post(new Runnable() {
        public void run() {
          if (target.getPreviousAlertLevel() != alertLevel) {
            target.setAlertUserLevel(alertLevel);
            alertLevelChangeListener.onAlertLevelChange(alertLevel, target);
          }
          progressChangeListener.onProgressChange(LocationUpdatedThread.this.location, target);
        }
      });

    } catch (ServicesException | TurfException exception) {
      exception.printStackTrace();
    }
  }

  void setAlertLevelChangeListener(AlertLevelChangeListener alertLevelChangeListener) {
    this.alertLevelChangeListener = alertLevelChangeListener;
  }

  void setProgressChangeListener(ProgressChangeListener progressChangeListener) {
    this.progressChangeListener = progressChangeListener;
  }

  void setSnapToRoute(boolean snapToRoute) {
    this.snapToRoute = snapToRoute;
  }

  private int monitorStepProgress(final RouteProgress routeProgress, Location location, Position truePosition,
                                  Position snappedPosition)
    throws ServicesException, TurfException {

    // Force an announcement when the user begins a route
    int alertLevel = routeProgress.getPreviousAlertLevel() == Constants.NONE_ALERT_LEVEL ? Constants.DEPART_ALERT_LEVEL
      : routeProgress.getPreviousAlertLevel();

    double userSnapToStepDistanceFromManeuver = RouteUtils.getDistanceToNextStep(
      snappedPosition,
      routeProgress.getRoute().getLegs().get(routeProgress.getLegIndex()),
      routeProgress.getStepIndex(),
      TurfConstants.UNIT_METERS);

    routeProgress.setDistanceRemainingOnStep(userSnapToStepDistanceFromManeuver);

    routeProgress.setDurationRemainingOnStep(userSnapToStepDistanceFromManeuver / location.getSpeed());
    boolean courseMatchesManeuverFinalHeading;

    double finalHeading = routeProgress.getUpComingStep().getManeuver().getBearingAfter();
    double finalHeadingNormalized = MathUtils.wrap(finalHeading, 0, 360);
    double userHeadingNormalized = MathUtils.wrap(location.getBearing(), 0, 360);
    courseMatchesManeuverFinalHeading = MathUtils.differenceBetweenAngles(finalHeadingNormalized, userHeadingNormalized)
      <= Constants.MAXIMUM_ALLOWED_DEGREE_OFFSET_FOR_TURN_COMPLETION;

    Timber.v("finalHeading: " + finalHeadingNormalized);
    Timber.v("user heading: " + userHeadingNormalized);
    Timber.v("userSnapToStepDistanceFromManeuver: %f", userSnapToStepDistanceFromManeuver);
    if (userSnapToStepDistanceFromManeuver <= Constants.MANEUVER_ZONE_RADIUS) {

      double userAbsoluteDistance = TurfMeasurement.distance(
        truePosition, routeProgress.getUpComingStep().getManeuver().asPosition(), TurfConstants.UNIT_METERS
      );

      // userAbsoluteDistanceToManeuverLocation is set to nil by default
      // If it's set to nil, we know the user has never entered the maneuver radius
      if (userDistanceToManeuverLocation == 0.0) {
        userDistanceToManeuverLocation = Constants.MANEUVER_ZONE_RADIUS;
      }

      double lastKnownUserAbsoluteDistance = userDistanceToManeuverLocation;

      // The objective here is to make sure the user is moving away from the maneuver location
      // This helps on maneuvers where the difference between the exit and enter heading are similar
      if (userAbsoluteDistance <= lastKnownUserAbsoluteDistance) {
        userDistanceToManeuverLocation = userAbsoluteDistance;
      }

      if (routeProgress.getUpComingStep().getManeuver().getType().equals(Constants.STEP_MANEUVER_TYPE_ARRIVE)) {
        alertLevel = Constants.ARRIVE_ALERT_LEVEL;
      } else if (courseMatchesManeuverFinalHeading) {
        routeProgress.setStepIndex(routeProgress.getStepIndex() + 1);
        userSnapToStepDistanceFromManeuver = RouteUtils.getDistanceToNextStep(
          snappedPosition,
          routeProgress.getRoute().getLegs().get(routeProgress.getLegIndex()),
          routeProgress.getStepIndex(),
          TurfConstants.UNIT_METERS);

        routeProgress.setDurationRemainingOnStep(userSnapToStepDistanceFromManeuver / location.getSpeed());
        alertLevel = routeProgress.getDurationRemainingOnStep()
          <= Constants.MEDIUM_ALERT_INTERVAL ? Constants.MEDIUM_ALERT_LEVEL : Constants.LOW_ALERT_LEVEL;
      }

    } else if (routeProgress.getDurationRemainingOnStep() <= Constants.HIGH_ALERT_INTERVAL
      && routeProgress.getRoute().getLegs().get(routeProgress.getLegIndex())
      .getSteps().get(routeProgress.getStepIndex()).getDistance() > Constants.MINIMUM_DISTANCE_FOR_HIGH_ALERT) {
      alertLevel = Constants.HIGH_ALERT_LEVEL;
    } else if (routeProgress.getDurationRemainingOnStep() <= Constants.MEDIUM_ALERT_INTERVAL
      && routeProgress.getRoute().getLegs().get(routeProgress.getLegIndex())
      .getSteps().get(routeProgress.getStepIndex()).getDistance() > Constants.MINIMUM_DISTANCE_FOR_MEDIUM_ALERT) {
      alertLevel = Constants.MEDIUM_ALERT_LEVEL;
    }
    return alertLevel;
  }

  /**
   * Gets the currents step and next steps intersections and returns a list of the intersections x meters ahead of the
   * user. This is done so we narrow down the number of intersections needed to calculate angle.
   *
   * @param userPosition Snap the user to the route so we get a more accurate measurement.
   * @return A list containing all intersections x meters away
   * @throws TurfException Thrown if turf calculation error occurs.
   * @since 2.0.0
   */
  private List<StepIntersection> getNextIntersections(RouteProgress routeProgress, Position userPosition)
    throws TurfException {
    List<StepIntersection> intersectionsWithinRange = new ArrayList<>();
    List<StepIntersection> stepIntersections = new ArrayList<>();

    double closestIntersectionDistance = routeProgress.getDistanceTraveledOnStep();

    // Add all the intersections for current and next step to list.
    stepIntersections.addAll(routeProgress.getCurrentStep().getIntersections());

    for (StepIntersection intersection : stepIntersections) {
      // Measures the distance between the beginning of step to the current intersection. If this distance is less then
      // the users distance traveled on route, we know they have passed the intersection already.
      double disToIntersection = TurfMeasurement.distance(
        routeProgress.getCurrentStep().getManeuver().asPosition(),
        intersection.asPosition(),
        TurfConstants.UNIT_METERS
      );

      if (disToIntersection > closestIntersectionDistance) {
        // If the user is within x meters of the intersection we add it to the returning list.
        if (TurfMeasurement.distance(
          userPosition, intersection.asPosition(), TurfConstants.UNIT_METERS
        ) <= Constants.METERS_TO_INTERSECTION) {
          intersectionsWithinRange.add(intersection);
        }
      }
    }
    // none the current step doesn't have any steps left, we go ahead and watch for the next steps first intersection
    if (intersectionsWithinRange.size() < 1) {
      if (TurfMeasurement.distance(
        userPosition,
        routeProgress.getUpComingStep().getIntersections().get(0).asPosition(),
        TurfConstants.UNIT_METERS
      ) <= Constants.METERS_TO_INTERSECTION) {
        intersectionsWithinRange.add(routeProgress.getUpComingStep().getIntersections().get(0));
      }
    }
    return intersectionsWithinRange;
  }

  /**
   * Method actually detects if the user has made a wrong turn in an intersection. While this greatly reduces the
   * amount of likelihood that the user made a wrong turn, it still isn't guaranteed to detect that the user is still
   * on the route or not, sometimes giving a false positive. Errors in calculations here tend to happen in
   * intersections that have turns with very close angles to the correct turn angle (since this decreases the
   * calculated tolerance). A false positive can also occur when the users bearing turns outside the calculated
   * tolerance.
   *
   * @param intersection The intersection you want to calculate whether the user made a wrong turn or not.
   * @param userHeading  Provided by the {@link Location} objects {@code getBearing()} method.
   * @return boolean true if the user remains on the route through the intersection, else false.
   * @since 2.0.0
   */
  private boolean isUserStillOnRoute(StepIntersection intersection, double userHeading) {
    // We start off assuming the user is on route.
    boolean isOnRoute = true;

    // Loop over all the turn possibilities in the intersection.
    for (int i = 0; i < intersection.getEntry().length; i++) {
      // Entry is false if the turn is illegal, therefore we ignore these.
      if (intersection.getEntry()[i]) {

        // Get the ange
        int in = intersection.getBearings()[intersection.getIn()] - 180;
        int out = intersection.getBearings()[intersection.getOut()];
        Timber.d("Correct angle into intersection: %d Correct angle leaving intersection: %d", in, out);

        // Correct angle the user must maintain to stay on route.
        int correctAngle = 180 - Math.abs(Math.abs(in - out) - 180);
        // The angle between the user and the correct outter angle.
        int userAngle = 180 - Math.abs(Math.abs((int) userHeading - out) - 180);
        Timber.d("Correct Angle: %d User Angle: %d", correctAngle, userAngle);

        // Adjust the angle tolerance to account for the smallest valid angle in the intersection. For example, if the
        // intersections sharpest turn is 50 degrees, the tolerance would equal plus or minus 25.
        int tolerance = Constants.DEFAULT_ANGLE_TOLERANCE;
        int intersectionPossibleTurnAngle = 180 - Math.abs(Math.abs(intersection.getBearings()[i] - out) - 180);
        if (tolerance > intersectionPossibleTurnAngle && intersectionPossibleTurnAngle != 0) {
          tolerance = intersectionPossibleTurnAngle;
        }

        Timber.d("tolerance value %d", tolerance);

        // If the user is within the tolerance, we know they are following the route correctly
        if (Math.abs(userAngle - correctAngle) > tolerance) {
          isOnRoute = false;
        }
      }
    }
    return isOnRoute;
  }

  /**
   * Prevents having a reference to our NavigationService class. Allows for garbage collected and reduces chance of
   * memory leak.
   */
  private static class RequestHandler extends Handler {
    //Using a weak reference means you won't prevent garbage collection
    private final WeakReference<LocationUpdatedThread> locationUpdatedHandler;

    RequestHandler(LocationUpdatedThread myClassInstance) {
      locationUpdatedHandler = new WeakReference<>(myClassInstance);
    }

    @Override
    public void handleMessage(Message msg) {
      LocationUpdatedThread locationUpdatedThread = this.locationUpdatedHandler.get();
      if (locationUpdatedThread != null) {
        if (msg.what == MESSAGE_LOCATION_UPDATED) {
          RouteProgress target = (RouteProgress) msg.obj;
          Timber.d("Received request to calculate new location information");
          locationUpdatedThread.handleRequest(target, locationUpdatedThread.location);
        }
      }
    }
  }
}
