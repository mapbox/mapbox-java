package com.mapbox.services.commons.geojson;

import com.mapbox.services.commons.models.Position;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LineStringTest extends BaseTest {

  private static final String SAMPLE_LINESTRING_FIXTURE = "src/test/fixtures/sample-linestring.json";

  @Test
  public void fromJson() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_LINESTRING_FIXTURE)), Charset.forName("utf-8"));
    LineString geo = LineString.fromJson(geojson);
    assertEquals(geo.getType(), "LineString");
    assertEquals(geo.getCoordinates().get(0).getLongitude(), 100.0, 0.0);
    assertEquals(geo.getCoordinates().get(0).getLatitude(), 0.0, 0.0);
    assertFalse(geo.getCoordinates().get(0).hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_LINESTRING_FIXTURE)), Charset.forName("utf-8"));
    LineString geo = LineString.fromJson(geojson);
    compareJson(geojson, geo.toJson());
  }

  @Test
  public void checksEqualityFromCoordinates() {
    LineString lineString = LineString.fromCoordinates(new double[][] {
      {100.0, 0.0}, {101.0, 1.0}
    });

    String lineCoordinates = obtainLiteralCoordinatesFrom(lineString);

    assertEquals("Line: \n"
        + "Position [longitude=100.0, latitude=0.0, altitude=NaN]\n"
        + "Position [longitude=101.0, latitude=1.0, altitude=NaN]\n",
      lineCoordinates);
  }

  @Test
  public void checksJsonEqualityFromCoordinates() {
    LineString lineString = LineString.fromCoordinates(new double[][] {
      {100.0, 0.0}, {101.0, 1.0}
    });

    String lineJsonCoordinates = lineString.toJson();

    compareJson("{ \"type\": \"LineString\",\n"
        + "\"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]\n}",
      lineJsonCoordinates);
  }

  private String obtainLiteralCoordinatesFrom(LineString line) {
    List<Position> lineCoordinates = line.getCoordinates();
    StringBuilder literalCoordinates = new StringBuilder();
    literalCoordinates.append("Line: \n");
    for (Position point : lineCoordinates) {
      literalCoordinates.append(point.toString());
      literalCoordinates.append("\n");
    }
    return literalCoordinates.toString();
  }

}
