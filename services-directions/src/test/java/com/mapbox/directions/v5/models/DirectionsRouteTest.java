package com.mapbox.directions.v5.models;

import com.mapbox.directions.v5.BaseTest;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class DirectionsRouteTest extends BaseTest {

  @Test
  public void sanity() throws Exception {
    DirectionsRoute route = DirectionsRoute.builder()
      .distance(100d)
      .build();
    assertNotNull(route);
  }
}
