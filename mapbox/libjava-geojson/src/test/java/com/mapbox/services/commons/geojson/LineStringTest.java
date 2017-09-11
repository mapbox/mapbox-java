package com.mapbox.services.commons.geojson;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class LineStringTest extends BaseTest {

  private static final String SAMPLE_LINESTRING_FIXTURE = "sample-linestring.json";

    @Test
    public void sanity() throws Exception {
      List<Point> points = new ArrayList<>();
      points.add(Point.fromLngLat(1.0, 1.0));
      points.add(Point.fromLngLat(2.0, 2.0));
      points.add(Point.fromLngLat(3.0, 3.0));
      LineString lineString = LineString.fromLngLats(points);
      assertNotNull(lineString);
    }

    @Test
    public void bbox_nullWhenNotSet() throws Exception {
      List<Point> points = new ArrayList<>();
      points.add(Point.fromLngLat(1.0, 1.0));
      points.add(Point.fromLngLat(2.0, 2.0));
      points.add(Point.fromLngLat(3.0, 3.0));
      LineString lineString = LineString.fromLngLats(points);
      assertNull(lineString.bbox());
    }

  @Test
  public void bbox_doesNotSerializeWhenNotPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 1.0));
    points.add(Point.fromLngLat(2.0, 2.0));
    points.add(Point.fromLngLat(3.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    compareJson(lineString.toJson(),
      "{\"coordinates\":[[1,1],[2,2],[3,3]],\"type\":\"LineString\"}");
  }

  @Test
    public void bbox_returnsCorrectBbox() throws Exception {
      List<Point> points = new ArrayList<>();
      points.add(Point.fromLngLat(1.0, 1.0));
      points.add(Point.fromLngLat(2.0, 2.0));
      points.add(Point.fromLngLat(3.0, 3.0));
      double[] bbox = new double[] {1.0, 2.0, 3.0, 4.0};
      LineString lineString = LineString.fromLngLats(points, bbox);
      assertNotNull(lineString.bbox());
      assertEquals(4, lineString.bbox().length);
      assertEquals(1.0, lineString.bbox()[0], DELTA);
      assertEquals(2.0, lineString.bbox()[1], DELTA);
      assertEquals(3.0, lineString.bbox()[2], DELTA);
      assertEquals(4.0, lineString.bbox()[3], DELTA);
    }

  @Test
  public void bbox_doesSerializeWhenPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 1.0));
    points.add(Point.fromLngLat(2.0, 2.0));
    points.add(Point.fromLngLat(3.0, 3.0));
    double[] bbox = new double[] {1.0, 2.0, 3.0, 4.0};
    LineString lineString = LineString.fromLngLats(points, bbox);
    compareJson(lineString.toJson(),
      "{\"coordinates\":[[1,1],[2,2],[3,3]]," +
        "\"type\":\"LineString\",\"bbox\":[1.0,2.0,3.0,4.0]}");
  }

    @Test
    public void testSerializable() throws Exception {
      List<Point> points = new ArrayList<>();
      points.add(Point.fromLngLat(1.0, 1.0));
      points.add(Point.fromLngLat(2.0, 2.0));
      points.add(Point.fromLngLat(3.0, 3.0));
      double[] bbox = new double[] {1.0, 2.0, 3.0, 4.0};
      LineString lineString = LineString.fromLngLats(points, bbox);
      byte[] bytes = serialize(lineString);
      assertEquals(lineString, deserialize(bytes, LineString.class));
    }

  @Test
  public void fromJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_LINESTRING_FIXTURE);
    System.out.println(json);
    LineString geo = LineString.fromJson(json);
    assertEquals(geo.type(), "LineString");
    assertEquals(geo.coordinates().get(0).longitude(), 100.0, 0.0);
    assertEquals(geo.coordinates().get(0).latitude(), 0.0, 0.0);
    assertFalse(geo.coordinates().get(0).hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_LINESTRING_FIXTURE);
    LineString geo = LineString.fromJson(json);
    compareJson(json, geo.toJson());
  }
}