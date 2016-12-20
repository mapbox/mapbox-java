package com.mapbox.services.geocoding.v5;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.MapboxBuilder;
import com.mapbox.services.commons.MapboxService;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;
import com.mapbox.services.geocoding.v5.gson.CarmenGeometryDeserializer;
import com.mapbox.services.geocoding.v5.models.GeocodingResponse;

import java.io.IOException;
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

  private Builder builder = null;
  private GeocodingService service = null;
  private Call<GeocodingResponse> call = null;

  /**
   * Public constructor.
   *
   * @param builder {@link Builder} object.
   * @since 1.0.0
   */
  public MapboxGeocoding(Builder builder) {
    this.builder = builder;
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

    // Gson instance with type adapters
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(Geometry.class, new CarmenGeometryDeserializer())
      .create();

    Retrofit retrofit = new Retrofit.Builder()
      .client(getOkHttpClient())
      .baseUrl(builder.getBaseUrl())
      .addConverterFactory(GsonConverterFactory.create(gson))
      .build();

    service = retrofit.create(GeocodingService.class);
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
      builder.getLimit());

    return call;
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
   * Cancel the call
   *
   * @since 1.0.0
   */
  @Override
  public void cancelCall() {
    getCall().cancel();
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
   * Builds your geocoder query by adding parameters.
   *
   * @since 1.0.0
   */
  public static class Builder extends MapboxBuilder {

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
    public Builder setAccessToken(String accessToken) {
      this.accessToken = accessToken;
      return this;
    }

    /**
     * The location equals the query.
     *
     * @param location query
     * @return Builder
     * @since 1.0.0
     */
    public Builder setLocation(String location) {
      query = location;
      return this;
    }

    /**
     * @param position {@link Position}
     * @return Builder
     * @since 1.0.0
     */
    public Builder setCoordinates(Position position) {
      if (position == null) {
        return this;
      }
      query = String.format(Locale.US, "%f,%f",
        position.getLongitude(),
        position.getLatitude());
      return this;
    }

    /**
     * mapbox.places or mapbox.places-permanent for enterprise/batch geocoding.
     *
     * @param mode mapbox.places or mapbox.places-permanent for enterprise/batch geocoding.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setMode(String mode) {
      this.mode = mode;
      return this;
    }

    /**
     * Country which you want the results to show up in.
     *
     * @param country ISO 3166 alpha 2 country code
     * @return Builder
     * @since 1.0.0
     */
    public Builder setCountry(String country) {
      this.country = country;
      return this;
    }

    /**
     * Countries which you want the results to show up in.
     *
     * @param countries ISO 3166 alpha 2 country codes, separated by commas.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setCountries(String[] countries) {
      this.country = TextUtils.join(",", countries);
      return this;
    }

    /**
     * Location around which to bias results.
     *
     * @param position A {@link Position}.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setProximity(Position position) {
      if (position == null) {
        return this;
      }
      proximity = String.format(Locale.US, "%f,%f",
        position.getLongitude(),
        position.getLatitude());
      return this;
    }

    /**
     * Filter results by one or more type. Options are country, region, postcode, place,
     * locality, neighborhood, address, poi, poi.landmark. Multiple options can be comma-separated.
     *
     * @param geocodingType String filtering the geocoder result types.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setGeocodingType(String geocodingType) {
      this.geocodingTypes = geocodingType;
      return this;
    }

    /**
     * Filter results by one or more type. Options are country, region, postcode, place,
     * locality, neighborhood, address, poi, poi.landmark. Multiple options can be comma-separated.
     *
     * @param geocodingType String array filtering the geocoder result types.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setGeocodingTypes(String[] geocodingType) {
      this.geocodingTypes = TextUtils.join(",", geocodingType);
      return this;
    }

    /**
     * Whether or not to return autocomplete results.
     *
     * @param autocomplete true, if you want autocomplete results, else false. (Defaults true)
     * @return Builder
     * @since 1.0.0
     */
    public Builder setAutocomplete(boolean autocomplete) {
      this.autocomplete = autocomplete;
      return this;
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
    public Builder setBbox(Position northeast, Position southwest) throws ServicesException {
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
    public Builder setBbox(double minX, double minY, double maxX, double maxY) throws ServicesException {
      if (minX == 0 && minY == 0 && maxX == 0 && maxY == 0) {
        throw new ServicesException("You provided an empty bounding box");
      }

      this.bbox = String.format(Locale.US, "%f,%f,%f,%f", minX, minY, maxX, maxY);
      return this;
    }

    /**
     * Limit the number of results returned. The default is 5 for forward geocoding and 1 for
     * reverse geocoding.
     *
     * @param limit the integer value representing the amount of results desired.
     * @return Builder
     * @since 2.0.0
     */
    public Builder setLimit(int limit) {
      if (limit == 0) {
        return this;
      }
      this.limit = String.format(Locale.US, "%d", limit);
      return this;
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

    public Builder setClientAppName(String appName) {
      super.clientAppName = appName;
      return this;
    }

    /**
     * Set the base url of the API.
     *
     * @param baseUrl base url used as end point
     * @return the current MapboxBuilder instance
     * @since 2.0.0
     */
    @Override
    public Builder setBaseUrl(String baseUrl) {
      super.baseUrl = baseUrl;
      return this;
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
