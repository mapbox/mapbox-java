package com.mapbox.services.api.utils.turf;

import java.util.HashMap;
import java.util.Map;

/**
 * This {@code TurfHelpers} class is made up of methods that take in an object, convert it, and then
 * return the object in the desired units or object.
 *
 * @see <a href="http://turfjs.org/docs/">Turfjs documentation</a>
 * @since 1.2.0
 */
public class TurfHelpers {

  private static final Map<String, Double> factors;

  static {
    factors = new HashMap<>();
    factors.put(TurfConstants.UNIT_MILES, 3960d);
    factors.put(TurfConstants.UNIT_NAUTICAL_MILES, 3441.145d);
    factors.put(TurfConstants.UNIT_DEGREES, 57.2957795d);
    factors.put(TurfConstants.UNIT_RADIANS, 1d);
    factors.put(TurfConstants.UNIT_INCHES, 250905600d);
    factors.put(TurfConstants.UNIT_YARDS, 6969600d);
    factors.put(TurfConstants.UNIT_METERS, 6373000d);
    factors.put(TurfConstants.UNIT_CENTIMETERS, 6.373e+8d);
    factors.put(TurfConstants.UNIT_KILOMETERS, 6373d);
    factors.put(TurfConstants.UNIT_FEET, 20908792.65d);

    // Also supported
    factors.put("centimetres", 6.373e+8d);
    factors.put("metres", 6373000d);
    factors.put("kilometres", 6373d);
  }

  /**
   * Convert radians to distance. The units used here equals the default.
   *
   * @param radians Double representing a radian value.
   * @return Converted radian to distance value.
   * @throws TurfException Signals that a Turf exception of some sort has occurred.
   * @since 1.2.0
   */
  public static double radiansToDistance(double radians) throws TurfException {
    return radiansToDistance(radians, TurfConstants.UNIT_DEFAULT);
  }

  /**
   * Convert radians to distance.
   *
   * @param radians Double representing a radian value.
   * @param units   Pass in the units you'd like to use.
   * @return Converted radian to distance value.
   * @throws TurfException Signals that a Turf exception of some sort has occurred.
   * @since 1.2.0
   */
  public static double radiansToDistance(double radians, String units) throws TurfException {
    Double factor = factors.get(units);
    if (factor == null) {
      throw new TurfException("Invalid unit.");
    }

    return radians * factor;
  }

  /**
   * Convert distance to radians. The units used here equals the default.
   *
   * @param distance Double representing a distance value.
   * @return Converted distance to radians value.
   * @throws TurfException Signals that a Turf exception of some sort has occurred.
   * @since 1.2.0
   */
  public static double distanceToRadians(double distance) throws TurfException {
    return distanceToRadians(distance, TurfConstants.UNIT_DEFAULT);
  }

  /**
   * Convert distance to radians.
   *
   * @param distance Double representing a distance value.
   * @param units    Pass in the units you'd like to use.
   * @return Converted distance to radians value.
   * @throws TurfException Signals that a Turf exception of some sort has occurred.
   * @since 1.2.0
   */
  public static double distanceToRadians(double distance, String units) throws TurfException {
    Double factor = factors.get(units);
    if (factor == null) {
      throw new TurfException("Invalid unit.");
    }
    return distance / factor;
  }

  /**
   * Converts a distance to the requested unit.
   * Valid units: miles, nauticalmiles, inches, yards, meters, metres, kilometers, centimeters, feet
   *
   * @param distance     the distance to be converted
   * @param originalUnit of the distance
   * @return the converted distance
   * @since 2.2.0
   */
  public static double convertDistance(double distance, String originalUnit) {
    return convertDistance(distance, originalUnit, TurfConstants.UNIT_DEFAULT);
  }

  /**
   * Converts a distance to the requested unit.
   * Valid units: miles, nauticalmiles, inches, yards, meters, metres, kilometers, centimeters, feet
   *
   * @param distance     the distance to be converted
   * @param originalUnit of the distance
   * @param finalUnit    returned unit, {@link TurfConstants#UNIT_DEFAULT} if not specified.
   * @return the converted distance
   * @since 2.2.0
   */
  public static double convertDistance(double distance, String originalUnit, String finalUnit) {
    Double factor = factors.get(originalUnit);
    if (factor == null) {
      throw new TurfException("Invalid unit.");
    } else if (!(distance >= 0)) {
      throw new TurfException("distance must be a positive number");
    }
    if (finalUnit == null) {
      finalUnit = TurfConstants.UNIT_DEFAULT;
    }

    return radiansToDistance(distanceToRadians(distance, originalUnit), finalUnit);
  }
}
