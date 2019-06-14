package com.mapbox.api.geocoding.v5;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mapbox.api.geocoding.v5.models.RoutableDestination;
import com.mapbox.api.geocoding.v5.models.RoutingInfo;

import java.lang.reflect.Type;

/**
 * Type Adapter to convert a {@link RoutableDestination} object.
 *
 * @since 4.10.0
 */
public class RoutingInfoDeserializer implements JsonDeserializer<RoutingInfo> {

  @Override
  public RoutingInfo deserialize(JsonElement jsonElement,
                                 Type type,
                                 JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {

    // Get the routable_points JSON element from the Geocoding API response
    JsonElement content = jsonElement.getAsJsonObject().get("routable_points");

    // Deserialize it. You use a new instance of Gson to avoid infinite recursion
    // to this deserializer
    return new Gson().fromJson(content, RoutingInfo.class);
  }

  /*@Override
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
*/
}
