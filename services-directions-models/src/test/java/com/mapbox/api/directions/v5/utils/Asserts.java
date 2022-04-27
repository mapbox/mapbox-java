package com.mapbox.api.directions.v5.utils;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class Asserts {
  public static void assertContains(String string, String substring) {
    assertTrue(
      "String doesn't contain \"" + substring + "/" + ", string: " + string,
      string.contains(substring)
    );
  }

  public static void assertDoesNotContain(String string, String substring) {
    assertFalse(
      "String contains \"" + substring + "/" + ", string: " + string,
      string.contains(substring)
    );
  }

  public static <T> void assertContains(Set<T> set, T object) {
    assertTrue(
      "Set doesn't contain an object " + object.toString() + ", set: " + set.toString(),
      set.contains(object)
    );
  }
}
