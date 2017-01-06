package com.mapbox.services.api.geojson;

import com.mapbox.services.api.BaseTest;
import com.mapbox.services.commons.geojson.MultiLineString;
import com.mapbox.services.commons.models.Position;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MultiLineStringTest extends BaseTest {

  private static final String SAMPLE_MULTILINESTRING_FIXTURE = "src/test/fixtures/geojson/sample-point.json";

  @Test
  public void fromJson() throws IOException {
    String geojson = new String(Files.readAllBytes(
      Paths.get(SAMPLE_MULTILINESTRING_FIXTURE)), Charset.forName("utf-8"));
    MultiLineString geo = MultiLineString.fromJson(geojson);
    assertEquals(geo.getType(), "MultiLineString");
    assertEquals(geo.getCoordinates().get(0).get(0).getLongitude(), 100.0, 0.0);
    assertEquals(geo.getCoordinates().get(0).get(0).getLatitude(), 0.0, 0.0);
    assertFalse(geo.getCoordinates().get(0).get(0).hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    String geojson = new String(Files.readAllBytes(
      Paths.get(SAMPLE_MULTILINESTRING_FIXTURE)), Charset.forName("utf-8"));
    MultiLineString geo = MultiLineString.fromJson(geojson);
    compareJson(geojson, geo.toJson());
  }

  @Test
  public void checksEqualityFromCoordinates() {
    MultiLineString multiLineString = MultiLineString.fromCoordinates(new double[][][] {
      {{100.0, 0.0}, {101.0, 1.0}},
      {{102.0, 2.0}, {103.0, 3.0}}
    });

    String multiLineCoordinates = obtainLiteralCoordinatesFrom(multiLineString);

    assertEquals("Lines: \n"
        + "Line: \n"
        + "Position [longitude=100.0, latitude=0.0, altitude=NaN]\n"
        + "Position [longitude=101.0, latitude=1.0, altitude=NaN]\n"
        + "Line: \n"
        + "Position [longitude=102.0, latitude=2.0, altitude=NaN]\n"
        + "Position [longitude=103.0, latitude=3.0, altitude=NaN]\n",
      multiLineCoordinates);
  }

  @Test
  public void checksJsonEqualityFromCoordinates() {
    MultiLineString multiLineString = MultiLineString.fromCoordinates(new double[][][] {
      {{100.0, 0.0}, {101.0, 1.0}},
      {{102.0, 2.0}, {103.0, 3.0}}
    });

    String multiLineJsonCoordinates = multiLineString.toJson();

    compareJson("{ \"type\": \"MultiLineString\",\n"
      + "\"coordinates\": [\n"
      + "[ [100.0, 0.0], [101.0, 1.0] ],\n"
      + "[ [102.0, 2.0], [103.0, 3.0] ]\n"
      + "]\n"
      + "}", multiLineJsonCoordinates);
  }

  private String obtainLiteralCoordinatesFrom(MultiLineString multiLine) {
    List<List<Position>> multiLineCoordinates = multiLine.getCoordinates();
    StringBuilder literalCoordinates = new StringBuilder();
    literalCoordinates.append("Lines: \n");
    for (List<Position> line : multiLineCoordinates) {
      literalCoordinates.append("Line: \n");
      for (Position point : line) {
        literalCoordinates.append(point.toString());
        literalCoordinates.append("\n");
      }
    }
    return literalCoordinates.toString();
  }

}
