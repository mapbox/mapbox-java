package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.GsonBuilder;
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

  @Nullable
  @UnrecognizedJsonProperties
  abstract Map<String, SerializableJsonElement> unrecognized();

  abstract static class Builder<T extends Builder> {
    @NonNull
    abstract T unrecognized(Map<String, SerializableJsonElement> value);
  }
}
