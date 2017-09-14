package com.mapbox.directions.v5.models;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated(
    value = "com.ryanharter.auto.value.gson.AutoValueGsonExtension",
    comments = "https://github.com/rharter/auto-value-gson"
)
final class AutoValue_StepManeuver extends $AutoValue_StepManeuver {
  AutoValue_StepManeuver(double[] rawLocation, Double bearingBefore, Double bearingAfter,
      String instruction, String type, String modifier, Integer exit) {
    super(rawLocation, bearingBefore, bearingAfter, instruction, type, modifier, exit);
  }

  public static final class GsonTypeAdapter extends TypeAdapter<StepManeuver> {
    private final TypeAdapter<double[]> rawLocationAdapter;
    private final TypeAdapter<Double> bearingBeforeAdapter;
    private final TypeAdapter<Double> bearingAfterAdapter;
    private final TypeAdapter<String> instructionAdapter;
    private final TypeAdapter<String> typeAdapter;
    private final TypeAdapter<String> modifierAdapter;
    private final TypeAdapter<Integer> exitAdapter;
    public GsonTypeAdapter(Gson gson) {
      this.rawLocationAdapter = gson.getAdapter(double[].class);
      this.bearingBeforeAdapter = gson.getAdapter(Double.class);
      this.bearingAfterAdapter = gson.getAdapter(Double.class);
      this.instructionAdapter = gson.getAdapter(String.class);
      this.typeAdapter = gson.getAdapter(String.class);
      this.modifierAdapter = gson.getAdapter(String.class);
      this.exitAdapter = gson.getAdapter(Integer.class);
    }
    @Override
    public void write(JsonWriter jsonWriter, StepManeuver object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      jsonWriter.name("location");
      rawLocationAdapter.write(jsonWriter, object.rawLocation());
      jsonWriter.name("bearing_before");
      bearingBeforeAdapter.write(jsonWriter, object.bearingBefore());
      jsonWriter.name("bearing_after");
      bearingAfterAdapter.write(jsonWriter, object.bearingAfter());
      jsonWriter.name("instruction");
      instructionAdapter.write(jsonWriter, object.instruction());
      jsonWriter.name("type");
      typeAdapter.write(jsonWriter, object.type());
      jsonWriter.name("modifier");
      modifierAdapter.write(jsonWriter, object.modifier());
      jsonWriter.name("exit");
      exitAdapter.write(jsonWriter, object.exit());
      jsonWriter.endObject();
    }
    @Override
    public StepManeuver read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      double[] rawLocation = null;
      Double bearingBefore = null;
      Double bearingAfter = null;
      String instruction = null;
      String type = null;
      String modifier = null;
      Integer exit = null;
      while (jsonReader.hasNext()) {
        String _name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        switch (_name) {
          case "location": {
            rawLocation = rawLocationAdapter.read(jsonReader);
            break;
          }
          case "bearing_before": {
            bearingBefore = bearingBeforeAdapter.read(jsonReader);
            break;
          }
          case "bearing_after": {
            bearingAfter = bearingAfterAdapter.read(jsonReader);
            break;
          }
          case "instruction": {
            instruction = instructionAdapter.read(jsonReader);
            break;
          }
          case "type": {
            type = typeAdapter.read(jsonReader);
            break;
          }
          case "modifier": {
            modifier = modifierAdapter.read(jsonReader);
            break;
          }
          case "exit": {
            exit = exitAdapter.read(jsonReader);
            break;
          }
          default: {
            jsonReader.skipValue();
          }
        }
      }
      jsonReader.endObject();
      return new AutoValue_StepManeuver(rawLocation, bearingBefore, bearingAfter, instruction, type, modifier, exit);
    }
  }
}
