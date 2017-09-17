package com.mapbox.samples;

import com.mapbox.directions.v5.DirectionsCriteria;
import com.mapbox.directions.v5.MapboxDirections;
import com.mapbox.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;
import retrofit2.Response;

import java.io.IOException;

public class DirectionsSample {

  public static void main(String[] args) throws IOException {
    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("")
      .overview(DirectionsCriteria.OVERVIEW_FULL)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
      .destination(Point.fromLngLat(-71.05818, 42.36294))
      .origin(Point.fromLngLat(-71.12375, 42.35196))
      .build();

    Response<DirectionsResponse> response = mapboxDirections.executeCall();
    System.out.println(response.body().routes());


  }
}
