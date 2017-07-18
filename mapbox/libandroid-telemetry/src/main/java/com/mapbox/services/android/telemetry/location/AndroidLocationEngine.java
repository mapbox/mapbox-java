package com.mapbox.services.android.telemetry.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.mapbox.services.android.telemetry.permissions.PermissionsManager;

import java.lang.ref.WeakReference;

/**
 * A location engine that uses core android.location and has no external dependencies
 * https://developer.android.com/guide/topics/location/strategies.html
 */
class AndroidLocationEngine extends LocationEngine implements LocationListener {

  private static final String LOG_TAG = AndroidLocationEngine.class.getSimpleName();

  private static final String DEFAULT_PROVIDER = LocationManager.PASSIVE_PROVIDER;
  private static final long DEFAULT_MIN_TIME = 0;
  private static final float DEFAULT_MIN_DISTANCE = 0;

  private static AndroidLocationEngine instance;

  private WeakReference<Context> context;
  private LocationManager locationManager;
  private String currentProvider = null;

  private AndroidLocationEngine(Context context) {
    super();

    Log.v(LOG_TAG, "Initializing.");
    this.context = new WeakReference<>(context);
    locationManager = (LocationManager) this.context.get().getSystemService(Context.LOCATION_SERVICE);
    currentProvider = DEFAULT_PROVIDER;

  }

  static synchronized LocationEngine getLocationEngine(Context context) {
    if (instance == null) {
      instance = new AndroidLocationEngine(context.getApplicationContext());
    }

    return instance;
  }

  @Override
  public void activate() {
    // "Connection" is immediate
    Log.v(LOG_TAG, "Activating.");
    for (LocationEngineListener listener : locationListeners) {
      listener.onConnected();
    }
  }

  @Override
  public void deactivate() {
    // No op
    Log.v(LOG_TAG, "Deactivating.");
  }

  @Override
  public boolean isConnected() {
    return true;
  }

  @Override
  public Location getLastLocation() {
    if (!TextUtils.isEmpty(currentProvider)) {
      //noinspection MissingPermission
      return locationManager.getLastKnownLocation(currentProvider);
    }

    return null;
  }

  @Override
  public void requestLocationUpdates() {
    if (!TextUtils.isEmpty(currentProvider)) {
      //noinspection MissingPermission
      locationManager.requestLocationUpdates(currentProvider, DEFAULT_MIN_TIME, DEFAULT_MIN_DISTANCE, this);
    }
  }

  @Override
  public void setPriority(int priority) {
    super.setPriority(priority);
    updateCurrentProvider();
  }

  private void updateCurrentProvider() {
    // We might want to explore android.location.Criteria here.
    if (priority == LocationEnginePriority.NO_POWER) {
      currentProvider = LocationManager.PASSIVE_PROVIDER;
    } else if (priority == LocationEnginePriority.LOW_POWER) {
      currentProvider = LocationManager.NETWORK_PROVIDER;
    } else if (priority == LocationEnginePriority.BALANCED_POWER_ACCURACY) {
      currentProvider = LocationManager.NETWORK_PROVIDER;
    } else if (priority == LocationEnginePriority.HIGH_ACCURACY) {
      currentProvider = LocationManager.GPS_PROVIDER;
    }

    Log.d(LOG_TAG, String.format("Priority set to %d (current provider is %s).", priority, currentProvider));
  }

  @Override
  public void removeLocationUpdates() {
    if (PermissionsManager.areLocationPermissionsGranted(context.get())) {
      //noinspection MissingPermission
      locationManager.removeUpdates(this);
    }
  }

  /**
   * Called when the location has changed.
   */
  @Override
  public void onLocationChanged(Location location) {
    Log.v(LOG_TAG, "New location received.");
    for (LocationEngineListener listener : locationListeners) {
      listener.onLocationChanged(location);
    }
  }

  /**
   * Called when the provider status changes.
   */
  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    Log.v(LOG_TAG, String.format("Provider %s status changed to %d (current provider is %s).",
      provider, status, currentProvider));
  }

  /**
   * Called when the provider is enabled by the user.
   */
  @Override
  public void onProviderEnabled(String provider) {
    Log.v(LOG_TAG, String.format("Provider %s was enabled (current provider is %s).", provider, currentProvider));
  }

  /**
   * Called when the provider is disabled by the user.
   */
  @Override
  public void onProviderDisabled(String provider) {
    Log.v(LOG_TAG, String.format("Provider %s was disabled (current provider is %s).", provider, currentProvider));
  }
}
