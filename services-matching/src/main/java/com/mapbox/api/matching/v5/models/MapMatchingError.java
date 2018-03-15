package com.mapbox.api.matching.v5.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;

/**
 * If an InvalidInput error is thrown, this class can be used to get both the code and the message
 * which holds an explanation of the invalid input.
 *
 * On error, the server responds with different HTTP status codes.
 * For responses with HTTP status codes lower than 500, the JSON response body includes the code property,
 * which may be used by client programs to manage control flow. The response body may also include a message property,
 * with a human-readable explaination of the error. If a server error occurs,
 * the HTTP status code will be 500 or higher and the response will not include a code property.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class MapMatchingError implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MapMatchingError.Builder();
  }

  /**
   * String indicating the state of the response. This is a separate code than the HTTP status code.
   * On normal valid responses, the value will be Ok. The possible responses are listed below:
   * <ul>
   * <li><strong>Ok</strong> Normal case.</li>
   * <li><strong>NoMatch</strong> The input did not produce any matches, or the  waypoints requested were not found
   * in the resulting match. features will be an empty array.</li>
   * <li><strong>TooManyCoordinates</strong> There are more than 100 points in the request.</li>
   * <li><strong>ProfileNotFound</strong> Needs to be a valid profile. </li>
   * <li><strong>InvalidInput</strong>message will hold an explanation of the invalid input.</li>
   * </ul>
   *
   * @return a string with one of the given values described in the list above
   * @since 3.0.0
   */
  @Nullable
  public abstract String code();

  /**
   * Provides a short message with the explanation of the invalid input.
   *
   * @return a string containing the message API MapMatching response
   * @since 3.0.0
   */
  @Nullable
  public abstract String message();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<MapMatchingError> typeAdapter(Gson gson) {
    return new AutoValue_MapMatchingError.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link MapMatchingError}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * String indicating the state of the response. This is a separate code than the HTTP status
     * code. On normal valid responses, the value will be Ok. The possible responses are listed
     * below:
     * <ul>
     * <li><strong>Ok</strong> Normal case.</li>
     * <li><strong>NoMatch</strong> The input did not produce any matches, or the  waypoints requested were not found
     * in the resulting match. features will be an empty array.</li>
     * <li><strong>TooManyCoordinates</strong> There are more than 100 points in the request.</li>
     * <li><strong>ProfileNotFound</strong> Needs to be a valid profile. </li>
     * <li><strong>InvalidInput</strong>message will hold an explanation of the invalid input.</li>
     * </ul>
     *
     * @param code a string with one of the given values described in the list above
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder code(String code);

    /**
     * Provides a short message with the explanation of the invalid input.
     *
     * @param message a string containing the message API Directions response
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder message(String message);

    /**
     * Build a new {@link MapMatchingError} object.
     *
     * @return a new {@link MapMatchingError} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract MapMatchingError build();
  }
}
