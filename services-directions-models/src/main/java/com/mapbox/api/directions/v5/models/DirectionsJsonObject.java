package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.PointAsCoordinatesTypeAdapter;
import com.mapbox.auto.value.gson.SerializableJsonElement;
import com.mapbox.auto.value.gson.UnrecognizedJsonProperties;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Provides a base class for Directions model classes.
 *
 * @since 3.4.0
 */
public abstract class DirectionsJsonObject implements Serializable {

  /**
   * This takes the currently defined values found inside this instance and converts it to a json
   * string.
   *
   * @return a JSON string which represents this DirectionsJsonObject
   * @since 3.4.0
   */
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointAsCoordinatesTypeAdapter());
    return gson.create().toJson(this);
  }

  /**
   * Use this method to access a JSON property that wasn't recognized during JSON serialization.
   * This may be useful to access experimental properties.
   * @param propertyName name of a json property
   * @return value of a requested property or null if the requested property doesn't exist.
   */
  @Nullable
  public final JsonElement getUnrecognizedProperty(String propertyName) {
    JsonElement result = null;
    Map<String, SerializableJsonElement> unrecognizedProperties = unrecognized();
    if (unrecognizedProperties != null) {
      SerializableJsonElement property = unrecognizedProperties.get(propertyName);
      if (property != null) {
        result = property.getElement();
      }
    }
    return result;
  }

  /**
   * Use this method to get names of unrecognized JSON properties.
   * @return names of unrecognized JSON properties or an empty set
   */
  @NonNull
  public final Set<String> getUnrecognizedPropertiesNames() {
    Set<String> result;
    Map<String, SerializableJsonElement> unrecognizedProperties = unrecognized();
    if (unrecognizedProperties != null) {
      result = unrecognizedProperties.keySet();
    } else {
      result = Collections.emptySet();
    }
    return result;
  }

  @Nullable
  @UnrecognizedJsonProperties
  abstract Map<String, SerializableJsonElement> unrecognized();

  abstract static class Builder<T extends Builder> {
    @NonNull
    abstract T unrecognized(Map<String, SerializableJsonElement> value);
  }
}
