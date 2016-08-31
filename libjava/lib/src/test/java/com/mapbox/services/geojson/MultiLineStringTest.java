package com.mapbox.services.geojson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MultiLineStringTest extends BaseGeoJSON {

  @Test
  public void fromJson() {
    com.mapbox.services.commons.geojson.MultiLineString geo = com.mapbox.services.commons.geojson.MultiLineString.fromJson(BaseGeoJSON.SAMPLE_MULTILINESTRING);
    assertEquals(geo.getType(), "MultiLineString");
    assertEquals(geo.getCoordinates().get(0).get(0).getLongitude(), 100.0, 0.0);
    assertEquals(geo.getCoordinates().get(0).get(0).getLatitude(), 0.0, 0.0);
    assertFalse(geo.getCoordinates().get(0).get(0).hasAltitude());
  }

  @Test
  public void toJson() {
    com.mapbox.services.commons.geojson.MultiLineString geo = com.mapbox.services.commons.geojson.MultiLineString.fromJson(BaseGeoJSON.SAMPLE_MULTILINESTRING);
    compareJson(BaseGeoJSON.SAMPLE_MULTILINESTRING, geo.toJson());
  }

}
