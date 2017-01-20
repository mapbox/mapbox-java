package com.mapbox.services.android.telemetry;

import android.os.Build;

import java.util.Locale;

/**
 * Created by antonio on 1/18/17.
 */

public class TelemetryConstants {

  public static final String OPERATING_SYSTEM = "Android - " + Build.VERSION.RELEASE;

  public static final Locale DEFAULT_LOCALE = Locale.US;

  public static final long FLUSH_DELAY_MS = 10 * 1_000; // 10 seconds
  public static final long FLUSH_PERIOD_MS = 3 * 60 * 1_000; // 3 minutes
  public static final int FLUSH_EVENTS_CAP = 1_000;

  public static final long HOUR_IN_MS = 60 * 60 * 1_000;

  public static final int SESSION_ID_ROTATION_HOURS = 24;

  public static final double LOCATION_EVENT_ACCURACY = 10_000_000;

  /**
   * Key used to store staging data server url in AndroidManifest.xml
   */
  public static final String KEY_META_DATA_STAGING_SERVER = "com.mapbox.TestEventsServer";

  /**
   * Key used to store staging data server access token in AndroidManifest.xml
   */
  public static final String KEY_META_DATA_STAGING_ACCESS_TOKEN = "com.mapbox.TestEventsAccessToken";

  public static final String MAPBOX_SHARED_PREFERENCES_FILE = "MapboxSharedPreferences";
  public static final String MAPBOX_SHARED_PREFERENCE_KEY_VENDOR_ID = "mapboxVendorId";
  public static final String MAPBOX_SHARED_PREFERENCE_KEY_TELEMETRY_ENABLED = "mapboxTelemetryEnabled";
  public static final String MAPBOX_SHARED_PREFERENCE_KEY_TELEMETRY_STAGING_URL = "mapboxTelemetryStagingUrl";
  public static final String MAPBOX_SHARED_PREFERENCE_KEY_TELEMETRY_STAGING_ACCESS_TOKEN =
    "mapboxTelemetryStagingAccessToken";

  public static final double MIN_LONGITUDE = 0.0;
  public static final double MAX_LONGITUDE = 0.0;

}
