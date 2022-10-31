package com.mapbox.samples;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.geojson.Point;
import com.mapbox.sample.BuildConfig;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows how to make a directions request using `waypoints_per_route=true` and access the waypoints from the response.
 */
public class BasicDirectionsWaypointsPerRoute {

  public static void main(String[] args) throws IOException {
    simpleMapboxDirectionsWithWaypointsPerRouteRequest();
  }

  private static void simpleMapboxDirectionsWithWaypointsPerRouteRequest() throws IOException {
    MapboxDirections.Builder builder = MapboxDirections.builder();

    // 1. Pass in all the required information to get a simple directions route.
    List<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(-95.6332, 29.7890));
    coordinates.add(Point.fromLngLat(-95.3591, 29.7576));
    RouteOptions routeOptions = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .coordinatesList(coordinates)
      .waypointsPerRoute(true)
      .alternatives(true)
      .build();
    builder.routeOptions(routeOptions);
    builder.accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN);

    // 2. That's it! Now execute the command and get the response.
    Response<DirectionsResponse> response = builder.build().executeCall();

    // 3. Log information from the response
    System.out.printf("%nCheck that the GET response is successful %b", response.isSuccessful());
    System.out.printf("%nFirst route's waypoints: %s", response.body().routes().get(0).waypoints());
    System.out.printf("%nSecond route's waypoints: %s", response.body().routes().get(1).waypoints());
    System.out.printf("%nRoot waypoints: %s", response.body().waypoints());
  }
}
