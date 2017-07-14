package com.mapbox.services.api.rx.mapmatching.v5;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.mapmatching.v5.MapboxMapMatching;
import com.mapbox.services.api.mapmatching.v5.models.MapMatchingResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The Mapbox map matching interface (v4)
 * <p>
 * The Mapbox Map Matching API snaps fuzzy, inaccurate traces from a GPS unit or a phone to the
 * OpenStreetMap road and path network using the Directions API. This produces clean paths that can
 * be displayed on a map or used for other analysis.
 *
 * This class has support for Rx Observables.
 *
 * @see <a href="https://www.mapbox.com/api-documentation/#map-matching">Map matching API documentation</a>
 * @since 2.0.0
 */
public class MapboxMapMatchingRx extends MapboxMapMatching {

  private MapMatchingServiceRx serviceRx = null;
  private Observable<MapMatchingResponse> observable = null;

  public MapboxMapMatchingRx(Builder builder) {
    super(builder);
  }

  private MapMatchingServiceRx getServiceRx() {
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

    // MapMatching service
    serviceRx = retrofit.create(MapMatchingServiceRx.class);
    return serviceRx;
  }

  public Observable<MapMatchingResponse> getObservable() {
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
      builder.getGeometries(),
      builder.getRadiuses(),
      builder.getSteps(),
      builder.getOverview(),
      builder.getTimestamps(),
      builder.getAnnotations(),
      builder.getLanguage()
    );

    // Done
    return observable;
  }

  public static class Builder extends MapboxMapMatching.Builder<Builder> {
    @Override
    public MapboxMapMatchingRx build() throws ServicesException {
      super.build();
      return new MapboxMapMatchingRx(this);
    }
  }
}
