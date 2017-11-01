package com.mapbox.services.android;

import android.location.Location;

import com.mapbox.services.android.core.location.LocationEngineListener;
import com.mapbox.services.android.location.MockLocationEngine;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MockLocationEngineTest extends BaseTest {

  @Test
  public void sanityTest() {
    MockLocationEngine mockLocationEngine = new MockLocationEngine(1000, 30, true);
    Assert.assertNotNull("should not be null", mockLocationEngine);
    Assert.assertEquals(30, mockLocationEngine.getSpeed());
    Assert.assertEquals(1000, mockLocationEngine.getDelay());
    Assert.assertTrue(mockLocationEngine.isNoisyGps());
  }

  @Test
  public void activate_onConnectCalled() {
    MockLocationEngine mockLocationEngine = new MockLocationEngine();

    LocationEngineListener listener = Mockito.mock(LocationEngineListener.class);
    mockLocationEngine.addLocationEngineListener(listener);
    mockLocationEngine.activate();

    Mockito.verify(listener, Mockito.only()).onConnected();
    Mockito.verify(listener, Mockito.times(1));
  }

  @Test
  public void getLastLocation_returnsNullWhenNotSet() {
    MockLocationEngine mockLocationEngine = new MockLocationEngine();
    Location lastLocation = mockLocationEngine.getLastLocation();
    Assert.assertNull(lastLocation);
  }
}