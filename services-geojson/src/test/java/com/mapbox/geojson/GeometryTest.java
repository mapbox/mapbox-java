package com.mapbox.geojson;

import com.mapbox.core.TestUtils;
import com.mapbox.geojson.gson.GeometryGeoJson;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GeometryTest extends TestUtils {

  private static final String SAMPLE_GEOMETRY_COLLECTION = "sample-geometrycollection.json";

  @Test
  public void fromJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_GEOMETRY_COLLECTION);
    Geometry geo = GeometryGeoJson.fromJson(json);
    assertEquals(geo.type(), "GeometryCollection");
  }
}
