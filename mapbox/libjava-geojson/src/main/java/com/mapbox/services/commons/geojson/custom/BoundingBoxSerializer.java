package com.mapbox.services.commons.geojson.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class BoundingBoxSerializer implements JsonSerializer<BoundingBox> {
  @Override
  public JsonElement serialize(BoundingBox src, Type typeOfSrc, JsonSerializationContext context) {
    JsonArray bbox = new JsonArray();
    bbox.add(src.southwest().longitude());
    bbox.add(src.southwest().latitude());
    if (src.southwest().hasAltitude()) {
      bbox.add(src.southwest().altitude());
    }
    bbox.add(src.northeast().longitude());
    bbox.add(src.northeast().latitude());
    if (src.northeast().hasAltitude()) {
      bbox.add(src.northeast().altitude());
    }
    return bbox;
  }
}
