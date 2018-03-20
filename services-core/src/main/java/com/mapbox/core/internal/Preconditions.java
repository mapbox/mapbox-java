package com.mapbox.core.internal;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.support.annotation.RestrictTo;

/**
 * Contains simple precondition checks.
 *
 * @since 3.0.0
 */
@RestrictTo(LIBRARY_GROUP)
public final class Preconditions {

  /**
   * Checks if the passed in value is not Null. Throws a NPE if the value is null.
   *
   * @param value The object to be checked fo null
   * @param message The message to be associated with NPE, if value is null
   * @since 3.0.0
   */
  public static void checkNotNull(Object value, String message) {
    if (value == null) {
      throw new NullPointerException(message);
    }
  }

  private Preconditions() {
    throw new AssertionError("No instances.");
  }
}
