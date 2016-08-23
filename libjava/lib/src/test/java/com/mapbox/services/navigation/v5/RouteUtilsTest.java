package com.mapbox.services.navigation.v5;

import com.google.gson.Gson;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.RouteLeg;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by antonio on 8/23/16.
 */
public class RouteUtilsTest {

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
    public void isInStepTest() {
    }

    @Test
    public void getDistanceToStepTest() {

    }

    @Test
    public void getSnapToRouteTest() {

    }

    @Test
    public void isOffRouteTest() {

    }

    @Test
    public void getClosestStepTest() {

    }
}
