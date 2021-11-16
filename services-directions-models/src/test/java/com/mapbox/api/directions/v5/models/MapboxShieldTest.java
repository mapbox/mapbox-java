package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapboxShieldTest  extends TestUtils {

  @Test
  public void testToFromJsonRouteShield() {
    MapboxShield shield = MapboxShield
        .builder()
        .baseUrl("https://api.mapbox.com/styles/v1/")
        .displayRef("242")
        .name("us-interstate")
        .textColor("black")
        .build();
    String jsonString = shield.toJson();
    MapboxShield mapboxShieldFromJson = MapboxShield.fromJson(jsonString);

    assertEquals(shield, mapboxShieldFromJson);
  }
}
