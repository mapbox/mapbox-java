package com.mapbox.samples;

import com.mapbox.geocoding.v5.MapboxGeocoding;
import com.mapbox.geocoding.v5.models.GeocodingResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicGeocoding {

  public static void main(String[] args) {
    MapboxGeocoding geocoding = MapboxGeocoding.builder()
      .accessToken("pk.eyJ1IjoiY2FtbWFjZSIsImEiOiI5OGQxZjRmZGQ2YjU3Mzk1YjJmZTQ5ZDY2MTg1NDJiOCJ9.hIFoCKGAGOwQkKyVPvrxvQ")
      .query("1600")
      .build();

    geocoding.enqueueCall(new Callback<GeocodingResponse>() {
      @Override
      public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
        System.out.println(response.body().type());
      }

      @Override
      public void onFailure(Call<GeocodingResponse> call, Throwable t) {
        System.out.println(t);
      }
    });
  }


}
