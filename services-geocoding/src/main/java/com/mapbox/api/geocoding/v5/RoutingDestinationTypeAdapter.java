package com.mapbox.api.geocoding.v5;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.api.geocoding.v5.models.RoutableDestination;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.GeoJsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Type Adapter to convert a {@link RoutableDestination} object.
 *
 * @since 4.10.0
 */
public class RoutingDestinationTypeAdapter extends TypeAdapter<RoutableDestination> {

  /**Empty constructor.*/
  public RoutingDestinationTypeAdapter() {
    super();
  }

  @Override
  public void write(JsonWriter jsonWriter, RoutableDestination routableDestination)
    throws IOException {
    if (routableDestination == null) {
      jsonWriter.nullValue();
      return;
    }
    jsonWriter.beginArray();
    List<Double> doubleList = routableDestination.coordinates();
    jsonWriter.value(GeoJsonUtils.trim(doubleList.get(0)));
    jsonWriter.value(GeoJsonUtils.trim(doubleList.get(1)));
    jsonWriter.endArray();
  }

  @Override
  public RoutableDestination read(JsonReader in) throws IOException {
    List<Double> rawCoordinates = new ArrayList<>();
    in.beginArray();
    while (in.hasNext()) {
      rawCoordinates.add(in.nextDouble());
    }
    in.endArray();
    return RoutableDestination.fromPoint(Point.fromLngLat(rawCoordinates.get(0),
      rawCoordinates.get(1)));
  }
}
