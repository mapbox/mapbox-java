package com.mapbox.services.android.telemetry.constants;

import android.os.Build;

import java.util.Locale;

/**
 * Created by antonio on 1/18/17.
 */

public class TelemetryConstants {

  public static final String PLATFORM = "Android";
  public static final String OPERATING_SYSTEM = PLATFORM + " - " + Build.VERSION.RELEASE;

  public static final Locale DEFAULT_LOCALE = Locale.US;

  public static final String TELEMETRY_SERVICE_NAME = "com.mapbox.services.android.telemetry.service.TelemetryService";

  public static final long FLUSH_DELAY_MS = 10 * 1_000; // 10 seconds
  public static final long FLUSH_PERIOD_MS = 3 * 60 * 1_000; // 3 minutes
  public static final int FLUSH_EVENTS_CAP = 180; // 180 seconds or 180 events

  public static final int SESSION_ID_ROTATION_MS = 24 * 60 * 60 * 1_000; // 24 hours

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

}
