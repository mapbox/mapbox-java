package com.mapbox.samples;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

public class BasicFeatureCollection {

  public static void main(String[] args) {

    Feature feature = Feature.fromGeometry(Point.fromLngLat(1.0, 2.0));
    System.out.println(feature.toJson());

  }




}
