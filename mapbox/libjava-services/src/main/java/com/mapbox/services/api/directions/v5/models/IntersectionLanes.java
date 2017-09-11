package com.mapbox.services.api.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Object representing lanes in an intersection.
 *
 * @since 2.0.0
 */
@AutoValue
public abstract class IntersectionLanes implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_IntersectionLanes.Builder();
  }

  /**
   * Provides a boolean value you can use to determine if the given lane is valid for the user to
   * complete the maneuver.
   *
   * @return Boolean value for whether this lane can be taken to complete the maneuver. For
   *   instance, if the lane array has four objects and the first two are marked as valid, then the
   *   driver can take either of the left lanes and stay on the route.
   * @since 2.0.0
   */
  @Nullable
  public abstract Boolean valid();

  /**
   * Array that can be made up of multiple signs such as {@code left}, {@code right}, etc.
   *
   * @return Array of signs for each turn lane. There can be multiple signs. For example, a turning
   *   lane can have a sign with an arrow pointing left and another sign with an arrow pointing
   *   straight.
   * @since 2.0.0
   */
  @Nullable
  public abstract List<String> indications();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<IntersectionLanes> typeAdapter(Gson gson) {
    return new AutoValue_IntersectionLanes.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link IntersectionLanes}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Provide a boolean value you can use to determine if the given lane is valid for the user to
     * complete the maneuver.
     *
     * @param valid Boolean value for whether this lane can be taken to complete the maneuver. For
     *              instance, if the lane array has four objects and the first two are marked as
     *              valid, then the driver can take either of the left lanes and stay on the route.
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder valid(@Nullable Boolean valid);

    /**
     * list that can be made up of multiple signs such as {@code left}, {@code right}, etc.
     *
     * @param indications list of signs for each turn lane. There can be multiple signs. For
     *                    example, a turning lane can have a sign with an arrow pointing left and
     *                    another sign with an arrow pointing straight.
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder indications(@Nullable List<String> indications);

    /**
     * Build a new {@link IntersectionLanes} object.
     *
     * @return a new {@link IntersectionLanes} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract IntersectionLanes build();
  }
}
