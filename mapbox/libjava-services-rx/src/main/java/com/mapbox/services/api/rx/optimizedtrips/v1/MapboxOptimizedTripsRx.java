package com.mapbox.services.api.rx.optimizedtrips.v1;

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
public class MapboxOptimizedTripsRx  {
//
//  private OptimizedTripsServiceRx serviceRx = null;
//  private Observable<OptimizationResponse> observable = null;
//
//  public MapboxOptimizedTripsRx(Builder builder) {
//    super(builder);
//  }
//
//  private OptimizedTripsServiceRx getServiceRx() {
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
//    serviceRx = retrofit.create(OptimizedTripsServiceRx.class);
//    return serviceRx;
//  }
//
//  public Observable<OptimizationResponse> getObservable() {
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
//  public static class Builder extends MapboxOptimization.Builder<Builder> {
//    @Override
//    public MapboxOptimizedTripsRx build() throws ServicesException {
//      super.build();
//      return new MapboxOptimizedTripsRx(this);
//    }
//  }
}
