package com.mapbox.services.api.optimizedtrips.v1;

import com.mapbox.services.api.MapboxBuilder;
import com.mapbox.services.api.MapboxService;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.optimizedtrips.v1.models.OptimizedTripsResponse;
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
 * The Mapbox Optimized Trips API returns a duration-optimized trip between the input coordinates. This is also known
 * as solving the Traveling Salesperson Problem. A typical use case for this API is planning the route for deliveries
 * in a city. Optimized trips can be retrieved for car driving, bicycling and walking or hiking.
 * <p>
 * Under normal plans, a maximum of 12 coordinates can be passed in at once at a maximum 60 requests per minute. For
 * higher volumes, reach out through our contact page.
 * <p>
 * Note that for under 10 coordinates, the returned results will be optimal. For 10 and more coordinates, the results
 * will be optimized approximations.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Travelling_salesman_problem">Traveling Salesperson Problem</a>
 * @see <a href="https://www.mapbox.com/api-documentation/#optimized-trips">API documentation</a>
 * @since 2.1.0
 */
public class MapboxOptimizedTrips extends MapboxService<OptimizedTripsResponse> {

  protected Builder builder = null;
  private OptimizedTripsService service = null;
  private Call<OptimizedTripsResponse> call = null;

  protected MapboxOptimizedTrips(Builder builder) {
    this.builder = builder;
  }

  private OptimizedTripsService getService() {
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
    service = retrofitBuilder.build().create(OptimizedTripsService.class);
    return service;
  }

  private Call<OptimizedTripsResponse> getCall() {
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
      builder.getRoundTrip(),
      builder.getRadiuses(),
      builder.getBearings(),
      builder.getSteps(),
      builder.getOverview(),
      builder.getGeometries(),
      builder.getAnnotation(),
      builder.getDestination(),
      builder.getSource());

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
  public Response<OptimizedTripsResponse> executeCall() throws IOException {
    return getCall().execute();
  }

  /**
   * Execute the call
   *
   * @param callback A Retrofit callback.
   * @since 2.1.0
   */
  @Override
  public void enqueueCall(Callback<OptimizedTripsResponse> callback) {
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
  public Call<OptimizedTripsResponse> cloneCall() {
    return getCall().clone();
  }

  /**
   * Optimized Trips v1 builder
   *
   * @since 2.1.0
   */
  public static class Builder<T extends Builder> extends MapboxBuilder {

    private String user;
    private String accessToken;
    private String profile;
    private List<Position> coordinates;
    private Boolean roundTrip;
    private String source;
    private String destination;
    private String geometries;
    private double[] radiuses;
    private Boolean steps;
    private double[][] bearings;
    private String[] annotation;
    private String overview;

    /**
     * Constructor
     *
     * @since 2.1.0
     */
    public Builder() {
      // Set defaults
      this.user = DirectionsCriteria.PROFILE_DEFAULT_USER;

      // by defauly the geometry is polyline with precision 6.
      this.geometries = DirectionsCriteria.GEOMETRY_POLYLINE6;
    }

    /*
     * Setters
     */

    /**
     * Required to call when building {@link Builder}.
     *
     * @param accessToken Mapbox access token, You must have a Mapbox account inorder to use
     *                    the Optimized Trip API.
     * @return Builder
     * @since 2.1.0
     */
    @Override
    public T setAccessToken(String accessToken) {
      this.accessToken = accessToken;
      return (T) this;
    }

    public T setUser(String user) {
      this.user = user;
      return (T) this;
    }

    public T setProfile(String profile) {
      this.profile = profile;
      return (T) this;
    }

    public T setCoordinates(List<Position> coordinates) {
      this.coordinates = coordinates;
      return (T) this;
    }

    public T setRoundTrip(Boolean roundTrip) {
      this.roundTrip = roundTrip;
      return (T) this;
    }

    public T setSource(String source) {
      this.source = source;
      return (T) this;
    }

    public T setDestination(String destination) {
      this.destination = destination;
      return (T) this;
    }

    public T setGeometries(String geometries) {
      this.geometries = geometries;
      return (T) this;
    }

    public T setRadiuses(double[] radiuses) {
      this.radiuses = radiuses;
      return (T) this;
    }

    public T setOverview(String overview) {
      this.overview = overview;
      return (T) this;
    }

    public T setSteps(Boolean steps) {
      this.steps = steps;
      return (T) this;
    }

    public T setBearings(double[]... bearings) {
      this.bearings = bearings;
      return (T) this;
    }

    public T setAnnotation(String... annotation) {
      this.annotation = annotation;
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

    public String getProfile() {
      return profile;
    }

    public Boolean getRoundTrip() {
      return roundTrip;
    }

    public String getSource() {
      return source;
    }

    public String getDestination() {
      return destination;
    }

    public String getCoordinates() {
      List<String> coordinatesFormatted = new ArrayList<>();
      for (Position coordinate : coordinates) {
        coordinatesFormatted.add(String.format(Locale.US, "%f,%f",
          coordinate.getLongitude(),
          coordinate.getLatitude()));
      }

      return TextUtils.join(";", coordinatesFormatted.toArray());
    }

    public String getGeometries() {
      return geometries;
    }

    public double[] getRadiuses() {
      return radiuses;
    }

    public String getUser() {
      return user;
    }

    public String getOverview() {
      return overview;
    }

    public Boolean getSteps() {
      return steps;
    }

    public double[][] getBearings() {
      return bearings;
    }

    public String[] getAnnotation() {
      return annotation;
    }

    /**
     * @return your Mapbox access token.
     * @since 2.1.0
     */
    @Override
    public String getAccessToken() {
      return accessToken;
    }

    @Override
    public MapboxOptimizedTrips build() throws ServicesException {
      validateAccessToken(accessToken);

      if (profile == null) {
        throw new ServicesException("A profile is required for the Optimized Trips API.");
      } else if (profile.equals(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)) {
        throw new ServicesException("The driving traffic profile does not work with this API.");
      } else if (!profile.equals(DirectionsCriteria.PROFILE_CYCLING)
        && !profile.equals(DirectionsCriteria.PROFILE_WALKING)
        && !profile.equals(DirectionsCriteria.PROFILE_DRIVING)) {
        throw new ServicesException("A valid profile must be used with the Optimized Trips API.");
      }

      if (coordinates == null) {
        throw new ServicesException("At least two coordinates must be provided with your API request.");
      } else if (coordinates.size() < 2) {
        throw new ServicesException("At least two coordinates must be provided with your API request.");
      } else if (coordinates.size() > 12) {
        throw new ServicesException("Maximum of 12 coordinates are allowed for this API.");
      }

      return new MapboxOptimizedTrips(this);
    }
  }
}
