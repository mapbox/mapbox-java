package com.mapbox.services.android.telemetry;

import android.location.Location;
import android.support.annotation.NonNull;

import com.mapbox.services.android.telemetry.utils.TelemetryUtils;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Constants and event builders
 */
public class MapboxEvent implements Serializable {

  // General information
  public static final int VERSION_NUMBER = 2;
  public static final String MAPBOX_EVENTS_BASE_URL = "https://events.mapbox.com";
  public static final String SOURCE_MAPBOX = "mapbox";

  // Event types
  public static final String TYPE_TURNSTILE = "appUserTurnstile";
  public static final String TYPE_MAP_LOAD = "map.load";
  public static final String TYPE_MAP_CLICK = "map.click";
  public static final String TYPE_MAP_DRAGEND = "map.dragend";
  public static final String TYPE_LOCATION = "location";
  public static final String TYPE_VISIT = "visit";

  // Event keys
  public static final String KEY_LATITUDE = "lat";
  public static final String KEY_LONGITUDE = "lng";
  public static final String KEY_SPEED = "speed";
  public static final String KEY_COURSE = "course";
  public static final String KEY_ALTITUDE = "altitude";
  public static final String KEY_HORIZONTAL_ACCURACY = "horizontalAccuracy";
  public static final String KEY_ZOOM = "zoom";

  public static final String KEY_PUSH_ENABLED = "enabled.push";
  public static final String KEY_EMAIL_ENABLED = "enabled.email";
  public static final String KEY_GESTURE_ID = "gesture";
  public static final String KEY_ARRIVAL_DATE = "arrivalDate";
  public static final String KEY_DEPARTURE_DATE = "departureDate";

  public static final String GESTURE_SINGLETAP = "SingleTap";
  public static final String GESTURE_DOUBLETAP = "DoubleTap";
  public static final String GESTURE_TWO_FINGER_SINGLETAP = "TwoFingerTap";
  public static final String GESTURE_QUICK_ZOOM = "QuickZoom";
  public static final String GESTURE_PAN_START = "Pan";
  public static final String GESTURE_PINCH_START = "Pinch";
  public static final String GESTURE_ROTATION_START = "Rotation";
  public static final String GESTURE_PITCH_START = "Pitch";

  // Event attributes
  public static final String ATTRIBUTE_EVENT = "event";
  public static final String ATTRIBUTE_USERID = "userId";
  public static final String ATTRIBUTE_SOURCE = "source";
  public static final String ATTRIBUTE_ENABLED_TELEMETRY = "enabled.telemetry";
  public static final String ATTRIBUTE_SESSION_ID = "sessionId";
  public static final String ATTRIBUTE_VERSION = "version";
  public static final String ATTRIBUTE_CREATED = "created";
  public static final String ATTRIBUTE_VENDOR_ID = "vendorId";
  public static final String ATTRIBUTE_APP_BUNDLE_ID = "appBundleId";
  public static final String ATTRIBUTE_MODEL = "model";
  public static final String ATTRIBUTE_OPERATING_SYSTEM = "operatingSystem";
  public static final String ATTRIBUTE_ORIENTATION = "orientation";
  public static final String ATTRIBUTE_BATTERY_LEVEL = "batteryLevel";
  public static final String ATTRIBUTE_PLUGGED_IN = "pluggedIn";
  public static final String ATTRIBUTE_APPLICATION_STATE = "applicationState";
  public static final String ATTRIBUTE_RESOLUTION = "resolution";
  public static final String ATTRIBUTE_ACCESSIBILITY_FONT_SCALE = "accessibilityFontScale";
  public static final String ATTRIBUTE_CARRIER = "carrier";
  public static final String ATTRIBUTE_CELLULAR_NETWORK_TYPE = "cellularNetworkType";
  public static final String ATTRIBUTE_WIFI = "wifi";

  /**
   * Helper method for tracking gesture events
   *
   * @param gestureId Type of Gesture See {@see MapboxEvent#GESTURE_SINGLETAP
   *                  MapboxEvent#GESTURE_DOUBLETAP
   *                  MapboxEvent#GESTURE_TWO_FINGER_SINGLETAP
   *                  MapboxEvent#GESTURE_QUICK_ZOOM
   *                  MapboxEvent#GESTURE_PAN_START
   *                  MapboxEvent#GESTURE_PINCH_START
   *                  MapboxEvent#GESTURE_ROTATION_START
   *                  MapboxEvent#GESTURE_PITCH_START}
   * @param location  Location coordinates at start of gesture
   * @param zoom      Zoom level to be registered
   */
  public static Hashtable<String, Object> buildGestureEvent(
    @NonNull Location location, @NonNull String gestureId, double zoom) {
    // NaN and Infinite checks to prevent JSON errors at send to server time
    if (Double.isNaN(location.getLatitude()) || Double.isNaN(location.getLongitude())) {
      return null;
    }

    if (Double.isInfinite(location.getLatitude()) || Double.isInfinite(location.getLongitude())) {
      return null;
    }

    Hashtable<String, Object> evt = new Hashtable<>();
    evt.put(MapboxEvent.ATTRIBUTE_EVENT, MapboxEvent.TYPE_MAP_CLICK);
    evt.put(MapboxEvent.ATTRIBUTE_CREATED, TelemetryUtils.generateCreateDate());
    evt.put(MapboxEvent.KEY_GESTURE_ID, gestureId);
    evt.put(MapboxEvent.KEY_LATITUDE, location.getLatitude());
    evt.put(MapboxEvent.KEY_LONGITUDE, location.getLongitude());
    evt.put(MapboxEvent.KEY_ZOOM, zoom);
    return evt;
  }

  /**
   * Helper method for tracking DragEnd gesture event
   * See {@see MapboxEvent#TYPE_MAP_DRAGEND}
   *
   * @param location Original location coordinate at end of drag
   * @param zoom     Zoom level to be registered
   */
  public static Hashtable<String, Object> buildGestureDragEndEvent(
    @NonNull Location location, double zoom) {
    // NaN and Infinite checks to prevent JSON errors at send to server time
    if (Double.isNaN(location.getLatitude()) || Double.isNaN(location.getLongitude())) {
      return null;
    }

    if (Double.isInfinite(location.getLatitude()) || Double.isInfinite(location.getLongitude())) {
      return null;
    }

    Hashtable<String, Object> evt = new Hashtable<>();
    evt.put(MapboxEvent.ATTRIBUTE_EVENT, MapboxEvent.TYPE_MAP_DRAGEND);
    evt.put(MapboxEvent.ATTRIBUTE_CREATED, TelemetryUtils.generateCreateDate());
    evt.put(MapboxEvent.KEY_LATITUDE, location.getLatitude());
    evt.put(MapboxEvent.KEY_LONGITUDE, location.getLongitude());
    evt.put(MapboxEvent.KEY_ZOOM, zoom);
    return evt;
  }

  /**
   * Helper method for tracking map load event
   */
  public static Hashtable<String, Object> buildMapLoadEvent() {
    Hashtable<String, Object> evt = new Hashtable<>();
    evt.put(MapboxEvent.ATTRIBUTE_EVENT, MapboxEvent.TYPE_MAP_LOAD);
    evt.put(MapboxEvent.ATTRIBUTE_CREATED, TelemetryUtils.generateCreateDate());
    return evt;
  }
}
