package com.mapbox.services.android.telemetry.http;

import android.text.TextUtils;
import android.util.Log;

import com.mapbox.services.android.telemetry.MapboxEvent;
import com.mapbox.services.android.telemetry.constants.GeoConstants;
import com.mapbox.services.android.telemetry.utils.MathUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Vector;

import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * HTTP client to Mapbox telemetry
 */
public class TelemetryClient {

  private static final String LOG_TAG = TelemetryClient.class.getSimpleName();

  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

  private String accessToken = null;

  private String userAgent = null;
  private String eventsEndpoint = MapboxEvent.MAPBOX_EVENTS_BASE_URL;
  private boolean stagingEnvironment = false;
  private OkHttpClient client;

  public TelemetryClient(String accessToken) {
    this.accessToken = accessToken;
    client = new OkHttpClient.Builder()
      .certificatePinner(buildCertificatePinner())
      .addInterceptor(new GzipRequestInterceptor())
      .retryOnConnectionFailure(true)
      .build();
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public String getEventsEndpoint() {
    return eventsEndpoint;
  }

  public void setEventsEndpoint(String eventsEndpoint) {
    this.eventsEndpoint = eventsEndpoint;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public boolean isStagingEnvironment() {
    return stagingEnvironment;
  }

  public void setStagingEnvironment(boolean stagingEnvironment) {
    this.stagingEnvironment = stagingEnvironment;
  }

  /**
   * Based on http://square.github.io/okhttp/3.x/okhttp/okhttp3/CertificatePinner.html
   *
   * @return The CertificatePinner instance
   */
  private CertificatePinner buildCertificatePinner() {
    CertificatePinner.Builder certificatePinnerBuilder = new CertificatePinner.Builder();

    if (isStagingEnvironment()) {
      // Staging - Geotrust
      certificatePinnerBuilder
        .add("cloudfront-staging.tilestream.net", "sha256/3euxrJOrEZI15R4104UsiAkDqe007EPyZ6eTL/XxdAY=")
        .add("cloudfront-staging.tilestream.net", "sha256/5kJvNEMw0KjrCAu7eXY5HZdvyCS13BbA0VJG1RSP91w=")
        .add("cloudfront-staging.tilestream.net", "sha256/r/mIkG3eEpVdm+u/ko/cwxzOMo1bk4TyHIlByibiA5E=");
    } else {
      certificatePinnerBuilder
        // Prod - Geotrust
        .add("events.mapbox.com", "sha256/BhynraKizavqoC5U26qgYuxLZst6pCu9J5stfL6RSYY=")
        .add("events.mapbox.com", "sha256/owrR9U9FWDWtrFF+myoRIu75JwU4sJwzvhCNLZoY37g=")
        .add("events.mapbox.com", "sha256/SQVGZiOrQXi+kqxcvWWE96HhfydlLVqFr4lQTqI5qqo=")
        // Prod - DigiCert
        .add("events.mapbox.com", "sha256/Tb0uHZ/KQjWh8N9+CZFLc4zx36LONQ55l6laDi1qtT4=")
        .add("events.mapbox.com", "sha256/RRM1dGqnDFsCJXBTHky16vi1obOlCgFFn/yOhI/y+ho=")
        .add("events.mapbox.com", "sha256/WoiWRyIOVNa9ihaBciRSC7XHjliYS9VwUGOIud4PB18=");
    }

    return certificatePinnerBuilder.build();
  }

  public void sendEvents(Vector<Hashtable<String, Object>> events, Callback callback) {
    if (events == null || events.size() == 0) {
      return;
    }

    try {
      sendEventsWrapped(events, callback);
    } catch (Exception exception) {
      Log.e(LOG_TAG, String.format("Request preparation failed: %s.", exception.getMessage()));
    }
  }

  private void sendEventsWrapped(Vector<Hashtable<String, Object>> events, Callback callback) throws JSONException {
    JSONArray jsonArray = new JSONArray();
    for (Hashtable<String, Object> evt : events) {
      JSONObject jsonObject = new JSONObject();

      // Build the JSON but only if there's a value for it in the evt
      jsonObject.putOpt(MapboxEvent.KEY_EVENT, evt.get(MapboxEvent.KEY_EVENT));
      jsonObject.putOpt(MapboxEvent.KEY_CREATED, evt.get(MapboxEvent.KEY_CREATED));
      jsonObject.putOpt(MapboxEvent.KEY_USER_ID, evt.get(MapboxEvent.KEY_USER_ID));
      jsonObject.putOpt(MapboxEvent.KEY_ENABLED_TELEMETRY, evt.get(MapboxEvent.KEY_ENABLED_TELEMETRY));
      jsonObject.putOpt(MapboxEvent.KEY_SOURCE, evt.get(MapboxEvent.KEY_SOURCE));
      jsonObject.putOpt(MapboxEvent.KEY_SESSION_ID, evt.get(MapboxEvent.KEY_SESSION_ID));
      jsonObject.putOpt(MapboxEvent.KEY_LATITUDE, evt.get(MapboxEvent.KEY_LATITUDE));

      // Make sure longitude is wrapped
      if (evt.containsKey(MapboxEvent.KEY_LONGITUDE)) {
        double lon = (double) evt.get(MapboxEvent.KEY_LONGITUDE);
        if ((lon < GeoConstants.MIN_LONGITUDE) || (lon > GeoConstants.MAX_LONGITUDE)) {
          lon = MathUtils.wrap(lon, GeoConstants.MIN_LONGITUDE, GeoConstants.MAX_LONGITUDE);
        }
        jsonObject.put(MapboxEvent.KEY_LONGITUDE, lon);
      }

      jsonObject.putOpt(MapboxEvent.KEY_ALTITUDE, evt.get(MapboxEvent.KEY_ALTITUDE));
      jsonObject.putOpt(MapboxEvent.KEY_ZOOM, evt.get(MapboxEvent.KEY_ZOOM));
      jsonObject.putOpt(MapboxEvent.KEY_OPERATING_SYSTEM, evt.get(MapboxEvent.KEY_OPERATING_SYSTEM));
      jsonObject.putOpt(MapboxEvent.KEY_USER_ID, evt.get(MapboxEvent.KEY_USER_ID));
      jsonObject.putOpt(MapboxEvent.KEY_MODEL, evt.get(MapboxEvent.KEY_MODEL));
      jsonObject.putOpt(MapboxEvent.KEY_RESOLUTION, evt.get(MapboxEvent.KEY_RESOLUTION));
      jsonObject.putOpt(MapboxEvent.KEY_ACCESSIBILITY_FONT_SCALE, evt.get(MapboxEvent.KEY_ACCESSIBILITY_FONT_SCALE));
      jsonObject.putOpt(MapboxEvent.KEY_BATTERY_LEVEL, evt.get(MapboxEvent.KEY_BATTERY_LEVEL));
      jsonObject.putOpt(MapboxEvent.KEY_PLUGGED_IN, evt.get(MapboxEvent.KEY_PLUGGED_IN));
      jsonObject.putOpt(MapboxEvent.KEY_WIFI, evt.get(MapboxEvent.KEY_WIFI));

      // Special cases where an empty string is denoting null and therefore should not be sent at all
      // This arises as thread safe hashtables do not accept null values (nor keys)
      if (evt.containsKey(MapboxEvent.KEY_ORIENTATION)) {
        String orientation = (String) evt.get(MapboxEvent.KEY_ORIENTATION);
        if (!TextUtils.isEmpty(orientation)) {
          jsonObject.putOpt(MapboxEvent.KEY_ORIENTATION, orientation);
        }
      }

      if (evt.containsKey(MapboxEvent.KEY_CARRIER)) {
        String carrier = (String) evt.get(MapboxEvent.KEY_CARRIER);
        if (!TextUtils.isEmpty(carrier)) {
          jsonObject.putOpt(MapboxEvent.KEY_CARRIER, carrier);
        }
      }

      if (evt.containsKey(MapboxEvent.KEY_APPLICATION_STATE)) {
        String appState = (String) evt.get(MapboxEvent.KEY_APPLICATION_STATE);
        if (!TextUtils.isEmpty(appState)) {
          jsonObject.putOpt(MapboxEvent.KEY_APPLICATION_STATE,
            evt.get(MapboxEvent.KEY_APPLICATION_STATE));
        }
      }

      // Special cases where null has to be passed if no value exists
      // Requires using put() instead of putOpt()
      String eventType = (String) evt.get(MapboxEvent.KEY_EVENT);
      if (!TextUtils.isEmpty(eventType) && eventType.equalsIgnoreCase(MapboxEvent.TYPE_MAP_CLICK)) {
        jsonObject.put(MapboxEvent.KEY_GESTURE_ID, evt.get(MapboxEvent.KEY_GESTURE_ID));
      }

      if (evt.containsKey(MapboxEvent.KEY_CELLULAR_NETWORK_TYPE)) {
        String cellularNetworkType = (String) evt.get(MapboxEvent.KEY_CELLULAR_NETWORK_TYPE);
        if (TextUtils.isEmpty(cellularNetworkType)) {
          jsonObject.put(MapboxEvent.KEY_CELLULAR_NETWORK_TYPE, null);
        } else {
          jsonObject.put(MapboxEvent.KEY_CELLULAR_NETWORK_TYPE,
            evt.get(MapboxEvent.KEY_CELLULAR_NETWORK_TYPE));
        }
      }

      jsonArray.put(jsonObject);
    }

    // Build body and URL
    String payload = jsonArray.toString();
    RequestBody body = RequestBody.create(JSON, payload);
    String url = getEventsEndpoint() + "/events/v2?access_token=" + getAccessToken();

    // Extra debug in staging mode
    if (isStagingEnvironment()) {
      Log.d(LOG_TAG, String.format("Sending POST to %s with %d event(s) (user agent: %s) with payload: %s",
        url, events.size(), getUserAgent(), payload));
    }

    // Async request
    Request request = new Request.Builder()
      .url(url)
      .header("User-Agent", getUserAgent())
      .post(body)
      .build();
    client.newCall(request).enqueue(callback);
  }

}
