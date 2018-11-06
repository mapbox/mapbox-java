package com.mapbox.api.routetiles.v1.versions;

import com.mapbox.api.routetiles.v1.versions.models.RouteTileVersionsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Interface that defines the Route Tile Versions Service (v1).
 *
 * @since 4.1.0
 */
public interface RouteTileVersionsService {
  /**
   *
   * @param userAgent     the user agent
   * @param accessToken   Mapbox access token
   * @return the ResponseBody containing the data stream wrapped in a Call wrapper
   * @since 4.1.0
   */
  @GET("route-tiles/v1/versions?")
  Call<RouteTileVersionsResponse> getCall(
    @Header("User-Agent") String userAgent,
    @Query("access_token") String accessToken
  );
}
