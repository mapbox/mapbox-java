package com.mapbox.services.api.optimizedtrips.v1;

import com.mapbox.services.api.optimizedtrips.v1.models.OptimizedTripsResponse;

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
   * @param userAgent   The user.
   * @param user        The user.
   * @param profile     The profile directions should use.
   * @param coordinates The coordinates used to calculate the trip.
   * @param accessToken Mapbox access token.
   * @param roundTrip   Returned route is a roundtrip (route returns to first location). Allowed values are: true
   *                    (default) or false.
   * @param radiuses    Maximum distance in meters that each coordinate is allowed to move when snapped to a nearby
   *                    road segment. There must be as many radiuses as there are coordinates in the request. Values
   *                    can be any number greater than 0 or they can be the string unlimited. If no routable road is
   *                    found within the radius, a NoSegment error is returned.
   * @param bearings    Used to filter the road segment the waypoint will be placed on by direction and dictates
   *                    the angle of approach
   * @param steps       Define if you'd like the route steps.
   * @param geometries  Route geometry.
   * @param annotations An annotations object that contains additional details about each line segment along the
   *                    route geometry. Each entry in an annotations field corresponds to a coordinate along the
   *                    route geometry.
   * @param destination Returned route ends at any or last coordinate. Allowed values are: any (default) or last.
   * @param source     Returned route starts at any or first coordinate. Allowed values are: any (default) or first.
   * @since 2.1.0
   */
  @GET("optimized-trips/v1/{user}/{profile}/{coordinates}")
  Call<OptimizedTripsResponse> getCall(
    // NOTE: OptimizedTripsServiceRx should be updated as well
    @Header("User-Agent") String userAgent,
    @Path("user") String user,
    @Path("profile") String profile,
    @Path("coordinates") String coordinates,
    @Query("access_token") String accessToken,
    @Query("roundtrip") Boolean roundTrip,
    @Query("radiuses") double[] radiuses,
    @Query("bearings") double[][] bearings,
    @Query("steps") Boolean steps,
    @Query("overview") String overview,
    @Query("geometries") String geometries,
    @Query("annotations") String[] annotations,
    @Query("destination") String destination,
    @Query("source") String source
  );
}


