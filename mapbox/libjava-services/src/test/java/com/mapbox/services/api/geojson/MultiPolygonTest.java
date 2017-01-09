package com.mapbox.services.api.geojson;

import com.mapbox.services.api.BaseTest;
import com.mapbox.services.commons.geojson.MultiPolygon;
import com.mapbox.services.commons.models.Position;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MultiPolygonTest extends BaseTest {

  private static final String SAMPLE_MULTIPOLYGON_FIXTURE = "src/test/fixtures/geojson/sample-multipolygon.json";

  @Test
  public void fromJson() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_MULTIPOLYGON_FIXTURE)), Charset.forName("utf-8"));
    MultiPolygon geo = MultiPolygon.fromJson(geojson);
    assertEquals(geo.getType(), "MultiPolygon");
    assertEquals(geo.getCoordinates().get(0).get(0).get(0).getLongitude(), 102.0, 0.0);
    assertEquals(geo.getCoordinates().get(0).get(0).get(0).getLatitude(), 2.0, 0.0);
    assertFalse(geo.getCoordinates().get(0).get(0).get(0).hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_MULTIPOLYGON_FIXTURE)), Charset.forName("utf-8"));
    MultiPolygon geo = MultiPolygon.fromJson(geojson);
    compareJson(geojson, geo.toJson());
  }

  @Test
  public void checksEqualityFromCoordinates() {
    MultiPolygon multiPolygon = MultiPolygon.fromCoordinates(new double[][][][] {
      {{{102.0, 2.0}, {103.0, 2.0}, {103.0, 3.0}, {102.0, 3.0}, {102.0, 2.0}}},
      {{{100.0, 0.0}, {101.0, 0.0}, {101.0, 1.0}, {100.0, 1.0}, {100.0, 0.0}},
        {{100.2, 0.2}, {100.8, 0.2}, {100.8, 0.8}, {100.2, 0.8}, {100.2, 0.2}}}
    });

    String multiPolygonCoordinates = obtainLiteralCoordinatesFrom(multiPolygon);

    assertEquals("Polygons: \n"
      + "Polygon: \n"
      + "Lines: \n"
      + "Position [longitude=102.0, latitude=2.0, altitude=NaN]\n"
      + "Position [longitude=103.0, latitude=2.0, altitude=NaN]\n"
      + "Position [longitude=103.0, latitude=3.0, altitude=NaN]\n"
      + "Position [longitude=102.0, latitude=3.0, altitude=NaN]\n"
      + "Position [longitude=102.0, latitude=2.0, altitude=NaN]\n"
      + "Polygon: \n"
      + "Lines: \n"
      + "Position [longitude=100.0, latitude=0.0, altitude=NaN]\n"
      + "Position [longitude=101.0, latitude=0.0, altitude=NaN]\n"
      + "Position [longitude=101.0, latitude=1.0, altitude=NaN]\n"
      + "Position [longitude=100.0, latitude=1.0, altitude=NaN]\n"
      + "Position [longitude=100.0, latitude=0.0, altitude=NaN]\n"
      + "Lines: \n"
      + "Position [longitude=100.2, latitude=0.2, altitude=NaN]\n"
      + "Position [longitude=100.8, latitude=0.2, altitude=NaN]\n"
      + "Position [longitude=100.8, latitude=0.8, altitude=NaN]\n"
      + "Position [longitude=100.2, latitude=0.8, altitude=NaN]\n"
      + "Position [longitude=100.2, latitude=0.2, altitude=NaN]\n", multiPolygonCoordinates);
  }

  @Test
  public void checksJsonEqualityFromCoordinates() {
    MultiPolygon multiPolygon = MultiPolygon.fromCoordinates(new double[][][][] {
      {{{102.0, 2.0}, {103.0, 2.0}, {103.0, 3.0}, {102.0, 3.0}, {102.0, 2.0}}},
      {{{100.0, 0.0}, {101.0, 0.0}, {101.0, 1.0}, {100.0, 1.0}, {100.0, 0.0}},
        {{100.2, 0.2}, {100.8, 0.2}, {100.8, 0.8}, {100.2, 0.8}, {100.2, 0.2}}}
    });

    String multiPolygonJsonCoordinates = multiPolygon.toJson();

    compareJson("{ \"type\": \"MultiPolygon\",\n"
      + "\"coordinates\": [\n"
      + "[[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],\n"
      + "[[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],\n"
      + "[[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]\n"
      + "]\n"
      + "}", multiPolygonJsonCoordinates);
  }

  private String obtainLiteralCoordinatesFrom(MultiPolygon multiPolygon) {
    List<List<List<Position>>> multiPolygonCoordinates = multiPolygon.getCoordinates();
    StringBuilder literalCoordinates = new StringBuilder();
    literalCoordinates.append("Polygons: \n");
    for (List<List<Position>> polygon : multiPolygonCoordinates) {
      literalCoordinates.append("Polygon: \n");
      for (List<Position> lines : polygon) {
        literalCoordinates.append("Lines: \n");
        for (Position point : lines) {
          literalCoordinates.append(point.toString());
          literalCoordinates.append("\n");
        }
      }
    }
    return literalCoordinates.toString();
  }

}
