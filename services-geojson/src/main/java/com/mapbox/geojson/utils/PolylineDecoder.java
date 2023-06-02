package com.mapbox.geojson.utils;

import androidx.annotation.Nullable;
import com.mapbox.geojson.Point;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Decodes an encoded path string as an iterator of {@link Point}.
 * This is a memory efficient version of {@link PolylineUtils#decode}.
 *
 * @see <a href="https://github.com/mapbox/polyline/blob/master/src/polyline.js">Part of algorithm came from this source</a>
 * @see <a href="https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/PolyUtil.java">Part of algorithm came from this source.</a>
 * @since 6.10.0
 */
public class PolylineDecoder implements Iterator<Point>, Closeable {

  private final InputStream inputStream;

  // OSRM uses precision=6, the default Polyline spec divides by 1E5, capping at precision=5
  private final double factor;

  // For speed we preallocate to an upper bound on the final length, then
  // truncate the array before returning.
  private int lat = 0;
  private int lng = 0;
  private int data = -1;
  private Point current;

  /**
   * Decodes an encoded input stream into a sequence of {@link Point}.
   *
   * @param inputStream InputStream that reads a String as bytes
   * @param precision   OSRMv4 uses 6, OSRMv5 and Google uses 5
   */
  public PolylineDecoder(InputStream inputStream, int precision) {
    this.inputStream = inputStream;
    this.factor = Math.pow(10, precision);
    loadNext();
  }

  /**
   * Returns the current [Point] for the iterator. Every call to [next] will update the [current].
   */
  @Nullable
  public Point getCurrent() {
    return current;
  }

  /**
   * Returns true if the geometry has more points.
   */
  @Override
  public boolean hasNext() {
    return data != -1;
  }

  /**
   * Returns the next point in the geometry.
   *
   * @throws NoSuchElementException if the geometry has no more points
   */
  @Override
  public Point next() throws NoSuchElementException {
    if (!hasNext()) {
      throw new NoSuchElementException("Next element is not available when hasNext is false.");
    }

    int result = 1;
    int shift = 0;
    int temp;
    do {
      temp = data - 63 - 1;
      loadNext();
      result += temp << shift;
      shift += 5;
    }
    while (temp >= 0x1f);
    lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

    result = 1;
    shift = 0;
    do {
      temp = data - 63 - 1;
      loadNext();
      result += temp << shift;
      shift += 5;
    }
    while (temp >= 0x1f);
    lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

    Point next = Point.fromLngLat(lng / factor, lat / factor);
    current = next;
    return next;
  }

  @Override
  public void close() {
    try {
      inputStream.close();
    } catch (IOException exception) {
      // Safe close
    }
  }

  private void loadNext() throws RuntimeException {
    try {
      this.data = inputStream.read();
    } catch (IOException exception) {
      this.data = -1;
      throw new RuntimeException("Failed to read the encoded path", exception);
    }
  }
}
