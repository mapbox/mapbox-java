package com.mapbox.services.api.geojson;

import com.mapbox.services.api.BaseTest;
import com.mapbox.services.commons.geojson.FeatureCollection;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class FeatureCollectionTest extends BaseTest {

  private static final String SAMPLE_FEATURECOLLECTION_FIXTURE =
    "src/test/fixtures/geojson/sample-featurecollection.json";

  @Test
  public void fromJson() throws IOException {
    String geojson = new String(Files.readAllBytes(
      Paths.get(SAMPLE_FEATURECOLLECTION_FIXTURE)), Charset.forName("utf-8"));
    FeatureCollection geo = FeatureCollection.fromJson(geojson);
    assertEquals(geo.getType(), "FeatureCollection");
    assertEquals(geo.getFeatures().size(), 3);
    assertEquals(geo.getFeatures().get(0).getType(), "Feature");
    assertEquals(geo.getFeatures().get(0).getGeometry().getType(), "Point");
    assertEquals(geo.getFeatures().get(1).getType(), "Feature");
    assertEquals(geo.getFeatures().get(1).getGeometry().getType(), "LineString");
    assertEquals(geo.getFeatures().get(2).getType(), "Feature");
    assertEquals(geo.getFeatures().get(2).getGeometry().getType(), "Polygon");
  }

  @Test
  public void toJson() throws IOException {
    String geojson = new String(Files.readAllBytes(
      Paths.get(SAMPLE_FEATURECOLLECTION_FIXTURE)), Charset.forName("utf-8"));
    FeatureCollection geo = FeatureCollection.fromJson(geojson);
    compareJson(geojson, geo.toJson());
  }

}
