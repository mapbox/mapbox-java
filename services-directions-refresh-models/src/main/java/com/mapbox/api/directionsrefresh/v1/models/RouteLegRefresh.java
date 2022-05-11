package com.mapbox.api.directionsrefresh.v1.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.models.DirectionsWaypoint;
import com.mapbox.api.directions.v5.models.Incident;
import com.mapbox.api.directions.v5.models.LegAnnotation;
import com.mapbox.api.directionsrefresh.v1.DirectionsRefreshAdapterFactory;

import java.util.List;

/**
 * A route refresh data between only two {@link DirectionsWaypoint}.
 */
@AutoValue
public abstract class RouteLegRefresh extends DirectionsRefreshJsonObject {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_RouteLegRefresh.Builder();
  }

  /**
   * A list of incidents that occur on this leg.
   *
   * @return a list of {@link Incident}
   */
  @Nullable
  public abstract List<Incident> incidents();

  /**
   * A {@link LegAnnotation} that contains additional details about each line segment along the
   * route geometry. If you'd like to receiving this, you must request it inside your Directions
   * request before executing the call.
   *
   * @return a {@link LegAnnotation} object
   */
  @Nullable
  public abstract LegAnnotation annotation();

  /**
   * Convert the current {@link RouteLegRefresh} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link RouteLegRefresh}.
   *
   * @return a {@link RouteLegRefresh.Builder} with the same values set to match the ones defined
   *   in this {@link RouteLegRefresh}
   */

  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<RouteLegRefresh> typeAdapter(Gson gson) {
    return new AutoValue_RouteLegRefresh.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a RouteLeg
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  public static RouteLegRefresh fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    gson.registerTypeAdapterFactory(DirectionsRefreshAdapterFactory.create());
    return gson.create().fromJson(json, RouteLegRefresh.class);
  }

  /**
   * This builder can be used to set the values describing the {@link RouteLegRefresh}.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * A list of incidents that occur on this leg.
     *
     * @param incidents a list of {@link Incident}
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder incidents(@Nullable List<Incident> incidents);

    /**
     * A {@link LegAnnotation} that contains additional details about each line segment along the
     * route geometry. If you'd like to receiving this, you must request it inside your Directions
     * request before executing the call.
     *
     * @param annotation a {@link LegAnnotation} object
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder annotation(@Nullable LegAnnotation annotation);

    /**
     * Build a new {@link RouteLegRefresh} object.
     *
     * @return a new {@link RouteLegRefresh} using the provided values in this builder
     */
    public abstract RouteLegRefresh build();
  }
}
