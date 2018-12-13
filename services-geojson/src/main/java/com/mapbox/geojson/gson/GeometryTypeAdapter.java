package com.mapbox.geojson.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import com.mapbox.geojson.CoordinateContainer;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.shifter.CoordinateShifterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts {@code Geometry} instances to JSON and JSON to
 * instances of {@code Geometry}.
 *
 * @since 3.0.0
 */
public class GeometryTypeAdapter extends TypeAdapter<Geometry> {


  @Override
  public void write(JsonWriter out, Geometry value) throws IOException {
    out.beginObject();
    out.name("type").value(value.type());
    if (value.bbox() != null) {
      out.name("bbox").jsonValue(value.bbox().toJson());
    }
    if (value instanceof CoordinateContainer) {

      String coordinates = ((CoordinateContainer) value).coordinates().toString();
      out.name("coordinates").jsonValue(coordinates);
    }
    out.endObject();
  }

  @Override
  public Geometry read(JsonReader in) throws IOException {
    return  null;
  }
}
