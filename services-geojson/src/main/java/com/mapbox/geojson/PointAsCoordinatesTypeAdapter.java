package com.mapbox.geojson;

import androidx.annotation.Keep;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * TypeAdapter to serialize Point as coordinates, i.e array of doubles and
 * to deserialize into Point out of array of doubles.
 *
 * @since 4.6.0
 */
@Keep
public class PointAsCoordinatesTypeAdapter extends BaseCoordinatesTypeAdapter<Point> {

  @Override
  public void write(JsonWriter out, Point value) throws IOException {
    writePoint(out, value);
  }

  @Override
  public Point read(JsonReader in) throws IOException {
    return readPoint(in);
  }
}
