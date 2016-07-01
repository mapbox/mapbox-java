package com.mapbox.services.directions.v4;

import com.mapbox.services.Constants;
import com.mapbox.services.directions.v4.models.DirectionsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Interface that defines the directions service (v4).
 */
public interface DirectionsService {

    /**
     * Call-based interface
     *
     * @param profile
     * @param waypoints
     * @param accessToken
     * @param alternatives
     * @param instructions
     * @param geometry
     * @param steps
     * @return A retrofit Call object
     */
    @Headers(Constants.HEADER_USER_AGENT)
    @GET("v4/directions/{profile}/{waypoints}.json")
    Call<DirectionsResponse> getCall(
            @Path("profile") String profile,
            @Path("waypoints") String waypoints,
            @Query("access_token") String accessToken,
            @Query("alternatives") Boolean alternatives,
            @Query("instructions") String instructions,
            @Query("geometry") String geometry,
            @Query("steps") Boolean steps
    );

    /**
     * RxJava-based interface
     *
     * @param profile
     * @param waypoints
     * @param accessToken
     * @param alternatives
     * @param instructions
     * @param geometry
     * @param steps
     * @return A RxJava Observable object
     */
    @Headers(Constants.HEADER_USER_AGENT)
    @GET("v4/directions/{profile}/{waypoints}.json")
    Observable<DirectionsResponse> getObservable(
            @Path("profile") String profile,
            @Path("waypoints") String waypoints,
            @Query("access_token") String accessToken,
            @Query("alternatives") Boolean alternatives,
            @Query("instructions") String instructions,
            @Query("geometry") String geometry,
            @Query("steps") Boolean steps
    );

}
