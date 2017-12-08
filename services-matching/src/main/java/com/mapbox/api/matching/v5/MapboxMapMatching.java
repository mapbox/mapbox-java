package com.mapbox.api.matching.v5;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.DirectionsCriteria.AnnotationCriteria;
import com.mapbox.api.directions.v5.DirectionsCriteria.GeometriesCriteria;
import com.mapbox.api.directions.v5.DirectionsCriteria.OverviewCriteria;
import com.mapbox.api.directions.v5.DirectionsCriteria.ProfileCriteria;
import com.mapbox.api.matching.v5.models.MapMatchingAdapterFactory;
import com.mapbox.api.matching.v5.models.MapMatchingResponse;
import com.mapbox.core.MapboxService;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.core.utils.ApiCallHelper;
import com.mapbox.core.utils.MapboxUtils;
import com.mapbox.core.utils.TextUtils;
import com.mapbox.geojson.Point;
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
 * The Mapbox map matching interface (v5)
 * <p>
 * The Mapbox Map Matching API snaps fuzzy, inaccurate traces from a GPS unit or a phone to the
 * OpenStreetMap road and path network using the Directions API. This produces clean paths that can
 * be displayed on a map or used for other analysis.
 *
 * @see <a href="https://www.mapbox.com/api-documentation/#map-matching">Map matching API
 *   documentation</a>
 * @since 2.0.0
 */
@AutoValue
public abstract class MapboxMapMatching extends MapboxService<MapMatchingResponse> {

  private Call<MapMatchingResponse> call;
  private MapMatchingService service;

  private MapMatchingService getService() {
    // No need to recreate it
    if (service != null) {
      return service;
    }

    // Retrofit instance
    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
      .baseUrl(baseUrl())
      .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
        .registerTypeAdapterFactory(MapMatchingAdapterFactory.create())
        .registerTypeAdapterFactory(DirectionsAdapterFactory.create())
        .create()));
    if (getCallFactory() != null) {
      retrofitBuilder.callFactory(getCallFactory());
    } else {
      retrofitBuilder.client(getOkHttpClient());
    }

    // MapMatching service
    service = retrofitBuilder.build().create(MapMatchingService.class);
    return service;
  }

  /**
   * Used internally.
   *
   * @return call
   * @since 2.0.0
   */
  public Call<MapMatchingResponse> getCall() {
    // No need to recreate it
    if (call != null) {
      return call;
    }

    call = getService().getCall(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      user(),
      profile(),
      coordinates(),
      accessToken(),
      geometries(),
      radiuses(),
      steps(),
      overview(),
      timestamps(),
      annotations(),
      language(),
      tidy()
    );

    return call;
  }

  /**
   * Wrapper method for Retrofits {@link Call#execute()} call returning a response specific to the
   * Map Matching API.
   *
   * @return the Map Matching v5 response once the call completes successfully
   * @throws IOException Signals that an I/O exception of some sort has occurred
   * @since 1.0.0
   */
  @Override
  public Response<MapMatchingResponse> executeCall() throws IOException {
    return getCall().execute();
  }

  /**
   * Wrapper method for Retrofits {@link Call#enqueue(Callback)} call returning a response specific
   * to the Map Matching API. Use this method to make a directions request on the Main Thread.
   *
   * @param callback a {@link Callback} which is used once the {@link MapMatchingResponse} is
   *                 created.
   * @since 1.0.0
   */
  @Override
  public void enqueueCall(Callback<MapMatchingResponse> callback) {
    getCall().enqueue(callback);
  }

  /**
   * Wrapper method for Retrofits {@link Call#cancel()} call, important to manually cancel call if
   * the user dismisses the calling activity or no longer needs the returned results.
   *
   * @since 1.0.0
   */
  @Override
  public void cancelCall() {
    getCall().cancel();
  }

  /**
   * Wrapper method for Retrofits {@link Call#clone()} call, useful for getting call information.
   *
   * @return cloned call
   * @since 1.0.0
   */
  @Override
  public Call<MapMatchingResponse> cloneCall() {
    return getCall().clone();
  }

  @Nullable
  abstract String clientAppName();

  @Nullable
  abstract String accessToken();

  @Nullable
  abstract Boolean tidy();

  @NonNull
  abstract String user();

  @NonNull
  abstract String profile();

  @NonNull
  abstract String coordinates();

  @Nullable
  abstract String geometries();

  @Nullable
  abstract String radiuses();

  @Nullable
  abstract Boolean steps();

  @Nullable
  abstract String overview();

  @Nullable
  abstract String timestamps();

  @Nullable
  abstract String annotations();

  @Nullable
  abstract String language();

  @NonNull
  abstract String baseUrl();

  /**
   * Build a new {@link MapboxMapMatching} object with the initial values set for
   * {@link #baseUrl()}, {@link #profile()}, {@link #geometries()}, and {@link #user()}.
   *
   * @return a {@link Builder} object for creating this object
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxMapMatching.Builder()
      .baseUrl(Constants.BASE_API_URL)
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .user(DirectionsCriteria.PROFILE_DEFAULT_USER);
  }

  /**
   * Builds your map matching query by adding parameters.
   *
   * @since 2.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    private List<Point> coordinates = new ArrayList<>();
    private String[] annotations;
    private String[] timestamps;
    private Double[] radiuses;

    /**
     * Required to call when this is being built. If no access token provided,
     * {@link ServicesException} will be thrown.
     *
     * @param accessToken Mapbox access token, You must have a Mapbox account inorder to use
     *                    the Map Matching API
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder accessToken(@NonNull String accessToken);

    /**
     * Whether or not to transparently remove clusters and re-sample traces for improved map
     * matching results. Pass in null to reset to the APIs default setting.
     *
     * @param tidy true if you'd like the API to remove coordinates clustered together, otherwise
     *             false
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder tidy(@Nullable Boolean tidy);

    /**
     * The username for the account that the directions engine runs on. In most cases, this should
     * always remain the default value of {@link DirectionsCriteria#PROFILE_DEFAULT_USER}.
     *
     * @param user a non-null string which will replace the default user used in the map matching
     *             request
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder user(@NonNull String user);

    /**
     * This selects which mode of transportation the user will be using to accurately give the
     * map matching route. The options include driving, driving considering traffic, walking, and
     * cycling. Using each of these profiles will result in different durations
     *
     * @param profile required to be one of the String values found in the {@link ProfileCriteria}
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder profile(@NonNull @ProfileCriteria String profile);

    /**
     * alter the default geometry being returned for the map matching route. A null value will reset
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
    public abstract Builder geometries(@Nullable @GeometriesCriteria String geometries);

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
     */
    public Builder radiuses(@Nullable @FloatRange(from = 0) Double... radiuses) {
      this.radiuses = radiuses;
      return this;
    }

    // Required for matching with MapboxMapMatching radiuses() method.
    abstract Builder radiuses(@Nullable String radiuses);

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
     * Type of returned overview geometry. Can be {@link DirectionsCriteria#OVERVIEW_FULL} (the most
     * detailed geometry available), {@link DirectionsCriteria#OVERVIEW_SIMPLIFIED} (a simplified
     * version of the full geometry), or {@link DirectionsCriteria#OVERVIEW_FALSE} (no overview
     * geometry). The default is simplified. Passing in null will use the APIs default setting for
     * the overview field.
     *
     * @param overview null or one of the options found in {@link OverviewCriteria}
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder overview(@Nullable @OverviewCriteria String overview);

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
     *   documentation</a>
     * @since 2.1.0
     */
    public Builder annotations(@Nullable @AnnotationCriteria String... annotations) {
      this.annotations = annotations;
      return this;
    }

    // Required for matching with MapboxMapMatching annotations() method.
    @SuppressWarnings("WeakerAccess")
    protected abstract Builder annotations(@Nullable String annotations);

    /**
     * Timestamps corresponding to each coordinate provided in the request; must be numbers in Unix
     * time (seconds since the Unix epoch) converted to a String. There must be as many timestamps
     * as there are coordinates in the request, each separated by {@code ;} .
     *
     * @param timestamps timestamp corresponding to the coordinate added at the identical index
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public Builder timestamps(@Nullable String... timestamps) {
      this.timestamps = timestamps;
      return this;
    }

    // Required for matching with MapboxMapMatching timestamps() method.
    @SuppressWarnings("WeakerAccess")
    protected abstract Builder timestamps(@Nullable String timestamps);

    /**
     * Add a list of {@link Point}'s which define the points to perform the map matching on. The
     * minimum points is 2 and the maximum points allowed in totals 100. You can use this method in
     * conjunction with {@link #coordinate(Point)}.
     *
     * @param coordinates a List full of {@link Point}s which define the points to perform the map
     *                    matching on
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public Builder coordinates(@NonNull List<Point> coordinates) {
      this.coordinates.addAll(coordinates);
      return this;
    }

    // Required for matching with MapboxMapMatching coordinates() method.
    @SuppressWarnings("WeakerAccess")
    protected abstract Builder coordinates(@NonNull String coordinates);

    /**
     * This will add a single {@link Point} to the coordinate list which is used to determine the
     * duration between points. This can be called up to 100 times until you hit the maximum allowed
     * points. You can use this method in conjunction with {@link #coordinates(List)}.
     *
     * @param coordinate a {@link Point} which you'd like the map matching APi to perform on
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder coordinate(@NonNull Point coordinate) {
      this.coordinates.add(coordinate);
      return this;
    }

    /**
     * Set the instruction language for the map matching request, the default is english. Only a
     * select number of languages are currently supported, reference the table provided in the see
     * link below.
     *
     * @param language a Locale value representing the language you'd like the instructions to be
     *                 written in when returned
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/#instructions-languages">Supported
     *   Languages</a>
     * @since 3.0.0
     */
    public Builder language(@Nullable Locale language) {
      if (language != null) {
        language(language.getLanguage());
      }
      return this;
    }

    /**
     * Set the instruction language for the map matching request, the default is english. Only a
     * select number of languages are currently supported, reference the table provided in the see
     * link below.
     *
     * @param language a String value representing the language you'd like the instructions to be
     *                 written in when returned
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/#instructions-languages">Supported
     *   Languages</a>
     * @since 2.2.0
     */
    public abstract Builder language(String language);

    /**
     * Base package name or other simple string identifier. Used inside the calls user agent header.
     *
     * @param clientAppName base package name or other simple string identifier
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder clientAppName(@NonNull String clientAppName);

    /**
     * Optionally change the APIs base URL to something other then the default Mapbox one.
     *
     * @param baseUrl base url used as end point
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder baseUrl(String baseUrl);

    @SuppressWarnings("WeakerAccess")
    protected abstract MapboxMapMatching autoBuild();

    /**
     * This uses the provided parameters set using the {@link Builder} and first checks that all
     * values are valid, formats the values as strings for easier consumption by the API, and lastly
     * creates a new {@link MapboxMapMatching} object with the values provided.
     *
     * @return a new instance of Mapbox Map Matching
     * @throws ServicesException when a provided parameter is detected to be incorrect
     * @since 2.1.0
     */
    public MapboxMapMatching build() {
      if (coordinates == null || coordinates.size() < 2) {
        throw new ServicesException("At least two coordinates must be provided with your API"
          + " request.");
      }

      if (coordinates.size() > 100) {
        throw new ServicesException("Maximum of 100 coordinates are allowed for this API.");
      }

      if (radiuses != null && radiuses.length != coordinates.size()) {
        throw new ServicesException(
          "There must be as many radiuses as there are coordinates.");
      }

      if (timestamps != null && timestamps.length != coordinates.size()) {
        throw new ServicesException(
          "There must be as many timestamps as there are coordinates.");
      }

      coordinates(formatCoordinates(coordinates));
      timestamps(TextUtils.join(",", timestamps));
      annotations(TextUtils.join(",", annotations));
      radiuses(TextUtils.join(",", radiuses));

      // Generate build so that we can check that values are valid.
      MapboxMapMatching mapMatching = autoBuild();

      if (!MapboxUtils.isAccessTokenValid(mapMatching.accessToken())) {
        throw new ServicesException("Using Mapbox Services requires setting a valid access token.");
      }
      return mapMatching;
    }

    private static String formatCoordinates(List<Point> coordinates) {
      List<String> coordinatesFormatted = new ArrayList<>();
      for (Point point : coordinates) {
        coordinatesFormatted.add(String.format(Locale.US, "%s,%s",
          TextUtils.formatCoordinate(point.longitude()),
          TextUtils.formatCoordinate(point.latitude())));
      }

      return TextUtils.join(";", coordinatesFormatted.toArray());
    }
  }
}
