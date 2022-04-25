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
import java.util.Map;

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
   * Access JSON property that wasn't recognised during JSON serialization.
   * This may be useful to access experimental properties.
   * @param propertyName name of a json property
   * @return value of a requested property or null if the requested property doesn't exist.
   */
  @Nullable
  public JsonElement getUnrecognisedProperty(String propertyName) {
    JsonElement result = null;
    Map<String, SerializableJsonElement> recognisedProperties = unrecognised();
    if (recognisedProperties != null) {
      SerializableJsonElement property = recognisedProperties.get(propertyName);
      if (property != null) {
        result = property.getElement();
      }
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
