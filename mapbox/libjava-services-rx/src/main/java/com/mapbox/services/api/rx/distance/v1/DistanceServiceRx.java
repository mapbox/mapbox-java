package com.mapbox.services.api.rx.distance.v1;

import com.mapbox.services.api.distance.v1.models.DistanceResponse;
import com.mapbox.services.commons.geojson.MultiPoint;

import io.reactivex.Observable;
import okhttp3.RequestBody;
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
public interface DistanceServiceRx {
  /**
   * Observable-based interface
   *
   * @param userAgent   The User.
   * @param user        The user.
   * @param profile     Directions profile id.
   * @param accessToken Mapbox access token.
   * @param coordinates converted to a {@link MultiPoint#toJson()}.
   * @return The {@link DistanceResponse} in a Observable wrapper
   * @since 2.0.0
   */
  @POST("distances/v1/{user}/{profile}")
  Observable<DistanceResponse> getObservable(
    @Header("User-Agent") String userAgent,
    @Path("user") String user,
    @Path("profile") String profile,
    @Query("access_token") String accessToken,
    @Body RequestBody coordinates);
}
