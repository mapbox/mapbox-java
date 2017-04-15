package com.mapbox.services.api.directions.v5;

import com.mapbox.services.api.directions.v5.models.DirectionsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the directions service (v5).
 *
 * @since 1.0.0
 */
public interface DirectionsService {

  /**
   * Call-based interface
   *
   * @param userAgent        The user.
   * @param user             The user.
   * @param profile          The profile directions should use.
   * @param coordinates      The coordinates the route should follow.
   * @param accessToken      Mapbox access token.
   * @param alternatives     Define whether you want to recieve more then one route.
   * @param geometries       Route geometry.
   * @param overview         Route full, simplified, etc.
   * @param radiuses         start at the most efficient point within the radius.
   * @param steps            Define if you'd like the route steps.
   * @param bearings         Used to filter the road segment the waypoint will be placed on by direction and dictates
   *                         the angle of approach
   * @param continueStraight Define whether the route should continue straight even if the route
   *                         will be slower.  @return A retrofit Call object
   * @param annotations      An annotations object that contains additional details about each line segment along the
   *                         route geometry. Each entry in an annotations field corresponds to a coordinate along the
   *                         route geometry.
   * @return The {@link DirectionsResponse} in a Call wrapper.
   * @since 1.0.0
   */
  @GET("directions/v5/{user}/{profile}/{coordinates}")
  Call<DirectionsResponse> getCall(
    // NOTE: DirectionsServiceRx should be updated as well
    @Header("User-Agent") String userAgent,
    @Path("user") String user,
    @Path("profile") String profile,
    @Path("coordinates") String coordinates,
    @Query("access_token") String accessToken,
    @Query("alternatives") Boolean alternatives,
    @Query("geometries") String geometries,
    @Query("overview") String overview,
    @Query("radiuses") String radiuses,
    @Query("steps") Boolean steps,
    @Query("bearings") String bearings,
    @Query("continue_straight") Boolean continueStraight,
    @Query("annotations") String annotations
  );
}
