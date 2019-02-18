package com.mapbox.api.directions.v5;

import com.mapbox.api.directions.v5.models.DirectionsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the directions service (v5).
 *
 * @since 1.0.0
 */
public interface DirectionsService {

  /**
   * Constructs the html get call using the information passed in through the
   * {@link MapboxDirections.Builder}.
   *
   * @param userAgent          the user agent
   * @param user               the user
   * @param profile            the profile directions should use
   * @param coordinates        the coordinates the route should follow
   * @param accessToken        Mapbox access token
   * @param alternatives       define whether you want to receive more then one route
   * @param geometries         route geometry
   * @param overview           route full, simplified, etc.
   * @param radiuses           start at the most efficient point within the radius
   * @param steps              define if you'd like the route steps
   * @param bearings           used to filter the road segment the waypoint will be placed on by
   *                           direction and dictates the angle of approach
   * @param continueStraight   define whether the route should continue straight even if the
   *                           route will be slower
   * @param annotations        an annotations object that contains additional details about each
   *                           line segment along the route geometry. Each entry in an
   *                           annotations field corresponds to a coordinate along the route
   *                           geometry
   * @param language           language of returned turn-by-turn text instructions
   * @param roundaboutExits    Add extra step when roundabouts occur with additional information
   *                           for the user
   * @param voiceInstructions  request that the response contain voice instruction information,
   *                           useful for navigation
   * @param bannerInstructions request that the response contain banner instruction information,
   *                           useful for navigation
   * @param voiceUnits         voice units
   * @param exclude            exclude tolls, motorways or more along your route
   * @param approaches         which side of the road to approach a waypoint
   * @param waypointIndices    which input coordinates should be treated as waypoints/separate legs.
   *                           Note: coordinate indices not added here act as silent waypoints
   * @param waypointNames      custom names for waypoints used for the arrival instruction
   * @param waypointTargets    list of coordinate pairs for drop-off locations
   * @param enableRefresh      whether the routes should be refreshable
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
    @Query("voice_units") String voiceUnits,
    @Query("exclude") String exclude,
    @Query("approaches") String approaches,
    @Query("waypoints") String waypointIndices,
    @Query("waypoint_names") String waypointNames,
    @Query("waypoint_targets") String waypointTargets,
    @Query("enable_refresh") Boolean enableRefresh
  );

  /**
   * Constructs the post html call using the information passed in through the
   * {@link MapboxDirections.Builder}.
   *
   * @param userAgent          the user agent
   * @param user               the user
   * @param profile            the profile directions should use
   * @param coordinates        the coordinates the route should follow
   * @param accessToken        Mapbox access token
   * @param alternatives       define whether you want to receive more then one route
   * @param geometries         route geometry
   * @param overview           route full, simplified, etc.
   * @param radiuses           start at the most efficient point within the radius
   * @param steps              define if you'd like the route steps
   * @param bearings           used to filter the road segment the waypoint will be placed on by
   *                           direction and dictates the angle of approach
   * @param continueStraight   define whether the route should continue straight even if the
   *                           route will be slower
   * @param annotations        an annotations object that contains additional details about each
   *                           line segment along the route geometry. Each entry in an
   *                           annotations field corresponds to a coordinate along the route
   *                           geometry
   * @param language           language of returned turn-by-turn text instructions
   * @param roundaboutExits    Add extra step when roundabouts occur with additional information
   *                           for the user
   * @param voiceInstructions  request that the response contain voice instruction information,
   *                           useful for navigation
   * @param bannerInstructions request that the response contain banner instruction information,
   *                           useful for navigation
   * @param voiceUnits         voice units
   * @param exclude            exclude tolls, motorways or more along your route
   * @param approaches         which side of the road to approach a waypoint
   * @param waypointIndices    which input coordinates should be treated as waypoints/separate legs.
   *                           Note: coordinate indices not added here act as silent waypoints
   * @param waypointNames      custom names for waypoints used for the arrival instruction
   * @param waypointTargets    list of coordinate pairs for drop-off locations
   * @param enableRefresh      whether the routes should be refreshable
   * @return the {@link DirectionsResponse} in a Call wrapper
   * @since 4.6.0
   */
  @FormUrlEncoded
  @POST("directions/v5/{user}/{profile}")
  Call<DirectionsResponse> postCall(
    @Header("User-Agent") String userAgent,
    @Path("user") String user,
    @Path("profile") String profile,
    @Field("coordinates") String coordinates,
    @Query("access_token") String accessToken,
    @Field("alternatives") Boolean alternatives,
    @Field("geometries") String geometries,
    @Field("overview") String overview,
    @Field("radiuses") String radiuses,
    @Field("steps") Boolean steps,
    @Field("bearings") String bearings,
    @Field("continue_straight") Boolean continueStraight,
    @Field("annotations") String annotations,
    @Field("language") String language,
    @Field("roundabout_exits") Boolean roundaboutExits,
    @Field("voice_instructions") Boolean voiceInstructions,
    @Field("banner_instructions") Boolean bannerInstructions,
    @Field("voice_units") String voiceUnits,
    @Field("exclude") String exclude,
    @Field("approaches") String approaches,
    @Field("waypoints") String waypointIndices,
    @Field("waypoint_names") String waypointNames,
    @Field("waypoint_targets") String waypointTargets,
    @Field("enable_refresh") Boolean enableRefresh
  );
}
