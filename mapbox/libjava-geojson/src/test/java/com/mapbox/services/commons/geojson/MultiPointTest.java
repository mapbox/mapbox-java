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

public class MultiPointTest extends BaseTest {

  private static final String SAMPLE_MULTIPOINT_FIXTURE = "src/test/fixtures/sample-multipoint.json";

  @Test
  public void fromJson() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_MULTIPOINT_FIXTURE)), Charset.forName("utf-8"));
    MultiPoint geo = MultiPoint.fromJson(geojson);
    assertEquals(geo.getType(), "MultiPoint");
    assertEquals(geo.getCoordinates().get(0).getLongitude(), 100.0, 0.0);
    assertEquals(geo.getCoordinates().get(0).getLatitude(), 0.0, 0.0);
    assertFalse(geo.getCoordinates().get(0).hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_MULTIPOINT_FIXTURE)), Charset.forName("utf-8"));
    MultiPoint geo = MultiPoint.fromJson(geojson);
    compareJson(geojson, geo.toJson());
  }

  @Test
  public void checksEqualityFromCoordinates() {
    MultiPoint multiPoint = MultiPoint.fromCoordinates(new double[][] {
      {100.0, 0.0}, {101.0, 1.0}
    });

    String multiPointCoordinates = obtainLiteralCoordinatesFrom(multiPoint);

    assertEquals("Points: \n"
        + "Position [longitude=100.0, latitude=0.0, altitude=NaN]\n"
        + "Position [longitude=101.0, latitude=1.0, altitude=NaN]\n",
      multiPointCoordinates);
  }

  @Test
  public void checksJsonEqualityFromCoordinates() {
    MultiPoint multiPoint = MultiPoint.fromCoordinates(new double[][] {
      {100.0, 0.0}, {101.0, 1.0}
    });

    String multiPointJsonCoordinates = multiPoint.toJson();

    compareJson("{ \"type\": \"MultiPoint\",\n"
      + "\"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]\n"
      + "}", multiPointJsonCoordinates);
  }

  private String obtainLiteralCoordinatesFrom(MultiPoint multiPoint) {
    List<Position> multiPointCoordinates = multiPoint.getCoordinates();
    StringBuilder literalCoordinates = new StringBuilder();
    literalCoordinates.append("Points: \n");
    for (Position point : multiPointCoordinates) {
      literalCoordinates.append(point.toString());
      literalCoordinates.append("\n");
    }
    return literalCoordinates.toString();
  }

}
