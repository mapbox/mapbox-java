package com.mapbox.samples;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;
import com.mapbox.sample.BuildConfig;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Shows how to make a directions request using some of the parameters offered.
 */
public class BasicDirections {

  public static void main(String[] args) throws IOException {
    simpleMapboxDirectionsRequest();
    asyncMapboxDirectionsRequest();
    simpleMapboxDirectionsPostRequest();
  }

  /**
   * Demonstrates how to make the most basic GET directions request.
   *
   * @throws IOException signals that an I/O exception of some sort has occurred
   */
  private static void simpleMapboxDirectionsRequest() throws IOException {

    MapboxDirections.Builder builder = MapboxDirections.builder();

    // 1. Pass in all the required information to get a simple directions route.
    builder.accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN);
    builder.origin(Point.fromLngLat(-95.6332, 29.7890));
    builder.destination(Point.fromLngLat(-95.3591, 29.7576));

    // 2. That's it! Now execute the command and get the response.
    Response<DirectionsResponse> response = builder.build().executeCall();

    // 3. Log information from the response
    System.out.printf("Check that the GET response is successful %b", response.isSuccessful());
    System.out.printf("%nGet the first routes distance from origin to destination: %f",
      response.body().routes().get(0).distance());
  }

  /**
   * Demonstrates how to make the most basic POST directions request.
   *
   * @throws IOException signals that an I/O exception of some sort has occurred
   */
  private static void simpleMapboxDirectionsPostRequest() throws IOException {

    MapboxDirections.Builder builder = MapboxDirections.builder();

    builder.accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN);
    builder.origin(Point.fromLngLat(-95.6332, 29.7890));
    builder.destination(Point.fromLngLat(-95.3591, 29.7576));
    builder.post();

    Response<DirectionsResponse> response = builder.build().executeCall();

    System.out.printf("%nCheck that the POST response is successful %b", response.isSuccessful());
    System.out.printf("%nGet the first routes distance from origin to destination: %f",
      response.body().routes().get(0).distance());
  }

  /**
   * Demonstrates how to make an asynchronous directions request.
   */
  private static void asyncMapboxDirectionsRequest() {

    // 1. Pass in all the required information to get a route.
    MapboxDirections request = MapboxDirections.builder()
      .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
      .origin(Point.fromLngLat(-95.6332, 29.7890))
      .destination(Point.fromLngLat(-95.3591, 29.7576))
      .profile(DirectionsCriteria.PROFILE_CYCLING)
      .steps(true)
      .build();

    // 2. Now request the route using a async call
    request.enqueueCall(new Callback<DirectionsResponse>() {
      @Override
      public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        // 3. Log information from the response
        if (response.isSuccessful()) {
          System.out.printf("%nGet the street name of the first step along the route: %s",
            response.body().routes().get(0).legs().get(0).steps().get(0).name());
        }
      }

      @Override
      public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
        System.err.println(throwable);
      }
    });
  }
}