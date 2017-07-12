package com.mapbox.services.android.telemetry.location;


import android.location.Location;

class MockLocationSource extends LocationEngine {
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
