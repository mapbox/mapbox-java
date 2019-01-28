package com.mapbox.api.directionsrefresh.v1;

import com.mapbox.api.directionsrefresh.v1.models.DirectionsRefreshResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface DirectionsRefreshService {

  /**
   *
   * @param userAgent
   * @param routeId
   * @param annotations
   * @param accessToken
   * @return
   * @since 4.4.0
   */
  @GET("directions-refresh/v1")
  Call<DirectionsRefreshResponse> getCall(
    @Header("User-Agent") String userAgent,
    @Query("route_id") String routeId,
    @Query("annotations") String annotations,
    @Query("access_token") String accessToken
  );
}
