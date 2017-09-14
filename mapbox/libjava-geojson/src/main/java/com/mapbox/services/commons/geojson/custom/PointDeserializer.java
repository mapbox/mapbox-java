package com.mapbox.services.commons.geojson.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

import java.lang.reflect.Type;

/**
 * Required to handle the "Expected BEGIN_OBJECT but was BEGIN_ARRAY" error that Gson would show
 * otherwise.
 *
 * @since 1.0.0
 */
public class PointDeserializer implements JsonDeserializer<Point> {

  /**
   * Required to handle the "Expected BEGIN_OBJECT but was BEGIN_ARRAY" error that Gson would show
   * otherwise.
   *
   * @param json    A class representing an element of Json.
   * @param typeOfT Common superinterface for all types in the Java.
   * @param context Context for deserialization that is passed to a custom deserializer during
   *                invocation of its {@link JsonDeserializer#deserialize(JsonElement, Type,
   *                JsonDeserializationContext)} method.
   * @return Either {@link Position} with an altitude or one that doesn't.
   * @throws JsonParseException This exception is raised if there is a serious issue that occurs
   *                            during parsing of a Json string.
   * @since 1.0.0
   */
  @Override
  public Point deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
    throws JsonParseException {
    JsonArray rawCoordinates;

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
