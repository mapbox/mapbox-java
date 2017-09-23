package com.mapbox.samples;

import com.mapbox.staticmap.v1.MapboxStaticMap;
import com.mapbox.staticmap.v1.models.StaticMarkerAnnotation;

public class BasicStaticMap {
  public static void main(String[] args) {
    MapboxStaticMap mapboxStaticMap = MapboxStaticMap.builder()
      .accessToken("pk.eyJ1IjoiY2FtbWFjZSIsImEiOiI5OGQxZjRmZGQ2YjU3Mzk1YjJmZTQ5ZDY2MTg1NDJiOCJ9.hIFoCKGAGOwQkKyVPvrxvQ")
      .width(300)
      .height(300)
      .build();

    StaticMarkerAnnotation.builder().name().label("vdjsinv").build();


    System.out.println(mapboxStaticMap.url());
  }
}
