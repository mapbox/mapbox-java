package com.mapbox.turf;

import com.mapbox.services.TestUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.mapbox.turf.TurfConstants.UNIT_KILOMETERS;
import static com.mapbox.turf.TurfConstants.UNIT_MILES;
import static com.mapbox.turf.TurfConstants.UNIT_RADIANS;
import static org.junit.Assert.assertEquals;

public class TurfConversionTest extends TestUtils {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void radiansToDistance() throws Exception {
    assertEquals(
      1, TurfConversion.radiansToDistance(1, UNIT_RADIANS), DELTA);
    assertEquals(
      6373, TurfConversion.radiansToDistance(1, UNIT_KILOMETERS), DELTA);
    assertEquals(
      3960, TurfConversion.radiansToDistance(1, UNIT_MILES), DELTA);
  }

  @Test
  public void distanceToRadians() throws Exception {
    assertEquals(
      1, TurfConversion.distanceToRadians(1, UNIT_RADIANS), DELTA);
    assertEquals(
      1, TurfConversion.distanceToRadians(6373, UNIT_KILOMETERS), DELTA);
    assertEquals(
      1, TurfConversion.distanceToRadians(3960, UNIT_MILES), DELTA);
  }

  @Test
  public void distanceToDegrees() throws Exception {
    assertEquals(
      57.29577951308232, TurfConversion.distanceToDegrees(1, UNIT_RADIANS), DELTA);
    assertEquals(
      0.8990393772647469, TurfConversion.distanceToDegrees(100,
        UNIT_KILOMETERS), DELTA);
    assertEquals(
      0.14468631190172304, TurfConversion.distanceToDegrees(10, UNIT_MILES), DELTA);
  }

  @Test
  public void convertDistance() throws TurfException {
    assertEquals(1,
      TurfConversion.convertDistance(1000, TurfConstants.UNIT_METERS), DELTA);
    assertEquals(0.6213714106386318,
      TurfConversion.convertDistance(1, UNIT_KILOMETERS,
        UNIT_MILES), DELTA);
    assertEquals(1.6093434343434343,
      TurfConversion.convertDistance(1, UNIT_MILES,
        UNIT_KILOMETERS), DELTA);
    assertEquals(1.851999843075488,
      TurfConversion.convertDistance(1, TurfConstants.UNIT_NAUTICAL_MILES), DELTA);
    assertEquals(100,
      TurfConversion.convertDistance(1, TurfConstants.UNIT_METERS,
        TurfConstants.UNIT_CENTIMETERS), DELTA);
  }
}
