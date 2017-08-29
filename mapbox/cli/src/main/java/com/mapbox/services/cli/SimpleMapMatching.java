package com.mapbox.services.cli;

import com.mapbox.services.api.mapmatching.v5.MapboxMapMatching;
import com.mapbox.services.api.mapmatching.v5.models.MapMatchingResponse;
import com.mapbox.services.commons.geojson.Point;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleMapMatching {

  public static void main(String[] args) {

    MapboxMapMatching matching = MapboxMapMatching.builder()
      .accessToken("pk.eyJ1IjoiY2FtbWFjZSIsImEiOiI5OGQxZjRmZGQ2YjU3Mzk1YjJmZTQ5ZDY2MTg1NDJiOCJ9.hIFoCKGAGOwQkKyVPvrxvQ")
      .coordinate(new Point(-117.1728265285492,32.71204416018209))
      .coordinate(new Point(-117.17288821935652,32.712258556224))
      .coordinate(new Point(-117.17293113470076,32.712443613445814))
      .coordinate(new Point(-117.17292040586472,32.71256999376694))
      .coordinate(new Point(-117.17298477888109,32.712603845608285))
      .coordinate(new Point(-117.17314302921294,32.71259933203019))
      .coordinate(new Point(-117.17334151268004,32.71254065549407))
      .build();

    System.out.println(matching.cloneCall().request().url());

    matching.enqueueCall(new Callback<MapMatchingResponse>() {
      @Override
      public void onResponse(Call<MapMatchingResponse> call, Response<MapMatchingResponse> response) {
        if (response.body() != null && response.body().matchings() != null) {
          if (response.body().matchings().size() > 0) {
            for (int i = 0; i < response.body().tracepoints().size(); i++) {
              System.out.println(response.body().tracepoints().get(i).location().toString());
            }
          }
        }
      }

      @Override
      public void onFailure(Call<MapMatchingResponse> call, Throwable throwable) {
        System.err.println("Error: " + throwable);
      }
    });
  }
}
