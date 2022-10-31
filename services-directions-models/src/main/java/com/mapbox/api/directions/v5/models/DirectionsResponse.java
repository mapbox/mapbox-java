package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.PointAsCoordinatesTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the root Mapbox Directions API response. Inside this class are several nested classes
 * chained together to make up a similar structure to the original APIs JSON response.
 *
 * @see <a href="https://www.mapbox.com/api-documentation/navigation/#directions-response-object">Direction
 *   Response Object</a>
 * @since 1.0.0
 */
@AutoValue
public abstract class DirectionsResponse extends DirectionsJsonObject {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  @NonNull
  public static Builder builder() {
    return new AutoValue_DirectionsResponse.Builder();
  }

  /**
   * String indicating the state of the response. This is a separate code than the HTTP status code.
   * On normal valid responses, the value will be Ok. The possible responses are listed below:
   * <ul>
   * <li><strong>Ok</strong>:200 Normal success case</li>
   * <li><strong>NoRoute</strong>: 200 There was no route found for the given coordinates. Check
   * for impossible routes (e.g. routes over oceans without ferry connections).</li>
   * <li><strong>NoSegment</strong>: 200 No road segment could be matched for coordinates. Check for
   * coordinates too far away from a road.</li>
   * <li><strong>ProfileNotFound</strong>: 404 Use a valid profile as described above</li>
   * <li><strong>InvalidInput</strong>: 422</li>
   * </ul>
   *
   * @return a string with one of the given values described in the list above
   * @since 1.0.0
   */
  @NonNull
  public abstract String code();

  /**
   * Optionally shows up in a directions response if an error or something unexpected occurred.
   *
   * @return a string containing the message API Directions response with if an error occurred
   * @since 3.0.0
   */
  @Nullable
  public abstract String message();

  /**
   * List of {@link DirectionsWaypoint} objects. Each {@code waypoint} is an input coordinate
   * snapped to the road and path network. The {@code waypoint} appear in the list in the order of
   * the input coordinates.
   * @deprecated the waypoints are returned in the root of the response only if
   *   {@link RouteOptions#waypointsPerRoute()} is false. Otherwise they are returned in
   *   {@link DirectionsRoute#waypoints()}. You can continue to use this and it will work as-is.
   *   However, it's recommended to use the new approach to distinguish waypoints between routes
   *   (for example, this is necessary when asking for an EV-optimized route with alternatives).
   *
   * @return list of {@link DirectionsWaypoint} objects ordered from start of route till the end
   * @since 1.0.0
   */
  @Deprecated
  @Nullable
  public abstract List<DirectionsWaypoint> waypoints();

  /**
   * List containing all the different route options. It's ordered by descending recommendation
   * rank. In other words, object 0 in the List is the highest recommended route. if you don't
   * setAlternatives to true (default is false) in your builder this should always be a List of
   * size 1. At most this will return 2 {@link DirectionsRoute} objects.
   *
   * @return list of {@link DirectionsRoute} objects
   * @since 1.0.0
   */
  @NonNull
  public abstract List<DirectionsRoute> routes();

  /**
   * A universally unique identifier (UUID) for identifying and executing a similar specific route
   * in the future.
   *
   * @return a String representing the UUID given by the directions request
   * @since 3.0.0
   */
  @Nullable
  public abstract String uuid();

  /**
   * A complex data structure that provides information about the source of the response.
   *
   * @return a Metadata with additional source information
   */
  @Nullable
  public abstract Metadata metadata();

  /**
   * Convert the current {@link DirectionsResponse} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link DirectionsResponse}.
   *
   * @return a {@link DirectionsResponse.Builder} with the same values set to match the ones defined
   *   in this {@link DirectionsResponse}
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
  public static TypeAdapter<DirectionsResponse> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsResponse.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   * <p>
   * Consider using {@link #fromJson(String, RouteOptions)} if the result is used with
   * downstream consumers of the directions models (like Mapbox Navigation SDK)
   * to provide rerouting and route refreshing features.
   *
   * @param json a formatted valid JSON string defining a GeoJson Directions Response
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @see #fromJson(String, RouteOptions)
   * @since 3.0.0
   */
  public static DirectionsResponse fromJson(@NonNull String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointAsCoordinatesTypeAdapter());
    // rebuilding to ensure that underlying routes have assigned indices and UUID
    return gson.create().fromJson(json, DirectionsResponse.class).toBuilder().build();
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   * <p>
   * The parameters of {@link RouteOptions} that were used to make the original route request
   * as well as the {@link String} UUID of the original response are needed
   * by the Navigation SDK to support correct rerouting and route refreshing.
   *
   * @param json         a formatted valid JSON string defining a GeoJson Directions Route
   * @param routeOptions options that were used during the original route request
   * @param requestUuid  UUID of the request found in the body of the original response,
   *                     see "response.body.uuid"
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @see RouteOptions#fromUrl(java.net.URL)
   * @see RouteOptions#fromJson(String)
   * @deprecated use {@link #fromJson(String, RouteOptions)} instead
   */
  @Deprecated
  public static DirectionsResponse fromJson(
    @NonNull String json, @Nullable RouteOptions routeOptions, @Nullable String requestUuid) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointAsCoordinatesTypeAdapter());
    DirectionsResponse response = gson.create().fromJson(json, DirectionsResponse.class);
    if (routeOptions != null) {
      response = response.updateWithRequestData(routeOptions);
    }
    return response.toBuilder()
      // set the provided UUID for backwards compatibility
      // even though it's most likely incorrect
      .uuid(requestUuid)
      // rebuilding to ensure that underlying routes have assigned indices and UUID
      .build();
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   * <p>
   * The parameter of {@link RouteOptions} that were used to make the original route request
   * which might be required by downstream consumers of the directions models
   * (like Mapbox Navigation SDK) to provide rerouting and route refreshing features.
   *
   * @param json         a formatted valid JSON string defining a GeoJson Directions Route
   * @param routeOptions options that were used during the original route request
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @see RouteOptions#fromUrl(java.net.URL)
   * @see RouteOptions#fromJson(String)
   */
  public static DirectionsResponse fromJson(
    @NonNull String json, @NonNull RouteOptions routeOptions) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointAsCoordinatesTypeAdapter());
    DirectionsResponse response = gson.create().fromJson(json, DirectionsResponse.class);
    // rebuilding to ensure that underlying routes have assigned indices and UUID
    return response.updateWithRequestData(routeOptions);
  }

  /**
   * Exposes an option to enhance an existing response with the request data which might be required
   * by downstream consumers of the directions models (like Mapbox Navigation SDK) to provide
   * rerouting and route refreshing features.
   *
   * @param routeOptions options used to generate this response
   * @see RouteOptions#fromUrl(java.net.URL)
   * @see RouteOptions#fromJson(String)
   * @see #fromJson(String)
   */
  @NonNull
  public DirectionsResponse updateWithRequestData(@NonNull RouteOptions routeOptions) {
    List<DirectionsRoute> modifiedRoutes = new ArrayList<>();
    for (DirectionsRoute route : routes()) {
      modifiedRoutes.add(
        route.toBuilder()
          .routeOptions(routeOptions)
          .build()
      );
    }
    return this.toBuilder()
      .routes(modifiedRoutes)
      .build();
  }

  /**
   * This builder can be used to set the values describing the {@link DirectionsResponse}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * String indicating the state of the response. This is a separate code than the HTTP status
     * code. On normal valid responses, the value will be Ok. For a full list of possible responses,
     * see {@link DirectionsResponse#code()}.
     *
     * @param code a string with one of the given values described in the list above
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder code(@NonNull String code);

    /**
     * Optionally shows up in a directions response if an error or something unexpected occurred.
     *
     * @param message a string containing the message API Directions response with if an error
     *                occurred
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder message(@Nullable String message);

    /**
     * List of {@link DirectionsWaypoint} objects. Each {@code waypoint} is an input coordinate
     * snapped to the road and path network. The {@code waypoint} appear in the list in the order of
     * the input coordinates.
     * @deprecated the waypoints are returned in the root of the response only if
     *   {@link RouteOptions#waypointsPerRoute()} is false. Otherwise they are returned in
     *   {@link DirectionsRoute#waypoints()}. You can continue to use this and it will work as-is.
     *   However, it's recommended to use the new approach to distinguish waypoints between routes
     *   (for example, this is necessary when asking for an EV-optimized route with alternatives).
     *
     * @param waypoints list of {@link DirectionsWaypoint} objects ordered from start of route till
     *                  the end
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @Deprecated
    public abstract Builder waypoints(@Nullable List<DirectionsWaypoint> waypoints);

    /**
     * List containing all the different route options. It's ordered by descending recommendation
     * rank. In other words, object 0 in the List is the highest recommended route. if you don't
     * setAlternatives to true (default is false) in your builder this should always be a List of
     * size 1. At most this will return 2 {@link DirectionsRoute} objects.
     *
     * @param routes list of {@link DirectionsRoute} objects
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder routes(@NonNull List<DirectionsRoute> routes);

    @NonNull
    abstract List<DirectionsRoute> routes();

    /**
     * A universally unique identifier (UUID) for identifying and executing a similar specific route
     * in the future.
     *
     * @param uuid a String representing the UUID given by the directions request
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder uuid(@Nullable String uuid);

    @Nullable
    abstract String uuid();

    /**
     * A complex data structure that provides information about the source of the response.
     *
     * @param metadata a Metadata with additional source information
     * @return this builder for chaining options together
     */
    public abstract Builder metadata(@Nullable Metadata metadata);

    abstract DirectionsResponse autoBuild();

    /**
     * Build a new {@link DirectionsResponse} object.
     *
     * @return a new {@link DirectionsResponse} using the provided values in this builder
     * @since 3.0.0
     */
    public DirectionsResponse build() {
      List<DirectionsRoute> transformedRoutes = new ArrayList<>(routes().size());
      for (int i = 0; i < routes().size(); i++) {
        transformedRoutes.add(
          i,
          routes().get(i)
            .toBuilder()
            .routeIndex(String.valueOf(i))
            .requestUuid(uuid())
            .build()
        );
      }
      routes(transformedRoutes);

      return autoBuild();
    }
  }
}
