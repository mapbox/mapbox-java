package com.mapbox.services.navigation;

import com.mapbox.services.Constants;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfConstants;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfMeasurement;
import com.mapbox.services.commons.turf.TurfMisc;
import com.mapbox.services.directions.v5.models.RouteLeg;

import java.util.List;

/**
 * This is a port of https://github.com/mapbox/navigation.js to Java, an utility for monitoring
 * a user location in relation to a route.
 */
public class NavigationUtils {

    // Constants
    private final static double feetToMiles = 0.000189394;
    private final static double metersToMiles = 0.000621371;
    private final static double feetToKilometers = 0.0003048;
    private final static double metersToKilometers = 1000;
    private final static double metersToFeet = 3.28084;

    // Either `miles` or `km`
    private final String units = TurfConstants.UNIT_MILES;

    // Number of seconds ahead of maneuver to warn user about maneuver.
    private final double warnUserTime = 30;

    // Bearing threshold for the user to complete a step.
    private final double userBearingCompleteThreshold = 30;

    // Max distance the user can be from the route.
    private final double maxReRouteDistance = 150 * feetToMiles;

    // Max distance to snap user to route.
    private final double maxSnapToLocation = 50 * feetToMiles;

    // Distance away from end of step that is considered a completion. If this distance is
    // shorter than the step distance, it will be changed to 10ft.
    private final double completionDistance = 50 * feetToMiles;

    // If the step is shorter than the `completionDistance`, this distance will be used to
    // calculate if the step has been completed.
    private final double shortCompletionDistance = 10 * feetToMiles;

    /**
     * Given a user location and route, calculates closest step to user.
     */
    public CurrentStepModel getCurrentStep(Point user, RouteLeg route, int userCurrentStep, Double userBearing) throws TurfException {
        CurrentStepModel currentStep = new CurrentStepModel();

        LineString lineString = LineString.fromPolyline(route.getSteps().get(userCurrentStep).getGeometry(), Constants.OSRM_PRECISION_V5);
        List<Position> stepCoordinates = lineString.getCoordinates();

        Feature segmentRoute = Feature.fromGeometry(LineString.fromCoordinates(stepCoordinates));

        Feature closestPoint = TurfMisc.pointOnLine(user, stepCoordinates);
        double distance = TurfMeasurement.distance(user, (Point)closestPoint.getGeometry(), this.units);

        Feature segmentEndPoint = Feature.fromGeometry(Point.fromCoordinates(stepCoordinates.get(stepCoordinates.size() - 1)));
        LineString segmentSlicedToUser = TurfMisc.lineSlice(user, (Point) segmentEndPoint.getGeometry(), segmentRoute);
        double userDistanceToEndStep = TurfMeasurement.lineDistance(segmentSlicedToUser, this.units);

        //
        // Check if user has completed step. Two factors:
        //   1. Are they within a certain threshold of the end of the step?
        //   2. If a bearing is provided, is their bearing within a current threshold of the exit bearing for the step
        //
        double stepDistance = (this.units == "miles")
                ? route.getSteps().get(userCurrentStep).getDistance() * metersToMiles
                : route.getSteps().get(userCurrentStep).getDistance() * metersToKilometers;

        // If the step distance is less than this.completionDistance, modify it and make it 10 ft
        double modifiedCompletionDistance = stepDistance < this.completionDistance
                ? this.shortCompletionDistance : this.completionDistance;

        // Check if users bearing is within threshold of the steps exit bearing
        boolean withinBearingThreshold = userBearing != null
                ? Math.abs(userBearing - route.getSteps().get(userCurrentStep).getManeuver().getBearingAfter()) <= this.userBearingCompleteThreshold ? true : false
                : true;

        // Do not increment userCurrentStep if the user is approaching the final step
        if (userCurrentStep < route.getSteps().size()- 2) {
            currentStep.step = withinBearingThreshold && (userDistanceToEndStep < modifiedCompletionDistance)
                    ? userCurrentStep + 1 : userCurrentStep; // Don't set next step + 1 if at the end of the route
        } else {
            currentStep.step = userCurrentStep;
        }

        currentStep.distance = userDistanceToEndStep;
        currentStep.stepDistance = stepDistance;
        currentStep.shouldReRoute = TurfMeasurement.distance(user, (Point)closestPoint.getGeometry(), this.units) > this.maxReRouteDistance ? true : false;
        currentStep.absoluteDistance = TurfMeasurement.distance(user, (Point)segmentEndPoint.getGeometry(), this.units);
        currentStep.snapToLocation = distance < this.maxSnapToLocation ? (Point)closestPoint.getGeometry() : user;

        // Alert levels
        currentStep.alertUserLevelLow = userDistanceToEndStep < 1 && route.getSteps().get(userCurrentStep).getDistance() * metersToMiles > 1; // Step must be longer than 1 miles
        currentStep.alertUserLevelHigh = (userDistanceToEndStep < 150 * feetToMiles) && route.getSteps().get(userCurrentStep).getDistance() * metersToFeet > 150; // Step must be longer than 150 ft

        return currentStep;
    }

}
