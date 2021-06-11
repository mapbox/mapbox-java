package com.mapbox.api.directions.v5;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * @since 4.4.0
 */
class DirectionsResponseFactory {

  private final MapboxDirections mapboxDirections;

  DirectionsResponseFactory(MapboxDirections mapboxDirections) {
    this.mapboxDirections = mapboxDirections;
  }

  Response<DirectionsResponse> generate(Response<DirectionsResponse> response) {
    if (isNotSuccessful(response)) {
      return response;
    } else {
      return Response.success(
        response
          .body()
          .toBuilder()
          .routes(generateRouteOptions(response))
          .build(),
        new okhttp3.Response.Builder()
          .code(200)
          .message("OK")
          .protocol(response.raw().protocol())
          .headers(response.headers())
          .request(response.raw().request())
          .build());
    }
  }

  private boolean isNotSuccessful(Response<DirectionsResponse> response) {
    return !response.isSuccessful()
      || response.body() == null
      || response.body().routes().isEmpty();
  }

  private List<DirectionsRoute> generateRouteOptions(Response<DirectionsResponse> response) {
    List<DirectionsRoute> routes = response.body().routes();
    List<DirectionsRoute> modifiedRoutes = new ArrayList<>();
    for (DirectionsRoute route : routes) {
      modifiedRoutes.add(route.toBuilder().routeOptions(mapboxDirections.routeOptions()).build());
    }
    return modifiedRoutes;
  }
}
