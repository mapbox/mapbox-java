package com.mapbox.services.api.rx.matrix.v1;

/**
 * The Directions Matrix API allows the calculation of routes between coordinates.
 * <p>
 * This class has support for Rx Observables.
 *
 * @since 2.1.0
 */
public class MapboxMatrixRx {//extends MapboxDirectionsMatrix {
//
//  private DirectionsMatrixServiceRx serviceRx = null;
//  private Observable<DirectionsMatrixResponse> observable = null;
//
//  public MapboxDirectionsMatrixRx(Builder builder) {
//    super(builder);
//  }
//
//  private DirectionsMatrixServiceRx getServiceRx() {
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
//    serviceRx = retrofit.create(DirectionsMatrixServiceRx.class);
//    return serviceRx;
//  }
//
//  public Observable<DirectionsMatrixResponse> getObservable() {
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
//      builder.getDestinations(),
//      builder.getSources());
//
//    // Done
//    return observable;
//  }
//
//  public static class Builder extends MapboxDirectionsMatrix.Builder<Builder> {
//    @Override
//    public MapboxDirectionsMatrixRx build() throws ServicesException {
//      super.build();
//      return new MapboxDirectionsMatrixRx(this);
//    }
//  }
}
