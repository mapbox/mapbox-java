package com.mapbox.api.geocoding.v5.models;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * An object with the routable points for the {@link CarmenFeature}.
 */
@AutoValue
public abstract class RoutablePoints {

  /**
   * A list of routable points for the {@link CarmenFeature}, or null if no points were found.
   *
   * @return a list of routable points for the {@link CarmenFeature},
   *               or null if no points were found
   */
  @Nullable
  @SerializedName("points")
  public abstract List<RoutablePoint> points();

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
  public static TypeAdapter<RoutablePoints> typeAdapter(Gson gson) {
    return new AutoValue_RoutablePoints.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link RoutablePoints}.
   */
  @AutoValue.Builder
  @SuppressWarnings("unused")
  public abstract static class Builder {

    /**
     * A list of routable points for the {@link CarmenFeature},
     * or null if no points were found.
     *
     * @param points a list of routable points for the {@link CarmenFeature},
     *               or null if no points were found
     * @return this builder for chaining options together
     */
    public abstract Builder points(@Nullable List<RoutablePoint> points);

    /**
     * Build a new {@link RoutablePoints} object.
     *
     * @return a new {@link RoutablePoints} using the provided values in this builder
     */
    public abstract RoutablePoints build();
  }
}
