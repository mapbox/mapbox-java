package com.mapbox.services.cli.geojson;

import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.Polygon;

import java.util.ArrayList;
import java.util.List;

public class CreatePolygon {

  public static void main(String[] args) {

    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString outer = LineString.fromLngLats(points);

    List<LineString> inner = new ArrayList<>();
//    inner.add(LineString.fromLngLats(points));
//    inner.add(LineString.fromLngLats(points));
    Polygon polygon = Polygon.fromOuterInner(outer);

  }
}
