package com.mapbox.services.android.telemetry.navigation;

import android.location.Location;

import com.mapbox.services.android.telemetry.constants.TelemetryConstants;
import com.mapbox.services.android.telemetry.utils.TelemetryUtils;

import java.util.Date;
import java.util.Hashtable;

/**
 * Represent events dispatched by the Navigation SDK.
 */
public class MapboxNavigationEvent {

  private static final int EVENT_VERSION = 1;

  // Event types
  public static final String TYPE_TURNSTILE = "navigation.turnstile";
  public static final String TYPE_DEPART = "navigation.depart";
  public static final String TYPE_FEEDBACK = "navigation.feedback";
  public static final String TYPE_ARRIVE = "navigation.arrive";
  public static final String TYPE_CANCEL = "navigation.cancel";

  // Event keys
  public static final String KEY_EVENT = "event";
  public static final String KEY_PLATFORM = "platform";
  public static final String KEY_OPERATING_SYSTEM = "operatingSystem";
  public static final String KEY_SDK_IDENTIFIER = "sdkIdentifier";
  public static final String KEY_SDK_VERSION = "sdkVersion";
  public static final String KEY_EVENT_VERSION = "eventVersion";
  public static final String KEY_SESSION_UUID = "sessionUUID";
  public static final String KEY_GEOMETRY = "geometry";
  public static final String KEY_CREATED = "created";
  public static final String KEY_PROFILE = "profile";
  public static final String KEY_ESTIMATED_DISTANCE = "estimatedDistance";
  public static final String KEY_ESTIMATED_DURATION = "estimatedDuration";
  public static final String KEY_REROUTE_COUNT = "rerouteCount";
  public static final String KEY_FEEDBACK_TYPE = "feedbackType";
  public static final String KEY_LOCATIONS_BEFORE = "locationsBefore";
  public static final String KEY_LOCATIONS_AFTER = "locationsAfter";
  public static final String KEY_CURRENT_DISTANCE = "currentDistance";
  public static final String KEY_CURRENT_DURATION = "currentDuration";
  public static final String KEY_NEW_ESTIMATED_DISTANCE = "newEstimatedDistance";
  public static final String KEY_NEW_ESTIMATED_DURATION = "newEstimatedDuration";
  public static final String KEY_START_TIMESTAMP = "startTimestamp";
  public static final String KEY_COMPLETED_DISTANCE = "completedDistance";
  public static final String KEY_COMPLETED_DURATION = "completedDuration";

  /**
   * Navigation turnstile. The Navigation SDK is
   * in charge of keeping track of a navigation session, and it should use
   * {@link TelemetryUtils#buildUUID()} to generate the random UUID.
   */
  public static Hashtable<String, Object> buildTurnstileEvent(String sdKIdentifier, String sdkVersion) {
    Hashtable<String, Object> event = new Hashtable<>();
    event.put(KEY_EVENT, TYPE_TURNSTILE);
    event.put(KEY_PLATFORM, TelemetryConstants.PLATFORM);
    event.put(KEY_OPERATING_SYSTEM, TelemetryConstants.OPERATING_SYSTEM);
    event.put(KEY_SDK_IDENTIFIER, sdKIdentifier);
    event.put(KEY_SDK_VERSION, sdkVersion);
    return event;
  }

  /**
   * User started a route.
   */
  public static Hashtable<String, Object> buildDepartEvent(
      String sdKIdentifier, String sdkVersion, String sessionUUID, String geometry, String profile,
      int estimatedDistance, int estimatedDuration, int rerouteCount) {
    Hashtable<String, Object> event = getMetadata(sdKIdentifier, sdkVersion, sessionUUID, geometry,
        profile, estimatedDistance, estimatedDuration, rerouteCount);
    event.put(KEY_EVENT, TYPE_DEPART);
    return event;
  }

  /**
   * User feedback/reroute event.
   */
  public static Hashtable<String, Object> buildFeedbackEvent(
      String sdKIdentifier, String sdkVersion, String sessionUUID, String geometry, String profile,
      int estimatedDistance, int estimatedDuration, int rerouteCount, String feedbackType,
      Location[] locationsBefore, Location[] locationsAfter, int currentDistance,
      int currentDuration, int newEstimatedDistance, int newEstimatedDuration) {
    Hashtable<String, Object> event = getMetadata(sdKIdentifier, sdkVersion, sessionUUID, geometry,
        profile, estimatedDistance, estimatedDuration, rerouteCount);
    event.put(KEY_EVENT, TYPE_FEEDBACK);
    event.put(KEY_FEEDBACK_TYPE, feedbackType);
    event.put(KEY_LOCATIONS_BEFORE, locationsBefore);
    event.put(KEY_LOCATIONS_AFTER, locationsAfter);
    event.put(KEY_CURRENT_DISTANCE, currentDistance);
    event.put(KEY_CURRENT_DURATION, currentDuration);
    event.put(KEY_NEW_ESTIMATED_DISTANCE, newEstimatedDistance);
    event.put(KEY_NEW_ESTIMATED_DURATION, newEstimatedDuration);
    return event;
  }

  /**
   * User arrived.
   */
  public static Hashtable<String, Object> buildArriveEvent(
      String sdKIdentifier, String sdkVersion, String sessionUUID, String geometry, String profile,
      int estimatedDistance, int estimatedDuration, int rerouteCount, Date startTimestamp,
      int completedDistance, int completedDuration) {
    Hashtable<String, Object> event = getMetadata(sdKIdentifier, sdkVersion, sessionUUID, geometry,
        profile, estimatedDistance, estimatedDuration, rerouteCount);
    event.put(KEY_EVENT, TYPE_ARRIVE);
    event.put(KEY_START_TIMESTAMP, TelemetryUtils.generateCreateDateFormatted(startTimestamp));
    event.put(KEY_COMPLETED_DISTANCE, completedDistance);
    event.put(KEY_COMPLETED_DURATION, completedDuration);
    return event;
  }

  /**
   * User canceled navigation.
   */
  public static Hashtable<String, Object> buildCancelEvent(
      String sdKIdentifier, String sdkVersion, String sessionUUID, String geometry, String profile,
      int estimatedDistance, int estimatedDuration, int rerouteCount, Date startTimestamp,
      int completedDistance, int completedDuration) {
    Hashtable<String, Object> event = getMetadata(sdKIdentifier, sdkVersion, sessionUUID, geometry,
        profile, estimatedDistance, estimatedDuration, rerouteCount);
    event.put(KEY_EVENT, TYPE_CANCEL);
    event.put(KEY_START_TIMESTAMP, TelemetryUtils.generateCreateDateFormatted(startTimestamp));
    event.put(KEY_COMPLETED_DISTANCE, completedDistance);
    event.put(KEY_COMPLETED_DURATION, completedDuration);
    return event;
  }

  /**
   * The following metadata should be attached to all non-turnstile events. The Navigation SDK is
   * in charge of keeping track of a navigation session, and it should use
   * {@link TelemetryUtils#buildUUID()} to generate the random UUID.
   */
  private static Hashtable<String, Object> getMetadata(
      String sdKIdentifier, String sdkVersion, String sessionUUID, String geometry, String profile,
      int estimatedDistance, int estimatedDuration, int rerouteCount) {
    Hashtable<String, Object> event = new Hashtable<>();
    event.put(KEY_PLATFORM, TelemetryConstants.PLATFORM);
    event.put(KEY_OPERATING_SYSTEM, TelemetryConstants.OPERATING_SYSTEM);
    event.put(KEY_SDK_IDENTIFIER, sdKIdentifier);
    event.put(KEY_SDK_VERSION, sdkVersion);
    event.put(KEY_EVENT_VERSION, EVENT_VERSION);
    event.put(KEY_SESSION_UUID, sessionUUID);
    event.put(KEY_GEOMETRY, geometry);
    event.put(KEY_CREATED, TelemetryUtils.generateCreateDate(null));
    event.put(KEY_PROFILE, profile);
    event.put(KEY_ESTIMATED_DISTANCE, estimatedDistance);
    event.put(KEY_ESTIMATED_DURATION, estimatedDuration);
    event.put(KEY_REROUTE_COUNT, rerouteCount);
    return event;
  }
}
