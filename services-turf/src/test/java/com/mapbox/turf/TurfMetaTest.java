package com.mapbox.turf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.MultiPolygon;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.junit.Assert.assertEquals;

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
}