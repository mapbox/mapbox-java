package com.mapbox.api.directions.v5.utils;

public final class StringUtil {
  /***
   * Counts how many times the substring is present in the string.
   * See examples in {@link CountSubstringsTest}
   * @param string
   * @param substring
   * @return
   */
  public static int countSubstrings(String string, String substring) {
    if (substring == null || substring.isEmpty()) {
      throw new IllegalArgumentException("substring can't be null or empty");
    }
    if (string == null || string.isEmpty()) {
      throw new IllegalArgumentException("string can't be null or empty");
    }
    int nextIndex = -1;
    int substringsFound = -1;
    do {
      substringsFound++;
      nextIndex = string.indexOf(substring, nextIndex + 1);
    } while (nextIndex != -1);
    return substringsFound;
  }
}
