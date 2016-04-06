package com.mapbox.services.directions.v5;

import com.mapbox.services.directions.v5.models.DirectionsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by antonio on 3/4/16.
 */
public interface DirectionsService {

    @GET("directions/v5/{user}/{profile}/{coordinates}")
    Call<DirectionsResponse> get(
            @Path("user") String user,
            @Path("profile") String profile,
            @Path("coordinates") String coordinates,
            @Query("access_token") String accessToken,
            @Query("alternative") Boolean alternative,
            @Query("bearings") String bearings,
            @Query("geometries") String geometries,
            @Query("overview") String overview,
            @Query("radiuses") String radiuses,
            @Query("steps") Boolean steps,
            @Query("uturns") Boolean uturns
    );

}
