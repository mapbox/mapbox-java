package com.mapbox.api.geocoding.v5;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.api.geocoding.v5.GeocodingCriteria.GeocodingTypeCriteria;
import com.mapbox.api.geocoding.v5.models.GeocodingAdapterFactory;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.MapboxService;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.core.utils.ApiCallHelper;
import com.mapbox.core.utils.MapboxUtils;
import com.mapbox.core.utils.TextUtils;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.GeometryAdapterFactory;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.gson.BoundingBoxTypeAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class gives you access to both Mapbox forward and reverse geocoding.
 * <p>
 * Forward geocoding lets you convert location text into geographic coordinates, turning
 * {@code 2 Lincoln Memorial Circle NW} into a {@link Point} with the coordinates
 * {@code -77.050, 38.889}.
 * <p>
 * Reverse geocoding turns geographic coordinates into place names, turning {@code -77.050, 38.889}
 * into {@code 2 Lincoln Memorial Circle NW}. These place names can vary from specific addresses to
 * states and countries that contain the given coordinates.
 * <p>
 * Batch Geocoding
 * This feature is limited to enterprise customers only and the {@link #mode()} must be set to
 * {@link GeocodingCriteria#MODE_PLACES_PERMANENT}.
 * <p>
 * Batch requests have the same parameters as normal requests, but can include more than one query
 * by using {@link MapboxGeocoding.Builder#query(String)} and separating queries with the {@code ;}
 * character.
 * <p>
 * With the {@link GeocodingCriteria#MODE_PLACES_PERMANENT} mode, you can make up to 50 forward or
 * reverse geocoding queries in a single request. The response is a list of individual
 * {@link GeocodingResponse}s. Each query in a batch request counts individually against your
 * account's rate limits.
 *
 * @see <a href="https://www.mapbox.com/android-docs/java-sdk/overview/geocoder/">Android
 *   Geocoding documentation</a>
 * @since 1.0.0
 */
@AutoValue
public abstract class MapboxGeocoding extends MapboxService<GeocodingResponse, GeocodingService> {
  private Call<List<GeocodingResponse>> batchCall;

  protected MapboxGeocoding() {
    super(GeocodingService.class);
  }

  @Override
  protected GsonBuilder getGsonBuilder() {

    return new GsonBuilder()
      .registerTypeAdapterFactory(GeocodingAdapterFactory.create())
      .registerTypeAdapterFactory(GeometryAdapterFactory.create())
      .registerTypeAdapter(BoundingBox.class, new BoundingBoxTypeAdapter());
  }

  @Override
  protected Call<GeocodingResponse> initializeCall() {
    if (mode().contains(GeocodingCriteria.MODE_PLACES_PERMANENT)) {
      throw new IllegalArgumentException("Use getBatchCall() for batch calls.");
    }

    return getService().getCall(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      mode(),
      query(),
      accessToken(),
      country(),
      proximity(),
      geocodingTypes(),
      autocomplete(),
      bbox(),
      limit(),
      languages(),
      reverseMode());
  }

  private Call<List<GeocodingResponse>> getBatchCall() {
    // No need to recreate it
    if (batchCall != null) {
      return batchCall;
    }

    if (mode().equals(GeocodingCriteria.MODE_PLACES)) {
      throw new ServicesException("Use getCall() for non-batch calls.");
    }

    batchCall = getService().getBatchCall(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      mode(),
      query(),
      accessToken(),
      country(),
      proximity(),
      geocodingTypes(),
      autocomplete(),
      bbox(),
      limit(),
      languages(),
      reverseMode());

    return batchCall;
  }

  /**
   * Wrapper method for Retrofits {@link Call#execute()} call returning a batch response specific to
   * the Geocoding API.
   *
   * @return the Geocoding v5 batch response once the call completes successfully
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   * @since 1.0.0
   */
  public Response<List<GeocodingResponse>> executeBatchCall() throws IOException {
    return getBatchCall().execute();
  }


  /**
   * Wrapper method for Retrofits {@link Call#enqueue(Callback)} call returning a batch response
   * specific to the Geocoding batch API. Use this method to make a geocoding request on the Main
   * Thread.
   *
   * @param callback a {@link Callback} which is used once the {@link GeocodingResponse} is created.
   * @since 1.0.0
   */
  public void enqueueBatchCall(Callback<List<GeocodingResponse>> callback) {
    getBatchCall().enqueue(callback);
  }

  /**
   * Wrapper method for Retrofits {@link Call#cancel()} call, important to manually cancel call if
   * the user dismisses the calling activity or no longer needs the returned results.
   *
   * @since 1.0.0
   */
  public void cancelBatchCall() {
    getBatchCall().cancel();
  }

  /**
   * Wrapper method for Retrofits {@link Call#clone()} call, useful for getting call information.
   *
   * @return cloned call
   * @since 1.0.0
   */
  public Call<List<GeocodingResponse>> cloneBatchCall() {
    return getBatchCall().clone();
  }

  @NonNull
  abstract String query();

  @NonNull
  abstract String mode();

  @NonNull
  abstract String accessToken();

  @NonNull
  @Override
  protected abstract String baseUrl();

  @Nullable
  abstract String country();

  @Nullable
  abstract String proximity();

  @Nullable
  abstract String geocodingTypes();

  @Nullable
  abstract Boolean autocomplete();

  @Nullable
  abstract String bbox();

  @Nullable
  abstract String limit();

  @Nullable
  abstract String languages();

  @Nullable
  abstract String reverseMode();

  @Nullable
  abstract String clientAppName();


  /**
   * Build a new {@link MapboxGeocoding} object with the initial values set for
   * {@link #baseUrl()} and {@link #mode()}.
   *
   * @return a {@link Builder} object for creating this object
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxGeocoding.Builder()
      .baseUrl(Constants.BASE_API_URL)
      .mode(GeocodingCriteria.MODE_PLACES);
  }

  /**
   * This builder is used to create a new request to the Mapbox Geocoding API. At a bare minimum,
   * your request must include an access token and a query of some kind. All other fields can
   * be left alone inorder to use the default behaviour of the API.
   * <p>
   * By default, the geocoding mode is set to places but can be changed to batch if you have an
   * enterprise Mapbox plan.
   * </p><p>
   * Note to contributors: All optional booleans in this builder use the object {@code Boolean}
   * rather than the primitive to allow for unset (null) values.
   * </p>
   *
   * @since 1.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    private List<String> countries = new ArrayList<>();

    /**
     * Perform a reverse geocode on the provided {@link Point}. Only one point can be passed in as
     * the query and isn't guaranteed to return a result. If you are an enterprise customer and
     * wanting to do a batch reverse Geocode, you can use the {@link #query(String)} method
     * separating them with a semicolon.
     *
     * @param point a GeoJSON point which matches to coordinate you'd like to reverse geocode
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder query(@NonNull Point point) {
      query(String.format(Locale.US, "%s,%s",
        TextUtils.formatCoordinate(point.longitude()),
        TextUtils.formatCoordinate(point.latitude())));
      return this;
    }

    /**
     * This method can be used for performing a forward geocode on a string representing a address
     * or POI. If you are an enterprise customer and wish to perform a batch geocode, separate your
     * queries with a semicolon.
     *
     * @param query a String containing the text you'd like to forward geocode
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder query(@NonNull String query);

    /**
     * This sets the kind of geocoding result you desire, either ephemeral geocoding or batch
     * geocoding.
     * <p>
     * Note tht batch geocoding's only available to users under a enterprise plan and will return an
     * error code rather than a successful result.
     * </p><p>
     * Options avaliable to pass in include, {@link GeocodingCriteria#MODE_PLACES} for a ephemeral
     * geocoding result (default) or {@link GeocodingCriteria#MODE_PLACES_PERMANENT} for enterprise
     * batch geocoding.
     * </p>
     *
     * @param mode mapbox.places or mapbox.places-permanent for enterprise/batch geocoding
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder mode(@NonNull @GeocodingCriteria.GeocodingModeCriteria String mode);

    /**
     * Bias local results base on a provided {@link Point}. This oftentimes increases accuracy in
     * the returned results.
     *
     * @param proximity a point defining the proximity you'd like to bias the results around
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public Builder proximity(@NonNull Point proximity) {
      proximity(String.format(Locale.US, "%s,%s",
        TextUtils.formatCoordinate(proximity.longitude()), proximity.latitude()));
      return this;
    }

    abstract Builder proximity(String proximity);

    /**
     * This optionally can be set to filter the results returned back after making your forward or
     * reverse geocoding request. A null value can't be passed in and only values defined in
     * {@link GeocodingTypeCriteria} are allowed.
     * <p>
     * Note that {@link GeocodingCriteria#TYPE_POI_LANDMARK} returns a subset of the results
     * returned by {@link GeocodingCriteria#TYPE_POI}. More than one type can be specified.
     * </p>
     *
     * @param geocodingTypes optionally filter the result types by one or more defined types inside
     *                       the {@link GeocodingTypeCriteria}
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public Builder geocodingTypes(@NonNull @GeocodingTypeCriteria String... geocodingTypes) {
      geocodingTypes(TextUtils.join(",", geocodingTypes));
      return this;
    }

    abstract Builder geocodingTypes(String geocodingTypes);

    /**
     * Add a single country locale to restrict the results. This method can be called as many times
     * as needed inorder to add multiple countries.
     *
     * @param country limit geocoding results to one
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder country(Locale country) {
      countries.add(country.getCountry());
      return this;
    }

    /**
     * Limit results to one or more countries. Options are ISO 3166 alpha 2 country codes separated
     * by commas.
     *
     * @param country limit geocoding results to one
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder country(String... country) {
      countries.addAll(Arrays.asList(country));
      return this;
    }

    /**
     * Limit results to one or more countries. Options are ISO 3166 alpha 2 country codes separated
     * by commas.
     *
     * @param country limit geocoding results to one
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder country(String country);

    /**
     * This controls whether autocomplete results are included. Autocomplete results can partially
     * match the query: for example, searching for {@code washingto} could include washington even
     * though only the prefix matches. Autocomplete is useful for offering fast, type-ahead results
     * in user interfaces.
     * <p>
     * If your queries represent complete addresses or place names, you can disable this behavior
     * and exclude partial matches by setting this to false, the defaults true.
     *
     * @param autocomplete optionally set whether to allow returned results to attempt prediction of
     *                     the full words prior to the user completing the search terms
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder autocomplete(Boolean autocomplete);

    /**
     * Limit the results to a defined bounding box. Unlike {@link #proximity()}, this will strictly
     * limit results to within the bounding box only. If simple biasing is desired rather than a
     * strict region, use proximity instead.
     *
     * @param northeast the northeast corner of the bounding box as a {@link Point}
     * @param southwest the southwest corner of the bounding box as a {@link Point}
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public Builder bbox(Point southwest, Point northeast) {
      bbox(southwest.longitude(), southwest.latitude(),
        northeast.longitude(), northeast.latitude());
      return this;
    }

    /**
     * Limit the results to a defined bounding box. Unlike {@link #proximity()}, this will strictly
     * limit results to within the bounding box only. If simple biasing is desired rather than a
     * strict region, use proximity instead.
     *
     * @param minX the minX of bounding box when maps facing north
     * @param minY the minY of bounding box when maps facing north
     * @param maxX the maxX of bounding box when maps facing north
     * @param maxY the maxY of bounding box when maps facing north
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public Builder bbox(@FloatRange(from = -180, to = 180) double minX,
                        @FloatRange(from = -90, to = 90) double minY,
                        @FloatRange(from = -180, to = 180) double maxX,
                        @FloatRange(from = -90, to = 90) double maxY) {
      bbox(String.format(Locale.US, "%s,%s,%s,%s",
        TextUtils.formatCoordinate(minX),
        TextUtils.formatCoordinate(minY),
        TextUtils.formatCoordinate(maxX),
        TextUtils.formatCoordinate(maxY))
      );
      return this;
    }

    /**
     * Limit the results to a defined bounding box. Unlike {@link #proximity()}, this will strictly
     * limit results to within the bounding box only. If simple biasing is desired rather than a
     * strict region, use proximity instead.
     *
     * @param bbox a String defining the bounding box for biasing results ordered in
     *             {@code minX,minY,maxX,maxY}
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder bbox(@NonNull String bbox);

    /**
     * This optionally specifies the maximum number of results to return. For forward geocoding, the
     * default is 5 and the maximum is 10. For reverse geocoding, the default is 1 and the maximum
     * is 5. If a limit other than 1 is used for reverse geocoding, a single types option must also
     * be specified.
     *
     * @param limit the number of returned results
     * @return this builder for chaining options together
     * @since 2.0.0
     */
    public Builder limit(@IntRange(from = 1, to = 10) int limit) {
      limit(String.valueOf(limit));
      return this;
    }

    abstract Builder limit(String limit);

    /**
     * This optionally specifies the desired response language for user queries. For forward
     * geocodes, results that match the requested language are favored over results in other
     * languages. If more than one language tag is supplied, text in all requested languages will be
     * returned. For forward geocodes with more than one language tag, only the first language will
     * be used to weight results.
     * <p>
     * Any valid IETF language tag can be submitted, and a best effort will be made to return
     * results in the requested language or languages, falling back first to similar and then to
     * common languages in the event that text is not available in the requested language. In the
     * event a fallback language is used, the language field will have a different value than the
     * one requested.
     * <p>
     * Translation availability varies by language and region, for a full list of supported regions,
     * see the link provided below.
     *
     * @param languages one or more locale's specifying the language you'd like results to support
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/search/#language-coverage">Supported languages
     *   </a>
     * @since 2.0.0
     */
    public Builder languages(Locale... languages) {
      String[] languageStrings = new String[languages.length];
      for (int i = 0; i < languages.length; i++) {
        languageStrings[i] = languages[i].getLanguage();
      }
      languages(TextUtils.join(",", languageStrings));
      return this;
    }

    /**
     * This optionally specifies the desired response language for user queries. For forward
     * geocodes, results that match the requested language are favored over results in other
     * languages. If more than one language tag is supplied, text in all requested languages will be
     * returned. For forward geocodes with more than one language tag, only the first language will
     * be used to weight results.
     * <p>
     * Any valid IETF language tag can be submitted, and a best effort will be made to return
     * results in the requested language or languages, falling back first to similar and then to
     * common languages in the event that text is not available in the requested language. In the
     * event a fallback language is used, the language field will have a different value than the
     * one requested.
     * <p>
     * Translation availability varies by language and region, for a full list of supported regions,
     * see the link provided below.
     *
     * @param languages a String specifying the language or languages you'd like results to support
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/search/#language-coverage">Supported languages
     *   </a>
     * @since 2.0.0
     */
    public abstract Builder languages(String languages);

    /**
     * Set the factors that are used to sort nearby results.
     * Options avaliable to pass in include, {@link GeocodingCriteria#REVERSE_MODE_DISTANCE} for
     * nearest feature result (default) or {@link GeocodingCriteria#REVERSE_MODE_SCORE}
     * the notability of features within approximately 1 kilometer of the queried point
     * along with proximity.
     *
     * @param reverseMode limit geocoding results based on the reverseMode
     * @return this builder for chaining options together
     * @since 3.3.0
     */
    public abstract Builder reverseMode(
      @Nullable @GeocodingCriteria.GeocodingReverseModeCriteria String reverseMode);

    /**
     * Required to call when this is being built. If no access token provided,
     * {@link ServicesException} will be thrown.
     *
     * @param accessToken Mapbox access token, You must have a Mapbox account inorder to use
     *                    the Geocoding API
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder accessToken(@NonNull String accessToken);

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
     * @since 1.0.0
     */
    public abstract Builder baseUrl(@NonNull String baseUrl);

    abstract MapboxGeocoding autoBuild();

    /**
     * Build a new {@link MapboxGeocoding} object.
     *
     * @return a new {@link MapboxGeocoding} using the provided values in this builder
     * @since 3.0.0
     */
    public MapboxGeocoding build() {

      if (!countries.isEmpty()) {
        country(TextUtils.join(",", countries.toArray()));
      }

      // Generate build so that we can check that values are valid.
      MapboxGeocoding geocoding = autoBuild();

      if (!MapboxUtils.isAccessTokenValid(geocoding.accessToken())) {
        throw new ServicesException("Using Mapbox Services requires setting a valid access token.");
      }
      if (geocoding.query().isEmpty()) {
        throw new ServicesException("A query with at least one character or digit is required.");
      }

      if (geocoding.reverseMode() != null
        && geocoding.limit() != null && !geocoding.limit().equals("1")) {
        throw new ServicesException("Limit must be combined with a single type parameter");
      }
      return geocoding;
    }
  }
}
