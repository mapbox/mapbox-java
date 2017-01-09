package com.mapbox.services.commons.geojson;

import com.mapbox.services.commons.models.Position;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PointTest extends BaseTest {

  private static final String SAMPLE_POINT_FIXTURE = "src/test/fixtures/sample-point.json";

  @Test
  public void fromJson() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_POINT_FIXTURE)), Charset.forName("utf-8"));
    Point geo = Point.fromJson(geojson);
    assertEquals(geo.getType(), "Point");
    assertEquals(geo.getCoordinates().getLongitude(), 100.0, 0.0);
    assertEquals(geo.getCoordinates().getLatitude(), 0.0, 0.0);
    assertFalse(geo.getCoordinates().hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_POINT_FIXTURE)), Charset.forName("utf-8"));
    Point geo = Point.fromJson(geojson);
    compareJson(geojson, geo.toJson());
  }

  @Test
  public void checksEqualityFromCoordinates() {
    Point point = Point.fromCoordinates(new double[] {100.0, 0.0});

    String pointCoordinates = obtainLiteralCoordinatesFrom(point);

    assertEquals("Point: \n"
      + "Position [longitude=100.0, latitude=0.0, altitude=NaN]\n", pointCoordinates);
  }

  @Test
  public void checksJsonEqualityFromCoordinates() {
    Point point = Point.fromCoordinates(new double[] {100.0, 0.0});

    String pointJsonCoordinates = point.toJson();

    compareJson("{ \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }", pointJsonCoordinates);
  }

  private String obtainLiteralCoordinatesFrom(Point point) {
    Position thePoint = point.getCoordinates();
    StringBuilder literalCoordinates = new StringBuilder();
    literalCoordinates.append("Point: \n");

    literalCoordinates.append(thePoint.toString());
    literalCoordinates.append("\n");

    return literalCoordinates.toString();
  }

}

