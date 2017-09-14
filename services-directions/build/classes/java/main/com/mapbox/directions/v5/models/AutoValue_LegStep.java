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
final class AutoValue_LegStep extends $AutoValue_LegStep {
  AutoValue_LegStep(Double distance, Double duration, String geometry, String name, String ref,
      String destinations, String mode, String pronunciation, String rotaryName,
      String rotaryPronunciation, StepManeuver maneuver, Double weight,
      List<StepIntersection> intersections, String exits) {
    super(distance, duration, geometry, name, ref, destinations, mode, pronunciation, rotaryName, rotaryPronunciation, maneuver, weight, intersections, exits);
  }

  public static final class GsonTypeAdapter extends TypeAdapter<LegStep> {
    private final TypeAdapter<Double> distanceAdapter;
    private final TypeAdapter<Double> durationAdapter;
    private final TypeAdapter<String> geometryAdapter;
    private final TypeAdapter<String> nameAdapter;
    private final TypeAdapter<String> refAdapter;
    private final TypeAdapter<String> destinationsAdapter;
    private final TypeAdapter<String> modeAdapter;
    private final TypeAdapter<String> pronunciationAdapter;
    private final TypeAdapter<String> rotaryNameAdapter;
    private final TypeAdapter<String> rotaryPronunciationAdapter;
    private final TypeAdapter<StepManeuver> maneuverAdapter;
    private final TypeAdapter<Double> weightAdapter;
    private final TypeAdapter<List<StepIntersection>> intersectionsAdapter;
    private final TypeAdapter<String> exitsAdapter;
    public GsonTypeAdapter(Gson gson) {
      this.distanceAdapter = gson.getAdapter(Double.class);
      this.durationAdapter = gson.getAdapter(Double.class);
      this.geometryAdapter = gson.getAdapter(String.class);
      this.nameAdapter = gson.getAdapter(String.class);
      this.refAdapter = gson.getAdapter(String.class);
      this.destinationsAdapter = gson.getAdapter(String.class);
      this.modeAdapter = gson.getAdapter(String.class);
      this.pronunciationAdapter = gson.getAdapter(String.class);
      this.rotaryNameAdapter = gson.getAdapter(String.class);
      this.rotaryPronunciationAdapter = gson.getAdapter(String.class);
      this.maneuverAdapter = gson.getAdapter(StepManeuver.class);
      this.weightAdapter = gson.getAdapter(Double.class);
      this.intersectionsAdapter = (TypeAdapter<List<StepIntersection>>) gson.getAdapter(TypeToken.getParameterized(List.class, StepIntersection.class));
      this.exitsAdapter = gson.getAdapter(String.class);
    }
    @Override
    public void write(JsonWriter jsonWriter, LegStep object) throws IOException {
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
      jsonWriter.name("name");
      nameAdapter.write(jsonWriter, object.name());
      jsonWriter.name("ref");
      refAdapter.write(jsonWriter, object.ref());
      jsonWriter.name("destinations");
      destinationsAdapter.write(jsonWriter, object.destinations());
      jsonWriter.name("mode");
      modeAdapter.write(jsonWriter, object.mode());
      jsonWriter.name("pronunciation");
      pronunciationAdapter.write(jsonWriter, object.pronunciation());
      jsonWriter.name("rotary_name");
      rotaryNameAdapter.write(jsonWriter, object.rotaryName());
      jsonWriter.name("rotary_pronunciation");
      rotaryPronunciationAdapter.write(jsonWriter, object.rotaryPronunciation());
      jsonWriter.name("maneuver");
      maneuverAdapter.write(jsonWriter, object.maneuver());
      jsonWriter.name("weight");
      weightAdapter.write(jsonWriter, object.weight());
      jsonWriter.name("intersections");
      intersectionsAdapter.write(jsonWriter, object.intersections());
      jsonWriter.name("exits");
      exitsAdapter.write(jsonWriter, object.exits());
      jsonWriter.endObject();
    }
    @Override
    public LegStep read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      Double distance = null;
      Double duration = null;
      String geometry = null;
      String name = null;
      String ref = null;
      String destinations = null;
      String mode = null;
      String pronunciation = null;
      String rotaryName = null;
      String rotaryPronunciation = null;
      StepManeuver maneuver = null;
      Double weight = null;
      List<StepIntersection> intersections = null;
      String exits = null;
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
          case "name": {
            name = nameAdapter.read(jsonReader);
            break;
          }
          case "ref": {
            ref = refAdapter.read(jsonReader);
            break;
          }
          case "destinations": {
            destinations = destinationsAdapter.read(jsonReader);
            break;
          }
          case "mode": {
            mode = modeAdapter.read(jsonReader);
            break;
          }
          case "pronunciation": {
            pronunciation = pronunciationAdapter.read(jsonReader);
            break;
          }
          case "rotary_name": {
            rotaryName = rotaryNameAdapter.read(jsonReader);
            break;
          }
          case "rotary_pronunciation": {
            rotaryPronunciation = rotaryPronunciationAdapter.read(jsonReader);
            break;
          }
          case "maneuver": {
            maneuver = maneuverAdapter.read(jsonReader);
            break;
          }
          case "weight": {
            weight = weightAdapter.read(jsonReader);
            break;
          }
          case "intersections": {
            intersections = intersectionsAdapter.read(jsonReader);
            break;
          }
          case "exits": {
            exits = exitsAdapter.read(jsonReader);
            break;
          }
          default: {
            jsonReader.skipValue();
          }
        }
      }
      jsonReader.endObject();
      return new AutoValue_LegStep(distance, duration, geometry, name, ref, destinations, mode, pronunciation, rotaryName, rotaryPronunciation, maneuver, weight, intersections, exits);
    }
  }
}
