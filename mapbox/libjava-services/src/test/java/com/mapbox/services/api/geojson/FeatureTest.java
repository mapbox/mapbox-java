package com.mapbox.services.api.geojson;

import com.google.gson.JsonObject;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FeatureTest extends BaseGeoJSON {

  @Test
  public void fromJson() {
    Feature geo = Feature.fromJson(BaseGeoJSON.SAMPLE_FEATURE);
    assertEquals(geo.getType(), "Feature");
    assertEquals(geo.getGeometry().getType(), "Point");
    assertEquals(geo.getProperties().get("name").getAsString(), "Dinagat Islands");
  }

  @Test
  public void toJson() {
    Feature geo = Feature.fromJson(BaseGeoJSON.SAMPLE_FEATURE);
    compareJson(BaseGeoJSON.SAMPLE_FEATURE, geo.toJson());
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
