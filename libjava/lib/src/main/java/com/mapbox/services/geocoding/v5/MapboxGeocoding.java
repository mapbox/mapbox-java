package com.mapbox.services.geocoding.v5;

import com.mapbox.services.Constants;
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

/**
 * Created by antonio on 1/30/16.
 */
public class MapboxGeocoding {

    private Call<GeocodingResponse> _call;

    public MapboxGeocoding(Builder builder) {
        GeocodingService service = getService();
        _call = service.geocode(
                builder._geocodingDataset,
                builder._query,
                builder._accessToken,
                builder._proximity,
                builder._geocodingType);
    }

    /*
     * Retrofit API
     */

    public Response<GeocodingResponse> execute() throws IOException {
        return _call.execute();
    }

    public void enqueue(Callback<GeocodingResponse> callback) {
        _call.enqueue(callback);
    }

    public void cancel() {
        _call.cancel();
    }

    public Call<GeocodingResponse> clone() {
        return _call.clone();
    }

    GeocodingService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        GeocodingService service = retrofit.create(GeocodingService.class);
        return service;
    }

    /*
     * Builder
     */

    public static class Builder {

        /*
         * Required
         */

        private String _accessToken;
        private String _query;

        private String _geocodingDataset;

        /*
         * Optional (Retrofit will omit these from the request if they remain null)
         */

        private String _proximity = null;

        private String _geocodingType = null;

        public Builder() {
            // Defaults
            _geocodingDataset = com.mapbox.services.geocoding.v5.GeocodingCriteria.DATASET_PLACES;
        }

        public Builder setAccessToken(String accessToken) {
            _accessToken = accessToken;
            return this;
        }

        public Builder setDataset(String geocodingDataset) {
            _geocodingDataset = geocodingDataset;
            return this;
        }

        public Builder setLocation(String location) {
            _query = location;
            return this;
        }

        public Builder setCoordinates(Position position) {
            if (position == null) return this;
            _query = String.format("%f,%f", position.getLongitude(), position.getLatitude());
            return this;
        }

        public Builder setProximity(Position position) {
            if (position == null) return this;
            _proximity = String.format("%f,%f", position.getLongitude(), position.getLatitude());
            return this;
        }

        public Builder setType(String geocodingType) {
            _geocodingType = geocodingType;
            return this;
        }

        public MapboxGeocoding build() {
            return new MapboxGeocoding(this);
        }

    }

}
