package com.mapbox.services.api.utils.turf;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.services.api.utils.turf.TurfConstants.TurfUnitCriteria;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is made up of methods that take in an object, convert it, and then return the object
 * in the desired units or object.
 *
 * @see <a href="http://turfjs.org/docs/">Turfjs documentation</a>
 * @since 1.2.0
 */
public class TurfHelpers {

  private static final Map<String, Double> FACTORS;

  static {
    FACTORS = new HashMap<>();
    FACTORS.put(TurfConstants.UNIT_MILES, 3960d);
    FACTORS.put(TurfConstants.UNIT_NAUTICAL_MILES, 3441.145d);
    FACTORS.put(TurfConstants.UNIT_DEGREES, 57.2957795d);
    FACTORS.put(TurfConstants.UNIT_RADIANS, 1d);
    FACTORS.put(TurfConstants.UNIT_INCHES, 250905600d);
    FACTORS.put(TurfConstants.UNIT_YARDS, 6969600d);
    FACTORS.put(TurfConstants.UNIT_METERS, 6373000d);
    FACTORS.put(TurfConstants.UNIT_CENTIMETERS, 6.373e+8d);
    FACTORS.put(TurfConstants.UNIT_KILOMETERS, 6373d);
    FACTORS.put(TurfConstants.UNIT_FEET, 20908792.65d);
    FACTORS.put(TurfConstants.UNIT_CENTIMETRES, 6.373e+8d);
    FACTORS.put(TurfConstants.UNIT_METRES, 6373000d);
    FACTORS.put(TurfConstants.UNIT_KILOMETRES, 6373d);
  }

  /**
   * Convert a distance measurement (assuming a spherical Earth) from radians to a more friendly
   * unit. The units used here equals the default.
   *
   * @param radians a double using unit radian
   * @return converted radian to distance value
   * @since 1.2.0
   */
  public static double radiansToDistance(double radians) {
    return radiansToDistance(radians, TurfConstants.UNIT_DEFAULT);
  }

  /**
   * Convert a distance measurement (assuming a spherical Earth) from radians to a more friendly
   * unit.
   *
   * @param radians a double using unit radian
   * @param units   pass in one of the units defined in {@link TurfUnitCriteria}
   * @return converted radian to distance value
   * @since 1.2.0
   */
  public static double radiansToDistance(double radians, @NonNull @TurfUnitCriteria String units) {
    return radians * FACTORS.get(units);
  }

  /**
   * Convert a distance measurement (assuming a spherical Earth) from a real-world unit into
   * radians.
   *
   * @param distance double representing a distance value assuming the distance units is in
   *                 kilometers
   * @return converted distance to radians value
   * @since 1.2.0
   */
  public static double distanceToRadians(double distance) {
    return distanceToRadians(distance, TurfConstants.UNIT_DEFAULT);
  }

  /**
   * Convert a distance measurement (assuming a spherical Earth) from a real-world unit into
   * radians.
   *
   * @param distance double representing a distance value
   * @param units    pass in one of the units defined in {@link TurfUnitCriteria}
   * @return converted distance to radians value
   * @since 1.2.0
   */
  public static double distanceToRadians(double distance, @NonNull @TurfUnitCriteria String units) {
    return distance / FACTORS.get(units);
  }

  /**
   * Converts a distance to the default units. Use
   * {@link TurfHelpers#convertDistance(double, String, String)} to specify a unit to convert to.
   *
   * @param distance     double representing a distance value
   * @param originalUnit of the distance, must be one of the units defined in
   *                     {@link TurfUnitCriteria}
   * @return converted distance in the default unit
   * @since 2.2.0
   */
  public static double convertDistance(@FloatRange(from = 0) double distance,
                                       @NonNull @TurfUnitCriteria String originalUnit) {
    return convertDistance(distance, originalUnit, TurfConstants.UNIT_DEFAULT);
  }

  /**
   * Converts a distance to a different unit specified.
   *
   * @param distance     the distance to be converted
   * @param originalUnit of the distance, must be one of the units defined in
   *                     {@link TurfUnitCriteria}
   * @param finalUnit    returned unit, {@link TurfConstants#UNIT_DEFAULT} if not specified
   * @return the converted distance
   * @since 2.2.0
   */
  public static double convertDistance(@FloatRange(from = 0) double distance,
                                       @NonNull @TurfUnitCriteria String originalUnit,
                                       @Nullable @TurfUnitCriteria String finalUnit) {
    if (finalUnit == null) {
      finalUnit = TurfConstants.UNIT_DEFAULT;
    }
    return radiansToDistance(distanceToRadians(distance, originalUnit), finalUnit);
  }
}
