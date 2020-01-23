package com.mapbox.api.directions.v5.models;

import com.mapbox.geojson.Point;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RouteOptionsTest {

    @Test
    public void toBuilder() {
        RouteOptions routeOptions = routeOptions();

        String language = "ru";
        String url = "new_base_url";

        RouteOptions updatedOptions = routeOptions.toBuilder()
                .language(language)
                .baseUrl(url)
                .build();

        assertEquals(language, updatedOptions.language());
        assertEquals(url, updatedOptions.baseUrl());
        assertEquals(routeOptions.accessToken(), updatedOptions.accessToken());
        assertEquals(routeOptions.coordinates(), updatedOptions.coordinates());
        assertEquals(routeOptions.user(), updatedOptions.user());
        assertEquals(routeOptions.profile(), updatedOptions.profile());
        assertEquals(routeOptions.geometries(), updatedOptions.geometries());
        assertEquals(routeOptions.requestUuid(), updatedOptions.requestUuid());
    }

    private RouteOptions routeOptions() {
        List<Point> coordinates = new ArrayList<>();
        coordinates.add(Point.fromLngLat(1.0, 2.0));
        coordinates.add(Point.fromLngLat(3.0, 4.0));

        return RouteOptions.builder()
                .accessToken("token")
                .baseUrl("base_url")
                .language("en")
                .coordinates(coordinates)
                .user("user")
                .profile("profile")
                .geometries("geometries")
                .requestUuid("requestUuid")
                .build();
    }
}
