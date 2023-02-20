package com.mapbox.api.geocoding.v6;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface V6GeocodingService {

    // TODO add worldview and permanent query parameters

    /**
     * Constructs the html call using the information passed in through the
     * {@link MapboxGeocodingV6.Builder}.
     *
     * @param userAgent    The user
     * @param query        a location; a place name for forward geocoding or a coordinate pair
     *                     (longitude, latitude location) for reverse geocoding
     * @param accessToken  Mapbox access token.
     * @param country      ISO 3166 alpha 2 country codes, separated by commas.
     * @param proximity    Location around which to bias results.
     * @param types        Filter results by one or more type.
     * @param autocomplete True if you want auto complete.
     * @param bbox         Optionally pass in a bounding box to limit results in.
     * @param limit        Optionally pass in a limit the amount of returning results.
     * @param language     The locale in which results should be returned.
     * @return A retrofit Call object
     */
    @GET("/search/geocode/v6/forward")
    Call<V6Response> getCall(
            @Header("User-Agent") String userAgent,
            @Query("q") String query,
            @Query("access_token") String accessToken,
            @Query("country") String country,
            @Query("proximity") String proximity,
            @Query("types") String types,
            @Query("autocomplete") Boolean autocomplete,
            @Query("bbox") String bbox,
            @Query("limit") String limit,
            @Query("language") String language
    );
}
