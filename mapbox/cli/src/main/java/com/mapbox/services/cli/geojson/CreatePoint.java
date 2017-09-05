package com.mapbox.services.cli.geojson;

import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;

import java.util.ArrayList;
import java.util.List;

public class CreatePoint {

  public static void main(String[] args) {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    LineString lineString = LineString.fromLngLats(points);
    lineString.coordinates().get(0).latitude();

    Point point = Point.fromLngLat(2.0, 3.0);

  }


}
