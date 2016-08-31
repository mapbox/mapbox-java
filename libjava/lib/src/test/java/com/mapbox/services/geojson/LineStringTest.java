package com.mapbox.services.geojson;

import com.mapbox.services.commons.geojson.LineString;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LineStringTest extends BaseGeoJSON {

  @Test
  public void fromJson() {
    LineString geo = LineString.fromJson(BaseGeoJSON.SAMPLE_LINESTRING);
    assertEquals(geo.getType(), "LineString");
    assertEquals(geo.getCoordinates().get(0).getLongitude(), 100.0, 0.0);
    assertEquals(geo.getCoordinates().get(0).getLatitude(), 0.0, 0.0);
    assertFalse(geo.getCoordinates().get(0).hasAltitude());
  }

  @Test
  public void toJson() {
    LineString geo = LineString.fromJson(BaseGeoJSON.SAMPLE_LINESTRING);
    compareJson(BaseGeoJSON.SAMPLE_LINESTRING, geo.toJson());
  }

}
