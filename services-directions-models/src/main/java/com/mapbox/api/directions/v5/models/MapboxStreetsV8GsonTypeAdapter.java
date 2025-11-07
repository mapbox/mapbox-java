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

 final class MapboxStreetsV8GsonTypeAdapter extends TypeAdapter<MapboxStreetsV8> {
    private volatile TypeAdapter<String> string_adapter;
    private final Gson gson;
    MapboxStreetsV8GsonTypeAdapter(Gson gson) {
      this.gson = gson;
    }
    @Override
    @SuppressWarnings("unchecked")
    public void write(JsonWriter jsonWriter, MapboxStreetsV8 object) throws IOException {
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
      jsonWriter.name("class");
      if (object.roadClass() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<String> string_adapter = this.string_adapter;
        if (string_adapter == null) {
          this.string_adapter = string_adapter = gson.getAdapter(String.class);
        }
        string_adapter.write(jsonWriter, object.roadClass());
      }
      jsonWriter.endObject();
    }
    @Override
    @SuppressWarnings("unchecked")
    public MapboxStreetsV8 read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      LinkedHashMap<String, SerializableJsonElement> unrecognised = null;
      String roadClass = null;
      while (jsonReader.hasNext()) {
        String _name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        if (_name.equals("class")) {
          TypeAdapter<String> string_adapter = this.string_adapter;
          if (string_adapter == null) {
            this.string_adapter = string_adapter = gson.getAdapter(String.class);
          }
          roadClass = string_adapter.read(jsonReader).intern();
        } else {
          if (unrecognised == null) {
            unrecognised = new LinkedHashMap<String, SerializableJsonElement>();
          }
          JsonElement element = gson.fromJson(jsonReader, JsonElement.class);
          unrecognised.put(_name, new SerializableJsonElement(element));
        }
      }
      jsonReader.endObject();
      return new AutoValue_MapboxStreetsV8(
              unrecognised,
              roadClass);
    }
    @Override
    public String toString() {
      return new StringBuilder().append("TypeAdapter(").append("MapboxStreetsV8").append(")").toString();
    }
  }