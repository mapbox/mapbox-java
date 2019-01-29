package com.mapbox.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.mapbox.core.TestUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiLineStringTest extends TestUtils {

  private static final String SAMPLE_MULTILINESTRING = "sample-multilinestring.json";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    List<LineString> lineStrings = new ArrayList<>();
    lineStrings.add(LineString.fromLngLats(points));
    lineStrings.add(LineString.fromLngLats(points));
    MultiLineString multiLineString = MultiLineString.fromLineStrings(lineStrings);
    assertNotNull(multiLineString);
  }

  @Test
  public void bbox_nullWhenNotSet() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    List<LineString> lineStrings = new ArrayList<>();
    lineStrings.add(LineString.fromLngLats(points));
    lineStrings.add(LineString.fromLngLats(points));
    MultiLineString multiLineString = MultiLineString.fromLineStrings(lineStrings);
    assertNull(multiLineString.bbox());
  }

  @Test
  public void bbox_doesNotSerializeWhenNotPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    List<LineString> lineStrings = new ArrayList<>();
    lineStrings.add(LineString.fromLngLats(points));
    lineStrings.add(LineString.fromLngLats(points));
    MultiLineString multiLineString = MultiLineString.fromLineStrings(lineStrings);
    compareJson(multiLineString.toJson(),
      "{\"type\":\"MultiLineString\",\"coordinates\":[[[1,2],[2,3]],[[1,2],[2,3]]]}");
  }

  @Test
  public void bbox_returnsCorrectBbox() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    List<LineString> lineStrings = new ArrayList<>();
    lineStrings.add(LineString.fromLngLats(points));
    lineStrings.add(LineString.fromLngLats(points));
    MultiLineString multiLineString = MultiLineString.fromLineStrings(lineStrings, bbox);
    assertNotNull(multiLineString.bbox());
    assertEquals(1.0, multiLineString.bbox().west(), DELTA);
    assertEquals(2.0, multiLineString.bbox().south(), DELTA);
    assertEquals(3.0, multiLineString.bbox().east(), DELTA);
    assertEquals(4.0, multiLineString.bbox().north(), DELTA);
  }

  @Test
  public void passingInSingleLineString_doesHandleCorrectly() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(3.0, 4.0));
    LineString geometry = LineString.fromLngLats(points);
    MultiLineString multiLineString = MultiLineString.fromLineString(geometry);
    assertNotNull(multiLineString);
    assertEquals(1, multiLineString.lineStrings().size());
    assertEquals(2.0, multiLineString.lineStrings().get(0).coordinates().get(0).latitude(), DELTA);
  }

  @Test
  public void bbox_doesSerializeWhenPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    List<LineString> lineStrings = new ArrayList<>();
    lineStrings.add(LineString.fromLngLats(points));
    lineStrings.add(LineString.fromLngLats(points));
    MultiLineString multiLineString = MultiLineString.fromLineStrings(lineStrings, bbox);
    compareJson(multiLineString.toJson(),
      "{\"type\":\"MultiLineString\",\"bbox\":[1.0,2.0,3.0,4.0],"
        + "\"coordinates\":[[[1,2],[2,3]],[[1,2],[2,3]]]}");
  }

  @Test
  public void testSerializable() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    List<LineString> lineStrings = new ArrayList<>();
    lineStrings.add(LineString.fromLngLats(points));
    lineStrings.add(LineString.fromLngLats(points));
    MultiLineString multiLineString = MultiLineString.fromLineStrings(lineStrings, bbox);
    byte[] bytes = serialize(multiLineString);
    assertEquals(multiLineString, deserialize(bytes, MultiLineString.class));
  }

  @Test
  public void fromJson() throws IOException {
    final String json = "{\"type\": \"MultiLineString\", " +
            "\"coordinates\": [[[100, 0],[101, 1]],[[102, 2],[103, 3]]] }";

    MultiLineString geo = MultiLineString.fromJson(json);
    assertEquals("MultiLineString", geo.type());
    assertEquals(geo.coordinates().get(0).get(0).longitude(), 100.0, DELTA);
    assertEquals(geo.coordinates().get(0).get(0).latitude(), 0.0, DELTA);
    assertFalse(geo.coordinates().get(0).get(0).hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    final String json = "{\"type\": \"MultiLineString\", " +
            "\"coordinates\": [[[100, 0],[101, 1]],[[102, 2],[103, 3]]] }";
    MultiLineString geo = MultiLineString.fromJson(json);
    compareJson(json, geo.toJson());
  }

  @Test
  public void fromJson_coordinatesPresent() throws Exception {
    thrown.expect(NullPointerException.class);
    MultiLineString.fromJson("{\"type\":\"MultiLineString\",\"coordinates\":null}");
  }
}
