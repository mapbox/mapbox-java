package com.mapbox.services.android.location;

import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapzen.android.lost.api.LocationListener;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.api.LocationServices;
import com.mapzen.android.lost.api.LostApiClient;

import java.lang.ref.WeakReference;

/**
 * Sample LocationEngine using the Open Source Lost library
 *
 * @deprecated Use {@link com.mapbox.services.android.telemetry.location.LostLocationEngine} instead
 */
@Deprecated
public class LostLocationEngine extends LocationEngine implements
  LostApiClient.ConnectionCallbacks, LocationListener {

  private static final String LOG_TAG = LostLocationEngine.class.getSimpleName();

  private static LocationEngine instance;

  private WeakReference<Context> context;
  private LostApiClient lostApiClient;

  @Deprecated
  public LostLocationEngine(Context context) {
    super();
    this.context = new WeakReference<>(context);
    lostApiClient = new LostApiClient.Builder(this.context.get())
      .addConnectionCallbacks(this)
      .build();
  }

  @Deprecated
  public static synchronized LocationEngine getLocationEngine(Context context) {
    if (instance == null) {
      instance = new LostLocationEngine(context.getApplicationContext());
    }

    return instance;
  }

  /**
   * Activate the location engine which will connect whichever location provider you are using. You'll need to call
   * this before requesting user location updates using {@link LocationEngine#requestLocationUpdates()}.
   *
   * @deprecated Use {@link com.mapbox.services.android.telemetry.location.LostLocationEngine} instead
   */
  @Deprecated
  @Override
  public void activate() {
    connect();
  }

  /**
   * Disconnect the location engine which is useful when you no longer need location updates or requesting the users
   * {@link LocationEngine#getLastLocation()}. Before deactivating, you'll need to stop request user location updates
   * using {@link LocationEngine#removeLocationUpdates()}.
   *
   * @deprecated Use {@link com.mapbox.services.android.telemetry.location.LostLocationEngine} instead
   */
  @Deprecated
  @Override
  public void deactivate() {
    if (lostApiClient != null && lostApiClient.isConnected()) {
      lostApiClient.disconnect();
    }
  }

  /**
   * Check if your location provider has been activated/connected. This is mainly used internally but is also useful in
   * the rare case when you'd like to know if your location engine is connected or not.
   *
   * @return boolean true if the location engine has been activated/connected, else false.
   * @deprecated Use {@link com.mapbox.services.android.telemetry.location.LostLocationEngine} instead
   */
  @Deprecated
  @Override
  public boolean isConnected() {
    return lostApiClient.isConnected();
  }

  /**
   * Invoked when the location provider has connected.
   *
   * @deprecated Use {@link com.mapbox.services.android.telemetry.location.LostLocationEngine} instead
   */
  @Deprecated
  @Override
  public void onConnected() {
    for (LocationEngineListener listener : locationListeners) {
      listener.onConnected();
    }
  }

  /**
   * Invoked when the location provider connection has been suspended.
   *
   * @deprecated Use {@link com.mapbox.services.android.telemetry.location.LostLocationEngine} instead
   */
  @Deprecated
  @Override
  public void onConnectionSuspended() {
    Log.d(LOG_TAG, "Connection suspended");
  }

  /**
   * Returns the Last known location if the location provider is connected.
   *
   * @return the last known location
   * @deprecated Use {@link com.mapbox.services.android.telemetry.location.LostLocationEngine} instead
   */
  @Deprecated
  @Override
  @Nullable
  public Location getLastLocation() {
    if (lostApiClient.isConnected()) {
      //noinspection MissingPermission
      return LocationServices.FusedLocationApi.getLastLocation(lostApiClient);
    }
    return null;
  }

  /**
   * Request location updates to the location provider.
   *
   * @deprecated Use {@link com.mapbox.services.android.telemetry.location.LostLocationEngine} instead
   */
  @Deprecated
  @Override
  public void requestLocationUpdates() {
    LocationRequest request = LocationRequest.create();

    if (interval != null) {
      request.setInterval(interval);
    }
    if (fastestInterval != null) {
      request.setFastestInterval(fastestInterval);
    }
    if (smallestDisplacement != null) {
      request.setSmallestDisplacement(smallestDisplacement);
    }

    if (priority == LocationEnginePriority.NO_POWER) {
      request.setPriority(LocationRequest.PRIORITY_NO_POWER);
    } else if (priority == LocationEnginePriority.LOW_POWER) {
      request.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    } else if (priority == LocationEnginePriority.BALANCED_POWER_ACCURACY) {
      request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    } else if (priority == LocationEnginePriority.HIGH_ACCURACY) {
      request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    if (lostApiClient.isConnected()) {
      //noinspection MissingPermission
      LocationServices.FusedLocationApi.requestLocationUpdates(lostApiClient, request, this);
    }
  }

  /**
   * Dismiss ongoing location update to the location provider.
   *
   * @deprecated Use {@link com.mapbox.services.android.telemetry.location.LostLocationEngine} instead
   */
  @Deprecated
  @Override
  public void removeLocationUpdates() {
    if (lostApiClient.isConnected()) {
      LocationServices.FusedLocationApi.removeLocationUpdates(lostApiClient, this);
    }
  }

  @Deprecated
  @Override
  public Type obtainType() {
    return Type.LOST;
  }

  /**
   * Invoked when the Location has changed.
   *
   * @param location the new location
   * @deprecated Use {@link com.mapbox.services.android.telemetry.location.LostLocationEngine} instead
   */
  @Deprecated
  @Override
  public void onLocationChanged(Location location) {
    for (LocationEngineListener listener : locationListeners) {
      listener.onLocationChanged(location);
    }
  }

  private void connect() {
    if (lostApiClient != null) {
      if (lostApiClient.isConnected()) {
        onConnected();
      } else {
        lostApiClient.connect();
      }
    }
  }
}