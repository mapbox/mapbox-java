package com.mapbox.services.api.directionsmatrix.v1;

import com.mapbox.services.api.directionsmatrix.v1.models.DirectionsMatrixResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the directions matrix service (v1).
 *
 * @since 2.1.0
 */
public interface DirectionsMatrixService {

  /**
   * Call-based interface
   *
   * @param userAgent    The user.
   * @param user         The user.
   * @param profile      The profile directions should use.
   * @param coordinates  The coordinates the route should follow.
   * @param accessToken  Mapbox access token.
   * @param destinations Array of waypoint objects. Each waypoints is an input coordinate snapped to the road and path
   *                     network. The waypoints appear in the array in the order of the input coordinates, or in the
   *                     order as specified in the destinations query parameter.
   * @param sources      Array of waypoint objects. Each waypoints is an input coordinate snapped to the road and path
   *                     network. The waypoints appear in the array in the order of the input coordinates, or in the
   *                     order as specified in the sources query parameter.
   * @since 2.1.0
   */
  @GET("directions-matrix/v1/{user}/{profile}/{coordinates}")
  Call<DirectionsMatrixResponse> getCall(
    // NOTE: DirectionsMatrixServiceRx should be updated as well
    @Header("User-Agent") String userAgent,
    @Path("user") String user,
    @Path("profile") String profile,
    @Path("coordinates") String coordinates,
    @Query("access_token") String accessToken,
    @Query("destinations") String destinations,
    @Query("sources") String sources
    );
}
