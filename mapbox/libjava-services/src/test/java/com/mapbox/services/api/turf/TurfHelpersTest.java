package com.mapbox.services.api.turf;

import com.mapbox.services.api.BaseTest;
import com.mapbox.services.api.utils.turf.TurfConstants;
import com.mapbox.services.api.utils.turf.TurfException;
import com.mapbox.services.api.utils.turf.TurfHelpers;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.startsWith;

public class TurfHelpersTest extends BaseTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void convertDistance_throwNegativeDistanceException() throws TurfException {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Distance must be a"));
    TurfHelpers.convertDistance(-1, TurfConstants.UNIT_MILES);
  }

  @Test
  public void convertDistance_throwInvalidUnitException() throws TurfException {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Invalid unit."));
    TurfHelpers.convertDistance(1, "foo");
  }

  @Test
  public void convertDistance() throws TurfException {
    Assert.assertEquals(1,
      TurfHelpers.convertDistance(1000, TurfConstants.UNIT_METERS), DELTA);
    Assert.assertEquals(0.6213714106386318,
      TurfHelpers.convertDistance(1, TurfConstants.UNIT_KILOMETERS, TurfConstants.UNIT_MILES), DELTA);
    Assert.assertEquals(1.6093434343434343,
      TurfHelpers.convertDistance(1, TurfConstants.UNIT_MILES, TurfConstants.UNIT_KILOMETERS), DELTA);
    Assert.assertEquals(1.851999843075488,
      TurfHelpers.convertDistance(1, TurfConstants.UNIT_NAUTICAL_MILES), DELTA);
    Assert.assertEquals(100,
      TurfHelpers.convertDistance(1, TurfConstants.UNIT_METERS, TurfConstants.UNIT_CENTIMETERS), DELTA);
  }
}
