package com.mapbox.services.android.testapp.location;

import android.location.Location;
import android.os.Handler;

import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;

/**
 * Sample LocationEngine that provides mocked locations simulating GPS updates
 */
public class MockLocationEngine extends LocationEngine {

  // Mocked data
  private static final int UPDATE_INTERVAL_MS = 1000;
  private static final double[][] locations = new double[][] {
    new double[] {38.909620, -77.043410},
    new double[] {38.909621, -77.043411},
    new double[] {38.909622, -77.043412},
    new double[] {38.909623, -77.043413},
    new double[] {38.909624, -77.043414}};

  private Handler handler;
  int currentIndex;

  public MockLocationEngine() {
    super();
  }

  @Override
  public void activate() {
    currentIndex = 0;

    // "Connection" is immediate here
    for (LocationEngineListener listener : locationListeners) {
      listener.onConnected();
    }
  }

  @Override
  public void deactivate() {
    handler = null;
  }

  @Override
  public boolean isConnected() {
    return true; // Always connected
  }

  @Override
  public Location getLastLocation() {
    return getNextLocation();
  }

  @Override
  public void requestLocationUpdates() {
    // Fake regular updates with a handler
    handler = new Handler();
    handler.postDelayed(new LocationUpdateRunnable(), UPDATE_INTERVAL_MS);
  }

  @Override
  public void removeLocationUpdates() {
    handler.removeCallbacksAndMessages(null);
  }

  private Location getNextLocation() {
    // Build the next location and rotate the index
    Location location = new Location(MockLocationEngine.class.getSimpleName());
    location.setLatitude(locations[currentIndex][0]);
    location.setLongitude(locations[currentIndex][1]);
    currentIndex = (currentIndex == locations.length - 1 ? 0 : currentIndex + 1);
    return location;
  }

  private class LocationUpdateRunnable implements Runnable {
    @Override
    public void run() {
      // Notify of an update
      Location location = getNextLocation();
      for (LocationEngineListener listener : locationListeners) {
        listener.onLocationChanged(location);
      }

      // Schedule the next update
      handler.postDelayed(new LocationUpdateRunnable(), UPDATE_INTERVAL_MS);
    }
  }
}
