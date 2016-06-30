package com.mapbox.services.mapmatching.v4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.services.Constants;
import com.mapbox.services.commons.MapboxBuilder;
import com.mapbox.services.commons.MapboxService;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.directions.v4.DirectionsCriteria;
import com.mapbox.services.mapmatching.v4.gson.MapMatchingGeometryDeserializer;
import com.mapbox.services.mapmatching.v4.models.MapMatchingResponse;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * The Mapbox map matching interface (v4)
 */
public class MapboxMapMatching implements MapboxService<MapMatchingResponse> {

    private Builder builder = null;
    private MapMatchingService service = null;
    private Call<MapMatchingResponse> call = null;
    private Observable<MapMatchingResponse> observable = null;

    // Allows testing
    private String baseUrl = Constants.BASE_API_URL;

    private MapboxMapMatching(Builder builder) {
        this.builder = builder;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private MapMatchingService getService() {
        // No need to recreate it
        if (service != null) {
            return service;
        }

        // Gson instance with type adapters
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Geometry.class, new MapMatchingGeometryDeserializer())
                .create();

        // Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        // MapMatching service
        service = retrofit.create(MapMatchingService.class);
        return service;
    }

    public Call<MapMatchingResponse> getCall() {
        // No need to recreate it
        if (call != null) return call;

        call = getService().getCall(
                builder.getProfile(),
                builder.getGeometry(),
                builder.getGpsPrecison()
        );

        return call;
    }

    @Override
    public Response<MapMatchingResponse> executeCall() throws IOException {
        return getCall().execute();
    }

    @Override
    public void enqueueCall(Callback<MapMatchingResponse> callback) {
        getCall().enqueue(callback);
    }

    @Override
    public void cancelCall() {
        getCall().cancel();
    }

    @Override
    public Call<MapMatchingResponse> cloneCall() {
        return getCall().clone();
    }

    @Override
    public Observable<MapMatchingResponse> getObservable() {
        // No need to recreate it
        if (observable != null) return observable;

        observable = getService().getObservable(
                builder.getProfile(),
                builder.getGeometry(),
                builder.getGpsPrecison()
        );

        return observable;
    }

    public static class Builder extends MapboxBuilder {

        private String accessToken;
        private String profile;
        private String geometry;
        private Integer gpsPrecison;

        public Builder() {
            // Use polyline by default as the return format
            this.geometry = DirectionsCriteria.GEOMETRY_POLYLINE;
        }

        @Override
        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        @Override
        public String getAccessToken() {
            return this.accessToken;
        }

        /**
         * Set a map matching profile. You should use one of the constants in Directions v4
         * com.mapbox.services.directions.v4.DirectionsCriteria
         *
         * @param profile on of DirectionsCriteria#PROFILE_*
         */
        public Builder setProfile(String profile) {
            this.profile = profile;
            return this;
        }

        public String getProfile() {
            return profile;
        }

        public String getGeometry() {
            return geometry;
        }

        public Builder setNoGeometry() {
            this.geometry = DirectionsCriteria.GEOMETRY_FALSE;
            return this;
        }

        public Integer getGpsPrecison() {
            return gpsPrecison;
        }

        /**
         * @param gpsPrecison Assumed accuracy of the tracking device in meters (1-10 inclusive, default 4)
         */
        public Builder setGpsPrecison(Integer gpsPrecison) {
            this.gpsPrecison = gpsPrecison;
            return this;
        }

        private void validateProfile() throws ServicesException {
            if (profile == null || !(profile.equals(DirectionsCriteria.PROFILE_CYCLING)
                    || profile.equals(DirectionsCriteria.PROFILE_DRIVING)
                    || profile.equals(DirectionsCriteria.PROFILE_WALKING))) {
                throw new ServicesException(
                        "Using Mapbox Map Matching requires setting a valid profile.");
            }
        }

        private void validateGpsPrecision() throws ServicesException {
            if (gpsPrecison != null && (gpsPrecison < 1 || gpsPrecison > 10)) {
                throw new ServicesException(
                        "Using Mapbox Map Matching requires setting a valid GPS precision.");
            }
        }

        @Override
        public MapboxMapMatching build() throws ServicesException {
            validateAccessToken(accessToken);
            validateProfile();
            validateGpsPrecision();
            return new MapboxMapMatching(this);
        }

    }
}
