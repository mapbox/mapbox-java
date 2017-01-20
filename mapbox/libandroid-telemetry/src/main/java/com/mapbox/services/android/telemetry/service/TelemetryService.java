package com.mapbox.services.android.telemetry.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.mapbox.services.android.telemetry.location.TelemetryLocationReceiver;

/**
 * Android telemetry Service
 */
public class TelemetryService extends Service {

  private static final String LOG_TAG = TelemetryService.class.getSimpleName();

  private TelemetryLocationReceiver telemetryLocationReceiver = null;

  /**
   * Return the communication channel to the service.
   */
  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    // A service may return null if clients can not bind to the service.
    return null;
  }

  /**
   * Called by the system when the service is first created.
   */
  @Override
  public void onCreate() {
    // Enable location listening for lifecycle of app
    telemetryLocationReceiver = new TelemetryLocationReceiver();
    LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
      telemetryLocationReceiver,
      new IntentFilter(TelemetryLocationReceiver.INTENT_STRING)
    );
  }

  /**
   * Called by the system every time a client explicitly starts the service by calling startService(Intent),
   * providing the arguments it supplied and a unique integer token representing the start request.
   */
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_NOT_STICKY;
  }

  /**
   * This is called if the service is currently running and the user has removed a task that comes from the
   * service's application.
   */
  @Override
  public void onTaskRemoved(Intent rootIntent) {
    shutdownTelemetryService();
  }

  /**
   * Called by the system to notify a Service that it is no longer used and is being removed. The
   * service should clean up any resources it holds (threads, registered receivers, etc) at this point.
   * Upon return, there will be no more calls in to this Service object and it is effectively dead.
   */
  @Override
  public void onDestroy() {
    shutdownTelemetryService();
  }

  private void shutdownTelemetryService() {
    try {
      unregisterReceiver(telemetryLocationReceiver);
    } catch (Exception exception) {
      Log.e(LOG_TAG, "unregisterReceiver failed: " + exception.getMessage());
    }
  }

}
