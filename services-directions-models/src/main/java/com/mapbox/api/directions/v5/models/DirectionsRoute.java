package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.PointAsCoordinatesTypeAdapter;

import java.util.List;

/**
 * Detailed information about an individual route such as the duration, distance and geometry.
 *
 * @since 1.0.0
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
public abstract class DirectionsRoute extends DirectionsJsonObject {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_DirectionsRoute.Builder();
  }

  /**
   * The index of this route in the original network response.
   *
   * @return string of an int value representing the index
   * @since 4.4.0
   */
  @Nullable
  public abstract String routeIndex();

  /**
   * The distance traveled from origin to destination.
   *
   * @return a double number with unit meters
   * @since 1.0.0
   */
  @NonNull
  public abstract Double distance();

  /**
   * The estimated travel time from origin to destination.
   *
   * @return a double number with unit seconds
   * @since 1.0.0
   */
  @NonNull
  public abstract Double duration();

  /**
   * The typical travel time from this route's origin to destination. There's a delay along
   * this route if you subtract this durationTypical() value from the route's duration()
   * value and the resulting difference is greater than 0. The delay is because of any
   * number of real-world situations (road repair, traffic jam, etc).
   *
   * @return a double number with unit seconds
   * @since 5.5.0
   */
  @Nullable
  @SerializedName("duration_typical")
  public abstract Double durationTypical();

  /**
   * Gives the geometry of the route. Commonly used to draw the route on the map view.
   *
   * @return an encoded polyline string
   * @since 1.0.0
   */
  @Nullable
  public abstract String geometry();

  /**
   * The calculated weight of the route.
   *
   * @return the weight value provided from the API as a {@code double} value
   * @since 2.1.0
   */
  @Nullable
  public abstract Double weight();

  /**
   * When using the driving-traffic profile, this will be returned as a double value
   * indicating the weight of the selected route under typical conditions
   * (not taking into account live traffic).
   */
  @Nullable
  @SerializedName("weight_typical")
  public abstract Double weightTypical();

  /**
   * The name of the weight profile used while calculating during extraction phase. The default is
   * {@code routability} which is duration based, with additional penalties for less desirable
   * maneuvers.
   *
   * @return a String representing the weight profile used while calculating the route
   * @since 2.1.0
   */
  @Nullable
  @SerializedName("weight_name")
  public abstract String weightName();

  /**
   * A Leg is a route between only two waypoints.
   *
   * @return list of {@link RouteLeg} objects
   * @since 1.0.0
   */
  @Nullable
  public abstract List<RouteLeg> legs();

  /**
   * List of {@link DirectionsWaypoint} objects. Each {@code waypoint} is an input coordinate
   * snapped to the road and path network. The {@code waypoint} appear in the list in the order of
   * the input coordinates.
   * Waypoints are returned in the {@link DirectionsRoute} object only when
   * {@link RouteOptions#waypointsPerRoute()} is set to true. Otherwise they are returned
   * in the root: {@link DirectionsResponse#waypoints()}.
   *
   * @return list of {@link DirectionsWaypoint} objects ordered from start of route till the end
   */
  @Nullable
  public abstract List<DirectionsWaypoint> waypoints();

  /**
   * Holds onto the parameter information used when making the directions request. Useful for
   * re-requesting a directions route using the same information previously used.
   *
   * @return a {@link RouteOptions}s object which holds onto critical information from the request
   *   that cannot be derived directly from the directions route
   * @since 3.0.0
   */
  @Nullable
  public abstract RouteOptions routeOptions();


  /**
   * String of the language to be used for voice instructions.  Defaults to en, and
   * can be any accepted instruction language.  Will be <tt>null</tt> when the language provided
   * <tt>MapboxDirections.Builder#language()</tt> via is not compatible with API Voice.
   *
   * @return String compatible with voice instructions, null otherwise
   * @since 3.1.0
   */
  @Nullable
  @SerializedName("voiceLocale")
  public abstract String voiceLanguage();

  /**
   * The universally unique identifier of the request that produced this route.
   *
   * @return request uuid
   */
  @Nullable
  public abstract String requestUuid();

  /*
   * List of calculated toll costs for the route. See `TollCost`.
   *
   * return list of toll costs
   */
  @Nullable
  @SerializedName("toll_costs")
  @SuppressWarnings("checkstyle:javadocmethod")
  public abstract List<TollCost> tollCosts();

  /**
   * Convert the current {@link DirectionsRoute} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link DirectionsRoute}.
   *
   * @return a {@link DirectionsRoute.Builder} with the same values set to match the ones defined
   *   in this {@link DirectionsRoute}
   * @since 3.0.0
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<DirectionsRoute> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsRoute.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   * <p>
   * If you're using the provided route with the Mapbox Navigation SDK, also see
   * {@link #fromJson(String, RouteOptions, String)}.
   *
   * @param json a formatted valid JSON string defining a GeoJson Directions Route
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  public static DirectionsRoute fromJson(@NonNull String json) {
    GsonBuilder gson = new GsonBuilder();
    JsonObject jsonObject = gson.create().fromJson(json, JsonObject.class);
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointAsCoordinatesTypeAdapter());
    return gson.create().fromJson(jsonObject, DirectionsRoute.class);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   * <p>
   * The parameters of {@link RouteOptions} that were used to make the original route request
   * as well as the {@link String} UUID of the original response are needed
   * by the Mapbox Navigation SDK to support correct rerouting and route refreshing.
   *
   * @param json         a formatted valid JSON string defining a GeoJson Directions Route
   * @param routeOptions options that were used during the original route request
   * @param requestUuid  UUID of the request found in the body of the original response,
   *                     see "response.body.uuid"
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @see RouteOptions#fromUrl(java.net.URL)
   * @see RouteOptions#fromJson(String)
   */
  public static DirectionsRoute fromJson(
    @NonNull String json, @Nullable RouteOptions routeOptions, @Nullable String requestUuid
  ) {
    return fromJson(json)
      .toBuilder()
      .routeOptions(routeOptions)
      .requestUuid(requestUuid)
      .build();
  }

  /**
   * This builder can be used to set the values describing the {@link DirectionsRoute}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * The distance traveled from origin to destination.
     *
     * @param distance a double number with unit meters
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @NonNull
    public abstract Builder distance(@NonNull Double distance);

    /**
     * The estimated travel time from origin to destination.
     *
     * @param duration a double number with unit seconds
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @NonNull
    public abstract Builder duration(@NonNull Double duration);

    /**
     * The typical travel time from this route's origin to destination. There's a delay along
     * this route if you subtract this durationTypical() value from the route's duration()
     * value and the resulting difference is greater than 0. The delay is because of any
     * number of real-world situations (road repair, traffic jam, etc).
     *
     * @param durationTypical a double number with unit seconds
     * @return this builder for chaining options together
     * @since 5.5.0
     */
    @NonNull
    public abstract Builder durationTypical(@Nullable Double durationTypical);

    /**
     * Gives the geometry of the route. Commonly used to draw the route on the map view.
     *
     * @param geometry an encoded polyline string
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @NonNull
    public abstract Builder geometry(@Nullable String geometry);

    /**
     * The calculated weight of the route.
     *
     * @param weight the weight value provided from the API as a {@code double} value
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @NonNull
    public abstract Builder weight(@Nullable Double weight);

    /**
     * The weight of the selected route under typical conditions
     * (not taking into account live traffic).
     *
     * @param weightTypical the weight of the selected route under typical conditions
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder weightTypical(@Nullable Double weightTypical);

    /**
     * The name of the weight profile used while calculating during extraction phase. The default is
     * {@code routability} which is duration based, with additional penalties for less desirable
     * maneuvers.
     *
     * @param weightName a String representing the weight profile used while calculating the route
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @NonNull
    public abstract Builder weightName(@Nullable String weightName);

    /**
     * A Leg is a route between only two waypoints.
     *
     * @param legs list of {@link RouteLeg} objects
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @NonNull
    public abstract Builder legs(@Nullable List<RouteLeg> legs);

    /**
     * List of {@link DirectionsWaypoint} objects. Each {@code waypoint} is an input coordinate
     * snapped to the road and path network. The {@code waypoint} appear in the list in the order of
     * the input coordinates.
     * Waypoints are returned in the {@link DirectionsRoute} object only when
     * {@link RouteOptions#waypointsPerRoute()} is set to true. Otherwise they are returned
     * in the root: {@link DirectionsResponse#waypoints()}.
     *
     * @param waypoints list of {@link DirectionsWaypoint} objects ordered from start of route
     *                  till the end
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder waypoints(@Nullable List<DirectionsWaypoint> waypoints);

    /**
     * Holds onto the parameter information used when making the directions request.
     *
     * @param routeOptions a {@link RouteOptions}s object which holds onto critical information from
     *                     the request that cannot be derived directly from the directions route
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @NonNull
    public abstract Builder routeOptions(@Nullable RouteOptions routeOptions);

    /**
     * String of the language to be used for voice instructions.  Defaults to en, and
     * can be any accepted instruction language.
     *
     * @param voiceLanguage String compatible with voice instructions, null otherwise
     * @return this builder for chaining options together
     * @since 3.1.0
     */
    @NonNull
    public abstract Builder voiceLanguage(@Nullable String voiceLanguage);

    /**
     * The universally unique identifier of the request that produced this route.
     *
     * @param requestUuid uuid
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder requestUuid(@Nullable String requestUuid);

    /**
     * The index of the route in the list of routes returned by the original response.
     *
     * @param routeIndex string of an int value representing the index
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder routeIndex(String routeIndex);

    /*
     * List of calculated toll costs for the route. See `TollCost`.
     *
     * param tollCosts list of toll costs
     * return this builder for chaining options together
     */
    @NonNull
    @SuppressWarnings("checkstyle:javadocmethod")
    public abstract Builder tollCosts(@Nullable List<TollCost> tollCosts);

    /**
     * Build a new {@link DirectionsRoute} object.
     *
     * @return a new {@link DirectionsRoute} using the provided values in this builder
     * @since 3.0.0
     */
    @NonNull
    public abstract DirectionsRoute build();
  }
}
