package com.mapbox.services.api.distance.v1.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.geojson.MultiPoint;

import java.lang.reflect.Type;

/**
 * A custom deserializer that assumes a {@link MultiPoint} string.
 *
 * @since 2.0.0
 */
public class DistanceGeometryDeserializer implements JsonDeserializer<Geometry> {

  /**
   * A custom deserializer that assumes a {@link MultiPoint} string.
   *
   * @param json    The Json data being deserialized.
   * @param typeOfT The type of the Object to deserialize to.
   * @param context Context for deserialization.
   * @return Deserialized Geometry.
   * @throws JsonParseException This exception is raised if there is a serious issue that occurs
   *                            during parsing of a Json string.
   * @since 2.0.0
   */
  @Override
  public Geometry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
    throws JsonParseException {
    return MultiPoint.fromJson(json.getAsString());
  }
}
