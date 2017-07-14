package com.mapbox.services.api.mapmatching.v5;

import com.mapbox.services.api.MapboxBuilder;
import com.mapbox.services.api.MapboxService;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.mapmatching.v5.models.MapMatchingResponse;
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
 * The Mapbox map matching interface (v5)
 * <p>
 * The Mapbox Map Matching API snaps fuzzy, inaccurate traces from a GPS unit or a phone to the
 * OpenStreetMap road and path network using the Directions API. This produces clean paths that can
 * be displayed on a map or used for other analysis.
 *
 * @see <a href="https://www.mapbox.com/api-documentation/#map-matching">Map matching API documentation</a>
 * @since 2.0.0
 */
public class MapboxMapMatching extends MapboxService<MapMatchingResponse> {

  protected Builder builder = null;
  private MapMatchingService service = null;
  private Call<MapMatchingResponse> call = null;

  protected MapboxMapMatching(Builder builder) {
    this.builder = builder;
  }

  private MapMatchingService getService() {
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
      getHeaderUserAgent(builder.getClientAppName()),
      builder.getUser(),
      builder.getProfile(),
      builder.getCoordinates(),
      builder.getAccessToken(),
      builder.getGeometries(),
      builder.getRadiuses(),
      builder.getSteps(),
      builder.getOverview(),
      builder.getTimestamps(),
      builder.getAnnotations(),
      builder.getLanguage()
    );

    return call;
  }

  /**
   * Execute the call
   *
   * @return The map matching v5 response
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   * @since 2.0.0
   */
  @Override
  public Response<MapMatchingResponse> executeCall() throws IOException {
    return getCall().execute();
  }

  /**
   * Execute the call
   *
   * @param callback A Retrofit callback.
   * @since 2.0.0
   */
  @Override
  public void enqueueCall(Callback<MapMatchingResponse> callback) {
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
  public Call<MapMatchingResponse> cloneCall() {
    return getCall().clone();
  }

  /**
   * Builds your map matching query by adding parameters.
   *
   * @since 2.0.0
   */
  public static class Builder<T extends Builder> extends MapboxBuilder {

    private String accessToken = null;
    private String user = null;
    private String profile = null;
    private Position[] coordinates = null;
    private String geometries = null;
    private double[] radiuses = null;
    private Boolean steps = null;
    private String overview = null;
    private String[] timestamps = null;
    private String annotations = null;
    private String language;

    /**
     * Constructor
     *
     * @since 2.0.0
     */
    public Builder() {
      // Set defaults
      this.user = DirectionsCriteria.PROFILE_DEFAULT_USER;

      // We only support polyline encoded geometries to reduce the size of the response.
      // If we need the corresponding LineString object, this SDK can do the decoding with
      // LineString.fromPolyline(String polyline, int precision).
      this.geometries = MapMatchingCriteria.GEOMETRY_POLYLINE_6;
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

    /**
     * @return annotations
     */
    public String getAnnotations() {
      return annotations;
    }

    /**
     * @param annotations value
     * @return Builder
     */
    public T setAnnotations(String annotations) {
      this.annotations = annotations;
      return (T) this;
    }

    /**
     * @return coordinates
     */
    public String getCoordinates() {
      List<String> coordinatesFormatted = new ArrayList<>();
      for (Position coordinate : coordinates) {
        coordinatesFormatted.add(String.format(Locale.US, "%s,%s",
          TextUtils.formatCoordinate(coordinate.getLongitude()),
          TextUtils.formatCoordinate(coordinate.getLatitude())));
      }

      return TextUtils.join(";", coordinatesFormatted.toArray());
    }

    /**
     * @param coordinates value
     * @return Builder
     */
    public T setCoordinates(Position[] coordinates) {
      this.coordinates = coordinates;
      return (T) this;
    }

    /**
     * @return geometries
     */
    public String getGeometries() {
      return geometries;
    }

    /**
     * @param geometries value
     * @return Builder
     */
    public T setGeometries(String geometries) {
      this.geometries = geometries;
      return (T) this;
    }

    /**
     * @return overview
     */
    public String getOverview() {
      return overview;
    }

    /**
     * @param overview value
     * @return Builder
     */
    public T setOverview(String overview) {
      this.overview = overview;
      return (T) this;
    }

    /**
     * @return profile
     */
    public String getProfile() {
      return profile;
    }

    /**
     * @param profile value
     * @return Builder
     */
    public T setProfile(String profile) {
      this.profile = profile;
      return (T) this;
    }

    /**
     * @return radiuses
     */
    public String getRadiuses() {
      if (radiuses == null || radiuses.length == 0) {
        return null;
      }

      String[] radiusesFormatted = new String[radiuses.length];
      for (int i = 0; i < radiuses.length; i++) {
        radiusesFormatted[i] = String.format(Locale.US, "%f", radiuses[i]);
      }

      return TextUtils.join(";", radiusesFormatted);
    }

    /**
     * @param radiuses value
     * @return Builder
     */
    public T setRadiuses(double[] radiuses) {
      this.radiuses = radiuses;
      return (T) this;
    }

    /**
     * @return steps
     */
    public Boolean getSteps() {
      return steps;
    }

    /**
     * @param steps value
     * @return Builder
     */
    public T setSteps(Boolean steps) {
      this.steps = steps;
      return (T) this;
    }

    /**
     * @return timestamps
     */
    public String getTimestamps() {
      if (timestamps == null || timestamps.length == 0) {
        return null;
      }

      return TextUtils.join(";", timestamps);
    }

    /**
     * @param timestamps value
     * @return Builder
     */
    public T setTimestamps(String[] timestamps) {
      this.timestamps = timestamps;
      return (T) this;
    }

    /**
     * @return user
     */
    public String getUser() {
      return user;
    }

    /**
     * @param user value
     * @return Builder
     */
    public T setUser(String user) {
      this.user = user;
      return (T) this;
    }

    /**
     * @param appName base package name or other simple string identifier
     * @return Builder
     */
    @Override
    public T setClientAppName(String appName) {
      super.clientAppName = appName;
      return (T) this;
    }

    /**
     * Set the base url of the API.
     *
     * @param baseUrl base url used as end point
     * @return the current MapboxBuilder instance
     * @since 2.0.0
     */
    @Override
    public T setBaseUrl(String baseUrl) {
      super.baseUrl = baseUrl;
      return (T) this;
    }

    /**
     * Optionally set the language of returned turn-by-turn text instructions. The default is {@code en} for English.
     *
     * @param language The locale in which results should be returned.
     * @return Builder
     * @see <a href="https://www.mapbox.com/api-documentation/#instructions-languages">Supported languages</a>
     * @since 2.2.0
     */
    public T setLanguage(String language) {
      this.language = language;
      return (T) this;
    }

    /**
     * @return The language the turn-by-turn directions will be in.
     * @since 2.2.0
     */
    public String getLanguage() {
      return language;
    }

    /**
     * Builder method
     *
     * @return MapboxMapMatching
     * @throws ServicesException Generic Exception occurring when something with map matching
     *                           goes wrong.
     * @since 2.0.0
     */
    @Override
    public MapboxMapMatching build() throws ServicesException {
      validateAccessToken(accessToken);

      if (profile == null) {
        throw new ServicesException(
          "A profile is required for the Map Matching API. Use one of the profiles found in the"
            + "MapMatchingCriteria.java file.");
      }

      if (geometries != null && geometries.equals(MapMatchingCriteria.GEOMETRY_GEOJSON)) {
        throw new ServicesException(
          "The SDK only supports encoded polylines for geometries values.");
      }

      if (coordinates == null || coordinates.length == 0) {
        throw new ServicesException(
          "Coordinates must be specified for Map Matching to be able to work.");
      }

      if (coordinates.length > 100) {
        throw new ServicesException(
          "All profiles allows for maximum of 100 coordinates.");
      }

      if (radiuses != null && radiuses.length != coordinates.length) {
        throw new ServicesException(
          "There must be as many radiuses as there are coordinates.");
      }

      if (timestamps != null && timestamps.length != coordinates.length) {
        throw new ServicesException(
          "There must be as many timestamps as there are coordinates.");
      }

      return new MapboxMapMatching(this);
    }

  }
}
