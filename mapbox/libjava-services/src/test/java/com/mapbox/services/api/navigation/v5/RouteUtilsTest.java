package com.mapbox.services.api.navigation.v5;

import com.google.gson.Gson;
import com.mapbox.services.api.BaseTest;
import com.mapbox.services.Constants;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.navigation.v5.RouteUtils;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.api.utils.turf.TurfConstants;
import com.mapbox.services.api.utils.turf.TurfException;
import com.mapbox.services.api.utils.turf.TurfMeasurement;
import com.mapbox.services.commons.utils.PolylineUtils;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.LegStep;
import com.mapbox.services.api.directions.v5.models.RouteLeg;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RouteUtilsTest extends BaseTest {

  private DirectionsResponse response;
  private RouteLeg route;

  @Before
  public void setUp() throws IOException {
    Gson gson = new Gson();
    byte[] content = Files.readAllBytes(Paths.get("src/test/fixtures/directions_v5.json"));
    String body = new String(content, StandardCharsets.UTF_8);
    response = gson.fromJson(body, DirectionsResponse.class);
    route = response.getRoutes().get(0).getLegs().get(0);
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();


  @Test
  public void isInStepTest() throws ServicesException, TurfException {
    RouteUtils routeUtils = new RouteUtils();

    // For each step, the first coordinate is in the step
    for (int stepIndex = 0; stepIndex < route.getSteps().size(); stepIndex++) {
      LegStep step = route.getSteps().get(stepIndex);
      List<Position> coords = PolylineUtils.decode(step.getGeometry(), Constants.OSRM_PRECISION_V5);
      assertTrue(routeUtils.isInStep(coords.get(0), route, stepIndex));
    }
  }

  @Test
  public void getDistanceToStepTest() throws ServicesException, TurfException {
    RouteUtils routeUtils = new RouteUtils();

    // For each step, the distance to the first coordinate is zero
    for (int stepIndex = 0; stepIndex < route.getSteps().size(); stepIndex++) {
      LegStep step = route.getSteps().get(stepIndex);
      List<Position> coords = PolylineUtils.decode(step.getGeometry(), Constants.OSRM_PRECISION_V5);
      assertEquals(0.0, routeUtils.getDistanceToStep(coords.get(0), route, stepIndex), DELTA);
    }
  }

  @Test
  public void getSnapToRouteTest() throws ServicesException, TurfException {
    RouteUtils routeUtils = new RouteUtils();

    // For each step, the first coordinate snap point is the same point
    for (int stepIndex = 0; stepIndex < route.getSteps().size(); stepIndex++) {
      LegStep step = route.getSteps().get(stepIndex);
      List<Position> coords = PolylineUtils.decode(step.getGeometry(), Constants.OSRM_PRECISION_V5);
      Position snapPoint = routeUtils.getSnapToRoute(coords.get(0), route, stepIndex);
      assertEquals(coords.get(0).getLatitude(), snapPoint.getLatitude(), DELTA);
      assertEquals(coords.get(0).getLongitude(), snapPoint.getLongitude(), DELTA);
    }
  }

  @Test
  public void isOffRouteTest() throws ServicesException, TurfException {
    RouteUtils routeUtils = new RouteUtils();

    // For each step, the first coordinate is not off-route
    for (int stepIndex = 0; stepIndex < route.getSteps().size(); stepIndex++) {
      LegStep step = route.getSteps().get(stepIndex);
      List<Position> coords = PolylineUtils.decode(step.getGeometry(), Constants.OSRM_PRECISION_V5);
      assertFalse(routeUtils.isOffRoute(coords.get(0), route));
    }

    // The route goes from SF (N) to San Jose (S). So 0.1 km south of the last point should
    // be off-route
    LegStep lastStep = route.getSteps().get(route.getSteps().size() - 1);
    List<Position> lastCoords = PolylineUtils.decode(lastStep.getGeometry(), Constants.OSRM_PRECISION_V5);
    Position offRoutePoint = TurfMeasurement.destination(lastCoords.get(0), routeUtils.getOffRouteThresholdKm(), 180, TurfConstants.UNIT_DEFAULT);
    assertTrue(routeUtils.isOffRoute(offRoutePoint, route));
  }

  @Test
  public void getClosestStepTest() throws ServicesException, TurfException {
    RouteUtils routeUtils = new RouteUtils();

    // For each step, the first coordinate is closest to its step
    for (int stepIndex = 0; stepIndex < route.getSteps().size(); stepIndex++) {
      LegStep step = route.getSteps().get(stepIndex);
      List<Position> coords = PolylineUtils.decode(step.getGeometry(), Constants.OSRM_PRECISION_V5);
      assertEquals(stepIndex, routeUtils.getClosestStep(coords.get(0), route));
    }
  }
}
