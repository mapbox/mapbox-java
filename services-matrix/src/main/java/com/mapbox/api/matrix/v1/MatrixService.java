package com.mapbox.api.matrix.v1;

import com.mapbox.api.matrix.v1.models.MatrixResponse;

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
public interface MatrixService {

  /**
   * Call-based interface.
   *
   * @param userAgent    the user
   * @param user         the user
   * @param profile      the profile directions should use
   * @param coordinates  the coordinates the route should follow
   * @param accessToken  Mapbox access token
   * @param annotations  Used to specify the resulting matrices.
   *                     Possible values are: duration (default),
   *                     distance, or both values separated by comma
   * @param approaches   A semicolon-separated list indicating the side of the road from
   *                     which to approach waypoints in a requested route.
   *                     Accepts  unrestricted (default, route can arrive at the waypoint
   *                     from either side of the road) or  curb (route will arrive at
   *                     the waypoint on the  driving_side of the region).
   *                     If provided, the number of approaches must be the same
   *                     as the number of waypoints.
   *                     However, you can skip a coordinate and
   *                     show its position in the list with the  ; separator.
   * @param destinations array of waypoint objects. Each waypoints is an input coordinate snapped to
   *                     the road and path network. The waypoints appear in the array in the order
   *                     of the input coordinates, or in the order as specified in the destinations
   *                     query parameter
   * @param sources      array of waypoint objects. Each waypoints is an input coordinate snapped to
   *                     the road and path network. The waypoints appear in the array in the order
   *                     of the input coordinates, or in the order as specified in the sources query
   *                     parameter
   * @return the {@link MatrixResponse} in a Call wrapper
   * @since 2.1.0
   */
  @GET("directions-matrix/v1/{user}/{profile}/{coordinates}")
  Call<MatrixResponse> getCall(
    // NOTE: DirectionsMatrixServiceRx should be updated as well
    @Header("User-Agent") String userAgent,
    @Path("user") String user,
    @Path("profile") String profile,
    @Path("coordinates") String coordinates,
    @Query("access_token") String accessToken,
    @Query("annotations") String annotations,
    @Query("approaches") String approaches,
    @Query("destinations") String destinations,
    @Query("sources") String sources
  );
}
