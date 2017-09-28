package com.mapbox.samples;

import com.mapbox.geojson.Point;
import com.mapbox.staticmap.v1.MapboxStaticMap;
import com.mapbox.staticmap.v1.StaticMapCriteria;
import com.mapbox.staticmap.v1.models.StaticMarkerAnnotation;
import com.mapbox.staticmap.v1.models.StaticPolylineAnnotation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class BasicStaticMap {
  public static void main(String[] args) {

    List<StaticMarkerAnnotation> markers = new ArrayList<>();
    List<StaticPolylineAnnotation> polylines = new ArrayList<>();


    markers.add(StaticMarkerAnnotation.builder().name(StaticMapCriteria.LARGE_PIN)
      .lnglat(Point.fromLngLat(-122.46589, 37.77343))
      .color(Color.MAGENTA).label("a").build());

    markers.add(StaticMarkerAnnotation.builder().name(StaticMapCriteria.LARGE_PIN)
      .lnglat(Point.fromLngLat(-122.42816,37.75965))
      .color(Color.GREEN).label("b").build());

    polylines.add(StaticPolylineAnnotation.builder().polyline("abcdef").build());

    MapboxStaticMap mapboxStaticMap = MapboxStaticMap.builder()
      .accessToken("pk.eyJ1IjoiY2FtbWFjZSIsImEiOiI5OGQxZjRmZGQ2YjU3Mzk1YjJmZTQ5ZDY2MTg1NDJiOCJ9.hIFoCKGAGOwQkKyVPvrxvQ")
      .width(500)
      .height(300)
      .retina(true)
      .cameraAuto(true)
      .staticMarkerAnnotations(markers)
      .staticPolylineAnnotations(polylines)
      .build();

    System.out.println(mapboxStaticMap.url());
  }
}