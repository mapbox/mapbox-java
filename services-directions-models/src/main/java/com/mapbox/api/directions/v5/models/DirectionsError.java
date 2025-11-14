package com.mapbox.api.directions.v5.models;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

import java.io.Serializable;

/**
 * If an InvalidInput error is thrown, this class can be used to get both the code and the message
 * which holds an explanation of the invalid input.
 *
 * @since 3.0.0
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
public abstract class DirectionsError extends DirectionsJsonObject implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_DirectionsError.Builder();
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
   * @since 3.0.0
   */
  @Nullable
  public abstract String code();

  /**
   * Provides a short message with the explanation of the invalid input.
   *
   * @return a string containing the message API Directions response
   * @since 3.0.0
   */
  @Nullable
  public abstract String message();

  /**
   * Convert the current {@link DirectionsError} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link DirectionsError}.
   *
   * @return a {@link DirectionsError.Builder} with the same values set to match the ones defined
   *   in this {@link DirectionsError}
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
  public static TypeAdapter<DirectionsError> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsError.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link DirectionsError}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * String indicating the state of the response. This is a separate code than the HTTP status
     * code. On normal valid responses, the value will be Ok. The possible responses are listed
     * below:
     * <ul>
     * <li><strong>Ok</strong>:200 Normal success case</li>
     * <li><strong>NoRoute</strong>: 200 There was no route found for the given coordinates. Check
     * for impossible routes (e.g. routes over oceans without ferry connections).</li>
     * <li><strong>NoSegment</strong>: 200 No road segment could be matched for coordinates. Check
     * for coordinates too far away from a road.</li>
     * <li><strong>ProfileNotFound</strong>: 404 Use a valid profile as described above</li>
     * <li><strong>InvalidInput</strong>: 422</li>
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
     * Build a new {@link DirectionsError} object.
     *
     * @return a new {@link DirectionsError} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract DirectionsError build();
  }
}
