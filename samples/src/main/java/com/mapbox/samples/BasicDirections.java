package com.mapbox.samples;

import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;
import com.mapbox.sample.BuildConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

/**
 * Shows how to make a directions request using some of the parameters offered.
 */
public class BasicDirections implements Callback<DirectionsResponse> {

  public static void main(String[] args) throws IOException {

    buildMapboxDirectionsRequest();


  }

  private static void buildMapboxDirectionsRequest() throws IOException {

    MapboxDirections.Builder builder = MapboxDirections.builder();

    // 1. Pass in all the required information to get a simple directions route.
    builder.accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN);
    builder.origin(Point.fromLngLat(-95.6332, 29.7890));
    builder.destination(Point.fromLngLat(-95.3591, 29.7576));

    // 2. That's it! Now execute the command and get the response.
    Response<DirectionsResponse> response = builder.build().executeCall();

    // 3. Log information from the response
    System.out.printf("Check that the response is successful %b", response.isSuccessful());
    System.out.printf("Get the first routes distance from origin to destination: %f",
      response.body().routes().get(0).distance());


//
//
//
//
//
//    return MapboxDirections.builder()
//      .origin(Point.fromLngLat(-95.6332, 29.7890))
//      .destination(Point.fromLngLat(-95.3591, 29.7576))
//      .bannerInstructions(true)
//      .voiceInstructions(true)
//      .annotations(DirectionsCriteria.ANNOTATION_CONGESTION)
//      .overview(DirectionsCriteria.OVERVIEW_FULL)
//      .addBearing(null, null)
//      .radiuses(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
//      .steps(true)
//      .build();
  }


  @Override
  public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

  }

  @Override
  public void onFailure(Call<DirectionsResponse> call, Throwable t) {

  }
}


//
//
//
//  // 1. Build the directions request using the provided builder.
//  MapboxDirections directions = buildMapboxDirections();
//
//// 2. Use either
//    directions.enqueueCall(new Callback<DirectionsResponse>() {
//@Override
//public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
//  System.out.println(response.code());
//  System.out.println(call.request().url());
//  System.out.println(response.body().routes().get(0).legs().get(0).steps().get(0).maneuver().location().latitude());
//  System.out.println(response.body().routes().get(0).distance());
//  System.out.println(response.body().routes().get(0).routeOptions().profile());
//  System.out.println(response.body().routes().get(0).routeOptions().alternatives());
//  System.out.println(response.body().routes().get(0).routeOptions().user());
//  System.out.println(response.body().routes().get(0).legs().get(0).steps().get(0).maneuver().toString());
//  System.out.println(response.body().routes().get(0).legs().get(0).steps().get(0)
//  .voiceInstructions().get(0).announcement());
//  System.out.println(response.body().routes().get(0).legs().get(0).annotation().congestion().size());
//  System.out.println("Distance: " + response.body().routes().get(0).legs().get(0).steps().get(0).bannerInstructions().get(0).distanceAlongGeometry());
//  }
//
//@Override
//public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
//  System.out.println(call.request().url());
//  System.out.println(throwable);
//  }
//  });