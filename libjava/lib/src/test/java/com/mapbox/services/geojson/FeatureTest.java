package com.mapbox.services.geojson;

import com.mapbox.services.commons.geojson.Feature;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

}
