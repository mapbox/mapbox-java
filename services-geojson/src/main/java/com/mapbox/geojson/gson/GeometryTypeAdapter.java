package com.mapbox.geojson.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.CoordinateContainer;
import com.mapbox.geojson.Geometry;

import java.io.IOException;

/**
 * Converts {@code Geometry} instances to JSON and JSON to
 * instances of {@code Geometry}.
 *
 * @since 3.0.0
 * @deprecated  this class is deprecated, {@link com.mapbox.geojson.GeometryAdapterFactory}
 *   should be used to serialize/deserialize Geometries.
 */
@Deprecated
public class GeometryTypeAdapter extends TypeAdapter<Geometry> {


  @Override
  public void write(JsonWriter out, Geometry value) throws IOException {
    out.beginObject();
    out.name("type").value(value.type());
    if (value.bbox() != null) {
      out.name("bbox").jsonValue(value.bbox().toJson());
    }
    if (value instanceof CoordinateContainer) {
      out.name("coordinates").jsonValue(((CoordinateContainer) value).coordinates().toString());
    }
    out.endObject();
  }

  @Override
  public Geometry read(JsonReader in) throws IOException {
    return null;
  }
}
