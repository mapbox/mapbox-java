package com.mapbox.services.android.testapp.location;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.mapzen.android.lost.api.LocationListener;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.api.LocationServices;
import com.mapzen.android.lost.api.LostApiClient;

/**
 * Sample LocationEngine using the Open Source Lost library
 */
public class LostLocationEngine extends LocationEngine implements
  LostApiClient.ConnectionCallbacks, LocationListener {

  private static final String LOG_TAG = LostLocationEngine.class.getSimpleName();

  private static LocationEngine instance;

  private Context context;
  private LostApiClient lostApiClient;

  public LostLocationEngine(Context context) {
    super();
    this.context = context;
    lostApiClient = new LostApiClient.Builder(context)
      .addConnectionCallbacks(this)
      .build();
  }

  public static synchronized LocationEngine getLocationEngine(Context context) {
    if (instance == null) {
      instance = new LostLocationEngine(context.getApplicationContext());
    }

    return instance;
  }

  @Override
  public void activate() {
    if (lostApiClient != null && !lostApiClient.isConnected()) {
      lostApiClient.connect();
    }
  }

  @Override
  public void deactivate() {
    if (lostApiClient != null && lostApiClient.isConnected()) {
      lostApiClient.disconnect();
    }
  }

  @Override
  public boolean isConnected() {
    return lostApiClient.isConnected();
  }

  @Override
  public void onConnected() {
    for (LocationEngineListener listener : locationListeners) {
      listener.onConnected();
    }
  }

  @Override
  public void onConnectionSuspended() {
    Log.d(LOG_TAG, "Connection suspended.");
  }

  @Override
  public Location getLastLocation() {
    if (lostApiClient.isConnected() && PermissionsManager.areLocationPermissionsGranted(context)) {
      //noinspection MissingPermission
      return LocationServices.FusedLocationApi.getLastLocation(lostApiClient);
    }

    return null;
  }

  @Override
  public void requestLocationUpdates() {
    // Common params
    LocationRequest request = LocationRequest.create()
      .setFastestInterval(1000)
      .setSmallestDisplacement(3.0f);

    // Priority matching is straightforward
    if (priority == LocationEnginePriority.NO_POWER) {
      request.setPriority(LocationRequest.PRIORITY_NO_POWER);
    } else if (priority == LocationEnginePriority.LOW_POWER) {
      request.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    } else if (priority == LocationEnginePriority.BALANCED_POWER_ACCURACY) {
      request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    } else if (priority == LocationEnginePriority.HIGH_ACCURACY) {
      request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    if (lostApiClient.isConnected() && PermissionsManager.areLocationPermissionsGranted(context)) {
      //noinspection MissingPermission
      LocationServices.FusedLocationApi.requestLocationUpdates(lostApiClient, request, this);
    }
  }

  @Override
  public void removeLocationUpdates() {
    LocationServices.FusedLocationApi.removeLocationUpdates(lostApiClient, this);
  }

  @Override
  public void onLocationChanged(Location location) {
    for (LocationEngineListener listener : locationListeners) {
      listener.onLocationChanged(location);
    }
  }

  @Override
  public void onProviderDisabled(String provider) {
    Log.d(LOG_TAG, "Provider disabled: " + provider);
  }

  @Override
  public void onProviderEnabled(String provider) {
    Log.d(LOG_TAG, "Provider enabled: " + provider);
  }
}
