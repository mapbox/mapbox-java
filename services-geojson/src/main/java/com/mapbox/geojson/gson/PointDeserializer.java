package com.mapbox.geojson.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Point;

import java.lang.reflect.Type;

/**
 * Deserialize the coordinates inside the json into {@link Point}s capturing the same information.
 * Otherwise when deserializing, you'll most likely see this error: Required to handle the
 * "Expected BEGIN_OBJECT but was BEGIN_ARRAY".
 *
 * @since 1.0.0
 */
public class PointDeserializer implements JsonDeserializer<Point> {

  /**
   * Deserialize the coordinates inside the json into {@link Point}s capturing the same information.
   * Otherwise when deserializing, you'll most likely see this error: Required to handle the
   * "Expected BEGIN_OBJECT but was BEGIN_ARRAY".
   *
   * @param json    a class representing an element of Json
   * @param typeOfT common superinterface for all types in the Java
   * @param context Context for deserialization that is passed to a custom deserializer during
   *                invocation of its {@link JsonDeserializer#deserialize(JsonElement, Type,
   *                JsonDeserializationContext)} method
   * @return either {@link Point} with an altitude or one that doesn't
   * @since 1.0.0
   */
  @Override
  public Point deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
    JsonArray rawCoordinates;

    System.out.println(json.toString());

    if (json instanceof JsonObject) {
      rawCoordinates = json.getAsJsonObject().getAsJsonArray("coordinates");
    } else {
      rawCoordinates = json.getAsJsonArray();
    }

    double longitude = rawCoordinates.get(0).getAsDouble();
    double latitude = rawCoordinates.get(1).getAsDouble();

    // Includes altitude
    if (rawCoordinates.size() > 2) {
      double altitude = rawCoordinates.get(2).getAsDouble();
      return Point.fromLngLat(longitude, latitude, altitude);
    }

    // It doesn't have altitude
    return Point.fromLngLat(longitude, latitude);
  }
}
