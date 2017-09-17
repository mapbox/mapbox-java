package com.mapbox.geojson.exception;

/**
 * A form of {@code Throwable} that indicates an issue occurred during a GeoJSON operation.
 *
 * @since 3.0.0
 */
public class GeoJsonException extends RuntimeException {

  /**
   * A form of {@code Throwable} that indicates an issue occurred during a GeoJSON operation.
   *
   * @param message the detail message (which is saved for later retrieval by the
   *                {@link #getMessage()} method)
   * @since 3.0.0
   */
  public GeoJsonException(String message) {
    super(message);
  }
}
