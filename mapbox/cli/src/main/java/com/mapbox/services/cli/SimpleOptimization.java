package com.mapbox.services.cli;

import com.mapbox.services.api.optimization.v1.MapboxOptimization;
import com.mapbox.services.api.optimization.v1.models.OptimizationResponse;
import com.mapbox.services.commons.geojson.Point;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleOptimization {

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
        if (response.body() != null && response.body().trips() != null) {
          if (response.body().trips().size() > 0) {
            for (int i = 0; i < response.body().trips().size(); i++) {
              System.out.println(response.body().trips().get(0).legs().get(i).duration());
            }
          }
        }
      }

      @Override
      public void onFailure(Call<OptimizationResponse> call, Throwable t) {

      }
    });
  }
}

