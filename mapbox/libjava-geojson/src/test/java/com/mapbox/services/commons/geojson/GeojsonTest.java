package com.mapbox.services.commons.geojson;

import com.mapbox.services.commons.models.Position;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GeojsonTest extends BaseTest {

  private static final String SAMPLE_GEOJSON_FIXTURE = "src/test/fixtures/sample-geojson-result.json";

  /**
   * Test whether we are rounding correctly to conform to the RFC 7946 GeoJSON spec.
   *
   * @throws IOException If fixture fails loading.
   * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1.10">section 3.1.10</a>
   */
  @Test
  public void testSevenDigitRounding() throws IOException {
    Position roundDown = Position.fromCoordinates(1.123456789, 1.123456789);
    Position noRound = Position.fromCoordinates(1.1234, 1.12345);
    Position matchRound = Position.fromCoordinates(1.1234567, 1.1234567);
    Position roundLat = Position.fromCoordinates(1.1234567, 1.12345678910);
    Position roundLon = Position.fromCoordinates(1.12345678910, 1.1234567);

    List<Feature> features = new ArrayList<>();
    features.add(Feature.fromGeometry(Point.fromCoordinates(roundDown)));
    features.add(Feature.fromGeometry(Point.fromCoordinates(noRound)));
    features.add(Feature.fromGeometry(Point.fromCoordinates(matchRound)));
    features.add(Feature.fromGeometry(Point.fromCoordinates(roundLat)));
    features.add(Feature.fromGeometry(Point.fromCoordinates(roundLon)));
    FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);

    String geojson = new String(Files.readAllBytes(
      Paths.get(SAMPLE_GEOJSON_FIXTURE)), Charset.forName("utf-8"));
    FeatureCollection geo = FeatureCollection.fromJson(featureCollection.toJson());
    compareJson(geojson, geo.toJson());
  }
}
