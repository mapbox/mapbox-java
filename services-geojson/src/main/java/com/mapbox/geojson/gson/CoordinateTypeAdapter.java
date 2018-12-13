package com.mapbox.geojson.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.shifter.CoordinateShifterManager;
import com.mapbox.geojson.utils.GeoJsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to read and write coordinates for Point class.
 *
 * @since 4.1.0
 */
public class CoordinateTypeAdapter extends TypeAdapter<List<Double>> {
  @Override
  public void write(JsonWriter out, List<Double> value) throws IOException {

    if (value == null) {
      out.nullValue();
      return;
    }

    out.beginArray();

    // Unshift coordinates
    List<Double> unshiftedCoordinates =
            CoordinateShifterManager.getCoordinateShifter().unshiftPoint(value);

    out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(0)));
    out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(1)));

    // Includes altitude
    if (value.size() > 2) {
      out.value(unshiftedCoordinates.get(2));
    }
    out.endArray();
  }

  @Override
  public List<Double> read(JsonReader in) throws IOException {

    if (in.peek() == JsonToken.NULL) {
      //in.nextNull();
      //return null;
      throw new NullPointerException();
    }

    List<Double> coordinates = new ArrayList<Double>();
    in.beginArray();
    while (in.hasNext()) {
      coordinates.add(in.nextDouble());
    }
    in.endArray();

    if (coordinates.size() > 2) {
      return CoordinateShifterManager.getCoordinateShifter()
              .shiftLonLatAlt(coordinates.get(0), coordinates.get(1), coordinates.get(2));
    }
    return CoordinateShifterManager.getCoordinateShifter()
            .shiftLonLat(coordinates.get(0), coordinates.get(1));
  }
}
