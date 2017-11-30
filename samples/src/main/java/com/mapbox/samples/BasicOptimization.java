package com.mapbox.samples;


import com.mapbox.geojson.Point;
import com.mapbox.api.optimization.v1.MapboxOptimization;
import com.mapbox.api.optimization.v1.models.OptimizationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicOptimization {

  public static void main(String[] args) {

    MapboxOptimization optimization = MapboxOptimization.builder()
      .accessToken("pk.eyJ1IjoiY2FtbWFjZSIsImEiOiI5OGQxZjRmZGQ2YjU3Mzk1YjJmZTQ5ZDY2MTg1NDJiOCJ9.hIFoCKGAGOwQkKyVPvrxvQ")
      .coordinate(Point.fromLngLat(-122.42, 37.78))
      .coordinate(Point.fromLngLat(-122.45, 37.91))
      .coordinate(Point.fromLngLat(-122.48, 37.73))
      .build();

    optimization.enqueueCall(new Callback<OptimizationResponse>() {
      @Override
      public void onResponse(Call<OptimizationResponse> call, Response<OptimizationResponse> response) {
        System.out.println(response.body().code());
      }

      @Override
      public void onFailure(Call<OptimizationResponse> call, Throwable t) {
        System.out.println(t);
      }
    });
  }
}
