package com.mapbox.optimization.v1;

import com.mapbox.geojson.Point;
import com.mapbox.services.exceptions.ServicesException;

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

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    MapboxOptimization mapboxOptimization = MapboxOptimization.builder()
      .coordinate(Point.fromLngLat(1.0, 2.0))
      .coordinate(Point.fromLngLat(2.0, 2.0))
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(mapboxOptimization);
  }

  @Test
  public void build_doesThrowTwoCoordinateMinException() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("At least two coordinates must be provided with your"));
    MapboxOptimization.builder().build();
  }

  @Test
  public void build_doesThrowRequiredAccessTokenException() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Services requires setting a valid access token"));
    MapboxOptimization.builder()
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .build();
  }

  @Test
  public void build_doesThrowTooManyCoordinatesException() throws ServicesException {
    int total = 13;
    List<Point> points = new ArrayList<>();
    for (int i = 0; i < total; i++) {
      // Fake too many positions
      points.add(Point.fromLngLat(1.0, 1.0));
    }
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Maximum of 12 coordinates are allowed for this API"));
    MapboxOptimization.builder()
      .coordinates(points)
      .accessToken(ACCESS_TOKEN)
      .build();
  }

  @Test
  public void build_doesAddCoordinatesToUrl() throws Exception {
    MapboxOptimization client = MapboxOptimization.builder()
      .coordinate(Point.fromLngLat(1.23456, 1.23456))
      .coordinate(Point.fromLngLat(20.9876, 20.9876))

      .accessToken(ACCESS_TOKEN)
      .build();

    assertTrue(client.cloneCall().request().toString().contains("1.23456,1.23456;20.9876,20.9876"));
  }

  @Test
  public void testUserAgent() throws ServicesException, IOException {
    MapboxOptimization client = MapboxOptimization.builder()
      .clientAppName("APP")
      .accessToken(ACCESS_TOKEN)
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .coordinate(Point.fromLngLat(1.0, 1.0))
      .build();
    assertTrue(client.executeCall().raw().request().header("User-Agent").contains("APP"));
  }
}
