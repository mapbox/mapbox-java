package com.mapbox.samples;

import com.mapbox.api.geocoding.v6.V6StructuredInputQuery;
import com.mapbox.api.geocoding.v6.MapboxV6Geocoding;
import com.mapbox.api.geocoding.v6.V6ForwardGeocodingRequestOptions;
import com.mapbox.api.geocoding.v6.models.V6Feature;
import com.mapbox.api.geocoding.v6.models.V6FeatureType;
import com.mapbox.api.geocoding.v6.models.V6Response;
import com.mapbox.sample.BuildConfig;

import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicV6StructuredInputForwardGeocoding {

  public static void main(String[] args) {
    final V6StructuredInputQuery query = V6StructuredInputQuery.builder()
      .addressNumber("740")
      .street("15th St")
      .place("Washington")
      .postcode("20005")
      .country("United States")
      .build();

    final V6ForwardGeocodingRequestOptions requestOptions = V6ForwardGeocodingRequestOptions
      .builder(query)
      .types(V6FeatureType.ADDRESS)
      .autocomplete(false)
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
