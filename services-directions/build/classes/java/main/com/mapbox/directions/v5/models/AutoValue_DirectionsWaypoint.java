package com.mapbox.directions.v5.models;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated(
    value = "com.ryanharter.auto.value.gson.AutoValueGsonExtension",
    comments = "https://github.com/rharter/auto-value-gson"
)
final class AutoValue_DirectionsWaypoint extends $AutoValue_DirectionsWaypoint {
  AutoValue_DirectionsWaypoint(String name, double[] rawLocation) {
    super(name, rawLocation);
  }

  public static final class GsonTypeAdapter extends TypeAdapter<DirectionsWaypoint> {
    private final TypeAdapter<String> nameAdapter;
    private final TypeAdapter<double[]> rawLocationAdapter;
    public GsonTypeAdapter(Gson gson) {
      this.nameAdapter = gson.getAdapter(String.class);
      this.rawLocationAdapter = gson.getAdapter(double[].class);
    }
    @Override
    public void write(JsonWriter jsonWriter, DirectionsWaypoint object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      jsonWriter.name("name");
      nameAdapter.write(jsonWriter, object.name());
      jsonWriter.name("location");
      rawLocationAdapter.write(jsonWriter, object.rawLocation());
      jsonWriter.endObject();
    }
    @Override
    public DirectionsWaypoint read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      String name = null;
      double[] rawLocation = null;
      while (jsonReader.hasNext()) {
        String _name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        switch (_name) {
          case "name": {
            name = nameAdapter.read(jsonReader);
            break;
          }
          case "location": {
            rawLocation = rawLocationAdapter.read(jsonReader);
            break;
          }
          default: {
            jsonReader.skipValue();
          }
        }
      }
      jsonReader.endObject();
      return new AutoValue_DirectionsWaypoint(name, rawLocation);
    }
  }
}
