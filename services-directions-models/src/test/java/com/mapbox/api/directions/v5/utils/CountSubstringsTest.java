package com.mapbox.api.directions.v5.utils;

import org.junit.Test;

import static com.mapbox.api.directions.v5.utils.StringUtil.countSubstrings;
import static org.junit.Assert.assertEquals;

public class CountSubstringsTest {

  @Test
  public void zeroSubstrings() {
    int result = countSubstrings("test", "zero");
    assertEquals(0, result);
  }

  @Test
  public void oneSubstring() {
    int result = countSubstrings("test-one-substring", "one");
    assertEquals(1, result);
  }

  @Test
  public void twoSubstring() {
    int result = countSubstrings("twotest-two-substring", "two");
    assertEquals(2, result);
  }

  @Test
  public void substringsAtTheEnd() {
    int result = countSubstrings("testheyheyhey", "hey");
    assertEquals(3, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptySubstring() {
    countSubstrings("test", "");
  }


  @Test(expected = IllegalArgumentException.class)
  public void emptyString() {
    countSubstrings("", "test");
  }
}