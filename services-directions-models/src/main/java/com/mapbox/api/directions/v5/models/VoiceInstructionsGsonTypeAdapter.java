package com.mapbox.api.directions.v5.models;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.auto.value.gson.SerializableJsonElement;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

final class VoiceInstructionsGsonTypeAdapter extends TypeAdapter<VoiceInstructions> {
  private volatile TypeAdapter<Double> double__adapter;
  private volatile TypeAdapter<String> string_adapter;
  private final Gson gson;

  VoiceInstructionsGsonTypeAdapter(Gson gson) {
    this.gson = gson;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void write(JsonWriter jsonWriter, VoiceInstructions object) throws IOException {
    if (object == null) {
      jsonWriter.nullValue();
      return;
    }
    jsonWriter.beginObject();
    if (object.unrecognized() != null) {
      for (Map.Entry<String, SerializableJsonElement> entry : object.unrecognized().entrySet()) {
        jsonWriter.name(entry.getKey());
        JsonElement element = entry.getValue().getElement();
        TypeAdapter adapter = gson.getAdapter(element.getClass());
        adapter.write(jsonWriter, element);
      }
    }
    jsonWriter.name("distanceAlongGeometry");
    if (object.distanceAlongGeometry() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<Double> double__adapter = this.double__adapter;
      if (double__adapter == null) {
        this.double__adapter = double__adapter = gson.getAdapter(Double.class);
      }
      double__adapter.write(jsonWriter, object.distanceAlongGeometry());
    }
    jsonWriter.name("announcement");
    if (object.announcement() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<String> string_adapter = this.string_adapter;
      if (string_adapter == null) {
        this.string_adapter = string_adapter = gson.getAdapter(String.class);
      }
      string_adapter.write(jsonWriter, object.announcement());
    }
    jsonWriter.name("ssmlAnnouncement");
    if (object.ssmlAnnouncement() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<String> string_adapter = this.string_adapter;
      if (string_adapter == null) {
        this.string_adapter = string_adapter = gson.getAdapter(String.class);
      }
      string_adapter.write(jsonWriter, object.ssmlAnnouncement());
    }
    jsonWriter.endObject();
  }

  @Override
  @SuppressWarnings("unchecked")
  public VoiceInstructions read(JsonReader jsonReader) throws IOException {
    if (jsonReader.peek() == JsonToken.NULL) {
      jsonReader.nextNull();
      return null;
    }
    jsonReader.beginObject();
    LinkedHashMap<String, SerializableJsonElement> unrecognised = null;
    Double distanceAlongGeometry = null;
    String announcement = null;
    String ssmlAnnouncement = null;
    while (jsonReader.hasNext()) {
      String _name = jsonReader.nextName();
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        continue;
      }
      switch (_name) {
        default: {
          if ("distanceAlongGeometry".equals(_name)) {
            TypeAdapter<Double> double__adapter = this.double__adapter;
            if (double__adapter == null) {
              this.double__adapter = double__adapter = gson.getAdapter(Double.class);
            }
            distanceAlongGeometry = double__adapter.read(jsonReader);
            continue;
          }
          if ("announcement".equals(_name)) {
            TypeAdapter<String> string_adapter = this.string_adapter;
            if (string_adapter == null) {
              this.string_adapter = string_adapter = gson.getAdapter(String.class);
            }
            announcement = string_adapter.read(jsonReader);
            continue;
          }
          if ("ssmlAnnouncement".equals(_name)) {
            TypeAdapter<String> string_adapter = this.string_adapter;
            if (string_adapter == null) {
              this.string_adapter = string_adapter = gson.getAdapter(String.class);
            }
            ssmlAnnouncement = string_adapter.read(jsonReader);
            continue;
          }
          if (unrecognised == null) {
            unrecognised = new LinkedHashMap<String, SerializableJsonElement>();
          }
          JsonElement element = gson.fromJson(jsonReader, JsonElement.class);
          unrecognised.put(_name, new SerializableJsonElement(element));
        }
      }
    }
    jsonReader.endObject();
    return new AutoValue_VoiceInstructions(
            unrecognised,
            distanceAlongGeometry,
            announcement,
            ssmlAnnouncement);
  }

  @Override
  public String toString() {
    return new StringBuilder().append("TypeAdapter(").append("VoiceInstructions").append(")").toString();
  }
}