package com.mapbox.geojson.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.Geometry;

import java.io.IOException;

public class GeometryTypeAdapter extends TypeAdapter<Geometry> {

  @Override
  public void write(JsonWriter out, Geometry value) throws IOException {
    out.beginObject();
    out.name("type").value(value.type());
    if (value.bbox() != null) {
      out.name("bbox").jsonValue(value.bbox().toJson());
    }
    out.name("coordinates").jsonValue(value.coordinates().toString());
    out.endObject();
  }

  @Override
  public Geometry read(JsonReader in) throws IOException {
    return null;
  }
}
