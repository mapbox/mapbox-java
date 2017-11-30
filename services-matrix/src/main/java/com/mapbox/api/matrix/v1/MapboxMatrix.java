package com.mapbox.api.matrix.v1;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.api.matrix.v1.models.MatrixResponse;
import com.mapbox.core.MapboxService;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.core.utils.ApiCallHelper;
import com.mapbox.core.utils.MapboxUtils;
import com.mapbox.core.utils.TextUtils;

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
 * the Matrix API returns all travel times between many points. The Matrix API will always return
 * the duration on the fastest route. Durations between points may not be symmetric (for example A
 * to B may have a different duration than B to A), as the routes may differ by direction due to
 * one-way streets or turn restrictions. The Matrix API returns durations in seconds. It does not
 * return route geometries or distances.
 * <p>
 * This API allows you to build tools that efficiently check the reachability of coordinates from
 * each other, filter points by travel time, or run your own algorithms for solving optimization
 * problems.
 * <p>
 * The standard limit for request are a maximum 60 requests per minute and maximum 25 input
 * coordinates. For example you can request a symmetric 25x25 matrix, an asymmetric 1x24 matrix with
 * distinct coordinates or a 12x24 where sources and destinations share some coordinates. For higher
 * volumes contact us.
 * <p>
 *
 * @see <a href="https://www.mapbox.com/api-documentation/#matrix">API documentation</a>
 * @since 2.1.0
 */
@AutoValue
public abstract class MapboxMatrix extends MapboxService<MatrixResponse> {

  private MatrixService service;
  private Call<MatrixResponse> call;

  /**
   * Wrapper method for Retrofits {@link Call#execute()} call returning a response specific to the
   * Matrix API.
   *
   * @return the Matrix v5 response once the call completes successfully
   * @throws IOException Signals that an I/O exception of some sort has occurred
   * @since 2.1.0
   */
  @Override
  public Response<MatrixResponse> executeCall() throws IOException {
    return getCall().execute();
  }

  /**
   * Wrapper method for Retrofits {@link Call#enqueue(Callback)} call returning a response specific
   * to the Matrix API. Use this method to make a Matrix request on the Main Thread.
   *
   * @param callback a {@link Callback} which is used once the {@link MatrixResponse} is created.
   * @since 2.1.0
   */
  @Override
  public void enqueueCall(Callback<MatrixResponse> callback) {
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
   * @since 2.1.0
   */
  @Override
  public Call<MatrixResponse> cloneCall() {
    return getCall().clone();
  }

  private MatrixService getService() {
    // No need to recreate it
    if (service != null) {
      return service;
    }

    // Retrofit instance
    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
      .baseUrl(baseUrl())
      .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
        .registerTypeAdapterFactory(MatrixAdapterFactory.create())
        .registerTypeAdapterFactory(DirectionsAdapterFactory.create())
        .create()));
    if (getCallFactory() != null) {
      retrofitBuilder.callFactory(getCallFactory());
    } else {
      retrofitBuilder.client(getOkHttpClient());
    }

    // Directions service
    service = retrofitBuilder.build().create(MatrixService.class);
    return service;
  }

  private Call<MatrixResponse> getCall() {
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
      destinations(),
      sources());

    // Done
    return call;
  }

  @Nullable
  abstract String clientAppName();

  @NonNull
  abstract String user();

  @NonNull
  abstract String coordinates();

  @NonNull
  abstract String accessToken();

  @NonNull
  abstract String profile();

  @Nullable
  abstract String sources();

  @Nullable
  abstract String destinations();

  @NonNull
  abstract String baseUrl();

  /**
   * Build a new {@link MapboxMatrix} object with the initial values set for {@link #baseUrl()},
   * {@link #profile()}, and {@link #user()}.
   *
   * @return a {@link Builder} object for creating this object
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxMatrix.Builder()
      .baseUrl(Constants.BASE_API_URL)
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .user(DirectionsCriteria.PROFILE_DEFAULT_USER);
  }

  /**
   * This builder is used to create a new request to the Mapbox Matrix API. At a bare minimum,
   * your request must include an access token, and a list of coordinates. All other fields can
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

    private List<Point> coordinates = new ArrayList<>();
    private Integer[] destinations;
    private Integer[] sources;

    /**
     * The username for the account that the directions engine runs on. In most cases, this should
     * always remain the default value of {@link DirectionsCriteria#PROFILE_DEFAULT_USER}.
     *
     * @param user a non-null string which will replace the default user used in the directions
     *             request
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder user(String user);

    /**
     * Add a list of {@link Point}'s which define the points to perform the matrix on. The minimum
     * points is 2 and the maximum points allowed in totals 25. You can use this method in
     * conjunction with {@link #coordinate(Point)}.
     *
     * @param coordinates a List full of {@link Point}s which define the points to perform the
     *                    matrix on
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public Builder coordinates(List<Point> coordinates) {
      this.coordinates.addAll(coordinates);
      return this;
    }

    // Required for matching with MapboxMatrix coordinates() method.
    abstract Builder coordinates(@NonNull String coordinates);

    /**
     * This will add a single {@link Point} to the coordinate list which is used to determine the
     * duration between points. This can be called up to 25 times until you hit the maximum allowed
     * points. You can use this method in conjunction with {@link #coordinates(List)}.
     *
     * @param coordinate a {@link Point} which you'd like the duration between all other points
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder coordinate(@NonNull Point coordinate) {
      this.coordinates.add(coordinate);
      return this;
    }

    /**
     * This selects which mode of transportation the user will be using to accurately give the
     * matrix durations. The options include driving, driving considering traffic, walking, and
     * cycling. Using each of these profiles will result in different durations
     *
     * @param profile required to be one of the String values found in the {@link DirectionsCriteria.ProfileCriteria}
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder profile(@NonNull @DirectionsCriteria.ProfileCriteria String profile);

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
     * Optionally pass in indexes to generate an asymmetric matrix.
     *
     * @param destinations 1 or more indexes as a integer, if more then one, separate with a comma
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public Builder destinations(@Nullable Integer... destinations) {
      this.destinations = destinations;
      return this;
    }

    // Required for matching with MapboxMatrix destinations() method.
    abstract Builder destinations(@Nullable String destinations);

    /**
     * Optionally pass in indexes to generate an asymmetric matrix.
     *
     * @param sources 1 or more indexes as a integer, if more then one, separate with a comma
     * @return Builder
     * @since 2.1.0
     */
    public Builder sources(@Nullable Integer... sources) {
      this.sources = sources;
      return this;
    }

    // Required for matching with MapboxMatrix sources() method.
    abstract Builder sources(@Nullable String sources);

    /**
     * Base package name or other simple string identifier. Used inside the calls user agent header.
     *
     * @param clientAppName base package name or other simple string identifier
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder clientAppName(@NonNull String clientAppName);

    /**
     * Optionally change the APIs base URL to something other then the default Mapbox one.
     *
     * @param baseUrl base url used as end point
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder baseUrl(@NonNull String baseUrl);

    abstract MapboxMatrix autoBuild();

    /**
     * This uses the provided parameters set using the {@link Builder} and first checks that all
     * values are valid, formats the values as strings for easier consumption by the API, and lastly
     * creates a new {@link MapboxMatrix} object with the values provided.
     *
     * @return a new instance of Mapbox Matrix
     * @since 2.1.0
     */
    public MapboxMatrix build() {
      if (coordinates == null || coordinates.size() < 2) {
        throw new ServicesException("At least two coordinates must be provided with your API"
          + " request.");
      } else if (coordinates.size() > 25) {
        throw new ServicesException("Maximum of 25 coordinates are allowed for this API.");
      }

      coordinates(formatCoordinates(coordinates));

      sources(TextUtils.join(";", sources));
      destinations(TextUtils.join(";", destinations));

      // Generate build so that we can check that values are valid.
      MapboxMatrix matrix = autoBuild();

      if (!MapboxUtils.isAccessTokenValid(matrix.accessToken())) {
        throw new ServicesException("Using Mapbox Services requires setting a valid access token.");
      }
      return matrix;
    }
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
