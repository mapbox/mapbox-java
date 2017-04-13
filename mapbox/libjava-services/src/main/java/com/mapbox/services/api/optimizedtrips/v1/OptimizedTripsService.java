package com.mapbox.services.api.optimizedtrips.v1;

import com.mapbox.services.api.optimizedtrips.v1.models.OptimizedTripsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the Optimized Trips service (v1).
 *
 * @since 2.1.0
 */
public interface OptimizedTripsService {

  /**
   * @param userAgent
   * @param user
   * @param profile
   * @param coordinates
   * @param accessToken
   * @param roundTrip
   * @param radiuses
   * @param bearings
   * @param steps
   * @param geometries
   * @param annotations
   * @param destination
   * @param sources
   * @return
   * @since 2.1.0
   */
  @GET("optimized-trips/v1/{user}/{profile}/{coordinates}")
  Call<OptimizedTripsResponse> getCall(
    // NOTE: OptimizedTripsServiceRx should be updated as well
    @Header("User-Agent") String userAgent,
    @Path("user") String user,
    @Path("profile") String profile,
    @Path("coordinates") List coordinates,
    @Query("access_token") String accessToken,
    @Query("roundtrip") Boolean roundTrip,
    @Query("radiuses") double[] radiuses,
    @Query("bearings") double[][] bearings,
    @Query("steps") Boolean steps,
    @Query("geometries") String geometries,
    @Query("annotations") String[] annotations,
    @Query("destination") String destination,
    @Query("sources") String sources
  );
}


