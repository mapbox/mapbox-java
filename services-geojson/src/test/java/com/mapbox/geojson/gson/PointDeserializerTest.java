package com.mapbox.geojson.gson;

import com.google.gson.GsonBuilder;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import org.junit.Test;

public class PointDeserializerTest extends TestUtils {

  private static final String POINT_FIXTURE = "sample-point.json";

  @Test
  public void deserialize_sanity() throws Exception {
    String fixtureJson = loadJsonFixture(POINT_FIXTURE);
    GsonBuilder gsonBuilder = new GsonBuilder()
      .registerTypeAdapter(Point.class, new PointDeserializer());
    Point point = gsonBuilder.create().fromJson(fixtureJson, Point.class);
    compareJson(point.toJson(), fixtureJson);
  }
}
