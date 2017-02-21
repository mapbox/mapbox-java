package com.mapbox.services.api.distance.v1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.services.api.MapboxBuilder;
import com.mapbox.services.api.MapboxService;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.geojson.MultiPoint;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.distance.v1.gson.DistanceGeometryDeserializer;
import com.mapbox.services.api.distance.v1.models.DistanceResponse;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Note this API is still in preview.
 * <p>
 * The Mapbox Distance API returns all travel times between many points. For example, given 3
 * locations A, B, C, the Distance API will return a matrix of travel times in seconds between each
 * location. This API allows you to build tools that efficiently check the reachability of
 * coordinates from each other, filter points by travel time, or run algorithms for solving
 * optimization problems.
 * </p>
 * <p>
 * Limits placed on this API include a maximum 100 coordinate pairs per request and a maximum 60
 * requests per minute.
 * </p>
 *
 * @see <a href="https://www.mapbox.com/api-documentation/?language=Java#distance">Mapbox Distance API documentation</a>
 * @since 2.0.0
 */
public class MapboxDistance extends MapboxService<DistanceResponse> {

  protected Builder builder = null;
  private DistanceService service = null;
  private Call<DistanceResponse> call = null;
  private Gson gson;

  protected MapboxDistance(Builder builder) {
    this.builder = builder;
  }

  protected Gson getGson() {
    // Gson instance with type adapters
    if (gson == null) {
      gson = new GsonBuilder()
        .registerTypeAdapter(Geometry.class, new DistanceGeometryDeserializer())
        .create();
    }

    return gson;
  }

  private DistanceService getService() {
    //No need to recreate it
    if (service != null) {
      return service;
    }

    // Retrofit instance
    Retrofit retrofit = new Retrofit.Builder()
      .client(getOkHttpClient())
      .baseUrl(builder.getBaseUrl())
      .addConverterFactory(GsonConverterFactory.create(getGson()))
      .callFactory(getCallFactory())
      .build();

    // Distance service
    service = retrofit.create(DistanceService.class);
    return service;
  }

  /**
   * Used internally.
   *
   * @return call
   * @since 2.0.0
   */
  public Call<DistanceResponse> getCall() {
    // No need to recreate it
    if (call != null) {
      return call;
    }

    call = getService().getCall(
      getHeaderUserAgent(builder.getClientAppName()),
      builder.getUser(),
      builder.getProfile(),
      builder.getAccessToken(),
      builder.getCoordinates()
    );

    return call;
  }

  /**
   * Execute the call
   *
   * @return The distance v1 response.
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   * @since 2.0.0
   */
  @Override
  public Response<DistanceResponse> executeCall() throws IOException {
    return getCall().execute();
  }

  /**
   * Execute the call
   *
   * @param callback A Retrofit callback.
   * @since 2.0.0
   */
  @Override
  public void enqueueCall(Callback<DistanceResponse> callback) {
    getCall().enqueue(callback);
  }

  /**
   * Cancel the call
   *
   * @since 2.0.0
   */
  @Override
  public void cancelCall() {
    getCall().cancel();
  }

  /**
   * clone the call
   *
   * @return cloned call
   * @since 2.0.0
   */
  @Override
  public Call<DistanceResponse> cloneCall() {
    return getCall().clone();
  }

  /**
   * Builds your distance query by adding parameters.
   *
   * @since 2.0.0
   */
  public static class Builder<T extends Builder> extends MapboxBuilder {

    private String accessToken;
    private String user = DirectionsCriteria.PROFILE_DEFAULT_USER;
    private String profile;

    private List<Position> coordinates;

    /**
     * Constructor
     *
     * @since 2.0.0
     */
    public Builder() {
    }

    /**
     * @param user User string, by default this is set to Mapbox
     *             {@link DirectionsCriteria#PROFILE_DEFAULT_USER}.
     * @return Builder
     * @since 2.0.0
     */
    public T setUser(String user) {
      this.user = user;
      return (T) this;
    }

    /**
     * @return the user as String.
     * @since 2.0.0
     */
    public String getUser() {
      return user;
    }

    /**
     * Set a map matching profile. You should use one of the constants found in
     * {@link DirectionsCriteria}.
     *
     * @param profile String containing A directions profile ID; either {@code driving},
     *                {@code walking}, or {@code cycling}. Use one of the
     *                {@link DirectionsCriteria} constants.
     * @return Builder
     * @since 2.0.0
     */
    public T setProfile(String profile) {
      this.profile = profile;
      return (T) this;
    }

    /**
     * @return String containing A directions profile ID; either {@code driving},
     * {@code walking}, or {@code cycling}.
     * @since 2.0.0
     */
    public String getProfile() {
      return profile;
    }

    public RequestBody getCoordinates() {
      return RequestBody.create(
        MediaType.parse("application/json"),
        MultiPoint.fromCoordinates(coordinates).toJson());
    }

    public T setCoordinates(List<Position> coordinates) {
      this.coordinates = coordinates;
      return (T) this;
    }

    private void validateProfile() throws ServicesException {
      if (profile == null || !(profile.equals(DirectionsCriteria.PROFILE_CYCLING)
        || profile.equals(DirectionsCriteria.PROFILE_DRIVING)
        || profile.equals(DirectionsCriteria.PROFILE_WALKING))) {
        throw new ServicesException(
          "Using Mapbox Distance API requires setting a valid profile.");
      }
    }

    private void validateTrace() throws ServicesException {
      if (coordinates == null || coordinates.size() <= 0) {
        throw new ServicesException("Using Mapbox Distance API requires to set some coordinates.");
      }

      if (coordinates.size() > 100) {
        throw new ServicesException("The Mapbox Distance API is limited to processing up to 100 coordinate pairs."
          + " If you need to process additional coordinates, you can split the list and make multiple requests.");
      }
    }

    /**
     * Required to call when building {@link Builder}
     *
     * @param accessToken Mapbox access token, you must have a Mapbox account in order to use
     *                    this API.
     * @return Builder
     * @since 2.0.0
     */
    @Override
    public T setAccessToken(String accessToken) {
      this.accessToken = accessToken;
      return (T) this;
    }

    /**
     * @return Mapbox access token
     * @since 2.0.0
     */
    @Override
    public String getAccessToken() {
      return this.accessToken;
    }

    public T setClientAppName(String appName) {
      super.clientAppName = appName;
      return (T) this;
    }

    /**
     * Set the base url of the API.
     *
     * @param baseUrl base url used as end point
     * @return Builder
     * @since 2.0.0
     */
    @Override
    public T setBaseUrl(String baseUrl) {
      super.baseUrl = baseUrl;
      return (T) this;
    }

    /**
     * Builder method
     *
     * @return {@link MapboxDistance}
     * @throws ServicesException Generic Exception occurring when something with the Distance
     *                           API goes wrong.
     * @since 2.0.0
     */
    @Override
    public MapboxDistance build() throws ServicesException {
      validateAccessToken(accessToken);
      validateProfile();
      validateTrace();
      return new MapboxDistance(this);
    }
  }
}
