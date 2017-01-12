package com.mapbox.services.android.telemetry.location;

import android.location.Location;

/**
 * In part inspired by the LocationSource interface
 *
 * https://developers.google.com/android/reference/com/google/android/gms/maps/LocationSource
 */

public interface LocationEngine {

  void activate();

  void deactivate();

  boolean isConnected();

  int getPriority();

  void setPriority(int priority);

  Location getLastLocation();

  void addLocationEngineListener(LocationEngineListener listener);

  boolean removeLocationEngineListener(LocationEngineListener listener);

  void requestLocationUpdates();

  void removeLocationUpdates();

}
