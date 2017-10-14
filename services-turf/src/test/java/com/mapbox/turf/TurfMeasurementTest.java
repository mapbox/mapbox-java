package com.mapbox.turf;

import static com.mapbox.turf.TurfConstants.UNIT_DEGREES;
import static com.mapbox.turf.TurfConstants.UNIT_KILOMETERS;
import static com.mapbox.turf.TurfConstants.UNIT_MILES;
import static com.mapbox.turf.TurfConstants.UNIT_NAUTICAL_MILES;
import static com.mapbox.turf.TurfConstants.UNIT_RADIANS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.services.TestUtils;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TurfMeasurementTest extends TestUtils {

  private static final String LINE_DISTANCE_ROUTE_ONE = "turf-line-distance/route1.geojson";
  private static final String LINE_DISTANCE_ROUTE_TWO = "turf-line-distance/route2.geojson";
  private static final String LINE_DISTANCE_POLYGON = "turf-line-distance/polygon.geojson";
  private static final String TURF_ALONG_DC_LINE = "turf-along/dc-line.geojson";
  private static final String TURF_BBOX_POINT = "turf-bbox/point.geojson";
  private static final String TURF_BBOX_LINESTRING = "turf-bbox/linestring.geojson";
  private static final String TURF_BBOX_POLYGON = "turf-bbox/polygon.geojson";
  private static final String TURF_BBOX_MULTILINESTRING = "turf-bbox/multilinestring.geojson";
  private static final String TURF_BBOX_MULTIPOLYGON = "turf-bbox/multipolygon.geojson";
  private static final String LINE_DISTANCE_MULTILINESTRING
    = "turf-line-distance/multilinestring.geojson";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testBearing() {
    Point pt1 = Point.fromLngLat(-75.4, 39.4);
    Point pt2 = Point.fromLngLat(-75.534, 39.123);
    assertNotEquals(TurfMeasurement.bearing(pt1, pt2), 0, DELTA);
  }

  @Test
  public void testDestination() throws TurfException {
    Point pt1 = Point.fromLngLat(-75.0, 39.0);
    double dist = 100;
    double bear = 180;
    assertNotNull(TurfMeasurement.destination(pt1, dist, bear, UNIT_KILOMETERS));
  }

  /*
   * Turf distance tests
   */

  @Test
  public void testDistance() throws TurfException {
    Point pt1 = Point.fromLngLat(-75.343, 39.984);
    Point pt2 = Point.fromLngLat(-75.534, 39.123);

    // Common cases
    assertEquals(60.37218405837491, TurfMeasurement.distance(pt1, pt2, UNIT_MILES), DELTA);
    assertEquals(52.461979624130436,
      TurfMeasurement.distance(pt1, pt2, UNIT_NAUTICAL_MILES), DELTA);
    assertEquals(97.15957803131901, TurfMeasurement.distance(pt1, pt2, UNIT_KILOMETERS), DELTA);
    assertEquals(0.015245501024842149, TurfMeasurement.distance(pt1, pt2, UNIT_RADIANS), DELTA);
    assertEquals(0.8735028650863799, TurfMeasurement.distance(pt1, pt2, UNIT_DEGREES), DELTA);

    // This also works
    assertEquals(97.15957803131901, TurfMeasurement.distance(pt1, pt2, UNIT_KILOMETERS), DELTA);

    // Default is kilometers
    assertEquals(97.15957803131901, TurfMeasurement.distance(pt1, pt2), DELTA);

    // Bad units not possible
  }

  @Test
  public void lineDistance_returnsZeroWhenRouteIsPoint() throws Exception {
    List<Point> coords = new ArrayList<>();
    coords.add(Point.fromLngLat(1.0, 1.0));

    LineString lineString = LineString.fromLngLats(coords);
    double distance = TurfMeasurement.lineDistance(lineString, TurfConstants.UNIT_METERS);
    assertEquals(0d, distance, DELTA);
  }

  @Test
  public void testLineDistanceWithGeometries() throws IOException, TurfException {
    Feature route1 = Feature.fromJson(loadJsonFixture(LINE_DISTANCE_ROUTE_ONE));
    Feature route2 = Feature.fromJson(loadJsonFixture(LINE_DISTANCE_ROUTE_TWO));
    assertEquals(202, Math.round(TurfMeasurement.lineDistance((LineString) route1.geometry(),
      UNIT_MILES)));
    Assert.assertEquals(741.7787396994203,
      TurfMeasurement.lineDistance((LineString) route2.geometry(), UNIT_KILOMETERS), DELTA);
  }

  @Test
  public void testLineDistancePolygon() throws IOException, TurfException {
    Feature feature = Feature.fromJson(loadJsonFixture(LINE_DISTANCE_POLYGON));
    assertEquals(5599, Math.round(1000 * TurfMeasurement.lineDistance((Polygon) feature.geometry(),
      UNIT_KILOMETERS)));
  }

  @Test
  public void testLineDistanceMultiLineString() throws IOException, TurfException {
    Feature feature = Feature.fromJson(loadJsonFixture(LINE_DISTANCE_MULTILINESTRING));
    assertEquals(4705d, Math.round(1000
      * TurfMeasurement.lineDistance((MultiLineString) feature.geometry(),
      UNIT_KILOMETERS)), DELTA);
  }

  /*
   * Turf midpoint tests
   */

  @Test
  public void testMidpointHorizontalEquator() throws TurfException {
    Point pt1 = Point.fromLngLat(0, 0);
    Point pt2 = Point.fromLngLat(10, 0);
    Point mid = TurfMeasurement.midpoint(pt1, pt2);

    assertEquals(TurfMeasurement.distance(pt1, mid, UNIT_MILES),
      TurfMeasurement.distance(pt2, mid, UNIT_MILES), DELTA);
  }

  @Test
  public void testMidpointVericalFromEquator() throws TurfException {
    Point pt1 = Point.fromLngLat(0, 0);
    Point pt2 = Point.fromLngLat(0, 10);
    Point mid = TurfMeasurement.midpoint(pt1, pt2);

    assertEquals(TurfMeasurement.distance(pt1, mid, UNIT_MILES),
      TurfMeasurement.distance(pt2, mid, UNIT_MILES), DELTA);
  }

  @Test
  public void testMidpointVericalToEquator() throws TurfException {
    Point pt1 = Point.fromLngLat(0, 10);
    Point pt2 = Point.fromLngLat(0, 0);
    Point mid = TurfMeasurement.midpoint(pt1, pt2);

    assertEquals(TurfMeasurement.distance(pt1, mid, UNIT_MILES),
      TurfMeasurement.distance(pt2, mid, UNIT_MILES), DELTA);
  }

  @Test
  public void testMidpointDiagonalBackOverEquator() throws TurfException {
    Point pt1 = Point.fromLngLat(-1, 10);
    Point pt2 = Point.fromLngLat(1, -1);
    Point mid = TurfMeasurement.midpoint(pt1, pt2);

    assertEquals(TurfMeasurement.distance(pt1, mid, UNIT_MILES),
      TurfMeasurement.distance(pt2, mid, UNIT_MILES), DELTA);
  }

  @Test
  public void testMidpointDiagonalForwardOverEquator() throws TurfException {
    Point pt1 = Point.fromLngLat(-5, -1);
    Point pt2 = Point.fromLngLat(5, 10);
    Point mid = TurfMeasurement.midpoint(pt1, pt2);

    assertEquals(TurfMeasurement.distance(pt1, mid, UNIT_MILES),
      TurfMeasurement.distance(pt2, mid, UNIT_MILES), DELTA);
  }

  @Test
  public void testMidpointLongDistance() throws TurfException {
    Point pt1 = Point.fromLngLat(22.5, 21.94304553343818);
    Point pt2 = Point.fromLngLat(92.10937499999999, 46.800059446787316);
    Point mid = TurfMeasurement.midpoint(pt1, pt2);

    assertEquals(TurfMeasurement.distance(pt1, mid, UNIT_MILES),
      TurfMeasurement.distance(pt2, mid, UNIT_MILES), DELTA);
  }

  // Custom test to make sure conversion of Position to point works correctly
  @Test
  public void testMidpointPositionToPoint() throws TurfException {
    Point pt1 = Point.fromLngLat(0, 0);
    Point pt2 = Point.fromLngLat(10, 0);
    Point mid = TurfMeasurement.midpoint(pt1, pt2);

    assertEquals(TurfMeasurement.distance(pt1, mid, UNIT_MILES),
      TurfMeasurement.distance(pt2, mid, UNIT_MILES), DELTA);
  }

  @Test
  public void turfAlong_returnsZeroWhenRouteIsPoint() throws Exception {
    List<Point> coords = new ArrayList<>();
    coords.add(Point.fromLngLat(1.0, 1.0));

    LineString lineString = LineString.fromLngLats(coords);
    Point point = TurfMeasurement.along(lineString, 0, TurfConstants.UNIT_METERS);
    assertEquals(1.0, point.latitude(), DELTA);
    assertEquals(1.0, point.longitude(), DELTA);
  }

  @Test
  public void testTurfAlong() throws IOException, TurfException {
    Feature feature = Feature.fromJson(loadJsonFixture(TURF_ALONG_DC_LINE));
    LineString line = (LineString) feature.geometry();

    Point pt1 = TurfMeasurement.along(line, 1, "miles");
    Point pt2 = TurfMeasurement.along(line, 1.2, "miles");
    Point pt3 = TurfMeasurement.along(line, 1.4, "miles");
    Point pt4 = TurfMeasurement.along(line, 1.6, "miles");
    Point pt5 = TurfMeasurement.along(line, 1.8, "miles");
    Point pt6 = TurfMeasurement.along(line, 2, "miles");
    Point pt7 = TurfMeasurement.along(line, 100, "miles");
    Point pt8 = TurfMeasurement.along(line, 0, "miles");
    FeatureCollection fc = FeatureCollection.fromFeatures(new Feature[] {
      Feature.fromGeometry(pt1),
      Feature.fromGeometry(pt2),
      Feature.fromGeometry(pt3),
      Feature.fromGeometry(pt4),
      Feature.fromGeometry(pt5),
      Feature.fromGeometry(pt6),
      Feature.fromGeometry(pt7),
      Feature.fromGeometry(pt8)
    });

    for (Feature f : fc.features()) {
      assertNotNull(f);
      assertEquals(f.type(), "Feature");
      assertEquals(f.geometry().type(), "Point");
    }

    assertEquals(fc.features().size(), 8);
    assertEquals(((Point) fc.features().get(7).geometry()).longitude(), pt8.longitude(), DELTA);
    assertEquals(((Point) fc.features().get(7).geometry()).latitude(), pt8.latitude(), DELTA);
  }

  /*
   * Turf bbox Test
   */

  @Test
  public void bboxFromPoint() throws IOException, TurfException {
    Feature feature = Feature.fromJson(loadJsonFixture(TURF_BBOX_POINT));
    double[] bbox = TurfMeasurement.bbox((Point) feature.geometry());

    assertEquals(bbox.length, 4);
    assertEquals(bbox[0], 102, DELTA);
    assertEquals(bbox[1], 0.5, DELTA);
    assertEquals(bbox[2], 102, DELTA);
    assertEquals(bbox[3], 0.5, DELTA);
  }

  @Test
  public void bboxFromLine() throws TurfException, IOException {
    LineString lineString = LineString.fromJson(loadJsonFixture(TURF_BBOX_LINESTRING));
    double[] bbox = TurfMeasurement.bbox(lineString);

    assertEquals(bbox.length, 4);
    assertEquals(bbox[0], 102, DELTA);
    assertEquals(bbox[1], -10, DELTA);
    assertEquals(bbox[2], 130, DELTA);
    assertEquals(bbox[3], 4, DELTA);
  }

  @Test
  public void bboxFromPolygon() throws TurfException, IOException {
    Feature feature = Feature.fromJson(loadJsonFixture(TURF_BBOX_POLYGON));
    double[] bbox = TurfMeasurement.bbox((Polygon) feature.geometry());

    assertEquals(bbox.length, 4);
    assertEquals(bbox[0], 100, DELTA);
    assertEquals(bbox[1], 0, DELTA);
    assertEquals(bbox[2], 101, DELTA);
    assertEquals(bbox[3], 1, DELTA);
  }

  @Test
  public void bboxFromMultiLineString() throws IOException, TurfException {
    MultiLineString multiLineString =
      MultiLineString.fromJson(loadJsonFixture(TURF_BBOX_MULTILINESTRING));
    double[] bbox = TurfMeasurement.bbox(multiLineString);

    assertEquals(bbox.length, 4);
    assertEquals(bbox[0], 100, DELTA);
    assertEquals(bbox[1], 0, DELTA);
    assertEquals(bbox[2], 103, DELTA);
    assertEquals(bbox[3], 3, DELTA);
  }

  @Test
  public void bboxFromMultiPolygon() throws IOException, TurfException {
    MultiPolygon multiPolygon = MultiPolygon.fromJson(loadJsonFixture(TURF_BBOX_MULTIPOLYGON));
    double[] bbox = TurfMeasurement.bbox(multiPolygon);

    assertEquals(bbox.length, 4);
    assertEquals(bbox[0], 100, DELTA);
    assertEquals(bbox[1], 0, DELTA);
    assertEquals(bbox[2], 103, DELTA);
    assertEquals(bbox[3], 3, DELTA);
  }
}
