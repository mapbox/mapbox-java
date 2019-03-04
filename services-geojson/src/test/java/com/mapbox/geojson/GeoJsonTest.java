package com.mapbox.geojson;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GeoJsonTest extends TestUtils {

  private static final String GEOJSON_FIXTURE = "sample-geojson-result.json";

  /**
   * Test whether we are rounding correctly to conform to the RFC 7946 GeoJson spec.
   *
   * @throws IOException If fixture fails loading.
   * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1.10">section 3.1.10</a>
   */
  @Test
  public void testSevenDigitRounding() throws IOException {
    Point roundDown = Point.fromLngLat(1.12345678, 1.12345678);
    Point noRound = Point.fromLngLat(1.1234, 1.12345);
    Point matchRound = Point.fromLngLat(1.1234567, 1.1234567);
    Point roundLat = Point.fromLngLat(1.1234567, 1.12345678);
    Point roundLon = Point.fromLngLat(1.12345678, 1.1234567);
    Point largeRound = Point.fromLngLat(105.12345678, 89.1234567);
    Point negRound = Point.fromLngLat(-105.12345678, -89.1234567);

    List<Feature> features = new ArrayList<>();
    features.add(Feature.fromGeometry(roundDown));
    features.add(Feature.fromGeometry(noRound));
    features.add(Feature.fromGeometry(matchRound));
    features.add(Feature.fromGeometry(roundLat));
    features.add(Feature.fromGeometry(roundLon));
    features.add(Feature.fromGeometry(largeRound));
    features.add(Feature.fromGeometry(negRound));

    FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);

    List<Feature> featureCollectionRounded =
            FeatureCollection.fromJson(featureCollection.toJson()).features();
    Point roundDown2 = (Point)featureCollectionRounded.get(0).geometry();
    Point noRound2 = (Point)featureCollectionRounded.get(1).geometry();
    Point matchRound2 = (Point)featureCollectionRounded.get(2).geometry();
    Point roundLat2 = (Point)featureCollectionRounded.get(3).geometry();
    Point roundLon2 = (Point)featureCollectionRounded.get(4).geometry();
    Point largeRound2 = (Point)featureCollectionRounded.get(5).geometry();
    Point negRound2 = (Point)featureCollectionRounded.get(6).geometry();

    assertEquals(1.1234568, roundDown2.longitude(), DELTA);
    assertEquals(1.1234568, roundDown2.latitude(), DELTA);
    assertEquals(noRound, noRound2);
    assertEquals(matchRound, matchRound2);
    assertEquals(roundLat.longitude(), roundLat2.longitude(), DELTA);
    assertEquals(1.1234568, roundLat2.latitude(), DELTA);
    assertEquals(1.1234568, roundLon2.longitude(), DELTA);
    assertEquals(roundLon.latitude(), roundLon2.latitude(), DELTA);
    assertEquals(105.1234568, largeRound2.longitude(), DELTA);
    assertEquals(largeRound.latitude(), largeRound2.latitude(), DELTA);
    assertEquals(-105.1234568, negRound2.longitude(), DELTA);
    assertEquals(negRound.latitude(), negRound2.latitude(), DELTA);
  }
}
