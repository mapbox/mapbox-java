package com.mapbox.services.directions.v4;

import com.mapbox.services.directions.v4.models.DirectionsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by antonio on 1/30/16.
 */
@Deprecated
public interface DirectionsService {

    @GET("v4/directions/{profile}/{waypoints}.json")
    Call<DirectionsResponse> get(
            @Path("profile") String profile,
            @Path("waypoints") String waypoints,
            @Query("access_token") String accessToken,
            @Query("alternatives") boolean alternatives,
            @Query("instructions") String instructions,
            @Query("geometry") String geometry,
            @Query("steps") boolean steps
    );

}
