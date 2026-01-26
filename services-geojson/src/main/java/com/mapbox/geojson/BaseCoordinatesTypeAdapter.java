package com.mapbox.geojson;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.shifter.CoordinateShifterManager;
import com.mapbox.geojson.utils.GeoJsonUtils;

import java.io.IOException;

/**
 * Base class for converting {@code T} instance of coordinates to JSON and
 * JSON to instance of {@code T}.
 *
 * @param <T> Type of coordinates
 * @since 4.6.0
 */
@Keep
abstract class BaseCoordinatesTypeAdapter<T> extends TypeAdapter<T> {


  protected void writePoint(JsonWriter out, Point point) throws IOException {
    if (point == null) {
      return;
    }
    writePointList(out, point.flattenCoordinates());
  }

  protected Point readPoint(JsonReader in) throws IOException {
    return new Point("Point", null, readPointList(in));
  }


  protected void writePointList(JsonWriter out, double[] value) throws IOException {

    if (value == null) {
      return;
    }

    out.beginArray();

    // Unshift coordinates
    double[] unshiftedCoordinates = CoordinateShifterManager.getCoordinateShifter()
            .unshiftPointArray(value);

    out.value(GeoJsonUtils.trim(unshiftedCoordinates[0]));
    out.value(GeoJsonUtils.trim(unshiftedCoordinates[1]));

    // Includes altitude
    if (value.length > 2) {
      out.value(unshiftedCoordinates[2]);
    }
    out.endArray();
  }

  @NonNull
  protected double[] readPointList(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      throw new NullPointerException();
    }

    double lon;
    double lat;
    double altitude;
    in.beginArray();
    if (in.hasNext()) {
      lon = in.nextDouble();
    } else {
      throw new IndexOutOfBoundsException("Point coordinates should contain at least two values");
    }
    if (in.hasNext()) {
      lat = in.nextDouble();
    } else {
      throw new IndexOutOfBoundsException("Point coordinates should contain at least two values");
    }
    if (in.hasNext()) {
      altitude = in.nextDouble();
      // Consume any extra value but don't store it
      while (in.hasNext()) {
        in.skipValue();
      }
      in.endArray();
      return CoordinateShifterManager.getCoordinateShifter().shift(lon, lat, altitude);
    } else {
      in.endArray();
      return CoordinateShifterManager.getCoordinateShifter().shift(lon, lat);
    }

  }

}
