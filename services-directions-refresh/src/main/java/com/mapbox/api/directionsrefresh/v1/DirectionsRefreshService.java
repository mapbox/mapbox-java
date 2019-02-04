package com.mapbox.api.directionsrefresh.v1;

import com.mapbox.api.directionsrefresh.v1.models.DirectionsRefreshResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DirectionsRefreshService {

  /**
   *
   * @param userAgent
   * @param routeId
   * @param routeIndex
   * @param accessToken
   * @return
   * @since 4.4.0
   */
  @GET("directions-refresh/v1/{route_id}/{route_index}")
  Call<DirectionsRefreshResponse> getCall(
    @Header("User-Agent") String userAgent,
    @Path("route_id") String routeId,
    @Path("route_index") String routeIndex,
    @Query("access_token") String accessToken
  );
}
