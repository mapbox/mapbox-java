package com.mapbox.optimization.v1;

import com.mapbox.optimization.v1.models.OptimizationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the Optimization service (v1).
 *
 * @since 2.1.0
 */
public interface OptimizationService {

  /**
   * @param userAgent     the application information
   * @param user          the user which the OSRM engine should run on, typically Mapbox
   * @param profile       the profile optimization should use
   * @param coordinates   the coordinates used to calculate the trip
   * @param accessToken   valid mapbox access token
   * @param roundTrip     returned route is a round trip (route returns to first location). Allowed
   *                      values are: true (default) or false
   * @param radiuses      maximum distance in meters that each coordinate is allowed to move when
   *                      snapped to a nearby road segment. There must be as many radiuses as there
   *                      are coordinates in the request. Values can be any number greater than 0 or
   *                      they can be the string unlimited. If no routable road is found within the
   *                      radius, a NoSegment error is returned
   * @param bearings      used to filter the road segment the waypoint will be placed on by
   *                      direction and dictates the angle of approach
   * @param steps         define if you'd like the route steps inside the response
   * @param overview      route geometry can be simplified or full
   * @param geometries    route geometry
   * @param annotations   an annotations object that contains additional details about each line
   *                      segment along the route geometry. Each entry in an annotations field
   *                      corresponds to a coordinate along the route geometry
   * @param destination   returned route ends at any or last coordinate. Allowed values are: any
   *                      (default) or last
   * @param source        returned route starts at any or first coordinate. Allowed values are: any
   *                      (default) or first
   * @param language      language of returned turn-by-turn text instructions
   * @param distributions specify pick-up and drop-off locations
   * @return The {@link OptimizationResponse} in a Call wrapper
   * @since 2.1.0
   */
  @GET("optimized-trips/v1/{user}/{profile}/{coordinates}")
  Call<OptimizationResponse> getCall(
    @Header("User-Agent") String userAgent,
    @Path("user") String user,
    @Path("profile") String profile,
    @Path("coordinates") String coordinates,
    @Query("access_token") String accessToken,
    @Query("roundtrip") Boolean roundTrip,
    @Query("radiuses") String radiuses,
    @Query("bearings") String bearings,
    @Query("steps") Boolean steps,
    @Query("overview") String overview,
    @Query("geometries") String geometries,
    @Query("annotations") String annotations,
    @Query("destination") String destination,
    @Query("source") String source,
    @Query("language") String language,
    @Query("distributions") String distributions
  );
}
