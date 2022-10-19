package com.mapbox.api.directionsrefresh.v1.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.utils.UnrecognizedPropertiesUtils;
import com.mapbox.api.directionsrefresh.v1.DirectionsRefreshAdapterFactory;
import com.mapbox.auto.value.gson.SerializableJsonElement;
import com.mapbox.auto.value.gson.UnrecognizedJsonProperties;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.PointAsCoordinatesTypeAdapter;
import java.io.Serializable;
import java.util.Map;

/**
 * Provides a base class for Directions model classes.
 */
public abstract class DirectionsRefreshJsonObject implements Serializable {

  /**
   * This takes the currently defined values found inside this instance and converts it to a json
   * string.
   *
   * @return a JSON string which represents this DirectionsJsonObject
   */
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointAsCoordinatesTypeAdapter());
    gson.registerTypeAdapterFactory(DirectionsRefreshAdapterFactory.create());
    return gson.create().toJson(this);
  }

  /**
   * Use this method to get JSON properties that weren't recognized during JSON
   * serialization by the model. This may be useful to access experimental API properties.
   * When an experimental API property becomes stable,
   * it will eventually have static field in a model introduced
   * and it won't be available via this dynamic method anymore.
   *
   * See
   * <a href="https://docs.mapbox.com/api/navigation/directions/">Directions API documentation</a>
   * for available experimental fields.
   *
   * @return unrecognized JSON properties
   */
  @Nullable
  public final Map<String, JsonElement> getUnrecognizedJsonProperties() {
    return UnrecognizedPropertiesUtils.fromSerializableProperties(unrecognized());
  }

  @Nullable
  @UnrecognizedJsonProperties
  abstract Map<String, SerializableJsonElement> unrecognized();

  abstract static class Builder<T extends Builder> {
    @NonNull
    abstract T unrecognized(@Nullable Map<String, SerializableJsonElement> value);

    /**
     * Use this method to add parameters which are not present in the model yet but are supported
     * on the Directions API side in the response.
     * Use it for experimental parameters.
     *
     * @param unrecognizedProperties parameters to add to request
     */
    @NonNull
    public T unrecognizedJsonProperties(@Nullable Map<String, JsonElement> unrecognizedProperties) {
      return unrecognized(
        UnrecognizedPropertiesUtils.toSerializableProperties(unrecognizedProperties)
      );
    }
  }
}
