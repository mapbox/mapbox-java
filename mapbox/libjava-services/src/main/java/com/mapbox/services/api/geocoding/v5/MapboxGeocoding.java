package com.mapbox.services.api.geocoding.v5;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.services.api.MapboxBuilder;
import com.mapbox.services.api.MapboxService;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.geocoding.v5.gson.CarmenGeometryDeserializer;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The Mapbox geocoding client (v5).
 *
 * @since 1.0.0
 */
public class MapboxGeocoding extends MapboxService<GeocodingResponse> {

  protected Builder builder = null;
  private GeocodingService service = null;
  private Call<GeocodingResponse> call = null;
  private Call<List<GeocodingResponse>> batchCall = null;
  private Gson gson;

  /**
   * Public constructor.
   *
   * @param builder {@link Builder} object.
   * @since 1.0.0
   */
  protected MapboxGeocoding(Builder builder) {
    this.builder = builder;
  }

  protected Gson getGson() {
    // Gson instance with type adapters
    if (gson == null) {
      gson = new GsonBuilder()
        .registerTypeAdapter(Geometry.class, new CarmenGeometryDeserializer())
        .create();
    }

    return gson;
  }

  /**
   * Used internally.
   *
   * @return Geocoding service.
   * @since 1.0.0
   */
  public GeocodingService getService() {
    // No need to recreate it
    if (service != null) {
      return service;
    }

    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
      .baseUrl(builder.getBaseUrl())
      .addConverterFactory(GsonConverterFactory.create(getGson()));
    if (getCallFactory() != null) {
      retrofitBuilder.callFactory(getCallFactory());
    } else {
      retrofitBuilder.client(getOkHttpClient());
    }

    service = retrofitBuilder.build().create(GeocodingService.class);
    return service;
  }

  /**
   * Used internally.
   *
   * @return call
   * @since 1.0.0
   */
  public Call<GeocodingResponse> getCall() {
    // No need to recreate it
    if (call != null) {
      return call;
    }

    if (builder.getQuery().contains(";")) {
      throw new IllegalArgumentException("Use getBatchCall() for batch calls.");
    }

    call = getService().getCall(
      getHeaderUserAgent(builder.getClientAppName()),
      builder.getMode(),
      builder.getQuery(),
      builder.getAccessToken(),
      builder.getCountry(),
      builder.getProximity(),
      builder.getGeocodingTypes(),
      builder.getAutocomplete(),
      builder.getBbox(),
      builder.getLimit(),
      builder.getLanguage());

    return call;
  }

  /**
   * Used internally.
   *
   * @return batch call
   * @since 2.0.0
   */
  public Call<List<GeocodingResponse>> getBatchCall() {
    // No need to recreate it
    if (batchCall != null) {
      return batchCall;
    }

    if (!builder.getQuery().contains(";")) {
      throw new IllegalArgumentException("Use getCall() for non-batch calls.");
    }

    batchCall = getService().getBatchCall(
      getHeaderUserAgent(builder.getClientAppName()),
      builder.getMode(),
      builder.getQuery(),
      builder.getAccessToken(),
      builder.getCountry(),
      builder.getProximity(),
      builder.getGeocodingTypes(),
      builder.getAutocomplete(),
      builder.getBbox(),
      builder.getLimit(),
      builder.getLanguage());

    return batchCall;
  }

  /**
   * Execute the call
   *
   * @return The Geocoding v5 response
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   * @since 1.0.0
   */
  @Override
  public Response<GeocodingResponse> executeCall() throws IOException {
    return getCall().execute();
  }

  /**
   * Execute the batch call
   *
   * @return The Geocoding v5 response
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   * @since 2.0.0
   */
  public Response<List<GeocodingResponse>> executeBatchCall() throws IOException {
    return getBatchCall().execute();
  }

  /**
   * Execute the call
   *
   * @param callback A Retrofit callback.
   * @since 1.0.0
   */
  @Override
  public void enqueueCall(Callback<GeocodingResponse> callback) {
    getCall().enqueue(callback);
  }

  /**
   * Execute the batch call
   *
   * @param callback A Retrofit callback.
   * @since 2.0.0
   */
  public void enqueueBatchCall(Callback<List<GeocodingResponse>> callback) {
    getBatchCall().enqueue(callback);
  }

  /**
   * Cancel the call
   *
   * @since 1.0.0
   */
  @Override
  public void cancelCall() {
    getCall().cancel();
  }

  /**
   * Cancel the batch call
   *
   * @since 2.0.0
   */
  public void cancelBatchCall() {
    getBatchCall().cancel();
  }

  /**
   * clone the call
   *
   * @return cloned call
   * @since 1.0.0
   */
  @Override
  public Call<GeocodingResponse> cloneCall() {
    return getCall().clone();
  }

  /**
   * clone the batch call
   *
   * @return cloned call
   * @since 2.0.0
   */
  public Call<List<GeocodingResponse>> cloneBatchCall() {
    return getBatchCall().clone();
  }

  /**
   * Builds your geocoder query by adding parameters.
   *
   * @since 1.0.0
   */
  public static class Builder<T extends Builder> extends MapboxBuilder {

    // Required
    private String accessToken;
    private String query;
    private String mode;

    // Optional (Retrofit will omit these from the request if they remain null)
    private String country = null;
    private String proximity = null;
    private String geocodingTypes = null;
    private Boolean autocomplete = null;
    private String bbox = null;
    private String limit = null;
    private String language = null;

    /**
     * Constructor
     *
     * @since 1.0.0
     */
    public Builder() {
      // Defaults
      mode = GeocodingCriteria.MODE_PLACES;
    }

    /**
     * Required to call when building {@link MapboxGeocoding.Builder}
     *
     * @param accessToken Mapbox access token, you must have a Mapbox account in order to use
     *                    this library.
     * @return Builder
     * @since 1.0.0
     */
    @Override
    public T setAccessToken(String accessToken) {
      this.accessToken = accessToken;
      return (T) this;
    }

    /**
     * The location equals the query.
     *
     * @param location query
     * @return Builder
     * @since 1.0.0
     */
    public T setLocation(String location) {
      query = location;
      return (T) this;
    }

    /**
     * @param position {@link Position}
     * @return Builder
     * @since 1.0.0
     */
    public T setCoordinates(Position position) {
      if (position == null) {
        return (T) this;
      }
      query = String.format(Locale.US, "%f,%f",
        position.getLongitude(),
        position.getLatitude());
      return (T) this;
    }

    /**
     * mapbox.places or mapbox.places-permanent for enterprise/batch geocoding.
     *
     * @param mode mapbox.places or mapbox.places-permanent for enterprise/batch geocoding.
     * @return Builder
     * @since 1.0.0
     */
    public T setMode(String mode) {
      this.mode = mode;
      return (T) this;
    }

    /**
     * Country which you want the results to show up in.
     *
     * @param country ISO 3166 alpha 2 country code
     * @return Builder
     * @since 1.0.0
     */
    public T setCountry(String country) {
      this.country = country;
      return (T) this;
    }

    /**
     * Countries which you want the results to show up in.
     *
     * @param countries ISO 3166 alpha 2 country codes, separated by commas.
     * @return Builder
     * @since 1.0.0
     */
    public T setCountries(String[] countries) {
      this.country = TextUtils.join(",", countries);
      return (T) this;
    }

    /**
     * Location around which to bias results.
     *
     * @param position A {@link Position}.
     * @return Builder
     * @since 1.0.0
     */
    public T setProximity(Position position) {
      if (position == null) {
        return (T) this;
      }
      proximity = String.format(Locale.US, "%f,%f",
        position.getLongitude(),
        position.getLatitude());
      return (T) this;
    }

    /**
     * Filter results by one or more type. Options are country, region, postcode, place,
     * locality, neighborhood, address, poi, poi.landmark. Multiple options can be comma-separated.
     *
     * @param geocodingType String filtering the geocoder result types.
     * @return Builder
     * @since 1.0.0
     */
    public T setGeocodingType(String geocodingType) {
      this.geocodingTypes = geocodingType;
      return (T) this;
    }

    /**
     * Filter results by one or more type. Options are country, region, postcode, place,
     * locality, neighborhood, address, poi, poi.landmark. Multiple options can be comma-separated.
     *
     * @param geocodingType String array filtering the geocoder result types.
     * @return Builder
     * @since 1.0.0
     */
    public T setGeocodingTypes(String[] geocodingType) {
      this.geocodingTypes = TextUtils.join(",", geocodingType);
      return (T) this;
    }

    /**
     * Whether or not to return autocomplete results.
     *
     * @param autocomplete true, if you want autocomplete results, else false. (Defaults true)
     * @return Builder
     * @since 1.0.0
     */
    public T setAutocomplete(boolean autocomplete) {
      this.autocomplete = autocomplete;
      return (T) this;
    }

    /**
     * Bounding box within which to limit results.
     *
     * @param northeast The northeast corner of the bounding box as {@link Position}.
     * @param southwest The southwest corner of the bounding box as {@link Position}.
     * @return Builder
     * @throws ServicesException Generic Exception for all things geocoding.
     * @since 1.0.0
     */
    public T setBbox(Position northeast, Position southwest) throws ServicesException {
      return setBbox(southwest.getLongitude(), southwest.getLatitude(),
        northeast.getLongitude(), northeast.getLatitude());
    }

    /**
     * Bounding box within which to limit results.
     *
     * @param minX The minX of bounding box when maps facing north.
     * @param minY The minY of bounding box when maps facing north.
     * @param maxX The maxX of bounding box when maps facing north.
     * @param maxY The maxY of bounding box when maps facing north.
     * @return Builder
     * @throws ServicesException Generic Exception for all things geocoding.
     * @since 1.0.0
     */
    public T setBbox(double minX, double minY, double maxX, double maxY) throws ServicesException {
      if (minX == 0 && minY == 0 && maxX == 0 && maxY == 0) {
        throw new ServicesException("You provided an empty bounding box");
      }

      this.bbox = String.format(Locale.US, "%f,%f,%f,%f", minX, minY, maxX, maxY);
      return (T) this;
    }

    /**
     * Limit the number of results returned. The default is 5 for forward geocoding and 1 for
     * reverse geocoding.
     *
     * @param limit the integer value representing the amount of results desired.
     * @return Builder
     * @since 2.0.0
     */
    public T setLimit(int limit) {
      if (limit == 0) {
        return (T) this;
      }
      this.limit = String.format(Locale.US, "%d", limit);
      return (T) this;
    }

    /**
     * @return your Mapbox access token.
     * @since 1.0.0
     */
    @Override
    public String getAccessToken() {
      return accessToken;
    }

    /**
     * @return your geocoder query.
     * @since 1.0.0
     */
    public String getQuery() {
      return query;
    }

    /**
     * @return mapbox.places or  mapbox.places-permanent for enterprise/batch geocoding.
     * @since 1.0.0
     */
    public String getMode() {
      return mode;
    }

    /**
     * @return ISO 3166 alpha 2 country codes, separated by commas
     * @since 1.0.0
     */
    public String getCountry() {
      return country;
    }

    /**
     * Location around which you biased the results.
     *
     * @return String with the format longitude, latitude.
     * @since 1.0.0
     */
    public String getProximity() {
      return proximity;
    }

    /**
     * If you filtered your results by one or more types you can get what those filters are by
     * using this method.
     *
     * @return String with list of filters you used.
     * @since 1.0.0
     */
    public String getGeocodingTypes() {
      return geocodingTypes;
    }

    /**
     * @return true if autocomplete results, else false.
     * @since 1.0.0
     */
    public Boolean getAutocomplete() {
      return autocomplete;
    }

    /**
     * @return Bounding box within which the results are limited
     * @since 1.0.0
     */
    public String getBbox() {
      return bbox;
    }

    /**
     * @return the integer value representing the amount of results desired.
     * @since 2.0.0
     */
    public String getLimit() {
      return limit;
    }

    /**
     * @return The locale in which results should be returned.
     * @since 2.0.0
     */
    public String getLanguage() {
      return language;
    }

    /**
     * The locale in which results should be returned.
     *
     * This property affects the language of returned results; generally speaking,
     * it does not determine which results are found. If the Geocoding API does not
     * recognize the language code, it may fall back to another language or the default
     * language. Components other than the language code, such as the country and
     * script codes, are ignored.
     *
     * By default, this property is set to `null`, causing results to be in the default
     * language.
     *
     * This option is experimental.
     *
     * @param language The locale in which results should be returned.
     * @return the current MapboxBuilder instance
     * @since 2.0.0
     */
    public T setLanguage(String language) {
      this.language = language;
      return (T) this;
    }

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
     * Build method
     *
     * @return MapboxGeocoding
     * @throws ServicesException Generic Exception for all things geocoding.
     * @since 1.0.0
     */
    @Override
    public MapboxGeocoding build() throws ServicesException {
      validateAccessToken(accessToken);
      return new MapboxGeocoding(this);
    }
  }
}
