package com.mapbox.api.optimization.v1.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import java.io.Serializable;
import java.util.List;

/**
 * When the Mapbox Optimization API response, this will be the root class for accessing all the
 * response information provided.
 *
 * @since 2.1.0
 */
@AutoValue
public abstract class OptimizationResponse implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  @NonNull
  public static Builder builder() {
    return new AutoValue_OptimizationResponse.Builder();
  }

  /**
   * String indicating the state of the response. This is a separate code than the HTTP status code.
   * On normal valid responses, the value will be {@code Ok}.
   * <p>
   * On error, the server responds with different HTTP status codes. For responses with HTTP status
   * codes lower than 500, the JSON response body includes the code property, which may be used by
   * client programs to manage control flow. The response body may also include a message property,
   * with a human-readable explanation of the error. If a server error occurs, the HTTP status code
   * will be 500 or higher and the response will not include a code property. Possible errors
   * include:
   * <ul>
   * <li><strong>Ok</strong>: {@code 200} Normal success case</li>
   * <li><strong>NoTrips</strong>: {@code 200} For one coordinate no route to other coordinates
   * could be found. Check for impossible routes (e.g. routes over oceans without ferry
   * connections).</li>
   * <li><strong>NotImplemented</strong>: {@code 200} For the given combination of {@code source},
   * {@code destination} and {@code roundtrip}, this request is not supported.</li>
   * <li><strong>ProfileNotFound</strong>: {@code 404} Use a valid profile</li>
   * <li><strong>InvalidInput</strong>: {@code 422} The given request was not valid. The message key
   * of the response will hold an explanation of the invalid input.</li>
   * </ul>
   *
   * @return string containing the response code. In normal conditions this will return {@code OK}
   * @since 2.1.0
   */
  @Nullable
  public abstract String code();

  /**
   * List of {@link OptimizationWaypoint} objects. Each waypoint is an input coordinate snapped to
   * the road and path network. The waypoints appear in the list in the order of the input
   * coordinates.
   *
   * @return a list of {@link OptimizationWaypoint}s in the order of the input coordinates
   * @since 2.1.0
   */
  @Nullable
  public abstract List<OptimizationWaypoint> waypoints();

  /**
   * List of trip {@link DirectionsRoute} objects. Will have zero or one trip.
   *
   * @return list of {@link DirectionsRoute} either having a size zero or one
   * @since 2.1.0
   */
  @Nullable
  public abstract List<DirectionsRoute> trips();

  /**
   * Convert the current {@link OptimizationResponse} to its builder holding the currently assigned
   * values. This allows you to modify a single variable and then rebuild the object resulting in
   * an updated and modified {@link OptimizationResponse}.
   *
   * @return a {@link OptimizationResponse.Builder} with the same values set to match the ones
   *   defined in this {@link OptimizationResponse}
   * @since 3.1.0
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<OptimizationResponse> typeAdapter(Gson gson) {
    return new AutoValue_OptimizationResponse.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link OptimizationResponse}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * String indicating the state of the response. This is a separate code than the HTTP status
     * code. On normal valid responses, the value will be {@code Ok}.
     * <p>
     * On error, the server responds with different HTTP status codes. For responses with HTTP
     * status codes lower than 500, the JSON response body includes the code property, which may be
     * used by client programs to manage control flow. The response body may also include a message
     * property, with a human-readable explanation of the error. If a server error occurs, the HTTP
     * status code
     * will be 500 or higher and the response will not include a code property. Possible errors
     * include:
     * <ul>
     * <li><strong>Ok</strong>: {@code 200} Normal success case</li>
     * <li><strong>NoTrips</strong>: {@code 200} For one coordinate no route to other coordinates
     * could be found. Check for impossible routes (e.g. routes over oceans without ferry
     * connections).</li>
     * <li><strong>NotImplemented</strong>: {@code 200} For the given combination of {@code source},
     * {@code destination} and {@code roundtrip}, this request is not supported.</li>
     * <li><strong>ProfileNotFound</strong>: {@code 404} Use a valid profile</li>
     * <li><strong>InvalidInput</strong>: {@code 422} The given request was not valid. The message
     * key of the response will hold an explanation of the invalid input.</li>
     * </ul>
     *
     * @param code string containing the response code. In normal conditions this will return
     *             {@code OK}
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder code(@Nullable String code);

    /**
     * List of {@link OptimizationWaypoint} objects. Each waypoint is an input coordinate snapped to
     * the road and path network. The waypoints appear in the list in the order of the input
     * coordinates.
     *
     * @param waypoints a list of {@link OptimizationWaypoint}s in the order of the input
     *                  coordinates
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder waypoints(@Nullable List<OptimizationWaypoint> waypoints);

    /**
     * List of trip {@link DirectionsRoute} objects. Will have zero or one trip.
     *
     * @param trips list of {@link DirectionsRoute} either having a size zero or one
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder trips(@Nullable List<DirectionsRoute> trips);

    /**
     * Build a new {@link OptimizationResponse} object.
     *
     * @return a new {@link OptimizationResponse} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract OptimizationResponse build();
  }
}
