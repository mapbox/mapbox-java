package com.mapbox.directions.v5.models;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "com.ryanharter.auto.value.gson.AutoValueGsonExtension",
    comments = "https://github.com/rharter/auto-value-gson"
)
final class AutoValue_IntersectionLanes extends $AutoValue_IntersectionLanes {
  AutoValue_IntersectionLanes(Boolean valid, List<String> indications) {
    super(valid, indications);
  }

  public static final class GsonTypeAdapter extends TypeAdapter<IntersectionLanes> {
    private final TypeAdapter<Boolean> validAdapter;
    private final TypeAdapter<List<String>> indicationsAdapter;
    public GsonTypeAdapter(Gson gson) {
      this.validAdapter = gson.getAdapter(Boolean.class);
      this.indicationsAdapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
    }
    @Override
    public void write(JsonWriter jsonWriter, IntersectionLanes object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      jsonWriter.name("valid");
      validAdapter.write(jsonWriter, object.valid());
      jsonWriter.name("indications");
      indicationsAdapter.write(jsonWriter, object.indications());
      jsonWriter.endObject();
    }
    @Override
    public IntersectionLanes read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      Boolean valid = null;
      List<String> indications = null;
      while (jsonReader.hasNext()) {
        String _name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        switch (_name) {
          case "valid": {
            valid = validAdapter.read(jsonReader);
            break;
          }
          case "indications": {
            indications = indicationsAdapter.read(jsonReader);
            break;
          }
          default: {
            jsonReader.skipValue();
          }
        }
      }
      jsonReader.endObject();
      return new AutoValue_IntersectionLanes(valid, indications);
    }
  }
}
