package com.mapbox.services.geocoding.v5;

import com.mapbox.services.Constants;
import com.mapbox.services.commons.MapboxService;
import com.mapbox.services.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.models.Position;

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
 * Created by antonio on 1/30/16.
 */
public class MapboxGeocoding implements MapboxService<GeocodingResponse> {

    private Builder builder = null;
    private GeocodingService service = null;
    private Call<GeocodingResponse> call = null;
    private Observable<GeocodingResponse> observable = null;

    public MapboxGeocoding(Builder builder) {
        this.builder = builder;
    }

    public GeocodingService getService() {
        // No need to recreate it
        if (service != null) return service;

        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        service = retrofit.create(GeocodingService.class);
        return service;
    }

    public Call<GeocodingResponse> getCall() {
        // No need to recreate it
        if (call != null) return call;

        call = getService().getCall(
                builder.geocodingDataset,
                builder.query,
                builder.accessToken,
                builder.proximity,
                builder.geocodingType);

        return call;
    }

    @Override
    public Response<GeocodingResponse> executeCall() throws IOException {
        return getCall().execute();
    }

    @Override
    public void enqueueCall(Callback<GeocodingResponse> callback) {
        getCall().enqueue(callback);
    }

    @Override
    public void cancelCall() {
        getCall().cancel();
    }

    @Override
    public Call<GeocodingResponse> cloneCall() {
        return getCall().clone();
    }

    @Override
    public Observable<GeocodingResponse> getObservable() {
        // No need to recreate it
        if (observable != null) return observable;

        observable = service.getObservable(
                builder.geocodingDataset,
                builder.query,
                builder.accessToken,
                builder.proximity,
                builder.geocodingType);

        return observable;
    }

    /*
     * Builder
     */

    public static class Builder {

        /*
         * Required
         */

        private String accessToken;
        private String query;

        private String geocodingDataset;

        /*
         * Optional (Retrofit will omit these from the request if they remain null)
         */

        private String proximity = null;

        private String geocodingType = null;

        public Builder() {
            // Defaults
            geocodingDataset = com.mapbox.services.geocoding.v5.GeocodingCriteria.DATASET_PLACES;
        }

        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder setDataset(String geocodingDataset) {
            this.geocodingDataset = geocodingDataset;
            return this;
        }

        public Builder setLocation(String location) {
            query = location;
            return this;
        }

        public Builder setCoordinates(Position position) {
            if (position == null) return this;
            query = String.format("%f,%f", position.getLongitude(), position.getLatitude());
            return this;
        }

        public Builder setProximity(Position position) {
            if (position == null) return this;
            proximity = String.format("%f,%f", position.getLongitude(), position.getLatitude());
            return this;
        }

        public Builder setType(String geocodingType) {
            this.geocodingType = geocodingType;
            return this;
        }

        public MapboxGeocoding build() {
            return new MapboxGeocoding(this);
        }

    }

}
