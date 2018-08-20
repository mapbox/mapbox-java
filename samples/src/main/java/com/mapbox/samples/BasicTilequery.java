package com.mapbox.samples;

import com.mapbox.api.tilequery.MapboxTilequery;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.sample.BuildConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicTilequery {

  public static void main(String[] args) {
    MapboxTilequery tilequery = MapboxTilequery.builder()
      .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
      .mapIds("mapbox.mapbox-streets-v7")
      .query(Point.fromLngLat(-122.42901, 37.806332))
      .radius(500)
      .limit(2)
      .geometry("point")
      .dedupe(true)
      .layers("poi_label,building")
      .build();

    tilequery.enqueueCall(new Callback<FeatureCollection>() {
      @Override
      public void onResponse(Call<FeatureCollection> call, Response<FeatureCollection> response) {
        System.out.println("Total results: " + response.body().features().size());
        for (Feature feature : response.body().features()) {
          if (feature.properties().has("name")) {
            System.out.println("" + feature.properties().get("name").getAsString());
          } else {
            System.out.println("Unnamed feature.");
          }
        }
      }

      @Override
      public void onFailure(Call<FeatureCollection> call, Throwable t) {
        System.out.println("Request failed: " + t.getMessage());
        t.printStackTrace();
      }
    });
  }
}
