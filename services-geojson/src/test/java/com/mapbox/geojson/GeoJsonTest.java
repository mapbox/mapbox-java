package com.mapbox.geojson;

import com.mapbox.services.BaseTest;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeoJsonTest extends BaseTest {

  private static final String GEOJSON_FIXTURE = "sample-geojson-result.json";

//  /**
//   * Test whether we are rounding correctly to conform to the RFC 7946 GeoJson spec.
//   *
//   * @throws IOException If fixture fails loading.
//   * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1.10">section 3.1.10</a>
//   */
//  @Test
//  public void testSevenDigitRounding() throws IOException {
//    Point roundDown = Point.fromLngLat(1.123456789, 1.123456789);
//    Point noRound = Point.fromLngLat(1.1234, 1.12345);
//    Point matchRound = Point.fromLngLat(1.1234567, 1.1234567);
//    Point roundLat = Point.fromLngLat(1.1234567, 1.12345678910);
//    Point roundLon = Point.fromLngLat(1.12345678910, 1.1234567);
//    Point largeRound = Point.fromLngLat(105.12345678910, 89.1234567);
//    Point negRound = Point.fromLngLat(-105.12345678910, -89.1234567);
//
//    List<Feature> features = new ArrayList<>();
//    features.add(Feature.fromGeometry(roundDown));
//    features.add(Feature.fromGeometry(noRound));
//    features.add(Feature.fromGeometry(matchRound));
//    features.add(Feature.fromGeometry(roundLat));
//    features.add(Feature.fromGeometry(roundLon));
//    features.add(Feature.fromGeometry(largeRound));
//    features.add(Feature.fromGeometry(negRound));
//
//    FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);
//    compareJson(loadJsonFixture(GEOJSON_FIXTURE), featureCollection.toJson());
//  }
}
