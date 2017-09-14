package com.mapbox.services.cli.geojson;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.Point;

public class CreateFeature {

  public static void main(String[] args) {
    Point point = Point.fromLngLat(2.0, 2.0);

    Feature feature = Feature.fromGeometry(point);
  }
}
