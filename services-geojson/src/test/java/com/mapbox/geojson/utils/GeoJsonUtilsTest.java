package com.mapbox.geojson.utils;

import static org.junit.Assert.assertEquals;

import com.mapbox.core.TestUtils;
import org.junit.Test;


public class GeoJsonUtilsTest extends TestUtils {


  @Test
  public void trimPositiveRoundUp() {
    double trimmedValue = GeoJsonUtils.trim(3.123456789);
    double expected = 3.1234568;
    assertEquals("trim to 7 digits after period", expected, trimmedValue, 1e-8);
  }

  @Test
  public void trimPositiveRoundDown() {
    double trimmedValue = GeoJsonUtils.trim(3.123456712);
    double expected = 3.1234567;
    assertEquals("trim to 7 digits after period", expected, trimmedValue, 1e-8);
  }

  @Test
  public void trimNegative() {
    double trimmedValue = GeoJsonUtils.trim(-3.123456789);
    double expected = -3.1234568;
    assertEquals("trim to 7 digits after period", expected, trimmedValue, 1e-8);
  }

  @Test
  public void trimZero() {
    double trimmedValue = GeoJsonUtils.trim(0);
    double expected = 0;
    assertEquals("trim to 7 digits after period", expected, trimmedValue, 1e-8);
  }

  @Test
  public void trimInt() {
    double trimmedValue = GeoJsonUtils.trim(8);
    double expected = 8;
    assertEquals("trim to 7 digits after period", expected, trimmedValue, 1e-8);
  }

  @Test
  public void trimMaxLong() {
    double trimmedValue = GeoJsonUtils.trim(Long.MAX_VALUE + 0.1);
    double expected = Long.MAX_VALUE;
    assertEquals("trim to 7 digits after period", expected, trimmedValue, 1e-8);
  }
}