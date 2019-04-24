package com.mapbox.geojson;

import android.support.annotation.Keep;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.exception.GeoJsonException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Type Adapter to serialize/deserialize List&lt;List&lt;List&lt;Point&gt;&gt;&gt; into/from
 * four dimentional double array.
 *
 * @since 4.6.0
 */
@Keep
class ListofListofListOfPointCoordinatesTypeAdapter
        extends BaseCoordinatesTypeAdapter<List<List<List<Point>>>> {

  @Override
  public void write(JsonWriter out, List<List<List<Point>>> points) throws IOException {

    if (points == null) {
      out.nullValue();
      return;
    }

    out.beginArray();

    for (List<List<Point>> listOfListOfPoints : points) {

      out.beginArray();

      for (List<Point> listOfPoints : listOfListOfPoints) {

        out.beginArray();

        for (Point point : listOfPoints) {
          writePoint(out, point);
        }
        out.endArray();
      }

      out.endArray();
    }
    out.endArray();
  }

  @Override
  public List<List<List<Point>>> read(JsonReader in) throws IOException {

    if (in.peek() == JsonToken.NULL) {
      throw new NullPointerException();
    }

    if (in.peek() == JsonToken.BEGIN_ARRAY) {

      in.beginArray();
      List<List<List<Point>>> listOfListOflistOfPoints = new ArrayList<>();
      while (in.peek() == JsonToken.BEGIN_ARRAY) {

        in.beginArray();
        List<List<Point>> listOfListOfPoints = new ArrayList<>();

        while (in.peek() == JsonToken.BEGIN_ARRAY) {

          in.beginArray();
          List<Point> listOfPoints = new ArrayList<>();
          while (in.peek() == JsonToken.BEGIN_ARRAY) {
            listOfPoints.add(readPoint(in));
          }
          in.endArray();

          listOfListOfPoints.add(listOfPoints);
        }
        in.endArray();
        listOfListOflistOfPoints.add(listOfListOfPoints);
      }
      in.endArray();
      return listOfListOflistOfPoints;
    }

    throw new GeoJsonException("coordinates should be array of array of array of double");
  }
}
