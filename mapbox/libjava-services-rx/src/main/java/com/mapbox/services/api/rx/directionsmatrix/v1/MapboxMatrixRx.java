package com.mapbox.services.api.rx.directionsmatrix.v1;

/**
 * The Directions Matrix API allows the calculation of routes between coordinates.
 * <p>
 * This class has support for Rx Observables.
 *
 * @since 2.1.0
 */
public class MapboxMatrixRx {
//public class MapboxMatrixRx extends MapboxMatrix {
//
//  private DirectionsMatrixServiceRx serviceRx = null;
//  private Observable<MatrixResponse> observable = null;
//
//  public MapboxMatrixRx(Builder builder) {
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
//  public Observable<MatrixResponse> getObservable() {
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
//  public static class Builder extends MapboxMatrix.Builder<Builder> {
//    @Override
//    public MapboxMatrixRx build() throws ServicesException {
//      super.build();
//      return new MapboxMatrixRx(this);
//    }
//  }
}
