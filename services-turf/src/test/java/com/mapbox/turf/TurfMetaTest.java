package com.mapbox.turf;

import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.GeometryCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TurfMetaTest extends TestUtils {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void coordAllPoint() throws TurfException {
    String jsonPoint = "{type: 'Point', coordinates: [0, 0]}";
    Point pointGeometry = Point.fromJson(jsonPoint);
    List<Point> resultList = TurfMeta.coordAll(pointGeometry);

    assertEquals(resultList.size(), 1, DELTA);
    assertEquals(resultList.get(0), Point.fromLngLat(0, 0));
  }

  @Test
  public void coordAllLineString() throws TurfException {
    String jsonLineString = "{type: 'LineString', coordinates: [[0, 0], [1, 1]]}";
    LineString lineStringGeometry = LineString.fromJson(jsonLineString);
    List<Point> resultList = TurfMeta.coordAll(lineStringGeometry);

    assertEquals(resultList.size(), 2, DELTA);
    assertEquals(resultList.get(0), Point.fromLngLat(0, 0));
    assertEquals(resultList.get(1), Point.fromLngLat(1, 1));
  }

  @Test
  public void coordAllPolygon() throws TurfException {
    String polygonString = "{type: 'Polygon', coordinates: [[[0, 0], [1, 1], [0, 1], [0, 0]]]}";
    Polygon polygonGeometry = Polygon.fromJson(polygonString);
    List<Point> resultList = TurfMeta.coordAll(polygonGeometry, false);

    assertEquals(resultList.size(), 4, DELTA);
    assertEquals(resultList.get(0), Point.fromLngLat(0, 0));
    assertEquals(resultList.get(1), Point.fromLngLat(1, 1));
    assertEquals(resultList.get(2), Point.fromLngLat(0, 1));
    assertEquals(resultList.get(3), Point.fromLngLat(0, 0));
  }

  @Test
  public void coordAllPolygonExcludeWrapCoord() throws TurfException {
    String polygonString = "{type: 'Polygon', coordinates: [[[0, 0], [1, 1], [0, 1], [0, 0]]]}";
    Polygon polygonGeometry = Polygon.fromJson(polygonString);
    List<Point> resultList = TurfMeta.coordAll(polygonGeometry, true);

    assertEquals(resultList.size(), 3, DELTA);
    assertEquals(resultList.get(0), Point.fromLngLat(0, 0));
    assertEquals(resultList.get(1), Point.fromLngLat(1, 1));
    assertEquals(resultList.get(2), Point.fromLngLat(0, 1));
  }

  @Test
  public void coordAllMultiPolygon() throws TurfException {
    String multipolygonString = "{type: 'MultiPolygon', coordinates: [[[[0, 0], [1, 1], [0, 1], [0, 0]]]]}";
    MultiPolygon multiPolygonGeometry = MultiPolygon.fromJson(multipolygonString);
    List<Point> resultList = TurfMeta.coordAll(multiPolygonGeometry, false);

    assertEquals(resultList.size(), 4, DELTA);
    assertEquals(resultList.get(0), Point.fromLngLat(0, 0));
    assertEquals(resultList.get(1), Point.fromLngLat(1, 1));
    assertEquals(resultList.get(2), Point.fromLngLat(0, 1));
    assertEquals(resultList.get(3), Point.fromLngLat(0, 0));
  }

  @Test
  public void testInvariantGetCoord() {
    String jsonFeature = "{type: 'Feature', geometry: {type: 'Point', coordinates: [1, 2]}}";
    assertEquals(TurfMeta.getCoord(Feature.fromJson(jsonFeature)),
      Point.fromLngLat(1, 2));
  }

  @Test
  public void coordAllFeatureCollection() throws TurfException {
    String multipolygonJson = "{type: 'MultiPolygon', coordinates: [[[[0, 0], [1, 1], [0, 1], [0, 0]]]]}";
    String lineStringJson = "{type: 'LineString', coordinates: [[0, 0], [1, 1]]}";
    FeatureCollection featureCollection = FeatureCollection.fromFeatures(
      new Feature[] {
        Feature.fromGeometry(MultiPolygon.fromJson(multipolygonJson)),
        Feature.fromGeometry(LineString.fromJson(lineStringJson))}
    );
    assertNotNull(featureCollection);
    assertEquals(5, TurfMeta.coordAll(featureCollection,true).size());
    assertEquals(0, TurfMeta.coordAll(featureCollection,true).get(0).latitude(), DELTA);
    assertEquals(0, TurfMeta.coordAll(featureCollection,true).get(0).longitude(), DELTA);
    assertEquals(1, TurfMeta.coordAll(featureCollection,true).get(4).latitude(), DELTA);
    assertEquals(1, TurfMeta.coordAll(featureCollection,true).get(4).longitude(), DELTA);
  }

  @Test
  public void coordAllSingleFeature() throws TurfException {
    String lineStringJson = "{type: 'LineString', coordinates: [[0, 0], [1, 1]]}";
    FeatureCollection featureCollection = FeatureCollection.fromFeature(
      Feature.fromGeometry(LineString.fromJson(lineStringJson))
    );
    assertNotNull(featureCollection);
    assertEquals(2, TurfMeta.coordAll(featureCollection,true).size());
    assertEquals(0, TurfMeta.coordAll(featureCollection,true).get(0).latitude(), DELTA);
    assertEquals(0, TurfMeta.coordAll(featureCollection,true).get(0).longitude(), DELTA);
    assertEquals(1, TurfMeta.coordAll(featureCollection,true).get(1).latitude(), DELTA);
    assertEquals(1, TurfMeta.coordAll(featureCollection,true).get(1).longitude(), DELTA);
  }

  @Test
  public void coordAllGeometryCollection() throws TurfException {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    List<Geometry> geometries = new ArrayList<>();
    geometries.add(points.get(0));
    geometries.add(lineString);

    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    GeometryCollection geometryCollection = GeometryCollection.fromGeometries(geometries, bbox);

    FeatureCollection featureCollection = FeatureCollection.fromFeature(
      Feature.fromGeometry(geometryCollection)
    );

    assertNotNull(featureCollection);
    assertNotNull(TurfMeta.coordAll(featureCollection,true));
    assertEquals(3, TurfMeta.coordAll(featureCollection,true).size());
    assertEquals(1.0, TurfMeta.coordAll(featureCollection,true).get(0).longitude(), DELTA);
    assertEquals(2.0, TurfMeta.coordAll(featureCollection,true).get(0).latitude(), DELTA);
  }

  @Test
  public void wrongFeatureGeometryForGetCoordThrowsException() throws TurfException {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("A Feature with a Point geometry is required."));
    TurfMeta.getCoord(Feature.fromGeometry(LineString.fromLngLats(Arrays.asList(
      Point.fromLngLat(0, 9),
      Point.fromLngLat(0, 10)
    ))));
  }
}