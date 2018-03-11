package com.mapbox.samples;

import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;
import com.mapbox.rx.api.directions.v5.RxDirections;
import io.reactivex.functions.Consumer;

@SuppressWarnings("CheckReturnValue")
public class RxDirectionsSample {

  public static void main(String[] args) {

    MapboxDirections mapboxDirections = MapboxDirections.builder()
      .accessToken("pk.eyJ1IjoiY2FtbWFjZSIsImEiOiI5OGQxZjRmZGQ2YjU3Mzk1YjJmZTQ5ZDY2MTg1NDJiOCJ9.hIFoCKGAGOwQkKyVPvrxvQ")
      .destination(Point.fromLngLat(-95.6332, 29.7890))
      .origin(Point.fromLngLat(-95.3591, 29.7576))
      .build();

    RxDirections.enqueueCall(mapboxDirections)
      .subscribe(new Consumer<DirectionsResponse>() {
        @Override
        public void accept(DirectionsResponse response) throws Exception {
          System.out.printf("Total distance: %f", response.routes().get(0).distance());
        }
      });
  }
}
