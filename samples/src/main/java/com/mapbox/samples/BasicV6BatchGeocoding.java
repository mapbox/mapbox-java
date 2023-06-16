package com.mapbox.samples;

import com.mapbox.api.geocoding.v6.MapboxV6BatchGeocoding;
import com.mapbox.api.geocoding.v6.V6RequestOptions;
import com.mapbox.api.geocoding.v6.V6StructuredInputQuery;
import com.mapbox.api.geocoding.v6.V6ForwardGeocodingRequestOptions;
import com.mapbox.api.geocoding.v6.models.V6BatchResponse;
import com.mapbox.api.geocoding.v6.models.V6Feature;
import com.mapbox.api.geocoding.v6.models.V6FeatureType;
import com.mapbox.api.geocoding.v6.models.V6Response;
import com.mapbox.api.geocoding.v6.V6ReverseGeocodingRequestOptions;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Point;
import com.mapbox.sample.BuildConfig;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicV6BatchGeocoding {

  private static List<V6RequestOptions> requestOptions() {
    final V6ForwardGeocodingRequestOptions forwardOptions = V6ForwardGeocodingRequestOptions
      .builder("1600 Pennsylvania Avenue NW, Washington, DC 20500, United States")
      .types(V6FeatureType.ADDRESS)
      .limit(1)
      .boundingBox(BoundingBox.fromLngLats(-80, 35, -70, 40))
      .build();

    final V6StructuredInputQuery structuredInputQuery = V6StructuredInputQuery.builder()
      .addressNumber("1600")
      .street("Pennsylvania Avenue NW")
      .postcode("20500")
      .place("Washington, DC")
      .build();

    final V6ForwardGeocodingRequestOptions structuredInputOptions = V6ForwardGeocodingRequestOptions
      .builder(structuredInputQuery)
      .country("us")
      .build();

    final V6ReverseGeocodingRequestOptions reverseOptions = V6ReverseGeocodingRequestOptions
      .builder(Point.fromLngLat(-73.986136, 40.748895))
      .types(V6FeatureType.ADDRESS)
      .build();

    return Arrays.asList(forwardOptions, structuredInputOptions, reverseOptions);
  }

  public static void main(String[] args) {
    final MapboxV6BatchGeocoding geocoding = MapboxV6BatchGeocoding
      .builder(
        BuildConfig.MAPBOX_ACCESS_TOKEN,
        requestOptions()
      )
      .build();

    geocoding.enqueueCall(new Callback<V6BatchResponse>() {
      @Override
      public void onResponse(Call<V6BatchResponse> call, Response<V6BatchResponse> response) {
        System.out.println("Response code: " + response.code());
        System.out.println("Response message: " + response.message());

        final V6BatchResponse body = response.body();
        if (body == null) {
          System.out.println("Response body is null");
          return;
        }

        System.out.println("Number of responses: " + body.responses().size());

        for (V6Response v6Response : body.responses()) {
          System.out.println("Number of results: " + v6Response.features().size());

          final String results = v6Response.features().stream()
            .map(V6Feature::toString)
            .collect(Collectors.joining(", \n"));

          System.out.println("Features: " + results);
        }
      }

      @Override
      public void onFailure(Call<V6BatchResponse> call, Throwable t) {
        System.out.println(t);
      }
    });
  }
}
