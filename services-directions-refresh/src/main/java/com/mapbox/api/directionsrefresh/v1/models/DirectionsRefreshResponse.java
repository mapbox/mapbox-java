package com.mapbox.api.directionsrefresh.v1.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.models.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.models.DirectionsJsonObject;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

/**
 * Response object for Directions Refresh requests.
 *
 * @since 4.4.0
 */
@AutoValue
public abstract class DirectionsRefreshResponse extends DirectionsJsonObject {

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
   * @since 4.4.0
   */
  @NonNull
  public abstract String code();

  /**
   * Optionally shows up in a response if an error or something unexpected occurred.
   *
   * @return a string containing the message
   * @since 4.4.0
   */
  @Nullable
  public abstract String message();

  /**
   * Barebones {@link DirectionsRoute} which only contains a list of
   * {@link com.mapbox.api.directions.v5.models.RouteLeg}s, which only contain lists of the
   * refreshed annotations.
   *
   * @return barebones route with annotation data
   * @since 4.4.0
   */
  @Nullable
  public abstract DirectionsRoute route();

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 4.4.0
   */
  @NonNull
  public static Builder builder() {
    return new AutoValue_DirectionsRefreshResponse.Builder();
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 4.4.0
   */
  public static TypeAdapter<DirectionsRefreshResponse> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsRefreshResponse.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a Directions Refresh response
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 4.4.0
   */
  public static DirectionsRefreshResponse fromJson(String json) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapterFactory(DirectionsRefreshAdapterFactory.create())
      .registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gsonBuilder.create().fromJson(json, DirectionsRefreshResponse.class);
  }

  /**
   * This builder can be used to set the values describing the {@link DirectionsRefreshResponse}.
   * @since 4.4.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * String indicating the state of the response. This is a separate code than the HTTP status
     * code. On normal valid responses, the value will be Ok. For a full list of possible responses,
     * see {@link DirectionsRefreshResponse#code()}.
     *
     * @param code a string with one of the given values described in the list above
     * @return this builder
     * @since 4.4.0
     */
    public abstract Builder code(String code);

    /**
     * Optionally shows up in a response if an error or something unexpected occurred.
     *
     * @param message a string containing the message
     * @return this builder
     * @since 4.4.0
     */
    public abstract Builder message(String message);

    /**
     * Barebones {@link DirectionsRoute} which only contains a list of
     * {@link com.mapbox.api.directions.v5.models.RouteLeg}s, which only contain lists of the
     * refreshed annotations.
     *
     * @param directionsRoute route containing annotation data
     * @return this builder
     * @since 4.4.0
     */
    public abstract Builder route(DirectionsRoute directionsRoute);

    /**
     * Builds a new {@link DirectionsRefreshResponse} object.
     *
     * @return a new {@link DirectionsRefreshResponse} object
     * @since 4.4.0
     */
    public abstract DirectionsRefreshResponse build();
  }
}
