package com.mapbox.api.geocoding.v6.models;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.Geometry;

/**
 * A type which contains a description for returned by the Geocoding V6 API object.
 */
@AutoValue
public abstract class V6Feature extends V6JsonObject {

  /**
   * Feature id.
   *
   * @return feature id
   */
  @NonNull
  @SerializedName("id")
  public abstract String id();

  /**
   * "Feature", a GeoJSON type from the
   * <a href="https://tools.ietf.org/html/rfc7946">GeoJSON specification</a>.
   *
   * @return feature type
   */
  @NonNull
  @SerializedName("type")
  public abstract String type();

  /**
   * An object describing the spatial geometry of the returned feature.
   *
   * @return spatial geometry of the feature
   */
  @NonNull
  @SerializedName("geometry")
  public abstract Geometry geometry();

  /**
   * Feature properties object which contains feature attributes.
   *
   * @return properties object
   */
  @NonNull
  @SerializedName("properties")
  public abstract V6Properties properties();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  @NonNull
  public static TypeAdapter<V6Feature> typeAdapter(Gson gson) {
    return new AutoValue_V6Feature.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  abstract static class Builder extends BaseBuilder<Builder> {

    abstract Builder id(String id);

    abstract Builder type(String type);

    abstract Builder geometry(Geometry geometry);

    abstract Builder properties(V6Properties properties);

    abstract V6Feature build();
  }
}
