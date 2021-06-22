package com.mapbox.api.directions.v5;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

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
          .routes(updateRoutesWithRequestData(response))
          .build(),
        response.raw()
      );
    }
  }

  private boolean isNotSuccessful(Response<DirectionsResponse> response) {
    return !response.isSuccessful()
      || response.body() == null;
  }

  private List<DirectionsRoute> updateRoutesWithRequestData(Response<DirectionsResponse> response) {
    List<DirectionsRoute> routes = response.body().routes();
    List<DirectionsRoute> modifiedRoutes = new ArrayList<>();
    for (DirectionsRoute route : routes) {
      modifiedRoutes.add(
        route.toBuilder()
          .routeOptions(mapboxDirections.routeOptions())
          .requestUuid(response.body().uuid())
          .build()
      );
    }
    return modifiedRoutes;
  }
}
