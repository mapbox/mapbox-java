package com.mapbox.directions.v5;

import com.mapbox.directions.v5.models.DirectionsResponse;

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
   * Constructs the html call using the information passed in through the
   * {@link MapboxDirections.Builder}.
   *
   * @param userAgent         the user
   * @param user              the user
   * @param profile           the profile directions should use
   * @param coordinates       the coordinates the route should follow
   * @param accessToken       Mapbox access token
   * @param alternatives      define whether you want to receive more then one route
   * @param geometries        route geometry
   * @param overview          route full, simplified, etc.
   * @param radiuses          start at the most efficient point within the radius
   * @param steps             define if you'd like the route steps
   * @param bearings          used to filter the road segment the waypoint will be placed on by
   *                          direction and dictates the angle of approach
   * @param continueStraight  define whether the route should continue straight even if the route
   *                          will be slower
   * @param annotations       an annotations object that contains additional details about each line
   *                          segment along the route geometry. Each entry in an annotations field
   *                          corresponds to a coordinate along the route geometry
   * @param language          language of returned turn-by-turn text instructions
   * @param roundaboutExits   Add extra step when roundabouts occur with additional information for
   *                          the user
   * @param voiceInstructions request that the response contain voice instruction information,
   *                          useful for navigation
   * @param exclude           exclude tolls, motorways or more along your route
   * @return the {@link DirectionsResponse} in a Call wrapper
   * @since 1.0.0
   */
  @GET("directions/v5/{user}/{profile}/{coordinates}")
  Call<DirectionsResponse> getCall(
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
    @Query("annotations") String annotations,
    @Query("language") String language,
    @Query("roundabout_exits") Boolean roundaboutExits,
    @Query("voice_instructions") Boolean voiceInstructions,
    @Query("banner_instructions") Boolean bannerInstructions,
    @Query("exclude") String exclude
  );
}
