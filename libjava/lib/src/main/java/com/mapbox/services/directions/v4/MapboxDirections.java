package com.mapbox.services.directions.v4;

import com.mapbox.services.Constants;
import com.mapbox.services.commons.MapboxBuilder;
import com.mapbox.services.commons.MapboxService;
import com.mapbox.services.commons.ServicesException;
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
import rx.Observable;

/**
 * Directions v4 are now deprecated in favor of v5
 */
@Deprecated
public class MapboxDirections implements MapboxService<DirectionsResponse> {

    private Builder builder = null;
    private DirectionsService service = null;
    private Call<DirectionsResponse> call = null;
    private Observable<DirectionsResponse> observable = null;

    public MapboxDirections(Builder builder) {
        this.builder = builder;
    }

    public DirectionsService getService() {
        // No need to recreate it
        if (service != null) return service;

        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service = retrofit.create(DirectionsService.class);
        return service;
    }

    public Call<DirectionsResponse> getCall() {
        // No need to recreate it
        if (call != null) return call;

        call = getService().getCall(
                builder.getProfile(),
                builder.getWaypoints(),
                builder.getAccessToken(),
                builder.isAlternatives(),
                builder.getInstructions(),
                builder.getGeometry(),
                builder.isSteps());

        return call;
    }

    @Override
    public Response<DirectionsResponse> executeCall() throws IOException {
        return getCall().execute();
    }

    @Override
    public void enqueueCall(Callback<DirectionsResponse> callback) {
        getCall().enqueue(callback);
    }

    @Override
    public void cancelCall() {
        getCall().cancel();
    }

    @Override
    public Call<DirectionsResponse> cloneCall() {
        return getCall().clone();
    }

    @Override
    public Observable<DirectionsResponse> getObservable() {
        // No need to recreate it
        if (observable != null) return observable;

        observable = service.getObservable(
                builder.profile,
                builder.getWaypoints(),
                builder.accessToken,
                builder.alternatives,
                builder.instructions,
                builder.geometry,
                builder.steps);

        return observable;
    }

    /*
     * Builder
     */

    public static class Builder extends MapboxBuilder {

        private String accessToken;
        private String profile;
        private List<Waypoint> waypoints;
        private Waypoint origin;
        private Waypoint destination;
        private Boolean alternatives;
        private String instructions;
        private String geometry;
        private Boolean steps;

        @Override
        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder setProfile(String profile) {
            this.profile = profile;
            return this;
        }

        /*
         * We offer some convenience for the typical case where we only have an origin
         * and a destination. Instead of having to create a List of waypoints, we just
         * call setOrigin() and setDestination() which is more meaningful. That's taken
         * into account in getWaypoints()
         */

        public Builder setWaypoints(List<Waypoint> waypoints) {
            this.waypoints = waypoints;
            return this;
        }

        public Builder setOrigin(Waypoint origin) {
            this.origin = origin;
            return this;
        }

        public Builder setDestination(Waypoint destination) {
            this.destination = destination;
            return this;
        }

        public Builder setAlternatives(Boolean alternatives) {
            this.alternatives = alternatives;
            return this;
        }

        public Builder setInstructions(String instructions) {
            this.instructions = instructions;
            return this;
        }

        public Builder setGeometry(String geometry) {
            this.geometry = geometry;
            return this;
        }

        public Builder setSteps(Boolean steps) {
            this.steps = steps;
            return this;
        }

        @Override
        public String getAccessToken() {
            return accessToken;
        }

        public String getProfile() {
            return profile;
        }

        public String getWaypoints() {
            String waypointsFormatted = "";

            // Set origin and destination
            if (origin != null && destination != null) {
                waypoints = new ArrayList<>(Arrays.asList(origin, destination));
            }

            // Empty list
            if (waypoints == null || waypoints.size() == 0) {
                return waypointsFormatted;
            }

            // Convert to {lon},{lat} coordinate pairs
            List<String> pieces = new ArrayList<>();
            for (Waypoint waypoint: waypoints) {
                pieces.add(String.format("%f,%f", waypoint.getLongitude(), waypoint.getLatitude()));
            }

            // The waypoints parameter should be a semicolon-separated list of locations to visit
            waypointsFormatted = StringUtils.join(pieces, ";");
            return waypointsFormatted;
        }

        public Waypoint getOrigin() {
            return origin;
        }

        public Waypoint getDestination() {
            return destination;
        }

        public Boolean isAlternatives() {
            return alternatives;
        }

        public String getInstructions() {
            return instructions;
        }

        public String getGeometry() {
            return geometry;
        }

        public Boolean isSteps() {
            return steps;
        }

        @Override
        public MapboxDirections build() throws ServicesException {
            validateAccessToken(accessToken);

            // We force the geometry to be a polyline to make the request more efficient.
            // We have utils to transform polylines into a LineString easily.
            geometry = DirectionsCriteria.GEOMETRY_POLYLINE;

            return new MapboxDirections(this);
        }

    }

}
