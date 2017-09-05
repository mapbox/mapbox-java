package com.mapbox.services.commons.geojson;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class PointTest extends BaseTest {

  private static final String SAMPLE_POINT_FIXTURE = "src/test/fixtures/sample-point.json";

  @Test
  public void sanityTest() throws Exception {
    Point point = Point.fromLngLat(1.0, 1.0);
    assertNotNull(point);
  }

  @Test
  public void fromJson_doesDeserializeCorrectly() throws IOException {
    String geoJson = loadJsonFixture(SAMPLE_POINT_FIXTURE);
    Point point = Point.fromJson(geoJson);
    assertEquals(point.type(), "Point");
    assertEquals(point.longitude(), 100.0, 0.0);
    assertEquals(point.latitude(), 0.0, 0.0);
    assertFalse(point.hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    String geoJson = loadJsonFixture(SAMPLE_POINT_FIXTURE);
    Point point = Point.fromJson(geoJson);
    compareJson(geoJson, point.toJson());
  }

  @Test
  public void checksEqualityFromCoordinates() {
    Point point = Point.fromLngLat(100.0, 0.0);
    String pointCoordinates = obtainLiteralCoordinatesFrom(point);
    assertEquals("Point: \n"
      + "Position [longitude=100.0, latitude=0.0, altitude=NaN]\n", pointCoordinates);
  }

  @Test
  public void checksJsonEqualityFromCoordinates() {
    Point point = Point.fromLngLat(100.0, 0.0);
    String pointJsonCoordinates = point.toJson();
    compareJson("{ \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }", pointJsonCoordinates);
  }

  private String obtainLiteralCoordinatesFrom(Point point) {
    StringBuilder literalCoordinates = new StringBuilder();
    literalCoordinates.append("Point: \n");
    literalCoordinates.append(point.toString());
    literalCoordinates.append("\n");
    return literalCoordinates.toString();
  }
}

