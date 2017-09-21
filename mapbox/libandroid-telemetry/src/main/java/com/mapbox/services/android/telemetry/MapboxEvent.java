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

  // Events and attributes for v2
  public static final String MAPBOX_EVENTS_BASE_URL = "https://events.mapbox.com";
  public static final String MAPBOX_EVENTS_CN_BASE_URL = "https://events.mapbox.cn";
  public static final String SOURCE_MAPBOX = "mapbox";

  // Event types
  public static final String TYPE_TURNSTILE = "appUserTurnstile";
  public static final String TYPE_MAP_LOAD = "map.load";
  public static final String TYPE_MAP_CLICK = "map.click";
  public static final String TYPE_MAP_DRAG_END = "map.dragend";
  public static final String TYPE_LOCATION = "location";

  // Event keys
  public static final String KEY_EVENT = "event";
  public static final String KEY_CREATED = "created";
  public static final String KEY_USER_ID = "userId";
  public static final String KEY_ENABLED_TELEMETRY = "enabled.telemetry";
  public static final String KEY_MODEL = "model";
  public static final String KEY_OPERATING_SYSTEM = "operatingSystem";
  public static final String KEY_RESOLUTION = "resolution";
  public static final String KEY_ACCESSIBILITY_FONT_SCALE = "accessibilityFontScale";
  public static final String KEY_ORIENTATION = "orientation";
  public static final String KEY_BATTERY_LEVEL = "batteryLevel";
  public static final String KEY_PLUGGED_IN = "pluggedIn";
  public static final String KEY_CARRIER = "carrier";
  public static final String KEY_CELLULAR_NETWORK_TYPE = "cellularNetworkType";
  public static final String KEY_WIFI = "wifi";
  public static final String KEY_GESTURE_ID = "gesture";
  public static final String KEY_LATITUDE = "lat";
  public static final String KEY_LONGITUDE = "lng";
  public static final String KEY_ZOOM = "zoom";
  public static final String KEY_SOURCE = "source";
  public static final String KEY_SESSION_ID = "sessionId";
  public static final String KEY_ALTITUDE = "altitude";
  public static final String KEY_APPLICATION_STATE = "applicationState";
  public static final String KEY_HORIZONTAL_ACCURACY = "horizontalAccuracy";

  // Gestures
  public static final String GESTURE_SINGLETAP = "SingleTap";
  public static final String GESTURE_DOUBLETAP = "DoubleTap";
  public static final String GESTURE_TWO_FINGER_SINGLETAP = "TwoFingerTap";
  public static final String GESTURE_PAN_START = "Pan";
  public static final String GESTURE_PINCH_START = "Pinch";
  public static final String GESTURE_ROTATION_START = "Rotation";
  public static final String GESTURE_PITCH_START = "Pitch";
  public static final String GESTURE_QUICK_ZOOM = "QuickZoom";

  /**
   * Helper method for tracking gesture events
   *
   * @param gestureId Type of Gesture
   * @see MapboxEvent#GESTURE_SINGLETAP
   * @see MapboxEvent#GESTURE_DOUBLETAP
   * @see MapboxEvent#GESTURE_TWO_FINGER_SINGLETAP
   * @see MapboxEvent#GESTURE_QUICK_ZOOM
   * @see MapboxEvent#GESTURE_PAN_START
   * @see MapboxEvent#GESTURE_PINCH_START
   * @see MapboxEvent#GESTURE_ROTATION_START
   * @see MapboxEvent#GESTURE_PITCH_START
   * @param location  Location coordinates at start of gesture
   * @param zoom      Zoom level to be registered
   */
  public static Hashtable<String, Object> buildMapClickEvent(
    @NonNull Location location, @NonNull String gestureId, double zoom) {
    // NaN and Infinite checks to prevent JSON errors at send to server time
    if (Double.isNaN(location.getLatitude()) || Double.isNaN(location.getLongitude())) {
      return null;
    }

    if (Double.isInfinite(location.getLatitude()) || Double.isInfinite(location.getLongitude())) {
      return null;
    }

    Hashtable<String, Object> evt = new Hashtable<>();
    evt.put(MapboxEvent.KEY_EVENT, MapboxEvent.TYPE_MAP_CLICK);
    evt.put(MapboxEvent.KEY_CREATED, TelemetryUtils.generateCreateDate(location));
    evt.put(MapboxEvent.KEY_GESTURE_ID, gestureId);
    evt.put(MapboxEvent.KEY_LATITUDE, location.getLatitude());
    evt.put(MapboxEvent.KEY_LONGITUDE, location.getLongitude());
    evt.put(MapboxEvent.KEY_ZOOM, zoom);
    return evt;
  }

  /**
   * Helper method for tracking DragEnd gesture event
   * @see MapboxEvent#TYPE_MAP_DRAG_END
   *
   * @param location Original location coordinate at end of drag
   * @param zoom     Zoom level to be registered
   */
  public static Hashtable<String, Object> buildMapDragEndEvent(
    @NonNull Location location, double zoom) {
    // NaN and Infinite checks to prevent JSON errors at send to server time
    if (Double.isNaN(location.getLatitude()) || Double.isNaN(location.getLongitude())) {
      return null;
    }

    if (Double.isInfinite(location.getLatitude()) || Double.isInfinite(location.getLongitude())) {
      return null;
    }

    Hashtable<String, Object> evt = new Hashtable<>();
    evt.put(MapboxEvent.KEY_EVENT, MapboxEvent.TYPE_MAP_DRAG_END);
    evt.put(MapboxEvent.KEY_CREATED, TelemetryUtils.generateCreateDate(location));
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
    evt.put(MapboxEvent.KEY_EVENT, MapboxEvent.TYPE_MAP_LOAD);
    evt.put(MapboxEvent.KEY_CREATED, TelemetryUtils.generateCreateDate(null));
    return evt;
  }
}
