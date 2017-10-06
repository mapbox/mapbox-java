package com.mapbox.services.android.telemetry.navigation;

import android.location.Location;

import com.mapbox.services.android.telemetry.constants.TelemetryConstants;
import com.mapbox.services.android.telemetry.utils.TelemetryUtils;

import org.json.JSONObject;

import java.util.Date;
import java.util.Hashtable;

/**
 * Represent events dispatched by the Navigation SDK.
 */
public class MapboxNavigationEvent {

  private static final int EVENT_VERSION = 3;

  // Event types
  public static final String TYPE_TURNSTILE = "navigation.turnstile";
  public static final String TYPE_DEPART = "navigation.depart";
  public static final String TYPE_FEEDBACK = "navigation.feedback";
  public static final String TYPE_REROUTE = "navigation.reroute";
  public static final String TYPE_ARRIVE = "navigation.arrive";
  public static final String TYPE_CANCEL = "navigation.cancel";

  // Event keys
  public static final String KEY_EVENT = "event";
  public static final String KEY_OPERATING_SYSTEM = "operatingSystem";
  public static final String KEY_SDK_IDENTIFIER = "sdkIdentifier";
  public static final String KEY_SDK_VERSION = "sdkVersion";
  public static final String KEY_EVENT_VERSION = "eventVersion";
  public static final String KEY_SESSION_IDENTIFIER = "sessionIdentifier";
  public static final String KEY_ORIGINAL_REQUEST_IDENTIFIER = "originalRequestIdentifier";
  public static final String KEY_REQUEST_IDENTIFIER = "requestIdentifier";
  public static final String KEY_LAT = "lat";
  public static final String KEY_LNG = "lng";
  public static final String KEY_ORIGINAL_GEOMETRY = "originalGeometry";
  public static final String KEY_ORIGINAL_ESTIMATED_DISTANCE = "originalEstimatedDistance";
  public static final String KEY_ORIGINAL_ESTIMATED_DURATION = "originalEstimatedDuration";
  public static final String KEY_AUDIO_TYPE = "audioType";
  public static final String KEY_GEOMETRY = "geometry";
  public static final String KEY_CREATED = "created";
  public static final String KEY_PROFILE = "profile";
  public static final String KEY_SIMULATION = "simulation";
  public static final String KEY_ESTIMATED_DISTANCE = "estimatedDistance";
  public static final String KEY_ESTIMATED_DURATION = "estimatedDuration";
  public static final String KEY_REROUTE_COUNT = "rerouteCount";
  public static final String KEY_DEVICE = "device";
  public static final String KEY_VOLUME_LEVEL = "volumeLevel";
  public static final String KEY_SCREEN_BRIGHTNESS = "screenBrightness";
  public static final String KEY_APPLICATION_STATE = "applicationState";
  public static final String KEY_BATTERY_PLUGGED_IN = "batteryPluggedIn";
  public static final String KEY_BATTERY_LEVEL = "batteryLevel";
  public static final String KEY_CONNECTIVITY = "connectivity";
  public static final String KEY_FEEDBACK_TYPE = "feedbackType";
  public static final String KEY_DESCRIPTIONS = "description";
  public static final String KEY_LOCATIONS_BEFORE = "locationsBefore";
  public static final String KEY_LOCATIONS_AFTER = "locationsAfter";
  public static final String KEY_USER_ID = "userId";
  public static final String KEY_FEEDBACK_ID = "feedbackId";
  public static final String KEY_SCREENSHOT = "screenshot";
  public static final String KEY_NEW_DISTANCE_REMAINING = "newDistanceRemaining";
  public static final String KEY_NEW_DURATION_REMAINING = "newDurationRemaining";
  public static final String KEY_NEW_GEOMETRY = "newGeometry";
  public static final String KEY_START_TIMESTAMP = "startTimestamp";
  public static final String KEY_DISTANCE_COMPLETED = "distanceCompleted";
  public static final String KEY_DISTANCE_REMAINING = "distanceRemaining";
  public static final String KEY_DURATION_REMAINING = "durationRemaining";
  public static final String KEY_SECONDS_SINCE_LAST_REROUTE = "secondsSinceLastReroute";
  public static final String KEY_ARRIVAL_TIMESTAMP = "arrivalTimestamp";
  public static final String KEY_STEP = "step";
  public static final String KEY_STEP_COUNT = "stepCount";

  // Step metadata
  public static final String KEY_UPCOMING_INSTRUCTION = "upcomingInstruction";
  public static final String KEY_UPCOMING_MODIFIER = "upcomingModifier";
  public static final String KEY_UPCOMING_NAME = "upcomingName";
  public static final String KEY_PREVIOUS_INSTRUCTION = "previousInstruction";
  public static final String KEY_PREVIOUS_TYPE = "previousType";
  public static final String KEY_PREVIOUS_MODIFIER = "previousModifier";
  public static final String KEY_PREVIOUS_NAME = "previousName";
  public static final String KEY_UPCOMING_TYPE = "upcomingType";
  public static final String KEY_DURATION = "duration";
  public static final String KEY_DISTANCE = "distance";
  public static final String KEY_ORIGINAL_STEP_COUNT = "originalStepCount";

  /**
   * Navigation turnstile.
   */
  public static Hashtable<String, Object> buildTurnstileEvent(String sdKIdentifier, String sdkVersion) {
    Hashtable<String, Object> event = new Hashtable<>();
    event.put(KEY_EVENT, TYPE_TURNSTILE);
    event.put(KEY_OPERATING_SYSTEM, TelemetryConstants.OPERATING_SYSTEM);
    event.put(KEY_SDK_IDENTIFIER, sdKIdentifier);
    event.put(KEY_SDK_VERSION, sdkVersion);
    return event;
  }

  /**
   * User started a route.
   */
  public static Hashtable<String, Object> buildDepartEvent(
    String sdKIdentifier, String sdkVersion, String sessionIdentifier, double lat, double lng,
    String geometry, String profile, int estimatedDistance, int estimatedDuration,
    int rerouteCount, boolean isSimulation, String originalRequestIdentifier,
    String requestIdentifier, String originalGeometry, int originalEstimatedDistance,
    int originalEstimatedDuration, String audioType, int stepCount, int originalStepCount,
    int distanceCompleted, int distanceRemaining, int durationRemaining, Date startTimestamp) {
    Hashtable<String, Object> event = getMetadata(sdKIdentifier, sdkVersion, sessionIdentifier,
      lat, lng, geometry, profile, estimatedDistance, estimatedDuration, rerouteCount,
      isSimulation, originalRequestIdentifier, requestIdentifier, originalGeometry,
      originalEstimatedDistance, originalEstimatedDuration, audioType, stepCount, originalStepCount);
    event.put(KEY_EVENT, TYPE_DEPART);
    event.put(KEY_START_TIMESTAMP, TelemetryUtils.generateCreateDateFormatted(startTimestamp));
    event.put(KEY_DISTANCE_COMPLETED, distanceCompleted);
    event.put(KEY_DISTANCE_REMAINING, distanceRemaining);
    event.put(KEY_DURATION_REMAINING, durationRemaining);
    return event;
  }

  /**
   * User feedback event.
   */
  public static Hashtable<String, Object> buildFeedbackEvent(
    String sdKIdentifier, String sdkVersion, String sessionIdentifier, double lat, double lng,
    String geometry, String profile, int estimatedDistance, int estimatedDuration,
    int rerouteCount, Date startTimestamp, String feedbackType,
    Location[] locationsBefore, Location[] locationsAfter, int distanceCompleted,
    int distanceRemaining, int durationRemaining, String description, String userId, String feedbackId,
    String encodedSnapshot, boolean isSimulation, String originalRequestIdentifier,
    String requestIdentifier, String originalGeometry, int originalEstimatedDistance,
    int originalEstimatedDuration, String audioType, String upcomingInstruction, String upcomingType,
    String upcomingModifier, String upcomingName, String previousInstruction, String previousType,
    String previousModifier, String previousName, int distance, int duration, int stepDistanceRemaining,
    int stepDurationRemaining, int stepCount, int originalStepCount) {
    Hashtable<String, Object> event = getMetadata(sdKIdentifier, sdkVersion, sessionIdentifier,
      lat, lng, geometry, profile, estimatedDistance, estimatedDuration, rerouteCount,
      isSimulation, originalRequestIdentifier, requestIdentifier, originalGeometry,
      originalEstimatedDistance, originalEstimatedDuration, audioType, stepCount, originalStepCount);
    event.put(KEY_STEP, getStepMetadata(upcomingInstruction, upcomingType, upcomingModifier, upcomingName,
      previousInstruction, previousType, previousModifier, previousName, distance, duration,
      stepDistanceRemaining, stepDurationRemaining));
    event.put(KEY_EVENT, TYPE_FEEDBACK);
    event.put(KEY_DESCRIPTIONS, description);
    event.put(KEY_USER_ID, userId);
    event.put(KEY_FEEDBACK_ID, feedbackId);
    event.put(KEY_SCREENSHOT, encodedSnapshot);
    event.put(KEY_START_TIMESTAMP, TelemetryUtils.generateCreateDateFormatted(startTimestamp));
    event.put(KEY_FEEDBACK_TYPE, feedbackType);
    if (locationsBefore != null) {
      event.put(KEY_LOCATIONS_BEFORE, locationsBefore);
    } else {
      event.put(KEY_LOCATIONS_BEFORE, JSONObject.NULL);
    }
    if (locationsAfter != null) {
      event.put(KEY_LOCATIONS_AFTER, locationsAfter);
    } else {
      event.put(KEY_LOCATIONS_AFTER, JSONObject.NULL);
    }
    event.put(KEY_DISTANCE_COMPLETED, distanceCompleted);
    event.put(KEY_DISTANCE_REMAINING, distanceRemaining);
    event.put(KEY_DURATION_REMAINING, durationRemaining);
    return event;
  }

  /**
   * User reroute event.
   */
  public static Hashtable<String, Object> buildRerouteEvent(
    String sdKIdentifier, String sdkVersion, String sessionIdentifier, double lat, double lng,
    String geometry, String profile, int estimatedDistance, int estimatedDuration,
    int rerouteCount, Date startTimestamp, Location[] locationsBefore, Location[] locationsAfter,
    int distanceCompleted, int distanceRemaining, int durationRemaining, int newDistanceRemaining,
    int newDurationRemaining, int secondsSinceLastReroute, String feedbackId, String newGeometry,
    boolean isSimulation, String originalRequestIdentifier, String requestIdentifier,
    String originalGeometry, int originalEstimatedDistance, int originalEstimatedDuration,
    String audioType, String upcomingInstruction, String upcomingType, String upcomingModifier,
    String upcomingName, String previousInstruction, String previousType, String previousModifier,
    String previousName, int distance, int duration, int stepDistanceRemaining, int stepDurationRemaining,
    int stepCount, int originalStepCount) {
    Hashtable<String, Object> event = getMetadata(sdKIdentifier, sdkVersion, sessionIdentifier,
      lat, lng, geometry, profile, estimatedDistance, estimatedDuration, rerouteCount,
      isSimulation, originalRequestIdentifier, requestIdentifier, originalGeometry,
      originalEstimatedDistance, originalEstimatedDuration, audioType, stepCount, originalStepCount);
    event.put(KEY_STEP, getStepMetadata(upcomingInstruction, upcomingType, upcomingModifier, upcomingName,
      previousInstruction, previousType, previousModifier, previousName, distance, duration,
      stepDistanceRemaining, stepDurationRemaining));
    event.put(KEY_EVENT, TYPE_REROUTE);
    event.put(KEY_FEEDBACK_ID, feedbackId);
    event.put(KEY_START_TIMESTAMP, TelemetryUtils.generateCreateDateFormatted(startTimestamp));
    if (locationsBefore != null) {
      event.put(KEY_LOCATIONS_BEFORE, locationsBefore);
    } else {
      event.put(KEY_LOCATIONS_BEFORE, JSONObject.NULL);
    }
    if (locationsAfter != null) {
      event.put(KEY_LOCATIONS_AFTER, locationsAfter);
    } else {
      event.put(KEY_LOCATIONS_AFTER, JSONObject.NULL);
    }
    event.put(KEY_DISTANCE_COMPLETED, distanceCompleted);
    event.put(KEY_DISTANCE_REMAINING, distanceRemaining);
    event.put(KEY_DURATION_REMAINING, durationRemaining);
    event.put(KEY_NEW_DISTANCE_REMAINING, newDistanceRemaining);
    event.put(KEY_NEW_DURATION_REMAINING, newDurationRemaining);
    event.put(KEY_NEW_GEOMETRY, newGeometry);
    event.put(KEY_SECONDS_SINCE_LAST_REROUTE, secondsSinceLastReroute);
    return event;
  }

  /**
   * User arrived.
   */
  public static Hashtable<String, Object> buildArriveEvent(
    String sdKIdentifier, String sdkVersion, String sessionIdentifier, double lat, double lng,
    String geometry, String profile, int estimatedDistance, int estimatedDuration,
    int rerouteCount, Date startTimestamp, int distanceCompleted, int distanceRemaining,
    int durationRemaining, boolean isSimulation, String originalRequestIdentifier,
    String requestIdentifier, String originalGeometry, int originalEstimatedDistance,
    int originalEstimatedDuration, String audioType, int stepCount, int originalStepCount) {
    Hashtable<String, Object> event = getMetadata(sdKIdentifier, sdkVersion, sessionIdentifier,
      lat, lng, geometry, profile, estimatedDistance, estimatedDuration, rerouteCount,
      isSimulation, originalRequestIdentifier, requestIdentifier, originalGeometry,
      originalEstimatedDistance, originalEstimatedDuration, audioType, stepCount, originalStepCount);
    event.put(KEY_EVENT, TYPE_ARRIVE);
    event.put(KEY_START_TIMESTAMP, TelemetryUtils.generateCreateDateFormatted(startTimestamp));
    event.put(KEY_DISTANCE_COMPLETED, distanceCompleted);
    event.put(KEY_DISTANCE_REMAINING, distanceRemaining);
    event.put(KEY_DURATION_REMAINING, durationRemaining);
    return event;
  }

  /**
   * User canceled navigation.
   */
  public static Hashtable<String, Object> buildCancelEvent(
    String sdKIdentifier, String sdkVersion, String sessionIdentifier, double lat, double lng,
    String geometry, String profile, int estimatedDistance, int estimatedDuration,
    int rerouteCount, Date startTimestamp, int distanceCompleted, int distanceRemaining,
    int durationRemaining, boolean isSimulation, String originalRequestIdentifier,
    String requestIdentifier, String originalGeometry, int originalEstimatedDistance,
    int originalEstimatedDuration, String audioType, Date arrivalTimestamp, int stepCount,
    int originalStepCount) {
    Hashtable<String, Object> event = getMetadata(sdKIdentifier, sdkVersion, sessionIdentifier,
      lat, lng, geometry, profile, estimatedDistance, estimatedDuration, rerouteCount,
      isSimulation, originalRequestIdentifier, requestIdentifier, originalGeometry,
      originalEstimatedDistance, originalEstimatedDuration, audioType, stepCount, originalStepCount);
    event.put(KEY_EVENT, TYPE_CANCEL);
    event.put(KEY_START_TIMESTAMP, TelemetryUtils.generateCreateDateFormatted(startTimestamp));
    event.put(KEY_DISTANCE_COMPLETED, distanceCompleted);
    event.put(KEY_DISTANCE_REMAINING, distanceRemaining);
    event.put(KEY_DURATION_REMAINING, durationRemaining);
    // arrivalTimestamp may be null
    addArrivalTimestamp(event, arrivalTimestamp);
    return event;
  }

  /**
   * The following metadata should be attached to all non-turnstile events. The Navigation SDK is
   * in charge of keeping track of a navigation sessionIdentifier, and it should use
   * {@link TelemetryUtils#buildUUID()} to generate the random UUID.
   */
  private static Hashtable<String, Object> getMetadata(
    String sdKIdentifier, String sdkVersion, String sessionIdentifier, double lat, double lng,
    String geometry, String profile, int estimatedDistance, int estimatedDuration,
    int rerouteCount, boolean isSimulation, String originalRequestIdentifier, String requestIdentifier,
    String originalGeometry, int originalEstimatedDistance, int originalEstimatedDuration,
    String audioType, int stepCount, int originalStepCount) {
    Hashtable<String, Object> event = new Hashtable<>();
    event.put(KEY_OPERATING_SYSTEM, TelemetryConstants.OPERATING_SYSTEM);
    event.put(KEY_SDK_IDENTIFIER, sdKIdentifier);
    event.put(KEY_SDK_VERSION, sdkVersion);
    event.put(KEY_EVENT_VERSION, EVENT_VERSION);
    event.put(KEY_SESSION_IDENTIFIER, sessionIdentifier);
    event.put(KEY_LAT, lat);
    event.put(KEY_LNG, lng);
    event.put(KEY_GEOMETRY, geometry);
    event.put(KEY_CREATED, TelemetryUtils.generateCreateDate(null));
    event.put(KEY_PROFILE, profile);
    event.put(KEY_ESTIMATED_DISTANCE, estimatedDistance);
    event.put(KEY_ESTIMATED_DURATION, estimatedDuration);
    event.put(KEY_STEP_COUNT, stepCount);
    event.put(KEY_ORIGINAL_STEP_COUNT, originalStepCount);
    event.put(KEY_REROUTE_COUNT, rerouteCount);
    event.put(KEY_SIMULATION, isSimulation);
    // originalRequestIdentifier may be "null"
    addPairIntoEventIfNeeded(event, KEY_ORIGINAL_REQUEST_IDENTIFIER, originalRequestIdentifier);
    // requestIdentifier may be "null"
    addPairIntoEventIfNeeded(event, KEY_REQUEST_IDENTIFIER, requestIdentifier);
    event.put(KEY_ORIGINAL_GEOMETRY, originalGeometry);
    event.put(KEY_ORIGINAL_ESTIMATED_DISTANCE, originalEstimatedDistance);
    event.put(KEY_ORIGINAL_ESTIMATED_DURATION, originalEstimatedDuration);
    // audioType may be "null"
    addPairIntoEventIfNeeded(event, KEY_AUDIO_TYPE, audioType);
    return event;
  }

  private static void addArrivalTimestamp(Hashtable<String, Object> event, Date arrivalTimestamp) {
    if (arrivalTimestamp == null) {
      event.put(KEY_ARRIVAL_TIMESTAMP, JSONObject.NULL);
    } else {
      event.put(KEY_ARRIVAL_TIMESTAMP, TelemetryUtils.generateCreateDateFormatted(arrivalTimestamp));
    }
  }

  private static void addPairIntoEventIfNeeded(Hashtable<String, Object> event, String key, String value) {
    // See NavigationMetricsWrapper.java in https://github.com/mapbox/mapbox-navigation-android
    if (value == null || value.equalsIgnoreCase("null")) {
      event.put(key, JSONObject.NULL);
    }
  }

  private static Hashtable<String, Object> getStepMetadata(
    String upcomingInstruction, String upcomingType, String upcomingModifier, String upcomingName,
    String previousInstruction, String previousType, String previousModifier, String previousName,
    int distance, int duration, int distanceRemaining, int durationRemaining
  ) {
    Hashtable<String, Object> event = new Hashtable<>();
    if (upcomingInstruction != null) {
      event.put(KEY_UPCOMING_INSTRUCTION, upcomingInstruction);
    } else {
      event.put(KEY_UPCOMING_INSTRUCTION, JSONObject.NULL);
    }
    if (upcomingType != null) {
      event.put(KEY_UPCOMING_TYPE, upcomingType);
    } else {
      event.put(KEY_UPCOMING_TYPE, JSONObject.NULL);
    }
    if (upcomingModifier != null) {
      event.put(KEY_UPCOMING_MODIFIER, upcomingModifier);
    } else {
      event.put(KEY_UPCOMING_MODIFIER, JSONObject.NULL);
    }
    if (upcomingName != null) {
      event.put(KEY_UPCOMING_NAME, upcomingName);
    } else {
      event.put(KEY_UPCOMING_NAME, JSONObject.NULL);
    }
    if (previousInstruction != null) {
      event.put(KEY_PREVIOUS_INSTRUCTION, previousInstruction);
    } else {
      event.put(KEY_PREVIOUS_INSTRUCTION, JSONObject.NULL);
    }
    if (previousType != null) {
      event.put(KEY_PREVIOUS_TYPE, previousType);
    } else {
      event.put(KEY_PREVIOUS_TYPE, JSONObject.NULL);
    }
    if (previousModifier != null) {
      event.put(KEY_PREVIOUS_MODIFIER, previousModifier);
    } else {
      event.put(KEY_PREVIOUS_MODIFIER, JSONObject.NULL);
    }
    if (previousName != null) {
      event.put(KEY_PREVIOUS_NAME, previousName);
    } else {
      event.put(KEY_PREVIOUS_NAME, JSONObject.NULL);
    }

    event.put(KEY_DISTANCE, distance);
    event.put(KEY_DURATION, duration);
    event.put(KEY_DISTANCE_REMAINING, distanceRemaining);
    event.put(KEY_DURATION_REMAINING, durationRemaining);
    return event;
  }
}