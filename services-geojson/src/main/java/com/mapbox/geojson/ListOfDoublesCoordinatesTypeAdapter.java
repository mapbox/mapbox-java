package com.mapbox.geojson;

import androidx.annotation.Keep;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Type Adapter to serialize/deserialize Point into/from for double array.
 *
 * @since 4.6.0
 */
@Keep
class ListOfDoublesCoordinatesTypeAdapter extends BaseCoordinatesTypeAdapter<double[]> {

  @Override
  public void write(JsonWriter out, double[] value) throws IOException {
    writePointList(out, value);
  }

  @Override
  public double[] read(JsonReader in) throws IOException {
    return readPointList(in);
  }
}
