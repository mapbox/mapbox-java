package com.mapbox.samples;

import com.mapbox.api.isochrone.IsochroneCriteria;
import com.mapbox.api.isochrone.MapboxIsochrone;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.sample.BuildConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicIsochrone {

  public static void main(String[] args) {
    getStandardIsochroneCallBuilder(true).enqueueCall(new Callback<FeatureCollection>() {
      @Override
      public void onResponse(Call<FeatureCollection> call, Response<FeatureCollection> response) {
        System.out.println("Total results: " + response.body().features().size());
        for (Feature feature : response.body().features()) {
          if (feature.properties().has("color")) {
            System.out.println("" + feature.properties().get("color").getAsString());
          } else {
            System.out.println("Unnamed feature.");
          }
        }

        makeNoPolygonCall();
      }

      @Override
      public void onFailure(Call<FeatureCollection> call, Throwable t) {
        System.out.println("Request failed: " + t.getMessage());
        t.printStackTrace();
      }
    });
  }

  private static void makeNoPolygonCall() {
    getStandardIsochroneCallBuilder(false).enqueueCall(new Callback<FeatureCollection>() {
      @Override
      public void onResponse(Call<FeatureCollection> call, Response<FeatureCollection> response) {
        System.out.println("Total results for no polygon isochrone call: " + response.body().features().size());
        for (Feature feature : response.body().features()) {
          if (feature.properties().has("color")) {
            System.out.println("" + feature.properties().get("color").getAsString());
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

  private static MapboxIsochrone getStandardIsochroneCallBuilder(boolean includePolygons) {
    return MapboxIsochrone.builder()
        .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
        .profile(IsochroneCriteria.PROFILE_WALKING)
        .addContoursMinutes(14, 35, 53)
        .polygons(includePolygons)
        .generalize(2f)
        .denoise(.4f)
        .addContoursColors("80f442", "403bd3", "bc404c")
        .coordinates(Point.fromLngLat(-122.42901, 37.806332))
        .build();
  }
}
