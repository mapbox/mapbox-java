package com.mapbox.turf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.mapbox.turf.TurfConstants.UNIT_KILOMETERS;
import static com.mapbox.turf.TurfConstants.UNIT_MILES;
import static com.mapbox.turf.TurfConstants.UNIT_RADIANS;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;

public class TurfHelpersTest extends BaseTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void radiansToDistance() throws Exception {
    assertEquals(
      1, TurfHelpers.radiansToDistance(1, UNIT_RADIANS), DELTA);
    assertEquals(
      6373, TurfHelpers.radiansToDistance(1, UNIT_KILOMETERS), DELTA);
    assertEquals(
      3960, TurfHelpers.radiansToDistance(1, UNIT_MILES), DELTA);
  }

  @Test
  public void distanceToRadians() throws Exception {
    assertEquals(
      1, TurfHelpers.distanceToRadians(1, UNIT_RADIANS), DELTA);
    assertEquals(
      1, TurfHelpers.distanceToRadians(6373, UNIT_KILOMETERS), DELTA);
    assertEquals(
      1, TurfHelpers.distanceToRadians(3960, UNIT_MILES), DELTA);
  }

  @Test
  public void distanceToDegrees() throws Exception {
    assertEquals(
      57.29577951308232, TurfHelpers.distanceToDegrees(1, UNIT_RADIANS), DELTA);
    assertEquals(
      0.8990393772647469, TurfHelpers.distanceToDegrees(100,
        UNIT_KILOMETERS), DELTA);
    assertEquals(
      0.14468631190172304, TurfHelpers.distanceToDegrees(10, UNIT_MILES), DELTA);
  }

  @Test
  public void convertDistance() throws TurfException {
    assertEquals(1,
      TurfHelpers.convertDistance(1000, TurfConstants.UNIT_METERS), DELTA);
    assertEquals(0.6213714106386318,
      TurfHelpers.convertDistance(1, UNIT_KILOMETERS,
        UNIT_MILES), DELTA);
    assertEquals(1.6093434343434343,
      TurfHelpers.convertDistance(1, UNIT_MILES,
        UNIT_KILOMETERS), DELTA);
    assertEquals(1.851999843075488,
      TurfHelpers.convertDistance(1, TurfConstants.UNIT_NAUTICAL_MILES), DELTA);
    assertEquals(100,
      TurfHelpers.convertDistance(1, TurfConstants.UNIT_METERS,
        TurfConstants.UNIT_CENTIMETERS), DELTA);
  }
}
