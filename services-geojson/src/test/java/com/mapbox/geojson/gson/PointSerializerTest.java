package com.mapbox.geojson.gson;


import com.mapbox.core.TestUtils;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Point;
import org.junit.Test;

public class PointSerializerTest extends TestUtils {

  private static final String POINT_FIXTURE = "sample-point.json";
  private static final String POINT_WITH_BBOX_FIXTURE = "sample-point-with-bbox.json";
  private static final String POINT_WITH_ALTITUDE_FIXTURE = "sample-point-with-altitude.json";

  @Test
  public void point_hasAltitudeValue() throws Exception {
    Point point = Point.fromLngLat(100, 0, 200,
      BoundingBox.fromLngLats(-100, -10, 100, 100, 10, 200));
    compareJson(loadJsonFixture(POINT_WITH_ALTITUDE_FIXTURE), point.toJson());
  }

  @Test
  public void point_hasBboxValue() throws Exception {
    Point point = Point.fromLngLat(100, 0,
      BoundingBox.fromLngLats(-100, -10,  100, 10));
    compareJson(loadJsonFixture(POINT_WITH_BBOX_FIXTURE), point.toJson());
  }

  @Test
  public void point_hasAltitudeAsNan() throws Exception {
    Point point = Point.fromLngLat(100, 0, Double.NaN);
    String expectedString = loadJsonFixture(POINT_FIXTURE);
    String jsonString = point.toJson();
    compareJson(expectedString, jsonString);
  }
}
