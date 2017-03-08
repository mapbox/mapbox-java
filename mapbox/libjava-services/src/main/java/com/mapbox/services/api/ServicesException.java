package com.mapbox.services.api;

/**
 * Generic Exception for all things Mapbox, this is used for geocoding, directions, and other APIs
 * in this SDK.
 *
 * @since 1.0.0
 */
public class ServicesException extends RuntimeException {

  /**
   * A form of {@code Throwable} that indicates conditions that a reasonable application might
   * want to catch.
   *
   * @param message the detail message (which is saved for later retrieval by the
   *                {@link #getMessage()} method).
   * @since 1.0.0
   */
  public ServicesException(String message) {
    super(message);
  }
}
