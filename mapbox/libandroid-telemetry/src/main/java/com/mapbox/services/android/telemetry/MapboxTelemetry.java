package com.mapbox.services.android.telemetry;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.mapbox.services.android.telemetry.backoff.ExponentialBackoff;
import com.mapbox.services.android.telemetry.connectivity.ConnectivityReceiver;
import com.mapbox.services.android.telemetry.constants.TelemetryConstants;
import com.mapbox.services.android.telemetry.http.TelemetryClient;
import com.mapbox.services.android.telemetry.location.AndroidLocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.mapbox.services.android.telemetry.service.TelemetryService;
import com.mapbox.services.android.telemetry.utils.TelemetryUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.Util;
import timber.log.Timber;

/**
 * This is the entry point to manage Mapbox telemetry
 */
public class MapboxTelemetry implements Callback, LocationEngineListener {

  private static MapboxTelemetry instance;

  private boolean initialized = false;
  private Context context = null;
  private String accessToken = null;
  private String mapboxSessionId = null;
  private long mapboxSessionIdLastSet = 0;
  private String mapboxVendorId = null;
  private DisplayMetrics displayMetrics = null;
  private Intent batteryStatus = null;
  private TelemetryClient client = null;
  private Vector<Hashtable<String, Object>> events = new Vector<>();
  private Timer timer = null;
  private LocationEngine locationEngine = null;
  private boolean withShutDown = false;
  private Boolean telemetryEnabled = null;

  /**
   * Private constructor for configuring the single instance per app.
   */
  private MapboxTelemetry() {
  }

  /**
   * Primary access method (using singleton pattern)
   *
   * @return MapboxTelemetry
   */
  public static synchronized MapboxTelemetry getInstance() {
    if (instance == null) {
      instance = new MapboxTelemetry();
    }

    return instance;
  }

  /**
   * Initialize MapboxTelemetry.
   *
   * @param context        The context associated with the application
   * @param accessToken    The accessToken associated with the application
   * @param locationEngine Initialize telemetry with a custom location engine
   */
  public void initialize(@NonNull Context context, @NonNull String accessToken,
                         @NonNull LocationEngine locationEngine) {
    this.locationEngine = locationEngine;
    initialize(context, accessToken);
  }

  /**
   * Initialize MapboxTelemetry.
   *
   * @param context     The context associated with the application
   * @param accessToken The accessToken associated with the application
   */
  public void initialize(@NonNull Context context, @NonNull String accessToken) {
    if (initialized) {
      return;
    }

    Timber.v("Initializing telemetry.");
    this.context = context.getApplicationContext();
    this.accessToken = accessToken;
    if (this.context == null || TextUtils.isEmpty(this.accessToken)) {
      throw new TelemetryException(
        "Please, make sure you provide a valid context and access token. "
          + "For more information, please visit https://www.mapbox.com/android-sdk.");
    }

    validateTelemetryServiceConfigured();
    setupHttpClient();
    checkStagingServerInformation();
    rotateSessionId();
    readDisplayMetrics();
    registerBatteryUpdates();
    loadUserPreferences();

    initialized = true;
  }

  /**
   * Checks that TelemetryService has been configured by developer
   */
  private void validateTelemetryServiceConfigured() {
    try {
      // Check the service is in the AndroidManifest.xml
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
        context.getPackageName(), PackageManager.GET_SERVICES);
      if (packageInfo.services != null) {
        for (ServiceInfo service : packageInfo.services) {
          if (TextUtils.equals("com.mapbox.services.android.telemetry.service.TelemetryService", service.name)) {
            return;
          }
        }
      }
    } catch (Exception exception) {
      Timber.w("Failed to inspect for the telemetry service: %s.", exception.getMessage());
    }

    throw new TelemetryException(
      "Please, make sure you add the Telemetry service "
        + "(`com.mapbox.services.android.telemetry.service.TelemetryService`) to your `AndroidManifest.xml` file. "
        + "For more information, please visit https://www.mapbox.com/android-sdk.");
  }

  private void setupHttpClient() {
    client = new TelemetryClient(accessToken);
  }

  private void checkStagingServerInformation() {
    try {
      String stagingURL = null;
      String stagingAccessToken = null;

      // Try app metadata first
      ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
        PackageManager.GET_META_DATA);
      if (appInfo != null && appInfo.metaData != null) {
        stagingURL = appInfo.metaData.getString(TelemetryConstants.KEY_META_DATA_STAGING_SERVER);
        stagingAccessToken = appInfo.metaData.getString(TelemetryConstants.KEY_META_DATA_STAGING_ACCESS_TOKEN);
      }

      // Try shared preferences otherwise
      if (TextUtils.isEmpty(stagingURL) || TextUtils.isEmpty(stagingAccessToken)) {
        SharedPreferences prefs = TelemetryUtils.getSharedPreferences(context);
        stagingURL = prefs.getString(TelemetryConstants.MAPBOX_SHARED_PREFERENCE_KEY_TELEMETRY_STAGING_URL, null);
        stagingAccessToken = prefs.getString(
          TelemetryConstants.MAPBOX_SHARED_PREFERENCE_KEY_TELEMETRY_STAGING_ACCESS_TOKEN, null);
      }

      // Set new client information (if needed)
      if (!TextUtils.isEmpty(stagingURL) && !TextUtils.isEmpty(stagingAccessToken)) {
        Timber.w("Using staging server '%s' with access token '%s'.", stagingURL, stagingAccessToken);
        client.setEventsEndpoint(stagingURL);
        client.setAccessToken(stagingAccessToken);
        client.setStagingEnvironment(true);
      }

      // Append appIdentifier to user agent
      String appIdentifier = TelemetryUtils.getApplicationIdentifier(context);
      String userAgent = client.getUserAgent();
      if (TextUtils.equals(userAgent, BuildConfig.MAPBOX_EVENTS_USER_AGENT_BASE)
        && !TextUtils.isEmpty(appIdentifier)) {
        String updatedUserAgent = Util.toHumanReadableAscii(
          String.format(TelemetryConstants.DEFAULT_LOCALE, "%s %s", appIdentifier, userAgent));
        Timber.v("Updating user agent value: '%s", updatedUserAgent);
        client.setUserAgent(updatedUserAgent);
      }
    } catch (Exception exception) {
      Timber.e("Failed to check for staging credentials: %s", exception.getMessage());
    }
  }

  /**
   * Changes session ID based on time boundary
   */
  private void rotateSessionId() {
    long timeSinceLastSet = System.currentTimeMillis() - mapboxSessionIdLastSet;
    if ((TextUtils.isEmpty(mapboxSessionId))
      || (timeSinceLastSet > TelemetryConstants.SESSION_ID_ROTATION_MS)) {
      mapboxSessionId = UUID.randomUUID().toString();
      mapboxSessionIdLastSet = System.currentTimeMillis();
    }
  }

  private void readDisplayMetrics() {
    displayMetrics = new DisplayMetrics();
    ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
  }

  private void registerBatteryUpdates() {
    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    batteryStatus = context.registerReceiver(null, filter);
  }

  private int getBatteryLevel() {
    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    return Math.round((level / (float) scale) * 100);
  }

  /**
   * Determine if device is plugged in to power via USB or AC or not.
   * http://developer.android.com/reference/android/os/BatteryManager.html#EXTRA_PLUGGED
   *
   * @return true if plugged in, false if not
   */
  private boolean isPluggedIn() {
    int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
    if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB
      || chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
      return true;
    }

    return false;
  }

  private void loadUserPreferences() {
    SharedPreferences prefs = TelemetryUtils.getSharedPreferences(context);

    // Load vendor ID
    if (prefs.contains(TelemetryConstants.MAPBOX_SHARED_PREFERENCE_KEY_VENDOR_ID)) {
      mapboxVendorId = prefs.getString(TelemetryConstants.MAPBOX_SHARED_PREFERENCE_KEY_VENDOR_ID, "");
    }

    // Create vendor ID (if needed)
    if (TextUtils.isEmpty(mapboxVendorId)) {
      mapboxVendorId = UUID.randomUUID().toString();
      SharedPreferences.Editor editor = prefs.edit();
      editor.putString(TelemetryConstants.MAPBOX_SHARED_PREFERENCE_KEY_VENDOR_ID, mapboxVendorId);
      editor.apply();
      editor.commit();
    }

    // Set telemetry opt-in/opt-out status
    setTelemetryEnabled(isTelemetryEnabled());
  }

  public boolean isTelemetryEnabled() {
    if (telemetryEnabled == null) {
      // Cache value
      telemetryEnabled = TelemetryUtils.getSharedPreferences(context)
        .getBoolean(TelemetryConstants.MAPBOX_SHARED_PREFERENCE_KEY_TELEMETRY_ENABLED, true);
    }

    return telemetryEnabled;
  }

  /**
   * Enables / disables telemetry
   *
   * @param telemetryEnabled True to start telemetry, false to stop it
   */
  public void setTelemetryEnabled(boolean telemetryEnabled) {
    if (initialized && isTelemetryEnabled() == telemetryEnabled) {
      Timber.v("Telemetry was already initialized on that state (enabled: %b).", telemetryEnabled);
      return;
    }

    if (telemetryEnabled) {
      Timber.v("Enabling telemetry.");
      context.startService(new Intent(context, TelemetryService.class));

      // Check for location permissions, periodically if necessary
      if (PermissionsManager.areLocationPermissionsGranted(context)) {
        registerLocationUpdates();
      } else {
        startPermissionsTimer();
      }

      // Send events timer
      timer = new Timer();
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          flushEventsQueueImmediately(false);
        }
      }, TelemetryConstants.FLUSH_DELAY_MS, TelemetryConstants.FLUSH_PERIOD_MS);
    } else {
      Timber.v("Disabling telemetry.");
      withShutDown = true;

      // This event is always recorded
      pushTurnstileEvent();
    }

    // Persist
    this.telemetryEnabled = telemetryEnabled;
    SharedPreferences prefs = TelemetryUtils.getSharedPreferences(context);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putBoolean(TelemetryConstants.MAPBOX_SHARED_PREFERENCE_KEY_TELEMETRY_ENABLED, telemetryEnabled);
    editor.apply();
    editor.commit();
  }

  /**
   * Start timer that checks for permissions
   */
  private void startPermissionsTimer() {
    final Handler handler = new Handler();
    final ExponentialBackoff counter = new ExponentialBackoff();
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
          registerLocationUpdates();
        } else {
          // Restart handler
          long nextWaitTime = counter.nextBackOffMillis();
          Timber.v("Location permissions not granted (checking again in %d seconds).", nextWaitTime / 1000);
          handler.postDelayed(this, nextWaitTime);
        }
      }
    };

    long nextWaitTime = counter.nextBackOffMillis();
    Timber.v("Location permissions not granted (checking again in %d seconds).", nextWaitTime / 1000);
    handler.postDelayed(runnable, nextWaitTime);
  }

  private void registerLocationUpdates() {
    if (locationEngine == null) {
      locationEngine = new AndroidLocationEngine(context);
    }

    locationEngine.addLocationEngineListener(this);
    locationEngine.activate();
  }

  @Override
  public void onConnected() {
    locationEngine.requestLocationUpdates();
  }

  @Override
  public void onLocationChanged(Location location) {
    Intent intent = new Intent(TelemetryLocationReceiver.INTENT_STRING);
    intent.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
    LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
  }

  private boolean isReadyForEvent() {
    return initialized && isTelemetryEnabled();
  }

  /**
   * Centralized method for adding populated event to the queue allowing for cap size checking
   *
   * @param event Event to add to the Events Queue
   */
  private void putEventOnQueue(@NonNull Hashtable<String, Object> event) {
    events.add(event);
    if (events.size() >= TelemetryConstants.FLUSH_EVENTS_CAP) {
      flushEventsQueueImmediately(false);
    }
  }

  /**
   * Adds a Location Event to the system for processing
   *
   * @param location Location event
   */
  protected void addLocationEvent(Location location) {
    // Only add events when we're properly initialized and the user has opted-in
    if (!isReadyForEvent()) {
      return;
    }

    // NaN and Infinite checks to prevent JSON errors at send to server time
    if (Double.isNaN(location.getLatitude()) || Double.isNaN(location.getLongitude())
      || Double.isNaN(location.getAltitude()) || Float.isNaN(location.getAccuracy())) {
      return;
    }

    if (Double.isInfinite(location.getLatitude()) || Double.isInfinite(location.getLongitude())
      || Double.isInfinite(location.getAltitude()) || Float.isInfinite(location.getAccuracy())) {
      return;
    }

    // Scale values
    double latitudeScaled = new BigDecimal(location.getLatitude()).setScale(7, BigDecimal.ROUND_DOWN).doubleValue();
    double longitudeScaled = new BigDecimal(location.getLongitude()).setScale(7, BigDecimal.ROUND_DOWN).doubleValue();

    // Add Location even to queue
    Hashtable<String, Object> event = new Hashtable<>();
    event.put(MapboxEvent.KEY_EVENT, MapboxEvent.TYPE_LOCATION);
    event.put(MapboxEvent.KEY_CREATED, TelemetryUtils.generateCreateDate());
    event.put(MapboxEvent.KEY_SOURCE, MapboxEvent.SOURCE_MAPBOX);
    event.put(MapboxEvent.KEY_SESSION_ID, mapboxSessionId);
    event.put(MapboxEvent.KEY_LATITUDE, latitudeScaled);
    event.put(MapboxEvent.KEY_LONGITUDE, longitudeScaled);
    event.put(MapboxEvent.KEY_ALTITUDE, Math.round(location.getAltitude()));
    event.put(MapboxEvent.KEY_HORIZONTAL_ACCURACY, Math.round(location.getAccuracy()));
    event.put(MapboxEvent.KEY_OPERATING_SYSTEM, TelemetryConstants.OPERATING_SYSTEM);
    event.put(MapboxEvent.KEY_APPLICATION_STATE, TelemetryUtils.getApplicationState(context));
    putEventOnQueue(event);

    // Rotate session ID with location events
    rotateSessionId();
  }

  /**
   * Push interactive events to the system for processing
   *
   * @param eventWithAttributes Event with attributes
   */
  public void pushEvent(Hashtable<String, Object> eventWithAttributes) {
    // Only add events when we're properly initialized and the user has opted-in
    if (!isReadyForEvent()) {
      return;
    }

    if (eventWithAttributes == null) {
      return;
    }

    String eventType = (String) eventWithAttributes.get(MapboxEvent.KEY_EVENT);
    if (TextUtils.isEmpty(eventType)) {
      return;
    }

    if (eventType.equalsIgnoreCase(MapboxEvent.TYPE_MAP_LOAD)) {
      // Map load event
      eventWithAttributes.put(MapboxEvent.KEY_USER_ID, mapboxVendorId);
      eventWithAttributes.put(MapboxEvent.KEY_MODEL, Build.MODEL);
      eventWithAttributes.put(MapboxEvent.KEY_OPERATING_SYSTEM, TelemetryConstants.OPERATING_SYSTEM);
      eventWithAttributes.put(MapboxEvent.KEY_RESOLUTION, displayMetrics.density);
      eventWithAttributes.put(MapboxEvent.KEY_ACCESSIBILITY_FONT_SCALE,
        TelemetryUtils.getAccesibilityFontScaleSize(context));
      eventWithAttributes.put(MapboxEvent.KEY_ORIENTATION, TelemetryUtils.getOrientation(context));
      eventWithAttributes.put(MapboxEvent.KEY_BATTERY_LEVEL, getBatteryLevel());
      eventWithAttributes.put(MapboxEvent.KEY_PLUGGED_IN, isPluggedIn());
      eventWithAttributes.put(MapboxEvent.KEY_CARRIER, TelemetryUtils.getCellularCarrier(context));
      eventWithAttributes.put(MapboxEvent.KEY_CELLULAR_NETWORK_TYPE, TelemetryUtils.getCellularNetworkType(context));
      eventWithAttributes.put(MapboxEvent.KEY_WIFI, TelemetryUtils.getConnectedToWifi(context));
      putEventOnQueue(eventWithAttributes);
      pushTurnstileEvent();
    } else if (eventType.equalsIgnoreCase(MapboxEvent.TYPE_MAP_CLICK)) {
      // Map click event
      eventWithAttributes.put(MapboxEvent.KEY_ORIENTATION, TelemetryUtils.getOrientation(context));
      eventWithAttributes.put(MapboxEvent.KEY_BATTERY_LEVEL, getBatteryLevel());
      eventWithAttributes.put(MapboxEvent.KEY_PLUGGED_IN, isPluggedIn());
      eventWithAttributes.put(MapboxEvent.KEY_CARRIER, TelemetryUtils.getCellularCarrier(context));
      eventWithAttributes.put(MapboxEvent.KEY_CELLULAR_NETWORK_TYPE, TelemetryUtils.getCellularNetworkType(context));
      eventWithAttributes.put(MapboxEvent.KEY_WIFI, TelemetryUtils.getConnectedToWifi(context));
      putEventOnQueue(eventWithAttributes);
    } else if (eventType.equalsIgnoreCase(MapboxEvent.TYPE_MAP_DRAG_END)) {
      // Map drag end event
      eventWithAttributes.put(MapboxEvent.KEY_ORIENTATION, TelemetryUtils.getOrientation(context));
      eventWithAttributes.put(MapboxEvent.KEY_BATTERY_LEVEL, getBatteryLevel());
      eventWithAttributes.put(MapboxEvent.KEY_PLUGGED_IN, isPluggedIn());
      eventWithAttributes.put(MapboxEvent.KEY_CARRIER, TelemetryUtils.getCellularCarrier(context));
      eventWithAttributes.put(MapboxEvent.KEY_CELLULAR_NETWORK_TYPE, TelemetryUtils.getCellularNetworkType(context));
      eventWithAttributes.put(MapboxEvent.KEY_WIFI, TelemetryUtils.getConnectedToWifi(context));
      putEventOnQueue(eventWithAttributes);
    } else {
      Timber.w("Unknown event type provided: %s.", eventType);
    }
  }

  /**
   * Immediately attempt to send all events data in the queue to the server.
   */
  private void flushEventsQueueImmediately(boolean hasTurnstileEvent) {
    boolean doRequest = hasTurnstileEvent || isReadyForEvent();
    if (events.size() > 0 && ConnectivityReceiver.isConnected(context) && doRequest) {
      client.sendEvents(events, this);
    } else if (withShutDown) {
      shutdownTelemetry();
    }
  }

  /**
   * Pushes turnstile event for internal billing purposes.
   */
  private void pushTurnstileEvent() {
    Hashtable<String, Object> event = new Hashtable<>();
    event.put(MapboxEvent.KEY_EVENT, MapboxEvent.TYPE_TURNSTILE);
    event.put(MapboxEvent.KEY_CREATED, TelemetryUtils.generateCreateDate());
    event.put(MapboxEvent.KEY_USER_ID, mapboxVendorId);
    event.put(MapboxEvent.KEY_ENABLED_TELEMETRY, isTelemetryEnabled());
    events.add(event);
    flushEventsQueueImmediately(true);
  }

  /**
   * Called when the request could not be executed due to cancellation, a connectivity problem or
   * timeout. Because networks can fail during an exchange, it is possible that the remote server
   * accepted the request before the failure.
   */
  @Override
  public void onFailure(Call call, IOException e) {
    Timber.v("HTTP request failed: %s", e);

    // Make sure that events don't pile up (e.g. offline) and thus impact available memory over time.
    events.removeAllElements();

    if (withShutDown) {
      shutdownTelemetry();
    }
  }

  /**
   * Called when the HTTP response was successfully returned by the remote server. The callback may
   * proceed to read the response body with {@link Response#body}. The response is still live until
   * its response body is closed. The recipient of the callback may consume the response body on
   * another thread.
   * <p>
   * <p>Note that transport-layer success (receiving a HTTP response code, headers and body) does
   * not necessarily indicate application-layer success: {@code response} may still indicate an
   * unhappy HTTP response code like 404 or 500.
   */
  @Override
  public void onResponse(Call call, Response response) throws IOException {
    Timber.v("HTTP request succeeded.");

    // Make sure that events don't pile up (e.g. offline) and thus impact available memory over time.
    events.removeAllElements();

    // Close the response body
    if (response != null && response.body() != null) {
      response.body().close();
    }

    if (withShutDown) {
      shutdownTelemetry();
    }
  }

  private void shutdownTelemetry() {
    Timber.d("Shutting down telemetry service.");
    withShutDown = false;
    events.removeAllElements();
    context.stopService(new Intent(context, TelemetryService.class));
    locationEngine.removeLocationEngineListener(this);
    locationEngine.removeLocationUpdates();
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
  }
}
