package com.mapbox.api.routetiles.v1;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the Route Tiles Service (v1).
 *
 * @since 4.1.0
 */
public interface RouteTilesService {

  /**
   * Constructs the html call using the informmation passed in through the
   * {@link MapboxRouteTiles.Builder}.
   *
   * @param userAgent     the user agent
   * @param coordinates   a string value of the min and max longitude and latitude
   * @param version       version which was previously fetched through
   *                      {@link com.mapbox.api.routetiles.v1.versions.MapboxRouteTileVersions}
   * @param accessToken   Mapbox access token
   * @return the ResponseBody containing the data stream wrapped in a Call wrapper
   * @since 4.1.0
   */
  @GET("route-tiles/v1/{coordinates}")
  Call<ResponseBody> getCall(
    @Header("User-Agent") String userAgent,
    @Path("coordinates") String coordinates,
    @Query("version") String version,
    @Query("access_token") String accessToken
  );
}
