package com.mapbox.services.api.directions.v5.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Detailed information about an individual route such as the duration, distance and geometry.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class DirectionsRoute {

  public static DirectionsRoute create(double distance, String weightName, double weight, double duration,
                                       String geometry, List<RouteLeg> getLegs) {
    return new AutoValue_DirectionsRoute(distance, weightName, weight, duration, geometry, getLegs);
  }

  public static TypeAdapter<DirectionsRoute> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsRoute.GsonTypeAdapter(gson);
  }

  /**
   * The distance traveled from origin to destination.
   *
   * @return a double number with unit meters.
   * @since 1.0.0
   */
  public abstract double getDistance();

  /**
   * The name of the weight profile used while calculating during extraction phase. In many cases, this will return
   * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#WEIGHT_NAME_ROUTABILITY},
   * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#WEIGHT_NAME_DURATION}, or
   * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#WEIGHT_NAME_DISTANCE}.
   *
   * @return a String representing the weight profile used while calculating the route.
   * @since 2.1.0
   */
  @SerializedName("weight_name")
  public abstract String getWeightName();

  /**
   * The calculated weight of the route.
   *
   * @return the weight value provided from the API as a {@code double} value.
   * @since 2.1.0
   */
  public abstract double getWeight();

  /**
   * The estimated travel time from origin to destination.
   *
   * @return a double number with unit seconds.
   * @since 1.0.0
   */
  public abstract double getDuration();

  /**
   * Gives the geometry of the route. Commonly used to draw the route on the map view.
   *
   * @return An encoded polyline string.
   * @since 1.0.0
   */
  public abstract String getGeometry();

  /**
   * A Leg is a route between only two waypoints
   *
   * @return List of {@link RouteLeg} objects.
   * @since 1.0.0
   */
  public abstract List<RouteLeg> getLegs();
}
