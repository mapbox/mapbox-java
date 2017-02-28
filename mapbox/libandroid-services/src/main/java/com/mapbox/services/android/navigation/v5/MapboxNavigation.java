package com.mapbox.services.android.navigation.v5;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.mapbox.services.Experimental;
import com.mapbox.services.android.location.LostLocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.MapboxDirections;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.commons.models.Position;

import retrofit2.Callback;
import timber.log.Timber;

/**
 * This is an experimental API. Experimental APIs are quickly evolving and
 * might change or be removed in minor versions.
 *
 * @since 2.0.0
 */
@Experimental
public class MapboxNavigation {
  private Context context;
  private String accessToken;
  private NavigationServiceConnection connection;

  private boolean isBound;
  private NavigationService navigationService;

  private NavigationEventListener eventCallback;
  private AlertLevelChangeListener alertLevelChangeListener;
  private ProgressChangeListener progressChangeListener;
  private LocationEngine locationEngine;

  private Position origin;
  private Position destination;
  private DirectionsRoute route;
  private boolean snapToRoute;

  /*
   * Constructor
   */

  public MapboxNavigation(Context context, String accessToken) {
    Timber.d("MapboxNavigation initiated.");
    this.context = context;
    this.accessToken = accessToken;
    connection = new NavigationServiceConnection();
    isBound = false;
    navigationService = null;
    snapToRoute = true;
  }

  /*
   * Getters and setters
   */

  public void setEventCallback(NavigationEventListener eventCallback) {
    Timber.d("MapboxNavigation callback set.");
    this.eventCallback = eventCallback;
  }

  public void setLocationEngine(LocationEngine locationEngine) {
    this.locationEngine = locationEngine;
  }

  public LocationEngine getLocationEngine() {
    // Use the LostLocationEngine if none is provided
    return locationEngine == null ? new LostLocationEngine(context) : locationEngine;
  }

  public void setBound(boolean bound) {
    Timber.d("MapboxNavigation isBound update: %b", bound);
    this.isBound = bound;

    // Notify developer when service binding changes
    if (eventCallback != null) {
      eventCallback.onRunning(bound);
    }
  }

  public Position getOrigin() {
    return origin;
  }

  public void setOrigin(Position origin) {
    this.origin = origin;
  }

  public Position getDestination() {
    return destination;
  }

  public void setDestination(Position destination) {
    this.destination = destination;
  }

  public void setAlertLevelChangeListener(AlertLevelChangeListener alertLevelChangeListener) {
    this.alertLevelChangeListener = alertLevelChangeListener;
  }

  public void setProgressChangeListener(ProgressChangeListener progressChangeListener) {
    this.progressChangeListener = progressChangeListener;
  }

  public void setSnapToRoute(boolean snapToRoute) {
    this.snapToRoute = snapToRoute;
  }

  /*
   * Lifecycle
   */

  public void onStart() {
    Timber.d("MapboxNavigation onStart.");

    // Only bind if the service was previously started
    context.bindService(getServiceIntent(), connection, 0);
  }

  public void onStop() {
    Timber.d("MapboxNavigation onStop.");
    if (isBound) {
      context.unbindService(connection);
    }
  }

  private boolean isServiceAvailable() {
    boolean isAvailable = (navigationService != null) && isBound;
    Timber.d("MapboxNavigation service available: %b", isAvailable);
    return isAvailable;
  }

  public void startNavigation(DirectionsRoute route) {
    if (!isServiceAvailable()) {
      Timber.d("MapboxNavigation origin navigation.");
      this.route = route;
      if (!isBound) {
        context.bindService(getServiceIntent(), connection, 0);
      }
      context.startService(getServiceIntent());
    }
  }

  public void setupNotification(Activity activity) {
    if (isServiceAvailable()) {
      Timber.d("MapboxNavigation setting up notification.");
      navigationService.setupNotification(activity);
    }
  }

  public void endNavigation() {
    if (isServiceAvailable()) {
      Timber.d("MapboxNavigation destination navigation.");
      navigationService.endNavigation();
      isBound = false;
    }
  }

  private Intent getServiceIntent() {
    return new Intent(context, NavigationService.class);
  }

  private class NavigationServiceConnection implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
      Timber.d("Connected to service.");
      NavigationService.LocalBinder binder = (NavigationService.LocalBinder) service;
      navigationService = binder.getService();
      navigationService.setLocationEngine(locationEngine);
      navigationService.setAlertLevelChangeListener(alertLevelChangeListener);
      navigationService.setProgressChangeListener(progressChangeListener);
      navigationService.setSnapToRoute(snapToRoute);
      navigationService.startRoute(route);
      setBound(true);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
      Timber.d("Disconnected from service.");
      navigationService = null;
      setBound(false);
    }
  }

  public void getRoute(Callback<DirectionsResponse> callback) {
    try {
      MapboxDirections directions = new MapboxDirections.Builder()
        .setProfile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
        .setAccessToken(accessToken)
        .setOverview(DirectionsCriteria.OVERVIEW_FULL)
        .setOrigin(origin)
        .setDestination(destination)
        .setSteps(true)
        .build();
      directions.enqueueCall(callback);
    } catch (ServicesException serviceException) {
      Timber.e("Failed to get route: %s", serviceException.getMessage());
    }
  }
}
