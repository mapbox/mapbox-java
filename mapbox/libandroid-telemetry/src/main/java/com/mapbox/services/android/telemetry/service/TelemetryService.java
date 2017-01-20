package com.mapbox.services.android.telemetry.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.mapbox.services.android.telemetry.TelemetryLocationReceiver;

import timber.log.Timber;

/**
 * Android telemetry Service
 */
public class TelemetryService extends Service {

  private TelemetryLocationReceiver receiver = null;

  /**
   * Return the communication channel to the service.
   */
  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    // A service may return null if clients can not bind to the service.
    Timber.w("The service doesn't support a binder interface.");
    return null;
  }

  /**
   * Called by the system when the service is first created.
   */
  @Override
  public void onCreate() {
    // Enable location listening for lifecycle of app
    Timber.v("Create event.");
    receiver = new TelemetryLocationReceiver();
    LocalBroadcastManager.getInstance(getApplicationContext())
      .registerReceiver(receiver, new IntentFilter(TelemetryLocationReceiver.INTENT_STRING));
  }

  /**
   * Called by the system every time a client explicitly starts the service by calling startService(Intent),
   * providing the arguments it supplied and a unique integer token representing the start request.
   */
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Timber.v("Start command event.");
    return START_NOT_STICKY;
  }

  /**
   * This is called if the service is currently running and the user has removed a task that comes from the
   * service's application (swiping left/right the tasks list).
   */
  @Override
  public void onTaskRemoved(Intent rootIntent) {
    Timber.v("Task removed event.");
    shutdownTelemetryService();
  }

  /**
   * Called by the system to notify a Service that it is no longer used and is being removed. The
   * service should clean up any resources it holds (threads, registered receivers, etc) at this point.
   * Upon return, there will be no more calls in to this Service object and it is effectively dead.
   */
  @Override
  public void onDestroy() {
    Timber.v("Destroy event.");
    shutdownTelemetryService();
  }

  private void shutdownTelemetryService() {
    try {
      Timber.v("Unregistering location receiver.");
      LocalBroadcastManager.getInstance(getApplicationContext())
        .unregisterReceiver(receiver);
    } catch (Exception exception) {
      Timber.e("Unregistering receiver failed: %s.", exception.getMessage());
    }
  }

}
