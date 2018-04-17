package com.mapbox.samples;

import com.mapbox.core.utils.ColorUtils;
import com.mapbox.geojson.Point;
import com.mapbox.api.staticmap.v1.MapboxStaticMap;
import com.mapbox.api.staticmap.v1.StaticMapCriteria;
import com.mapbox.api.staticmap.v1.models.StaticMarkerAnnotation;
import com.mapbox.api.staticmap.v1.models.StaticPolylineAnnotation;
import com.mapbox.sample.BuildConfig;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class BasicStaticMap {
  public static void main(String[] args) {

    List<StaticMarkerAnnotation> markers = new ArrayList<>();
    List<StaticPolylineAnnotation> polylines = new ArrayList<>();


    markers.add(StaticMarkerAnnotation.builder().name(StaticMapCriteria.LARGE_PIN)
      .lnglat(Point.fromLngLat(-122.46589, 37.77343))
      .color(ColorUtils.toHexString(
        Color.MAGENTA.getRed(),
        Color.MAGENTA.getGreen(),
        Color.MAGENTA.getBlue()))
      .label("a").build());

    markers.add(StaticMarkerAnnotation.builder().name(StaticMapCriteria.LARGE_PIN)
      .lnglat(Point.fromLngLat(-122.42816,37.75965))
      .color(Color.GREEN.getRed(),
        Color.GREEN.getGreen(),
        Color.GREEN.getBlue())
      .label("b")
      .build());

    polylines.add(StaticPolylineAnnotation.builder().polyline("abcdef").build());

    MapboxStaticMap mapboxStaticMap = MapboxStaticMap.builder()
      .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
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