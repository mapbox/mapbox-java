package com.mapbox.services.cli.geojson;

import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.custom.BoundingBox;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreatePoint {

  public static void main(String[] args) {

    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    LineString lineString = LineString.fromLngLats(points);
    lineString.coordinates().get(0).latitude();

//    BoundingBox bbox = BoundingBox.fromCoordinates(2.0, -3.0, 100, 4.0,5.0, 10);
//    Point point = Point.fromLngLat(2.0, 2.0, 3.0, bbox);
    Point point = Point.fromJson("{\"type\":\"Point\",\"bbox\":[2.0,-3.0,100.0,4.0,5.0,10.0],\"coordinates\":[2.0,2.0,3.0]}");
    System.out.println(point.toString());

  }
}
