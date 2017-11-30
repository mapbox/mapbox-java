package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;

import org.junit.Test;

public class DirectionsRouteTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    DirectionsRoute route = DirectionsRoute.builder()
      .distance(100d)
      .build();
    assertNotNull(route);
  }
}
