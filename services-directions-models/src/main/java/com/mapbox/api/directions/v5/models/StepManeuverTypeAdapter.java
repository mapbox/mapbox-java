package com.mapbox.api.directions.v5.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A TypeAdapter for {@link StepManeuver} used to optimize JSON deserialization.
 *
 * <p>Strings {@link StepManeuver#type()} and {@link StepManeuver#modifier()} can accept
 * a limited set of possible values.
 * This adapter invokes {@link String#intern()} on these strings to save memory.</p>
 */
class StepManeuverTypeAdapter extends TypeAdapter<StepManeuver> {
  private final TypeAdapter<StepManeuver> defaultAdapter;

  StepManeuverTypeAdapter(TypeAdapter<StepManeuver> defaultAdapter) {
    this.defaultAdapter = defaultAdapter;
  }

  @Override
  public void write(JsonWriter out, StepManeuver value) throws IOException {
    defaultAdapter.write(out, value);
  }

  @Override
  public StepManeuver read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    final JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();

    final StepManeuver.Builder builder = StepManeuver.builder();
    Map<String, JsonElement> unrecognized = null;

    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      final String key = entry.getKey();
      final JsonElement value = entry.getValue();

      if (value.isJsonNull()) {
        continue;
      }

      switch (key) {
        case "location":
          final JsonArray jsonArray = value.getAsJsonArray();
          final double[] locations = new double[jsonArray.size()];
          for (int i = 0; i < jsonArray.size(); ++i) {
            locations[i] = jsonArray.get(i).getAsDouble();
          }
          builder.rawLocation(locations);
          break;

        case "bearing_before":
          builder.bearingBefore(value.getAsDouble());
          break;

        case "bearing_after":
          builder.bearingAfter(value.getAsDouble());
          break;

        case "instruction":
          builder.instruction(value.getAsString());
          break;

        case "type":
          builder.type(value.getAsString().intern());
          break;

        case "modifier":
          builder.modifier(value.getAsString().intern());
          break;

        case "exit":
          builder.exit(value.getAsInt());
          break;

        default:
          if (unrecognized == null) {
            unrecognized = new LinkedHashMap<>();
          }
          unrecognized.put(key, value);
          break;
      }
    }

    return builder
      .unrecognizedJsonProperties(unrecognized)
      .build();
  }
}
