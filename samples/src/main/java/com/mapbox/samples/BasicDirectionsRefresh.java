package com.mapbox.samples;

import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directionsrefresh.v1.MapboxDirectionsRefresh;
import com.mapbox.api.directionsrefresh.v1.models.DirectionsRefreshResponse;
import com.mapbox.geojson.Point;
import com.mapbox.sample.BuildConfig;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.api.directions.v5.DirectionsCriteria.ANNOTATION_CONGESTION;
import static com.mapbox.api.directions.v5.DirectionsCriteria.OVERVIEW_FULL;
import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_DRIVING_TRAFFIC;

public class BasicDirectionsRefresh {
  public static void main(String[] args) throws IOException {
    String requestId = simpleMapboxDirectionsRequest();
    simpleMapboxDirectionsRefreshRequest(requestId);
  }

  private static String simpleMapboxDirectionsRequest() throws IOException {
    MapboxDirections directions = MapboxDirections.builder()
      .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
      .enableRefresh(true)
      .origin(Point.fromLngLat(-95.6332, 29.7890))
      .destination(Point.fromLngLat(-95.3591, 29.7576))
      .overview(OVERVIEW_FULL)
      .profile(PROFILE_DRIVING_TRAFFIC)
      .annotations(ANNOTATION_CONGESTION).build();

    Response<DirectionsResponse> response = directions.executeCall();
    System.out.println("Directions response: " + response);
    String requestId = response.body().routes().get(0).routeOptions().requestUuid();

    return requestId;
  }

  private static void simpleMapboxDirectionsRefreshRequest(String requestId) {
    MapboxDirectionsRefresh refresh =
      MapboxDirectionsRefresh.builder()
        .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
        .requestId(requestId)
        .routeIndex(0)
        .legIndex(0)
        .build();

    refresh.enqueueCall(new Callback<DirectionsRefreshResponse>() {
      @Override
      public void onResponse(Call<DirectionsRefreshResponse> call, Response<DirectionsRefreshResponse> response) {
        System.out.println("Refresh response: " + response);
      }

      @Override
      public void onFailure(Call<DirectionsRefreshResponse> call, Throwable throwable) {
        System.out.println("" + call.request().url());
        System.out.printf("Failure: " + throwable);
      }
    });
  }
}
