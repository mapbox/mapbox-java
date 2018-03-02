package com.mapbox.api.directions.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.geojson.Point;

import java.util.List;

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
   * The same base URL which was used during the request that resulted in this root directions
   * response.
   *
   * @return string value representing the base URL
   * @since 3.0.0
   */
  @NonNull
  public abstract String baseUrl();

  /**
   * The same user which was used during the request that resulted in this root directions response.
   *
   * @return string value representing the user
   * @since 3.0.0
   */
  @NonNull
  public abstract String user();

  /**
   * The same profile which was used during the request that resulted in this root directions
   * response. {@link MapboxDirections#builder()} ensures that a profile is always set even if the
   * {@link MapboxDirections} requesting object doesn't specifically set a profile.
   *
   * @return string value representing the profile
   * @since 3.0.0
   */
  @NonNull
  public abstract String profile();

  /**
   * The coordinates used for the routes origin, destination, and optionally, waypoints. Note that
   * these coordinates are different than the direction responses {@link DirectionsWaypoint}s in
   * that these are the non-snapped coordinates.
   *
   * @return a list of {@link Point}s which represent the route origin, destination, and optionally,
   * waypoints
   * @since 3.0.0
   */
  @NonNull
  public abstract List<Point> coordinates();

  /**
   * The same alternative setting which were used during the request that resulted in this root
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
   * empty
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
   * the initial request
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
   * The same exclusions the user originally made when the request was made.
   *
   * @return a string matching one of the {@link DirectionsCriteria} exclusions
   * @since 3.0.0
   */
  @Nullable
  public abstract String exclude();

  /**
   * Whether or not the request had voice instructions set to true or not.
   *
   * @return true if the original request included voice instructions
   * @since 3.0.0
   */
  @Nullable
  public abstract Boolean voiceInstructions();

  /**
   * Whether or not the request had banner instructions set to true or not.
   *
   * @return true if the original request included banner instructions
   * @since 3.0.0
   */
  @Nullable
  public abstract Boolean bannerInstructions();

  /**
   * Whether or not the units used inside the voice instruction's string are in imperial or metric.
   *
   * @return a string matching either imperial or metric
   * @since 3.0.0
   */
  @Nullable
  public abstract String voiceUnits();

  /**
   * A valid Mapbox access token used to making the request.
   *
   * @return a string representing the Mapbox access token
   * @since 3.0.0
   */
  @NonNull
  public abstract String accessToken();

  /**
   * A universally unique identifier (UUID) for identifying and executing a similar specific route
   * in the future. {@link MapboxDirections} always waits for the response object which ensures this
   * value will never be null.
   *
   * @return a string containing the request UUID
   * @since 3.0.0
   */
  @NonNull
  public abstract String requestUuid();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<RouteOptions> typeAdapter(Gson gson) {
    return new AutoValue_RouteOptions.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link RouteOptions}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * The base URL that was used during the request time and resulted in this responses
     * result.
     *
     * @param baseUrl base URL used for original request
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder baseUrl(@NonNull String baseUrl);

    /**
     * The user value that was used during the request.
     *
     * @param user string representing the user field in the calling url
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder user(@NonNull String user);

    /**
     * The directions profile that was used during the request time and resulted in this responses
     * result.
     *
     * @param profile One of the direction profiles defined in
     *                {@link DirectionsCriteria#DirectionsCriteria()}
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder profile(@NonNull @DirectionsCriteria.ProfileCriteria String profile);

    /**
     * The coordinates used for the routes origin, destination, and optionally, waypoints. Note that
     * these coordinates are different than the direction responses {@link DirectionsWaypoint}s in
     * that these are the non-snapped coordinates.
     *
     * @param coordinates a list of {@link Point}s which represent the route origin, destination,
     *                    and optionally, waypoints
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder coordinates(@NonNull List<Point> coordinates);

    /**
     * Whether the alternatives value was set to true or not.
     *
     * @param alternatives true if the request contained additional route request, otherwise false
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder alternatives(@Nullable Boolean alternatives);

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
     * Whether or not the request had banner instructions set to true or not.
     *
     * @param bannerInstructions true if the original request included banner instructions
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder bannerInstructions(Boolean bannerInstructions);

    /**
     * Whether or not the units used inside the voice instruction's string are in imperial or metric.
     *
     * @param voiceUnits string matching either imperial or metric
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder voiceUnits(@Nullable String voiceUnits);

    /**
     * A valid Mapbox access token used to making the request.
     *
     * @param accessToken a string containing a valid Mapbox access token
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder accessToken(@NonNull String accessToken);

    /**
     * A universally unique identifier (UUID) for identifying and executing a similar specific route
     * in the future.
     *
     * @param requestUuid a string containing the request UUID
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder requestUuid(@NonNull String requestUuid);

    /**
     * The same exclusions the user originally made when the request was made.
     *
     * @param exclude a string matching one of the {@link DirectionsCriteria} exclusions
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @Nullable
    public abstract Builder exclude(String exclude);

    /**
     * Builds a new instance of the {@link RouteOptions} object.
     *
     * @return a new {@link RouteOptions} instance
     * @since 3.0.0
     */
    public abstract RouteOptions build();
  }
}
