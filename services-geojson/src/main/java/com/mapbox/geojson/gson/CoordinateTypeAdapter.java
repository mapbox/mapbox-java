package com.mapbox.geojson.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.shifter.CoordinateShifterManager;

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

    // Unshift coordinates
    List<Double> unshiftedCoordinates =
            CoordinateShifterManager.getCoordinateShifter().unshiftPoint(value);

    BigDecimal lon = BigDecimal.valueOf(unshiftedCoordinates.get(0));
    String lonString = lon.setScale(7, RoundingMode.HALF_UP)
            .stripTrailingZeros().toPlainString();

    BigDecimal lat = BigDecimal.valueOf(unshiftedCoordinates.get(1));
    String latString = lat.setScale(7, RoundingMode.HALF_UP)
            .stripTrailingZeros().toPlainString();

    out.value(Double.valueOf(lonString));
    out.value(Double.valueOf(latString));

    // Includes altitude
    if (value.size() > 2) {
      out.value(unshiftedCoordinates.get(2));
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

    if (coordinates.size() > 2) {
      return CoordinateShifterManager.getCoordinateShifter()
              .shiftLonLatAlt(coordinates.get(0), coordinates.get(1), coordinates.get(2));
    }
    return CoordinateShifterManager.getCoordinateShifter()
            .shiftLonLat(coordinates.get(0), coordinates.get(1));
  }
}
