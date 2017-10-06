package com.mapbox.services.android.telemetry.navigation;

import android.location.Location;
import android.util.JsonToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class NavigationLocation {

  private static final String UTC_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  private static final String ACCURACY = "horizontalAccuracy";
  private static final String TIMESTAMP = "timestamp";
  private static final String ALTITUDE = "altitude";
  private static final String COURSE = "course";
  private static final String LONGITUDE = "lng";
  private static final String LATITUDE = "lat";
  private static final String SPEED = "speed";

  public JSONArray getSerializedJson(Location[] locations) {
    JSONArray jsonArray = new JSONArray();
    for (Location loc : locations) {
      jsonArray.put(serializeToJson(loc));
    }
    return jsonArray;
  }

  private JSONObject serializeToJson(Location location) {
    formatLocationTime(location);

    HashMap<String, Object> map = new HashMap<>();
    // Potentially null values
    map.put(ALTITUDE, location.hasAltitude() ? location.getAltitude() : JsonToken.NULL);
    map.put(ACCURACY, location.hasAccuracy() ? location.getAccuracy() : JsonToken.NULL);
    map.put(COURSE, location.hasBearing() ? location.getBearing() : JsonToken.NULL);
    map.put(SPEED, location.hasSpeed() ? location.getSpeed() : JsonToken.NULL);

    // Never null values
    map.put(LATITUDE, location.getLatitude());
    map.put(LONGITUDE, location.getLongitude());
    map.put(TIMESTAMP, formatLocationTime(location));

    return new JSONObject(map);
  }

  private static String formatLocationTime(Location location) {
    DateFormat formatter = new SimpleDateFormat(UTC_TIME_FORMAT, Locale.US);
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    return formatter.format(new Date(location.getTime()));
  }
}
