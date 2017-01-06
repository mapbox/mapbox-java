package com.mapbox.services.api.geojson;

import com.mapbox.services.api.BaseTest;
import com.mapbox.services.commons.geojson.GeometryCollection;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class GeometryCollectionTest extends BaseTest {

  private static final String SAMPLE_GEOMETRYCOLLECTION_FIXTURE =
    "src/test/fixtures/geojson/sample-geometrycollection.json";

  @Test
  public void fromJson() throws IOException {
    String geojson = new String(Files.readAllBytes(
      Paths.get(SAMPLE_GEOMETRYCOLLECTION_FIXTURE)), Charset.forName("utf-8"));
    GeometryCollection geo = GeometryCollection.fromJson(geojson);
    assertEquals(geo.getType(), "GeometryCollection");
    assertEquals(geo.getGeometries().get(0).getType(), "Point");
    assertEquals(geo.getGeometries().get(1).getType(), "LineString");
  }

  @Test
  public void toJson() throws IOException {
    String geojson = new String(Files.readAllBytes(
      Paths.get(SAMPLE_GEOMETRYCOLLECTION_FIXTURE)), Charset.forName("utf-8"));
    GeometryCollection geo = GeometryCollection.fromJson(geojson);
    compareJson(geojson, geo.toJson());
  }

}
