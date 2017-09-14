package com.mapbox.services.api.rx.optimization.v1;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.mapbox.services.api.optimization.v1.MapboxOptimization;
import com.mapbox.services.api.optimization.v1.models.OptimizationResponse;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;

import static autovalue.shaded.com.google$.common.base.$Preconditions.checkNotNull;

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
public final class RxOptimization {

  /**
   * Create an observable which emits the dismiss events from {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult
  @NonNull
  public static Observable<Response<OptimizationResponse>> callback(@NonNull MapboxOptimization optimization) {
    return new OptimizationResponseObservable(optimization);
  }

  private RxOptimization() {
    throw new AssertionError("No instances.");
  }

//public class MapboxOptimizedTripsRx extends MapboxOptimizedTrips {
//
//  private RxOptimization serviceRx = null;
//  private Observable<OptimizedTripsResponse> observable = null;
//
//  public MapboxOptimizedTripsRx(Builder builder) {
//    super(builder);
//  }
//
//  private RxOptimization getServiceRx() {
//    // No need to recreate it
//    if (serviceRx != null) {
//      return serviceRx;
//    }
//
//    // Retrofit instance
//    Retrofit retrofit = new Retrofit.Builder()
//      .client(getOkHttpClient())
//      .baseUrl(builder.getBaseUrl())
//      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//      .addConverterFactory(GsonConverterFactory.create())
//      .build();
//
//    // Directions service
//    serviceRx = retrofit.create(RxOptimization.class);
//    return serviceRx;
//  }
//
//  public Observable<OptimizedTripsResponse> getObservable() {
//    // No need to recreate it
//    if (observable != null) {
//      return observable;
//    }
//
//    observable = getServiceRx().getObservable(
//      getHeaderUserAgent(builder.getClientAppName()),
//      builder.getUser(),
//      builder.getProfile(),
//      builder.getCoordinates(),
//      builder.getAccessToken(),
//      builder.getRoundTrip(),
//      builder.getRadiuses(),
//      builder.getBearings(),
//      builder.getSteps(),
//      builder.getOverview(),
//      builder.getGeometries(),
//      builder.getAnnotation(),
//      builder.getDestination(),
//      builder.getSource(),
//      builder.getLanguage(),
//      builder.getDistributions());
//
//    // Done
//    return observable;
//  }
//
//  public static class Builder extends MapboxOptimizedTrips.Builder<Builder> {
//    @Override
//    public MapboxOptimizedTripsRx build() throws ServicesException {
//      super.build();
//      return new MapboxOptimizedTripsRx(this);
//    }
//  }
}
