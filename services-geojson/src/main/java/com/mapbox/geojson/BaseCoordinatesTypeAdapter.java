package com.mapbox.geojson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.exception.GeoJsonException;
import com.mapbox.geojson.shifter.CoordinateShifterManager;
import com.mapbox.geojson.utils.GeoJsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  Base class for converting {@code T} instance of coordinates to JSON and
 *  JSON to instance of {@code T}.
 *
 * @param <T> Type of coordinates
 * @since 4.6.0
 */
abstract class BaseCoordinatesTypeAdapter<T> extends TypeAdapter<T> {


  protected void writePoint(JsonWriter out, Point value) throws  IOException {
    writePointList(out, value.coordinates());
  }

  protected Point readPoint(JsonReader in) throws IOException {

    List<Double> coordinates = readPointList(in);
    if (coordinates != null && coordinates.size() > 1) {
      return new Point("Point",null, coordinates);
    }

    throw new GeoJsonException(" Point coordinates should be non-null double array");
  }


  protected void writePointList(JsonWriter out, List<Double> value) throws IOException {

    if (value == null) {
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

  protected List<Double> readPointList(JsonReader in) throws IOException {

    if (in.peek() == JsonToken.NULL) {
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
