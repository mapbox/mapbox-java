package com.mapbox.api.matching.v5;

import com.mapbox.api.matching.v5.models.MapMatchingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the map matching service.
 *
 * @since 2.0.0
 */
public interface MapMatchingService {

  /**
   * Constructs the html call using the information passed in through the
   * {@link MapboxMapMatching.Builder}.
   *
   * @param userAgent   user agent
   * @param user        user
   * @param profile     directions profile ID; either mapbox/driving, mapbox/walking,
   *                    or mapbox/cycling
   * @param coordinates inaccurate traces from a GPS unit or a phone
   * @param accessToken Mapbox access token
   * @param geometries  format of the returned geometry. Allowed values are: geojson
   *                    (as LineString), polyline with precision 5, polyline6. The default
   *                    value is polyline
   * @param radiuses    a list of integers in meters indicating the assumed precision of
   *                    the used tracking device. There must be as many radiuses as there
   *                    are coordinates in the request, each separated by ;. Values can be
   *                    a number between 0 and 30. Use higher numbers (20-30) for noisy
   *                    traces and lower numbers (1-10) for clean traces. The default value
   *                    is 5
   * @param steps       whether to return steps and turn-by-turn instructions. Can be true
   *                    or false. The default is false
   * @param overview    type of returned overview geometry. Can be full (the most detailed
   *                    geometry available), simplified (a simplified version of the full
   *                    geometry), or false (no overview geometry). The default is simplified
   * @param timestamps  timestamps corresponding to each coordinate provided in the request;
   *                    must be numbers in Unix time (seconds since the Unix epoch). There
   *                    must be as many timestamps as there are coordinates in the request,
   *                    each separated by {@code ;}
   * @param annotations whether or not to return additional metadata for each coordinate
   *                    along the match geometry. Can be one or all of 'duration', 'distance',
   *                    or 'nodes', each separated by ,. See the response object for more
   *                    details on what it is included with annotations
   * @param language    language of returned turn-by-turn text instructions
   * @param tidy        whether or not to transparently remove clusters and re-sample traces for
   *                    improved map matching results
   * @param roundaboutExits  Whether or not to emit instructions at roundabout exits.
   * @param bannerInstructions Whether or not to return banner objects associated with the `routeSteps`.
   *                           Should be used in conjunction with `steps`.
   * @param voiceInstructions whether or not to return
   *                        marked-up text for voice guidance along the route.
   * @param waypoints  Which input coordinates should be treated as waypoints.
   * @return the MapMatchingResponse in a Call wrapper
   * @since 2.0.0
   */
  @GET("matching/v5/{user}/{profile}/{coordinates}")
  Call<MapMatchingResponse> getCall(
    @Header("User-Agent") String userAgent,
    @Path("user") String user,
    @Path("profile") String profile,
    @Path("coordinates") String coordinates,
    @Query("access_token") String accessToken,
    @Query("geometries") String geometries,
    @Query("radiuses") String radiuses,
    @Query("steps") Boolean steps,
    @Query("overview") String overview,
    @Query("timestamps") String timestamps,
    @Query("annotations") String annotations,
    @Query("language") String language,
    @Query("tidy") Boolean tidy,
    @Query("roundabout_exits") Boolean roundaboutExits,
    @Query("banner_instructions") Boolean bannerInstructions,
    @Query("voice_instructions") Boolean voiceInstructions,
    @Query("waypoints") String waypoints);
}
