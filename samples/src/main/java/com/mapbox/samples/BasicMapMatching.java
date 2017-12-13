package com.mapbox.samples;

import com.mapbox.api.matching.v5.MapboxMapMatching;
import com.mapbox.api.matching.v5.models.MapMatchingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.sample.BuildConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicMapMatching {
  public static void main(String[] args) {

    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
      .coordinate(Point.fromLngLat(-117.1728265285492,32.71204416018209))
      .coordinate(Point.fromLngLat(-117.17288821935652,32.712258556224))
      .coordinate(Point.fromLngLat(-117.17293113470076,32.712443613445814))
      .coordinate(Point.fromLngLat(-117.17292040586472,32.71256999376694))
      .coordinate(Point.fromLngLat(-117.17298477888109,32.712603845608285))
      .coordinate(Point.fromLngLat(-117.17314302921294,32.71259933203019))
      .coordinate(Point.fromLngLat(-117.17334151268004,32.71254065549407))
      .build();

    mapMatching.enqueueCall(new Callback<MapMatchingResponse>() {
      @Override
      public void onResponse(Call<MapMatchingResponse> call,
                             Response<MapMatchingResponse> response) {
        System.out.println(response.body().toString());
      }

      @Override
      public void onFailure(Call<MapMatchingResponse> call, Throwable throwable) {
        System.out.println(throwable);
      }
    });
  }
}
