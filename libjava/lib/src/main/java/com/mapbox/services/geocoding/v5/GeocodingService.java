package com.mapbox.services.geocoding.v5;

import com.mapbox.services.geocoding.v5.models.GeocodingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Interface that defines the geocoding service.
 */
public interface GeocodingService {

    /**
     * Call-based interface
     *
     * @param mode
     * @param query
     * @param accessToken
     * @param country
     * @param proximity
     * @param types
     * @param autocomplete
     * @param bbox
     * @return A retrofit Call object
     */
    @GET("/geocoding/v5/{mode}/{query}.json")
    Call<GeocodingResponse> getCall(
            @Header("User-Agent") String userAgent,
            @Path("mode") String mode,
            @Path("query") String query,
            @Query("access_token") String accessToken,
            @Query("country") String country,
            @Query("proximity") String proximity,
            @Query("types") String types,
            @Query("autocomplete") Boolean autocomplete,
            @Query("bbox") String bbox);

    /**
     * RxJava-based interface
     *
     * @param mode
     * @param query
     * @param accessToken
     * @param country
     * @param proximity
     * @param types
     * @param autocomplete
     * @param bbox
     * @return A RxJava Observable object
     */
    @GET("/geocoding/v5/{mode}/{query}.json")
    Observable<GeocodingResponse> getObservable(
            @Header("User-Agent") String userAgent,
            @Path("mode") String mode,
            @Path("query") String query,
            @Query("access_token") String accessToken,
            @Query("country") String country,
            @Query("proximity") String proximity,
            @Query("types") String types,
            @Query("autocomplete") Boolean autocomplete,
            @Query("bbox") String bbox);

}
