package com.mapbox.geojson.gson;

import androidx.annotation.Keep;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.exception.GeoJsonException;
import com.mapbox.geojson.shifter.CoordinateShifterManager;
import com.mapbox.geojson.utils.GeoJsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to read and write coordinates for BoundingBox class.
 *
 * @since 4.6.0
 */
@Keep
public class BoundingBoxTypeAdapter extends TypeAdapter<BoundingBox> {

  @Override
  public void write(JsonWriter out, BoundingBox value) throws IOException {

    if (value == null) {
      out.nullValue();
      return;
    }

    out.beginArray();

    // Southwest
    Point point = value.southwest();
    List<Double> unshiftedCoordinates =
            CoordinateShifterManager.getCoordinateShifter().unshiftPoint(point);

    out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(0)));
    out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(1)));
    if (point.hasAltitude()) {
      out.value(unshiftedCoordinates.get(2));
    }

    // Northeast
    point = value.northeast();
    unshiftedCoordinates =
            CoordinateShifterManager.getCoordinateShifter().unshiftPoint(point);
    out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(0)));
    out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(1)));
    if (point.hasAltitude()) {
      out.value(unshiftedCoordinates.get(2));
    }

    out.endArray();
  }

  @Override
  public BoundingBox read(JsonReader in) throws IOException {

    List<Double> rawCoordinates = new ArrayList<Double>(6);
    in.beginArray();
    while (in.hasNext()) {
      rawCoordinates.add(in.nextDouble());
    }
    in.endArray();

    if (rawCoordinates.size() == 6) {
      return BoundingBox.fromLngLats(
              rawCoordinates.get(0),
              rawCoordinates.get(1),
              rawCoordinates.get(2),
              rawCoordinates.get(3),
              rawCoordinates.get(4),
              rawCoordinates.get(5));
    }
    if (rawCoordinates.size() == 4) {
      return BoundingBox.fromLngLats(
              rawCoordinates.get(0),
              rawCoordinates.get(1),
              rawCoordinates.get(2),
              rawCoordinates.get(3));
    } else {
      throw new GeoJsonException("The value of the bbox member MUST be an array of length 2*n where"
              + " n is the number of dimensions represented in the contained geometries,"
              + "with all axes of the most southwesterly point followed "
              + " by all axes of the more northeasterly point. The "
              + "axes order of a bbox follows the axes order of geometries.");
    }
  }
}
