package com.mapbox.turf;

import com.mapbox.geojson.LineString;

import java.util.List;

/**
 * An interface for lazy distance calculation
 * used in {@link TurfMisc#lineSliceAlong(LineString, List, double, double, String)}.
 */
public interface DistanceProvider {

  /**
   * Invoked when the distance should be calculated.
   *
   * @return distance
   */
  double get();
}
