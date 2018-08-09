package com.mapbox.api.directions.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.gson.PointDeserializer;

import java.util.List;

/**
 * This is the root Mapbox Directions API response. Inside this class are several nested classes
 * chained together to make up a similar structure to the original APIs JSON response.
 *
 * @see <a href="https://www.mapbox.com/api-documentation/#directions-response-object">Direction
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
   *
   * @return list of {@link DirectionsWaypoint} objects ordered from start of route till the end
   * @since 1.0.0
   */
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
   *
   * @param json a formatted valid JSON string defining a GeoJson Directions Response
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static DirectionsResponse fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    return gson.create().fromJson(json, DirectionsResponse.class);
  }

  /**
   * This builder can be used to set the values describing the {@link DirectionsResponse}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

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
     *
     * @param waypoints list of {@link DirectionsWaypoint} objects ordered from start of route till
     *                  the end
     * @return this builder for chaining options together
     * @since 3.0.0
     */
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

    /**
     * A universally unique identifier (UUID) for identifying and executing a similar specific route
     * in the future.
     *
     * @param uuid a String representing the UUID given by the directions request
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder uuid(@Nullable String uuid);

    /**
     * Build a new {@link DirectionsResponse} object.
     *
     * @return a new {@link DirectionsResponse} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract DirectionsResponse build();
  }
}
