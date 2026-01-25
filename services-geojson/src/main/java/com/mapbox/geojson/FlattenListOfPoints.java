package com.mapbox.geojson;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A class that contains the required data to store a list of {@link Point}s as a flat structure.
 */
@Keep
public class FlattenListOfPoints implements Serializable {
  /**
   * A one-dimensional array to store the flattened coordinates: [lat1, lng1, lat2, lng2, ...].
   * <p>
   * Note: we use one-dimensional array for performance reasons related to JNI access (
   * <a href="https://developer.android.com/ndk/guides/jni-tips#primitive-arrays">Android JNI Tips
   * - Primitive arrays</a>)
   */
  @NonNull
  private final double[] flattenLatLngPoints;
  /**
   * An array to store the altitudes of each coordinate or {@link Double#NaN} if the coordinate
   * does not have altitude.
   */
  @Nullable
  private final double[] altitudes;
  /**
   * An array to store the {@link BoundingBox} of each coordinate or null if the coordinate does
   * not have bounding box.
   */
  @Nullable
  private BoundingBox[] boundingBoxes;

  FlattenListOfPoints(List<Point> points) {
    if (points.isEmpty()) {
      this.flattenLatLngPoints = new double[0];
      this.altitudes = null;
      this.boundingBoxes = null;
      return;
    }
    double[] flattenLatLngCoordinates = new double[points.size() * 2];
    double[] altitudes = null;
    for (int i = 0; i < points.size(); i++) {
      Point point = points.get(i);
      flattenLatLngCoordinates[i * 2] = point.longitude();
      flattenLatLngCoordinates[(i * 2) + 1] = point.latitude();

      // It is quite common to not have altitude in Point. Therefore only if we have points
      // with altitude then we create an array to store those.
      if (point.hasAltitude()) {
        // If one point has altitude we create an array of double to store the altitudes.
        if (altitudes == null) {
          altitudes = new double[points.size()];
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
        if (boundingBoxes == null) {
          boundingBoxes = new BoundingBox[points.size()];
        }
        boundingBoxes[i] = point.bbox();
      }
    }
    this.flattenLatLngPoints = flattenLatLngCoordinates;
    this.altitudes = altitudes;
  }

  /**
   * @return a flatten array of all the coordinates (lat, lng): [lat1, lng1, lat2, lng2, ...].
   */
  @NonNull
  public double[] getFlattenLatLngArray() {
    return flattenLatLngPoints;
  }

  /**
   * @return an array of all the altitudes (or null if no altitudes are present at all). If a
   * coordinate does not contain altitude it's represented as {@link Double#NaN}
   */
  @Nullable
  public double[] getAltitudes() {
    return altitudes;
  }

  /**
   * Creates a list of {@link Point}s and returns it.
   * </p>
   * If possible consider using {@link #getFlattenLatLngArray()} and {@link #getAltitudes()} instead.
   *
   * @return a list of {@link Point}s
   */
  @NonNull
  public List<Point> points() {
    if (flattenLatLngPoints.length == 0) {
      return new ArrayList<>();
    }
    ArrayList<Point> points = new ArrayList<>(flattenLatLngPoints.length / 2);
    for (int i = 0; i < flattenLatLngPoints.length / 2; i++) {
      double[] coordinates;
      if (altitudes != null && !Double.isNaN(altitudes[i])) {
        coordinates = new double[]{flattenLatLngPoints[i * 2], flattenLatLngPoints[(i * 2) + 1], altitudes[i]};
      } else {
        coordinates = new double[]{flattenLatLngPoints[i * 2], flattenLatLngPoints[(i * 2) + 1]};
      }
      BoundingBox pointBbox = null;
      if (boundingBoxes != null) {
        pointBbox = boundingBoxes[i];
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
    return Objects.deepEquals(flattenLatLngPoints, that.flattenLatLngPoints) && Objects.deepEquals(altitudes, that.altitudes) && Objects.deepEquals(boundingBoxes, that.boundingBoxes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.hashCode(flattenLatLngPoints), Arrays.hashCode(altitudes), Arrays.hashCode(boundingBoxes));
  }
}
