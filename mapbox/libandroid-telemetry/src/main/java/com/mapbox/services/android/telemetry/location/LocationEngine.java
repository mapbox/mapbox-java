package com.mapbox.services.android.telemetry.location;

import android.location.Location;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * In part inspired by the LocationSource interface
 * https://developers.google.com/android/reference/com/google/android/gms/maps/LocationSource
 */

public abstract class LocationEngine {

  private static final int TWO_MINUTES = 1000 * 60 * 2;

  protected int priority;
  protected CopyOnWriteArrayList<LocationEngineListener> locationListeners;

  public LocationEngine() {
    locationListeners = new CopyOnWriteArrayList<>();
  }

  public abstract void activate();

  public abstract void deactivate();

  public abstract boolean isConnected();

  public abstract Location getLastLocation();

  public abstract void requestLocationUpdates();

  public abstract void removeLocationUpdates();

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public void addLocationEngineListener(LocationEngineListener listener) {
    if (!this.locationListeners.contains(listener)) {
      this.locationListeners.add(listener);
    }
  }

  public boolean removeLocationEngineListener(LocationEngineListener listener) {
    return this.locationListeners.remove(listener);
  }

  /**
   * Determines whether one Location reading is better than the current Location fix
   * https://developer.android.com/guide/topics/location/strategies.html#BestEstimate
   *
   * @param location  The new Location that you want to evaluate
   * @param currentBestLocation  The current Location fix, to which you want to compare the new one
   */
  protected static boolean isBetterLocation(Location location, Location currentBestLocation) {
    if (currentBestLocation == null) {
      // A new location is always better than no location
      return true;
    }

    // Check whether the new location fix is newer or older
    long timeDelta = location.getTime() - currentBestLocation.getTime();
    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
    boolean isNewer = timeDelta > 0;

    if (isSignificantlyNewer) {
      // If it's been more than two minutes since the current location, use the new location
      // because the user has likely moved
      return true;
    } else if (isSignificantlyOlder) {
      // If the new location is more than two minutes older, it must be worse
      return false;
    }

    // Check whether the new location fix is more or less accurate
    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
    boolean isLessAccurate = accuracyDelta > 0;
    boolean isMoreAccurate = accuracyDelta < 0;
    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

    // Check if the old and new location are from the same provider
    boolean isFromSameProvider = isSameProvider(location.getProvider(),
      currentBestLocation.getProvider());

    // Determine location quality using a combination of timeliness and accuracy
    if (isMoreAccurate) {
      return true;
    } else if (isNewer && !isLessAccurate) {
      return true;
    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
      return true;
    }

    return false;
  }

  /**
   * Checks whether two providers are the same
   * https://developer.android.com/guide/topics/location/strategies.html#BestEstimate
   * */
  public static boolean isSameProvider(String provider1, String provider2) {
    if (provider1 == null) {
      return provider2 == null;
    }

    return provider1.equals(provider2);
  }

}
