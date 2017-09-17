package com.mapbox.geojson.gson;

import com.google.gson.GsonBuilder;
import com.mapbox.geojson.Point;
import com.mapbox.services.BaseTest;

import org.junit.Test;

public class PointDeserializerTest extends BaseTest{

  private static final String FEATURE_FIXTURE = "sample-feature.json";
  private static final String POINT_FIXTURE = "sample-point.json";


  @Test
  public void deserialize_sanity() throws Exception {
    GsonBuilder gsonBuilder = new GsonBuilder()
      .registerTypeAdapter(Point.class, new PointDeserializer());
    Point point = gsonBuilder.create().fromJson(POINT_FIXTURE, Point.class);
    System.out.println(point.toString());



  }
}
