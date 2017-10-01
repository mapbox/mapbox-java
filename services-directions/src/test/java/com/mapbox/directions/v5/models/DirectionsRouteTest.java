package com.mapbox.directions.v5.models;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

import com.mapbox.services.TestUtils;

public class DirectionsRouteTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    DirectionsRoute route = DirectionsRoute.builder()
      .distance(100d)
      .build();
    assertNotNull(route);
  }
}
