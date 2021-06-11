package com.mapbox.samples;

import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.api.directionsrefresh.v1.MapboxDirectionsRefresh;
import com.mapbox.api.directionsrefresh.v1.models.DirectionsRefreshResponse;
import com.mapbox.geojson.Point;
import com.mapbox.sample.BuildConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.api.directions.v5.DirectionsCriteria.ANNOTATION_CONGESTION;
import static com.mapbox.api.directions.v5.DirectionsCriteria.ANNOTATION_MAXSPEED;
import static com.mapbox.api.directions.v5.DirectionsCriteria.OVERVIEW_FULL;
import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_DRIVING_TRAFFIC;

public class BasicDirectionsRefresh {
  public static void main(String[] args) throws IOException {
    String requestIdFirst = simpleMapboxDirectionsRequest(mapboxDirections(false));
    simpleMapboxDirectionsRefreshRequest(requestIdFirst, 0);

    String requestIdSecond = simpleMapboxDirectionsRequest(mapboxDirections(true));
    simpleMapboxDirectionsRefreshRequest(requestIdSecond, 1);
  }

  private static String simpleMapboxDirectionsRequest(MapboxDirections directions) throws IOException {
    Response<DirectionsResponse> response = directions.executeCall();
    System.out.println("Directions response: " + response);
    String requestId = response.body().routes().get(0).routeOptions().requestUuid();

    return requestId;
  }

  private static MapboxDirections mapboxDirections(Boolean addWaypoint) {
    MapboxDirections.Builder builder = MapboxDirections.builder();

    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-95.6332, 29.7890));
    if (addWaypoint) {
      coordinates.add(Point.fromLngLat(-95.5591, 29.7376));
    }
    coordinates.add(Point.fromLngLat(-95.3591, 29.7576));
    RouteOptions routeOptions = RouteOptions.builder()
      .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
      .coordinates(coordinates)
      .enableRefresh(true)
      .overview(OVERVIEW_FULL)
      .profile(PROFILE_DRIVING_TRAFFIC)
      .annotations(Arrays.asList(ANNOTATION_CONGESTION, ANNOTATION_MAXSPEED))
      .build();
    builder.routeOptions(routeOptions);

    return builder.build();
  }

  private static void simpleMapboxDirectionsRefreshRequest(String requestId, Integer legIndex) {
    MapboxDirectionsRefresh refresh =
      MapboxDirectionsRefresh.builder()
        .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
        .requestId(requestId)
        .routeIndex(0)
        .legIndex(legIndex)
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
