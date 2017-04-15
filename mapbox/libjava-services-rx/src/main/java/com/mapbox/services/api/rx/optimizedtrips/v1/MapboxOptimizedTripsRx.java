package com.mapbox.services.api.rx.optimizedtrips.v1;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.optimizedtrips.v1.MapboxOptimizedTrips;
import com.mapbox.services.api.optimizedtrips.v1.models.OptimizedTripsResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The Mapbox Optimized Trips API returns a duration-optimized trip between the input coordinates. This is also known
 * as solving the Traveling Salesperson Problem. A typical use case for this API is planning the route for deliveries
 * in a city. Optimized trips can be retrieved for car driving, bicycling and walking or hiking.
 * <p>
 * Under normal plans, a maximum of 12 coordinates can be passed in at once at a maximum 60 requests per minute. For
 * higher volumes, reach out through our contact page.
 * <p>
 * Note that for under 10 coordinates, the returned results will be optimal. For 10 and more coordinates, the results
 * will be optimized approximations.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Travelling_salesman_problem">Traveling Salesperson Problem</a>
 * @see <a href="https://www.mapbox.com/api-documentation/#optimized-trips">API documentation</a>
 * @since 2.1.0
 */
public class MapboxOptimizedTripsRx extends MapboxOptimizedTrips {

  private OptimizedTripsServiceRx serviceRx = null;
  private Observable<OptimizedTripsResponse> observable = null;

  public MapboxOptimizedTripsRx(Builder builder) {
    super(builder);
  }

  private OptimizedTripsServiceRx getServiceRx() {
    // No need to recreate it
    if (serviceRx != null) {
      return serviceRx;
    }

    // Retrofit instance
    Retrofit retrofit = new Retrofit.Builder()
      .client(getOkHttpClient())
      .baseUrl(builder.getBaseUrl())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create())
      .build();

    // Directions service
    serviceRx = retrofit.create(OptimizedTripsServiceRx.class);
    return serviceRx;
  }

  public Observable<OptimizedTripsResponse> getObservable() {
    // No need to recreate it
    if (observable != null) {
      return observable;
    }

    observable = getServiceRx().getObservable(
      getHeaderUserAgent(builder.getClientAppName()),
      builder.getUser(),
      builder.getProfile(),
      builder.getCoordinates(),
      builder.getAccessToken(),
      builder.getRoundTrip(),
      builder.getRadiuses(),
      builder.getBearings(),
      builder.getSteps(),
      builder.getOverview(),
      builder.getGeometries(),
      builder.getAnnotation(),
      builder.getDestination(),
      builder.getSource());

    // Done
    return observable;
  }

  public static class Builder extends MapboxOptimizedTrips.Builder<Builder> {
    @Override
    public MapboxOptimizedTripsRx build() throws ServicesException {
      super.build();
      return new MapboxOptimizedTripsRx(this);
    }
  }
}
