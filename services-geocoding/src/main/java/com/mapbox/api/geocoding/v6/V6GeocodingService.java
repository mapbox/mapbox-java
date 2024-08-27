package com.mapbox.api.geocoding.v6;

import com.mapbox.api.geocoding.v6.models.V6BatchResponse;
import com.mapbox.api.geocoding.v6.models.V6Response;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Forward geocoding HTTP interface.
 */
interface V6GeocodingService {

  /**
   * Constructs the http call to the /forward endpoint.
   *
   * @param userAgent    the user agent
   * @param query        {@link V6ForwardGeocodingRequestOptions#query()}
   * @param accessToken  {@link MapboxV6Geocoding#accessToken()}
   * @param permanent    {@link MapboxV6Geocoding#permanent()}
   * @param autocomplete {@link V6ForwardGeocodingRequestOptions#autocomplete()}
   * @param bbox         {@link V6ForwardGeocodingRequestOptions#bbox()}
   * @param country      {@link V6ForwardGeocodingRequestOptions#country()}
   * @param language     {@link V6ForwardGeocodingRequestOptions#language()}
   * @param limit        {@link V6ForwardGeocodingRequestOptions#limit()}
   * @param proximity    {@link V6ForwardGeocodingRequestOptions#proximity()}
   * @param types        {@link V6ForwardGeocodingRequestOptions#types()}
   * @param worldview    {@link V6ForwardGeocodingRequestOptions#worldview()}
   * @return A retrofit Call object
   */
  @GET("/search/geocode/v6/forward")
  Call<V6Response> forwardGeocoding(
    @Header("User-Agent") String userAgent,
    @Query("q") String query,
    @Query("access_token") String accessToken,
    @Query("permanent") Boolean permanent,
    @Query("autocomplete") Boolean autocomplete,
    @Query("bbox") String bbox,
    @Query("country") String country,
    @Query("language") String language,
    @Query("limit") Integer limit,
    @Query("proximity") String proximity,
    @Query("types") String types,
    @Query("worldview") String worldview
  );

  /**
   * Constructs the http call to the /forward endpoint.
   *
   * @param userAgent     the user agent
   * @param accessToken   {@link MapboxV6Geocoding#accessToken()}
   * @param addressLine1  {@link V6StructuredInputQuery#addressLine1()}
   * @param addressNumber {@link V6StructuredInputQuery#addressNumber()}
   * @param street        {@link V6StructuredInputQuery#street()}
   * @param block         {@link V6StructuredInputQuery#block()}
   * @param place         {@link V6StructuredInputQuery#place()}
   * @param region        {@link V6StructuredInputQuery#region()}
   * @param postcode      {@link V6StructuredInputQuery#postcode()}
   * @param locality      {@link V6StructuredInputQuery#locality()}
   * @param neighborhood  {@link V6StructuredInputQuery#neighborhood()}
   * @param permanent     {@link MapboxV6Geocoding#permanent()}
   * @param autocomplete  {@link V6ForwardGeocodingRequestOptions#autocomplete()}
   * @param bbox          {@link V6ForwardGeocodingRequestOptions#bbox()}
   * @param country       {@link V6StructuredInputQuery#country()}
   * @param language      {@link V6ForwardGeocodingRequestOptions#language()}
   * @param limit         {@link V6ForwardGeocodingRequestOptions#limit()}
   * @param proximity     {@link V6ForwardGeocodingRequestOptions#proximity()}
   * @param types         {@link V6ForwardGeocodingRequestOptions#types()}
   * @param worldview     {@link V6ForwardGeocodingRequestOptions#worldview()}
   * @return A retrofit Call object
   */
  @GET("/search/geocode/v6/forward")
  Call<V6Response> structureInputForwardGeocoding(
    @Header("User-Agent") String userAgent,
    @Query("access_token") String accessToken,
    @Query("address_line1") String addressLine1,
    @Query("address_number") String addressNumber,
    @Query("street") String street,
    @Query("block") String block,
    @Query("place") String place,
    @Query("region") String region,
    @Query("postcode") String postcode,
    @Query("locality") String locality,
    @Query("neighborhood") String neighborhood,
    @Query("permanent") Boolean permanent,
    @Query("autocomplete") Boolean autocomplete,
    @Query("bbox") String bbox,
    @Query("country") String country,
    @Query("language") String language,
    @Query("limit") Integer limit,
    @Query("proximity") String proximity,
    @Query("types") String types,
    @Query("worldview") String worldview
  );

  /**
   * Constructs the html call to the /reverse endpoint.
   *
   * @param userAgent   the user agent
   * @param accessToken {@link MapboxV6Geocoding#accessToken()}
   * @param longitude   {@link V6ReverseGeocodingRequestOptions#longitude()}
   * @param latitude    {@link V6ReverseGeocodingRequestOptions#latitude()}
   * @param permanent   {@link MapboxV6Geocoding#permanent()}
   * @param country     {@link V6ReverseGeocodingRequestOptions#country()}
   * @param language    {@link V6ReverseGeocodingRequestOptions#language()}
   * @param limit       {@link V6ReverseGeocodingRequestOptions#limit()}
   * @param types       {@link V6ReverseGeocodingRequestOptions#types()}
   * @param worldview   {@link V6ReverseGeocodingRequestOptions#worldview()}
   * @return A retrofit Call object
   */
  @GET("/search/geocode/v6/reverse")
  Call<V6Response> reverseGeocoding(
    @Header("User-Agent") String userAgent,
    @Query("access_token") String accessToken,
    @Query("longitude") Double longitude,
    @Query("latitude") Double latitude,
    @Query("permanent") Boolean permanent,
    @Query("country") String country,
    @Query("language") String language,
    @Query("limit") Integer limit,
    @Query("types") String types,
    @Query("worldview") String worldview
  );

  /**
   * Constructs the html call to the /batch endpoint.
   *
   * @param userAgent   the user agent
   * @param accessToken {@link MapboxV6BatchGeocoding#accessToken()}
   * @param permanent   {@link MapboxV6BatchGeocoding#permanent()}
   * @param body        json serialized {@link MapboxV6BatchGeocoding#requestOptions()}
   * @return A retrofit Call object
   */
  @POST("/search/geocode/v6/batch")
  Call<V6BatchResponse> batchGeocoding(
    @Header("User-Agent") String userAgent,
    @Query("access_token") String accessToken,
    @Query("permanent") Boolean permanent,
    @Body RequestBody body
  );
}
