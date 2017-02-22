package com.mapbox.services.api.rx.distance.v1;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.distance.v1.MapboxDistance;
import com.mapbox.services.api.distance.v1.models.DistanceResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Note this API is still in preview.
 * <p>
 * The Mapbox Distance API returns all travel times between many points. For example, given 3
 * locations A, B, C, the Distance API will return a matrix of travel times in seconds between each
 * location. This API allows you to build tools that efficiently check the reachability of
 * coordinates from each other, filter points by travel time, or run algorithms for solving
 * optimization problems.
 * </p>
 * <p>
 * Limits placed on this API include a maximum 100 coordinate pairs per request and a maximum 60
 * requests per minute.
 * </p>
 * This class has support for Rx Observables.
 *
 * @see <a href="https://www.mapbox.com/api-documentation/?language=Java#distance">Mapbox Distance API documentation</a>
 * @since 2.0.0
 */
public class MapboxDistanceRx extends MapboxDistance {

  private DistanceServiceRx serviceRx = null;
  private Observable<DistanceResponse> observable = null;

  public MapboxDistanceRx(Builder builder) {
    super(builder);
  }

  private DistanceServiceRx getServiceRx() {
    // No need to recreate it
    if (serviceRx != null) {
      return serviceRx;
    }

    // Retrofit instance
    Retrofit retrofit = new Retrofit.Builder()
      .client(getOkHttpClient())
      .baseUrl(builder.getBaseUrl())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(getGson()))
      .build();

    // Distance service
    serviceRx = retrofit.create(DistanceServiceRx.class);
    return serviceRx;
  }

  public Observable<DistanceResponse> getObservable() {
    // No need to recreate it
    if (observable != null) {
      return observable;
    }

    observable = getServiceRx().getObservable(
      getHeaderUserAgent(builder.getClientAppName()),
      builder.getUser(),
      builder.getProfile(),
      builder.getAccessToken(),
      builder.getCoordinates()
    );

    // Done
    return observable;
  }

  public static class Builder extends MapboxDistance.Builder<Builder> {
    @Override
    public MapboxDistanceRx build() throws ServicesException {
      super.build();
      return new MapboxDistanceRx(this);
    }
  }

}
