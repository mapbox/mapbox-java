package com.mapbox.samples;


import com.mapbox.directions.v5.MapboxDirections;
import com.mapbox.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicDirections {

  public static void main(String[] args) {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken("pk.eyJ1IjoiY2FtbWFjZSIsImEiOiI5OGQxZjRmZGQ2YjU3Mzk1YjJmZTQ5ZDY2MTg1NDJiOCJ9.hIFoCKGAGOwQkKyVPvrxvQ")
      .origin(Point.fromLngLat(-71.0555, 42.3612))
      .destination(Point.fromLngLat(-71.1014, 42.3411))
      .build();

    directions.enqueueCall(new Callback<DirectionsResponse>() {
      @Override
      public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        System.out.println(response.body().routes().get(0).distance());
      }

      @Override
      public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
        System.out.println(throwable);
      }
    });
  }
}
