package com.mapbox.services.mapmatching.v4;

import com.mapbox.services.Constants;
import com.mapbox.services.mapmatching.v4.models.MapMatchingResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ivo on 17/05/16.
 */
public interface MapMatchingService {

    /**
     * Call based interface
     *
     * @param profile      directions profile id
     * @param geometry     format for the returned geometry (optional)
     * @param gpsPrecision assumed precission in meters of the used tracking device
     * @return The MapMatchingResponse in a Call wrapper
     */
    @Headers(Constants.HEADER_USER_AGENT)
    @POST("matching/v4/{profile}.json")
    Call<MapMatchingResponse> getCall(
            @Path("profile") String profile,
            @Query("access_token") String accessToken,
            @Query("geometry") String geometry,
            @Query("gps_precision") Integer gpsPrecision,
            @Body RequestBody trace);

    /**
     * RxJava-based interface
     *
     * @param profile      directions profile id
     * @param geometry     format for the returned geometry (optional)
     * @param gpsPrecision assumed precision in meters of the used tracking device
     * @return The MapMatchingResponse in an RX Java Observable
     */
    @Headers(Constants.HEADER_USER_AGENT)
    @POST("matching/v4/{profile}.json")
    Observable<MapMatchingResponse> getObservable(
            @Path("profile") String profile,
            @Query("access_token") String accessToken,
            @Query("geometry") String geometry,
            @Query("gps_precision") Integer gpsPrecision,
            @Body RequestBody trace);
}
