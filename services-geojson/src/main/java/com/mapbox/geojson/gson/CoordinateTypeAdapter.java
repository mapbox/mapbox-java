package com.mapbox.geojson.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    out.beginArray();

    BigDecimal lon = BigDecimal.valueOf(value.get(0));
    String lonString = lon.setScale(7, RoundingMode.HALF_UP)
            .stripTrailingZeros().toPlainString();

    BigDecimal lat = BigDecimal.valueOf(value.get(1));
    String latString = lat.setScale(7, RoundingMode.HALF_UP)
            .stripTrailingZeros().toPlainString();

    out.value(Double.valueOf(lonString));
    out.value(Double.valueOf(latString));

    // Includes altitude
    if (value.size() > 2) {
      out.value(value.get(2));
    }
    out.endArray();
  }

  @Override
  public List<Double> read(JsonReader in) throws IOException {
    List<Double> coordinates = new ArrayList<Double>();
    in.beginArray();
    while (in.hasNext()) {
      coordinates.add(in.nextDouble());
    }
    in.endArray();

    return coordinates;
  }
}
