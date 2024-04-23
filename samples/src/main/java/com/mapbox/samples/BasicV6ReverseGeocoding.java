package com.mapbox.samples;

import com.mapbox.api.geocoding.v6.MapboxV6Geocoding;
import com.mapbox.api.geocoding.v6.models.V6Feature;
import com.mapbox.api.geocoding.v6.models.V6Response;
import com.mapbox.api.geocoding.v6.V6ReverseGeocodingRequestOptions;
import com.mapbox.geojson.Point;
import com.mapbox.sample.BuildConfig;

import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicV6ReverseGeocoding {

  public static void main(String[] args) {
    final V6ReverseGeocodingRequestOptions requestOptions = V6ReverseGeocodingRequestOptions
      .builder(Point.fromLngLat(-77.03397315668123, 38.89991317162873))
      .build();

    final MapboxV6Geocoding geocoding = MapboxV6Geocoding
      .builder(BuildConfig.MAPBOX_ACCESS_TOKEN, requestOptions)
      .build();

    geocoding.enqueueCall(new Callback<V6Response>() {
      @Override
      public void onResponse(Call<V6Response> call, Response<V6Response> response) {
        final V6Response body = response.body();
        if (body == null) {
          System.out.println("Response body is null");
          return;
        }

        System.out.println("Number of results: " + body.features().size());

        final String results = body.features().stream()
            .map(V6Feature::toString)
            .collect(Collectors.joining(", \n"));

        System.out.println("Features: " + results);
      }

      @Override
      public void onFailure(Call<V6Response> call, Throwable t) {
        System.out.println(t);
      }
    });
  }
}
