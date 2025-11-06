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

public final class StepIntersectionGsonTypeAdapter extends TypeAdapter<StepIntersection> {
  private volatile TypeAdapter<double[]> array__double_adapter;
  private volatile TypeAdapter<List<Integer>> list__integer_adapter;
  private volatile TypeAdapter<List<String>> list__string_adapter;
  private volatile TypeAdapter<List<Boolean>> list__boolean_adapter;
  private volatile TypeAdapter<Integer> integer_adapter;
  private volatile TypeAdapter<List<IntersectionLanes>> list__intersectionLanes_adapter;
  private volatile TypeAdapter<Boolean> boolean__adapter;
  private volatile TypeAdapter<RestStop> restStop_adapter;
  private volatile TypeAdapter<TollCollection> tollCollection_adapter;
  private volatile TypeAdapter<MapboxStreetsV8> mapboxStreetsV8_adapter;
  private volatile TypeAdapter<String> string_adapter;
  private volatile TypeAdapter<Interchange> interchange_adapter;
  private volatile TypeAdapter<Junction> junction_adapter;
  private volatile TypeAdapter<MergingArea> mergingArea_adapter;
  private volatile TypeAdapter<Double> double__adapter;
  private final Gson gson;
  StepIntersectionGsonTypeAdapter(Gson gson) {
    this.gson = gson;
  }
  @Override
  @SuppressWarnings("unchecked")
  public void write(JsonWriter jsonWriter, StepIntersection object) throws IOException {
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
    jsonWriter.name("location");
    if (object.rawLocation() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<double[]> array__double_adapter = this.array__double_adapter;
      if (array__double_adapter == null) {
        this.array__double_adapter = array__double_adapter = gson.getAdapter(double[].class);
      }
      array__double_adapter.write(jsonWriter, object.rawLocation());
    }
    jsonWriter.name("bearings");
    if (object.bearings() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<List<Integer>> list__integer_adapter = this.list__integer_adapter;
      if (list__integer_adapter == null) {
        this.list__integer_adapter = list__integer_adapter = (TypeAdapter<List<Integer>>) gson.getAdapter(TypeToken.getParameterized(List.class, Integer.class));
      }
      list__integer_adapter.write(jsonWriter, object.bearings());
    }
    jsonWriter.name("classes");
    if (object.classes() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<List<String>> list__string_adapter = this.list__string_adapter;
      if (list__string_adapter == null) {
        this.list__string_adapter = list__string_adapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
      }
      list__string_adapter.write(jsonWriter, object.classes());
    }
    jsonWriter.name("entry");
    if (object.entry() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<List<Boolean>> list__boolean_adapter = this.list__boolean_adapter;
      if (list__boolean_adapter == null) {
        this.list__boolean_adapter = list__boolean_adapter = (TypeAdapter<List<Boolean>>) gson.getAdapter(TypeToken.getParameterized(List.class, Boolean.class));
      }
      list__boolean_adapter.write(jsonWriter, object.entry());
    }
    jsonWriter.name("form_of_way");
    if (object.formOfWay() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<List<String>> list__string_adapter = this.list__string_adapter;
      if (list__string_adapter == null) {
        this.list__string_adapter = list__string_adapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
      }
      list__string_adapter.write(jsonWriter, object.formOfWay());
    }
    jsonWriter.name("geometries");
    if (object.geometries() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<List<String>> list__string_adapter = this.list__string_adapter;
      if (list__string_adapter == null) {
        this.list__string_adapter = list__string_adapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
      }
      list__string_adapter.write(jsonWriter, object.geometries());
    }
    jsonWriter.name("access");
    if (object.access() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<List<Integer>> list__integer_adapter = this.list__integer_adapter;
      if (list__integer_adapter == null) {
        this.list__integer_adapter = list__integer_adapter = (TypeAdapter<List<Integer>>) gson.getAdapter(TypeToken.getParameterized(List.class, Integer.class));
      }
      list__integer_adapter.write(jsonWriter, object.access());
    }
    jsonWriter.name("elevated");
    if (object.elevated() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<List<Boolean>> list__boolean_adapter = this.list__boolean_adapter;
      if (list__boolean_adapter == null) {
        this.list__boolean_adapter = list__boolean_adapter = (TypeAdapter<List<Boolean>>) gson.getAdapter(TypeToken.getParameterized(List.class, Boolean.class));
      }
      list__boolean_adapter.write(jsonWriter, object.elevated());
    }
    jsonWriter.name("bridges");
    if (object.bridges() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<List<Boolean>> list__boolean_adapter = this.list__boolean_adapter;
      if (list__boolean_adapter == null) {
        this.list__boolean_adapter = list__boolean_adapter = (TypeAdapter<List<Boolean>>) gson.getAdapter(TypeToken.getParameterized(List.class, Boolean.class));
      }
      list__boolean_adapter.write(jsonWriter, object.bridges());
    }
    jsonWriter.name("in");
    if (object.in() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<Integer> integer_adapter = this.integer_adapter;
      if (integer_adapter == null) {
        this.integer_adapter = integer_adapter = gson.getAdapter(Integer.class);
      }
      integer_adapter.write(jsonWriter, object.in());
    }
    jsonWriter.name("out");
    if (object.out() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<Integer> integer_adapter = this.integer_adapter;
      if (integer_adapter == null) {
        this.integer_adapter = integer_adapter = gson.getAdapter(Integer.class);
      }
      integer_adapter.write(jsonWriter, object.out());
    }
    jsonWriter.name("lanes");
    if (object.lanes() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<List<IntersectionLanes>> list__intersectionLanes_adapter = this.list__intersectionLanes_adapter;
      if (list__intersectionLanes_adapter == null) {
        this.list__intersectionLanes_adapter = list__intersectionLanes_adapter = (TypeAdapter<List<IntersectionLanes>>) gson.getAdapter(TypeToken.getParameterized(List.class, IntersectionLanes.class));
      }
      list__intersectionLanes_adapter.write(jsonWriter, object.lanes());
    }
    jsonWriter.name("geometry_index");
    if (object.geometryIndex() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<Integer> integer_adapter = this.integer_adapter;
      if (integer_adapter == null) {
        this.integer_adapter = integer_adapter = gson.getAdapter(Integer.class);
      }
      integer_adapter.write(jsonWriter, object.geometryIndex());
    }
    jsonWriter.name("is_urban");
    if (object.isUrban() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
      if (boolean__adapter == null) {
        this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
      }
      boolean__adapter.write(jsonWriter, object.isUrban());
    }
    jsonWriter.name("admin_index");
    if (object.adminIndex() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<Integer> integer_adapter = this.integer_adapter;
      if (integer_adapter == null) {
        this.integer_adapter = integer_adapter = gson.getAdapter(Integer.class);
      }
      integer_adapter.write(jsonWriter, object.adminIndex());
    }
    jsonWriter.name("rest_stop");
    if (object.restStop() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<RestStop> restStop_adapter = this.restStop_adapter;
      if (restStop_adapter == null) {
        this.restStop_adapter = restStop_adapter = gson.getAdapter(RestStop.class);
      }
      restStop_adapter.write(jsonWriter, object.restStop());
    }
    jsonWriter.name("toll_collection");
    if (object.tollCollection() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<TollCollection> tollCollection_adapter = this.tollCollection_adapter;
      if (tollCollection_adapter == null) {
        this.tollCollection_adapter = tollCollection_adapter = gson.getAdapter(TollCollection.class);
      }
      tollCollection_adapter.write(jsonWriter, object.tollCollection());
    }
    jsonWriter.name("mapbox_streets_v8");
    if (object.mapboxStreetsV8() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<MapboxStreetsV8> mapboxStreetsV8_adapter = this.mapboxStreetsV8_adapter;
      if (mapboxStreetsV8_adapter == null) {
        this.mapboxStreetsV8_adapter = mapboxStreetsV8_adapter = gson.getAdapter(MapboxStreetsV8.class);
      }
      mapboxStreetsV8_adapter.write(jsonWriter, object.mapboxStreetsV8());
    }
    jsonWriter.name("tunnel_name");
    if (object.tunnelName() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<String> string_adapter = this.string_adapter;
      if (string_adapter == null) {
        this.string_adapter = string_adapter = gson.getAdapter(String.class);
      }
      string_adapter.write(jsonWriter, object.tunnelName());
    }
    jsonWriter.name("railway_crossing");
    if (object.railwayCrossing() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
      if (boolean__adapter == null) {
        this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
      }
      boolean__adapter.write(jsonWriter, object.railwayCrossing());
    }
    jsonWriter.name("traffic_signal");
    if (object.trafficSignal() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
      if (boolean__adapter == null) {
        this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
      }
      boolean__adapter.write(jsonWriter, object.trafficSignal());
    }
    jsonWriter.name("stop_sign");
    if (object.stopSign() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
      if (boolean__adapter == null) {
        this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
      }
      boolean__adapter.write(jsonWriter, object.stopSign());
    }
    jsonWriter.name("yield_sign");
    if (object.yieldSign() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
      if (boolean__adapter == null) {
        this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
      }
      boolean__adapter.write(jsonWriter, object.yieldSign());
    }
    jsonWriter.name("ic");
    if (object.interchange() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<Interchange> interchange_adapter = this.interchange_adapter;
      if (interchange_adapter == null) {
        this.interchange_adapter = interchange_adapter = gson.getAdapter(Interchange.class);
      }
      interchange_adapter.write(jsonWriter, object.interchange());
    }
    jsonWriter.name("jct");
    if (object.junction() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<Junction> junction_adapter = this.junction_adapter;
      if (junction_adapter == null) {
        this.junction_adapter = junction_adapter = gson.getAdapter(Junction.class);
      }
      junction_adapter.write(jsonWriter, object.junction());
    }
    jsonWriter.name("merging_area");
    if (object.mergingArea() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<MergingArea> mergingArea_adapter = this.mergingArea_adapter;
      if (mergingArea_adapter == null) {
        this.mergingArea_adapter = mergingArea_adapter = gson.getAdapter(MergingArea.class);
      }
      mergingArea_adapter.write(jsonWriter, object.mergingArea());
    }
    jsonWriter.name("duration");
    if (object.duration() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<Double> double__adapter = this.double__adapter;
      if (double__adapter == null) {
        this.double__adapter = double__adapter = gson.getAdapter(Double.class);
      }
      double__adapter.write(jsonWriter, object.duration());
    }
    jsonWriter.endObject();
  }
  @Override
  @SuppressWarnings("unchecked")
  public StepIntersection read(JsonReader jsonReader) throws IOException {
    if (jsonReader.peek() == JsonToken.NULL) {
      jsonReader.nextNull();
      return null;
    }
    jsonReader.beginObject();
//    StepIntersection.Builder builder = StepIntersection.builder();
    LinkedHashMap<String, SerializableJsonElement> unrecognised = null;

    double[] rawLocation = null;
 List<Integer> bearings = null;
 List<String> classes = null;
List<Boolean> entry = null;
 List<String> formOfWay = null;
List<String> geometries = null;
 List<Integer> access = null;
List<Boolean> elevated = null;
 List<Boolean> bridges = null;
 Integer in = null;
 Integer out = null;
 List<IntersectionLanes> lanes = null;
Integer geometryIndex = null;
 Boolean isUrban = null;
 Integer adminIndex = null;
RestStop restStop = null;
 TollCollection tollCollection = null;
MapboxStreetsV8 mapboxStreetsV8 = null;
 String tunnelName = null;
Boolean railwayCrossing = null;
 Boolean trafficSignal = null;
Boolean stopSign = null;
 Boolean yieldSign = null;
 Interchange interchange = null;
Junction junction = null;
 MergingArea mergingArea = null;
 Double duration = null;

    while (jsonReader.hasNext()) {
      String _name = jsonReader.nextName();
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        continue;
      }
      switch (_name) {
        case "location": {
          TypeAdapter<double[]> array__double_adapter = this.array__double_adapter;
          if (array__double_adapter == null) {
            this.array__double_adapter = array__double_adapter = gson.getAdapter(double[].class);
          }
          rawLocation = array__double_adapter.read(jsonReader);
          break;
        }
        case "form_of_way": {
          TypeAdapter<List<String>> list__string_adapter = this.list__string_adapter;
          if (list__string_adapter == null) {
            this.list__string_adapter = list__string_adapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
          }
          formOfWay = list__string_adapter.read(jsonReader);
          break;
        }
        case "geometries": {
          TypeAdapter<List<String>> list__string_adapter = this.list__string_adapter;
          if (list__string_adapter == null) {
            this.list__string_adapter = list__string_adapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
          }
          geometries = list__string_adapter.read(jsonReader);
          break;
        }
        case "access": {
          TypeAdapter<List<Integer>> list__integer_adapter = this.list__integer_adapter;
          if (list__integer_adapter == null) {
            this.list__integer_adapter = list__integer_adapter = (TypeAdapter<List<Integer>>) gson.getAdapter(TypeToken.getParameterized(List.class, Integer.class));
          }
          access = list__integer_adapter.read(jsonReader);
          break;
        }
        case "elevated": {
          TypeAdapter<List<Boolean>> list__boolean_adapter = this.list__boolean_adapter;
          if (list__boolean_adapter == null) {
            this.list__boolean_adapter = list__boolean_adapter = (TypeAdapter<List<Boolean>>) gson.getAdapter(TypeToken.getParameterized(List.class, Boolean.class));
          }
          elevated = list__boolean_adapter.read(jsonReader);
          break;
        }
        case "bridges": {
          TypeAdapter<List<Boolean>> list__boolean_adapter = this.list__boolean_adapter;
          if (list__boolean_adapter == null) {
            this.list__boolean_adapter = list__boolean_adapter = (TypeAdapter<List<Boolean>>) gson.getAdapter(TypeToken.getParameterized(List.class, Boolean.class));
          }
          bridges = list__boolean_adapter.read(jsonReader);
          break;
        }
        case "geometry_index": {
          TypeAdapter<Integer> integer_adapter = this.integer_adapter;
          if (integer_adapter == null) {
            this.integer_adapter = integer_adapter = gson.getAdapter(Integer.class);
          }
          geometryIndex = integer_adapter.read(jsonReader);
          break;
        }
        case "is_urban": {
          TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
          if (boolean__adapter == null) {
            this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
          }
          isUrban = boolean__adapter.read(jsonReader);
          break;
        }
        case "admin_index": {
          TypeAdapter<Integer> integer_adapter = this.integer_adapter;
          if (integer_adapter == null) {
            this.integer_adapter = integer_adapter = gson.getAdapter(Integer.class);
          }
          adminIndex = integer_adapter.read(jsonReader);
          break;
        }
        case "rest_stop": {
          TypeAdapter<RestStop> restStop_adapter = this.restStop_adapter;
          if (restStop_adapter == null) {
            this.restStop_adapter = restStop_adapter = gson.getAdapter(RestStop.class);
          }
          restStop = restStop_adapter.read(jsonReader);
          break;
        }
        case "toll_collection": {
          TypeAdapter<TollCollection> tollCollection_adapter = this.tollCollection_adapter;
          if (tollCollection_adapter == null) {
            this.tollCollection_adapter = tollCollection_adapter = gson.getAdapter(TollCollection.class);
          }
          tollCollection = tollCollection_adapter.read(jsonReader);
          break;
        }
        case "mapbox_streets_v8": {
          TypeAdapter<MapboxStreetsV8> mapboxStreetsV8_adapter = this.mapboxStreetsV8_adapter;
          if (mapboxStreetsV8_adapter == null) {
            this.mapboxStreetsV8_adapter = mapboxStreetsV8_adapter = gson.getAdapter(MapboxStreetsV8.class);
          }
          mapboxStreetsV8 = mapboxStreetsV8_adapter.read(jsonReader);
          break;
        }
        case "tunnel_name": {
          TypeAdapter<String> string_adapter = this.string_adapter;
          if (string_adapter == null) {
            this.string_adapter = string_adapter = gson.getAdapter(String.class);
          }
          tunnelName = string_adapter.read(jsonReader);
          break;
        }
        case "railway_crossing": {
          TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
          if (boolean__adapter == null) {
            this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
          }
          railwayCrossing = boolean__adapter.read(jsonReader);
          break;
        }
        case "traffic_signal": {
          TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
          if (boolean__adapter == null) {
            this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
          }
          trafficSignal = boolean__adapter.read(jsonReader);
          break;
        }
        case "stop_sign": {
          TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
          if (boolean__adapter == null) {
            this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
          }
          stopSign = boolean__adapter.read(jsonReader);
          break;
        }
        case "yield_sign": {
          TypeAdapter<Boolean> boolean__adapter = this.boolean__adapter;
          if (boolean__adapter == null) {
            this.boolean__adapter = boolean__adapter = gson.getAdapter(Boolean.class);
          }
          yieldSign = boolean__adapter.read(jsonReader);
          break;
        }
        case "ic": {
          TypeAdapter<Interchange> interchange_adapter = this.interchange_adapter;
          if (interchange_adapter == null) {
            this.interchange_adapter = interchange_adapter = gson.getAdapter(Interchange.class);
          }
          interchange = interchange_adapter.read(jsonReader);
          break;
        }
        case "jct": {
          TypeAdapter<Junction> junction_adapter = this.junction_adapter;
          if (junction_adapter == null) {
            this.junction_adapter = junction_adapter = gson.getAdapter(Junction.class);
          }
          junction = junction_adapter.read(jsonReader);
          break;
        }
        case "merging_area": {
          TypeAdapter<MergingArea> mergingArea_adapter = this.mergingArea_adapter;
          if (mergingArea_adapter == null) {
            this.mergingArea_adapter = mergingArea_adapter = gson.getAdapter(MergingArea.class);
          }
          mergingArea = mergingArea_adapter.read(jsonReader);
          break;
        }
        case "duration": {
          TypeAdapter<Double> double__adapter = this.double__adapter;
          if (double__adapter == null) {
            this.double__adapter = double__adapter = gson.getAdapter(Double.class);
          }
          duration = double__adapter.read(jsonReader);
          break;
        }
        default: {
          if ("bearings".equals(_name)) {
            TypeAdapter<List<Integer>> list__integer_adapter = this.list__integer_adapter;
            if (list__integer_adapter == null) {
              this.list__integer_adapter = list__integer_adapter = (TypeAdapter<List<Integer>>) gson.getAdapter(TypeToken.getParameterized(List.class, Integer.class));
            }
            bearings = list__integer_adapter.read(jsonReader);
            continue;
          }
          if ("classes".equals(_name)) {
            TypeAdapter<List<String>> list__string_adapter = this.list__string_adapter;
            if (list__string_adapter == null) {
              this.list__string_adapter = list__string_adapter = (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class, String.class));
            }
            classes = list__string_adapter.read(jsonReader);
            continue;
          }
          if ("entry".equals(_name)) {
            TypeAdapter<List<Boolean>> list__boolean_adapter = this.list__boolean_adapter;
            if (list__boolean_adapter == null) {
              this.list__boolean_adapter = list__boolean_adapter = (TypeAdapter<List<Boolean>>) gson.getAdapter(TypeToken.getParameterized(List.class, Boolean.class));
            }
            entry = list__boolean_adapter.read(jsonReader);
            continue;
          }
          if ("in".equals(_name)) {
            TypeAdapter<Integer> integer_adapter = this.integer_adapter;
            if (integer_adapter == null) {
              this.integer_adapter = integer_adapter = gson.getAdapter(Integer.class);
            }
            in = integer_adapter.read(jsonReader);
            continue;
          }
          if ("out".equals(_name)) {
            TypeAdapter<Integer> integer_adapter = this.integer_adapter;
            if (integer_adapter == null) {
              this.integer_adapter = integer_adapter = gson.getAdapter(Integer.class);
            }
            out = integer_adapter.read(jsonReader);
            continue;
          }
          if ("lanes".equals(_name)) {
            TypeAdapter<List<IntersectionLanes>> list__intersectionLanes_adapter = this.list__intersectionLanes_adapter;
            if (list__intersectionLanes_adapter == null) {
              this.list__intersectionLanes_adapter = list__intersectionLanes_adapter = (TypeAdapter<List<IntersectionLanes>>) gson.getAdapter(TypeToken.getParameterized(List.class, IntersectionLanes.class));
            }
            lanes = list__intersectionLanes_adapter.read(jsonReader);
            continue;
          }
          if (unrecognised == null) {
            unrecognised = new LinkedHashMap<String, SerializableJsonElement>();
//            unrecognized = unrecognised;
          }
          JsonElement element = gson.fromJson(jsonReader, JsonElement.class);
          unrecognised.put(_name, new SerializableJsonElement(element));
          continue;
        }
      }
    }
    jsonReader.endObject();
    String missing = "";
    if (rawLocation == null) {
      missing += " rawLocation";
    }
    if (!missing.isEmpty()) {
      throw new IllegalStateException("Missing required properties:" + missing);
    }
    return new AutoValue_StepIntersection(
            unrecognised,
            rawLocation,
            bearings,
            classes,
            entry,
            formOfWay,
            geometries,
            access,
            elevated,
            bridges,
            in,
            out,
            lanes,
            geometryIndex,
            isUrban,
            adminIndex,
            restStop,
            tollCollection,
            mapboxStreetsV8,
            tunnelName,
            railwayCrossing,
            trafficSignal,
            stopSign,
            yieldSign,
            interchange,
            junction,
            mergingArea,
            duration);
//    return builder.build();
  }
  @Override
  public String toString() {
    return new StringBuilder().append("TypeAdapter(").append("StepIntersection").append(")").toString();
  }
}