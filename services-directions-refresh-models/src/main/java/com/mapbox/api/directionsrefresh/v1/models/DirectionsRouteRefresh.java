package com.mapbox.api.directionsrefresh.v1.models;

import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.models.DirectionsWaypoint;
import com.mapbox.api.directionsrefresh.v1.DirectionsRefreshAdapterFactory;
import java.util.List;

/**
 * Detailed information about an individual route such as the duration, distance and geometry.
 */
@AutoValue
public abstract class DirectionsRouteRefresh extends DirectionsRefreshJsonObject {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_DirectionsRouteRefresh.Builder();
  }

  /**
   * A Leg Refresh is an object contain refresh data between only two {@link DirectionsWaypoint}.
   *
   * @return list of {@link RouteLegRefresh} objects
   */
  @Nullable
  public abstract List<RouteLegRefresh> legs();

  /**
   * Convert the current {@link DirectionsRouteRefresh} to its builder holding the currently
   * assigned values. This allows you to modify a single property and then rebuild the object
   * resulting in an updated and modified {@link DirectionsRouteRefresh}.
   *
   * @return a {@link DirectionsRouteRefresh.Builder} with the same values set to match the ones
   *   defined in this {@link DirectionsRouteRefresh}
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<DirectionsRouteRefresh> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsRouteRefresh.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a GeoJson Directions Route
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  public static DirectionsRouteRefresh fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsRefreshAdapterFactory.create());
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, DirectionsRouteRefresh.class);
  }

  /**
   * This builder can be used to set the values describing the {@link DirectionsRouteRefresh}.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * A Leg Refresh is an object contain refresh data between only two {@link DirectionsWaypoint}.
     *
     * @param legs list of {@link RouteLegRefresh} objects
     * @return this builder for chaining options together
     */
    public abstract Builder legs(@Nullable List<RouteLegRefresh> legs);

    /**
     * Build a new {@link DirectionsRouteRefresh} object.
     *
     * @return a new {@link DirectionsRouteRefresh} using the provided values in this builder
     */
    public abstract DirectionsRouteRefresh build();
  }
}
