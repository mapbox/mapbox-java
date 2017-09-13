package com.mapbox.services.commons.geojson;

import com.mapbox.services.commons.geojson.custom.BoundingBox;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class GeometryCollectionTest extends BaseTest {

  private static final String SAMPLE_GEOMETRYCOLLECTION = "sample-geometrycollection.json";

  @Test
  public void sanity() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    List<Geometry> geometries = new ArrayList<>();
    geometries.add(points.get(0));
    geometries.add(lineString);

    GeometryCollection geometryCollection = GeometryCollection.fromGeometries(geometries);
    assertNotNull(geometryCollection);
  }

  @Test
  public void bbox_nullWhenNotSet() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    List<Geometry> geometries = new ArrayList<>();
    geometries.add(points.get(0));
    geometries.add(lineString);

    GeometryCollection geometryCollection = GeometryCollection.fromGeometries(geometries);
    assertNull(geometryCollection.bbox());
  }

  @Test
  public void bbox_doesNotSerializeWhenNotPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    List<Geometry> geometries = new ArrayList<>();
    geometries.add(points.get(0));
    geometries.add(lineString);

    GeometryCollection geometryCollection = GeometryCollection.fromGeometries(geometries);
    compareJson(geometryCollection.toJson(),
      "{\"type\":\"GeometryCollection\",\"geometries\":["+ "{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},"
        + "{\"type\":\"LineString\",\"coordinates\":[[1,2],[2,3]]}]}");
  }

  @Test
  public void bbox_returnsCorrectBbox() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    List<Geometry> geometries = new ArrayList<>();
    geometries.add(points.get(0));
    geometries.add(lineString);

    BoundingBox bbox = BoundingBox.fromCoordinates(1.0, 2.0, 3.0, 4.0);
    GeometryCollection geometryCollection = GeometryCollection.fromGeometries(geometries, bbox);
    assertNotNull(geometryCollection.bbox());
    assertEquals(1.0, geometryCollection.bbox().west(), DELTA);
    assertEquals(2.0, geometryCollection.bbox().south(), DELTA);
    assertEquals(3.0, geometryCollection.bbox().east(), DELTA);
    assertEquals(4.0, geometryCollection.bbox().north(), DELTA);
  }

  @Test
  public void bbox_doesSerializeWhenPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    List<Geometry> geometries = new ArrayList<>();
    geometries.add(points.get(0));
    geometries.add(lineString);

    BoundingBox bbox = BoundingBox.fromCoordinates(1.0, 2.0, 3.0, 4.0);
    GeometryCollection geometryCollection = GeometryCollection.fromGeometries(geometries, bbox);
    compareJson(geometryCollection.toJson(),
      "{\"type\":\"GeometryCollection\",\"bbox\":[1.0,2.0,3.0,4.0],"
        + "\"geometries\":[{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},"
        + "{\"type\":\"LineString\",\"coordinates\":[[1,2],[2,3]]}]}");
  }

  @Test
  public void testSerializable() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    List<Geometry> geometries = new ArrayList<>();
    geometries.add(points.get(0));
    geometries.add(lineString);

    BoundingBox bbox = BoundingBox.fromCoordinates(1.0, 2.0, 3.0, 4.0);
    GeometryCollection geometryCollection = GeometryCollection.fromGeometries(geometries, bbox);
    byte[] bytes = serialize(geometryCollection);
    assertEquals(geometryCollection, deserialize(bytes, GeometryCollection.class));
  }

  @Test
  public void fromJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_GEOMETRYCOLLECTION);
    GeometryCollection geo = GeometryCollection.fromJson(json);
    assertEquals(geo.type(), "GeometryCollection");
    assertEquals(geo.geometries().get(0).type(), "Point");
    assertEquals(geo.geometries().get(1).type(), "LineString");
  }

  @Test
  public void toJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_GEOMETRYCOLLECTION);
    GeometryCollection geo = GeometryCollection.fromJson(json);
    compareJson(json, geo.toJson());
  }
}
