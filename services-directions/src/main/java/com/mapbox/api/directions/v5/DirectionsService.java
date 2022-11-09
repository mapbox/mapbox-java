package com.mapbox.api.directions.v5;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.RouteOptions;

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
 */
interface DirectionsService {

  /**
   * Request definition.
   *
   * @param userAgent                     the user agent
   * @param user                          {@link RouteOptions#user()}
   * @param profile                       {@link RouteOptions#profile()}
   * @param coordinates                   {@link RouteOptions#coordinatesList()}
   * @param accessToken                   the access token
   * @param alternatives                  {@link RouteOptions#alternatives()}
   * @param geometries                    {@link RouteOptions#geometries()}
   * @param overview                      {@link RouteOptions#overview()}
   * @param radiuses                      {@link RouteOptions#radiuses()}
   * @param steps                         {@link RouteOptions#steps()}
   * @param bearings                      {@link RouteOptions#bearings()}
   * @param avoidManeuverRadius           {@link RouteOptions#avoidManeuverRadius()}
   * @param layers                        {@link RouteOptions#layers()}
   * @param continueStraight              {@link RouteOptions#continueStraight()}
   * @param annotations                   {@link RouteOptions#annotations()}
   * @param language                      {@link RouteOptions#language()}
   * @param roundaboutExits               {@link RouteOptions#roundaboutExits()}
   * @param voiceInstructions             {@link RouteOptions#voiceInstructions()}
   * @param bannerInstructions            {@link RouteOptions#bannerInstructions()}
   * @param voiceUnits                    {@link RouteOptions#voiceUnits()}
   * @param exclude                       {@link RouteOptions#exclude()}
   * @param include                       {@link RouteOptions#include()}
   * @param approaches                    {@link RouteOptions#approaches()}
   * @param waypointIndices               {@link RouteOptions#waypointIndices()}
   * @param waypointNames                 {@link RouteOptions#waypointNames()}
   * @param waypointTargets               {@link RouteOptions#waypointTargets()}
   * @param enableRefresh                 {@link RouteOptions#enableRefresh()}
   * @param walkingSpeed                  {@link RouteOptions#walkingSpeed()}
   * @param walkwayBias                   {@link RouteOptions#walkwayBias()}
   * @param alleyBias                     {@link RouteOptions#alleyBias()}
   * @param snappingIncludeClosures       {@link RouteOptions#snappingIncludeClosures()}
   * @param snappingIncludeStaticClosures {@link RouteOptions#snappingIncludeStaticClosures()}
   * @param arriveBy                      {@link RouteOptions#arriveBy()}
   * @param departAt                      {@link RouteOptions#departAt()}
   * @param maxHeight                     {@link RouteOptions#maxHeight()}
   * @param maxWidth                      {@link RouteOptions#maxWidth()}
   * @param computeTollCost               {@link RouteOptions#computeTollCost()}
   * @param metadata                      {@link RouteOptions#metadata()}
   * @return the {@link DirectionsResponse} in a Call wrapper
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
    @Query("avoid_maneuver_radius") Double avoidManeuverRadius,
    @Query("layers") String layers,
    @Query("continue_straight") Boolean continueStraight,
    @Query("annotations") String annotations,
    @Query("language") String language,
    @Query("roundabout_exits") Boolean roundaboutExits,
    @Query("voice_instructions") Boolean voiceInstructions,
    @Query("banner_instructions") Boolean bannerInstructions,
    @Query("voice_units") String voiceUnits,
    @Query("exclude") String exclude,
    @Query("include") String include,
    @Query("approaches") String approaches,
    @Query("waypoints") String waypointIndices,
    @Query("waypoint_names") String waypointNames,
    @Query("waypoint_targets") String waypointTargets,
    @Query("enable_refresh") Boolean enableRefresh,
    @Query("walking_speed") Double walkingSpeed,
    @Query("walkway_bias") Double walkwayBias,
    @Query("alley_bias") Double alleyBias,
    @Query("snapping_include_closures") String snappingIncludeClosures,
    @Query("snapping_include_static_closures") String snappingIncludeStaticClosures,
    @Query("arrive_by") String arriveBy,
    @Query("depart_at") String departAt,
    @Query("max_height") Double maxHeight,
    @Query("max_width") Double maxWidth,
    @Query("max_weight") Double maxWeight,
    @Query("compute_toll_cost") Boolean computeTollCost,
    @Query("metadata") Boolean metadata
  );

  /**
   * Request definition.
   *
   * @param userAgent                     the user agent
   * @param user                          {@link RouteOptions#user()}
   * @param profile                       {@link RouteOptions#profile()}
   * @param coordinates                   {@link RouteOptions#coordinatesList()}
   * @param accessToken                   the access token
   * @param alternatives                  {@link RouteOptions#alternatives()}
   * @param geometries                    {@link RouteOptions#geometries()}
   * @param overview                      {@link RouteOptions#overview()}
   * @param radiuses                      {@link RouteOptions#radiuses()}
   * @param steps                         {@link RouteOptions#steps()}
   * @param bearings                      {@link RouteOptions#bearings()}
   * @param avoidManeuverRadius           {@link RouteOptions#avoidManeuverRadius()}
   * @param layers                        {@link RouteOptions#layers()}
   * @param continueStraight              {@link RouteOptions#continueStraight()}
   * @param annotations                   {@link RouteOptions#annotations()}
   * @param language                      {@link RouteOptions#language()}
   * @param roundaboutExits               {@link RouteOptions#roundaboutExits()}
   * @param voiceInstructions             {@link RouteOptions#voiceInstructions()}
   * @param bannerInstructions            {@link RouteOptions#bannerInstructions()}
   * @param voiceUnits                    {@link RouteOptions#voiceUnits()}
   * @param exclude                       {@link RouteOptions#exclude()}
   * @param include                       {@link RouteOptions#include()}
   * @param approaches                    {@link RouteOptions#approaches()}
   * @param waypointIndices               {@link RouteOptions#waypointIndices()}
   * @param waypointNames                 {@link RouteOptions#waypointNames()}
   * @param waypointTargets               {@link RouteOptions#waypointTargets()}
   * @param enableRefresh                 {@link RouteOptions#enableRefresh()}
   * @param walkingSpeed                  {@link RouteOptions#walkingSpeed()}
   * @param walkwayBias                   {@link RouteOptions#walkwayBias()}
   * @param alleyBias                     {@link RouteOptions#alleyBias()}
   * @param snappingIncludeClosures       {@link RouteOptions#snappingIncludeClosures()}
   * @param snappingIncludeStaticClosures {@link RouteOptions#snappingIncludeStaticClosures()}
   * @param arriveBy                      {@link RouteOptions#arriveBy()}
   * @param departAt                      {@link RouteOptions#departAt()}
   * @param maxHeight                     {@link RouteOptions#maxHeight()}
   * @param maxWidth                      {@link RouteOptions#maxWidth()}
   * @param computeTollCost               {@link RouteOptions#computeTollCost()}
   * @param metadata                      {@link RouteOptions#metadata()}
   * @return the {@link DirectionsResponse} in a Call wrapper
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
    @Field("avoid_maneuver_radius") Double avoidManeuverRadius,
    @Field("layers") String layers,
    @Field("continue_straight") Boolean continueStraight,
    @Field("annotations") String annotations,
    @Field("language") String language,
    @Field("roundabout_exits") Boolean roundaboutExits,
    @Field("voice_instructions") Boolean voiceInstructions,
    @Field("banner_instructions") Boolean bannerInstructions,
    @Field("voice_units") String voiceUnits,
    @Field("exclude") String exclude,
    @Field("include") String include,
    @Field("approaches") String approaches,
    @Field("waypoints") String waypointIndices,
    @Field("waypoint_names") String waypointNames,
    @Field("waypoint_targets") String waypointTargets,
    @Field("enable_refresh") Boolean enableRefresh,
    @Field("walking_speed") Double walkingSpeed,
    @Field("walkway_bias") Double walkwayBias,
    @Field("alley_bias") Double alleyBias,
    @Field("snapping_include_closures") String snappingIncludeClosures,
    @Field("snapping_include_static_closures") String snappingIncludeStaticClosures,
    @Field("arrive_by") String arriveBy,
    @Field("depart_at") String departAt,
    @Field("max_height") Double maxHeight,
    @Field("max_width") Double maxWidth,
    @Field("max_weight") Double maxWeight,
    @Field("compute_toll_cost") Boolean computeTollCost,
    @Field("metadata") Boolean metadata
  );
}
