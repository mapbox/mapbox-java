package com.mapbox.api.directions.v5;

import androidx.annotation.NonNull;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.RouteOptions;
import retrofit2.Response;

class DirectionsResponseFactory {

  static Response<DirectionsResponse> generate(
    @NonNull RouteOptions routeOptions,
    Response<DirectionsResponse> response
  ) {
    if (isNotSuccessful(response)) {
      return response;
    } else {
      return Response.success(
        response
          .body()
          .updateWithRequestData(routeOptions),
        response.raw()
      );
    }
  }

  private static boolean isNotSuccessful(Response<DirectionsResponse> response) {
    return !response.isSuccessful()
      || response.body() == null;
  }
}
