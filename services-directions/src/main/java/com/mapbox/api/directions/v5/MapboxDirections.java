package com.mapbox.api.directions.v5;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.api.directions.v5.DirectionsCriteria.AnnotationCriteria;
import com.mapbox.api.directions.v5.DirectionsCriteria.ExcludeCriteria;
import com.mapbox.api.directions.v5.DirectionsCriteria.GeometriesCriteria;
import com.mapbox.api.directions.v5.DirectionsCriteria.OverviewCriteria;
import com.mapbox.api.directions.v5.DirectionsCriteria.ProfileCriteria;
import com.mapbox.api.directions.v5.DirectionsCriteria.VoiceUnitCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.RouteLeg;
import com.mapbox.api.directions.v5.utils.FormatUtils;
import com.mapbox.core.MapboxService;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.core.utils.ApiCallHelper;
import com.mapbox.core.utils.MapboxUtils;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.EventListener;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The Directions API allows the calculation of routes between coordinates. The fastest route can be
 * returned with geometries, turn-by-turn instructions, and much more. The Mapbox Directions API
 * supports routing for driving cars (including live traffic), riding bicycles and walking.
 * Requested routes can include as much as 25 coordinates anywhere on earth (except the traffic
 * profile).
 * <p>
 * Requesting a route at a bare minimal must include, a Mapbox access token, destination, and an
 * origin.
 * </p>
 *
 * @see <a href="https://www.mapbox.com/android-docs/java-sdk/overview/directions/">Android
 * Directions documentation</a>
 * @see <a href="https://www.mapbox.com/api-documentation/navigation/#directions">Directions API
 * documentation</a>
 * @since 1.0.0
 */
@AutoValue
public abstract class MapboxDirections extends
  MapboxService<DirectionsResponse, DirectionsService> {

  protected MapboxDirections() {
    super(DirectionsService.class);
  }

  @Override
  protected Call<DirectionsResponse> initializeCall() {
    if (usePostMethod() == null) {
      return callForUrlLength();
    }

    if (usePostMethod()) {
      return post();
    }

    return get();
  }

  private Call<DirectionsResponse> callForUrlLength() {
    Call<DirectionsResponse> get = get();
    if (get.request().url().toString().length() < MAX_URL_SIZE) {
      return get;
    }
    return post();
  }

  private Call<DirectionsResponse> get() {
    return getService().getCall(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      user(),
      profile(),
      FormatUtils.formatCoordinates(coordinates()),
      accessToken(),
      alternatives(),
      geometries(),
      overview(),
      radius(),
      steps(),
      bearing(),
      continueStraight(),
      annotation(),
      language(),
      roundaboutExits(),
      voiceInstructions(),
      bannerInstructions(),
      voiceUnits(),
      exclude(),
      approaches(),
      waypointIndices(),
      waypointNames(),
      waypointTargets(),
      enableRefresh(),
      walkingSpeed(),
      walkwayBias(),
      alleyBias(),
      snappingClosures()
    );
  }

  private Call<DirectionsResponse> post() {
    return getService().postCall(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      user(),
      profile(),
      FormatUtils.formatCoordinates(coordinates()),
      accessToken(),
      alternatives(),
      geometries(),
      overview(),
      radius(),
      steps(),
      bearing(),
      continueStraight(),
      annotation(),
      language(),
      roundaboutExits(),
      voiceInstructions(),
      bannerInstructions(),
      voiceUnits(),
      exclude(),
      approaches(),
      waypointIndices(),
      waypointNames(),
      waypointTargets(),
      enableRefresh(),
      walkingSpeed(),
      walkwayBias(),
      alleyBias(),
      snappingClosures()
    );
  }

  @Override
  protected GsonBuilder getGsonBuilder() {
    return super.getGsonBuilder()
      .registerTypeAdapterFactory(DirectionsAdapterFactory.create());
  }

  /**
   * Wrapper method for Retrofits {@link Call#execute()} call returning a response specific to the
   * Directions API.
   *
   * @return the Directions v5 response once the call completes successfully
   * @throws IOException Signals that an I/O exception of some sort has occurred
   * @since 1.0.0
   */
  @Override
  public Response<DirectionsResponse> executeCall() throws IOException {
    Response<DirectionsResponse> response = super.executeCall();
    DirectionsResponseFactory factory = new DirectionsResponseFactory(this);
    return factory.generate(response);
  }

  /**
   * Wrapper method for Retrofits {@link Call#enqueue(Callback)} call returning a response specific
   * to the Directions API. Use this method to make a directions request on the Main Thread.
   *
   * @param callback a {@link Callback} which is used once the {@link DirectionsResponse} is
   *                 created.
   * @since 1.0.0
   */
  @Override
  public void enqueueCall(final Callback<DirectionsResponse> callback) {
    getCall().enqueue(new Callback<DirectionsResponse>() {
      @Override
      public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        DirectionsResponseFactory factory = new DirectionsResponseFactory(MapboxDirections.this);
        Response<DirectionsResponse> generatedResponse = factory.generate(response);
        callback.onResponse(call, generatedResponse);
      }

      @Override
      public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
        callback.onFailure(call, throwable);
      }
    });
  }

  @Override
  protected synchronized OkHttpClient getOkHttpClient() {
    if (okHttpClient == null) {
      OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
      if (isEnableDebug()) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        httpClient.addInterceptor(logging);
      }
      Interceptor interceptor = interceptor();
      if (interceptor != null) {
        httpClient.addInterceptor(interceptor);
      }
      Interceptor networkInterceptor = networkInterceptor();
      if (networkInterceptor != null) {
        httpClient.addNetworkInterceptor(networkInterceptor);
      }
      EventListener eventListener = eventListener();
      if (eventListener != null) {
        httpClient.eventListener(eventListener);
      }

      okHttpClient = httpClient.build();
    }
    return okHttpClient;
  }

  @NonNull
  abstract String user();

  @NonNull
  abstract String profile();

  @NonNull
  abstract List<Point> coordinates();

  @NonNull
  @Override
  protected abstract String baseUrl();

  @NonNull
  abstract String accessToken();

  @Nullable
  abstract Boolean alternatives();

  @Nullable
  abstract String geometries();

  @Nullable
  abstract String overview();

  @Nullable
  abstract String radius();

  @Nullable
  abstract String bearing();

  @Nullable
  abstract Boolean steps();

  @Nullable
  abstract Boolean continueStraight();

  @Nullable
  abstract String annotation();

  @Nullable
  abstract String language();

  @Nullable
  abstract Boolean roundaboutExits();

  @Nullable
  abstract String clientAppName();

  @Nullable
  abstract Boolean voiceInstructions();

  @Nullable
  abstract Boolean bannerInstructions();

  @Nullable
  abstract String voiceUnits();

  @Nullable
  abstract String exclude();

  @Nullable
  abstract String approaches();

  @Nullable
  abstract String waypointIndices();

  @Nullable
  abstract String waypointNames();

  @Nullable
  abstract String waypointTargets();

  @Nullable
  abstract Boolean enableRefresh();

  @Nullable
  abstract Interceptor interceptor();

  @Nullable
  abstract Interceptor networkInterceptor();

  @Nullable
  abstract EventListener eventListener();

  @Nullable
  abstract Boolean usePostMethod();

  @Nullable
  abstract WalkingOptions walkingOptions();

  @Nullable
  Double walkingSpeed() {
    if (!hasWalkingOptions()) {
      return null;
    }

    return walkingOptions().walkingSpeed();
  }

  @Nullable
  Double walkwayBias() {
    if (!hasWalkingOptions()) {
      return null;
    }

    return walkingOptions().walkwayBias();
  }

  @Nullable
  Double alleyBias() {
    if (!hasWalkingOptions()) {
      return null;
    }

    return walkingOptions().alleyBias();
  }

  @Nullable
  abstract String snappingClosures();

  private boolean hasWalkingOptions() {
    return walkingOptions() != null;
  }

  /**
   * Build a new {@link MapboxDirections} object with the initial values set for
   * {@link #baseUrl()}, {@link #profile()}, {@link #user()}, and {@link #geometries()}.
   *
   * @return a {@link Builder} object for creating this object
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxDirections.Builder()
      .baseUrl(Constants.BASE_API_URL)
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .user(DirectionsCriteria.PROFILE_DEFAULT_USER)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6);
  }

  /**
   * Returns the builder which created this instance of {@link MapboxDirections} and allows for
   * modification and building a new directions request with new information.
   *
   * @return {@link MapboxDirections.Builder} with the same variables set as this directions object
   * @since 3.0.0
   */
  public abstract Builder toBuilder();

  /**
   * This builder is used to create a new request to the Mapbox Directions API. At a bare minimum,
   * your request must include an access token, an origin, and a destination. All other fields can
   * be left alone inorder to use the default behaviour of the API.
   * <p>
   * By default, the directions profile is set to driving (without traffic) but can be changed to
   * reflect your users use-case.
   * </p><p>
   * Note to contributors: All optional booleans in this builder use the object {@code Boolean}
   * rather than the primitive to allow for unset (null) values.
   * </p>
   *
   * @since 1.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {
    // TODO: change List<Double> to a custom model or to a Pair
    private List<List<Double>> bearings = new ArrayList<>();
    private List<Point> coordinates = new ArrayList<>();
    private List<String> annotations = new ArrayList<>();
    private List<Double> radiuses = new ArrayList<>();
    private Point destination;
    private Point origin;
    private List<String> approaches = new ArrayList<>();
    private List<Integer> waypointIndices = new ArrayList<>();
    private List<String> waypointNames = new ArrayList<>();
    private List<Point> waypointTargets = new ArrayList<>();
    private List<Boolean> snappingClosures = new ArrayList<>();

    /**
     * The username for the account that the directions engine runs on. In most cases, this should
     * always remain the default value of {@link DirectionsCriteria#PROFILE_DEFAULT_USER}.
     *
     * @param user a non-null string which will replace the default user used in the directions
     *             request
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder user(@NonNull String user);

    /**
     * This selects which mode of transportation the user will be using while navigating from the
     * origin to the final destination. The options include driving, driving considering traffic,
     * walking, and cycling. Using each of these profiles will result in different routing biases.
     *
     * @param profile required to be one of the String values found in the {@link ProfileCriteria}
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder profile(@NonNull @ProfileCriteria String profile);

    /**
     * This sets the starting point on the map where the route will begin. It is one of the
     * required parameters which must be set for a successful directions response.
     *
     * @param origin a GeoJson {@link Point} object representing the starting location for the route
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public Builder origin(@NonNull Point origin) {
      this.origin = origin;
      return this;
    }

    /**
     * This sets the ending point on the map where the route will end. It is one of the required
     * parameters which must be set for a successful directions response.
     *
     * @param destination a GeoJson {@link Point} object representing the starting location for the
     *                    route
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public Builder destination(@NonNull Point destination) {
      this.destination = destination;
      return this;
    }

    /**
     * This can be used to set up to 23 additional in-between points which will act as pit-stops
     * along the users route. Note that if you are using the
     * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC} that the max number of waypoints allowed
     * in the request is currently limited to 1.
     *
     * @param waypoint a {@link Point} which represents the pit-stop or waypoint where you'd like
     *                 one of the {@link RouteLeg} to navigate the user to
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder addWaypoint(@NonNull Point waypoint) {
      coordinates.add(waypoint);
      return this;
    }

    /**
     * This can be used to set up to 23 additional in-between points which will act as pit-stops
     * along the users route. Note that if you are using the
     * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC} that the max number of waypoints allowed
     * in the request is currently limited to 1.
     *
     * @param waypoints a list which represents the pit-stops or waypoints where
     *                  you'd like one of the {@link RouteLeg} to navigate the user to
     * @return this builder for chaining options together
     */
    public Builder waypoints(@NonNull List<Point> waypoints) {
      coordinates = waypoints;
      return this;
    }

    /**
     * Optionally set whether to try to return alternative routes. An alternative is classified as a
     * route that is significantly different then the fastest route, but also still reasonably fast.
     * Not in all circumstances such a route exists. At the moment at most one alternative can be
     * returned.
     *
     * @param alternatives true if you'd like to receive an alternative route, otherwise false or
     *                     null to use the APIs default value
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder alternatives(@Nullable Boolean alternatives);

    /**
     * alter the default geometry being returned for the directions route. A null value will reset
     * this field to the APIs default value vs this SDKs default value of
     * {@link DirectionsCriteria#GEOMETRY_POLYLINE6}.
     * <p>
     * Note that while the API supports GeoJson as an option for geometry, this SDK intentionally
     * removes this as an option since an encoded string for the geometry significantly reduces
     * bandwidth on mobile devices and speeds up response time.
     * </p>
     *
     * @param geometries null if you'd like the default geometry, else one of the options found in
     *                   {@link GeometriesCriteria}.
     * @return this builder for chaining options together
     * @since 2.0.0
     */
    public abstract Builder geometries(@GeometriesCriteria String geometries);

    /**
     * Type of returned overview geometry. Can be {@link DirectionsCriteria#OVERVIEW_FULL} (the most
     * detailed geometry available), {@link DirectionsCriteria#OVERVIEW_SIMPLIFIED} (a simplified
     * version of the full geometry), or {@link DirectionsCriteria#OVERVIEW_FALSE} (no overview
     * geometry). The default is simplified. Passing in null will use the APIs default setting for
     * the overview field.
     *
     * @param overview null or one of the options found in
     *                 {@link OverviewCriteria}
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder overview(@Nullable @OverviewCriteria String overview);

    /**
     * Setting this will determine whether to return steps and turn-by-turn instructions. Can be
     * set to either true or false to enable or disable respectively. null can also optionally be
     * passed in to set the default behavior to match what the API does by default.
     *
     * @param steps true if you'd like step information
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder steps(@Nullable Boolean steps);

    /**
     * Sets allowed direction of travel when departing intermediate waypoints. If true the route
     * will continue in the same direction of travel. If false the route may continue in the
     * opposite direction of travel. API defaults to true for
     * {@link DirectionsCriteria#PROFILE_DRIVING} and false for
     * {@link DirectionsCriteria#PROFILE_WALKING} and {@link DirectionsCriteria#PROFILE_CYCLING}.
     *
     * @param continueStraight boolean true if you want to always continue straight, else false.
     *                         Null can also be passed in here to use the APIs default option
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder continueStraight(@Nullable Boolean continueStraight);

    /**
     * Set the instruction language for the directions request, the default is english. Only a
     * select number of languages are currently supported, reference the table provided in the see
     * link below.
     *
     * @param language a Locale value representing the language you'd like the instructions to be
     *                 written in when returned
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/navigation/#instructions-languages">Supported
     * Languages</a>
     * @since 2.2.0
     */
    public Builder language(@Nullable Locale language) {
      if (language != null) {
        language(language.getLanguage());
      }
      return this;
    }

    abstract Builder language(@Nullable String language);

    /**
     * Optionally, set this to true if you want to enable instructions while exiting roundabouts
     * and rotaries.
     *
     * @param roundaboutExits true if you'd like extra roundabout instructions
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder roundaboutExits(@Nullable Boolean roundaboutExits);

    /**
     * Whether or not to return additional metadata along the route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE},
     * {@link DirectionsCriteria#ANNOTATION_DURATION} and
     * {@link DirectionsCriteria#ANNOTATION_CONGESTION}. Several annotation can be used by
     * separating them with {@code ,}.
     *
     * @param annotations string referencing one of the annotation direction criteria's. The strings
     *                    restricted to one or multiple values inside the {@link AnnotationCriteria}
     *                    or null which will result in no annotations being used
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/navigation/#route-leg-object">RouteLeg object
     * documentation</a>
     * @since 2.1.0
     * @deprecated use {@link #annotations(List)}
     */
    @Deprecated
    public Builder annotations(@AnnotationCriteria @NonNull String... annotations) {
      return annotations(Arrays.asList(annotations));
    }

    /**
     * Whether or not to return additional metadata along the route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE},
     * {@link DirectionsCriteria#ANNOTATION_DURATION} and
     * {@link DirectionsCriteria#ANNOTATION_CONGESTION}
     *
     * @param annotations a list of annotations
     * @return this builder for chaining options together
     */
    public Builder annotations(@NonNull List<String> annotations) {
      this.annotations = annotations;
      return this;
    }

    /**
     * Whether or not to return additional metadata along the route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE},
     * {@link DirectionsCriteria#ANNOTATION_DURATION} and
     * {@link DirectionsCriteria#ANNOTATION_CONGESTION}
     *
     * @param annotation string referencing one of the annotation direction criteria's.
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/navigation/#route-leg-object">RouteLeg object
     * documentation</a>
     * @since 2.1.0
     */
    public Builder addAnnotation(@AnnotationCriteria @NonNull String annotation) {
      this.annotations.add(annotation);
      return this;
    }

    abstract Builder annotation(@Nullable String annotation);

    /**
     * Optionally, Use to filter the road segment the waypoint will be placed on by direction and
     * dictates the angle of approach. This option should always be used in conjunction with the
     * {@link #radiuses} parameter.
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
     * @since 2.0.0
     */
    public Builder addBearing(@Nullable @FloatRange(from = 0, to = 360) Double angle,
                              @Nullable @FloatRange(from = 0, to = 360) Double tolerance) {
      bearings.add(Arrays.asList(angle, tolerance));
      return this;
    }

    /**
     * Optionally, Use to filter the road segment the waypoint will be placed on by direction and
     * dictates the angle of approach. This option should always be used in conjunction with the
     * {@link #radiuses} parameter.
     *
     * @param bearings  a list of list of doubles. Every list has two values:
     *                  the first is an angle clockwise from true north between 0 and 360. The
     *                  second is the range of degrees the angle can deviate by. We recommend
     *                  a value of 45 degrees or 90 degrees for the range, as bearing measurements
     *                  tend to be inaccurate.
     * @return this builder for chaining options together
     */
    public Builder bearings(@NonNull List<List<Double>> bearings) {
      this.bearings = bearings;
      return this;
    }

    abstract Builder bearing(@Nullable String bearings);

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
     * @since 1.0.0
     * @deprecated use {@link #radiuses(List)}
     */
    @Deprecated
    public Builder radiuses(@NonNull @FloatRange(from = 0) Double... radiuses) {
      return radiuses(Arrays.asList(radiuses));
    }

    /**
     * Optionally, set the maximum distance in meters that each coordinate is allowed to move when
     * snapped to a nearby road segment. There must be as many radiuses as there are coordinates in
     * the request. Values can be any number greater than 0 or they can be unlimited simply by
     * passing {@link Double#POSITIVE_INFINITY}.
     * <p>
     * If no routable road is found within the radius, a {@code NoSegment} error is returned.
     * </p>
     *
     * @param radiuses a list with radiuses defined in unit meters.
     * @return this builder for chaining options together
     */
    public Builder radiuses(@NonNull List<Double> radiuses) {
      this.radiuses = radiuses;
      return this;
    }

    /**
     * Optionally, set the maximum distance in meters that each coordinate is allowed to move when
     * snapped to a nearby road segment. There must be as many radiuses as there are coordinates in
     * the request. Values can be any number greater than 0 or they can be unlimited simply by
     * passing {@link Double#POSITIVE_INFINITY}.
     * <p>
     * If no routable road is found within the radius, a {@code NoSegment} error is returned.
     * </p>
     *
     * @param radius double param defined in unit meters.
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public Builder addRadius(@NonNull @FloatRange(from = 0) Double radius) {
      this.radiuses.add(radius);
      return this;
    }

    abstract Builder radius(@Nullable String radiuses);

    /**
     * Exclude certain road types from routing. Valid values depend on the profile in use. The
     * default is to not exclude anything from the profile selected.
     *
     * @param exclude one of the constants defined in {@link ExcludeCriteria}
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder exclude(@ExcludeCriteria String exclude);

    /**
     * Request voice instructions objects to be returned in your response. This offers instructions
     * specific for navigation and provides well spoken text instructions along with the distance
     * from the maneuver the instructions should be said.
     * <p>
     * It's important to note that the {@link #steps(Boolean)} should be true or else these results
     * wont be returned.
     * </p>
     *
     * @param voiceInstructions true if you'd like voice instruction objects be attached to your
     *                          response
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder voiceInstructions(@Nullable Boolean voiceInstructions);

    /**
     * Request banner instructions object to be returned in your response. This is useful
     * specifically for navigation and provides an abundance of information one might want to
     * display to their user inside an Android view for example.
     *
     * @param bannerInstructions true if you'd like the receive banner objects within your response
     *                           object
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder bannerInstructions(@Nullable Boolean bannerInstructions);

    /**
     * Specify what unit you'd like voice and banner instructions to use.
     *
     * @param voiceUnits either Imperial (default) or Metric
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder voiceUnits(@Nullable @VoiceUnitCriteria String voiceUnits);

    /**
     * Base package name or other simple string identifier. Used inside the calls user agent header.
     *
     * @param clientAppName base package name or other simple string identifier
     * @return this builder for chaining options together
     * @since 1.0.0
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
    public abstract Builder baseUrl(String baseUrl);

    /**
     * Adds an optional interceptor to set in the OkHttp client.
     *
     * @param interceptor to set for OkHttp
     * @return this builder for chaining options together
     */
    public abstract Builder interceptor(Interceptor interceptor);

    /**
     * Adds an optional network interceptor to set in the OkHttp client.
     *
     * @param interceptor to set for OkHttp
     * @return this builder for chaining options together
     */
    public abstract Builder networkInterceptor(Interceptor interceptor);

    /**
     * Adds an optional event listener to set in the OkHttp client.
     *
     * @param eventListener to set for OkHttp
     * @return this builder for chaining options together
     */
    public abstract Builder eventListener(EventListener eventListener);

    abstract Builder coordinates(@NonNull List<Point> coordinates);

    /**
     * Indicates from which side of the road to approach a waypoint.
     * Accepts  unrestricted (default), curb or null.
     * If set to  unrestricted , the route can approach waypoints
     * from either side of the road. If set to  curb , the route will be returned
     * so that on arrival, the waypoint will be found on the side that corresponds with the
     * driving_side of the region in which the returned route is located.
     * If provided, the list of approaches must be the same length as the list of waypoints.
     *
     * @param approaches null if you'd like the default approaches,
     *                   else one of the options found in
     *                   {@link com.mapbox.api.directions.v5.DirectionsCriteria.ApproachesCriteria}.
     * @return this builder for chaining options together
     * @since 3.2.0
     * @deprecated use {@link #approaches(List)}
     */
    @Deprecated
    public Builder addApproaches(@NonNull String... approaches) {
      return approaches(Arrays.asList(approaches));
    }

    /**
     * Indicates from which side of the road to approach a waypoint.
     * Accepts  unrestricted (default), curb or null.
     * If set to  unrestricted , the route can approach waypoints
     * from either side of the road. If set to  curb , the route will be returned
     * so that on arrival, the waypoint will be found on the side that corresponds with the
     * driving_side of the region in which the returned route is located.
     * If provided, the list of approaches must be the same length as the list of waypoints.
     *
     * @param approaches empty if you'd like the default approaches,
     *                   else one of the options found in
     *                   {@link com.mapbox.api.directions.v5.DirectionsCriteria.ApproachesCriteria}.
     * @return this builder for chaining options together
     */
    public Builder approaches(@NonNull List<String> approaches) {
      this.approaches = approaches;
      return this;
    }

    abstract Builder approaches(@Nullable String approaches);

    /**
     * Indicates from which side of the road to approach a waypoint.
     * Accepts  unrestricted (default), curb or null.
     * If set to  unrestricted , the route can approach waypoints
     * from either side of the road. If set to  curb , the route will be returned
     * so that on arrival, the waypoint will be found on the side that corresponds with the
     * driving_side of the region in which the returned route is located.
     * If provided, the list of approaches must be the same length as the list of waypoints.
     *
     * @param approach null if you'd like the default approaches, else one of the options found in
     *                 {@link com.mapbox.api.directions.v5.DirectionsCriteria.ApproachesCriteria}.
     * @return this builder for chaining options together
     * @since 3.2.0
     */
    public Builder addApproach(@Nullable String approach) {
      this.approaches.add(approach);
      return this;
    }

    /**
     * Optionally, set which input coordinates should be treated as waypoints / separate legs.
     * Note: coordinate indices not added here act as silent waypoints
     * <p>
     * Most useful in combination with  steps=true and requests based on traces
     * with high sample rates. Can be an index corresponding to any of the input coordinates,
     * but must contain the first ( 0 ) and last coordinates' indices.
     * {@link #steps()}
     * </p>
     *
     * @param waypointIndices integer array of coordinate indices to be used as waypoints
     * @return this builder for chaining options together
     * @since 4.4.0
     * @deprecated use {@link #waypointIndices(List)}
     */
    @Deprecated
    public Builder addWaypointIndices(@NonNull @IntRange(from = 0) Integer... waypointIndices) {
      return waypointIndices(Arrays.asList(waypointIndices));
    }

    /**
     * Optionally, set which input coordinates should be treated as waypoints / separate legs.
     * Note: coordinate indices not added here act as silent waypoints
     * <p>
     * Most useful in combination with  steps=true and requests based on traces
     * with high sample rates. Can be an index corresponding to any of the input coordinates,
     * but must contain the first ( 0 ) and last coordinates' indices.
     * {@link #steps()}
     * </p>
     *
     * @param waypointIndices a list of coordinate indices to be used as waypoints
     * @return this builder for chaining options together
     */
    public Builder waypointIndices(@NonNull List<Integer> waypointIndices) {
      this.waypointIndices = waypointIndices;
      return this;
    }

    abstract Builder waypointIndices(@Nullable String waypointIndices);

    /**
     * Optionally, set which input coordinates should be treated as waypoints / separate legs.
     * Note: coordinate indices not added here act as silent waypoints
     * <p>
     * Most useful in combination with  steps=true and requests based on traces
     * with high sample rates. Can be an index corresponding to any of the input coordinates,
     * but must contain the first ( 0 ) and last coordinates' indices.
     * {@link #steps()}
     * </p>
     *
     * @param waypointIndex integer index to be used as waypoint
     * @return this builder for chaining options together
     * @since 4.4.0
     */
    public Builder addWaypointIndex(@NonNull @IntRange(from = 0) Integer waypointIndex) {
      this.waypointIndices.add(waypointIndex);
      return this;
    }

    /**
     * Custom names for waypoints used for the arrival instruction.
     * Values can be any string and total number of all characters cannot exceed 500.
     * If provided, the list of waypointNames must be the same length as the list of
     * coordinates, but you can skip a coordinate and show its position with the null value.
     *
     * @param waypointNames Custom names for waypoints used for the arrival instruction.
     * @return this builder for chaining options together
     * @since 3.3.0
     * @deprecated use {@link #waypointNames(List)}
     */
    @Deprecated
    public Builder addWaypointNames(@NonNull String... waypointNames) {
      return waypointNames(Arrays.asList(waypointNames));
    }

    /**
     * Custom names for waypoints used for the arrival instruction.
     * Values can be any string and total number of all characters cannot exceed 500.
     * If provided, the list of waypointNames must be the same length as the list of
     * coordinates, but you can skip a coordinate and show its position with the null value.
     *
     * @param waypointNames a list of custom names for waypoints used for the arrival instruction.
     * @return this builder for chaining options together
     */
    public Builder waypointNames(@NonNull List<String> waypointNames) {
      this.waypointNames = waypointNames;
      return this;
    }

    abstract Builder waypointNames(@Nullable String waypointNames);

    /**
     * Custom names for waypoints used for the arrival instruction.
     * Values can be any string and total number of all characters cannot exceed 500.
     * If provided, the list of waypointNames must be the same length as the list of
     * coordinates, but you can skip a coordinate and show its position with the null value.
     *
     * @param waypointName Custom name for waypoint used for the arrival instruction.
     * @return this builder for chaining options together
     * @since 3.3.0
     */
    public Builder addWaypointName(@Nullable String waypointName) {
      this.waypointNames.add(waypointName);
      return this;
    }

    /**
     * Points to specify drop-off locations that are distinct from the locations specified in
     * coordinates.
     * The number of waypoint targets must be the same as the number of coordinates,
     * but you can skip a coordinate with a null value.
     * Must be used with steps=true.
     *
     * @param waypointTargets list of coordinate points for drop-off locations
     * @return this builder for chaining options together
     * @since 4.3.0
     * @deprecated use {@link #waypointTargets(List)}
     */
    @Deprecated
    public Builder addWaypointTargets(@NonNull Point... waypointTargets) {
      return waypointTargets(Arrays.asList(waypointTargets));
    }

    /**
     * A list of points used to specify drop-off locations that are distinct from the locations
     * specified in coordinates.
     * The number of waypoint targets must be the same as the number of coordinates,
     * but you can skip a coordinate with a null value.
     * Must be used with steps=true.
     *
     * @param waypointTargets list of coordinate points for drop-off locations
     * @return this builder for chaining options together
     */
    public Builder waypointTargets(@NonNull List<Point> waypointTargets) {
      this.waypointTargets = waypointTargets;
      return this;
    }

    abstract Builder waypointTargets(@Nullable String waypointTargets);

    /**
     * A point to specify drop-off locations that are distinct from the locations specified in
     * coordinates.
     * The number of waypoint targets must be the same as the number of coordinates,
     * but you can skip a coordinate with a null value.
     * Must be used with steps=true.
     *
     * @param waypointTarget a point for drop-off locations
     * @return this builder for chaining options together
     * @since 4.3.0
     */
    public Builder addWaypointTarget(@Nullable Point waypointTarget) {
      this.waypointTargets.add(waypointTarget);
      return this;
    }

    /**
     * A list of booleans affecting snapping of waypoint locations to road segments.
     * If true, road segments closed due to live-traffic closures will be considered for snapping.
     * If false, they will not be considered for snapping.
     * If provided, the number of snappingClosures must be the same as the number of
     * coordinates.
     * You can skip a coordinate and show its position in the list with null value.
     * Must be used with {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}
     *
     * @param snappingClosures a list of booleans
     * @return this builder for chaining options together
     */
    public Builder snappingClosures(@NonNull List<Boolean> snappingClosures) {
      this.snappingClosures = snappingClosures;
      return this;
    }

    abstract Builder snappingClosures(@Nullable String snappingClosures);

    /**
     * A boolean affecting snapping of waypoint location to the road segment.
     * If true, road segment closed due to live-traffic closure will be considered for snapping.
     * If false, it will not be considered for snapping.
     * If provided, the number of snappingClosures must be the same as the number of
     * coordinates.
     * You can skip a coordinate with null value.
     * Must be used with {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}
     *
     * @param snappingClosure a boolean affecting snapping of waypoint location to the road segment.
     * @return this builder for chaining options together
     * @since 5.9.0
     */
    public Builder addSnappingClosure(@Nullable Boolean snappingClosure) {
      this.snappingClosures.add(snappingClosure);
      return this;
    }

    /**
     * Whether the routes should be refreshable via the directions refresh API.
     *
     * @param enableRefresh whether the routes should be refreshable
     * @return this builder
     * @since 4.4.0
     */
    public abstract Builder enableRefresh(Boolean enableRefresh);

    /**
     * Use POST method to request data.
     * The default is to use GET.
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    public Builder post() {
      usePostMethod(true);
      return this;
    }

    /**
     * Use GET method to request data.
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    public Builder get() {
      usePostMethod(false);
      return this;
    }

    /**
     * To be used to specify settings for use with the walking profile.
     *
     * @param walkingOptions options to use for walking profile
     * @return this builder for chaining options together
     * @since 4.8.0
     */
    public abstract Builder walkingOptions(@NonNull WalkingOptions walkingOptions);

    abstract WalkingOptions walkingOptions();

    abstract Builder usePostMethod(@NonNull Boolean usePost);

    abstract Boolean usePostMethod();

    abstract MapboxDirections autoBuild();

    /**
     * This uses the provided parameters set using the {@link Builder} and first checks that all
     * values are valid, formats the values as strings for easier consumption by the API, and lastly
     * creates a new {@link MapboxDirections} object with the values provided.
     *
     * @return a new instance of Mapbox Directions
     * @since 2.1.0
     */
    public MapboxDirections build() {
      if (origin != null) {
        coordinates.add(0, origin);
      }
      if (destination != null) {
        coordinates.add(destination);
      }

      if (coordinates.size() < 2) {
        throw new ServicesException("An origin and destination are required before making the"
          + " directions API request.");
      }

      if (!waypointIndices.isEmpty()) {
        if (waypointIndices.size() < 2) {
          throw new ServicesException(
            "Waypoints must be a list of at least two indexes separated by ';'");
        }
        if (waypointIndices.get(0) != 0 || waypointIndices.get(waypointIndices.size() - 1)
          != coordinates.size() - 1) {
          throw new ServicesException(
            "Waypoints must contain indices of the first and last coordinates"
          );
        }
        for (int i = 1; i < waypointIndices.size() - 1; i++) {
          if (waypointIndices.get(i) < 0 || waypointIndices.get(i) >= coordinates.size()) {
            throw new ServicesException(
              "Waypoints index too large (no corresponding coordinate)");
          }
        }
      }

      if (!waypointNames.isEmpty()) {
        final String waypointNamesStr = FormatUtils.formatWaypointNames(waypointNames);
        waypointNames(waypointNamesStr);
      }

      if (!waypointTargets.isEmpty()) {
        if (waypointTargets.size() != coordinates.size()) {
          throw new ServicesException("Number of waypoint targets must match "
            + " the number of waypoints provided.");
        }

        waypointTargets(FormatUtils.formatPointsList(waypointTargets));
      }

      if (!approaches.isEmpty()) {
        if (approaches.size() != coordinates.size()) {
          throw new ServicesException("Number of approach elements must match "
            + "number of coordinates provided.");
        }
        String formattedApproaches = FormatUtils.formatApproaches(approaches);
        if (formattedApproaches == null) {
          throw new ServicesException("All approaches values must be one of curb, unrestricted");
        }
        approaches(formattedApproaches);
      }

      if (!snappingClosures.isEmpty()) {
        if (snappingClosures.size() != coordinates.size()) {
          throw new ServicesException("Number of snapping closures elements must match "
              + "number of coordinates provided.");
        }
        snappingClosures(FormatUtils.join(";", snappingClosures));
      }

      coordinates(coordinates);
      bearing(FormatUtils.formatBearings(bearings));
      annotation(FormatUtils.join(",", annotations));
      radius(FormatUtils.formatRadiuses(radiuses));
      waypointIndices(FormatUtils.join(";", waypointIndices, true));

      MapboxDirections directions = autoBuild();

      if (!MapboxUtils.isAccessTokenValid(directions.accessToken())) {
        throw new ServicesException("Using Mapbox Services requires setting a valid access"
          + " token.");
      }
      return directions;
    }
  }
}
