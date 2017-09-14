package com.mapbox.geojson;

import org.junit.Test;

public class PointTest {

  @Test
  public void name() throws Exception {
    Point p = Point.create("hello");
    System.out.println(p.string());
  }
}
