package com.mapbox.api.matching.v5;

import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.api.matching.v5.models.MapMatchingMatching;
import com.mapbox.api.matching.v5.models.MapMatchingResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * @since 4.4.0
 */
class MatchingResponseFactory {

  private static final String PLACEHOLDER_UUID = "mapmatching";
  private final MapboxMapMatching mapboxMapMatching;

  MatchingResponseFactory(MapboxMapMatching mapboxMapMatching) {
    this.mapboxMapMatching = mapboxMapMatching;
  }

  Response<MapMatchingResponse> generate(Response<MapMatchingResponse> response) {
    if (isNotSuccessful(response)) {
      return response;
    } else {
      return Response.success(
        response
          .body()
          .toBuilder()
          .matchings(generateRouteOptions(response))
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

  private boolean isNotSuccessful(Response<MapMatchingResponse> response) {
    return !response.isSuccessful()
      || response.body() == null
      || response.body().matchings() == null
      || response.body().matchings().isEmpty();
  }

  private List<MapMatchingMatching> generateRouteOptions(Response<MapMatchingResponse> response) {
    List<MapMatchingMatching> matchings = response.body().matchings();
    List<MapMatchingMatching> modifiedMatchings = new ArrayList<>();
    for (int i = 0; i < matchings.size(); i++) {
      MapMatchingMatching matching = matchings.get(i);
      modifiedMatchings.add(
        matching.toBuilder()
          .routeOptions(
            RouteOptions.builder()
              .profile(mapboxMapMatching.profile())
              .coordinates(mapboxMapMatching.coordinates())
              .annotations(mapboxMapMatching.annotations())
              .approaches(mapboxMapMatching.approaches())
              .language(mapboxMapMatching.language())
              .radiuses(mapboxMapMatching.radiuses())
              .user(mapboxMapMatching.user())
              .voiceInstructions(mapboxMapMatching.voiceInstructions())
              .bannerInstructions(mapboxMapMatching.bannerInstructions())
              .roundaboutExits(mapboxMapMatching.roundaboutExits())
              .geometries(mapboxMapMatching.geometries())
              .overview(mapboxMapMatching.overview())
              .steps(mapboxMapMatching.steps())
              .voiceUnits(mapboxMapMatching.voiceUnits())
              .waypointIndices(mapboxMapMatching.waypointIndices())
              .waypointNames(mapboxMapMatching.waypointNames())
              .baseUrl(mapboxMapMatching.baseUrl())
              .build()
          )
          .requestUuid(PLACEHOLDER_UUID)
          .routeIndex(String.valueOf(i))
          .build()
      );
    }
    return modifiedMatchings;
  }
}
