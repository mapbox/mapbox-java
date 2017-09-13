package com.mapbox.services.api.geocoding.v5.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mapbox.services.commons.geojson.Geometry;

import java.lang.reflect.Type;

/**
 * A custom deserializer for Gson, used for the Geocoder.
 *
 * @since 1.0.0
 */
public class CarmenGeometryDeserializer implements JsonDeserializer<Geometry> {
  @Override
  public Geometry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {


      System.out.println(json.getAsJsonObject().get("center"));




    return null;
  }

//  /**
//   * A custom deserializer for Gson, used for the Geocoder.
//   *
//   * @param json    The Json data being deserialized.
//   * @param typeOfT The type of the Object to deserialize to.
//   * @param context Context for deserialization.
//   * @return Deserialized Geometry.
//   * @throws JsonParseException This exception is raised if there is a serious issue that occurs
//   *                            during parsing of a Json string.
//   * @since 1.0.0
//   */
//  @Override
//  public Geometry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
//    throws JsonParseException {
//    JsonObject jsonObject = (JsonObject) json;
//    String geometryType = jsonObject.get("type").getAsString();
//    if (geometryType.equals("Point")) {
//      JsonArray coordinates = jsonObject.getAsJsonArray("coordinates");
//      return Point.fromCoordinates(Position.fromCoordinates(
//        coordinates.get(0).getAsDouble(),
//        coordinates.get(1).getAsDouble()));
//    } else {
//      throw new JsonParseException("Unexpected geometry found: " + geometryType);
//    }
//  }
}
