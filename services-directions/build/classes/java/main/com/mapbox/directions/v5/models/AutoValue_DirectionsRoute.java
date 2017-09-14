package com.mapbox.directions.v5.models;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Double;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "com.ryanharter.auto.value.gson.AutoValueGsonExtension",
    comments = "https://github.com/rharter/auto-value-gson"
)
final class AutoValue_DirectionsRoute extends $AutoValue_DirectionsRoute {
  AutoValue_DirectionsRoute(Double distance, Double duration, String geometry, Double weight,
      String weightName, List<RouteLeg> legs) {
    super(distance, duration, geometry, weight, weightName, legs);
  }

  public static final class GsonTypeAdapter extends TypeAdapter<DirectionsRoute> {
    private final TypeAdapter<Double> distanceAdapter;
    private final TypeAdapter<Double> durationAdapter;
    private final TypeAdapter<String> geometryAdapter;
    private final TypeAdapter<Double> weightAdapter;
    private final TypeAdapter<String> weightNameAdapter;
    private final TypeAdapter<List<RouteLeg>> legsAdapter;
    public GsonTypeAdapter(Gson gson) {
      this.distanceAdapter = gson.getAdapter(Double.class);
      this.durationAdapter = gson.getAdapter(Double.class);
      this.geometryAdapter = gson.getAdapter(String.class);
      this.weightAdapter = gson.getAdapter(Double.class);
      this.weightNameAdapter = gson.getAdapter(String.class);
      this.legsAdapter = (TypeAdapter<List<RouteLeg>>) gson.getAdapter(TypeToken.getParameterized(List.class, RouteLeg.class));
    }
    @Override
    public void write(JsonWriter jsonWriter, DirectionsRoute object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      jsonWriter.name("distance");
      distanceAdapter.write(jsonWriter, object.distance());
      jsonWriter.name("duration");
      durationAdapter.write(jsonWriter, object.duration());
      jsonWriter.name("geometry");
      geometryAdapter.write(jsonWriter, object.geometry());
      jsonWriter.name("weight");
      weightAdapter.write(jsonWriter, object.weight());
      jsonWriter.name("weight_name");
      weightNameAdapter.write(jsonWriter, object.weightName());
      jsonWriter.name("legs");
      legsAdapter.write(jsonWriter, object.legs());
      jsonWriter.endObject();
    }
    @Override
    public DirectionsRoute read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      Double distance = null;
      Double duration = null;
      String geometry = null;
      Double weight = null;
      String weightName = null;
      List<RouteLeg> legs = null;
      while (jsonReader.hasNext()) {
        String _name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        switch (_name) {
          case "distance": {
            distance = distanceAdapter.read(jsonReader);
            break;
          }
          case "duration": {
            duration = durationAdapter.read(jsonReader);
            break;
          }
          case "geometry": {
            geometry = geometryAdapter.read(jsonReader);
            break;
          }
          case "weight": {
            weight = weightAdapter.read(jsonReader);
            break;
          }
          case "weight_name": {
            weightName = weightNameAdapter.read(jsonReader);
            break;
          }
          case "legs": {
            legs = legsAdapter.read(jsonReader);
            break;
          }
          default: {
            jsonReader.skipValue();
          }
        }
      }
      jsonReader.endObject();
      return new AutoValue_DirectionsRoute(distance, duration, geometry, weight, weightName, legs);
    }
  }
}
