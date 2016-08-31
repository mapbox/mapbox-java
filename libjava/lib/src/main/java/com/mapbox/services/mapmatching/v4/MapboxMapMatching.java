package com.mapbox.services.mapmatching.v4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.services.Constants;
import com.mapbox.services.commons.MapboxBuilder;
import com.mapbox.services.commons.MapboxService;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.directions.v4.DirectionsCriteria;
import com.mapbox.services.mapmatching.v4.gson.MapMatchingGeometryDeserializer;
import com.mapbox.services.mapmatching.v4.models.MapMatchingResponse;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The Mapbox map matching interface (v4)
 * <p>
 * The Mapbox Map Matching API snaps fuzzy, inaccurate traces from a GPS unit or a phone to the
 * OpenStreetMap? road and path network using the Directions API. This produces clean paths that can
 * be displayed on a map or used for other analysis.
 *
 * @see <a href="https://www.mapbox.com/api-documentation/#map-matching">Map matching API documentation</a>
 * @since 1.2.0
 */
public class MapboxMapMatching extends MapboxService<MapMatchingResponse> {

  private Builder builder = null;
  private MapMatchingService service = null;
  private Call<MapMatchingResponse> call = null;

  // Allows testing
  private String baseUrl = Constants.BASE_API_URL;

  private MapboxMapMatching(Builder builder) {
    this.builder = builder;
  }

  /**
   * Used internally.
   *
   * @param baseUrl the baseURL.
   * @since 1.2.0
   */
  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }


  private MapMatchingService getService() {
    // No need to recreate it
    if (service != null) {
      return service;
    }

    // Gson instance with type adapters
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(Geometry.class, new MapMatchingGeometryDeserializer())
      .create();

    // Retrofit instance
    Retrofit retrofit = new Retrofit.Builder()
      .client(getOkHttpClient())
      .baseUrl(baseUrl)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .build();

    // MapMatching service
    service = retrofit.create(MapMatchingService.class);
    return service;
  }

  /**
   * Used internally.
   *
   * @return call
   * @since 1.2.0
   */
  public Call<MapMatchingResponse> getCall() {
    // No need to recreate it
    if (call != null) {
      return call;
    }

    call = getService().getCall(
      getHeaderUserAgent(),
      builder.getProfile(),
      builder.getAccessToken(),
      builder.getGeometry(),
      builder.getGpsPrecison(),
      builder.getTrace()
    );

    return call;
  }

  /**
   * Execute the call
   *
   * @return The map matching v4 response
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   * @since 1.2.0
   */
  @Override
  public Response<MapMatchingResponse> executeCall() throws IOException {
    return getCall().execute();
  }

  /**
   * Execute the call
   *
   * @param callback A Retrofit callback.
   * @since 1.2.0
   */
  @Override
  public void enqueueCall(Callback<MapMatchingResponse> callback) {
    getCall().enqueue(callback);
  }

  /**
   * Cancel the call
   *
   * @since 1.2.0
   */
  @Override
  public void cancelCall() {
    getCall().cancel();
  }

  /**
   * clone the call
   *
   * @return cloned call
   * @since 1.2.0
   */
  @Override
  public Call<MapMatchingResponse> cloneCall() {
    return getCall().clone();
  }

  /**
   * Builds your map matching query by adding parameters.
   *
   * @since 1.2.0
   */
  public static class Builder extends MapboxBuilder {

    private String accessToken;
    private String profile;
    private String geometry;
    private Integer gpsPrecison;

    private LineString trace;

    /**
     * Constructor
     *
     * @since 1.2.0
     */
    public Builder() {
      // Use polyline by default as the return format
      this.geometry = DirectionsCriteria.GEOMETRY_POLYLINE;
    }

    /**
     * Required to call when building {@link Builder}
     *
     * @param accessToken Mapbox access token, you must have a Mapbox account in order to use
     *                    this API.
     * @return Builder
     * @since 1.2.0
     */
    @Override
    public Builder setAccessToken(String accessToken) {
      this.accessToken = accessToken;
      return this;
    }

    /**
     * @return Mapbox access token
     * @since 1.2.0
     */
    @Override
    public String getAccessToken() {
      return this.accessToken;
    }

    /**
     * Set a map matching profile. You should use one of the constants in Directions v4
     * com.mapbox.services.directions.v4.DirectionsCriteria
     *
     * @param profile String containg A directions profile ID; either {@code mapbox.driving},
     *                {@code mapbox.walking}, or {@code mapbox.cycling}. Use one of the
     *                {@link DirectionsCriteria} constants.
     * @return Builder
     * @since 1.2.0
     */
    public Builder setProfile(String profile) {
      this.profile = profile;
      return this;
    }

    /**
     * @return String containg A directions profile ID; either {@code mapbox.driving},
     * {@code mapbox.walking}, or {@code mapbox.cycling}.
     * @since 1.2.0
     */
    public String getProfile() {
      return profile;
    }

    /**
     * Format of the returned geometry. Allowed values are: {@code geojson} (default, as
     * LineString), {@code polyline} (documentation) with precision 6, {@code false} (no
     * geometry, but matched points).
     *
     * @return String containing one of the values allowed.
     * @since 1.2.0
     */
    public String getGeometry() {
      return geometry;
    }

    /**
     * Set the geometry to {@code false}.
     *
     * @return Builder
     * @since 1.2.0
     */
    public Builder setNoGeometry() {
      this.geometry = DirectionsCriteria.GEOMETRY_FALSE;
      return this;
    }

    /**
     * An integer in meters indicating the assumed precision of the used tracking device. Use
     * higher numbers (5-10) for noisy traces and lower numbers (1-3) for clean traces. The
     * default value is 4.
     *
     * @return integer value representing the GPS precision.
     * @since 1.2.0
     */
    public Integer getGpsPrecison() {
      return gpsPrecison;
    }

    /**
     * @return Returns a new request body that transmits {@code content}. If {@code contentType}
     * is non-null and lacks a charset, this will use UTF-8.
     * @since 1.2.0
     */
    public RequestBody getTrace() {
      return RequestBody.create(
        MediaType.parse("application/json"),
        Feature.fromGeometry(trace).toJson());
    }

    /**
     * @param gpsPrecison Assumed accuracy of the tracking device in meters
     *                    (1-10 inclusive, default 4)
     * @return Builder
     * @since 1.2.0
     */
    public Builder setGpsPrecison(Integer gpsPrecison) {
      this.gpsPrecison = gpsPrecison;
      return this;
    }

    /**
     * @param trace A {@link LineString} representing the route geometry you want to match.
     * @return Builder
     * @since 1.2.0
     */
    public Builder setTrace(LineString trace) {
      this.trace = trace;
      return this;
    }

    private void validateProfile() throws ServicesException {
      if (profile == null || !(profile.equals(DirectionsCriteria.PROFILE_CYCLING)
        || profile.equals(DirectionsCriteria.PROFILE_DRIVING)
        || profile.equals(DirectionsCriteria.PROFILE_WALKING))) {
        throw new ServicesException(
          "Using Mapbox Map Matching requires setting a valid profile.");
      }
    }

    private void validateGpsPrecision() throws ServicesException {
      if (gpsPrecison != null && (gpsPrecison < 1 || gpsPrecison > 10)) {
        throw new ServicesException(
          "Using Mapbox Map Matching requires setting a valid GPS precision.");
      }
    }

    private void validateTrace() throws ServicesException {
      if (trace == null || trace.getCoordinates() == null) {
        throw new ServicesException("Using Mapbox Map Matching requires to set some "
          + "coordinates representing the trace.");
      }

      if (trace.getCoordinates().size() > 100) {
        throw new ServicesException("The Map Matching API is limited to processing traces "
          + "with up to 100 coordinates. If you need to process longer traces, you can "
          + "split the trace and make multiple requests.");
      }
    }

    /**
     * Builder method
     *
     * @return MapboxMapMatching
     * @throws ServicesException Generic Exception occuring when something with map matching
     *                           goes wrong.
     * @since 1.2.0
     */
    @Override
    public MapboxMapMatching build() throws ServicesException {
      validateAccessToken(accessToken);
      validateProfile();
      validateGpsPrecision();
      validateTrace();
      return new MapboxMapMatching(this);
    }

  }
}
