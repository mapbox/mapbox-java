package com.mapbox.api.directions.v5.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.api.directions.v5.utils.ParseUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A TypeAdapter for {@link IntersectionLanes} used to optimize JSON deserialization.
 *
 * <p>Strings in {@link IntersectionLanes#indications()} and
 * {@link IntersectionLanes#validIndication()} can accept a limited set of possible values
 * (e.g., "straight," "right," "left," etc.).
 * This adapter invokes {@link String#intern()} on these strings to save memory.</p>
 */
class IntersectionLanesTypeAdapter extends TypeAdapter<IntersectionLanes> {

  private final TypeAdapter<IntersectionLanes> defaultAdapter;

  IntersectionLanesTypeAdapter(TypeAdapter<IntersectionLanes> defaultAdapter) {
    this.defaultAdapter = defaultAdapter;
  }

  @Override
  public void write(JsonWriter out, IntersectionLanes value) throws IOException {
    defaultAdapter.write(out, value);
  }

  @Override
  public IntersectionLanes read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    final JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();

    final IntersectionLanes.Builder builder = IntersectionLanes.builder();
    Map<String, JsonElement> unrecognized = null;

    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      final String key = entry.getKey();
      final JsonElement value = entry.getValue();

      if (value.isJsonNull()) {
        continue;
      }

      switch (key) {
        case "valid":
          builder.valid(value.getAsBoolean());
          break;

        case "active":
          builder.active(value.getAsBoolean());
          break;

        case "valid_indication":
          builder.validIndication(value.getAsString().intern());
          break;

        case "indications":
          builder.indications(ParseUtils.parseAndInternJsonStringArray(value.getAsJsonArray()));
          break;

        case "payment_methods":
          builder.paymentMethods(ParseUtils.parseAndInternJsonStringArray(value.getAsJsonArray()));
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
