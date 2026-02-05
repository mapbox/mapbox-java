package com.mapbox.geojson;

import androidx.annotation.Keep;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.exception.GeoJsonException;

import java.io.IOException;

/**
 * Type Adapter to serialize/deserialize {@link FlattenListOfPoints} into/from two dimensional
 * JSON array.
 */
@Keep
class FlattenListOfPointsTypeAdapter extends BaseCoordinatesTypeAdapter<FlattenListOfPoints> {

  private static final int INITIAL_CAPACITY = 100;

  @Override
  public void write(JsonWriter out, FlattenListOfPoints flattenListOfPoints) throws IOException {
    if (flattenListOfPoints == null) {
      out.nullValue();
      return;
    }

    out.beginArray();
    int size = flattenListOfPoints.size();
    if (size == 0) {
      out.endArray();
      return;
    }

    double[] flattenLngLatCoordinates = flattenListOfPoints.getFlattenLngLatArray();
    double[] altitudes = flattenListOfPoints.getAltitudes();

    for (int i = 0; i < size; i++) {
      double[] value;
      if (altitudes != null && !Double.isNaN(altitudes[i])) {
        value = new double[]{
                flattenLngLatCoordinates[i * 2],
                flattenLngLatCoordinates[(i * 2) + 1],
                altitudes[i]
        };
      } else {
        value = new double[]{
                flattenLngLatCoordinates[i * 2],
                flattenLngLatCoordinates[(i * 2) + 1]
        };
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
      in.beginArray();
      double[] flattenLngLats = new double[INITIAL_CAPACITY * 2];
      double[] altitudes = null;
      int currentIdx = 0;

      while (in.peek() == JsonToken.BEGIN_ARRAY) {
        in.beginArray();
        // Read longitude
        if (in.hasNext()) {
          flattenLngLats[currentIdx * 2] = in.nextDouble();
        } else {
          throw new IndexOutOfBoundsException(
                  "Point coordinates should contain at least two values"
          );
        }

        // Read latitude
        if (in.hasNext()) {
          flattenLngLats[currentIdx * 2 + 1] = in.nextDouble();
        } else {
          throw new IndexOutOfBoundsException(
                  "Point coordinates should contain at least two values"
          );
        }

        // Finally altitude if present
        if (in.hasNext()) {
          if (altitudes == null) {
            altitudes = new double[flattenLngLats.length / 2];
            // Fill in any previous altitude as NaN
            for (int j = 0; j < currentIdx; j++) {
              altitudes[j] = Double.NaN;
            }
          }
          altitudes[currentIdx] = in.nextDouble();
          // Consume any extra value but don't store it
          while (in.hasNext()) {
            in.skipValue();
          }
          in.endArray();
        } else {
          in.endArray();
          if (altitudes != null) {
            // If we are storing altitudes but this point doesn't have it then set it to NaN
            altitudes[currentIdx] = Double.NaN;
          }
        }
        currentIdx++;
        // If we run out of space we grow the the arrays
        if (currentIdx * 2 >= flattenLngLats.length) {
          double[] newFlattenLngLats = new double[flattenLngLats.length * 2];
          System.arraycopy(flattenLngLats, 0, newFlattenLngLats, 0, flattenLngLats.length);
          flattenLngLats = newFlattenLngLats;
          if (altitudes != null) {
            double[] newAltitudes = new double[altitudes.length * 2];
            System.arraycopy(altitudes, 0, newAltitudes, 0, altitudes.length);
            altitudes = newAltitudes;
          }
        }
      }
      in.endArray();

      int totalPoints = currentIdx;
      double[] trimmedFlattenLngLats = new double[totalPoints * 2];
      System.arraycopy(flattenLngLats, 0, trimmedFlattenLngLats, 0, totalPoints * 2);
      double[] trimmedAltitudes = null;
      if (altitudes != null) {
        trimmedAltitudes = new double[totalPoints];
        System.arraycopy(altitudes, 0, trimmedAltitudes, 0, totalPoints);
      }

      return new FlattenListOfPoints(trimmedFlattenLngLats, trimmedAltitudes);
    }

    throw new GeoJsonException("coordinates should be non-null array of array of double");
  }
}
