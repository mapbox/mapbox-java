package com.mapbox.api.directions.v5.utils;

import androidx.annotation.Nullable;
import com.google.gson.JsonElement;
import com.mapbox.auto.value.gson.SerializableJsonElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides utility methods to work with unrecognized properties.
 */
public final class UnrecognizedPropertiesUtils {

  /**
   * Converts unrecognized properties in form of Map&#60;String, SerializableJsonElement&#60;
   *   to unrecognized properties in form of Map&#60;String, JsonElement&#60;.
   * @param unrecognizedProperties original map
   * @return converted map
   */
  @Nullable
  public static Map<String, JsonElement> fromSerializableProperties(
    @Nullable Map<String, SerializableJsonElement> unrecognizedProperties
  ) {
    if (unrecognizedProperties != null) {
      Map<String, JsonElement> result = new HashMap<>();
      for (String key : unrecognizedProperties.keySet()) {
        result.put(key, unrecognizedProperties.get(key).getElement());
      }
      return result;
    }
    return null;
  }

  /**
   * Converts unrecognized properties in form of Map&#60;String, JsonElement&#60;.
   *   to unrecognized properties in form of Map&#60;String, SerializableJsonElement&#60;.
   * @param unrecognizedProperties original map
   * @return converted map
   */
  @Nullable
  public static Map<String, SerializableJsonElement> toSerializableProperties(
    @Nullable Map<String, JsonElement> unrecognizedProperties
  ) {
    if (unrecognizedProperties != null) {
      Map<String, SerializableJsonElement> result = new HashMap<>();
      for (Map.Entry<String, JsonElement> entry : unrecognizedProperties.entrySet()) {
        result.put(entry.getKey(), new SerializableJsonElement(entry.getValue()));
      }
      return result;
    }
    return null;
  }
}
