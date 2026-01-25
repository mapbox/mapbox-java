package com.mapbox.geojson;

import androidx.annotation.Keep;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.exception.GeoJsonException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Type Adapter to serialize/deserialize List&lt;Point&gt; into/from two dimentional double array.
 *
 * @since 4.6.0
 */
@Keep
class FlattenListOfPointsTypeAdapter extends BaseCoordinatesTypeAdapter<FlattenListOfPoints> {

  @Override
  public void write(JsonWriter out, FlattenListOfPoints flattenListOfPoints) throws IOException {

    if (flattenListOfPoints == null) {
      out.nullValue();
      return;
    }

    out.beginArray();
    double[][] coordinatesPrimitives = flattenListOfPoints.coordinatesPrimitives();
    double[] flattenLatLngCoordinates = coordinatesPrimitives[0];
    double[] altitudes = coordinatesPrimitives[1];

    for (int i = 0; i < flattenLatLngCoordinates.length / 2; i++) {
      double[] value;
      if (altitudes != null && !Double.isNaN(altitudes[i])) {
        value = new double[]{flattenLatLngCoordinates[i * 2], flattenLatLngCoordinates[(i * 2) + 1], altitudes[i]};
      } else {
        value = new double[]{flattenLatLngCoordinates[i * 2], flattenLatLngCoordinates[(i * 2) + 1]};
      }

      writePointList(out, value);
    }

    out.endArray();
  }

  @Override
  public FlattenListOfPoints read(JsonReader in) throws IOException {

    if (in.peek() == JsonToken.NULL) {
      throw new NullPointerException();
    }

    if (in.peek() == JsonToken.BEGIN_ARRAY) {
      List<Point> points = new ArrayList<>();
      in.beginArray();

      while (in.peek() == JsonToken.BEGIN_ARRAY) {
        points.add(readPoint(in));
      }
      in.endArray();

      return new FlattenListOfPoints(points);
    }

    throw new GeoJsonException("coordinates should be non-null array of array of double");
  }
}
