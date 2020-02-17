package com.mapbox.api.directions.v5;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.api.directions.v5.utils.ParseUtils;

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
      modifiedRoutes.add(route.toBuilder().routeOptions(
        RouteOptions.builder()
          .profile(mapboxDirections.profile())
          .coordinates(mapboxDirections.coordinates())
          .waypointIndicesList(ParseUtils.parseToIntegers(mapboxDirections.waypointIndices()))
          .waypointNamesList(ParseUtils.parseToStrings(mapboxDirections.waypointNames()))
          .waypointTargetsList(ParseUtils.parseToPoints(mapboxDirections.waypointTargets()))
          .continueStraight(mapboxDirections.continueStraight())
          .annotations(mapboxDirections.annotation())
          .approachesList(ParseUtils.parseToStrings(mapboxDirections.approaches()))
          .bearingsList(ParseUtils.parseToListOfListOfDoubles(mapboxDirections.bearing()))
          .alternatives(mapboxDirections.alternatives())
          .language(mapboxDirections.language())
          .radiusesList(ParseUtils.parseToDoubles(mapboxDirections.radius()))
          .user(mapboxDirections.user())
          .voiceInstructions(mapboxDirections.voiceInstructions())
          .bannerInstructions(mapboxDirections.bannerInstructions())
          .roundaboutExits(mapboxDirections.roundaboutExits())
          .geometries(mapboxDirections.geometries())
          .overview(mapboxDirections.overview())
          .steps(mapboxDirections.steps())
          .exclude(mapboxDirections.exclude())
          .voiceUnits(mapboxDirections.voiceUnits())
          .accessToken(mapboxDirections.accessToken())
          .requestUuid(response.body().uuid())
          .baseUrl(mapboxDirections.baseUrl())
          .walkingOptions(mapboxDirections.walkingOptions())
          .build()
      ).build());
    }
    return modifiedRoutes;
  }
}
