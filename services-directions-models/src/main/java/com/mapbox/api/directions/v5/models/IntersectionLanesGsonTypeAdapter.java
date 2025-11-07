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

final class IntersectionLanesGsonTypeAdapter extends TypeAdapter<IntersectionLanes> {
    private volatile TypeAdapter<Boolean> boolean__adapter;
    private volatile TypeAdapter<String> string_adapter;
    private volatile TypeAdapter<List<String>> list__string_adapter;
    private volatile TypeAdapter<IntersectionLaneAccess> intersectionLaneAccess_adapter;
    private final Gson gson;
    IntersectionLanesGsonTypeAdapter(Gson gson) {
      this.gson = gson;
    }
    @Override
    @SuppressWarnings("unchecked")
    public void write(JsonWriter jsonWriter, IntersectionLanes object) throws IOException {
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
      jsonWriter.name("valid");
      if (object.valid() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
        if (boolean__adapter == null) {
          this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
        }
        boolean__adapter.write(jsonWriter, object.valid());
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
      jsonWriter.name("valid_indication");
      if (object.validIndication() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<String> string_adapter = this.string_adapter;
        if (string_adapter == null) {
          this.string_adapter = string_adapter = gson.getAdapter(String.class);
        }
        string_adapter.write(jsonWriter, object.validIndication());
      }
      jsonWriter.name("indications");
      if (object.indications() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<List<String>> list__string_adapter = this.list__string_adapter;
        if (list__string_adapter == null) {
          this.list__string_adapter = list__string_adapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
        }
        list__string_adapter.write(jsonWriter, object.indications());
      }
      jsonWriter.name("payment_methods");
      if (object.paymentMethods() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<List<String>> list__string_adapter = this.list__string_adapter;
        if (list__string_adapter == null) {
          this.list__string_adapter = list__string_adapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
        }
        list__string_adapter.write(jsonWriter, object.paymentMethods());
      }
      jsonWriter.name("access");
      if (object.access() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<IntersectionLaneAccess> intersectionLaneAccess_adapter = this.intersectionLaneAccess_adapter;
        if (intersectionLaneAccess_adapter == null) {
          this.intersectionLaneAccess_adapter = intersectionLaneAccess_adapter = gson.getAdapter(IntersectionLaneAccess.class);
        }
        intersectionLaneAccess_adapter.write(jsonWriter, object.access());
      }
      jsonWriter.endObject();
    }
    @Override
    @SuppressWarnings("unchecked")
    public IntersectionLanes read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      LinkedHashMap<String, SerializableJsonElement> unrecognised = null;
      Boolean valid = null;
      Boolean active = null;
      String validIndication = null;
      List<String> indications = null;
      List<String> paymentMethods = null;
      IntersectionLaneAccess access = null;
      while (jsonReader.hasNext()) {
        String _name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        switch (_name) {
          case "valid_indication": {
            TypeAdapter<String> string_adapter = this.string_adapter;
            if (string_adapter == null) {
              this.string_adapter = string_adapter = gson.getAdapter(String.class);
            }
            validIndication = string_adapter.read(jsonReader);
            break;
          }
          case "payment_methods": {
            TypeAdapter<List<String>> list__string_adapter = this.list__string_adapter;
            if (list__string_adapter == null) {
              this.list__string_adapter = list__string_adapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
            }
            paymentMethods = list__string_adapter.read(jsonReader);
            break;
          }
          case "access": {
            TypeAdapter<IntersectionLaneAccess> intersectionLaneAccess_adapter = this.intersectionLaneAccess_adapter;
            if (intersectionLaneAccess_adapter == null) {
              this.intersectionLaneAccess_adapter = intersectionLaneAccess_adapter = gson.getAdapter(IntersectionLaneAccess.class);
            }
            access = intersectionLaneAccess_adapter.read(jsonReader);
            break;
          }
          default: {
            if ("valid".equals(_name)) {
              TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
              if (boolean__adapter == null) {
                this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
              }
              valid = boolean__adapter.read(jsonReader);
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
            if ("indications".equals(_name)) {
              TypeAdapter<List<String>> list__string_adapter = this.list__string_adapter;
              if (list__string_adapter == null) {
                this.list__string_adapter = list__string_adapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
              }
              indications = list__string_adapter.read(jsonReader);
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
      return new AutoValue_IntersectionLanes(
              unrecognised,
              valid,
              active,
              validIndication,
              indications,
              paymentMethods,
              access);
    }
    @Override
    public String toString() {
      return new StringBuilder().append("TypeAdapter(").append("IntersectionLanes").append(")").toString();
    }
  }