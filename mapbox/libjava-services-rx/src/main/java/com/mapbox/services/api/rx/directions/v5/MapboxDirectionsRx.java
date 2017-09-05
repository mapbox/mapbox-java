package com.mapbox.services.api.rx.directions.v5;

/**
 * The Directions API allows the calculation of routes between coordinates. The fastest route
 * is returned with geometries, and turn-by-turn instructions. The Mapbox Directions API supports
 * routing for driving cars, riding bicycles and walking.
 *
 * This class has support for Rx Observables.
 *
 * @since 2.0.0
 */
public class MapboxDirectionsRx  {
//
//  private DirectionsServiceRx serviceRx = null;
//  private Observable<DirectionsResponse> observable = null;
//
//  public MapboxDirectionsRx(Builder builder) {
//    super(builder);
//  }
//
//  private DirectionsServiceRx getServiceRx() {
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
//    serviceRx = retrofit.create(DirectionsServiceRx.class);
//    return serviceRx;
//  }
//
//  public Observable<DirectionsResponse> getObservable() {
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
//      builder.isAlternatives(),
//      builder.getGeometries(),
//      builder.getOverview(),
//      builder.getRadiuses(),
//      builder.isSteps(),
//      builder.getBearings(),
//      builder.isContinueStraight(),
//      builder.getAnnotation(),
//      builder.getLanguage());
//
//    // Done
//    return observable;
//  }
//
//  public static class Builder extends MapboxDirections.Builder<Builder> {
//    @Override
//    public MapboxDirectionsRx build() throws ServicesException {
//      super.build();
//      return new MapboxDirectionsRx(this);
//    }
//  }
}
