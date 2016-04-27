package com.mapbox.services.directions.v5;

import com.mapbox.services.Constants;
import com.mapbox.services.commons.MapboxBuilder;
import com.mapbox.services.commons.MapboxService;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.models.Bearing;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;
import com.mapbox.services.directions.v5.models.DirectionsResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * The Directions API allows the calculation of routes between coordinates. The fastest route
 * is returned with geometries, and turn-by-turn instructions. The Mapbox Directions API supports
 * routing for driving cars, riding bicycles and walking.
 */
public class MapboxDirections implements MapboxService<DirectionsResponse> {

    private Builder builder = null;
    private DirectionsService service = null;
    private Call<DirectionsResponse> call = null;
    private Observable<DirectionsResponse> observable = null;

    // Allows testing
    private String baseUrl = Constants.BASE_API_URL;

    private MapboxDirections(Builder builder) {
        this.builder = builder;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private DirectionsService getService() {
        // No need to recreate it
        if (service != null) return service;

        // Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        // Directions service
        service = retrofit.create(DirectionsService.class);
        return service;
    }

    private Call<DirectionsResponse> getCall() {
        // No need to recreate it
        if (call != null) return call;

        call = getService().getCall(
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

        observable = getService().getObservable(
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
        return observable;
    }

    /*
     * Builder
     */

    public static class Builder extends MapboxBuilder {

        // We use `Boolean` instead of `boolean` to allow unset (null) values.
        private String user = null;
        private String profile = null;
        private ArrayList<Position> coordinates = null;
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

        /**
         * Set the list of coordinates for the directions service. If you've previously set an
         * origin with setOrigin() or a destination with setDestination(), those will be
         * overridden.
         *
         * @param coordinates List of {@link Position} giving origin and destination(s) coordinates.
         */
        public Builder setCoordinates(ArrayList<Position> coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        /**
         * Inserts the specified position at the beginning of the coordinates list. If you've
         * set other coordinates previously with setCoordinates() those elements are kept
         * and their index will be moved up by one (the coordinates are moved to the right).
         *
         * @param origin {@link Position} of route origin.
         */
        public Builder setOrigin(Position origin) {
            if (coordinates == null) {
                coordinates = new ArrayList<>();
            }

            // The default behavior of ArrayList is to inserts the specified element at the
            // specified position in this list (beginning) and to shift the element currently at
            // that position (if any) and any subsequent elements to the right (adds one to
            // their indices)
            coordinates.add(0, origin);

            return this;
        }

        /**
         * Appends the specified destination to the end of the coordinates list. If you've
         * set other coordinates previously with setCoordinates() those elements are kept
         * and the destination is added at the end of the list.
         *
         * @param destination {@link Position} of route destination.
         */
        public Builder setDestination(Position destination) {
            if (coordinates == null) {
                coordinates = new ArrayList<>();
            }

            // The default behavior for ArrayList is to appends the specified element
            // to the end of this list.
            coordinates.add(destination);

            return this;
        }

        @Override
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

        public String getProfile() {
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
        public String getCoordinates() {
            List<String> coordinatesFormatted = new ArrayList<>();
            for (Position coordinate: coordinates) {
                coordinatesFormatted.add(String.format(Locale.US, "%f,%f",
                        coordinate.getLongitude(),
                        coordinate.getLatitude()));
            }

            return TextUtils.join(";", coordinatesFormatted.toArray());
        }

        @Override
        public String getAccessToken() {
            return accessToken;
        }

        public Boolean isAlternative() {
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
        public String getBearings() {
            if (bearings == null || bearings.length == 0) return null;

            String[] bearingsFormatted = new String[bearings.length];
            for (int i = 0; i < bearings.length; i++) {
                bearingsFormatted[i] = String.format(Locale.US, "%d,%d",
                        bearings[i].getDirection(),
                        bearings[i].getRange());
            }

            return TextUtils.join(";", bearingsFormatted);
        }

        public String getGeometries() {
            return geometries;
        }

        public String getOverview() {
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
        public String getRadiuses() {
            if (radiuses == null || radiuses.length == 0) return null;

            String[] radiusesFormatted = new String[radiuses.length];
            for (int i = 0; i < radiuses.length; i++) {
                radiusesFormatted[i] = String.format(Locale.US, "%f", radiuses[i]);
            }

            return TextUtils.join(";", radiusesFormatted);
        }

        public Boolean isSteps() {
            return steps;
        }

        public Boolean isUturns() {
            return uturns;
        }

        /*
         * Build method
         */

        @Override
        public MapboxDirections build() throws ServicesException {
            validateAccessToken(accessToken);

            if (coordinates == null || coordinates.size() < 2) {
                throw new ServicesException(
                        "You should provide at least two coordinates (from/to).");
            }

            if (bearings != null && bearings.length != coordinates.size()) {
                throw new ServicesException(
                        "There must be as many bearings as there are coordinates.");
            }

            if (radiuses != null && radiuses.length != coordinates.size()) {
                throw new ServicesException(
                        "There must be as many radiuses as there are coordinates.");
            }

            return new MapboxDirections(this);
        }

    }

}
