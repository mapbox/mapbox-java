package com.mapbox.api.geocoding.v5.models;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.Point;

/**
 * Object representing {@link CarmenFeature}'s routable point.
 */
@AutoValue
public abstract class RoutablePoint {

  /**
   * A string representing the routable point name.
   *
   * @return routable point name
   */
  @Nullable
  @SerializedName("name")
  public abstract String name();

  /**
   * A {@link Point} object which represents the routable point location.
   *
   * @return a GeoJson {@link Point} which defines the routable point location
   */
  @Nullable
  public Point coordinate() {
    final double[] coordinate = rawCoordinate();
    if (coordinate != null && coordinate.length == 2) {
      return Point.fromLngLat(coordinate[0], coordinate[1]);
    }
    return null;
  }

  // No public access thus, we lessen enforcement on mutability here.
  @Nullable
  @SerializedName("coordinates")
  @SuppressWarnings("mutable")
  abstract double[] rawCoordinate();

  /**
   * Convert current instance values into another Builder to quickly change one or more values.
   *
   * @return a new instance of {@link Builder}
   */
  @SuppressWarnings("unused")
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<RoutablePoint> typeAdapter(Gson gson) {
    return new AutoValue_RoutablePoint.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link RoutablePoint}.
   */
  @AutoValue.Builder
  @SuppressWarnings("unused")
  public abstract static class Builder {

    /**
     * A string representing the routable point name.
     *
     * @param name routable point name
     * @return this builder for chaining options together
     */
    public abstract Builder name(@Nullable String name);

    /**
     * Raw coordinates (longitude and latitude, order matters)
     * that represent the routable point location.
     *
     * @param coordinate raw coordinates that represent the routable point location
     * @return this builder for chaining options together
     */
    public abstract Builder rawCoordinate(@Nullable double[] coordinate);

    /**
     * Build a new {@link RoutablePoint} object.
     *
     * @return a new {@link RoutablePoint} using the provided values in this builder
     */
    public abstract RoutablePoint build();
  }
}
