package com.mapbox.turf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TurfMiscTest extends TestUtils {

  private static final String LINE_SLICE_ONE = "turf-line-slice/line1.geojson";
  private static final String LINE_SLICE_ROUTE_ONE = "turf-line-slice/route1.geojson";
  private static final String LINE_SLICE_ROUTE_TWO = "turf-line-slice/route2.geojson";
  private static final String LINE_SLICE_VERTICAL = "turf-line-slice/vertical.geojson";

  private static final String LINE_SLICE_ALONG_LINE_ONE = "turf-line-slice-along/line1.geojson";
  private static final String LINE_SLICE_ALONG_ROUTE_ONE = "turf-line-slice-along/route1.geojson";
  private static final String LINE_SLICE_ALONG_ROUTE_TWO = "turf-line-slice-along/route2.geojson";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void lineIntersect_intersectingEdge() {
    List<Point> line1Coords = new ArrayList<>();
    line1Coords.add(Point.fromLngLat(1.0, 2.0));
    line1Coords.add(Point.fromLngLat(2.0, 3.0));
    LineString line1 = LineString.fromLngLats(line1Coords);

    List<Point> line2Coords = new ArrayList<>();
    line2Coords.add(Point.fromLngLat(2.0, 3.0));
    line2Coords.add(Point.fromLngLat(4.0, 2.0));
    LineString line2 = LineString.fromLngLats(line2Coords);

    List<Point> result1 = TurfMisc.lineIntersect(line1, line2);
    assertEquals(1, result1.size());
    assertEquals(Point.fromLngLat(2.0, 3.0), result1.get(0));

    List<Point> line3Coords = new ArrayList<>();
    line3Coords.add(Point.fromLngLat(0.0, 3.0));
    line3Coords.add(Point.fromLngLat(1.0, 2.0));
    LineString line3 = LineString.fromLngLats(line3Coords);

    List<Point> result2 = TurfMisc.lineIntersect(line1, line3);
    assertEquals(1, result2.size());
    assertEquals(Point.fromLngLat(1.0, 2.0), result2.get(0));
  }

  @Test
  public void lineIntersect_intersecting() {
    List<Point> line1Coords = new ArrayList<>();
    line1Coords.add(Point.fromLngLat(2.0, 1.0));
    line1Coords.add(Point.fromLngLat(2.0, 5.0));
    line1Coords.add(Point.fromLngLat(2.0, 9.0));
    LineString line1 = LineString.fromLngLats(line1Coords);

    List<Point> line2Coords = new ArrayList<>();
    line2Coords.add(Point.fromLngLat(4.0, 1.0));
    line2Coords.add(Point.fromLngLat(0.0, 5.0));
    line2Coords.add(Point.fromLngLat(2.0, 9.0));
    LineString line2 = LineString.fromLngLats(line2Coords);

    List<Point> expected = new ArrayList<>();
    expected.add(Point.fromLngLat(2.0, 3.0));
    expected.add(Point.fromLngLat(2.0, 9.0));

    List<Point> result = TurfMisc.lineIntersect(line1, line2);
    for(int i = 0; i < result.size(); i++) {
      assertEquals(expected.get(i), result.get(i));
    }
  }

  @Test
  public void lineIntersect_nonintersecting() {
    List<Point> line1Coords = new ArrayList<>();
    line1Coords.add(Point.fromLngLat(2.0, 1.0));
    line1Coords.add(Point.fromLngLat(2.0, 5.0));
    line1Coords.add(Point.fromLngLat(2.0, 9.0));
    LineString line1 = LineString.fromLngLats(line1Coords);

    List<Point> line2Coords = new ArrayList<>();
    line2Coords.add(Point.fromLngLat(1.0, 1.0));
    line2Coords.add(Point.fromLngLat(1.0, 5.0));
    line2Coords.add(Point.fromLngLat(1.0, 9.0));
    LineString line2 = LineString.fromLngLats(line2Coords);

    List<Point> expected = new ArrayList<>();
    expected.add(Point.fromLngLat(2.0, 3.0));
    expected.add(Point.fromLngLat(2.0, 9.0));

    List<Point> result = TurfMisc.lineIntersect(line1, line2);
    assertEquals(0, result.size());
  }

  @Test
  public void lineSlice_throwsStartStopPointException() throws Exception {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Turf lineSlice requires a LineString made up of at least 2 "
      + "coordinates."));

    List<Point> coords = new ArrayList<>();
    coords.add(Point.fromLngLat(1.0, 1.0));
    Point point = Point.fromLngLat(1.0, 1.0);
    Point point2 = Point.fromLngLat(2.0, 2.0);
    LineString lineString = LineString.fromLngLats(coords);
    TurfMisc.lineSlice(point, point2, lineString);
  }

  @Test
  public void lineSlice_throwLineMustContainTwoOrMorePoints() throws Exception {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Start and stop points in Turf lineSlice cannot equal each "
      + "other."));

    List<Point> coords = new ArrayList<>();
    coords.add(Point.fromLngLat(1.0, 1.0));
    coords.add(Point.fromLngLat(2.0, 2.0));
    Point point = Point.fromLngLat(1.0, 1.0);
    LineString lineString = LineString.fromLngLats(coords);
    TurfMisc.lineSlice(point, point, lineString);
  }

  @Test
  public void lineSlice_returnsEmptyLineStringRatherThanNull() throws Exception {
    List<Point> coords = new ArrayList<>();
    coords.add(Point.fromLngLat(1.0, 1.0));
    coords.add(Point.fromLngLat(2.0, 2.0));
    LineString lineString = LineString.fromLngLats(coords);
    assertNotNull(TurfMisc.lineSlice(coords.get(0), coords.get(1), lineString));
  }

  @Test
  public void testTurfLineSliceLine1() throws IOException, TurfException {
    Point start = Point.fromLngLat(-97.79617309570312, 22.254624939561698);
    Point stop = Point.fromLngLat(-97.72750854492188, 22.057641623615734);

    Feature line1 = Feature.fromJson(loadJsonFixture(LINE_SLICE_ONE));

    LineString sliced = TurfMisc.lineSlice(start, stop, line1);
    assertNotNull(sliced);
  }

  @Test
  public void testTurfLineSliceRawGeometry() throws IOException, TurfException {
    Point start = Point.fromLngLat(-97.79617309570312, 22.254624939561698);
    Point stop = Point.fromLngLat(-97.72750854492188, 22.057641623615734);

    Feature line1 = Feature.fromJson(loadJsonFixture(LINE_SLICE_ONE));

    LineString sliced = TurfMisc.lineSlice(start, stop, (LineString) line1.geometry());
    assertNotNull(sliced);
  }

  @Test
  public void testTurfLineSliceLine2() throws TurfException {
    Point start = Point.fromLngLat(0, 0.1);
    Point stop = Point.fromLngLat(.9, .8);

    ArrayList<Point> coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(0, 0));
    coordinates.add(Point.fromLngLat(1, 1));
    LineString line2 = LineString.fromLngLats(coordinates);

    LineString sliced = TurfMisc.lineSlice(start, stop, line2);
    assertNotNull(sliced);
  }

  @Test
  public void testTurfLineSliceRoute1() throws IOException, TurfException {
    Point start = Point.fromLngLat(-79.0850830078125, 37.60117623656667);
    Point stop = Point.fromLngLat(-77.7667236328125, 38.65119833229951);

    Feature route1 = Feature.fromJson(loadJsonFixture(LINE_SLICE_ROUTE_ONE));

    LineString sliced = TurfMisc.lineSlice(start, stop, route1);
    assertNotNull(sliced);
  }

  @Test
  public void testTurfLineSliceRoute2() throws IOException, TurfException {
    Point start = Point.fromLngLat(-112.60660171508789, 45.96021963947196);
    Point stop = Point.fromLngLat(-111.97265625, 48.84302835299516);

    Feature route2 = Feature.fromJson(loadJsonFixture(LINE_SLICE_ROUTE_TWO));

    LineString sliced = TurfMisc.lineSlice(start, stop, route2);
    assertNotNull(sliced);
  }

  @Test
  public void testTurfLineSliceVertical() throws IOException, TurfException {
    Point start = Point.fromLngLat(-121.25447809696198, 38.70582415504791);
    Point stop = Point.fromLngLat(-121.25447809696198, 38.70634324369764);

    Feature vertical = Feature.fromJson(loadJsonFixture(LINE_SLICE_VERTICAL));

    LineString sliced = TurfMisc.lineSlice(start, stop, vertical);
    assertNotNull(sliced);

    // No duplicated coords
    assertEquals(2, sliced.coordinates().size());

    // Vertical slice does not collapse to 1st coord
    assertNotEquals(sliced.coordinates().get(0), sliced.coordinates().get(1));
  }

  /*
   * Point on line test
   */

  @Test
  public void pointOnLine_throwLineMustContainTwoOrMorePoints() throws Exception {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Turf nearestPointOnLine requires a List of Points made up of at least"
      + " 2 coordinates."));

    List<Point> line = new ArrayList<>();
    line.add(Point.fromLngLat(-122.45717525482178, 37.72003306385638));
    TurfMisc.nearestPointOnLine(line.get(0), line);
  }

  @Test
  public void testTurfPointOnLineFirstPoint() throws TurfException {
    List<Point> line = new ArrayList<>();
    line.add(Point.fromLngLat(-122.45717525482178, 37.72003306385638));
    line.add(Point.fromLngLat(-122.45717525482178, 37.718242366859215));

    Point pt = Point.fromLngLat(-122.45717525482178, 37.72003306385638);

    Feature snappedFeature = TurfMisc.nearestPointOnLine(pt, line);
    Point snapped = (Point) snappedFeature.geometry();
    // pt on start does not move
    assertEquals(pt, snapped);
  }

  @Test
  public void testTurfPointOnLinePointsBehindFirstPoint() throws TurfException {
    List<Point> line = new ArrayList<>();
    line.add(Point.fromLngLat(-122.45717525482178, 37.72003306385638));
    line.add(Point.fromLngLat(-122.45717525482178, 37.718242366859215));

    Point first = line.get(0);

    List<Point> pts = new ArrayList<>();
    pts.add(Point.fromLngLat(-122.45717525482178, 37.72009306385638));
    pts.add(Point.fromLngLat(-122.45717525482178, 37.82009306385638));
    pts.add(Point.fromLngLat(-122.45716525482177, 37.72009306385638));
    pts.add(Point.fromLngLat(-122.45516525482178, 37.72009306385638));

    for (Point pt : pts) {
      Feature snappedFeature = TurfMisc.nearestPointOnLine(pt, line);
      Point snapped = (Point) snappedFeature.geometry();
      // pt behind start moves to first vertex
      assertEquals(first, snapped);
    }
  }

  @Test
  public void testTurfPointOnLinePointsInFrontOfLastPoint() throws TurfException {
    List<Point> line = new ArrayList<>();
    line.add(Point.fromLngLat(-122.45616137981413, 37.72125936929241));
    line.add(Point.fromLngLat(-122.45717525482178, 37.72003306385638));
    line.add(Point.fromLngLat(-122.45717525482178, 37.718242366859215));

    Point last = line.get(2);

    List<Point> pts = new ArrayList<>();
    pts.add(Point.fromLngLat(-122.45696067810057, 37.7181405249708));
    pts.add(Point.fromLngLat(-122.4573630094528, 37.71813203814049));
    pts.add(Point.fromLngLat(-122.45730936527252, 37.71797927502795));
    pts.add(Point.fromLngLat(-122.45718061923981, 37.71704571582896));

    for (Point pt : pts) {
      Feature snappedFeature = TurfMisc.nearestPointOnLine(pt, line);
      Point snapped = (Point) snappedFeature.geometry();
      // pt behind start moves to last vertex
      assertEquals(last, snapped);
    }
  }

  @Test
  public void testTurfPointOnLinePointsOnJoints() throws TurfException {
    List<Point> line1 = new ArrayList<>();
    line1.add(Point.fromLngLat(-122.45616137981413, 37.72125936929241));
    line1.add(Point.fromLngLat(-122.45717525482178, 37.72003306385638));
    line1.add(Point.fromLngLat(-122.45717525482178, 37.718242366859215));

    List<Point> line2 = new ArrayList<>();
    line2.add(Point.fromLngLat(26.279296875, 31.728167146023935));
    line2.add(Point.fromLngLat(21.796875, 32.69486597787505));
    line2.add(Point.fromLngLat(18.80859375, 29.99300228455108));
    line2.add(Point.fromLngLat(12.919921874999998, 33.137551192346145));
    line2.add(Point.fromLngLat(10.1953125, 35.60371874069731));
    line2.add(Point.fromLngLat(4.921875, 36.527294814546245));
    line2.add(Point.fromLngLat(-1.669921875, 36.527294814546245));
    line2.add(Point.fromLngLat(-5.44921875, 34.74161249883172));
    line2.add(Point.fromLngLat(-8.7890625, 32.99023555965106));

    List<Point> line3 = new ArrayList<>();
    line3.add(Point.fromLngLat(-0.10919809341430663, 51.52204224896724));
    line3.add(Point.fromLngLat(-0.10923027992248535, 51.521942114455435));
    line3.add(Point.fromLngLat(-0.10916590690612793, 51.52186200668747));
    line3.add(Point.fromLngLat(-0.10904788970947266, 51.52177522311313));
    line3.add(Point.fromLngLat(-0.10886549949645996, 51.521601655468345));
    line3.add(Point.fromLngLat(-0.10874748229980469, 51.52138135712038));
    line3.add(Point.fromLngLat(-0.10855436325073242, 51.5206870765674));
    line3.add(Point.fromLngLat(-0.10843634605407713, 51.52027984939518));
    line3.add(Point.fromLngLat(-0.10839343070983887, 51.519952729849024));
    line3.add(Point.fromLngLat(-0.10817885398864746, 51.51957887606202));
    line3.add(Point.fromLngLat(-0.10814666748046874, 51.51928513164789));
    line3.add(Point.fromLngLat(-0.10789990425109863, 51.518624199789016));
    line3.add(Point.fromLngLat(-0.10759949684143065, 51.51778299991493));

    List<List<Point>> lines = new ArrayList<>();
    lines.add(line1);
    lines.add(line2);
    lines.add(line3);

    for (List<Point> line : lines) {
      List<Point> linePoint = new ArrayList<>();
      for (Point pt : line) {
        linePoint.add(
          Point.fromLngLat(pt.longitude(), pt.latitude()));
      }
      for (Point pt : line) {
        Feature snappedFeature = TurfMisc.nearestPointOnLine(pt, linePoint);
        Point snapped = (Point) snappedFeature.geometry();
        // pt on joint stayed in place
        assertEquals(pt, snapped);
      }
    }
  }

  @Test
  public void testTurfPointOnLinePointsOnTopOfLine() throws TurfException {
    List<Point> line = new ArrayList<>();
    line.add(Point.fromLngLat(-0.10919809341430663, 51.52204224896724));
    line.add(Point.fromLngLat(-0.10923027992248535, 51.521942114455435));
    line.add(Point.fromLngLat(-0.10916590690612793, 51.52186200668747));
    line.add(Point.fromLngLat(-0.10904788970947266, 51.52177522311313));
    line.add(Point.fromLngLat(-0.10886549949645996, 51.521601655468345));
    line.add(Point.fromLngLat(-0.10874748229980469, 51.52138135712038));
    line.add(Point.fromLngLat(-0.10855436325073242, 51.5206870765674));
    line.add(Point.fromLngLat(-0.10843634605407713, 51.52027984939518));
    line.add(Point.fromLngLat(-0.10839343070983887, 51.519952729849024));
    line.add(Point.fromLngLat(-0.10817885398864746, 51.51957887606202));
    line.add(Point.fromLngLat(-0.10814666748046874, 51.51928513164789));
    line.add(Point.fromLngLat(-0.10789990425109863, 51.518624199789016));
    line.add(Point.fromLngLat(-0.10759949684143065, 51.51778299991493));

    double dist = TurfMeasurement.length(LineString.fromLngLats(line), TurfConstants.UNIT_MILES);
    double increment = dist / 10;

    for (int i = 0; i < 10; i++) {
      Point pt = TurfMeasurement.along(
        LineString.fromLngLats(line), increment * i, TurfConstants.UNIT_MILES);
      Feature snappedFeature = TurfMisc.nearestPointOnLine(pt, line);
      Point snapped = (Point) snappedFeature.geometry();

      double shift = TurfMeasurement.distance(pt, snapped, TurfConstants.UNIT_MILES);

      // pt did not shift far
      assertTrue(shift < 0.000001);
    }
  }

  @Test
  public void testTurfPointOnLinePointAlongLine() throws TurfException {
    List<Point> line = new ArrayList<>();
    line.add(Point.fromLngLat(-122.45717525482178, 37.7200330638563));
    line.add(Point.fromLngLat(-122.45717525482178, 37.718242366859215));

    Point pt = TurfMeasurement.along(
      LineString.fromLngLats(line), 0.019, TurfConstants.UNIT_MILES);
    Feature snappedFeature = TurfMisc.nearestPointOnLine(pt, line);
    Point snapped = (Point) snappedFeature.geometry();
    double shift = TurfMeasurement.distance(pt, snapped, TurfConstants.UNIT_MILES);

    // pt did not shift far
    assertTrue(shift < 0.00001);
  }

  @Test
  public void testTurfPointOnLinePointsOnSidesOfLines() throws TurfException {
    List<Point> line = new ArrayList<>();
    line.add(Point.fromLngLat(-122.45616137981413, 37.72125936929241));
    line.add(Point.fromLngLat(-122.45717525482178, 37.718242366859215));
    Point first = line.get(0);
    Point last = line.get(1);

    List<Point> pts = new ArrayList<>();
    pts.add(Point.fromLngLat(-122.45702505111694, 37.71881098149625));
    pts.add(Point.fromLngLat(-122.45733618736267, 37.719235317933844));
    pts.add(Point.fromLngLat(-122.45686411857605, 37.72027068864082));
    pts.add(Point.fromLngLat(-122.45652079582213, 37.72063561093274));

    for (Point pt : pts) {
      Feature snappedFeature = TurfMisc.nearestPointOnLine(pt, line);
      Point snapped = (Point) snappedFeature.geometry();
      // pt did not snap to first vertex
      assertNotEquals(snapped, first);
      // pt did not snap to last vertex
      assertNotEquals(snapped, last);
    }
  }

  @Test
  public void testTurfPointOnLinePointsOnSidesOfLinesCustomUnit() throws TurfException {
    List<Point> line = new ArrayList<>();
    line.add(Point.fromLngLat(-122.45616137981413, 37.72125936929241));
    line.add(Point.fromLngLat(-122.45717525482178, 37.718242366859215));
    Point first = line.get(0);
    Point last = line.get(1);

    List<Point> pts = new ArrayList<>();
    pts.add(Point.fromLngLat(-122.45702505111694, 37.71881098149625));
    pts.add(Point.fromLngLat(-122.45733618736267, 37.719235317933844));
    pts.add(Point.fromLngLat(-122.45686411857605, 37.72027068864082));
    pts.add(Point.fromLngLat(-122.45652079582213, 37.72063561093274));

    for (Point pt : pts) {
      Feature snappedFeature = TurfMisc.nearestPointOnLine(pt, line, TurfConstants.UNIT_MILES);
      Point snapped = (Point) snappedFeature.geometry();
      // pt did not snap to first vertex
      assertNotEquals(snapped, first);
      // pt did not snap to last vertex
      assertNotEquals(snapped, last);
    }
  }

  @Test
  public void testLineSliceAlongLine1() throws IOException, TurfException {
    Feature line1 = Feature.fromJson(loadJsonFixture(LINE_SLICE_ALONG_LINE_ONE));
    LineString lineStringLine1 = (LineString) line1.geometry();

    double start = 500;
    double stop = 750;

    Point start_point = TurfMeasurement.along(lineStringLine1, start, TurfConstants.UNIT_MILES);
    Point end_point = TurfMeasurement.along(lineStringLine1, stop, TurfConstants.UNIT_MILES);
    LineString sliced = TurfMisc.lineSliceAlong(line1, start, stop, TurfConstants.UNIT_MILES);

    assertEquals(sliced.coordinates().get(0).coordinates(), start_point.coordinates());
    assertEquals(sliced.coordinates().get(0).coordinatesPrimitives(), start_point.coordinatesPrimitives());
    assertEquals(sliced.coordinates().get(sliced.coordinates().size() - 1).coordinates(),
      end_point.coordinates());
    assertEquals(sliced.coordinates().get(sliced.coordinates().size() - 1).coordinatesPrimitives(),
            end_point.coordinatesPrimitives());
  }

  @Test
   public void testLineSliceAlongOvershootLine1() throws IOException, TurfException {
    Feature line1 = Feature.fromJson(loadJsonFixture(LINE_SLICE_ALONG_LINE_ONE));
    LineString lineStringLine1 = (LineString) line1.geometry();

    double start = 500;
    double stop = 1500;

    Point start_point = TurfMeasurement.along(lineStringLine1, start, TurfConstants.UNIT_MILES);
    Point end_point = TurfMeasurement.along(lineStringLine1, stop, TurfConstants.UNIT_MILES);
    LineString sliced = TurfMisc.lineSliceAlong(line1, start, stop, TurfConstants.UNIT_MILES);

    assertEquals(sliced.coordinates().get(0).coordinates(), start_point.coordinates());
    assertEquals(sliced.coordinates().get(0).coordinatesPrimitives(), start_point.coordinatesPrimitives());
    assertEquals(sliced.coordinates().get(sliced.coordinates().size() - 1).coordinates(),
      end_point.coordinates());
    assertEquals(sliced.coordinates().get(sliced.coordinates().size() - 1).coordinatesPrimitives(),
            end_point.coordinatesPrimitives());
  }

  @Test
  public void testLineSliceAlongRoute1() throws IOException, TurfException {
    Feature route1 = Feature.fromJson(loadJsonFixture(LINE_SLICE_ALONG_ROUTE_ONE));
    LineString lineStringRoute1 = (LineString)route1.geometry();

    double start = 500;
    double stop = 750;

    Point start_point = TurfMeasurement.along(lineStringRoute1, start, TurfConstants.UNIT_MILES);
    Point end_point = TurfMeasurement.along(lineStringRoute1, stop, TurfConstants.UNIT_MILES);

    LineString sliced = TurfMisc.lineSliceAlong(route1, start, stop, TurfConstants.UNIT_MILES);

    assertEquals(sliced.coordinates().get(0).coordinates(), start_point.coordinates());
    assertEquals(sliced.coordinates().get(0).coordinatesPrimitives(), start_point.coordinatesPrimitives());
    assertEquals(sliced.coordinates().get(sliced.coordinates().size() - 1).coordinates(),
      end_point.coordinates());
    assertEquals(sliced.coordinates().get(sliced.coordinates().size() - 1).coordinatesPrimitives(),
            end_point.coordinatesPrimitives());
  }

  @Test
  public void testLineSliceAlongRoute2() throws IOException, TurfException {

    Feature route2 = Feature.fromJson(loadJsonFixture(LINE_SLICE_ALONG_ROUTE_TWO));
    LineString lineStringRoute2 = (LineString)route2.geometry();
    double start = 25;
    double stop = 50;

    Point start_point = TurfMeasurement.along(lineStringRoute2, start, TurfConstants.UNIT_MILES);
    Point end_point = TurfMeasurement.along(lineStringRoute2, stop, TurfConstants.UNIT_MILES);
    LineString sliced = TurfMisc.lineSliceAlong(route2, start, stop, TurfConstants.UNIT_MILES);

    assertEquals(sliced.coordinates().get(0).coordinates(), start_point.coordinates());
    assertEquals(sliced.coordinates().get(0).coordinatesPrimitives(), start_point.coordinatesPrimitives());
    assertEquals(sliced.coordinates().get(sliced.coordinates().size() - 1).coordinates(),
      end_point.coordinates());
    assertEquals(sliced.coordinates().get(sliced.coordinates().size() - 1).coordinatesPrimitives(),
      end_point.coordinatesPrimitives());
  }

  @Test
  public void testLineAlongStartLongerThanLength() throws Exception {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Start position is beyond line"));

    Feature line1 = Feature.fromJson(loadJsonFixture(LINE_SLICE_ALONG_LINE_ONE));

    double start = 500000;
    double stop = 800000;
    TurfMisc.lineSliceAlong(line1, start, stop, TurfConstants.UNIT_MILES);
  }

  @Test
  public void testLineAlongStopLongerThanLength() throws IOException, TurfException {
    Feature line1 = Feature.fromJson(loadJsonFixture(LINE_SLICE_ALONG_LINE_ONE));
    LineString lineStringLine1 = (LineString) line1.geometry();

    double start = 500;
    double stop = 800000;
    Point start_point = TurfMeasurement.along(lineStringLine1, start, TurfConstants.UNIT_MILES);
    List<Point> lineCoordinates = lineStringLine1.coordinates();
    LineString sliced = TurfMisc.lineSliceAlong(line1, start, stop, TurfConstants.UNIT_MILES);
    assertEquals(sliced.coordinates().get(0).coordinates(), start_point.coordinates());
    assertEquals(sliced.coordinates().get(0).coordinatesPrimitives(), start_point.coordinatesPrimitives());

    assertEquals(sliced.coordinates().get(sliced.coordinates().size() - 1).coordinates(),
      lineCoordinates.get(lineCoordinates.size() - 1).coordinates());
    assertEquals(sliced.coordinates().get(sliced.coordinates().size() - 1).coordinatesPrimitives(),
            lineCoordinates.get(lineCoordinates.size() - 1).coordinatesPrimitives());
  }

  @Test
  public void testShortLine() throws IOException, TurfException {

    // Distance between points is about 186 miles
    LineString lineStringLine1 = LineString.fromLngLats(Arrays.asList(
      Point.fromLngLat(113.99414062499999, 22.350075806124867),
      Point.fromLngLat(116.76269531249999, 23.241346102386135)));

    double start = 50;
    double stop =  100;

    Point start_point = TurfMeasurement.along(lineStringLine1, start, TurfConstants.UNIT_MILES);
    Point end_point = TurfMeasurement.along(lineStringLine1, stop, TurfConstants.UNIT_MILES);
    LineString sliced = TurfMisc.lineSliceAlong(lineStringLine1, start, stop, TurfConstants.UNIT_MILES);

    assertEquals(sliced.coordinates().get(0).coordinates(), start_point.coordinates());
    assertEquals(sliced.coordinates().get(0).coordinatesPrimitives(), start_point.coordinatesPrimitives());
    assertEquals(sliced.coordinates().get(sliced.coordinates().size() - 1).coordinates(),
      end_point.coordinates());
    assertEquals(sliced.coordinates().get(sliced.coordinates().size() - 1).coordinatesPrimitives(),
            end_point.coordinatesPrimitives());
  }
}