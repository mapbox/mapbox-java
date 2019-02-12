package com.mapbox.api.directionsrefresh.v5;

import com.mapbox.api.directionsrefresh.v5.models.DirectionsRefreshResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the directions refresh service. This corresponds to v5 of the
 * directions API, specifically driving directions.
 *
 * @since 4.4.0
 */
public interface DirectionsRefreshService {

  /**
   * Constructs the html call using the information passed in through the
   * {@link MapboxDirectionsRefresh.Builder}.
   *
   * @param userAgent   the user agent
   * @param requestId   a uuid specifying the request containing the route being refreshed
   * @param routeIndex  the index of the specified route
   * @param legIndex    the index of the leg to start the refresh response (inclusive)
   * @param accessToken Mapbox access token
   * @return the {@link DirectionsRefreshResponse} in a Call wrapper
   * @since 4.4.0
   */
  @GET("directions-refresh/v5/mapbox/driving-traffic/{request_id}/{route_index}/{leg_index}")
  Call<DirectionsRefreshResponse> getCall(
    @Header("User-Agent") String userAgent,
    @Path("request_id") String requestId,
    @Path("route_index") String routeIndex,
    @Path("leg_index") String legIndex,
    @Query("access_token") String accessToken
  );
}
