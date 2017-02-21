package com.mapbox.services.api.turf;

import com.mapbox.services.api.utils.turf.TurfException;
import com.mapbox.services.api.utils.turf.TurfMeta;
import com.mapbox.services.commons.geojson.GeometryCollection;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.MultiPolygon;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TurfMetaTest extends BaseTurf {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void coordEachPoint() throws TurfException {

    String jsonPoint = "{type: 'Point', coordinates: [0, 0]}";
    Point pointGeometry = Point.fromJson(jsonPoint);
    List<Position> resultList = TurfMeta.coordEach(pointGeometry, false);

    assertEquals(resultList.size(), 1, DELTA);
    assertEquals(resultList.get(0), Position.fromCoordinates(0, 0));
  }

  @Test
  public void coordEachLineString() throws TurfException {
    String jsonLineString = "{type: 'LineString', coordinates: [[0, 0], [1, 1]]}";
    LineString lineStringGeometry = LineString.fromJson(jsonLineString);
    List<Position> resultList = TurfMeta.coordEach(lineStringGeometry, false);

    assertEquals(resultList.size(), 2, DELTA);
    assertEquals(resultList.get(0), Position.fromCoordinates(0, 0));
    assertEquals(resultList.get(1), Position.fromCoordinates(1, 1));
  }

  @Test
  public void coordEachPolygon() throws TurfException {
    String polygonString = "{type: 'Polygon', coordinates: [[[0, 0], [1, 1], [0, 1], [0, 0]]]}";
    Polygon polygonGeometry = Polygon.fromJson(polygonString);
    List<Position> resultList = TurfMeta.coordEach(polygonGeometry, false);

    assertEquals(resultList.size(), 4, DELTA);
    assertEquals(resultList.get(0), Position.fromCoordinates(0, 0));
    assertEquals(resultList.get(1), Position.fromCoordinates(1, 1));
    assertEquals(resultList.get(2), Position.fromCoordinates(0, 1));
    assertEquals(resultList.get(3), Position.fromCoordinates(0, 0));
  }

  @Test
  public void coordEachPolygonExcludeWrapCoord() throws TurfException {
    String polygonString = "{type: 'Polygon', coordinates: [[[0, 0], [1, 1], [0, 1], [0, 0]]]}";
    Polygon polygonGeometry = Polygon.fromJson(polygonString);
    List<Position> resultList = TurfMeta.coordEach(polygonGeometry, true);

    assertEquals(resultList.size(), 3, DELTA);
    assertEquals(resultList.get(0), Position.fromCoordinates(0, 0));
    assertEquals(resultList.get(1), Position.fromCoordinates(1, 1));
    assertEquals(resultList.get(2), Position.fromCoordinates(0, 1));
  }

  @Test
  public void coordEachMultiPolygon() throws TurfException {
    String multipolygonString = "{type: 'MultiPolygon', coordinates: [[[[0, 0], [1, 1], [0, 1], [0, 0]]]]}";
    MultiPolygon multiPolygonGeometry = MultiPolygon.fromJson(multipolygonString);
    List<Position> resultList = TurfMeta.coordEach(multiPolygonGeometry, false);

    assertEquals(resultList.size(), 4, DELTA);
    assertEquals(resultList.get(0), Position.fromCoordinates(0, 0));
    assertEquals(resultList.get(1), Position.fromCoordinates(1, 1));
    assertEquals(resultList.get(2), Position.fromCoordinates(0, 1));
    assertEquals(resultList.get(3), Position.fromCoordinates(0, 0));
  }

  @Test
  public void coordEachGeometryCollection() throws TurfException {
    String pointGeometry = "{type: 'Point', coordinates: [0, 0]}";
    String lineStringGeometry = "{type: 'LineString', coordinates: [[0, 0], [1, 1]]}";
    String geometryCollection =
      "{type: 'GeometryCollection', geometries: [" + pointGeometry + ", " + lineStringGeometry + "]}";
    GeometryCollection geometryCollectionGeometry = GeometryCollection.fromJson(geometryCollection);
    List<Position> resultList = TurfMeta.coordEach(geometryCollectionGeometry, false);

    assertEquals(resultList.size(), 3, DELTA);
    assertEquals(resultList.get(0), Position.fromCoordinates(0, 0));
    assertEquals(resultList.get(1), Position.fromCoordinates(0, 0));
    assertEquals(resultList.get(2), Position.fromCoordinates(1, 1));
  }
}
