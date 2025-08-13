package com.mapbox.samples;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.Notification;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.sample.BuildConfig;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BasicRouteNotification {

  public static void main(String[] args) throws IOException {
    simpleMapboxDirectionsRequest();
  }

  private static void simpleMapboxDirectionsRequest() throws IOException {
    MapboxDirections.Builder builder = MapboxDirections.builder();

    // 1. Pass in all the required information to get a simple directions route.
    RouteOptions routeOptions = RouteOptions.builder()
      .user("") // the user which has route notifications enabled
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .coordinates("-115.5747924943478,49.58740426100405;-115.33330133850265,49.444367698479994")
      .steps(true)
      .overview(DirectionsCriteria.OVERVIEW_FULL)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .excludeList(Arrays.asList(DirectionsCriteria.EXCLUDE_UNPAVED))
      .build();
    builder.routeOptions(routeOptions);
    builder.accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN);

    // 2. That's it! Now execute the command and get the response.
    Response<DirectionsResponse> response = builder.build().executeCall();

    // 3. Log information from the response
    System.out.println("Check that the GET response is successful " + response.isSuccessful());
    if (response.isSuccessful()) {
      List<Notification> notifications = response.body().routes().get(0).legs().get(0).notifications();
      System.out.println("Notifications: " + notifications);
    }
  }
}
