package com.mapbox.services.api.rx.mapmatching.v5;

import com.mapbox.services.api.mapmatching.v5.models.MapMatchingResponse;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Interface that defines the map matching service.
 *
 * @since 2.0.0
 */
public interface MapMatchingServiceRx {
  /**
   * Observable-based interface
   *
   * @param userAgent   User agent
   * @param user        User
   * @param profile     Directions profile ID; either mapbox/driving, mapbox/walking,
   *                    or mapbox/cycling
   * @param coordinates Inaccurate traces from a GPS unit or a phone
   * @param accessToken Mapbox access token
   * @param geometries  Format of the returned geometry. Allowed values are: geojson
   *                    (as LineString), polyline with precision 5, polyline6. The default
   *                    value is polyline .
   * @param radiuses    A list of integers in meters indicating the assumed precision of
   *                    the used tracking device. There must be as many radiuses as there
   *                    are coordinates in the request, each separated by ;. Values can be
   *                    a number between 0 and 30. Use higher numbers (20-30) for noisy
   *                    traces and lower numbers (1-10) for clean traces. The default value
   *                    is 5.
   * @param steps       Whether to return steps and turn-by-turn instructions. Can be true
   *                    or false. The default is false.
   * @param overview    Type of returned overview geometry. Can be full (the most detailed
   *                    geometry available), simplified (a simplified version of the full
   *                    geometry), or false (no overview geometry). The default is simplified.
   * @param timestamps  Timestamps corresponding to each coordinate provided in the request;
   *                    must be numbers in Unix time (seconds since the Unix epoch). There
   *                    must be as many timestamps as there are coordinates in the request,
   *                    each separated by ;.
   * @param annotations Whether or not to return additional metadata for each coordinate
   *                    along the match geometry. Can be one or all of 'duration', 'distance',
   *                    or 'nodes', each separated by ,. See the response object for more
   *                    details on what it is included with annotations.
   * @return The MapMatchingResponse in an Observable wrapper
   * @since 2.0.0
   */
  @GET("matching/v5/{user}/{profile}/{coordinates}")
  Observable<MapMatchingResponse> getObservable(
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
    @Query("annotations") String annotations);
}
