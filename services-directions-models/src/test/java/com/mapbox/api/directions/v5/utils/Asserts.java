package com.mapbox.api.directions.v5.utils;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static com.mapbox.api.directions.v5.utils.StringUtil.countSubstrings;
import static org.junit.Assert.assertEquals;
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

  public static void assertContainsExactCount(String string, String substring, int count) {
    int substringsFound = countSubstrings(string, substring);
    assertEquals(
      "String \"" + string + "\" contains " +
        substringsFound + " \"" + substring + "\"",
      substringsFound,
      count
    );
  }

  /***
   * Verifies that URL doesn't contain duplicated query parameters.
   * Examples:
   * https://api.mapbox.com/test?a=1&a=2 - invalid
   * https://api.mapbox.com/test?a=1&b=2 - valid
   * @param url
   */
  public static void assertNoDuplicatedParameters(URL url) {
    String query = url.getQuery();
    Set<String> queryParameters = new HashSet<>();
    for (String param : query.split("&")) {
      String parameterName = param.split("=")[0];
      assertFalse(
        "parameter \"" + parameterName + "\" is duplicated in query response",
        queryParameters.contains(parameterName)
      );
      queryParameters.add(parameterName);
    }
  }
}
