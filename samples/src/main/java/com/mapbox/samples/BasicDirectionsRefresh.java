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

public class BasicDirectionsRefresh {
  public static void main(String[] args) throws IOException {
    String requestId = simpleMapboxDirectionsRequest();
    simpleMapboxDirectionsRefreshRequest(requestId);
  }

  /**
   * Demonstrates how to make the most basic directions request.
   *
   * @throws IOException signals that an I/O exception of some sort has occurred
   */
  private static String simpleMapboxDirectionsRequest() throws IOException {
    MapboxDirections.Builder builder = MapboxDirections.builder();

    builder.accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN);
    builder.enableRefresh(true);
    builder.origin(Point.fromLngLat(-95.6332, 29.7890));
    builder.destination(Point.fromLngLat(-95.3591, 29.7576));
    builder.overview(OVERVIEW_FULL);
    builder.annotations(ANNOTATION_CONGESTION);

    Response<DirectionsResponse> response = builder.build().executeCall();

    String requestId = response.body().routes().get(0).routeOptions().requestUuid();

    System.out.println("Directions response: " + response + " " + requestId);

    return requestId;
  }
  private static void simpleMapboxDirectionsRefreshRequest(String requestId) {

    MapboxDirectionsRefresh refresh =
      MapboxDirectionsRefresh.builder()
        .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
        .requestId(requestId)
        .routeIndex(String.valueOf(0))
        .legIndex(String.valueOf(0))
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
