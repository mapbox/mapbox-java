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
   * @param requestId
   * @param routeIndex
   * @param legIndex
   * @param accessToken
   * @return
   * @since 4.4.0
   */
  @GET("directions-refresh/v1/{request_id}/{route_index}/{leg_index}")
  Call<DirectionsRefreshResponse> getCall(
    @Header("User-Agent") String userAgent,
    @Path("request_id") String requestId,
    @Path("route_index") String routeIndex,
    @Path("leg_index") String legIndex,
    @Query("access_token") String accessToken
  );
}
