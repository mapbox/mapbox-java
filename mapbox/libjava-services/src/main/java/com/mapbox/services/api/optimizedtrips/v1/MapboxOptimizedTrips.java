package com.mapbox.services.api.optimizedtrips.v1;

import com.mapbox.services.api.MapboxBuilder;
import com.mapbox.services.api.MapboxService;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.optimizedtrips.v1.models.OptimizedTripsResponse;
import com.mapbox.services.commons.models.Position;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
      builder.getGeometries(),
      builder.getAnnotation(),
      builder.getDestination(),
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
    private String sources;
    private String destination;
    private String geometries;
    private double[] radiuses;
    private Boolean steps;
    private double[][] bearings;
    private String[] annotation;

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

    public T setSources(String sources) {
      this.sources = sources;
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

    public String getSources() {
      return sources;
    }

    public String getDestination() {
      return destination;
    }

    public List<Position> getCoordinates() {
      return coordinates;
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
      return new MapboxOptimizedTrips(this);
    }
  }


}
