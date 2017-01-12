package com.mapbox.services.android.telemetry.location;

/**
 * Same priorities GMS and Lost support:
 *
 * https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest
 * https://github.com/mapzen/lost/blob/master/lost/src/main/java/com/mapzen/android/lost/api/LocationRequest.java
 */
public class LocationEnginePriority {

  public static final int NO_POWER = 0;
  public static final int LOW_POWER = 1;
  public static final int BALANCED_POWER_ACCURACY = 2;
  public static final int HIGH_ACCURACY = 3;

}
