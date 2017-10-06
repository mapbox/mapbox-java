package com.mapbox.services.api.directionsmatrix.v1;

import com.mapbox.services.api.MapboxBuilder;
import com.mapbox.services.api.MapboxService;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directionsmatrix.v1.models.DirectionsMatrixResponse;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The Directions Matrix API returns all travel times between many points. The Matrix API will always return the
 * duration on the fastest route. Durations between points may not be symmetric (for example A to B may have a
 * different duration than B to A), as the routes may differ by direction due to one-way streets or turn restrictions.
 * The Matrix API returns durations in seconds. It does not return route geometries or distances.
 * <p>
 * This API allows you to build tools that efficiently check the reachability of coordinates from each other, filter
 * points by travel time, or run your own algorithms for solving optimization problems.
 * <p>
 * The standard limit for request are a maximum 60 requests per minute and maximum 25 input coordinates. For example
 * you can request a symmetric 25x25 matrix, an asymmetric 1x24 matrix with distinct coordinates or a 12x24 where
 * sources and destinations share some coordinates. For higher volumes contact us.
 * <p>
 * This replace the distance.v1 API
 *
 * @see <a href="https://www.mapbox.com/api-documentation/#directions-matrix">API documentation</a>
 * @since 2.1.0
 */
public class MapboxDirectionsMatrix extends MapboxService<DirectionsMatrixResponse> {

  protected Builder builder = null;
  private DirectionsMatrixService service = null;
  private Call<DirectionsMatrixResponse> call = null;

  protected MapboxDirectionsMatrix(Builder builder) {
    this.builder = builder;
  }

  private DirectionsMatrixService getService() {
    // No need to recreate it
    if (service != null) {
      return service;
    }

    // Retrofit instance
    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
      .baseUrl(builder.getBaseUrl())
      .addConverterFactory(GsonConverterFactory.create());
    if (getCallFactory() != null) {
      retrofitBuilder.callFactory(getCallFactory());
    } else {
      retrofitBuilder.client(getOkHttpClient());
    }

    // Directions service
    service = retrofitBuilder.build().create(DirectionsMatrixService.class);
    return service;
  }

  private Call<DirectionsMatrixResponse> getCall() {
    // No need to recreate it
    if (call != null) {
      return call;
    }

    call = getService().getCall(
      getHeaderUserAgent(builder.getClientAppName()),
      builder.getUser(),
      builder.getProfile(),
      builder.getCoordinates(),
      builder.getAccessToken(),
      builder.getDestinations(),
      builder.getSources());

    // Done
    return call;
  }

  /**
   * Execute the call
   *
   * @return The Directions Matrix v1 response
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   * @since 2.1.0
   */
  @Override
  public Response<DirectionsMatrixResponse> executeCall() throws IOException {
    return getCall().execute();
  }

  /**
   * Execute the call
   *
   * @param callback A Retrofit callback.
   * @since 2.1.0
   */
  @Override
  public void enqueueCall(Callback<DirectionsMatrixResponse> callback) {
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
  public Call<DirectionsMatrixResponse> cloneCall() {
    return getCall().clone();
  }

  /**
   * Directions Matrix v1 builder
   *
   * @since 2.1.0
   */
  public static class Builder<T extends Builder> extends MapboxBuilder {

    private String user = null;
    private List<Position> coordinates = null;
    private String accessToken = null;
    private String profile = null;
    private int[] sources = null;
    private int[] destinations = null;

    /**
     * Constructor
     *
     * @since 2.1.0
     */
    public Builder() {
      // Set defaults
      this.user = DirectionsCriteria.PROFILE_DEFAULT_USER;
    }

    /*
     * Setters
     */

    /**
     * @param user User string
     * @return Builder
     * @since 2.1.0
     */
    public T setUser(String user) {
      this.user = user;
      return (T) this;
    }

    /**
     * @param profile Profile string
     * @return Builder
     * @since 2.1.0
     */
    public T setProfile(String profile) {
      this.profile = profile;
      return (T) this;
    }

    /**
     * Set the list of coordinates for the directions matrix service. If you've previously set an
     * origin with setOrigin() or a destination with setDestination(), those will be
     * overridden.
     *
     * @param coordinates List of {@link Position} giving origin and destination(s) coordinates.
     * @return Builder
     * @since 2.1.0
     */
    public T setCoordinates(List<Position> coordinates) {
      this.coordinates = coordinates;
      return (T) this;
    }

    /**
     * Optionally pass in indexes to generate an asymmetric matrix.
     *
     * @param destinations 1 or more indexes as a integer, if more then one, separate with a comma.
     * @return Builder
     * @since 2.1.0
     */
    public T setDestinations(int... destinations) {
      this.destinations = destinations;
      return (T) this;
    }

    /**
     * Optionally pass in indexes to generate an asymmetric matrix.
     *
     * @param sources 1 or more indexes as a integer, if more then one, separate with a comma.
     * @return Builder
     * @since 2.1.0
     */
    public T setSources(int... sources) {
      this.sources = sources;
      return (T) this;
    }

    /**
     * Inserts the specified position at the beginning of the coordinates list. If you've
     * set other coordinates previously with setCoordinates() those elements are kept
     * and their index will be moved up by one (the coordinates are moved to the right).
     *
     * @param origin {@link Position} of route origin.
     * @return Builder
     * @since 2.1.0
     */
    public T setOrigin(Position origin) {
      if (coordinates == null) {
        coordinates = new ArrayList<>();
      }

      // The default behavior of ArrayList is to inserts the specified element at the
      // specified position in this list (beginning) and to shift the element currently at
      // that position (if any) and any subsequent elements to the right (adds one to
      // their indices)
      coordinates.add(0, origin);

      return (T) this;
    }

    /**
     * Appends the specified destination to the end of the coordinates list. If you've
     * set other coordinates previously with setCoordinates() those elements are kept
     * and the destination is added at the end of the list.
     *
     * @param destination {@link Position} of route destination.
     * @return Builder
     * @since 2.1.0
     */
    public T setDestination(Position destination) {
      if (coordinates == null) {
        coordinates = new ArrayList<>();
      }

      // The default behavior for ArrayList is to appends the specified element
      // to the end of this list.
      coordinates.add(destination);

      return (T) this;
    }

    /**
     * Required to call when building {@link Builder}.
     *
     * @param accessToken Mapbox access token, You must have a Mapbox account inorder to use
     *                    this library.
     * @return Builder
     * @since 2.1.0
     */
    @Override
    public T setAccessToken(String accessToken) {
      this.accessToken = accessToken;
      return (T) this;
    }

    public T setBaseUrl(String baseUrl) {
      super.baseUrl = baseUrl;
      return (T) this;
    }

    public T setClientAppName(String appName) {
      super.clientAppName = appName;
      return (T) this;
    }

    /*
     * Getters
     */

    /**
     * @return A formatted string that contains all the destinations integers you used insider your request.
     * @since 2.1.0
     */
    public String getDestinations() {
      if (destinations == null || destinations.length == 0) {
        return null;
      }

      String[] destinationsFormatted = new String[destinations.length];

      for (int i = 0; i < destinations.length; i++) {
        destinationsFormatted[i] = String.valueOf(destinations[i]);
      }

      return TextUtils.join(";", destinationsFormatted);
    }

    /**
     * @return A formatted string that contains all the source integers you used insider your request.
     * @since 2.1.0
     */
    public String getSources() {
      if (sources == null || sources.length == 0) {
        return null;
      }

      String[] sourcesFormatted = new String[sources.length];

      for (int i = 0; i < sources.length; i++) {
        sourcesFormatted[i] = String.valueOf(sources[i]);
      }

      return TextUtils.join(";", sourcesFormatted);
    }

    /**
     * The coordinates parameter denotes the points the API will consider happens. The coordinates
     * must be in the format:
     * <p>
     * {longitude},{latitude};{longitude},{latitude}[;{longitude},{latitude} ...]
     * <p>
     * - Each coordinate is a pair of a longitude double and latitude double, which are separated by a ,
     * - Coordinates are separated by a ; from each other
     * - A query must at minimum have 2 coordinates and may at maximum have 25 coordinates
     *
     * @return String containing coordinates formatted.
     * @since 2.1.0
     */
    public String getCoordinates() {
      List<String> coordinatesFormatted = new ArrayList<>();
      for (Position coordinate : coordinates) {
        coordinatesFormatted.add(String.format(Locale.US, "%f,%f",
          coordinate.getLongitude(),
          coordinate.getLatitude()));
      }

      return TextUtils.join(";", coordinatesFormatted.toArray());
    }

    /**
     * @return the user as String
     * @since 2.1.0
     */
    public String getUser() {
      return user;
    }

    /**
     * @return {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#PROFILE_DRIVING},
     * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#PROFILE_CYCLING},
     * or {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#PROFILE_WALKING}
     * @since 2.1.0
     */
    public String getProfile() {
      return profile;
    }

    /**
     * @return your Mapbox access token.
     * @since 2.1.0
     */
    @Override
    public String getAccessToken() {
      return accessToken;
    }

    /**
     * Build method
     *
     * @return MapboxDirectionsMatrix
     * @throws ServicesException Generic Exception for all things directions matrix.
     * @since 2.1.0
     */
    @Override
    public MapboxDirectionsMatrix build() throws ServicesException {
      validateAccessToken(accessToken);

      if (profile == null) {
        throw new ServicesException(
          "A profile is required for the Directions Matrix API. Use one of the profiles inside the"
            + "DirectionsCriteria.");
      }

      if (coordinates == null || coordinates.size() < 2) {
        throw new ServicesException(
          "You should provide at least two coordinates (from/to).");
      }

      if (profile.equals(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)) {
        throw new ServicesException(
          "The Directions Matrix doesn't support the driving traffic profile.");
      }

      if (!profile.equals(DirectionsCriteria.PROFILE_DRIVING)
        && !profile.equals(DirectionsCriteria.PROFILE_CYCLING)
        && !profile.equals(DirectionsCriteria.PROFILE_WALKING)) {
        throw new ServicesException("Profile must be one of the values found inside the DirectionsCriteria.");
      }

      if (coordinates.size() > 25) {
        throw new ServicesException(
          "All profiles allow for a maximum of 25 coordinates.");
      }

      return new MapboxDirectionsMatrix(this);
    }
  }
}
