package com.mapbox.services.directions.v5;

import com.mapbox.services.Constants;
import com.mapbox.services.directions.shared.DirectionsException;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.commons.models.Bearing;
import com.mapbox.services.commons.models.Position;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The Directions API allows the calculation of routes between coordinates. The fastest route
 * is returned with geometries, and turn-by-turn instructions. The Mapbox Directions API supports
 * routing for driving cars, riding bicycles and walking.
 */
public class MapboxDirections {

    private Builder builder;
    private Call<DirectionsResponse> call;

    private MapboxDirections(Builder builder) {
        this.builder = builder;
    }

    public Response<DirectionsResponse> execute() throws IOException {
        return getCall().execute();
    }

    public void cancel() {
        getCall().cancel();
    }

    public void enqueue(Callback<DirectionsResponse> callback) {
        getCall().enqueue(callback);
    }

    private Call<DirectionsResponse> getCall() {
        if (call != null) return call;

        // Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        // Directions service
        DirectionsService service = retrofit.create(DirectionsService.class);

        // Call
        call = service.get(
                builder.getUser(),
                builder.getProfile(),
                builder.getCoordinates(),
                builder.getAccessToken(),
                builder.isAlternative(),
                builder.getBearings(),
                builder.getGeometries(),
                builder.getOverview(),
                builder.getRadiuses(),
                builder.isSteps(),
                builder.isUturns());

        // Done
        return call;
    }

    /*
     * Builder
     */

    public static class Builder {

        // We use `Boolean` instead of `boolean` to allow unset (null) values.
        private String user = null;
        private String profile = null;
        private Position[] coordinates = null;
        private String accessToken = null;
        private Boolean alternative = null;
        private Bearing[] bearings = null;
        private String geometries = null;
        private String overview = null;
        private double[] radiuses = null;
        private Boolean steps = null;
        private Boolean uturns = null;

        /*
         * Constructor
         */

        public Builder() {
            // Set defaults
            this.user = DirectionsCriteria.PROFILE_DEFAULT_USER;

            // We only support polyline encoded geometries to reduce the size of the response.
            // If we need the corresponding LineString object, this SDK can do the decoding with
            // LineString.fromPolyline(String polyline, int precision).
            this.geometries = DirectionsCriteria.GEOMETRY_POLYLINE;
        }

        /*
         * Setters
         */

        public Builder setUser(String user) {
            this.user = user;
            return this;
        }

        public Builder setProfile(String profile) {
            this.profile = profile;
            return this;
        }

        public Builder setCoordinates(Position[] coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder setAlternative(Boolean alternative) {
            this.alternative = alternative;
            return this;
        }

        public Builder setBearings(Bearing[] bearings) {
            this.bearings = bearings;
            return this;
        }

        public Builder setOverview(String overview) {
            this.overview = overview;
            return this;
        }

        public Builder setRadiuses(double[] radiuses) {
            this.radiuses = radiuses;
            return this;
        }

        public Builder setSteps(Boolean steps) {
            this.steps = steps;
            return this;
        }

        public Builder setUturns(Boolean uturns) {
            this.uturns = uturns;
            return this;
        }

        /*
         * Getters, they return the value in a format ready for the API to consume
         */

        public String getUser() {
            return user;
        }

        private String getProfile() {
            return profile;
        }

        /*
         * The coordinates parameter denotes between which points routing happens. The coordinates
         * must be in the format:
         *
         *    {longitude},{latitude};{longitude},{latitude}[;{longitude},{latitude} ...]
         *
         * - Each coordinate is a pair of a longitude float and latitude float, which are separated by a ,
         * - Coordinates are separated by a ; from each other
         * - A query must at minimum have 2 coordinates and may at maximum have 25 coordinates
         */
        private String getCoordinates() {
            String[] coordinatesFormatted = new String[coordinates.length];
            for (int i = 0; i < coordinates.length; i++) {
                coordinatesFormatted[i] = String.format("%f,%f",
                        coordinates[i].getLongitude(),
                        coordinates[i].getLatitude());
            }

            return StringUtils.join(coordinatesFormatted, ";");
        }

        private String getAccessToken() {
            return accessToken;
        }

        private Boolean isAlternative() {
            return alternative;
        }

        /*
         * Bearings indicate the allowed direction of travel through a coordinate. They are
         * indicated as a query parameter:
         *
         *    ?bearings={direction},{range};{direction},{range}[;{direction},{range} ...]
         *
         * - Each bearing consists of direction and range, which are separated by a ,
         * - There must be as many bearings as there are coordinates
         * - It is possible to have empty bearings via ;;, which allow all directions
         */
        private String getBearings() {
            if (bearings == null || bearings.length == 0) return null;

            String[] bearingsFormatted = new String[bearings.length];
            for (int i = 0; i < bearings.length; i++) {
                bearingsFormatted[i] = String.format("%d,%d",
                        bearings[i].getDirection(),
                        bearings[i].getRange());
            }

            return StringUtils.join(bearingsFormatted, ";");
        }

        private String getGeometries() {
            return geometries;
        }

        private String getOverview() {
            return overview;
        }

        /*
         * Radiuses indicate how far from a coordinate a routeable way is searched. They
         * are indicated like this:
         *
         *    ?radiuses={radius};{radius}}[;{radius} ...].
         *
         * If no routeble way can be found within the serach radius, a NoRoute error will be returned.
         * - Radiuses are separated by a ,
         * - Each radius must be of a value float >= 0 in meters or unlimited (default)
         * - There must be as many radiuses as there are coordinates
         * - It is possible to not specify radiuses via ;;, which result in the same behaviour as setting unlimited
         */
        private String getRadiuses() {
            if (radiuses == null || radiuses.length == 0) return null;

            String[] radiusesFormatted = new String[radiuses.length];
            for (int i = 0; i < radiuses.length; i++) {
                radiusesFormatted[i] = String.format("%f", radiuses[i]);
            }

            return StringUtils.join(radiusesFormatted, ";");
        }

        private Boolean isSteps() {
            return steps;
        }

        private Boolean isUturns() {
            return uturns;
        }

        /*
         * Build method
         */

        public MapboxDirections build() throws DirectionsException {
            if (coordinates == null || coordinates.length < 2) {
                throw new DirectionsException(
                        "You should provide at least two coordinates (from/to).");
            }

            if (bearings != null && bearings.length != coordinates.length) {
                throw new DirectionsException(
                        "There must be as many bearings as there are coordinates.");
            }

            if (radiuses != null && radiuses.length != coordinates.length) {
                throw new DirectionsException(
                        "There must be as many radiuses as there are coordinates.");
            }

            return new MapboxDirections(this);
        }

    }

}
