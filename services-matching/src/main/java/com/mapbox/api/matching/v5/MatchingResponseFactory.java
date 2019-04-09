package com.mapbox.api.matching.v5;

import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.api.matching.v5.models.MapMatchingMatching;
import com.mapbox.api.matching.v5.models.MapMatchingResponse;
import com.mapbox.geojson.Point;

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
    for (MapMatchingMatching matching : matchings) {
      modifiedMatchings.add(matching.toBuilder().routeOptions(
        RouteOptions.builder()
          .profile(mapboxMapMatching.profile())
          .coordinates(formatCoordinates(mapboxMapMatching.coordinates()))
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
          .requestUuid(PLACEHOLDER_UUID)
          .accessToken(mapboxMapMatching.accessToken())
          .approaches(mapboxMapMatching.approaches())
          .waypointIndices(mapboxMapMatching.waypointIndices())
          .waypointNames(mapboxMapMatching.waypointNames())
          .baseUrl(mapboxMapMatching.baseUrl())
          .build()
      ).build());
    }
    return modifiedMatchings;
  }

  private static List<Point> formatCoordinates(String coordinates) {
    String[] coordPairs = coordinates.split(";", -1);
    List<Point> coordinatesFormatted = new ArrayList<>();
    for (String coordPair : coordPairs) {
      String[] coords = coordPair.split(",", -1);
      coordinatesFormatted.add(
        Point.fromLngLat(Double.valueOf(coords[0]), Double.valueOf(coords[1])));

    }
    return coordinatesFormatted;
  }
}
