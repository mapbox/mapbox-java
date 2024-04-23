package com.mapbox.api.geocoding.v6.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonElement;
import com.mapbox.auto.value.gson.SerializableJsonElement;
import com.mapbox.auto.value.gson.UnrecognizedJsonProperties;

import java.io.Serializable;
import java.util.Map;

/**
 * Base class for V6 types.
 */
public abstract class V6JsonObject implements Serializable {

  /**
   * Use this method to get JSON properties that weren't recognized during JSON
   * serialization by the model. This may be useful to access experimental API properties.
   * When an experimental API property becomes stable,
   * it will eventually have static field in a model introduced
   * and it won't be available via this dynamic method anymore.
   *
   * @return unrecognized JSON properties
   */
  @Nullable
  @SuppressWarnings("unused")
  public final Map<String, JsonElement> getUnrecognizedJsonProperties() {
    return Utils.fromSerializableProperties(unrecognized());
  }

  @Nullable
  @UnrecognizedJsonProperties
  abstract Map<String, SerializableJsonElement> unrecognized();

  abstract static class BaseBuilder<T> {

    abstract T unrecognized(@Nullable Map<String, SerializableJsonElement> value);

    @SuppressWarnings("unused")
    public T unrecognizedJsonProperties(@Nullable Map<String, JsonElement> unrecognizedProperties) {
      return unrecognized(Utils.toSerializableProperties(unrecognizedProperties));
    }
  }
}
