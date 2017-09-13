package com.mapbox.services.android.telemetry.location;

import android.location.Location;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LocationEngineTest {

  @Test
  public void testReflexive() {
    TestLocationEngine testLocationEngine = new TestLocationEngine();
    EqualsTester.testReflexive(testLocationEngine);
  }

  @Test
  public void testSymmetric() {
    TestLocationEngine testLocationEngineA = new TestLocationEngine();
    TestLocationEngine testLocationEngineB = new TestLocationEngine();
    EqualsTester.testSymmetric(testLocationEngineA, testLocationEngineB);
  }

  @Test
  public void testTransitive() {
    TestLocationEngine TestLocationEngineA = new TestLocationEngine();
    TestLocationEngine TestLocationEngineB = new TestLocationEngine();
    TestLocationEngine TestLocationEngineC = new TestLocationEngine();
    EqualsTester.testTransitive(TestLocationEngineA, TestLocationEngineB, TestLocationEngineC);
  }

  @Test
  public void testNonNullity() {
    TestLocationEngine testLocationEngine = new TestLocationEngine();
    EqualsTester.testNonNullity(testLocationEngine);
  }

  @Test
  public void testDifferent() {
    TestLocationEngine testLocationEngineA = new TestLocationEngine();
    TestLocationEngine testLocationEngineB = new TestLocationEngine(1, 500, 500, 3);
    EqualsTester.testDifferent(testLocationEngineA, testLocationEngineB);
  }

  static class TestLocationEngine extends LocationEngine {

    TestLocationEngine() {
      super();
    }

    TestLocationEngine(int priority, int interval,
                       int fastestInterval, float smallestDisplacement) {
      super();
      setPriority(priority);
      setInterval(interval);
      setFastestInterval(fastestInterval);
      setSmallestDisplacement(smallestDisplacement);
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public boolean isConnected() {
      return false;
    }

    @Override
    public Location getLastLocation() {
      return null;
    }

    @Override
    public void requestLocationUpdates() {

    }

    @Override
    public void removeLocationUpdates() {

    }
  }
}