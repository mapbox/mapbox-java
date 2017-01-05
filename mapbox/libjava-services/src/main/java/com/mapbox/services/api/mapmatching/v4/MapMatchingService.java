package com.mapbox.services.api.mapmatching.v4;

import com.mapbox.services.api.mapmatching.v4.models.MapMatchingResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the map matching service.
 *
 * @since 1.2.0
 */
public interface MapMatchingService {

  /**
   * Call based interface
   *
   * @param userAgent    user
   * @param profile      map matching profile id
   * @param accessToken  Mapbox access token
   * @param geometry     format for the returned geometry (optional)
   * @param gpsPrecision assumed precision in meters of the used tracking device
   * @param trace        The trace wanting to be matched
   * @return The MapMatchingResponse in a Call wrapper
   * @since 1.2.0
   */
  @POST("matching/v4/{profile}.json")
  Call<MapMatchingResponse> getCall(
    @Header("User-Agent") String userAgent,
    @Path("profile") String profile,
    @Query("access_token") String accessToken,
    @Query("geometry") String geometry,
    @Query("gps_precision") Integer gpsPrecision,
    @Body RequestBody trace);
}
