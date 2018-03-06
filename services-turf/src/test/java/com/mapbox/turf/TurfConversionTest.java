package com.mapbox.turf;

import com.mapbox.core.TestUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class TurfConversionTest extends TestUtils {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void radiansToDistance() throws Exception {
    assertEquals(
      1, TurfConversion.radiansToLength(1, TurfConstants.UNIT_RADIANS), DELTA);
    assertEquals(
      6373, TurfConversion.radiansToLength(1, TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(
      3960, TurfConversion.radiansToLength(1, TurfConstants.UNIT_MILES), DELTA);
  }

  @Test
  public void distanceToRadians() throws Exception {
    assertEquals(
      1, TurfConversion.lengthToRadians(1, TurfConstants.UNIT_RADIANS), DELTA);
    assertEquals(
      1, TurfConversion.lengthToRadians(6373, TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(
      1, TurfConversion.lengthToRadians(3960, TurfConstants.UNIT_MILES), DELTA);
  }

  @Test
  public void distanceToDegrees() throws Exception {
    assertEquals(
      57.29577951308232, TurfConversion.lengthToDegrees(1, TurfConstants.UNIT_RADIANS), DELTA);
    assertEquals(
      0.8990393772647469, TurfConversion.lengthToDegrees(100,
        TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(
      0.14468631190172304, TurfConversion.lengthToDegrees(10, TurfConstants.UNIT_MILES), DELTA);
  }

  @Test
  public void convertDistance() throws TurfException {
    assertEquals(1,
      TurfConversion.convertLength(1000, TurfConstants.UNIT_METERS), DELTA);
    assertEquals(0.6213714106386318,
      TurfConversion.convertLength(1, TurfConstants.UNIT_KILOMETERS,
        TurfConstants.UNIT_MILES), DELTA);
    assertEquals(1.6093434343434343,
      TurfConversion.convertLength(1, TurfConstants.UNIT_MILES,
        TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(1.851999843075488,
      TurfConversion.convertLength(1, TurfConstants.UNIT_NAUTICAL_MILES), DELTA);
    assertEquals(100,
      TurfConversion.convertLength(1, TurfConstants.UNIT_METERS,
        TurfConstants.UNIT_CENTIMETERS), DELTA);
  }
}
