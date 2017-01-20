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
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.mapbox.services.android.telemetry.backoff.ExponentialBackoff;
import com.mapbox.services.android.telemetry.connectivity.ConnectivityReceiver;
import com.mapbox.services.android.telemetry.http.TelemetryClient;
import com.mapbox.services.android.telemetry.location.AndroidLocationEngine;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.mapbox.services.android.telemetry.service.TelemetryService;
import com.mapbox.services.android.telemetry.utils.TelemetryUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.Util;

/**
 * This is the entry point to manage Mapbox telemetry
 */
public class MapboxTelemetry implements Callback {

  private static final String LOG_TAG = MapboxTelemetry.class.getSimpleName();

  private static MapboxTelemetry instance;

  private boolean initialized = false;
  private Context context = null;
  private String accessToken = null;
  private MessageDigest messageDigest = null;
  private String mapboxSessionId = null;
  private long mapboxSessionIdLastSet = 0;
  private String mapboxVendorId = null;
  private boolean telemetryEnabled = true;
  private DisplayMetrics displayMetrics = null;
  private Intent batteryStatus = null;
  private TelemetryClient client = null;
  private Vector<Hashtable<String, Object>> events = new Vector<>();
  private Timer timer = null;
  private boolean withShutDown = false;

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
  public static MapboxTelemetry getInstance() {
    if (instance == null) {
      instance = new MapboxTelemetry();
    }

    return instance;
  }

  /**
   * Internal setup of MapboxTelemetry. It needs to be called once before @link
   * MapboxTelemetry#getInstance(). This allows for a cleaner getInstance() that doesn't
   * require context and accessToken.
   *
   * @param context     The context associated with MapView
   * @param accessToken The accessToken to load MapView
   */
  public void initialize(@NonNull Context context, @NonNull String accessToken) {
    if (initialized) {
      return;
    }

    this.context = context.getApplicationContext();
    this.accessToken = accessToken;
    if (this.context == null || TextUtils.isEmpty(this.accessToken)) {
      throw new TelemetryException(
        "Please, make sure you provide a valid context and access token. "
          + "For more information, please visit https://www.mapbox.com/android-sdk.");
    }

    validateTelemetryServiceConfigured();
    setupMessageDigest();
    rotateSessionId();
    loadUserPreferences();
    readDisplayMetrics();
    checkStagingServerInformation();
    registerBatteryUpdates();
    setupHttpClient();

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
      Log.w(LOG_TAG, "Failed to look for the telemetry service: " + exception.getMessage());
    }

    throw new TelemetryException(
      "Please, make sure you add the Telemetry service "
        + "(`com.mapbox.services.android.telemetry.service.TelemetryService`) to your `AndroidManifest.xml` file. "
        + "For more information, please visit https://www.mapbox.com/android-sdk.");
  }

  private void setupMessageDigest() {
    try {
      messageDigest = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException exception) {
      Log.w(LOG_TAG, "Error getting encryption algorithm: ", exception);
    }
  }

  /**
   * Changes session ID based on time boundary
   */
  private void rotateSessionId() {
    long timeSinceLastSet = System.currentTimeMillis() - mapboxSessionIdLastSet;
    if ((TextUtils.isEmpty(mapboxSessionId))
      || (timeSinceLastSet > (TelemetryConstants.SESSION_ID_ROTATION_HOURS * TelemetryConstants.HOUR_IN_MS))) {
      mapboxSessionId = UUID.randomUUID().toString();
      mapboxSessionIdLastSet = System.currentTimeMillis();
    }
  }

  private void loadUserPreferences() {
    SharedPreferences prefs = TelemetryUtils.getSharedPreferences(context);

    // Determine if telemetry should be enabled
    setTelemetryEnabled(prefs.getBoolean(TelemetryConstants.MAPBOX_SHARED_PREFERENCE_KEY_TELEMETRY_ENABLED, true));

    // Load vendor ID
    if (prefs.contains(TelemetryConstants.MAPBOX_SHARED_PREFERENCE_KEY_VENDOR_ID)) {
      mapboxVendorId = prefs.getString(TelemetryConstants.MAPBOX_SHARED_PREFERENCE_KEY_VENDOR_ID, "");
    }

    // Create vendor ID
    if (TextUtils.isEmpty(mapboxVendorId)) {
      String vendorId = UUID.randomUUID().toString();
      mapboxVendorId = encodeString(vendorId);
      SharedPreferences.Editor editor = prefs.edit();
      editor.putString(TelemetryConstants.MAPBOX_SHARED_PREFERENCE_KEY_VENDOR_ID, mapboxVendorId);
      editor.apply();
      editor.commit();
    }
  }

  private void readDisplayMetrics() {
    displayMetrics = new DisplayMetrics();
    ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
  }

  /**
   * SHA-1 encoding for strings
   *
   * @param string String to encode
   * @return String encoded if no error, original string if error
   */
  private String encodeString(String string) {
    try {
      if (messageDigest != null) {
        messageDigest.reset();
        messageDigest.update(string.getBytes("UTF-8"));
        byte[] bytes = messageDigest.digest();

        // Get the hex version of the digest
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
          sb.append(String.format("%02X", b));
        }

        return sb.toString();
      }
    } catch (Exception exception) {
      Log.w(LOG_TAG, "Error encoding string, will return in original form: " + exception.getMessage());
    }

    return string;
  }

  private void checkStagingServerInformation() {
    try {
      SharedPreferences prefs = TelemetryUtils.getSharedPreferences(context);
      ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
        PackageManager.GET_META_DATA);
      String stagingURL = appInfo.metaData.getString(TelemetryConstants.KEY_META_DATA_STAGING_SERVER);
      String stagingAccessToken = appInfo.metaData.getString(TelemetryConstants.KEY_META_DATA_STAGING_ACCESS_TOKEN);

      if (TextUtils.isEmpty(stagingURL) || TextUtils.isEmpty(stagingAccessToken)) {
        stagingURL = prefs.getString(TelemetryConstants.MAPBOX_SHARED_PREFERENCE_KEY_TELEMETRY_STAGING_URL, null);
        stagingAccessToken = prefs.getString(
          TelemetryConstants.MAPBOX_SHARED_PREFERENCE_KEY_TELEMETRY_STAGING_ACCESS_TOKEN, null);
      }

      if (!TextUtils.isEmpty(stagingURL) && !TextUtils.isEmpty(stagingAccessToken)) {
        client.setEventsEndpoint(stagingURL);
        client.setAccessToken(stagingAccessToken);
        client.setStagingEnvironment(true);
      }

      // Append appIdentifier to user agent
      String appIdentifier = TelemetryUtils.getApplicationIdentifier(context);
      String userAgent = client.getUserAgent();
      if (TextUtils.equals(userAgent, BuildConfig.MAPBOX_EVENTS_USER_AGENT_BASE)
        && !TextUtils.isEmpty(appIdentifier)) {
        client.setUserAgent(Util.toHumanReadableAscii(
          String.format(TelemetryConstants.DEFAULT_LOCALE, "%s %s", appIdentifier, userAgent)));
      }
    } catch (Exception exception) {
      Log.e(LOG_TAG, "Failed to check staging credentials: " + exception.getMessage());
    }
  }

  private void registerBatteryUpdates() {
    IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    batteryStatus = context.registerReceiver(null, iFilter);
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
    if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB || chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
      return true;
    }

    return false;
  }

  private void setupHttpClient() {
    client = new TelemetryClient(accessToken);
  }

  public boolean isTelemetryEnabled() {
    return telemetryEnabled;
  }

  /**
   * Enables / Disables Telemetry
   *
   * @param telemetryEnabled True to start telemetry, false to stop it
   */
  public void setTelemetryEnabled(boolean telemetryEnabled) {
    if (this.telemetryEnabled == telemetryEnabled) {
      return;
    }

    if (telemetryEnabled) {
      context.startService(new Intent(context, TelemetryService.class));

      // Check for location permissions, periodically if necessary
      if (!PermissionsManager.areLocationPermissionsGranted(context)) {
        AndroidLocationEngine.getLocationEngine(context).activate();
      } else {
        startPermissionsTimer();
      }

      // Send events timer
      timer = new Timer();
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          flushEventsQueueImmediately();
        }
      }, TelemetryConstants.FLUSH_DELAY_MS, TelemetryConstants.FLUSH_PERIOD_MS);
    } else {
      // Shut it down
      withShutDown = true;
      flushEventsQueueImmediately();
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
    final Handler permsHandler = new Handler();
    Runnable runnable = new Runnable() {

      private ExponentialBackoff exponentialBackoffCounter = new ExponentialBackoff();

      @Override
      public void run() {
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
          AndroidLocationEngine.getLocationEngine(context).activate();
        } else {
          // Restart Handler
          long nextWaitTime = exponentialBackoffCounter.nextBackOffMillis();
          permsHandler.postDelayed(this, nextWaitTime);
        }
      }
    };

    permsHandler.postDelayed(runnable, TelemetryConstants.FLUSH_DELAY_MS);
  }

  /**
   * Centralized method for adding populated event to the queue allowing for cap size checking
   *
   * @param event Event to add to the Events Queue
   */
  private void putEventOnQueue(@NonNull Hashtable<String, Object> event) {
    events.add(event);
    if (events.size() == TelemetryConstants.FLUSH_EVENTS_CAP) {
      flushEventsQueueImmediately();
    }
  }

  /**
   * Adds a Location Event to the system for processing
   *
   * @param location Location event
   */
  public void addLocationEvent(Location location) {
    // NaN and Infinite checks to prevent JSON errors at send to server time
    if (Double.isNaN(location.getLatitude()) || Double.isNaN(location.getLongitude())
      || Double.isNaN(location.getAltitude()) || Float.isNaN(location.getAccuracy())) {
      return;
    }

    if (Double.isInfinite(location.getLatitude()) || Double.isInfinite(location.getLongitude())
      || Double.isInfinite(location.getAltitude()) || Float.isInfinite(location.getAccuracy())) {
      return;
    }

    // Add Location even to queue
    Hashtable<String, Object> event = new Hashtable<>();
    event.put(MapboxEvent.ATTRIBUTE_EVENT, MapboxEvent.TYPE_LOCATION);
    event.put(MapboxEvent.ATTRIBUTE_CREATED, TelemetryUtils.generateCreateDate());
    event.put(MapboxEvent.ATTRIBUTE_SOURCE, MapboxEvent.SOURCE_MAPBOX);
    event.put(MapboxEvent.ATTRIBUTE_SESSION_ID, encodeString(mapboxSessionId));
    event.put(MapboxEvent.KEY_LATITUDE, Math.floor(
      location.getLatitude() * TelemetryConstants.LOCATION_EVENT_ACCURACY)
      / TelemetryConstants.LOCATION_EVENT_ACCURACY);
    event.put(MapboxEvent.KEY_LONGITUDE, Math.floor(
      location.getLongitude() * TelemetryConstants.LOCATION_EVENT_ACCURACY)
      / TelemetryConstants.LOCATION_EVENT_ACCURACY);
    event.put(MapboxEvent.KEY_ALTITUDE, location.getAltitude());
    event.put(MapboxEvent.KEY_HORIZONTAL_ACCURACY, Math.round(location.getAccuracy()));
    event.put(MapboxEvent.ATTRIBUTE_OPERATING_SYSTEM, TelemetryConstants.OPERATING_SYSTEM);
    event.put(MapboxEvent.ATTRIBUTE_APPLICATION_STATE, TelemetryUtils.getApplicationState(context));
    putEventOnQueue(event);

    // Rotate session ID with location events
    rotateSessionId();
  }

  /**
   * Push Interactive Events to the system for processing
   *
   * @param eventWithAttributes Event with attributes
   */
  public void pushEvent(Hashtable<String, Object> eventWithAttributes) {
    if (eventWithAttributes == null) {
      return;
    }

    String eventType = (String) eventWithAttributes.get(MapboxEvent.ATTRIBUTE_EVENT);
    if (TextUtils.isEmpty(eventType)) {
      return;
    }

    if (eventType.equalsIgnoreCase(MapboxEvent.TYPE_MAP_LOAD)) {
      // Map load event
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_USERID, mapboxVendorId);
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_MODEL, Build.MODEL);
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_OPERATING_SYSTEM, TelemetryConstants.OPERATING_SYSTEM);
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_RESOLUTION, displayMetrics.density);
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_ACCESSIBILITY_FONT_SCALE,
        TelemetryUtils.getAccesibilityFontScaleSize(context));
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_ORIENTATION, TelemetryUtils.getOrientation(context));
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_BATTERY_LEVEL, getBatteryLevel());
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_PLUGGED_IN, isPluggedIn());
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_CARRIER,TelemetryUtils.getCellularCarrier(context));
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_CELLULAR_NETWORK_TYPE,
        TelemetryUtils.getCellularNetworkType(context));
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_WIFI, TelemetryUtils.getConnectedToWifi(context));
      putEventOnQueue(eventWithAttributes);
      pushTurnstileEvent();
    } else if (eventType.equalsIgnoreCase(MapboxEvent.TYPE_MAP_CLICK)) {
      // Map click event
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_ORIENTATION, TelemetryUtils.getOrientation(context));
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_BATTERY_LEVEL, getBatteryLevel());
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_PLUGGED_IN, isPluggedIn());
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_CARRIER, TelemetryUtils.getCellularCarrier(context));
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_CELLULAR_NETWORK_TYPE,
        TelemetryUtils.getCellularNetworkType(context));
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_WIFI, TelemetryUtils.getConnectedToWifi(context));
      putEventOnQueue(eventWithAttributes);
    } else if (eventType.equalsIgnoreCase(MapboxEvent.TYPE_MAP_DRAGEND)) {
      // Map drag end event
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_ORIENTATION, TelemetryUtils.getOrientation(context));
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_BATTERY_LEVEL, getBatteryLevel());
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_PLUGGED_IN, isPluggedIn());
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_CARRIER, TelemetryUtils.getCellularCarrier(context));
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_CELLULAR_NETWORK_TYPE,
        TelemetryUtils.getCellularNetworkType(context));
      eventWithAttributes.put(MapboxEvent.ATTRIBUTE_WIFI, TelemetryUtils.getConnectedToWifi(context));
      putEventOnQueue(eventWithAttributes);
    } else {
      Log.w(LOG_TAG, "Unknown event type provided: " + eventType);
    }
  }

  /**
   * Immediately attempt to send all events data in the queue to the server.
   * <p>
   * <p>NOTE: Permission set to package private to enable only telemetry code to use this.
   */
  public void flushEventsQueueImmediately() {
    if (events.size() > 0 && ConnectivityReceiver.isConnected(context)) {
      client.sendEvents(events, this);
    } else if (withShutDown) {
      shutdownTelemetry();
    }
  }

  /**
   * Pushes turnstile event for internal billing purposes
   */
  private void pushTurnstileEvent() {
    Hashtable<String, Object> event = new Hashtable<>();
    event.put(MapboxEvent.ATTRIBUTE_EVENT, MapboxEvent.TYPE_TURNSTILE);
    event.put(MapboxEvent.ATTRIBUTE_CREATED, TelemetryUtils.generateCreateDate());
    event.put(MapboxEvent.ATTRIBUTE_USERID, mapboxVendorId);
    event.put(MapboxEvent.ATTRIBUTE_ENABLED_TELEMETRY, telemetryEnabled);
    events.add(event);
    flushEventsQueueImmediately();
  }

  /**
   * Called when the request could not be executed due to cancellation, a connectivity problem or
   * timeout. Because networks can fail during an exchange, it is possible that the remote server
   * accepted the request before the failure.
   */
  @Override
  public void onFailure(Call call, IOException e) {
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
    withShutDown = false;
    events.removeAllElements();
    context.stopService(new Intent(context, TelemetryService.class));
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
  }
}
