package com.mapbox.api.geocoding.v5;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.api.geocoding.v5.models.RoutingInfo;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Type Adapter to convert a {@link RoutingInfo} object.
 *
 * @since 4.10.0
 */
public class RoutingInfoTypeAdapter extends TypeAdapter<RoutingInfo> {

  @Override
  public void write(JsonWriter jsonWriter, RoutingInfo routingInfo) throws IOException {
    if (routingInfo == null) {
      jsonWriter.nullValue();
      return;
    }
    /*
      jsonWriter.beginArray();
      List<RoutableDestination> routableDestinations = routingInfo.routableDestinations();
      for (RoutableDestination singleRoutableDestination: routableDestinations) {
        jsonWriter.value(GeoJsonUtils.trim(singleRoutableDestination.longitude()));
        jsonWriter.value(GeoJsonUtils.trim(singleRoutableDestination.latitude()));
      }
      jsonWriter.endArray();
    */

    jsonWriter.beginObject();
    jsonWriter.name("points").value(routingInfo.routableDestinations().toString());
    jsonWriter.endObject();

  }

  @Override
  public RoutingInfo read(JsonReader in) throws IOException {
    List<Point> pointList = new ArrayList<>();
    in.beginArray();
    while (in.hasNext()) {
      pointList.add(Point.fromLngLat(in.nextDouble(),in.nextDouble()));
    }
    in.endArray();
    return RoutingInfo.fromPoints(pointList);
  }
}
