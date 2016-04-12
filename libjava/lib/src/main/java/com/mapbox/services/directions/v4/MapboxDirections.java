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
 * @deprecated Directions v4 are now deprecated in favor of v5.
 */
@Deprecated
public class MapboxDirections implements MapboxService<DirectionsResponse> {

    private Builder builder = null;
    private DirectionsService service = null;
    private Call<DirectionsResponse> call = null;
    private Observable<DirectionsResponse> observable = null;

    // Allows testing
    private String baseUrl = Constants.BASE_API_URL;

    public MapboxDirections(Builder builder) {
        this.builder = builder;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public DirectionsService getService() {
        // No need to recreate it
        if (service != null) return service;

        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(baseUrl)
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

        /**
         * Required to call when building {@link com.mapbox.services.directions.v4.MapboxDirections.Builder}.
         *
         * @param accessToken Mapbox access token, You must have a Mapbox account inorder to use
         *                    this library.
         */
        @Override
        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        /**
         * Specify what routing profile you'd like: driving, walking, or cycling.
         *
         * @param profile {@link DirectionsCriteria#PROFILE_DRIVING},
         * {@link DirectionsCriteria#PROFILE_CYCLING}, or {@link DirectionsCriteria#PROFILE_WALKING}
         */
        public Builder setProfile(String profile) {
            this.profile = profile;
            return this;
        }

        /**
         * If you have more then one destination, call this method passing in a list of all
         * waypoints including origin and final destination.
         *
         * @param waypoints List including all {@link Waypoint} you'd line to include in route.
         */
        public Builder setWaypoints(List<Waypoint> waypoints) {
            this.waypoints = waypoints;
            return this;
        }

        /**
         * Origin of the destination.
         *
         * @param origin {@link Waypoint} of origin.
         */
        public Builder setOrigin(Waypoint origin) {
            this.origin = origin;
            return this;
        }

        /**
         * Final destination of your route, call this only if you have one destination, otherwise,
         * use {@link #setWaypoints(List)}
         *
         * @param destination {@link Waypoint} of destination.
         */
        public Builder setDestination(Waypoint destination) {
            this.destination = destination;
            return this;
        }

        /**
         * Optionally, call if you'd like to receive alternative routes besides just one.
         *
         * @param alternatives true if you'd like alternative routes, else false.
         */
        public Builder setAlternatives(Boolean alternatives) {
            this.alternatives = alternatives;
            return this;
        }

        /**
         * Optionally, call if you'd like to receive human-readable instructions.
         *
         * @param instructions {@link DirectionsCriteria#INSTRUCTIONS_TEXT} or
         * {@link DirectionsCriteria#INSTRUCTIONS_HTML}
         */
        public Builder setInstructions(String instructions) {
            this.instructions = instructions;
            return this;
        }

        /**
         * Optionally, call with the format you'd like the route geometry to be in.
         *
         * @param geometry {@link DirectionsCriteria#GEOMETRY_GEOJSON},
         * {@link DirectionsCriteria#GEOMETRY_POLYLINE}, or {@link DirectionsCriteria#GEOMETRY_FALSE}
         */
        public Builder setGeometry(String geometry) {
            this.geometry = geometry;
            return this;
        }

        /**
         * Optionally, call if you'd like to include step information within route.
         *
         * @param steps true if you'd like step information.
         */
        public Builder setSteps(Boolean steps) {
            this.steps = steps;
            return this;
        }

        /**
         * @return your Mapbox access token.
         */
        @Override
        public String getAccessToken() {
            return accessToken;
        }

        /**
         * @return {@link DirectionsCriteria#PROFILE_DRIVING},
         * {@link DirectionsCriteria#PROFILE_CYCLING}, or {@link DirectionsCriteria#PROFILE_WALKING}
         */
        public String getProfile() {
            return profile;
        }

        /**
         * @return List including all {@link Waypoint} within route.
         */
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

        /**
         * @return routes origin {@link Waypoint}.
         */
        public Waypoint getOrigin() {
            return origin;
        }

        /**
         * @return routes final destination {@link Waypoint}.
         */
        public Waypoint getDestination() {
            return destination;
        }

        /**
         * @return true if you {@link #setAlternatives(Boolean)} to true.
         */
        public Boolean isAlternatives() {
            return alternatives;
        }

        /**
         * @return {@link DirectionsCriteria#INSTRUCTIONS_TEXT} or
         * {@link DirectionsCriteria#INSTRUCTIONS_HTML}
         */
        public String getInstructions() {
            return instructions;
        }

        /**
         * @return {@link DirectionsCriteria#GEOMETRY_GEOJSON},
         * {@link DirectionsCriteria#GEOMETRY_POLYLINE}, or {@link DirectionsCriteria#GEOMETRY_FALSE}
         */
        public String getGeometry() {
            return geometry;
        }

        /**
         * @return true if you requested step information in {@link #setSteps(Boolean)}.
         */
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
