package com.mapbox.services.api.geocoding.v5;

import com.google.gson.JsonObject;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class CarmenFeatureTest {

  private static final double DELTA = 1E-10;
  private static final String GEOCODING_FIXTURE = "src/test/fixtures/geocoder_tofromjson.json";

  @Test
  public void testAllowStandalone() {
    // Build the standalone object
    double[] bbox = new double[] {1.2, 3.4, 5.6, 7.8};
    double[] center = new double[] {1.2, 3.4};
    double relevance = 1.2;
    JsonObject properties = new JsonObject();
    properties.addProperty("foo1", "bar");
    CarmenFeature feature = new CarmenFeature();

    // Type specific
    feature.setText("Text field 1");
    feature.setPlaceName("Text field 2");
    feature.setBbox(bbox);
    feature.setAddress("Text field 3");
    feature.setCenter(center);
    feature.setContext(null);
    feature.setRelevance(relevance);

    // Inherited
    feature.setGeometry(null);
    feature.setProperties(properties);
    feature.setId("Text field 4");

    // Checks
    assertEquals(feature.getText(), "Text field 1");
    assertEquals(feature.getPlaceName(), "Text field 2");
    assertEquals(feature.getBbox(), bbox);
    assertEquals(feature.getAddress(), "Text field 3");
    assertEquals(feature.getCenter(), center);
    assertEquals(feature.getContext(), null);
    assertEquals(feature.getRelevance(), relevance, DELTA);
    assertEquals(feature.getGeometry(), null);
    assertEquals(feature.getProperties(), properties);
    assertEquals(feature.getStringProperty("foo1"), "bar");
    assertEquals(feature.hasNonNullValueForProperty("foo1"), true);
    assertEquals(feature.hasNonNullValueForProperty("foo2"), false);
    assertEquals(feature.getId(), "Text field 4");
  }

  @Test
  public void carmenFeatureFromJson() throws IOException {
    // Build the standalone object
    double[] bbox = new double[] {1.2, 3.4, 5.6, 7.8};
    double[] center = new double[] {1.2, 3.4};
    double relevance = 1.2;
    JsonObject properties = new JsonObject();
    properties.addProperty("foo1", "bar");
    CarmenFeature feature = new CarmenFeature();

    // Type specific
    feature.setText("Text field 1");
    feature.setPlaceName("Text field 2");
    feature.setBbox(bbox);
    feature.setAddress("Text field 3");
    feature.setCenter(center);
    feature.setContext(null);
    feature.setRelevance(relevance);

    // Inherited
    feature.setGeometry(null);
    feature.setProperties(properties);
    feature.setId("Text field 4");

    String body = new String(Files.readAllBytes(Paths.get(GEOCODING_FIXTURE)), Charset.forName("utf-8"));
    CarmenFeature carmenFeature = CarmenFeature.fromJson(body);
    assertEquals(carmenFeature.getText(), feature.getText());
  }
}
