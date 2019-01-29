package com.mapbox.geojson.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mapbox.geojson.Geometry;

import java.lang.reflect.Type;

/**
 * Required to handle the "Unable to invoke no-args constructor for interface {@link Geometry} error
 * that Gson shows when trying to deserialize a list of {@link Geometry}.
 *
 * @since 1.0.0
 * @deprecated  this class is deprecated, {@link com.mapbox.geojson.GeometryAdapterFactory}
 *   should be used to serialize/deserialize Geometries.
 */
@Deprecated
public class GeometryDeserializer implements JsonDeserializer<Geometry> {

  /**
   * Empty constructor to prevent relying on the default one.
   *
   * @since 3.0.0
   */
  public GeometryDeserializer() {
    // Empty Constructor
  }

  /**
   * Required to handle the "Unable to invoke no-args constructor for interface {@link Geometry}
   * error that Gson shows when trying to deserialize a list of {@link Geometry}.
   *
   * @param json    A class representing an element of Json.
   * @param typeOfT Common superinterface for all types in the Java.
   * @param context Context for deserialization that is passed to a custom deserializer during
   *                invocation of its {@link JsonDeserializer#deserialize(JsonElement, Type,
   *                JsonDeserializationContext)} method.
   * @return either default deserialization on the specified object or JsonParseException.
   * @throws JsonParseException This exception is raised if there is a serious issue that occurs
   *                            during parsing of a Json string.
   * @since 1.0.0
   */
  @Override
  public Geometry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
    String geometryType;
    // Find the actual class name from the type property in the JSON.
    if (json.isJsonObject()) {
      geometryType = json.getAsJsonObject().get("type").getAsString();
    } else {
      geometryType = json.getAsJsonArray().get(0).getAsJsonObject().get("type").getAsString();
    }

    try {
      // Use the current context to deserialize it
      Type classType = Class.forName("com.mapbox.geojson." + geometryType);
      return context.deserialize(json, classType);
    } catch (ClassNotFoundException classNotFoundException) {
      // Unknown geometry
      throw new JsonParseException(classNotFoundException);
    }
  }
}
