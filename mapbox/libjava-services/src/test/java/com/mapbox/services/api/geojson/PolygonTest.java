package com.mapbox.services.api.geojson;

import com.mapbox.services.api.BaseTest;
import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PolygonTest extends BaseTest {

  private static final String SAMPLE_POLYGON_FIXTURE = "src/test/fixtures/geojson/sample-polygon.json";
  private static final String SAMPLE_POLYGON_HOLES_FIXTURE = "src/test/fixtures/geojson/sample-polygon-holes.json";

  @Test
  public void fromJson() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_POLYGON_FIXTURE)), Charset.forName("utf-8"));
    Polygon geo = Polygon.fromJson(geojson);
    assertEquals(geo.getType(), "Polygon");
    assertEquals(geo.getCoordinates().get(0).get(0).getLongitude(), 100.0, DELTA);
    assertEquals(geo.getCoordinates().get(0).get(0).getLatitude(), 0.0, DELTA);
    assertFalse(geo.getCoordinates().get(0).get(0).hasAltitude());
  }

  @Test
  public void fromJsonHoles() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_POLYGON_FIXTURE)), Charset.forName("utf-8"));
    Polygon geo = Polygon.fromJson(geojson);
    assertEquals(geo.getType(), "Polygon");
    assertEquals(geo.getCoordinates().get(0).get(0).getLongitude(), 100.0, DELTA);
    assertEquals(geo.getCoordinates().get(0).get(0).getLatitude(), 0.0, DELTA);
    assertFalse(geo.getCoordinates().get(0).get(0).hasAltitude());
  }

  @Test
  public void toJson() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_POLYGON_FIXTURE)), Charset.forName("utf-8"));
    Polygon geo = Polygon.fromJson(geojson);
    compareJson(geojson, geo.toJson());
  }

  @Test
  public void toJsonHoles() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_POLYGON_HOLES_FIXTURE)), Charset.forName("utf-8"));
    Polygon geo = Polygon.fromJson(geojson);
    compareJson(geojson, geo.toJson());
  }

  @Test
  public void checksEqualityFromCoordinates() {
    Polygon polygon = Polygon.fromCoordinates(new double[][][] {
      {{100.0, 0.0}, {101.0, 0.0}, {101.0, 1.0}, {100.0, 1.0}, {100.0, 0.0}},
      {{100.2, 0.2}, {100.8, 0.2}, {100.8, 0.8}, {100.2, 0.8}, {100.2, 0.2}}
    });

    String polygonCoordinates = obtainLiteralCoordinatesFrom(polygon);

    assertEquals("Polygon: \n"
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
      + "Position [longitude=100.2, latitude=0.2, altitude=NaN]\n", polygonCoordinates);
  }

  @Test
  public void checksJsonEqualityFromCoordinates() {
    Polygon polygon = Polygon.fromCoordinates(new double[][][] {
      {{100.0, 0.0}, {101.0, 0.0}, {101.0, 1.0}, {100.0, 1.0}, {100.0, 0.0}},
      {{100.2, 0.2}, {100.8, 0.2}, {100.8, 0.8}, {100.2, 0.8}, {100.2, 0.2}}
    });

    String polygonJsonCoordinates = polygon.toJson();

    compareJson("{ \"type\": \"Polygon\",\n"
      + "\"coordinates\": [\n"
      + "[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ],\n"
      + "[ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]\n"
      + "]\n"
      + "}", polygonJsonCoordinates);
  }

  private String obtainLiteralCoordinatesFrom(Polygon polygon) {
    List<List<Position>> polygonCoordinates = polygon.getCoordinates();
    StringBuilder literalCoordinates = new StringBuilder();
    literalCoordinates.append("Polygon: \n");
    for (List<Position> lines : polygonCoordinates) {
      literalCoordinates.append("Lines: \n");
      for (Position point : lines) {
        literalCoordinates.append(point.toString());
        literalCoordinates.append("\n");
      }
    }
    return literalCoordinates.toString();
  }

}
