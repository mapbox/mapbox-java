package com.mapbox.api.directions.v5.models;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.auto.value.gson.SerializableJsonElement;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class BannerComponentsGsonTypeAdapter extends TypeAdapter<BannerComponents> {
    private volatile TypeAdapter<Map<String, SerializableJsonElement>> map__string_serializableJsonElement_adapter;
    private volatile TypeAdapter<String> string_adapter;
    private volatile TypeAdapter<Integer> integer_adapter;
    private volatile TypeAdapter<MapboxShield> mapboxShield_adapter;
    private volatile TypeAdapter<List<String>> list__string_adapter;
    private volatile TypeAdapter<Boolean> boolean__adapter;
    private final Gson gson;
    BannerComponentsGsonTypeAdapter(Gson gson) {
      this.gson = gson;
    }
    @Override
    @SuppressWarnings("unchecked")
    public void write(JsonWriter jsonWriter, BannerComponents object) throws IOException {
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
      jsonWriter.name("text");
      if (object.text() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<String> string_adapter = this.string_adapter;
        if (string_adapter == null) {
          this.string_adapter = string_adapter = gson.getAdapter(String.class);
        }
        string_adapter.write(jsonWriter, object.text());
      }
      jsonWriter.name("type");
      if (object.type() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<String> string_adapter = this.string_adapter;
        if (string_adapter == null) {
          this.string_adapter = string_adapter = gson.getAdapter(String.class);
        }
        string_adapter.write(jsonWriter, object.type());
      }
      jsonWriter.name("subType");
      if (object.subType() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<String> string_adapter = this.string_adapter;
        if (string_adapter == null) {
          this.string_adapter = string_adapter = gson.getAdapter(String.class);
        }
        string_adapter.write(jsonWriter, object.subType());
      }
      jsonWriter.name("abbr");
      if (object.abbreviation() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<String> string_adapter = this.string_adapter;
        if (string_adapter == null) {
          this.string_adapter = string_adapter = gson.getAdapter(String.class);
        }
        string_adapter.write(jsonWriter, object.abbreviation());
      }
      jsonWriter.name("abbr_priority");
      if (object.abbreviationPriority() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<Integer> integer_adapter = this.integer_adapter;
        if (integer_adapter == null) {
          this.integer_adapter = integer_adapter = gson.getAdapter(Integer.class);
        }
        integer_adapter.write(jsonWriter, object.abbreviationPriority());
      }
      jsonWriter.name("imageBaseURL");
      if (object.imageBaseUrl() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<String> string_adapter = this.string_adapter;
        if (string_adapter == null) {
          this.string_adapter = string_adapter = gson.getAdapter(String.class);
        }
        string_adapter.write(jsonWriter, object.imageBaseUrl());
      }
      jsonWriter.name("mapbox_shield");
      if (object.mapboxShield() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<MapboxShield> mapboxShield_adapter = this.mapboxShield_adapter;
        if (mapboxShield_adapter == null) {
          this.mapboxShield_adapter = mapboxShield_adapter = gson.getAdapter(MapboxShield.class);
        }
        mapboxShield_adapter.write(jsonWriter, object.mapboxShield());
      }
      jsonWriter.name("imageURL");
      if (object.imageUrl() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<String> string_adapter = this.string_adapter;
        if (string_adapter == null) {
          this.string_adapter = string_adapter = gson.getAdapter(String.class);
        }
        string_adapter.write(jsonWriter, object.imageUrl());
      }
      jsonWriter.name("directions");
      if (object.directions() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<List<String>> list__string_adapter = this.list__string_adapter;
        if (list__string_adapter == null) {
          this.list__string_adapter = list__string_adapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
        }
        list__string_adapter.write(jsonWriter, object.directions());
      }
      jsonWriter.name("active");
      if (object.active() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
        if (boolean__adapter == null) {
          this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
        }
        boolean__adapter.write(jsonWriter, object.active());
      }
      jsonWriter.name("active_direction");
      if (object.activeDirection() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<String> string_adapter = this.string_adapter;
        if (string_adapter == null) {
          this.string_adapter = string_adapter = gson.getAdapter(String.class);
        }
        string_adapter.write(jsonWriter, object.activeDirection());
      }
      jsonWriter.endObject();
    }
    @Override
    @SuppressWarnings("unchecked")
    public BannerComponents read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      LinkedHashMap<String, SerializableJsonElement> unrecognised = null;
      String text = null;
      String type = null;
      String subType = null;
      String abbreviation = null;
      Integer abbreviationPriority = null;
      String imageBaseUrl = null;
      MapboxShield mapboxShield = null;
      String imageUrl = null;
      List<String> directions = null;
      Boolean active = null;
      String activeDirection = null;
      while (jsonReader.hasNext()) {
        String _name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        switch (_name) {
          case "abbr": {
            TypeAdapter<String> string_adapter = this.string_adapter;
            if (string_adapter == null) {
              this.string_adapter = string_adapter = gson.getAdapter(String.class);
            }
            abbreviation = string_adapter.read(jsonReader);
            break;
          }
          case "abbr_priority": {
            TypeAdapter<Integer> integer_adapter = this.integer_adapter;
            if (integer_adapter == null) {
              this.integer_adapter = integer_adapter = gson.getAdapter(Integer.class);
            }
            abbreviationPriority = integer_adapter.read(jsonReader);
            break;
          }
          case "imageBaseURL": {
            TypeAdapter<String> string_adapter = this.string_adapter;
            if (string_adapter == null) {
              this.string_adapter = string_adapter = gson.getAdapter(String.class);
            }
            imageBaseUrl = string_adapter.read(jsonReader);
            break;
          }
          case "mapbox_shield": {
            TypeAdapter<MapboxShield> mapboxShield_adapter = this.mapboxShield_adapter;
            if (mapboxShield_adapter == null) {
              this.mapboxShield_adapter = mapboxShield_adapter = gson.getAdapter(MapboxShield.class);
            }
            mapboxShield = mapboxShield_adapter.read(jsonReader);
            break;
          }
          case "imageURL": {
            TypeAdapter<String> string_adapter = this.string_adapter;
            if (string_adapter == null) {
              this.string_adapter = string_adapter = gson.getAdapter(String.class);
            }
            imageUrl = string_adapter.read(jsonReader);
            break;
          }
          case "active_direction": {
            TypeAdapter<String> string_adapter = this.string_adapter;
            if (string_adapter == null) {
              this.string_adapter = string_adapter = gson.getAdapter(String.class);
            }
            activeDirection = string_adapter.read(jsonReader);
            break;
          }
          default: {
            if ("text".equals(_name)) {
              TypeAdapter<String> string_adapter = this.string_adapter;
              if (string_adapter == null) {
                this.string_adapter = string_adapter = gson.getAdapter(String.class);
              }
              text = string_adapter.read(jsonReader);
              continue;
            }
            if ("type".equals(_name)) {
              TypeAdapter<String> string_adapter = this.string_adapter;
              if (string_adapter == null) {
                this.string_adapter = string_adapter = gson.getAdapter(String.class);
              }
              type = string_adapter.read(jsonReader);
              continue;
            }
            if ("subType".equals(_name)) {
              TypeAdapter<String> string_adapter = this.string_adapter;
              if (string_adapter == null) {
                this.string_adapter = string_adapter = gson.getAdapter(String.class);
              }
              subType = string_adapter.read(jsonReader);
              continue;
            }
            if ("directions".equals(_name)) {
              TypeAdapter<List<String>> list__string_adapter = this.list__string_adapter;
              if (list__string_adapter == null) {
                this.list__string_adapter = list__string_adapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
              }
              directions = list__string_adapter.read(jsonReader);
              continue;
            }
            if ("active".equals(_name)) {
              TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
              if (boolean__adapter == null) {
                this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
              }
              active = boolean__adapter.read(jsonReader);
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
      String missing = "";
      if (text == null) {
        missing += " text";
      }
      if (type == null) {
        missing += " type";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_BannerComponents(
              unrecognised,
              text,
              type,
              subType,
              abbreviation,
              abbreviationPriority,
              imageBaseUrl,
              mapboxShield,
              imageUrl,
              directions,
              active,
              activeDirection);
    }
    @Override
    public String toString() {
      return new StringBuilder().append("TypeAdapter(").append("BannerComponents").append(")").toString();
    }
  }