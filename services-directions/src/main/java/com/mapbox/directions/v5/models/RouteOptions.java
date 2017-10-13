package com.mapbox.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.mapbox.directions.v5.DirectionsCriteria;
import com.mapbox.directions.v5.DirectionsCriteria.ProfileCriteria;

/**
 * Provides information connected to your request that help when a new directions request is needing
 * using the identical parameters as the original request.
 * <p>
 * For example, if I request a driving (profile) with alternatives and continueStraight set to true.
 * I make the request but loose reference and information which built the original request. Thus, If
 * I only want to change a single variable such as the destination coordinate, i'd have to have all
 * the other route information stores so the request was made identical to the previous but only now
 * using this new destination point.
 * <p>
 * Using this class can provide you wth the information used when the {@link DirectionsRoute} was
 * made.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class RouteOptions {

  /**
   * Build a new instance of this RouteOptions class optionally settling values.
   *
   * @return {@link RouteOptions.Builder}
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_RouteOptions.Builder();
  }

  /**
   * The same user which was used during the request that resulted in this root directions response.
   *
   * @return string value representing the user
   * @since 3.0.0
   */
  @Nullable
  public abstract String user();

  /**
   * The same profile which was used during the request that resulted in this root directions
   * response.
   *
   * @return string value representing the profile
   * @since 3.0.0
   */
  @Nullable
  public abstract String profile();

  /**
   * The same alternative setting which was used during the request that resulted in this root
   * directions response.
   *
   * @return boolean object representing the setting for alternatives
   * @since 3.0.0
   */
  @Nullable
  public abstract Boolean alternatives();

  /**
   * The same language which was used during the request that resulted in this root directions
   * response.
   *
   * @return the language as a string used during the request, if english, this will most likely be
   *   empty
   * @since 3.0.0
   */
  @Nullable
  public abstract String language();

  /**
   * The same radiuses were used during the request that resulted in this root directions response.
   *
   * @return a string representing the radiuses
   * @since 3.0.0
   */
  @Nullable
  public abstract String radiuses();

  /**
   * The same bearings which were used during the request that resulted in this root directions
   * response. Note that even though these are saved. it's a good idea to recalculate any bearings
   * being used and use the newer values for the directions request.
   *
   * @return a string representing the bearings used in the original request
   * @since 3.0.0
   */
  @Nullable
  public abstract String bearings();

  /**
   * The same continueStraight setting which was used during the request that resulted in this root
   * directions response.
   *
   * @return a boolean value representing whether or not continueStraight was enabled or not during
   *   the initial request
   * @since 3.0.0
   */
  @Nullable
  public abstract Boolean continueStraight();

  /**
   * The same annotations in String format which were used during the request that resulted in this
   * root directions response.
   *
   * @return a string containing any of the annotations that were used during the request
   * @since 3.0.0
   */
  @Nullable
  public abstract String annotations();

  /**
   * Whether or not the request had voice instructions set to true or not.
   *
   * @return true if the original request included voice instructions
   * @since 3.0.0
   */
  @Nullable
  public abstract Boolean voiceInstructions();

  /**
   * This builder can be used to set the values describing the {@link RouteOptions}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * The user value that was used during the request.
     *
     * @param user string representing the user field in the calling url
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder user(String user);

    /**
     * The directions profile that was used during the request time and resulted in this responses
     * result.
     *
     * @param profile One of the direction profiles defined in
     *                {@link DirectionsCriteria#DirectionsCriteria()}
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder profile(@Nullable @ProfileCriteria String profile);

    /**
     * Whether the alternatives value was set to true or not.
     *
     * @param alternatives true if the request contained additional route request, otherwise false
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder alternatives(Boolean alternatives);

    /**
     * The language for instructions to be in when the response is given.
     *
     * @param language a string with the language which was requested in the url
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder language(String language);

    /**
     * The radiuses in string format that were used during the original request.
     *
     * @param radiuses radiuses values separated by comma
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder radiuses(String radiuses);

    /**
     * The bearing values the user used for the original request which resulted in this response.
     * It is best to recalculate these values since they are probably outdated already.
     *
     * @param bearings number values representing the bearings separated by commas
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder bearings(String bearings);

    /**
     * Whether the original request wanted continueStraight enabled or not.
     *
     * @param continueStraight true if you'd like the user to continue straight from the starting
     *                         point
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder continueStraight(Boolean continueStraight);

    /**
     * The annotation which were used during the request process.
     *
     * @param annotations in string format and separated by commas if more than one annotation was
     *                    requested
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder annotations(String annotations);

    /**
     * Whether or not the request had voice instructions set to true or not.
     *
     * @param voiceInstructions true if the original request included voice instructions
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder voiceInstructions(Boolean voiceInstructions);

    /**
     * Builds a new instance of the {@link RouteOptions} object.
     *
     * @return a new {@link RouteOptions} instance
     * @since 3.0.0
     */
    public abstract RouteOptions build();
  }
}