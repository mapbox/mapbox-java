package com.mapbox.services.commons.turf;

import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

/**
 * Created by antonio on 5/12/16.
 */
public class TurfMeasurement {

    /**
     * Calculates the distance between two points in kilometers.
     * This uses the Haversine formula to account for global curvature.
     */
    public static double distance(Point point1, Point point2) throws TurfException {
        return distance(point1, point2, TurfConstants.UNIT_DEFAULT);
    }

    /**
     * Calculates the distance between two points in degress, radians, miles, or kilometers.
     * This uses the Haversine formula to account for global curvature.
     */
    public static double distance(Point point1, Point point2, String units) throws TurfException {
        double degrees2radians = Math.PI / 180;
        Position coordinates1 = point1.getCoordinates();
        Position coordinates2 = point2.getCoordinates();
        double dLat = degrees2radians * (coordinates2.getLatitude() - coordinates1.getLatitude());
        double dLon = degrees2radians * (coordinates2.getLongitude() - coordinates1.getLongitude());
        double lat1 = degrees2radians * coordinates1.getLatitude();
        double lat2 = degrees2radians * coordinates2.getLatitude();

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);

        return TurfHelpers.radiansToDistance(2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)), units);
    }

}
