package com.mapbox.services.directions.v4;

import com.mapbox.services.Constants;
import com.mapbox.services.directions.v4.models.DirectionsResponse;
import com.mapbox.services.directions.v4.models.Waypoint;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Directions v4 are now deprecated in favor of v5
 */
@Deprecated
public class MapboxDirections {

    private final Call<DirectionsResponse> _call;

    public MapboxDirections(Builder builder) {
        com.mapbox.services.directions.v4.DirectionsService service = getService();
        _call = service.get(
                builder._profile,
                builder.getWaypointsFormatted(),
                builder._accessToken,
                builder._alternatives,
                builder._instructions,
                builder._geometry,
                builder._steps);
    }

    /*
     * Retrofit API
     */

    public Response<DirectionsResponse> execute() throws IOException {
        return _call.execute();
    }

    public void enqueue(Callback<DirectionsResponse> callback) {
        _call.enqueue(callback);
    }

    public void cancel() {
        _call.cancel();
    }

    public Call<DirectionsResponse> clone() {
        return _call.clone();
    }

    private com.mapbox.services.directions.v4.DirectionsService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        com.mapbox.services.directions.v4.DirectionsService service = retrofit.create(com.mapbox.services.directions.v4.DirectionsService.class);
        return service;
    }

    /*
     * Builder
     */

    public static class Builder {

        private String _accessToken;
        private String _profile;
        private List<Waypoint> _waypoints;
        private Waypoint _origin;
        private Waypoint _destination;
        private boolean _alternatives;
        private String _instructions;
        private String _geometry;
        private boolean _steps;

        public Builder setAccessToken(String accessToken) {
            _accessToken = accessToken;
            return this;
        }

        public Builder setProfile(String profile) {
            _profile = profile;
            return this;
        }

        /*
         * We offer some convenience for the typical case where we only have an origin
         * and a destination. Instead of having to create a List of waypoints, we just
         * call setOrigin() and setDestination() which is more meaningful. That's taken
         * into account in getWaypointsFormatted()
         */

        public Builder setWaypoints(List<Waypoint> waypoints) {
            _waypoints = waypoints;
            return this;
        }

        public Builder setOrigin(Waypoint origin) {
            _origin = origin;
            return this;
        }

        public Builder setDestination(Waypoint destination) {
            _destination = destination;
            return this;
        }

        public String getWaypointsFormatted() {
            String waypointsFormatted = "";

            // Set origin and destination
            if (_origin != null && _destination != null) {
                _waypoints = new ArrayList<>(Arrays.asList(_origin, _destination));
            }

            // Empty list
            if (_waypoints == null || _waypoints.size() == 0) {
                return waypointsFormatted;
            }

            // Convert to {lon},{lat} coordinate pairs
            List<String> pieces = new ArrayList<>();
            for (Waypoint waypoint: _waypoints) {
                pieces.add(String.format("%f,%f", waypoint.getLongitude(), waypoint.getLatitude()));
            }

            // The waypoints parameter should be a semicolon-separated list of locations to visit
            waypointsFormatted = StringUtils.join(pieces, ";");
            return waypointsFormatted;
        }

        public Builder setAlternatives(boolean alternatives) {
            _alternatives = alternatives;
            return this;
        }

        public Builder setInstructions(String instructions) {
            _instructions = instructions;
            return this;
        }

        public Builder setSteps(boolean steps) {
            _steps = steps;
            return this;
        }

        // Checks if the given token is valid
        private void validateAccessToken(String accessToken) {
            if (StringUtils.isEmpty(accessToken) || (!accessToken.startsWith("pk.") && !accessToken.startsWith("sk."))) {
                throw new RuntimeException("Using the Mapbox Directions API requires setting a valid access token.");
            }
        }

        public MapboxDirections build() {
            // We force the geometry to be a polyline to make the request more efficient.
            // We have utils to transform polylines into a LineString easily.
            _geometry = com.mapbox.services.directions.v4.DirectionsCriteria.GEOMETRY_POLYLINE;

            validateAccessToken(_accessToken);
            return new MapboxDirections(this);
        }

    }

}
