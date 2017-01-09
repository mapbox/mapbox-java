package com.mapbox.services.commons.geojson;

import com.google.gson.JsonObject;
import com.mapbox.services.commons.models.Position;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FeatureTest extends BaseTest {

  private static final String SAMPLE_FEATURE_FIXTURE = "src/test/fixtures/sample-feature.json";

  @Test
  public void fromJson() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_FEATURE_FIXTURE)), Charset.forName("utf-8"));
    Feature geo = Feature.fromJson(geojson);
    assertEquals(geo.getType(), "Feature");
    assertEquals(geo.getGeometry().getType(), "Point");
    assertEquals(geo.getProperties().get("name").getAsString(), "Dinagat Islands");
  }

  @Test
  public void toJson() throws IOException {
    String geojson = new String(Files.readAllBytes(Paths.get(SAMPLE_FEATURE_FIXTURE)), Charset.forName("utf-8"));
    Feature geo = Feature.fromJson(geojson);
    compareJson(geojson, geo.toJson());
  }

  @Test
  public void testNullProperties() {
    List<Position> coordinates = Arrays.asList(Position.fromCoordinates(0.1, 2.3), Position.fromCoordinates(4.5, 6.7));
    LineString line = LineString.fromCoordinates(coordinates);
    Feature feature = Feature.fromGeometry(line);
    assertTrue(feature.toJson().contains("\"properties\":{}"));
  }

  @Test
  public void testNonNullProperties() {
    List<Position> coordinates = Arrays.asList(Position.fromCoordinates(0.1, 2.3), Position.fromCoordinates(4.5, 6.7));
    LineString line = LineString.fromCoordinates(coordinates);
    JsonObject properties = new JsonObject();
    properties.addProperty("key", "value");
    Feature feature = Feature.fromGeometry(line, properties);
    assertTrue(feature.toJson().contains("\"properties\":{\"key\":\"value\"}"));
  }

}
