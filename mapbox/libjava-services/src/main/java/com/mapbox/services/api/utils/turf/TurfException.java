package com.mapbox.services.api.utils.turf;

/**
 * This indicates conditions that a reasonable application might want to catch.
 * <p>
 * A {@link RuntimeException} specific to Turf calculation errors and is thrown whenever either an
 * unintended event occurs or the data passed into the method isn't sufficient enough to perform the
 * calculation.
 * </p>
 *
 * @see <a href="http://turfjs.org/docs/">Turfjs documentation</a>
 * @since 1.2.0
 */
public class TurfException extends RuntimeException {

  /**
   * A form of {@link RuntimeException} that indicates conditions that a reasonable application
   * might want to catch.
   *
   * @param message the detail message (which is saved for later retrieval by the
   *                {@link #getMessage()} method).
   * @since 1.2.0
   */
  public TurfException(String message) {
    super(message);
  }
}
