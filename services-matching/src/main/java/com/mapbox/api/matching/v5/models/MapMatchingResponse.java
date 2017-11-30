package com.mapbox.api.matching.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Mapbox map matching API response and convenience getter methods for optional properties.
 *
 * @see <a href="https://www.mapbox.com/api-documentation/#retrieve-a-match">Map Matching API Documentation</a>
 * @since 2.0.0
 */
@AutoValue
public abstract class MapMatchingResponse implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MapMatchingResponse.Builder();
  }

  /**
   * A string depicting the state of the response.
   * <ul>
   * <li>"Ok" - Normal case</li>
   * <li>"NoMatch" - The input did not produce any matches. matchings will be an empty array.</li>
   * <li>"TooManyCoordinates" - There are more than 100 points in the request.</li>
   * <li>"InvalidInput" - message will hold an explanation of the invalid input.</li>
   * <li>"ProfileNotFound" - Profile should be {@code mapbox.driving}, {@code mapbox.walking},
   * or {@code mapbox.cycling}.</li>
   * </ul>
   *
   * @return string containing the code
   * @since 2.0.0
   */
  @NonNull
  public abstract String code();

  /**
   * List of {@link MapMatchingMatching} objects, essentially a DirectionsWaypoint object with the
   * addition of a confidence value.
   *
   * @return a list made up of {@link MapMatchingMatching} objects
   * @since 2.0.0
   */
  @Nullable
  public abstract List<MapMatchingMatching> matchings();

  /**
   * A list of {@link MapMatchingTracepoint} objects representing the location an input point was
   * matched with. list of Waypoint objects representing all input points of the trace in the order
   * they were matched. If a trace point is omitted by map matching because it is an outlier, the
   * entry will be null.
   *
   * @return tracepoints list
   * @since 2.0.0
   */
  @Nullable
  public abstract List<MapMatchingTracepoint> tracepoints();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<MapMatchingResponse> typeAdapter(Gson gson) {
    return new AutoValue_MapMatchingResponse.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link MapMatchingResponse}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * A string depicting the state of the response.
     * <ul>
     * <li>"Ok" - Normal case</li>
     * <li>"NoMatch" - The input did not produce any matches. matchings will be an empty array.</li>
     * <li>"TooManyCoordinates" - There are more than 100 points in the request.</li>
     * <li>"InvalidInput" - message will hold an explanation of the invalid input.</li>
     * <li>"ProfileNotFound" - Profile should be {@code mapbox.driving}, {@code mapbox.walking},
     * or {@code mapbox.cycling}.</li>
     * </ul>
     *
     * @param code string containing the code
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder code(@Nullable String code);

    /**
     * List of {@link MapMatchingMatching} objects, essentially a DirectionsWaypoint object with the
     * addition of a confidence value.
     *
     * @param matchings a list made up of {@link MapMatchingMatching} objects
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder matchings(@Nullable List<MapMatchingMatching> matchings);


    /**
     * A list of {@link MapMatchingTracepoint} objects representing the location an input point was
     * matched with. list of Waypoint objects representing all input points of the trace in the
     * order they were matched. If a trace point is omitted by map matching because it is an
     * outlier, the entry will be null.
     *
     * @param tracepoints tracepoints list
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder tracepoints(@Nullable List<MapMatchingTracepoint> tracepoints);

    /**
     * Build a new {@link MapMatchingResponse} object.
     *
     * @return a new {@link MapMatchingResponse} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract MapMatchingResponse build();
  }
}
