package com.mapbox.geojson.shifter;

import com.mapbox.geojson.Point;

import java.util.Arrays;
import java.util.List;

/**
 * CoordinateShifterManager keeps track of currently set CoordinateShifter.
 *
 * @since 4.2.0
 */
public final class CoordinateShifterManager {

  private static final CoordinateShifter DEFAULT = new CoordinateShifter() {
    @Override
    public List<Double> shiftLonLat(double lon, double lat) {
      return Arrays.asList(lon, lat);
    }

    @Override
    public List<Double> shiftLonLatAlt(double lon, double lat, double alt) {
      return Double.isNaN(alt)
              ? Arrays.asList(lon, lat) :
              Arrays.asList(lon, lat, alt);
    }

    @Override
    public List<Double> unshiftPoint(Point point) {
      return point.coordinates();
    }

    @Override
    public List<Double> unshiftPoint(List<Double> coordinates) {
      return coordinates;
    }

    @Override
    public double[] shift(double lon, double lat) {
      return new double[]{lon, lat};
    }

    @Override
    public double[] shift(double lon, double lat, double altitude) {
      if (Double.isNaN(altitude)) {
        return shift(lon, lat);
      } else {
        return new double[]{lon, lat, altitude};
      }
    }

    @Override
    public double[] unshiftPointArray(double[] shiftedCoordinates) {
      return shiftedCoordinates;
    }
  };

  private static volatile CoordinateShifter coordinateShifter = DEFAULT;

  /**
   * Currently set CoordinateShifterManager.
   *
   * @return Currently set CoordinateShifterManager
   * @since 4.2.0
   */
  public static CoordinateShifter getCoordinateShifter() {
    return coordinateShifter;
  }

  /**
   * Sets CoordinateShifterManager.
   *
   * @param coordinateShifter CoordinateShifterManager to be set
   * @since 4.2.0
   */
  public static void setCoordinateShifter(CoordinateShifter coordinateShifter) {
    CoordinateShifterManager.coordinateShifter =
      coordinateShifter == null ? DEFAULT : coordinateShifter;
  }

  /**
   * Check whether the current shifter is the default one.
   * @return true if using default shifter.
   */
  public static boolean isUsingDefaultShifter() {
    return coordinateShifter == DEFAULT;
  }
}
