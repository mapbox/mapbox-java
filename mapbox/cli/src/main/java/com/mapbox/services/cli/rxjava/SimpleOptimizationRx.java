package com.mapbox.services.cli.rxjava;

import com.mapbox.services.api.optimization.v1.MapboxOptimization;
import com.mapbox.services.api.optimization.v1.models.OptimizationResponse;
import com.mapbox.services.api.rx.optimization.v1.RxOptimization;
import com.mapbox.services.commons.geojson.Point;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

public class SimpleOptimizationRx {

  public static void main(String[] args) {

    MapboxOptimization optimization = MapboxOptimization.builder()
      .accessToken("pk.eyJ1IjoiY2FtbWFjZSIsImEiOiI5OGQxZjRmZGQ2YjU3Mzk1YjJmZTQ5ZDY2MTg1NDJiOCJ9.hIFoCKGAGOwQkKyVPvrxvQ")
      .coordinate(Point.fromLngLat(-71.05587, 42.36451))
      .coordinate(Point.fromLngLat(-71.06414, 42.34843))
      .coordinate(Point.fromLngLat(-71.09978, 42.34517))
      .build();

    RxOptimization.callback(optimization).subscribe(optimizationResponseResponse ->
      System.out.println(optimizationResponseResponse.body().trips().get(0).distance()));
  }
}
