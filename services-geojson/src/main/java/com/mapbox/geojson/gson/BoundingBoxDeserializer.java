package com.mapbox.geojson.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mapbox.geojson.BoundingBox;

import java.lang.reflect.Type;

/**
 * When deserializing a GeoJSONs optional bounding box, we want to convert the JsonArray to a
 * {@link BoundingBox} object for easier consumption in developers java applications.
 *
 * @since 3.0.0
 */
public class BoundingBoxDeserializer implements JsonDeserializer<BoundingBox> {

  /**
   * When deserializing a GeoJSONs optional bounding box, we want to convert the JsonArray to a
   * {@link BoundingBox} object for easier consumption in developers java applications.
   *
   * @param json    a class representing an element of JSON
   * @param typeOfT common superinterface for all types in the Java
   * @param context context for deserialization that is passed to a custom deserializer during
   *                invocation of its {@link JsonDeserializer#deserialize(JsonElement, Type,
   *                JsonDeserializationContext)} method
   * @return a new {@link BoundingBox} object representing the values originally in the JSON
   * @throws JsonParseException this exception is raised if there is a serious issue that occurs
   *                            during parsing of a Json string
   * @since 3.0.0
   */
  @Override
  public BoundingBox deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
    throws JsonParseException {

    JsonArray rawCoordinates = json.getAsJsonArray();

    // TODO one point might have altitude while the other doesn't
    // Has altitude
    if (rawCoordinates.size() > 4) {
      return BoundingBox.fromCoordinates(
        rawCoordinates.get(0).getAsDouble(),
        rawCoordinates.get(1).getAsDouble(),
        rawCoordinates.get(2).getAsDouble(),
        rawCoordinates.get(3).getAsDouble(),
        rawCoordinates.get(4).getAsDouble(),
        rawCoordinates.get(5).getAsDouble());
    }
    return BoundingBox.fromCoordinates(
      rawCoordinates.get(0).getAsDouble(),
      rawCoordinates.get(1).getAsDouble(),
      rawCoordinates.get(2).getAsDouble(),
      rawCoordinates.get(3).getAsDouble());
  }
}
