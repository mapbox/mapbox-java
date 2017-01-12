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

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Sample LocationEngine using the Open Source Lost library
 */
public class LostLocationEngine implements
  LocationEngine, LostApiClient.ConnectionCallbacks, LocationListener {

  private static final String LOG_TAG = LostLocationEngine.class.getSimpleName();

  private static LocationEngine instance;

  private Context context;
  private CopyOnWriteArrayList<LocationEngineListener> locationListeners;
  private LostApiClient lostApiClient;

  private int priority;

  public LostLocationEngine(Context context) {
    this.context = context;
    locationListeners = new CopyOnWriteArrayList<>();
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
    for (LocationEngineListener listener : this.locationListeners) {
      listener.onConnected();
    }
  }

  @Override
  public void onConnectionSuspended() {
    Log.d(LOG_TAG, "Connection suspended.");
  }

  @Override
  public int getPriority() {
    return priority;
  }

  @Override
  public void setPriority(int priority) {
    this.priority = priority;
  }

  @Override
  public Location getLastLocation() {
    if (lostApiClient.isConnected()
      && PermissionsManager.isPermissionGranted(context, PermissionsManager.FINE_LOCATION_PERMISSION)) {
      //noinspection MissingPermission
      return LocationServices.FusedLocationApi.getLastLocation(lostApiClient);
    }

    return null;
  }

  @Override
  public void addLocationEngineListener(LocationEngineListener listener) {
    if (!this.locationListeners.contains(listener)) {
      this.locationListeners.add(listener);
    }
  }

  @Override
  public boolean removeLocationEngineListener(LocationEngineListener listener) {
    return this.locationListeners.remove(listener);
  }

  @Override
  public void requestLocationUpdates() {
    // Common params
    LocationRequest request = LocationRequest.create()
      .setFastestInterval(1000)
      .setSmallestDisplacement(3.0f);

    // Priority matching is straightforward
    if (priority == LocationEnginePriority.PRIORITY_NO_POWER) {
      request.setPriority(LocationRequest.PRIORITY_NO_POWER);
    } else if (priority == LocationEnginePriority.PRIORITY_LOW_POWER) {
      request.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    } else if (priority == LocationEnginePriority.PRIORITY_BALANCED_POWER_ACCURACY) {
      request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    } else if (priority == LocationEnginePriority.PRIORITY_HIGH_ACCURACY) {
      request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    //noinspection MissingPermission
    LocationServices.FusedLocationApi.requestLocationUpdates(lostApiClient, request, this);
  }

  @Override
  public void removeLocationUpdates() {
    LocationServices.FusedLocationApi.removeLocationUpdates(lostApiClient, this);
  }

  @Override
  public void onLocationChanged(Location location) {
    for (LocationEngineListener listener : this.locationListeners) {
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
