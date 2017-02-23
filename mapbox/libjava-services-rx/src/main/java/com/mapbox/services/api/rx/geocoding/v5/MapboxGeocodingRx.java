package com.mapbox.services.api.rx.geocoding.v5;

import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The Mapbox geocoding client (v5).
 *
 * This class has support for Rx Observables.
 *
 * @since 2.0.0
 */
public class MapboxGeocodingRx extends MapboxGeocoding {

  private GeocodingServiceRx serviceRx = null;
  private Observable<GeocodingResponse> observable = null;
  private Observable<List<GeocodingResponse>> batchObservable = null;

  public MapboxGeocodingRx(Builder builder) {
    super(builder);
  }

  private GeocodingServiceRx getServiceRx() {
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

    // Geocoding service
    serviceRx = retrofit.create(GeocodingServiceRx.class);
    return serviceRx;
  }

  public Observable<GeocodingResponse> getObservable() {
    // No need to recreate it
    if (observable != null) {
      return observable;
    }

    if (builder.getQuery().contains(";")) {
      throw new IllegalArgumentException("Use getBatchObservable() for batch calls.");
    }

    observable = getServiceRx().getObservable(
      getHeaderUserAgent(builder.getClientAppName()),
      builder.getMode(),
      builder.getQuery(),
      builder.getAccessToken(),
      builder.getCountry(),
      builder.getProximity(),
      builder.getGeocodingTypes(),
      builder.getAutocomplete(),
      builder.getBbox(),
      builder.getLimit(),
      builder.getLanguage());

    // Done
    return observable;
  }

  public Observable<List<GeocodingResponse>> getBatchObservable() {
    // No need to recreate it
    if (batchObservable != null) {
      return batchObservable;
    }

    if (!builder.getQuery().contains(";")) {
      throw new IllegalArgumentException("Use getObservable() for non-batch calls.");
    }

    batchObservable = getServiceRx().getBatchObservable(
      getHeaderUserAgent(builder.getClientAppName()),
      builder.getMode(),
      builder.getQuery(),
      builder.getAccessToken(),
      builder.getCountry(),
      builder.getProximity(),
      builder.getGeocodingTypes(),
      builder.getAutocomplete(),
      builder.getBbox(),
      builder.getLimit(),
      builder.getLanguage());

    // Done
    return batchObservable;
  }

  public static class Builder extends MapboxGeocoding.Builder<Builder> {
    @Override
    public MapboxGeocodingRx build() throws ServicesException {
      super.build();
      return new MapboxGeocodingRx(this);
    }
  }
}
