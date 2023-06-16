package com.mapbox.api.geocoding.v6.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonElement;
import com.mapbox.auto.value.gson.SerializableJsonElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class Utils {

  @Nullable
  static Map<String, JsonElement> fromSerializableProperties(
    @Nullable Map<String, SerializableJsonElement> unrecognizedProperties
  ) {
    if (unrecognizedProperties == null) {
      return null;
    }

    final Map<String, JsonElement> result = new HashMap<>(unrecognizedProperties.size());
    for (Map.Entry<String, SerializableJsonElement> entry : unrecognizedProperties.entrySet()) {
      result.put(entry.getKey(), entry.getValue().getElement());
    }
    return Collections.unmodifiableMap(result);
  }

  @Nullable
  static Map<String, SerializableJsonElement> toSerializableProperties(
    @Nullable Map<String, JsonElement> unrecognizedProperties
  ) {
    if (unrecognizedProperties == null) {
      return null;
    }

    final Map<String, SerializableJsonElement> result =
      new HashMap<>(unrecognizedProperties.size());
    for (Map.Entry<String, JsonElement> entry : unrecognizedProperties.entrySet()) {
      result.put(entry.getKey(), new SerializableJsonElement(entry.getValue()));
    }
    return Collections.unmodifiableMap(result);
  }
}
