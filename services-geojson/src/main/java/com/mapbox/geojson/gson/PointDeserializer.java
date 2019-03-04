package com.mapbox.geojson.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.mapbox.geojson.Point;

import java.lang.reflect.Type;

/**
 * Deserialize the coordinates inside the json into {@link Point}s capturing the same information.
 * Otherwise when deserializing, you'll most likely see this error: Required to handle the
 * "Expected BEGIN_OBJECT but was BEGIN_ARRAY".
 *
 * @since 1.0.0
 * @deprecated  this class is deprecated, {@link com.mapbox.geojson.PointAsCoordinatesTypeAdapter}
 *   should be used to serialize/deserialize coordinates as Points.
 */
@Deprecated
public class PointDeserializer implements JsonDeserializer<Point> {

  /**
   * Empty constructor to prevent relying on the default one.
   *
   * @since 3.0.0
   */
  public PointDeserializer() {
    // Empty Constructor
  }

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
    JsonArray rawCoordinates = json.getAsJsonArray();

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
