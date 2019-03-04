package com.mapbox.geojson.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.exception.GeoJsonException;

import java.lang.reflect.Type;

/**
 * When deserializing a GeoJSONs optional bounding box, we want to convert the JsonArray to a
 * {@link BoundingBox} object for easier consumption in developers java applications.
 *
 * @since 3.0.0
 * @deprecated this class is deprecated, {@link com.mapbox.geojson.gson.BoundingBoxTypeAdapter}
 *   should be used to serialize/deserialize coordinates as Points.
 */
@Deprecated
public class BoundingBoxDeserializer implements JsonDeserializer<BoundingBox> {

  /**
   * Empty constructor to prevent relying on the default one.
   *
   * @since 3.0.0
   */
  public BoundingBoxDeserializer() {
    // Empty Constructor
  }

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
  public BoundingBox deserialize(JsonElement json, Type typeOfT,
                                 JsonDeserializationContext context) {

    JsonArray rawCoordinates = json.getAsJsonArray();

    if (rawCoordinates.size() == 6) {
      return BoundingBox.fromLngLats(
        rawCoordinates.get(0).getAsDouble(),
        rawCoordinates.get(1).getAsDouble(),
        rawCoordinates.get(2).getAsDouble(),
        rawCoordinates.get(3).getAsDouble(),
        rawCoordinates.get(4).getAsDouble(),
        rawCoordinates.get(5).getAsDouble());
    }
    if (rawCoordinates.size() == 4) {
      return BoundingBox.fromLngLats(
        rawCoordinates.get(0).getAsDouble(),
        rawCoordinates.get(1).getAsDouble(),
        rawCoordinates.get(2).getAsDouble(),
        rawCoordinates.get(3).getAsDouble());
    } else {
      throw new GeoJsonException("The value of the bbox member MUST be an array of length 2*n where"
        + " n is the number of dimensions represented in the contained geometries, with all axes of"
        + " the most southwesterly point followed by all axes of the more northeasterly point. The "
        + "axes order of a bbox follows the axes order of geometries.");
    }
  }
}
