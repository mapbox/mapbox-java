package com.mapbox.api.matrix.v1.models;

import java.io.Serializable;
import java.util.List;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.models.DirectionsWaypoint;

import android.support.annotation.NonNull;

/**
 * This contains the Matrix API response information which can be used to display the results.
 *
 * @since 2.1.0
 */
@AutoValue
public abstract class MatrixResponse implements Serializable {

  /**
   * Create a new instance of this class using the {@link MatrixResponse.Builder} which provides a
   * to pass in variables which define the instance.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MatrixResponse.Builder();
  }

  /**
   * String indicating the state of the response. This is a separate code than the HTTP status code.
   * On normal valid responses, the value will be {@code Ok}.
   * <p>
   * On error, the server responds with different HTTP status codes. For responses with HTTP status
   * codes lower than 500, the JSON response body includes the code property, which may be used by
   * client programs to manage control flow. The response body may also include a message property,
   * with a human-readable explanation of the error. If a server error occurs, the HTTP status code
   * will be 500 or higher and the response will not include a code property.
   * </p><p>
   * Note that in cases where no route is found between a source and destination, no error will be
   * returned, but instead the respective value in the durations matrix will be null.
   * </p>
   *
   * @return "Ok", "NoRoute", "ProfileNotFound", or "InvalidInput"
   * @since 2.1.0
   */
  @NonNull
  public abstract String code();

  /**
   * List of {@link DirectionsWaypoint} objects. Each waypoint is an input coordinate snapped to the
   * road and path network. The waypoints appear in the list in the order of the input coordinates,
   * or in the order as specified in the destinations query parameter.
   *
   * @return list object with each item being a {@link DirectionsWaypoint}
   * @since 2.1.0
   */
  @Nullable
  public abstract List<DirectionsWaypoint> destinations();

  /**
   * List of {@link DirectionsWaypoint} objects. Each waypoints is an input coordinate snapped to
   * the road and path network. The waypoints appear in the list in the order of the input
   * coordinates, or in the order as specified in the sources query parameter.
   *
   * @return list object with each item being a {@link DirectionsWaypoint}
   * @since 2.1.0
   */
  @Nullable
  public abstract List<DirectionsWaypoint> sources();

  /**
   * Durations as a list of arrays representing the matrix in row-major order. durations[i][j] gives
   * the travel time from the i-th source to the j-th destination. All values are in seconds. The
   * duration between the same coordinate is always 0. If a duration can not be found, the result is
   * null.
   *
   * @return an array of array with each entry being a duration value given in seconds.
   * @since 2.1.0
   */
  @Nullable
  public abstract List<Double[]> durations();

  /**
   * Distances as a list of arrays representing the matrix in row-major order. distances[i][j] gives
   * the travel distance from the i-th source to the j-th destination. All values are in meters. The
   * duration between the same coordinate is always 0. If a distance can not be found, the result is
   * null.
   *
   * @return an array of array with each entry being a distance value given in meters.
   * @since 3.4.2
   */
  @Nullable
  public abstract List<Double[]> distances();

  /**
   * Convert the current {@link MatrixResponse} to its builder holding the currently assigned
   * values. This allows you to modify a single variable and then rebuild the object resulting in
   * an updated and modified {@link MatrixResponse}.
   *
   * @return a {@link MatrixResponse.Builder} with the same values set to match the ones
   *   defined in this {@link MatrixResponse}
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
  public static TypeAdapter<MatrixResponse> typeAdapter(Gson gson) {
    return new AutoValue_MatrixResponse.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link MatrixResponse}.
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
     * status code will be 500 or higher and the response will not include a code property.
     * </p><p>
     * Note that in cases where no route is found between a source and destination, no error will be
     * returned, but instead the respective value in the durations matrix will be null.
     * </p>
     *
     * @param code "Ok", "NoRoute", "ProfileNotFound", or "InvalidInput"
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder code(@NonNull String code);

    /**
     * List of {@link DirectionsWaypoint} objects. Each waypoint is an input coordinate snapped to
     * the road and path network. The waypoints appear in the list in the order of the input
     * coordinates, or in the order as specified in the destinations query parameter.
     *
     * @param destinations list object with each item being a {@link DirectionsWaypoint}
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder destinations(@Nullable List<DirectionsWaypoint> destinations);

    /**
     * List of {@link DirectionsWaypoint} objects. Each waypoints is an input coordinate snapped to
     * the road and path network. The waypoints appear in the list in the order of the input
     * coordinates, or in the order as specified in the sources query parameter.
     *
     * @param sources list object with each item being a {@link DirectionsWaypoint}
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder sources(@Nullable List<DirectionsWaypoint> sources);

    /**
     * Durations as array of arrays representing the matrix in row-major order. durations[i][j]
     * gives the travel time from the i-th source to the j-th destination. All values are in
     * seconds. The duration between the same coordinate is always 0. If a duration can not be
     * found, the result is null.
     *
     * @param durations an array of array with each entry being a duration value given in seconds.
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder durations(@Nullable List<Double[]> durations);

    public abstract Builder distances(@Nullable List<Double[]> distances);

    /**
     * Build a new {@link MatrixResponse} object.
     *
     * @return a new {@link MatrixResponse} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract MatrixResponse build();
  }
}
