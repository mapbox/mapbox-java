package com.mapbox.services.android.testapp.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Sample LocationEngine using Google Play Services
 */
public class GoogleLocationEngine implements LocationEngine,
  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

  private static final String LOG_TAG = GoogleLocationEngine.class.getSimpleName();

  private static LocationEngine instance;

  private Context context;
  private CopyOnWriteArrayList<LocationEngineListener> locationListeners;
  private GoogleApiClient googleApiClient;

  private int priority;

  public GoogleLocationEngine(Context context) {
    this.context = context;
    locationListeners = new CopyOnWriteArrayList<>();
    googleApiClient = new GoogleApiClient.Builder(context)
      .addConnectionCallbacks(this)
      .addOnConnectionFailedListener(this)
      .addApi(LocationServices.API)
      .build();
  }

  public static synchronized LocationEngine getLocationEngine(Context context) {
    if (instance == null) {
      instance = new GoogleLocationEngine(context.getApplicationContext());
    }

    return instance;
  }

  @Override
  public void activate() {
    if (googleApiClient != null && !googleApiClient.isConnected()) {
      googleApiClient.connect();
    }
  }

  @Override
  public void deactivate() {
    if (googleApiClient != null && googleApiClient.isConnected()) {
      googleApiClient.disconnect();
    }
  }

  @Override
  public boolean isConnected() {
    return googleApiClient.isConnected();
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    for (LocationEngineListener listener : this.locationListeners) {
      listener.onConnected();
    }
  }

  @Override
  public void onConnectionSuspended(int cause) {
    Log.d(LOG_TAG, "Connection suspended: " + cause);
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Log.d(LOG_TAG, "Connection failed:" + connectionResult.getErrorMessage());
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
    if (googleApiClient.isConnected()
      && PermissionsManager.isPermissionGranted(context, PermissionsManager.FINE_LOCATION_PERMISSION)) {
      //noinspection MissingPermission
      return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
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
    if (priority == LocationEnginePriority.NO_POWER) {
      request.setPriority(LocationRequest.PRIORITY_NO_POWER);
    } else if (priority == LocationEnginePriority.LOW_POWER) {
      request.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    } else if (priority == LocationEnginePriority.BALANCED_POWER_ACCURACY) {
      request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    } else if (priority == LocationEnginePriority.HIGH_ACCURACY) {
      request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    //noinspection MissingPermission
    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, this);
  }

  @Override
  public void removeLocationUpdates() {
    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
  }

  @Override
  public void onLocationChanged(Location location) {
    for (LocationEngineListener listener : this.locationListeners) {
      listener.onLocationChanged(location);
    }
  }
}
