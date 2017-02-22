package com.mapbox.services.android.telemetry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * The TelemetryService will register for updates sent to this TelemetryLocationReceiver.
 * Messages are sent locally using a LocalBroadcastManager.
 */
public class TelemetryLocationReceiver extends BroadcastReceiver {

  private static final String LOG_TAG = TelemetryLocationReceiver.class.getSimpleName();

  public static final String INTENT_STRING =
    "com.mapbox.services.android.telemetry.location.TelemetryLocationReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.v(LOG_TAG, "Event received.");

    // See https://github.com/mapbox/mapbox-gl-native/issues/6934
    if (intent == null || intent.getExtras() == null) {
      return;
    }

    Location location = (Location) intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);
    if (location != null) {
      MapboxTelemetry.getInstance().addLocationEvent(location);
    }
  }

}
