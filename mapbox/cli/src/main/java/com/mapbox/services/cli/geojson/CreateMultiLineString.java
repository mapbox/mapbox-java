package com.mapbox.services.cli.geojson;

import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.MultiLineString;
import com.mapbox.services.commons.geojson.Point;

import java.util.ArrayList;
import java.util.List;

public class CreateMultiLineString {

  public static void main(String[] args) {

    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 1.0));
    points.add(Point.fromLngLat(2.0, 2.0));

    List<LineString> lineStrings = new ArrayList<>();
    lineStrings.add(LineString.fromLngLats(points));
    lineStrings.add(LineString.fromLngLats(points));
    MultiLineString multiLineString = MultiLineString.fromLineStrings(lineStrings);

    multiLineString.lineStrings().get(0).coordinates().get(0).latitude();

  }
}
