package com.mapbox.services.api.optimization.v1;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.services.Constants;
import com.mapbox.services.api.MapboxAdapterFactory;
import com.mapbox.services.api.MapboxCallHelper;
import com.mapbox.services.api.MapboxService;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.DirectionsCriteria.AnnotationCriteria;
import com.mapbox.services.api.directions.v5.DirectionsCriteria.DestinationCriteria;
import com.mapbox.services.api.directions.v5.DirectionsCriteria.GeometriesCriteria;
import com.mapbox.services.api.directions.v5.DirectionsCriteria.OverviewCriteria;
import com.mapbox.services.api.directions.v5.DirectionsCriteria.ProfileCriteria;
import com.mapbox.services.api.directions.v5.DirectionsCriteria.SourceCriteria;
import com.mapbox.services.api.optimization.v1.models.OptimizationResponse;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.utils.MapboxUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The Mapbox Optimization API returns a duration-optimized trip between the input coordinates.
 * This is also known as solving the Traveling Salesperson Problem. A typical use case for this API
 * is planning the route for deliveries in a city. Optimized trips can be retrieved for car driving,
 * bicycling and walking or hiking.
 * <p>
 * Under normal plans, a maximum of 12 coordinates can be passed in at once at a maximum 60 requests
 * per minute. For higher volumes, reach out through our contact page.
 * <p>
 * Note that for under 10 coordinates, the returned results will be optimal. For 10 and more
 * coordinates, the results will be optimized approximations.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Travelling_salesman_problem">Traveling Salesperson
 * Problem</a>
 * @see <a href="https://www.mapbox.com/api-documentation/#optimized-trips">API documentation</a>
 * @since 2.1.0
 */
@AutoValue
public abstract class MapboxOptimization extends MapboxService<OptimizationResponse> {

  private Call<OptimizationResponse> call;
  private OptimizationService service;
  protected Builder builder;

  /**
   * Execute the call
   *
   * @return The Directions Matrix v1 response
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   * @since 2.1.0
   */
  @Override
  public Response<OptimizationResponse> executeCall() throws IOException {
    return getCall().execute();
  }

  /**
   * Execute the call
   *
   * @param callback A Retrofit callback.
   * @since 2.1.0
   */
  @Override
  public void enqueueCall(Callback<OptimizationResponse> callback) {
    getCall().enqueue(callback);
  }

  /**
   * Cancel the call
   *
   * @since 2.1.0
   */
  @Override
  public void cancelCall() {
    getCall().cancel();
  }

  /**
   * clone the call
   *
   * @return cloned call
   * @since 2.1.0
   */
  @Override
  public Call<OptimizationResponse> cloneCall() {
    return getCall().clone();
  }

  private OptimizationService getService() {
    // No need to recreate it
    if (service != null) {
      return service;
    }

    // Retrofit instance
    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
      .baseUrl(baseUrl())
      .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
        .registerTypeAdapterFactory(MapboxAdapterFactory.create())
        .create()));
    if (getCallFactory() != null) {
      retrofitBuilder.callFactory(getCallFactory());
    } else {
      retrofitBuilder.client(getOkHttpClient());
    }

    // Optimization's service
    service = retrofitBuilder.build().create(OptimizationService.class);
    return service;
  }

  private Call<OptimizationResponse> getCall() {
    // No need to recreate it
    if (call != null) {
      return call;
    }

    call = getService().getCall(
      getHeaderUserAgent(clientAppName()),
      user(),
      profile(),
      coordinates(),
      accessToken(),
      roundTrip(),
      radiuses(),
      bearings(),
      steps(),
      overview(),
      geometries(),
      annotations(),
      destination(),
      source(),
      language(),
      distributions());

    // Done
    return call;
  }

  @Nullable
  abstract String distributions();

  @NonNull
  abstract String user();

  @NonNull
  abstract String profile();

  @Nullable
  abstract Boolean roundTrip();

  @Nullable
  abstract String source();

  @Nullable
  abstract String destination();

  @Nullable
  abstract String geometries();

  @Nullable
  abstract String overview();

  @Nullable
  abstract Boolean steps();

  @Nullable
  abstract String clientAppName();

  @Nullable
  abstract String accessToken();

  @NonNull
  abstract String baseUrl();

  @Nullable
  abstract String language();

  @Nullable
  abstract String radiuses();

  @Nullable
  abstract String bearings();

  @NonNull
  abstract String coordinates();

  @Nullable
  abstract String annotations();

  /**
   * Build a new {@link MapboxOptimization} object with the initial values set for
   * {@link #baseUrl()}, {@link #profile()}, {@link #user()}, and {@link #geometries()}.
   *
   * @return a {@link Builder} object for creating this object
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxOptimization.Builder()
      .baseUrl(Constants.BASE_API_URL)
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .user(DirectionsCriteria.PROFILE_DEFAULT_USER)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6);
  }

  /**
   * Optimized Trips v1 builder
   *
   * @since 2.1.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    private List<Integer[]> distributions = new ArrayList<>();
    private List<Double[]> bearings = new ArrayList<>();
    private List<Point> coordinates = new ArrayList<>();
    private String[] annotations;
    private double[] radiuses;

    /**
     * The username for the account that the directions engine runs on. In most cases, this should
     * always remain the default value of {@link DirectionsCriteria#PROFILE_DEFAULT_USER}.
     *
     * @param user a non-null string which will replace the default user used in the directions
     *             request
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder user(@NonNull String user);

    /**
     * Add a list of {@link Point}'s which define the route which will become optimized. The minimum
     * points is 2 and the maximum points allowed in totals 12. You can use this method in
     * conjunction with {@link #coordinate(Point)}.
     *
     * @param coordinates a List full of {@link Point}s which define the route
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public Builder coordinates(@NonNull List<Point> coordinates) {
      this.coordinates.addAll(coordinates);
      return this;
    }

    // Required for matching with MapboxOptimization coordinates() method.
    abstract Builder coordinates(@NonNull String coordinates);

    /**
     * This will add a single {@link Point} to the coordinate list which is used to determine the
     * most optimal route. This can be called up to 12 times until you hit the maximum allowed
     * points. You can use this method in conjunction with {@link #coordinates(List)}.
     *
     * @param coordinate a {@link Point} which you'd like the optimized route to pass through
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder coordinate(@NonNull Point coordinate) {
      this.coordinates.add(coordinate);
      return this;
    }

    /**
     * This selects which mode of transportation the user will be using while navigating from the
     * origin to the final destination. The options include driving, driving considering traffic,
     * walking, and cycling. Using each of these profiles will result in different routing biases.
     *
     * @param profile required to be one of the String values found in the {@link ProfileCriteria}
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder profile(@NonNull @ProfileCriteria String profile);

    /**
     * Returned route is a roundtrip (route returns to first location). Allowed values are:
     * {@code true} (default), {@code false} and null (to reset to the default value). If the
     * roundtrip is set to false, then {@link #source()} and {@link #destination()} parameters are
     * required but not all combinations are possible.
     * <p>
     * It is possible to explicitly set the start or end coordinate of the trip. When source? is set
     * to first, the first coordinate is used as the start coordinate of the trip in the output.
     * When destination is set to last, the last coordinate will be used as destination of the trip
     * in the returned output. If you specify {@link DirectionsCriteria#DESTINATION_ANY}/
     * {@link DirectionsCriteria#SOURCE_ANY}, any of the coordinates can be used as the first or
     * last coordinate in the output.
     *
     * @param roundTrip true if you'd like the route to return to the origin, else false
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/#retrieve-an-optimization">Possible
     * roundtrip combinations</a>
     * @since 2.1.0
     */
    public abstract Builder roundTrip(@Nullable Boolean roundTrip);

    /**
     * Returned route starts at {@link DirectionsCriteria#SOURCE_ANY} or
     * {@link DirectionsCriteria#SOURCE_FIRST} coordinate. Null can also be passed in to reset this
     * value back to the API default if needed.
     *
     * @param source one of the values in {@link SourceCriteria}
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder source(@Nullable @SourceCriteria String source);

    /**
     * Returned route ends at {@link DirectionsCriteria#DESTINATION_ANY} or
     * {@link DirectionsCriteria#DESTINATION_LAST} coordinate.
     *
     * @param destination either {@code "any" or "last"}
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder destination(@Nullable @DestinationCriteria String destination);

    /**
     * alter the default geometry being returned for the directions route. A null value will reset
     * this field to the APIs default value vs this SDKs default value of
     * {@link DirectionsCriteria#GEOMETRY_POLYLINE6}.
     * <p>
     * Note that while the API supports GeoJSON as an option for geometry, this SDK intentionally
     * removes this as an option since an encoded string for the geometry significantly reduces
     * bandwidth on mobile devices and speeds up response time.
     * </p>
     *
     * @param geometries null if you'd like the default geometry, else one of the options found in
     *                   {@link GeometriesCriteria}.
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder geometries(@Nullable @GeometriesCriteria String geometries);

    /**
     * Type of returned overview geometry. Can be {@link DirectionsCriteria#OVERVIEW_FULL} (the most
     * detailed geometry available), {@link DirectionsCriteria#OVERVIEW_SIMPLIFIED} (a simplified
     * version of the full geometry), or {@link DirectionsCriteria#OVERVIEW_FALSE} (no overview
     * geometry). The default is simplified. Passing in null will use the APIs default setting for
     * the overview field.
     *
     * @param overview null or one of the options found in {@link OverviewCriteria}
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder overview(@Nullable @OverviewCriteria String overview);

    /**
     * Optionally, set the maximum distance in meters that each coordinate is allowed to move when
     * snapped to a nearby road segment. There must be as many radiuses as there are coordinates in
     * the request. Values can be any number greater than 0 or they can be unlimited simply by
     * passing {@link Double#POSITIVE_INFINITY}.
     * <p>
     * If no routable road is found within the radius, a {@code NoSegment} error is returned.
     * </p>
     *
     * @param radiuses double array containing the radiuses defined in unit meters.
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public Builder radiuses(@FloatRange(from = 0) double... radiuses) {
      this.radiuses = radiuses;
      return this;
    }

    // Required for matching with MapboxOptimization radiuses() method.
    abstract Builder radiuses(@Nullable String radiuses);

    /**
     * Optionally, Use to filter the road segment the waypoint will be placed on by direction and
     * dictates the angle of approach. This option should always be used in conjunction with the
     * {@link #radiuses(double...)} parameter.
     * <p>
     * The parameter takes two values per waypoint: the first is an angle clockwise from true north
     * between 0 and 360. The second is the range of degrees the angle can deviate by. We recommend
     * a value of 45 degrees or 90 degrees for the range, as bearing measurements tend to be
     * inaccurate. This is useful for making sure we reroute vehicles on new routes that continue
     * traveling in their current direction. A request that does this would provide bearing and
     * radius values for the first waypoint and leave the remaining values empty. If provided, the
     * list of bearings must be the same length as the list of waypoints, but you can skip a
     * coordinate and show its position by passing in null value for both the angle and tolerance
     * values.
     * </p><p>
     * Each bearing value gets associated with the same order which coordinates are arranged in this
     * builder. For example, the first bearing added in this builder will be associated with the
     * origin {@code Point}, the nth bearing being associated with the nth waypoint added (if added)
     * and the last bearing being added will be associated with the destination.
     * </p>
     *
     * @param angle     double value used for setting the corresponding coordinate's angle of travel
     *                  when determining the route
     * @param tolerance the deviation the bearing angle can vary while determining the route,
     *                  recommended to be either 45 or 90 degree tolerance
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public Builder bearing(@Nullable @FloatRange(from = 0, to = 360) Double angle,
                              @Nullable @FloatRange(from = 0, to = 360) Double tolerance) {
      bearings.add(new Double[] {angle, tolerance});
      return this;
    }

    // Required for matching with MapboxOptimization bearings() method.
    abstract Builder bearings(@Nullable String bearings);

    /**
     * Setting this will determine whether to return steps and turn-by-turn instructions. Can be
     * set to either true or false to enable or disable respectively. null can also optionally be
     * passed in to set the default behavior to match what the API does by default.
     *
     * @param steps true if you'd like step information
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder steps(@Nullable Boolean steps);

    /**
     * Whether or not to return additional metadata along the route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE},
     * {@link DirectionsCriteria#ANNOTATION_DURATION},
     * {@link DirectionsCriteria#ANNOTATION_DURATION} and
     * {@link DirectionsCriteria#ANNOTATION_CONGESTION}. Several annotation can be used by
     * separating them with {@code ,}.
     *
     * @param annotations string referencing one of the annotation direction criteria's. The strings
     *                    restricted to one or multiple values inside the {@link AnnotationCriteria}
     *                    or null which will result in no annotations being used
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/#routeleg-object">RouteLeg object
     * documentation</a>
     * @since 2.1.0
     */
    public Builder annotations(@Nullable @AnnotationCriteria String... annotations) {
      this.annotations = annotations;
      return this;
    }

    // Required for matching with MapboxOptimization annotations() method.
    abstract Builder annotations(@Nullable String annotations);

    /**
     * Set the instruction language for the directions request, the default is english. Only a
     * select number of languages are currently supported, reference the table provided in the see
     * link below.
     *
     * @param language a Locale value representing the language you'd like the instructions to be
     *                 written in when returned
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/#instructions-languages">Supported
     * Languages</a>
     * @since 3.0.0
     */
    public Builder language(@Nullable Locale language) {
      if (language != null) {
        language(language.toString());
      }
      return this;
    }

    /**
     * Set the instruction language for the directions request, the default is english. Only a
     * select number of languages are currently supported, reference the table provided in the see
     * link below. It is recommended to use the {@link #language(Locale)} method to prevent errors
     * when making the request.
     *
     * @param language a String value representing the language you'd like the instructions to be
     *                 written in when returned
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/#instructions-languages">Supported
     * Languages</a>
     * @since 2.2.0
     */
    public abstract Builder language(@Nullable String language);

    /**
     * Base package name or other simple string identifier. Used inside the calls user agent header.
     *
     * @param clientAppName base package name or other simple string identifier
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder clientAppName(@NonNull String clientAppName);

    /**
     * Required to call when this is being built. If no access token provided,
     * {@link ServicesException} will be thrown.
     *
     * @param accessToken Mapbox access token, You must have a Mapbox account inorder to use
     *                    the Optimization API
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder accessToken(@NonNull String accessToken);

    /**
     * Optionally change the APIs base URL to something other then the default Mapbox one.
     *
     * @param baseUrl base url used as end point
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder baseUrl(@NonNull String baseUrl);

    /**
     * Specify pick-up and drop-off locations for a trip by providing a {@code pickup} and
     * {@code dropOff} value correspond with the coordinates list. The first number indicates what
     * place the coordinate of the pick-up location is in the coordinates list, and the second
     * number indicates what place the coordinate of the drop-off location is in the coordinates
     * list. Pick-up and drop-off locations in one pair cannot be the same. The returned solution
     * will visit pick-up locations before visiting drop-off locations. The depot (first location)
     * can only be a pick-up location but not a drop-off location.
     *
     * @param dropOff the coordinate index in the coordinates list which should be the drop off
     *                location
     * @param pickup  the coordinate index in the coordinates list which should be the pick-up
     *                location
     * @return this builder for chaining options together
     * @since 2.2.0
     */
    public Builder distribution(@Nullable Integer pickup, @Nullable Integer dropOff) {
      distributions.add(new Integer[] {pickup, dropOff});
      return this;
    }

    // Required for matching with MapboxOptimization distributions() method.
    abstract Builder distributions(@Nullable String distributions);

    abstract MapboxOptimization autoBuild();

    /**
     * This uses the provided parameters set using the {@link Builder} and first checks that all
     * values are valid, formats the values as strings for easier consumption by the API, and lastly
     * creates a new {@link MapboxOptimization} object with the values provided.
     *
     * @return a new instance of Mapbox Optimization
     * @throws ServicesException when a provided parameter is detected to be incorrect
     * @since 2.1.0
     */
    public MapboxOptimization build() throws ServicesException {
      if (coordinates == null || coordinates.size() < 2) {
        throw new ServicesException("At least two coordinates must be provided with your API"
          + "request.");
      } else if (coordinates.size() > 12) {
        throw new ServicesException("Maximum of 12 coordinates are allowed for this API.");
      }

      coordinates(MapboxCallHelper.formatCoordinates(coordinates));
      bearings(MapboxCallHelper.formatBearing(bearings));
      annotations(MapboxCallHelper.formatStringArray(annotations));
      radiuses(MapboxCallHelper.formatRadiuses(radiuses));
      distributions(MapboxCallHelper.formatDistributions(distributions));

      // Generate build so that we can check that values are valid.
      MapboxOptimization optimization = autoBuild();

      if (!MapboxUtils.isAccessTokenValid(optimization.accessToken())) {
        throw new ServicesException("Using Mapbox Services requires setting a valid access token.");
      }
      return optimization;
    }
  }
}
