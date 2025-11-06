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

public final class MaxSpeedGsonTypeAdapter extends TypeAdapter<MaxSpeed> {
    private volatile TypeAdapter<Integer> integer_adapter;
    private volatile TypeAdapter<String> string_adapter;
    private volatile TypeAdapter<Boolean> boolean__adapter;
    private final Gson gson;
    MaxSpeedGsonTypeAdapter(Gson gson) {
      this.gson = gson;
      integer_adapter = gson.getAdapter(Integer.class);
      string_adapter = gson.getAdapter(String.class);
      boolean__adapter = gson.getAdapter(Boolean.class);
      boolean__adapter = gson.getAdapter(Boolean.class);
    }
    @Override
    @SuppressWarnings("unchecked")
    public void write(JsonWriter jsonWriter, MaxSpeed object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      if(object.unrecognized() != null) {
        for (Map.Entry<String, SerializableJsonElement> entry : object.unrecognized().entrySet()) {
          jsonWriter.name(entry.getKey());
          JsonElement element = entry.getValue().getElement();
          TypeAdapter adapter = gson.getAdapter(element.getClass());
          adapter.write(jsonWriter, element);
        }
      }
      jsonWriter.name("speed");
      if (object.speed() == null) {
        jsonWriter.nullValue();
      } else {
        integer_adapter.write(jsonWriter, object.speed());
      }
      jsonWriter.name("unit");
      if (object.unit() == null) {
        jsonWriter.nullValue();
      } else {
        string_adapter.write(jsonWriter, object.unit());
      }
      jsonWriter.name("unknown");
      if (object.unknown() == null) {
        jsonWriter.nullValue();
      } else {
        boolean__adapter.write(jsonWriter, object.unknown());
      }
      jsonWriter.name("none");
      if (object.none() == null) {
        jsonWriter.nullValue();
      } else {
        boolean__adapter.write(jsonWriter, object.none());
      }
      jsonWriter.endObject();
    }
    @Override
    @SuppressWarnings("unchecked")
    public MaxSpeed read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      Map<String, SerializableJsonElement> unrecognised = null;
      Integer speed = null;
      String unit = null;
      Boolean unknown = null;
      Boolean none = null;
      while (jsonReader.hasNext()) {
        String _name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        if ("speed".equals(_name)) {
          speed = integer_adapter.read(jsonReader);
          continue;
        }
        if ("unit".equals(_name)) {
          unit = string_adapter.read(jsonReader).intern();
          continue;
        }
        if ("unknown".equals(_name)) {
          unknown = boolean__adapter.read(jsonReader);
          continue;
        }
        if ("none".equals(_name)) {
          none = boolean__adapter.read(jsonReader);
          continue;
        }
        if (unrecognised == null) {
          unrecognised = new LinkedHashMap<String, SerializableJsonElement>();
        }
        JsonElement element = gson.fromJson(jsonReader, JsonElement.class);
        unrecognised.put(_name, new SerializableJsonElement(element));
        continue;
      }
      jsonReader.endObject();
      return new AutoValue_MaxSpeed(unrecognised, speed, unit, unknown, none);
//      return builder.build();
    }
    @Override
    public String toString() {
      return new StringBuilder().append("TypeAdapter(").append("MaxSpeed").append(")").toString();
    }
  }