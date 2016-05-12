package com.mapbox.services.commons.turf;

import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

/**
 * Created by antonio on 5/12/16.
 */
public class TurfMeasurement {

    /**
     * Calculates the distance between two points in degress, radians, miles, or kilometers.
     * This uses the Haversine formula to account for global curvature.
     */
    public static double distance(Point point1, Point point2, String units) throws TurfException {
        Position coordinates1 = point1.getCoordinates();
        Position coordinates2 = point2.getCoordinates();

        double dLat = Math.toRadians(coordinates2.getLatitude() - coordinates1.getLatitude());
        double dLon = Math.toRadians(coordinates2.getLongitude() - coordinates1.getLongitude());
        double lat1 = Math.toRadians(coordinates1.getLatitude());
        double lat2 = Math.toRadians(coordinates2.getLatitude());

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) *
                Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double R;
        switch (units) {
            case TurfConstants.UNIT_MILES:
                R = 3960;
                break;
            case TurfConstants.UNIT_KILOMETERS:
                R = 6373;
                break;
            case TurfConstants.UNIT_DEGREES:
                R = 57.2957795;
                break;
            case TurfConstants.UNIT_RADIANS:
                R = 1;
                break;
            default:
                throw new TurfException("Unknown option given to units.");
        }

        double distance = R * c;
        return distance;
    }

}
