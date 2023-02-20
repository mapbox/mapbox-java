package com.mapbox.samples;

import com.mapbox.api.geocoding.v6.V6Response;
import com.mapbox.api.geocoding.v6.MapboxGeocodingV6;
import com.mapbox.api.geocoding.v6.V6Feature;
import com.mapbox.api.geocoding.v6.V6FeatureType;
import com.mapbox.sample.BuildConfig;

import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicGeocodingV6 {

  public static void main(String[] args) {
    final MapboxGeocodingV6 geocoding = MapboxGeocodingV6.builder()
      .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
      .query("740 15th St NW, Washington, DC 20005")
      .geocodingTypes(V6FeatureType.ADDRESS)
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
