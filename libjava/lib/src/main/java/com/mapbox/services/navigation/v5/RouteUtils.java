package com.mapbox.services.navigation.v5;

import com.mapbox.services.Constants;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfConstants;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfMeasurement;
import com.mapbox.services.commons.turf.TurfMisc;
import com.mapbox.services.commons.utils.PolylineUtils;
import com.mapbox.services.directions.v5.models.LegStep;
import com.mapbox.services.directions.v5.models.RouteLeg;

import java.util.List;
import java.util.Locale;

/**
 * A few utilities to work with RouteLeg objects.
 */
public class RouteUtils {

    // Default threshold for the user to be considered to be off-route (100 meters)
    public static final double DEFAULT_OFF_ROUTE_THRESHOLD_KM = 0.1;

    private double offRouteThresholdKm;

    public RouteUtils() {
        this.offRouteThresholdKm = DEFAULT_OFF_ROUTE_THRESHOLD_KM;
    }

    public RouteUtils(double offRouteThresholdKm) {
        this.offRouteThresholdKm = offRouteThresholdKm;
    }

    public double getOffRouteThresholdKm() {
        return offRouteThresholdKm;
    }

    public boolean isInStep(Position position, RouteLeg route, int stepIndex) throws ServicesException, TurfException {
        // Compute the distance between the position and the step line (the closest point), and
        // checks if it's within the off-route threshold
        double distance = getDistanceToStep(position, route, stepIndex);
        return (distance <= offRouteThresholdKm);
    }

    public double getDistanceToStep(Position position, RouteLeg route, int stepIndex) throws ServicesException, TurfException {
        // Compute the distance between the position and the step line (the closest point).
        Position closestPoint = getSnapToRoute(position, route, stepIndex);
        return TurfMeasurement.distance(Point.fromCoordinates(position), Point.fromCoordinates(closestPoint), TurfConstants.UNIT_DEFAULT);
    }

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

    public boolean isOffRoute(Position position, RouteLeg route) throws ServicesException, TurfException {
        for (int stepIndex = 0; stepIndex < route.getSteps().size(); stepIndex++) {
            if (isInStep(position, route, stepIndex)) {
                // We aren't off-route if we're close to at least one route step
                return false;
            }
        }

        return true;
    }

    public int getClosestStep(Position position, RouteLeg route) throws ServicesException, TurfException {
        double minDistance = Double.MAX_VALUE;
        int closestIndex = 0;

        double distance;
        for (int stepIndex = 0; stepIndex < route.getSteps().size(); stepIndex++) {
            distance = getDistanceToStep(position, route, stepIndex);
            if (distance < minDistance) {
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
            throw new ServicesException(String.format(Locale.US, "The provided route doesn't have so many steps (%d).", stepIndex));
        }

        return route.getSteps().get(stepIndex);
    }
}
