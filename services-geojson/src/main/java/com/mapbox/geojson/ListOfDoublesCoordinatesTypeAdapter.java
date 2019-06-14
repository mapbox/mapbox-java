package com.mapbox.geojson;

import android.support.annotation.Keep;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.List;

/**
 * Type Adapter to serialize/deserialize Point into/from a double array.
 *
 * @since 4.6.0
 */
@Keep
class ListOfDoublesCoordinatesTypeAdapter extends BaseCoordinatesTypeAdapter<List<Double>> {

  @Override
  public void write(JsonWriter out, List<Double> value) throws IOException {
    writePointList(out, value);
  }

  @Override
  public List<Double> read(JsonReader in) throws IOException {
    return readPointList(in);
  }
}
