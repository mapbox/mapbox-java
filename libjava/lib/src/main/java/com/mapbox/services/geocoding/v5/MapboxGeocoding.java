package com.mapbox.services.geocoding.v5;

import com.mapbox.services.Constants;
import com.mapbox.services.commons.MapboxBuilder;
import com.mapbox.services.commons.MapboxService;
import com.mapbox.services.commons.ServicesException;
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
 * The Mapbox geocoding client (v5).
 */
public class MapboxGeocoding implements MapboxService<GeocodingResponse> {

    private Builder builder = null;
    private GeocodingService service = null;
    private Call<GeocodingResponse> call = null;
    private Observable<GeocodingResponse> observable = null;

    // Allows testing
    private String baseUrl = Constants.BASE_API_URL;

    public MapboxGeocoding(Builder builder) {
        this.builder = builder;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public GeocodingService getService() {
        // No need to recreate it
        if (service != null) return service;

        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(baseUrl)
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
                builder.getGeocodingDataset(),
                builder.getQuery(),
                builder.getAccessToken(),
                builder.getProximity(),
                builder.getGeocodingType());

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
                builder.getGeocodingDataset(),
                builder.getQuery(),
                builder.getAccessToken(),
                builder.getProximity(),
                builder.getGeocodingType());

        return observable;
    }

    /**
     * Builds your geocoder query by adding parameters.
     */
    public static class Builder extends MapboxBuilder {

        // Required
        private String accessToken;
        private String query;
        private String geocodingDataset;

        // Optional (Retrofit will omit these from the request if they remain null)
        private String proximity = null;
        private String geocodingType = null;

        public Builder() {
            // Defaults
            geocodingDataset = com.mapbox.services.geocoding.v5.GeocodingCriteria.DATASET_PLACES;
        }

        /**
         * Required to call when building {@link MapboxGeocoding.Builder}
         *
         * @param accessToken Mapbox access token, you must have a Mapbox account
         *                    in order to use this library.
         */
        @Override
        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
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

        public Builder setDataset(String geocodingDataset) {
            this.geocodingDataset = geocodingDataset;
            return this;
        }

        /**
         * Location around which to bias results.
         *
         * @param position A {@link Position}.
         */
        public Builder setProximity(Position position) {
            if (position == null) return this;
            proximity = String.format("%f,%f", position.getLongitude(), position.getLatitude());
            return this;
        }

        /**
         * Filter results by one or more type. Options are country, region, postcode, place,
         * locality, neighborhood, address, poi. Multiple options can be comma-separated.
         *
         * @param geocodingType String filtering the geocoder result types.
         */
        public Builder setType(String geocodingType) {
            this.geocodingType = geocodingType;
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
         * @return your geocoder query.
         */
        public String getQuery() {
            return query;
        }

        public String getGeocodingDataset() {
            return geocodingDataset;
        }

        /**
         * Location around which you biased the results.
         *
         * @return String with the format longitude, latitude.
         */
        public String getProximity() {
            return proximity;
        }

        /**
         * If you filtered your results by one or more types you can get what those filters are by
         * using this method.
         *
         * @return String with list of filters you used.
         */
        public String getGeocodingType() {
            return geocodingType;
        }

        @Override
        public MapboxGeocoding build() throws ServicesException {
            validateAccessToken(accessToken);
            return new MapboxGeocoding(this);
        }
    }
}
