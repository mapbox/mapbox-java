package com.mapbox.geojson.shifter;

import com.mapbox.geojson.Point;

import java.util.List;

/**
 * ShifterManager allows the movement of all Point objects according to a custom algorithm.
 * Once set, it will be applied to all Point objects created through this method.
 *
 * @since 4.1.1
 */
public interface CoordinateShifter {

  /**
   * Shifted coordinate values according to its algorithm.
   *
   * @param lon unshifted longitude
   * @param lat unshifted latitude
   * @return shifted longitude, shifted latitude in the form of a List of Double values
   * @since 4.1.1
   */
  List<Double> shiftLonLat(double lon, double lat);

  /**
   * Shifted coordinate values according to its algorithm.
   *
   * @param lon unshifted longitude
   * @param lat unshifted latitude
   * @param altitude  unshifted altitude
   * @return shifted longitude, shifted latitude, shifted altitude in the form of a
   * List of Double values
   * @since 4.1.1
   */
  List<Double> shiftLonLatAlt(double lon, double lat, double altitude);

  /**
   * Unshifted coordinate values according to its algorithm.
   *
   * @param shiftedPoint shifted point
   * @return unshifted longitude, shifted latitude,
   *         and altitude (if present) in the form of List of Double
   * @since 4.1.1
   */
  List<Double> unshiftPoint(Point shiftedPoint);


  /**
   * Unshifted coordinate values according to its algorithm.
   *
   * @param shiftedCoordinates shifted point
   * @return unshifted longitude, shifted latitude,
   *         and altitude (if present) in the form of List of Double
   * @since 4.1.1
   */
  List<Double> unshiftPoint(List<Double> shiftedCoordinates);
}
