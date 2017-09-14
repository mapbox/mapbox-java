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
final class AutoValue_LegAnnotation extends $AutoValue_LegAnnotation {
  AutoValue_LegAnnotation(List<Double> distance, List<Double> duration, List<Double> speed,
      List<String> congestion) {
    super(distance, duration, speed, congestion);
  }

  public static final class GsonTypeAdapter extends TypeAdapter<LegAnnotation> {
    private final TypeAdapter<List<Double>> distanceAdapter;
    private final TypeAdapter<List<Double>> durationAdapter;
    private final TypeAdapter<List<Double>> speedAdapter;
    private final TypeAdapter<List<String>> congestionAdapter;
    public GsonTypeAdapter(Gson gson) {
      this.distanceAdapter = (TypeAdapter<List<Double>>) gson.getAdapter(TypeToken.getParameterized(List.class, Double.class));
      this.durationAdapter = (TypeAdapter<List<Double>>) gson.getAdapter(TypeToken.getParameterized(List.class, Double.class));
      this.speedAdapter = (TypeAdapter<List<Double>>) gson.getAdapter(TypeToken.getParameterized(List.class, Double.class));
      this.congestionAdapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
    }
    @Override
    public void write(JsonWriter jsonWriter, LegAnnotation object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      jsonWriter.name("distance");
      distanceAdapter.write(jsonWriter, object.distance());
      jsonWriter.name("duration");
      durationAdapter.write(jsonWriter, object.duration());
      jsonWriter.name("speed");
      speedAdapter.write(jsonWriter, object.speed());
      jsonWriter.name("congestion");
      congestionAdapter.write(jsonWriter, object.congestion());
      jsonWriter.endObject();
    }
    @Override
    public LegAnnotation read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      List<Double> distance = null;
      List<Double> duration = null;
      List<Double> speed = null;
      List<String> congestion = null;
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
          case "speed": {
            speed = speedAdapter.read(jsonReader);
            break;
          }
          case "congestion": {
            congestion = congestionAdapter.read(jsonReader);
            break;
          }
          default: {
            jsonReader.skipValue();
          }
        }
      }
      jsonReader.endObject();
      return new AutoValue_LegAnnotation(distance, duration, speed, congestion);
    }
  }
}
