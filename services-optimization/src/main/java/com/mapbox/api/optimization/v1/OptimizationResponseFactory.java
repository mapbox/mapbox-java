package com.mapbox.api.optimization.v1;

import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.api.optimization.v1.models.OptimizationResponse;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

class OptimizationResponseFactory {

  private final MapboxOptimization mapboxOptimization;

  OptimizationResponseFactory(MapboxOptimization mapboxOptimization) {
    this.mapboxOptimization = mapboxOptimization;
  }

  Response<OptimizationResponse> generate(Response<OptimizationResponse> response) {
    if (isNotSuccessful(response)) {
      return response;
    } else {
      return Response.success(
        response
          .body()
          .toBuilder()
          .trips(generateDirectionRoutes(response))
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

  private boolean isNotSuccessful(Response<OptimizationResponse> response) {
    return !response.isSuccessful()
      || response.body() == null
      || response.body().trips() == null
      || response.body().trips().isEmpty();
  }

  private List<DirectionsRoute> generateDirectionRoutes(Response<OptimizationResponse> response) {
    List<DirectionsRoute> routes = response.body().trips();
    List<DirectionsRoute> modifiedRoutes = new ArrayList<>();
    for (int i = 0; i < routes.size(); i++) {
      DirectionsRoute route = routes.get(i);
      modifiedRoutes.add(
        route.toBuilder()
          .routeOptions(generateRouteOptions())
          .routeIndex(String.valueOf(i))
          .build()
      );
    }
    return modifiedRoutes;
  }

  private RouteOptions generateRouteOptions() {
    RouteOptions.Builder builder = RouteOptions.builder()
      .profile(mapboxOptimization.profile())
      .coordinates(mapboxOptimization.coordinates())
      .annotations(mapboxOptimization.annotations())
      .bearings(mapboxOptimization.bearings())
      .language(mapboxOptimization.language())
      .radiuses(mapboxOptimization.radiuses())
      .user(mapboxOptimization.user())
      .overview(mapboxOptimization.overview())
      .steps(mapboxOptimization.steps())
      .baseUrl(mapboxOptimization.baseUrl());
    if (mapboxOptimization.geometries() != null) {
      builder.geometries(mapboxOptimization.geometries());
    }
    return builder.build();
  }
}
