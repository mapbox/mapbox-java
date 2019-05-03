package com.mapbox.samples;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;

import com.mapbox.sample.BuildConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicGeocoding {

  public static void main(String[] args) {
    MapboxGeocoding geocoding = MapboxGeocoding.builder()
      .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
      //.query("1600")
      .query("-77.0366,38.8971")
      .languages("ru,es")
      .build();

    geocoding.enqueueCall(new Callback<GeocodingResponse>() {
      @Override
      public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
        System.out.println(">>>>>>" + response.body());
        System.out.println(">>>>>" + response.body().type());
      }

      @Override
      public void onFailure(Call<GeocodingResponse> call, Throwable t) {
        System.out.println(t);
      }
    });
  }


}
