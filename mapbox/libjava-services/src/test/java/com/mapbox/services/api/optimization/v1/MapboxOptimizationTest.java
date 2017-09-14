package com.mapbox.services.api.optimization.v1;

import com.mapbox.services.api.BaseTest;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.commons.geojson.Point;
import org.hamcrest.junit.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class MapboxOptimizationTest extends BaseTest {
//
//  @Rule
//  public ExpectedException thrown = ExpectedException.none();
//
//  @Test
//  public void sanityTest() throws Exception {
//    MapboxOptimization mapboxOptimization = MapboxOptimization.builder()
//      .coordinate(new Point(1.0, 2.0))
//      .coordinate(new Point(2.0, 2.0))
//      .accessToken("pk.XXX")
//      .build();
//    assertNotNull(mapboxOptimization);
//  }
//
//  @Test
//  public void build_doesThrowTwoCoordinateMinException() throws Exception {
//    thrown.expect(ServicesException.class);
//    thrown.expectMessage(startsWith("At least two coordinates must be provided with your"));
//    MapboxOptimization.builder().build();
//  }
//
//  @Test
//  public void build_doesThrowRequiredAccessTokenException() throws ServicesException {
//    thrown.expect(ServicesException.class);
//    thrown.expectMessage(startsWith("Using Mapbox Services requires setting a valid access token"));
//    MapboxOptimization.builder()
//      .coordinate(new Point(1.0, 1.0))
//      .coordinate(new Point(1.0, 1.0))
//      .build();
//  }
//
//  @Test
//  public void build_doesThrowTooManyCoordinatesException() throws ServicesException {
//    int total = 13;
//    List<Point> points = new ArrayList<>();
//    for (int i = 0; i < total; i++) {
//      // Fake too many positions
//      points.add(new Point(1.0, 1.0));
//    }
//    thrown.expect(ServicesException.class);
//    thrown.expectMessage(startsWith("Maximum of 12 coordinates are allowed for this API"));
//    MapboxOptimization.builder()
//      .coordinates(points)
//      .accessToken("pk.XXX")
//      .build();
//  }
//
//  @Test
//  public void build_doesAddCoordinatesToUrl() throws Exception {
//    MapboxOptimization client = MapboxOptimization.builder()
//      .coordinate(new Point(1.23456, 1.23456))
//      .coordinate(new Point(20.9876, 20.9876))
//
//      .accessToken("pk.XXX")
//      .build();
//
//    assertTrue(client.cloneCall().request().toString().contains("1.23456,1.23456;20.9876,20.9876"));
//  }
//
//  @Test
//  public void testUserAgent() throws ServicesException, IOException {
//    MapboxOptimization client = MapboxOptimization.builder()
//      .clientAppName("APP")
//      .accessToken("pk.XXX")
//      .coordinate(new Point(1.0, 1.0))
//      .coordinate(new Point(1.0, 1.0))
//      .build();
//    assertTrue(client.executeCall().raw().request().header("User-Agent").contains("APP"));
//  }
}
