package com.mapbox.turf;

import android.support.annotation.StringDef;
import com.mapbox.geojson.Point;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This class holds the Turf constants which are useful when specifying additional information such
 * as units prior to executing the Turf function. For example, if I intend to get the distance
 * between two GeoJson Points using {@link TurfMeasurement#distance(Point, Point, String)} the third
 * optional parameter can define the output units.
 * <p>
 * Note that {@link TurfConversion#convertDistance(double, String, String)} can be used to transform
 * one unit to another, such as miles to feet.
 * </p>
 *
 * @see <a href="http://turfjs.org/docs/">Turfjs documentation</a>
 * @since 1.2.0
 */
public class TurfConstants {

  /**
   * The mile is an English unit of length of linear measure equal to 5,280 feet, or 1,760 yards,
   * and standardised as exactly 1,609.344 meters by international agreement in 1959.
   *
   * @since 1.2.0
   */
  public static final String UNIT_MILES = "miles";

  /**
   * The nautical mile per hour is known as the knot. Nautical miles and knots are almost
   * universally used for aeronautical and maritime navigation, because of their relationship with
   * degrees and minutes of latitude and the convenience of using the latitude scale on a map for
   * distance measuring.
   *
   * @since 1.2.0
   */
  public static final String UNIT_NAUTICAL_MILES = "nauticalmiles";

  /**
   * The kilometer (American spelling) is a unit of length in the metric system, equal to one
   * thousand meters. It is now the measurement unit used officially for expressing distances
   * between geographical places on land in most of the world; notable exceptions are the United
   * States and the road network of the United Kingdom where the statute mile is the official unit
   * used.
   * <p>
   * In many Turf calculations, if a unit is not provided, the output value will fallback onto using
   * this unit. See {@link #UNIT_DEFAULT} for more information.
   * </p>
   *
   * @since 1.2.0
   */
  public static final String UNIT_KILOMETERS = "kilometers";

  /**
   * The radian is the standard unit of angular measure, used in many areas of mathematics.
   *
   * @since 1.2.0
   */
  public static final String UNIT_RADIANS = "radians";

  /**
   * A degree, is a measurement of a plane angle, defined so that a full rotation is 360 degrees.
   *
   * @since 1.2.0
   */
  public static final String UNIT_DEGREES = "degrees";

  /**
   * The inch (abbreviation: in or ″) is a unit of length in the (British) imperial and United
   * States customary systems of measurement now formally equal to  1/36th yard but usually
   * understood as  1/12th of a foot.
   *
   * @since 1.2.0
   */
  public static final String UNIT_INCHES = "inches";

  /**
   * The yard (abbreviation: yd) is an English unit of length, in both the British imperial and US
   * customary systems of measurement, that comprises 3 feet or 36 inches.
   *
   * @since 1.2.0
   */
  public static final String UNIT_YARDS = "yards";

  /**
   * The metre (international spelling) or meter (American spelling) is the base unit of length in
   * the International System of Units (SI).
   *
   * @since 1.2.0
   */
  public static final String UNIT_METERS = "meters";

  /**
   * A centimeter (American spelling) is a unit of length in the metric system, equal to one
   * hundredth of a meter.
   *
   * @since 1.2.0
   */
  public static final String UNIT_CENTIMETERS = "centimeters";

  /**
   * The foot is a unit of length in the imperial and US customary systems of measurement.
   *
   * @since 1.2.0
   */
  public static final String UNIT_FEET = "feet";

  /**
   * A centimetre (international spelling) is a unit of length in the metric system, equal to one
   * hundredth of a meter.
   *
   * @since 3.0.0
   */
  public static final String UNIT_CENTIMETRES = "centimetres";

  /**
   * The metre (international spelling) is the base unit of length in
   * the International System of Units (SI).
   *
   * @since 3.0.0
   */
  public static final String UNIT_METRES = "metres";

  /**
   * The kilometre (international spelling) is a unit of length in the metric system, equal to one
   * thousand metres. It is now the measurement unit used officially for expressing distances
   * between geographical places on land in most of the world; notable exceptions are the United
   * States and the road network of the United Kingdom where the statute mile is the official unit
   * used.
   *
   * @since 3.0.0
   */
  public static final String UNIT_KILOMETRES = "kilometres";

  /**
   * Retention policy for the various Turf units.
   *
   * @since 3.0.0
   */
  @Retention(RetentionPolicy.SOURCE)
  @StringDef( {
    UNIT_KILOMETRES,
    UNIT_METRES,
    UNIT_CENTIMETRES,
    UNIT_FEET,
    UNIT_CENTIMETERS,
    UNIT_METERS,
    UNIT_YARDS,
    UNIT_INCHES,
    UNIT_DEGREES,
    UNIT_RADIANS,
    UNIT_KILOMETERS,
    UNIT_MILES,
    UNIT_NAUTICAL_MILES
  })
  public @interface TurfUnitCriteria {
  }

  /**
   * The default unit used in most Turf methods when no other unit is specified is kilometers.
   *
   * @since 1.2.0
   */
  public static final String UNIT_DEFAULT = UNIT_KILOMETERS;
}
