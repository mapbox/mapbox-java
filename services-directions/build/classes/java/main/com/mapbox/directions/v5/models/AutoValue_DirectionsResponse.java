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
final class AutoValue_DirectionsResponse extends $AutoValue_DirectionsResponse {
  AutoValue_DirectionsResponse(String code) {
    super(code);
  }

  public static final class GsonTypeAdapter extends TypeAdapter<DirectionsResponse> {
    private final TypeAdapter<String> codeAdapter;
    public GsonTypeAdapter(Gson gson) {
      this.codeAdapter = gson.getAdapter(String.class);
    }
    @Override
    public void write(JsonWriter jsonWriter, DirectionsResponse object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      jsonWriter.name("code");
      codeAdapter.write(jsonWriter, object.code());
      jsonWriter.endObject();
    }
    @Override
    public DirectionsResponse read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      String code = null;
      while (jsonReader.hasNext()) {
        String _name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        switch (_name) {
          case "code": {
            code = codeAdapter.read(jsonReader);
            break;
          }
          default: {
            jsonReader.skipValue();
          }
        }
      }
      jsonReader.endObject();
      return new AutoValue_DirectionsResponse(code);
    }
  }
}
