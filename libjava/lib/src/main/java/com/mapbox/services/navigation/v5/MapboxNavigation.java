package com.mapbox.services.navigation.v5;

import com.mapbox.services.Constants;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfConstants;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfMeasurement;
import com.mapbox.services.commons.turf.TurfMisc;
import com.mapbox.services.directions.v5.models.LegStep;
import com.mapbox.services.directions.v5.models.RouteLeg;

import java.util.List;

/**
 * Created by antonio on 7/21/16.
 */
public class MapboxNavigation {

    private RouteLeg route;
    private Position position;

    public MapboxNavigation() {
    }

    public RouteLeg getRoute() {
        return route;
    }

    public void setRoute(RouteLeg route) {
        this.route = route;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    private void checkValidState() throws ServicesException {
        if (getRoute() == null) {
            throw new ServicesException("MapboxNavigation requires a valid route.");
        }

        if (getPosition() == null) {
            throw new ServicesException("MapboxNavigation requires a valid user location");
        }
    }

    private Point getPositionAsPoint() {
        return Point.fromCoordinates(getPosition());
    }

    public LineString getStepCoordinatesLineString(LegStep step) {
        return LineString.fromPolyline(step.getGeometry(), Constants.GOOGLE_PRECISION);
    }

    public List<Position> getStepCoordinates(LegStep step) {
        return getStepCoordinatesLineString(step).getCoordinates();
    }

    public Point getClosestPointToStep(LegStep step) throws TurfException {
        // TurfMisc.pointOnLine() takes a Point and a LineString and calculates the closest Point
        // on the LineString.
        Feature feature = TurfMisc.pointOnLine(getPositionAsPoint(), getStepCoordinates(step));
        return (Point) feature.getGeometry();
    }

    public Position getClosestPositionToStep(LegStep step) throws TurfException {
        return getClosestPointToStep(step).getCoordinates();
    }

    public double getDistanceToStep(LegStep step) throws TurfException {
        // TurfMeasurement.distance calculates the distance between two points. This uses the
        // Haversine formula to account for global curvature.
        return TurfMeasurement.distance(
                getClosestPointToStep(step),
                getPositionAsPoint(),
                TurfConstants.UNIT_DEFAULT);
    }

    public double getDistanceToStepEnd(LegStep step) throws TurfException {
        // TurfMisc.lineSlice takes a line, a start Point, and a stop point and returns a
        // subsection of the line in-between those points. The start & stop points don't need to
        // fall exactly on the line. This can be useful for extracting only the part of a route
        // between waypoints.
        List<Position> coordinates = getStepCoordinates(step);
        LineString segmentSlicedToUser = TurfMisc.lineSlice(
                getPositionAsPoint(),
                Point.fromCoordinates(coordinates.get(coordinates.size() - 1)),
                getStepCoordinatesLineString(step));

        return TurfMeasurement.lineDistance(segmentSlicedToUser, TurfConstants.UNIT_DEFAULT);
    }
}
