package com.mapbox.services.api.distance.v1;

import com.mapbox.services.commons.geojson.MultiPoint;
import com.mapbox.services.api.distance.v1.models.DistanceResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the distance service.
 *
 * @since 2.0.0
 */
public interface DistanceService {

  /**
   * Call based interface
   *
   * @param userAgent   The User.
   * @param user        The user.
   * @param profile     Directions profile id.
   * @param accessToken Mapbox access token.
   * @param coordinates converted to a {@link MultiPoint#toJson()}.
   * @return The {@link DistanceResponse} in a Call wrapper
   * @since 2.0.0
   */
  @POST("distances/v1/{user}/{profile}")
  Call<DistanceResponse> getCall(
    // NOTE: DistanceServiceRx should be updated as well
    @Header("User-Agent") String userAgent,
    @Path("user") String user,
    @Path("profile") String profile,
    @Query("access_token") String accessToken,
    @Body RequestBody coordinates);
}
