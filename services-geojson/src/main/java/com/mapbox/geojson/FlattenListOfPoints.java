package com.mapbox.geojson;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Keep
public class FlattenListOfPoints implements Serializable {
  /**
   * A one-dimensional array to store the flattened coordinates: [lat1, lng1, lat2, lng2, ...].
   * <p>
   * Note: we use one-dimensional array for performance reasons related to JNI access (
   * <a href="https://developer.android.com/ndk/guides/jni-tips#primitive-arrays">Android JNI Tips
   * - Primitive arrays</a>)
   *
   * @see #coordinatesPrimitives()
   */
  @NonNull
  private final double[] flattenLatLngCoordinates;
  /**
   * An array to store the altitudes of each coordinate or {@link Double#NaN} if the coordinate
   * does not have altitude.
   *
   * @see #coordinatesPrimitives()
   */
  @Nullable
  private final double[] altitudes;
  /**
   * An array to store the {@link BoundingBox} of each coordinate or null if the coordinate does
   * not have bounding box.
   */
  @Nullable
  private BoundingBox[] coordinatesBoundingBoxes;

  FlattenListOfPoints(List<Point> coordinates) {
    double[] flattenLatLngCoordinates = new double[coordinates.size() * 2];
    double[] altitudes = null;
    for (int i = 0; i < coordinates.size(); i++) {
      Point point = coordinates.get(i);
      flattenLatLngCoordinates[i * 2] = point.longitude();
      flattenLatLngCoordinates[(i * 2) + 1] = point.latitude();

      // It is quite common to not have altitude in Point. Therefore only if we have points
      // with altitude then we create an array to store those.
      if (point.hasAltitude()) {
        // If one point has altitude we create an array of double to store the altitudes.
        if (altitudes == null) {
          altitudes = new double[coordinates.size()];
          // Fill in any previous altitude as NaN
          for (int j = 0; j < i; j++) {
            altitudes[j] = Double.NaN;
          }
        }
        altitudes[i] = point.altitude();
      } else if (altitudes != null) {
        // If we are storing altitudes but this point doesn't have it then set it to NaN
        altitudes[i] = Double.NaN;
      }

      // Similarly to altitudes, if one point has bound we create an array to store those.
      if (point.bbox() != null) {
        if (coordinatesBoundingBoxes == null) {
          coordinatesBoundingBoxes = new BoundingBox[coordinates.size()];
        }
        coordinatesBoundingBoxes[i] = point.bbox();
      }
    }
    this.flattenLatLngCoordinates = flattenLatLngCoordinates;
    this.altitudes = altitudes;
  }

  /**
   * Returns two arrays of doubles:
   * - The first one is a flatten array of all the coordinates (lat, lng) in the line string: [lat1, lng1, lat2, lng2, ...].
   * - The second (nullable) one is an array of all the altitudes in the line string (or null if no altitudes are present).
   */
  public double[][] coordinatesPrimitives() {
    return new double[][]{flattenLatLngCoordinates, altitudes};
  }

  @Nullable
  public BoundingBox[] getCoordinatesBoundingBoxes() {
    return coordinatesBoundingBoxes;
  }

  public List<Point> coordinates() {
    ArrayList<Point> points = new ArrayList<>(flattenLatLngCoordinates.length / 2);
    for (int i = 0; i < flattenLatLngCoordinates.length / 2; i++) {
      double[] coordinates;
      if (altitudes != null && !Double.isNaN(altitudes[i])) {
        coordinates = new double[]{flattenLatLngCoordinates[i * 2], flattenLatLngCoordinates[(i * 2) + 1], altitudes[i]};
      } else {
        coordinates = new double[]{flattenLatLngCoordinates[i * 2], flattenLatLngCoordinates[(i * 2) + 1]};
      }
      BoundingBox pointBbox = null;
      if (coordinatesBoundingBoxes != null) {
        pointBbox = coordinatesBoundingBoxes[i];
      }
      // We create the Point directly instead of static factory method to avoid double coordinate
      // shifting.
      Point point = new Point(Point.TYPE, pointBbox, coordinates);
      points.add(point);
    }
    return points;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof FlattenListOfPoints)) return false;
    FlattenListOfPoints that = (FlattenListOfPoints) o;
    return Objects.deepEquals(flattenLatLngCoordinates, that.flattenLatLngCoordinates) && Objects.deepEquals(altitudes, that.altitudes) && Objects.deepEquals(coordinatesBoundingBoxes, that.coordinatesBoundingBoxes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.hashCode(flattenLatLngCoordinates), Arrays.hashCode(altitudes), Arrays.hashCode(coordinatesBoundingBoxes));
  }
}
