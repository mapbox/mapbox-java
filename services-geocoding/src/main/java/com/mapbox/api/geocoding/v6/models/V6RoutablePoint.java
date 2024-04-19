package com.mapbox.api.geocoding.v6.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.Point;

import java.io.Serializable;

/**
 * Object containing routable point (lat, long) and name.
 */
@AutoValue
public abstract class V6RoutablePoint implements Serializable {

  /**
   * Longitude of result.
   *
   * @return longitude of result
   */
  @NonNull
  @SerializedName("longitude")
  public abstract Double longitude();

  /**
   * Latitude of result.
   *
   * @return latitude of result
   */
  @NonNull
  @SerializedName("latitude")
  public abstract Double latitude();

  /**
   * Result coordinate as a {@code Point}.
   *
   * @return Result coordinate as a {@code Point}.
   */
  @NonNull
  public Point point() {
    return Point.fromLngLat(longitude(), latitude());
  }

  /**
   * The routable point name.
   *
   * @return the routable point name
   */
  @Nullable
  @SerializedName("name")
  public abstract String name();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<V6RoutablePoint> typeAdapter(Gson gson) {
    return new AutoValue_V6RoutablePoint.GsonTypeAdapter(gson);
  }
}
