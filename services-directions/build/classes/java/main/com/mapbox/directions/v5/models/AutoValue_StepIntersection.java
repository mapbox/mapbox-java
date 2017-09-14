package com.mapbox.directions.v5.models;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "com.ryanharter.auto.value.gson.AutoValueGsonExtension",
    comments = "https://github.com/rharter/auto-value-gson"
)
final class AutoValue_StepIntersection extends $AutoValue_StepIntersection {
  AutoValue_StepIntersection(double[] rawLocation, List<Integer> bearings, List<String> classes,
      List<Boolean> entry, Integer in, Integer out, List<IntersectionLanes> lanes) {
    super(rawLocation, bearings, classes, entry, in, out, lanes);
  }

  public static final class GsonTypeAdapter extends TypeAdapter<StepIntersection> {
    private final TypeAdapter<double[]> rawLocationAdapter;
    private final TypeAdapter<List<Integer>> bearingsAdapter;
    private final TypeAdapter<List<String>> classesAdapter;
    private final TypeAdapter<List<Boolean>> entryAdapter;
    private final TypeAdapter<Integer> inAdapter;
    private final TypeAdapter<Integer> outAdapter;
    private final TypeAdapter<List<IntersectionLanes>> lanesAdapter;
    public GsonTypeAdapter(Gson gson) {
      this.rawLocationAdapter = gson.getAdapter(double[].class);
      this.bearingsAdapter = (TypeAdapter<List<Integer>>) gson.getAdapter(TypeToken.getParameterized(List.class, Integer.class));
      this.classesAdapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
      this.entryAdapter = (TypeAdapter<List<Boolean>>) gson.getAdapter(TypeToken.getParameterized(List.class, Boolean.class));
      this.inAdapter = gson.getAdapter(Integer.class);
      this.outAdapter = gson.getAdapter(Integer.class);
      this.lanesAdapter = (TypeAdapter<List<IntersectionLanes>>) gson.getAdapter(TypeToken.getParameterized(List.class, IntersectionLanes.class));
    }
    @Override
    public void write(JsonWriter jsonWriter, StepIntersection object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      jsonWriter.name("location");
      rawLocationAdapter.write(jsonWriter, object.rawLocation());
      jsonWriter.name("bearings");
      bearingsAdapter.write(jsonWriter, object.bearings());
      jsonWriter.name("classes");
      classesAdapter.write(jsonWriter, object.classes());
      jsonWriter.name("entry");
      entryAdapter.write(jsonWriter, object.entry());
      jsonWriter.name("in");
      inAdapter.write(jsonWriter, object.in());
      jsonWriter.name("out");
      outAdapter.write(jsonWriter, object.out());
      jsonWriter.name("lanes");
      lanesAdapter.write(jsonWriter, object.lanes());
      jsonWriter.endObject();
    }
    @Override
    public StepIntersection read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      double[] rawLocation = null;
      List<Integer> bearings = null;
      List<String> classes = null;
      List<Boolean> entry = null;
      Integer in = null;
      Integer out = null;
      List<IntersectionLanes> lanes = null;
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
          case "bearings": {
            bearings = bearingsAdapter.read(jsonReader);
            break;
          }
          case "classes": {
            classes = classesAdapter.read(jsonReader);
            break;
          }
          case "entry": {
            entry = entryAdapter.read(jsonReader);
            break;
          }
          case "in": {
            in = inAdapter.read(jsonReader);
            break;
          }
          case "out": {
            out = outAdapter.read(jsonReader);
            break;
          }
          case "lanes": {
            lanes = lanesAdapter.read(jsonReader);
            break;
          }
          default: {
            jsonReader.skipValue();
          }
        }
      }
      jsonReader.endObject();
      return new AutoValue_StepIntersection(rawLocation, bearings, classes, entry, in, out, lanes);
    }
  }
}
