package com.mapbox.geojson;

import androidx.annotation.Keep;

/**
 * Interface for GeoJSON geometry types that support both standard and flattened coordinate
 * representations. The flattened representation stores coordinates in primitive arrays for
 * improved performance, particularly for JNI access on Android.
 * <p>
 * This interface extends {@link CoordinateContainer} to provide access to coordinates in their
 * standard form (typically as {@link Point} objects) while also offering a flattened form
 * optimized for low-level operations.
 *
 * @param <T> the standard coordinate container type (e.g., {@code List<Point>})
 * @param <P> the flattened coordinate representation type (e.g., {@link FlattenListOfPoints})
 */
@Keep
interface FlattenedCoordinateContainer<T, P> extends CoordinateContainer<T> {
  /**
   * Returns the flattened coordinate representation of this geometry.
   * <p>
   * The flattened form stores coordinates in primitive arrays, which provides better performance
   * for operations that require direct array access, particularly in JNI contexts.
   *
   * @return the flattened coordinate representation.
   */
  P flattenCoordinates();
}
