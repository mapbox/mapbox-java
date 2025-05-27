package com.mapbox.turf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.GeometryCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TurfConversionTest extends TestUtils {

  private static final String TURF_EXPLODE_MULTI_POINT = "turf-explode/multipoint.geojson";
  private static final String TURF_EXPLODE_LINESTRING = "turf-explode/linestring.geojson";
  private static final String TURF_EXPLODE_MULTILINESTRING = "turf-explode/multilinestring.geojson";
  private static final String TURF_EXPLODE_MULTIPOLYGON = "turf-explode/multipolygon.geojson";
  private static final String TURF_EXPLODE_GEOMETRY_COLLECTION = "turf-explode/geometrycollection.geojson";
  private static final String TURF_COMBINE_FEATURE_COLLECTION_TO_COMBINE = "turf-combine/feature_collection_to_combine.geojson";

  private static final String TURF_POLYGON_TO_LINE_PATH_IN = "turf-polygon-to-line/in/";
  private static final String TURF_POLYGON_TO_LINE_PATH_OUT = "turf-polygon-to-line/expected/";

  private static final String TURF_POLYGON_TO_LINE_FILENAME_POLYGON= "polygon.geojson";
  private static final String TURF_POLYGON_TO_LINE_FILENAME_GEOMETRY_POLYGON= "geometry-polygon.geojson";
  private static final String TURF_POLYGON_TO_LINE_FILENAME_POLYGON_WITH_HOLE = "polygon-with-hole.geojson";

  private static final String TURF_POLYGON_TO_LINE_FILENAME_MULTIPOLYGON = "multi-polygon.geojson";
  private static final String TURF_POLYGON_TO_LINE_FILENAME_MULTIPOLYGON_OUTER_DOUGHNUT = "multi-polygon-outer-doughnut.geojson";
  private static final String TURF_POLYGON_TO_LINE_FILENAME_MULTIPOLYGON_WITH_HOLES = "multi-polygon-with-holes.geojson";


  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void degreesToRadians() {
    assertEquals(1, TurfConversion.degreesToRadians(57.295), 0.0001);
    assertEquals(3.1416, TurfConversion.degreesToRadians(180), 0.0001);
    assertEquals(2.1817 , TurfConversion.degreesToRadians(125), 0.0001);
    assertEquals(0, TurfConversion.degreesToRadians(360), 0.0001);
    assertEquals(0, TurfConversion.degreesToRadians(0), 0.0001);
  }

  @Test
  public void radiansToDegrees() {
    assertEquals(57.295, TurfConversion.radiansToDegrees(1), 0.01);
    assertEquals(180, TurfConversion.radiansToDegrees(3.1416), 0.01);
    assertEquals(125, TurfConversion.radiansToDegrees(2.1817), 0.01);
    assertEquals(0, TurfConversion.radiansToDegrees(0), 0.01);
  }

  @Test
  public void radiansToDistance() {
    assertEquals(
      1, TurfConversion.radiansToLength(1, TurfConstants.UNIT_RADIANS), DELTA);
    assertEquals(
      6373, TurfConversion.radiansToLength(1, TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(
      3960, TurfConversion.radiansToLength(1, TurfConstants.UNIT_MILES), DELTA);
  }

  @Test
  public void distanceToRadians() {
    assertEquals(
      1, TurfConversion.lengthToRadians(1, TurfConstants.UNIT_RADIANS), DELTA);
    assertEquals(
      1, TurfConversion.lengthToRadians(6373, TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(
      1, TurfConversion.lengthToRadians(3960, TurfConstants.UNIT_MILES), DELTA);
  }

  @Test
  public void distanceToDegrees() {
    assertEquals(
      57.29577951308232, TurfConversion.lengthToDegrees(1, TurfConstants.UNIT_RADIANS), DELTA);
    assertEquals(
      0.8990393772647469, TurfConversion.lengthToDegrees(100,
        TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(
      0.14468631190172304, TurfConversion.lengthToDegrees(10, TurfConstants.UNIT_MILES), DELTA);
  }

  @Test
  public void convertDistance() throws TurfException {
    assertEquals(1,
      TurfConversion.convertLength(1000, TurfConstants.UNIT_METERS), DELTA);
    assertEquals(0.6213714106386318,
      TurfConversion.convertLength(1, TurfConstants.UNIT_KILOMETERS,
        TurfConstants.UNIT_MILES), DELTA);
    assertEquals(1.6093434343434343,
      TurfConversion.convertLength(1, TurfConstants.UNIT_MILES,
        TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(1.851999843075488,
      TurfConversion.convertLength(1, TurfConstants.UNIT_NAUTICAL_MILES), DELTA);
    assertEquals(100,
      TurfConversion.convertLength(1, TurfConstants.UNIT_METERS,
        TurfConstants.UNIT_CENTIMETERS), DELTA);
  }


  @Test
  public void combinePointsToMultiPoint() throws Exception {
    FeatureCollection pointFeatureCollection =
      FeatureCollection.fromFeatures(
        Arrays.asList(
          Feature.fromGeometry(Point.fromLngLat(-2.46, 27.6835)),
          Feature.fromGeometry(Point.fromLngLat(41.83, 7.3624))
        ));

    FeatureCollection featureCollectionWithNewMultiPointObject = TurfConversion.combine(pointFeatureCollection);
    assertNotNull(featureCollectionWithNewMultiPointObject);

    MultiPoint multiPoint = (MultiPoint) featureCollectionWithNewMultiPointObject.features().get(0).geometry();
    assertNotNull(multiPoint);

    assertEquals(-2.46, multiPoint.coordinates().get(0).longitude(), DELTA);
    assertEquals(27.6835, multiPoint.coordinates().get(0).latitude(), DELTA);
    assertEquals(41.83, multiPoint.coordinates().get(1).longitude(), DELTA);
    assertEquals(7.3624, multiPoint.coordinates().get(1).latitude(), DELTA);
  }

  @Test
  public void combinePointAndMultiPointToMultiPoint() throws Exception {
    FeatureCollection pointAndMultiPointFeatureCollection =
        FeatureCollection.fromFeatures(
            Arrays.asList(
                Feature.fromGeometry(Point.fromLngLat(-2.46, 27.6835)),
                Feature.fromGeometry(MultiPoint.fromLngLats(
                    Arrays.asList(Point.fromLngLat(41.83, 7.3624),
                        Point.fromLngLat(100, 101)))
                )));

    FeatureCollection combinedFeatureCollection =
        TurfConversion.combine(pointAndMultiPointFeatureCollection);

    assertNotNull(combinedFeatureCollection);

    MultiPoint multiPoint = (MultiPoint) combinedFeatureCollection.features().get(0).geometry();
    assertNotNull(multiPoint);

    assertEquals(-2.46, multiPoint.coordinates().get(0).longitude(), DELTA);
    assertEquals(27.6835, multiPoint.coordinates().get(0).latitude(), DELTA);
    assertEquals(41.83, multiPoint.coordinates().get(1).longitude(), DELTA);
    assertEquals(7.3624, multiPoint.coordinates().get(1).latitude(), DELTA);
    assertEquals(100, multiPoint.coordinates().get(2).longitude(), DELTA);
    assertEquals(101, multiPoint.coordinates().get(2).latitude(), DELTA);
  }

  @Test
  public void combineTwoLineStringsToMultiLineString() throws Exception {
    FeatureCollection lineStringFeatureCollection =
        FeatureCollection.fromFeatures(
            Arrays.asList(
                Feature.fromGeometry(LineString.fromLngLats(
                    Arrays.asList(Point.fromLngLat(-11.25, 55.7765),
                        Point.fromLngLat(41.1328, 22.91792)))),
                Feature.fromGeometry(LineString.fromLngLats(
                    Arrays.asList(Point.fromLngLat(3.8671, 19.3111),
                        Point.fromLngLat(20.742, -20.3034))))
            ));

    FeatureCollection featureCollectionWithNewMultiLineStringObject = TurfConversion.combine(lineStringFeatureCollection);
    assertNotNull(featureCollectionWithNewMultiLineStringObject);

    MultiLineString multiLineString = (MultiLineString) featureCollectionWithNewMultiLineStringObject.features().get(0).geometry();
    assertNotNull(multiLineString);

    // Checking the first LineString in the MultiLineString
    assertEquals(-11.25, multiLineString.coordinates().get(0).get(0).longitude(), DELTA);
    assertEquals(55.7765, multiLineString.coordinates().get(0).get(0).latitude(), DELTA);

    // Checking the second LineString in the MultiLineString
    assertEquals(41.1328, multiLineString.coordinates().get(0).get(1).longitude(), DELTA);
    assertEquals(22.91792, multiLineString.coordinates().get(0).get(1).latitude(), DELTA);
  }

  @Test
  public void combineLineStringAndMultiLineStringToMultiLineString() throws Exception {
    FeatureCollection lineStringFeatureCollection =
        FeatureCollection.fromFeatures(
            Arrays.asList(
                Feature.fromGeometry(LineString.fromLngLats(Arrays.asList(
                    Point.fromLngLat(-11.25, 55.7765),
                    Point.fromLngLat(41.1328, 22.91792)))),
                Feature.fromGeometry(
                    MultiLineString.fromLineStrings(Arrays.asList(
                        LineString.fromLngLats(Arrays.asList(
                            Point.fromLngLat(102, -10),
                            Point.fromLngLat(130.0, 4.0)
                        )),
                        LineString.fromLngLats(Arrays.asList(
                            Point.fromLngLat(40.0, -20.0),
                            Point.fromLngLat(150.0, 18.0)
                        ))
                    )))
            ));

    FeatureCollection featureCollectionWithNewMultiLineStringObject =
        TurfConversion.combine(lineStringFeatureCollection);
    assertNotNull(featureCollectionWithNewMultiLineStringObject);

    MultiLineString multiLineString = (MultiLineString) featureCollectionWithNewMultiLineStringObject.
        features().get(0).geometry();
    assertNotNull(multiLineString);

    // Checking the first LineString in the MultiLineString
    assertEquals(-11.25, multiLineString.coordinates().get(0).get(0).longitude(), DELTA);
    assertEquals(55.7765, multiLineString.coordinates().get(0).get(0).latitude(), DELTA);

    assertEquals(41.1328, multiLineString.coordinates().get(0).get(1).longitude(), DELTA);
    assertEquals(22.91792, multiLineString.coordinates().get(0).get(1).latitude(), DELTA);

    // Checking the second LineString in the MultiLineString
    assertEquals(102, multiLineString.coordinates().get(1).get(0).longitude(), DELTA);
    assertEquals(-10, multiLineString.coordinates().get(1).get(0).latitude(), DELTA);

    assertEquals(130.0, multiLineString.coordinates().get(1).get(1).longitude(), DELTA);
    assertEquals(4.0, multiLineString.coordinates().get(1).get(1).latitude(), DELTA);

    // Checking the third LineString in the MultiLineString
    assertEquals(40.0, multiLineString.coordinates().get(2).get(0).longitude(), DELTA);
    assertEquals(-20.0, multiLineString.coordinates().get(2).get(0).latitude(), DELTA);

    assertEquals(150.0, multiLineString.coordinates().get(2).get(1).longitude(), DELTA);
    assertEquals(18.0, multiLineString.coordinates().get(2).get(1).latitude(), DELTA);
  }

  @Test
  public void combinePolygonToMultiPolygon() throws Exception {
    FeatureCollection polygonFeatureCollection =
        FeatureCollection.fromFeatures(
            Arrays.asList(
                Feature.fromGeometry(Polygon.fromLngLats(Arrays.asList(
                    Arrays.asList(
                        Point.fromLngLat(61.938950426660604, 5.9765625),
                        Point.fromLngLat(52.696361078274485, 33.046875),
                        Point.fromLngLat(69.90011762668541, 28.828124999999996),
                        Point.fromLngLat(61.938950426660604, 5.9765625))))),
                Feature.fromGeometry(Polygon.fromLngLats(Arrays.asList(
                    Arrays.asList(
                        Point.fromLngLat(11.42578125, 16.636191878397664),
                        Point.fromLngLat(7.91015625, -9.102096738726443),
                        Point.fromLngLat(31.113281249999996, 17.644022027872726),
                        Point.fromLngLat(11.42578125, 16.636191878397664)
                    ))))
            ));

    FeatureCollection featureCollectionWithNewMultiPolygonObject = TurfConversion.combine(polygonFeatureCollection);
    assertNotNull(featureCollectionWithNewMultiPolygonObject);

    MultiPolygon multiPolygon = (MultiPolygon) featureCollectionWithNewMultiPolygonObject.features().get(0).geometry();
    assertNotNull(multiPolygon);

    // Checking the first Polygon in the MultiPolygon

    // Checking the first Point
    assertEquals(61.938950426660604, multiPolygon.coordinates().get(0).get(0).get(0).longitude(), DELTA);
    assertEquals(5.9765625, multiPolygon.coordinates().get(0).get(0).get(0).latitude(), DELTA);

    // Checking the second Point
    assertEquals(52.696361078274485, multiPolygon.coordinates().get(0).get(0).get(1).longitude(), DELTA);
    assertEquals(33.046875, multiPolygon.coordinates().get(0).get(0).get(1).latitude(), DELTA);

    // Checking the second Polygon in the MultiPolygon

    // Checking the first Point
    assertEquals(11.42578125, multiPolygon.coordinates().get(1).get(0).get(0).longitude(), DELTA);
    assertEquals(16.636191878397664, multiPolygon.coordinates().get(1).get(0).get(0).latitude(), DELTA);

    // Checking the second Point
    assertEquals(7.91015625, multiPolygon.coordinates().get(1).get(0).get(1).longitude(), DELTA);
    assertEquals(-9.102096738726443, multiPolygon.coordinates().get(1).get(0).get(1).latitude(), DELTA);
  }

  @Test
  public void combinePolygonAndMultiPolygonToMultiPolygon() throws Exception {
    FeatureCollection polygonFeatureCollection =
        FeatureCollection.fromFeatures(
            Arrays.asList(
              Feature.fromGeometry(
                Polygon.fromLngLats(Arrays.asList(Arrays.asList(
                  Point.fromLngLat(61.938950426660604, 5.9765625),
                  Point.fromLngLat(52.696361078274485, 33.046875),
                  Point.fromLngLat(69.90011762668541, 28.828124999999996),
                  Point.fromLngLat(61.938950426660604, 5.9765625))))),
                Feature.fromGeometry(MultiPolygon.fromPolygons(Arrays.asList(
                    Polygon.fromLngLats(Arrays.asList(Arrays.asList(
                        Point.fromLngLat(11.42578125, 16.636191878397664),
                        Point.fromLngLat(7.91015625, -9.102096738726443),
                        Point.fromLngLat(31.113281249999996, 17.644022027872726),
                        Point.fromLngLat(11.42578125, 16.636191878397664)
                    ))),
                    Polygon.fromLngLats(Arrays.asList(Arrays.asList(
                        Point.fromLngLat(30.0, 0.0),
                        Point.fromLngLat(102.0, 0.0),
                        Point.fromLngLat(103.0, 1.0),
                        Point.fromLngLat(30.0, 0.0)
                    )))
                )))
            ));

    FeatureCollection combinedFeatureCollection = TurfConversion.combine(polygonFeatureCollection);
    assertNotNull(combinedFeatureCollection);

    MultiPolygon multiPolygon = (MultiPolygon) combinedFeatureCollection.features().get(0).geometry();
    assertNotNull(multiPolygon);

    // Checking the first Polygon in the MultiPolygon

    // Checking the first Point
    assertEquals(61.938950426660604, multiPolygon.coordinates().get(0).get(0).get(0).longitude(), DELTA);
    assertEquals(5.9765625, multiPolygon.coordinates().get(0).get(0).get(0).latitude(), DELTA);

    // Checking the second Point
    assertEquals(52.696361078274485, multiPolygon.coordinates().get(0).get(0).get(1).longitude(), DELTA);
    assertEquals(33.046875, multiPolygon.coordinates().get(0).get(0).get(1).latitude(), DELTA);

    // Checking the second Polygon in the MultiPolygon

    // Checking the first Point
    assertEquals(11.42578125, multiPolygon.coordinates().get(1).get(0).get(0).longitude(), DELTA);
    assertEquals(16.636191878397664, multiPolygon.coordinates().get(1).get(0).get(0).latitude(), DELTA);

    // Checking the second Point
    assertEquals(7.91015625, multiPolygon.coordinates().get(1).get(0).get(1).longitude(), DELTA);
    assertEquals(-9.102096738726443, multiPolygon.coordinates().get(1).get(0).get(1).latitude(), DELTA);

    // Checking the third Polygon in the MultiPolygon

    // Checking the first Point
    assertEquals(30.0, multiPolygon.coordinates().get(2).get(0).get(0).longitude(), DELTA);
    assertEquals(0.0, multiPolygon.coordinates().get(2).get(0).get(0).latitude(), DELTA);

    // Checking the second Point
    assertEquals(102.0, multiPolygon.coordinates().get(2).get(0).get(1).longitude(), DELTA);
    assertEquals(0.0, multiPolygon.coordinates().get(2).get(0).get(1).latitude(), DELTA);
  }

  @Test
  public void combinePolygonAndMultiPolygonAndPointToMultiPolygon() throws Exception {
    FeatureCollection featureCollectionWithPointPolygonAndMultiPolygon =
        FeatureCollection.fromFeatures(
            Arrays.asList(
                Feature.fromGeometry(
                    Point.fromLngLat(-2.46, 27.6835)),
                Feature.fromGeometry(
                    Polygon.fromLngLats(Arrays.asList(Arrays.asList(
                        Point.fromLngLat(61.938950426660604, 5.9765625),
                        Point.fromLngLat(52.696361078274485, 33.046875),
                        Point.fromLngLat(69.90011762668541, 28.828124999999996),
                        Point.fromLngLat(61.938950426660604, 5.9765625))))),
                Feature.fromGeometry(
                    MultiPolygon.fromPolygons(Arrays.asList(
                        Polygon.fromLngLats(Arrays.asList(Arrays.asList(
                        Point.fromLngLat(11.42578125, 16.636191878397664),
                        Point.fromLngLat(7.91015625, -9.102096738726443),
                        Point.fromLngLat(31.113281249999996, 17.644022027872726),
                        Point.fromLngLat(11.42578125, 16.636191878397664)
                    ))),
                        Polygon.fromLngLats(Arrays.asList(Arrays.asList(
                        Point.fromLngLat(30.0, 0.0),
                        Point.fromLngLat(102.0, 0.0),
                        Point.fromLngLat(103.0, 1.0),
                        Point.fromLngLat(30.0, 0.0)
                    )))
                )))
            ));

    FeatureCollection combinedFeatureCollection = TurfConversion.combine(featureCollectionWithPointPolygonAndMultiPolygon);
    assertNotNull(combinedFeatureCollection);
    MultiPolygon multiPolygon = null;
    MultiPoint multiPoint = null;
    for (int x = 0; x < combinedFeatureCollection.features().size(); x++) {
      Feature singleFeature = combinedFeatureCollection.features().get(x);
      if (singleFeature.geometry() instanceof MultiPolygon) {
        multiPolygon = (MultiPolygon) combinedFeatureCollection.features().get(x).geometry();
      }
      if (singleFeature.geometry() instanceof MultiPoint) {
        multiPoint = (MultiPoint) combinedFeatureCollection.features().get(x).geometry();
      }
    }
    assertNotNull(multiPolygon);
    assertNotNull(multiPoint);

    // Checking the first Polygon in the MultiPolygon

    // Checking the first Point
    assertEquals(61.938950426660604, multiPolygon.coordinates().get(0).get(0).get(0).longitude(), DELTA);
    assertEquals(5.9765625, multiPolygon.coordinates().get(0).get(0).get(0).latitude(), DELTA);

    // Checking the second Point
    assertEquals(52.696361078274485, multiPolygon.coordinates().get(0).get(0).get(1).longitude(), DELTA);
    assertEquals(33.046875, multiPolygon.coordinates().get(0).get(0).get(1).latitude(), DELTA);

    // Checking the second Polygon in the MultiPolygon

    // Checking the first Point
    assertEquals(11.42578125, multiPolygon.coordinates().get(1).get(0).get(0).longitude(), DELTA);
    assertEquals(16.636191878397664, multiPolygon.coordinates().get(1).get(0).get(0).latitude(), DELTA);

    // Checking the second Point
    assertEquals(7.91015625, multiPolygon.coordinates().get(1).get(0).get(1).longitude(), DELTA);
    assertEquals(-9.102096738726443, multiPolygon.coordinates().get(1).get(0).get(1).latitude(), DELTA);

    // Checking the third Polygon in the MultiPolygon

    // Checking the first Point
    assertEquals(30.0, multiPolygon.coordinates().get(2).get(0).get(0).longitude(), DELTA);
    assertEquals(0.0, multiPolygon.coordinates().get(2).get(0).get(0).latitude(), DELTA);

    // Checking the second Point
    assertEquals(102.0, multiPolygon.coordinates().get(2).get(0).get(1).longitude(), DELTA);
    assertEquals(0.0, multiPolygon.coordinates().get(2).get(0).get(1).latitude(), DELTA);
  }

  @Test
  public void combinePointAndLineStringGeometry() throws Exception {
    FeatureCollection pointAndLineStringFeatureCollection =
      FeatureCollection.fromFeatures(
        Arrays.asList(
          Feature.fromGeometry(Point.fromLngLat(-2.46, 27.6835)),
          Feature.fromGeometry(
            LineString.fromLngLats(
              Arrays.asList(Point.fromLngLat(-11.25, 55.7765),
                Point.fromLngLat(41.1328, 22.91792)))
          )));

    FeatureCollection combinedFeatureCollection = TurfConversion.combine(pointAndLineStringFeatureCollection);
    assertNotNull(combinedFeatureCollection);
    MultiPoint multiPoint = null;
    MultiLineString multiLineString = null;
    for (int x = 0; x < combinedFeatureCollection.features().size(); x++) {
      Feature singleFeature = combinedFeatureCollection.features().get(x);
      if (singleFeature.geometry() instanceof MultiPoint) {
        multiPoint = (MultiPoint) combinedFeatureCollection.features().get(x).geometry();
      }
      if (singleFeature.geometry() instanceof MultiLineString) {
        multiLineString = (MultiLineString) combinedFeatureCollection.features().get(x).geometry();
      }
    }
    assertNotNull(multiPoint);
    assertNotNull(multiLineString);

    // Checking the LineString in the MultiLineString

    // Checking the first LineString location
    assertEquals(-11.25, multiLineString.coordinates().get(0).get(0).longitude(), DELTA);
    assertEquals(55.7765, multiLineString.coordinates().get(0).get(0).latitude(), DELTA);

    // Checking the second LineString location
    assertEquals(41.1328, multiLineString.coordinates().get(0).get(1).longitude(), DELTA);
    assertEquals(22.91792, multiLineString.coordinates().get(0).get(1).latitude(), DELTA);

    // Checking the Point in the MultiPoint

    // Checking the first and only Point
    assertEquals(-2.46, multiPoint.coordinates().get(0).longitude(), DELTA);
    assertEquals(27.6835, multiPoint.coordinates().get(0).latitude(), DELTA);
  }

  @Test
  public void combinePointAndMultiPolygonAndLineStringGeometry() throws Exception {
    FeatureCollection pointMultiPolygonAndLineStringFeatureCollection =
      FeatureCollection.fromFeatures(
        Arrays.asList(
          Feature.fromGeometry(Point.fromLngLat(-2.46, 27.6835)),
          Feature.fromGeometry(MultiPolygon.fromPolygons(Arrays.asList(
            Polygon.fromLngLats(Arrays.asList(Arrays.asList(
              Point.fromLngLat(11.42578125, 16.636191878397664),
              Point.fromLngLat(7.91015625, -9.102096738726443),
              Point.fromLngLat(31.113281249999996, 17.644022027872726),
              Point.fromLngLat(11.42578125, 16.636191878397664)
            )))))),
          Feature.fromGeometry(LineString.fromLngLats(
            Arrays.asList(Point.fromLngLat(-11.25, 55.7765),
              Point.fromLngLat(41.1328, 22.91792)))
          )));

    FeatureCollection combinedFeatureCollection = TurfConversion.combine(pointMultiPolygonAndLineStringFeatureCollection);
    assertNotNull(combinedFeatureCollection);
    MultiPoint multiPoint = null;
    MultiLineString multiLineString = null;
    MultiPolygon multiPolygon = null;
    for (int x = 0; x < combinedFeatureCollection.features().size(); x++) {
      Feature singleFeature = combinedFeatureCollection.features().get(x);
      if (singleFeature.geometry() instanceof MultiPoint) {
        multiPoint = (MultiPoint) combinedFeatureCollection.features().get(x).geometry();
      }
      if (singleFeature.geometry() instanceof MultiLineString) {
        multiLineString = (MultiLineString) combinedFeatureCollection.features().get(x).geometry();
      }
      if (singleFeature.geometry() instanceof MultiPolygon) {
        multiPolygon = (MultiPolygon) combinedFeatureCollection.features().get(x).geometry();
      }
    }
    assertNotNull(multiPoint);
    assertNotNull(multiLineString);
    assertNotNull(multiPolygon);

    // Checking the Polygon in the MultiPolygon

    // Checking the first Point
    assertEquals(11.42578125, multiPolygon.coordinates().get(0).get(0).get(0).longitude(), DELTA);
    assertEquals(16.636191878397664, multiPolygon.coordinates().get(0).get(0).get(0).latitude(), DELTA);

    // Checking the second Point
    assertEquals(7.91015625, multiPolygon.coordinates().get(0).get(0).get(1).longitude(), DELTA);
    assertEquals(-9.102096738726443, multiPolygon.coordinates().get(0).get(0).get(1).latitude(), DELTA);

    // Checking the LineString in the MultiLineString

    // Checking the first LineString location
    assertEquals(-11.25, multiLineString.coordinates().get(0).get(0).longitude(), DELTA);
    assertEquals(55.7765, multiLineString.coordinates().get(0).get(0).latitude(), DELTA);

    // Checking the second LineString location
    assertEquals(41.1328, multiLineString.coordinates().get(0).get(1).longitude(), DELTA);
    assertEquals(22.91792, multiLineString.coordinates().get(0).get(1).latitude(), DELTA);

    // Checking the Point in the MultiPoint

    // Checking the first and only Point
    assertEquals(-2.46, multiPoint.coordinates().get(0).longitude(), DELTA);
    assertEquals(27.6835, multiPoint.coordinates().get(0).latitude(), DELTA);
  }

  @Test
  public void combine_featureCollectionSizeCheck() throws Exception {
    FeatureCollection pointMultiPolygonAndLineStringFeatureCollection =
      FeatureCollection.fromFeatures(
        Arrays.asList(
          Feature.fromGeometry(Point.fromLngLat(-2.46, 27.6835)),
          Feature.fromGeometry(MultiPolygon.fromPolygons(Arrays.asList(
            Polygon.fromLngLats(Arrays.asList(Arrays.asList(
              Point.fromLngLat(11.42578125, 16.636191878397664),
              Point.fromLngLat(7.91015625, -9.102096738726443),
              Point.fromLngLat(31.113281249999996, 17.644022027872726),
              Point.fromLngLat(11.42578125, 16.636191878397664)
            )))))),
          Feature.fromGeometry(LineString.fromLngLats(
            Arrays.asList(Point.fromLngLat(-11.25, 55.7765),
              Point.fromLngLat(41.1328, 22.91792)))
          )));

    FeatureCollection combinedFeatureCollection = TurfConversion.combine(pointMultiPolygonAndLineStringFeatureCollection);
    assertNotNull(combinedFeatureCollection);
    assertEquals(3, combinedFeatureCollection.features().size());
  }

  @Test
  public void combineEmptyFeatureCollectionThrowsException() throws Exception {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Your FeatureCollection doesn't have any Feature objects in it."));
    TurfConversion.combine(FeatureCollection.fromJson(
      "{\n" +
        "  \"type\": \"FeatureCollection\",\n" +
        "  \"features\": []\n" +
        "}"
    ));
  }

  @Test
  public void explodePointSingleFeature() throws NullPointerException {
    Point point = Point.fromLngLat(102, 0.5);
    assertEquals(1, TurfConversion.explode(Feature.fromGeometry(point)).features().size());
  }

  @Test
  public void explodeMultiPointSingleFeature() throws NullPointerException {
    MultiPoint multiPoint = MultiPoint.fromJson(loadJsonFixture(TURF_EXPLODE_MULTI_POINT));
    assertEquals(4, TurfConversion.explode(Feature.fromGeometry(multiPoint)).features().size());
  }

  @Test
  public void explodeLineStringSingleFeature() throws NullPointerException {
    LineString lineString = LineString.fromJson(loadJsonFixture(TURF_EXPLODE_LINESTRING));
    assertEquals(4, TurfConversion.explode(Feature.fromGeometry(lineString)).features().size());
  }

  @Test
  public void explodePolygonSingleFeature() throws NullPointerException {
    Polygon polygon = Polygon.fromLngLats(Arrays.asList(
      Arrays.asList(
        Point.fromLngLat(0, 101),
        Point.fromLngLat(1, 101),
        Point.fromLngLat(1, 100),
        Point.fromLngLat(0, 100))));
    assertEquals(3, TurfConversion.explode(Feature.fromGeometry(polygon)).features().size());
  }

  @Test
  public void explodeMultiLineStringSingleFeature() throws NullPointerException {
    MultiLineString multiLineString = MultiLineString.fromJson(loadJsonFixture(TURF_EXPLODE_MULTILINESTRING));
    assertEquals(4, TurfConversion.explode(Feature.fromGeometry(multiLineString)).features().size());
  }

  @Test
  public void explodeMultiPolygonSingleFeature() throws NullPointerException {
    MultiPolygon multiPolygon = MultiPolygon.fromJson(loadJsonFixture(TURF_EXPLODE_MULTIPOLYGON));
    assertEquals(12, TurfConversion.explode(Feature.fromGeometry(multiPolygon)).features().size());
  }

  @Test
  public void explodeGeometryCollectionSingleFeature() throws NullPointerException {
    GeometryCollection geometryCollection = GeometryCollection.fromJson(loadJsonFixture(TURF_EXPLODE_GEOMETRY_COLLECTION));
    assertEquals(3, TurfConversion.explode(Feature.fromGeometry(geometryCollection)).features().size());
  }

  @Test
  public void explodeFeatureCollection() throws NullPointerException {
    FeatureCollection featureCollection = FeatureCollection.fromFeatures(new Feature[] {
      Feature.fromGeometry(MultiLineString.fromJson(loadJsonFixture(TURF_EXPLODE_MULTILINESTRING))),
      Feature.fromGeometry(MultiPolygon.fromJson(loadJsonFixture(TURF_EXPLODE_MULTIPOLYGON)))
    });
    assertEquals(16, TurfConversion.explode(featureCollection).features().size());
  }

  @Test
  public void polygonToLine_GeometryPolygon() throws NullPointerException {
    Polygon polygon = Polygon.fromJson(loadJsonFixture(TURF_POLYGON_TO_LINE_PATH_IN + TURF_POLYGON_TO_LINE_FILENAME_GEOMETRY_POLYGON));
    Feature expected = Feature.fromJson(loadJsonFixture(TURF_POLYGON_TO_LINE_PATH_OUT + TURF_POLYGON_TO_LINE_FILENAME_GEOMETRY_POLYGON));
    compareJson(expected.toJson(), TurfConversion.polygonToLine(polygon).toJson());
  }

  @Test
  public void polygonToLine_Polygon() throws NullPointerException {
    Feature polygon = Feature.fromJson(loadJsonFixture(TURF_POLYGON_TO_LINE_PATH_IN + TURF_POLYGON_TO_LINE_FILENAME_POLYGON));
    Feature expected = Feature.fromJson(loadJsonFixture(TURF_POLYGON_TO_LINE_PATH_OUT + TURF_POLYGON_TO_LINE_FILENAME_POLYGON));
    compareJson(expected.toJson(), TurfConversion.polygonToLine(polygon).toJson());
  }

  @Test
  public void polygonToLine_PolygonWithHole() throws NullPointerException {
    Feature polygon = Feature.fromJson(loadJsonFixture(TURF_POLYGON_TO_LINE_PATH_IN + TURF_POLYGON_TO_LINE_FILENAME_POLYGON_WITH_HOLE));
    Feature expected = Feature.fromJson(loadJsonFixture(TURF_POLYGON_TO_LINE_PATH_OUT + TURF_POLYGON_TO_LINE_FILENAME_POLYGON_WITH_HOLE));
    compareJson(expected.toJson(), TurfConversion.polygonToLine(polygon).toJson());
  }

  @Test
  public void polygonToLine_MultiPolygon() throws NullPointerException {
    Feature multiPolygon = Feature.fromJson(loadJsonFixture(TURF_POLYGON_TO_LINE_PATH_IN + TURF_POLYGON_TO_LINE_FILENAME_MULTIPOLYGON));
    FeatureCollection expected = FeatureCollection.fromJson(loadJsonFixture(TURF_POLYGON_TO_LINE_PATH_OUT + TURF_POLYGON_TO_LINE_FILENAME_MULTIPOLYGON));
    compareJson(expected.toJson(), TurfConversion.multiPolygonToLine(multiPolygon).toJson());
  }

  @Test
  public void polygonToLine_MultiPolygonWithHoles() throws NullPointerException {
    Feature multiPolygon = Feature.fromJson(loadJsonFixture(TURF_POLYGON_TO_LINE_PATH_IN + TURF_POLYGON_TO_LINE_FILENAME_MULTIPOLYGON_WITH_HOLES));
    FeatureCollection expected = FeatureCollection.fromJson(loadJsonFixture(TURF_POLYGON_TO_LINE_PATH_OUT + TURF_POLYGON_TO_LINE_FILENAME_MULTIPOLYGON_WITH_HOLES));
    compareJson(expected.toJson(), TurfConversion.multiPolygonToLine(multiPolygon).toJson());
  }

  @Test
  public void polygonToLine_MultiPolygonWithOuterDoughnut() throws NullPointerException {
    Feature multiPolygon = Feature.fromJson(loadJsonFixture(TURF_POLYGON_TO_LINE_PATH_IN + TURF_POLYGON_TO_LINE_FILENAME_MULTIPOLYGON_OUTER_DOUGHNUT));
    FeatureCollection expected = FeatureCollection.fromJson(loadJsonFixture(TURF_POLYGON_TO_LINE_PATH_OUT + TURF_POLYGON_TO_LINE_FILENAME_MULTIPOLYGON_OUTER_DOUGHNUT));
    compareJson(expected.toJson(), TurfConversion.multiPolygonToLine(multiPolygon).toJson());
  }
}
