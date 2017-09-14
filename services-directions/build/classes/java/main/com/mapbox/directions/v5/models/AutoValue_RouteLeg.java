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
final class AutoValue_RouteLeg extends $AutoValue_RouteLeg {
  AutoValue_RouteLeg(Double distance, Double duration, String summary, List<LegStep> steps,
      LegAnnotation annotation) {
    super(distance, duration, summary, steps, annotation);
  }

  public static final class GsonTypeAdapter extends TypeAdapter<RouteLeg> {
    private final TypeAdapter<Double> distanceAdapter;
    private final TypeAdapter<Double> durationAdapter;
    private final TypeAdapter<String> summaryAdapter;
    private final TypeAdapter<List<LegStep>> stepsAdapter;
    private final TypeAdapter<LegAnnotation> annotationAdapter;
    public GsonTypeAdapter(Gson gson) {
      this.distanceAdapter = gson.getAdapter(Double.class);
      this.durationAdapter = gson.getAdapter(Double.class);
      this.summaryAdapter = gson.getAdapter(String.class);
      this.stepsAdapter = (TypeAdapter<List<LegStep>>) gson.getAdapter(TypeToken.getParameterized(List.class, LegStep.class));
      this.annotationAdapter = gson.getAdapter(LegAnnotation.class);
    }
    @Override
    public void write(JsonWriter jsonWriter, RouteLeg object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      jsonWriter.name("distance");
      distanceAdapter.write(jsonWriter, object.distance());
      jsonWriter.name("duration");
      durationAdapter.write(jsonWriter, object.duration());
      jsonWriter.name("summary");
      summaryAdapter.write(jsonWriter, object.summary());
      jsonWriter.name("steps");
      stepsAdapter.write(jsonWriter, object.steps());
      jsonWriter.name("annotation");
      annotationAdapter.write(jsonWriter, object.annotation());
      jsonWriter.endObject();
    }
    @Override
    public RouteLeg read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      Double distance = null;
      Double duration = null;
      String summary = null;
      List<LegStep> steps = null;
      LegAnnotation annotation = null;
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
          case "summary": {
            summary = summaryAdapter.read(jsonReader);
            break;
          }
          case "steps": {
            steps = stepsAdapter.read(jsonReader);
            break;
          }
          case "annotation": {
            annotation = annotationAdapter.read(jsonReader);
            break;
          }
          default: {
            jsonReader.skipValue();
          }
        }
      }
      jsonReader.endObject();
      return new AutoValue_RouteLeg(distance, duration, summary, steps, annotation);
    }
  }
}
