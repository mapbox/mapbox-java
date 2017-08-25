package com.mapbox.services.api.directions.v5.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Detailed information about an individual route such as the duration, distance and geometry.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class DirectionsRoute implements Serializable {

  public static Builder builder() {
    return new AutoValue_DirectionsRoute.Builder();
  }

  /**
   * The distance traveled from origin to destination.
   *
   * @return a double number with unit meters
   * @since 1.0.0
   */
  public abstract double distance();

  /**
   * The estimated travel time from origin to destination.
   *
   * @return a double number with unit seconds
   * @since 1.0.0
   */
  public abstract double duration();

  /**
   * Gives the geometry of the route. Commonly used to draw the route on the map view.
   *
   * @return an encoded polyline string
   * @since 1.0.0
   */
  public abstract String geometry();

  /**
   * The calculated weight of the route.
   *
   * @return the weight value provided from the API as a {@code double} value
   * @since 2.1.0
   */
  public abstract double weight();

  /**
   * The name of the weight profile used while calculating during extraction phase. The default is
   * {@code routability} which is duration based, with additional penalties for less desirable
   * maneuvers.
   *
   * @return a String representing the weight profile used while calculating the route
   * @since 2.1.0
   */
  @SerializedName("weight_name")
  public abstract String weightName();

  /**
   * A Leg is a route between only two waypoints
   *
   * @return list of {@link RouteLeg} objects
   * @since 1.0.0
   */
  public abstract List<RouteLeg> legs();

  public static TypeAdapter<DirectionsRoute> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsRoute.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder distance(double distance);

    public abstract Builder duration(double duration);

    public abstract Builder geometry(String geometry);

    public abstract Builder weight(double weight);

    public abstract Builder weightName(String weightName);

    public abstract Builder legs(List<RouteLeg> legs);

    public abstract DirectionsRoute build();
  }
}
