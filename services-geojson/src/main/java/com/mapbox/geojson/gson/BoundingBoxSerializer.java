package com.mapbox.geojson.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mapbox.geojson.BoundingBox;

import java.lang.reflect.Type;

/**
 * Serializer used for converting the {@link BoundingBox} object inside a GeoJson object to a JSON
 * element which can be read by GSON and added to the final JSON output.
 *
 * @since 3.0.0
 */
public class BoundingBoxSerializer implements JsonSerializer<BoundingBox> {
  @Override
  public JsonElement serialize(BoundingBox src, Type typeOfSrc, JsonSerializationContext context) {

    JsonArray bbox = new JsonArray();
    bbox.add(new JsonPrimitive(src.southwest().longitude()));
    bbox.add(new JsonPrimitive(src.southwest().latitude()));
    bbox.add(new JsonPrimitive(src.northeast().longitude()));
    bbox.add(new JsonPrimitive(src.northeast().latitude()));
    return bbox;
  }
}
