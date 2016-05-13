package com.mapbox.services.commons.turf;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by antonio on 5/12/16.
 */
public class TurfHelpers {

    private static final Map<String, Double> factors;
    static
    {
        factors = new HashMap<>();
        factors.put(TurfConstants.UNIT_MILES, 3960d);
        factors.put(TurfConstants.UNIT_NAUTICAL_MILES, 3441.145d);
        factors.put(TurfConstants.UNIT_DEGREES, 57.2957795d);
        factors.put(TurfConstants.UNIT_RADIANS, 1d);
        factors.put(TurfConstants.UNIT_INCHES, 250905600d);
        factors.put(TurfConstants.UNIT_YARDS, 6969600d);
        factors.put(TurfConstants.UNIT_METERS, 637300d);
        factors.put(TurfConstants.UNIT_KILOMETERS, 6373d);

        // Also supported
        factors.put("metres", 637300d);
        factors.put("kilometres", 6373d);
    }

    public static double radiansToDistance(double radians) throws TurfException {
        return radiansToDistance(radians, TurfConstants.UNIT_DEFAULT);
    }

    public static double radiansToDistance(double radians, String units) throws TurfException {
        Double factor = factors.get(units);
        if (factor == null) {
            throw new TurfException("Invalid unit.");
        }

        return radians * factor;
    }

    public static double distanceToRadians(double radians) throws TurfException {
        return distanceToRadians(radians, TurfConstants.UNIT_DEFAULT);
    }

    public static double distanceToRadians(double radians, String units) throws TurfException {
        Double factor = factors.get(units);
        if (factor == null) {
            throw new TurfException("Invalid unit.");
        }

        return radians / factor;
    }

}
