package com.mapbox.samples;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.sample.BuildConfig;
import com.mapbox.turf.TurfSimplify;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BasicTurfSimplify {

    public static void main(String[] args) throws IOException {
        LineString complexLineString = generateLineString();
        LineString simplifiedLineString = LineString.fromLngLats(
          TurfSimplify.simplify(complexLineString.coordinates(), 0.001, true),
          complexLineString.bbox()
        );
        System.out.println("[Turf] complex: " + complexLineString.coordinates());
        System.out.println("[Turf] simplified: " + simplifiedLineString.coordinates());
    }

    private static LineString generateLineString() throws IOException {
        MapboxDirections.Builder builder = MapboxDirections.builder();

        // 1. Pass in all the required information to get a simple directions route.
        List<Point> coordinates = new ArrayList<>();
        coordinates.add(Point.fromLngLat(-95.6332, 29.7890));
        coordinates.add(Point.fromLngLat(-95.3591, 29.7576));
        RouteOptions routeOptions = RouteOptions.builder()
                .coordinatesList(coordinates)
                .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
                .build();
        builder.routeOptions(routeOptions);
        builder.accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN);

        // 2. That's it! Now execute the command and get the response.
        Response<DirectionsResponse> response = builder.build().executeCall();
        return LineString.fromPolyline(response.body().routes().get(0).geometry(), Constants.PRECISION_6);
    }
}
