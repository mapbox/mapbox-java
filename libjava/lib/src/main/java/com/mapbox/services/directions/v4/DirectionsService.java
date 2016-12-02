package com.mapbox.services.directions.v4;

import com.mapbox.services.directions.v4.models.DirectionsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the directions service (v4).
 *
 * @since 1.0.0
 */
@Deprecated
public interface DirectionsService {

  /**
   * Call-based interface
   *
   * @param userAgent    The user.
   * @param profile      The profile directions should use.
   * @param waypoints    The waypoints the route should follow.
   * @param accessToken  Mapbox access token.
   * @param alternatives Define whether you want to recieve more then one route.
   * @param instructions Define if you'd like to recieve route instructions.
   * @param geometry     Route geometry.
   * @param steps        Define if you'd like the route steps.
   * @return A retrofit Call object
   * @since 1.0.0
   */
  @GET("v4/directions/{profile}/{waypoints}.json")
  Call<DirectionsResponse> getCall(
    @Header("User-Agent") String userAgent,
    @Path("profile") String profile,
    @Path("waypoints") String waypoints,
    @Query("access_token") String accessToken,
    @Query("alternatives") Boolean alternatives,
    @Query("instructions") String instructions,
    @Query("geometry") String geometry,
    @Query("steps") Boolean steps
  );
}
