package com.mapbox.geojson.gson;

import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;
import org.junit.Test;

public class PointSerializerTest extends TestUtils {

  private static final String POINT_WITH_BBOX_FIXTURE = "sample-point-with-bbox.json";

  @Test
  public void point_hasAltitudeValue() throws Exception {
    Point point = Point.fromLngLat(100, 0, 200);
    compareJson(loadJsonFixture(POINT_WITH_BBOX_FIXTURE), point.toJson());
  }
}
