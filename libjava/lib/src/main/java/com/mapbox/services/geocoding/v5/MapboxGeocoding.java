package com.mapbox.services.geocoding.v5;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.services.Constants;
import com.mapbox.services.commons.MapboxBuilder;
import com.mapbox.services.commons.MapboxService;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;
import com.mapbox.services.geocoding.v5.gson.CarmenGeometryDeserializer;
import com.mapbox.services.geocoding.v5.models.GeocodingResponse;

import java.io.IOException;
import java.util.Locale;

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
public class MapboxGeocoding extends MapboxService<GeocodingResponse> {

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

        // Gson instance with type adapters
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Geometry.class, new CarmenGeometryDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        service = retrofit.create(GeocodingService.class);
        return service;
    }

    public Call<GeocodingResponse> getCall() {
        // No need to recreate it
        if (call != null) return call;

        call = getService().getCall(
                getHeaderUserAgent(),
                builder.getMode(),
                builder.getQuery(),
                builder.getAccessToken(),
                builder.getCountry(),
                builder.getProximity(),
                builder.getGeocodingTypes(),
                builder.getAutocomplete(),
                builder.getBbox());

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

        observable = getService().getObservable(
                getHeaderUserAgent(),
                builder.getMode(),
                builder.getQuery(),
                builder.getAccessToken(),
                builder.getCountry(),
                builder.getProximity(),
                builder.getGeocodingTypes(),
                builder.getAutocomplete(),
                builder.getBbox());

        return observable;
    }

    /**
     * Builds your geocoder query by adding parameters.
     */
    public static class Builder extends MapboxBuilder {

        // Required
        private String accessToken;
        private String query;
        private String mode;

        // Optional (Retrofit will omit these from the request if they remain null)
        private String country = null;
        private String proximity = null;
        private String geocodingTypes = null;
        private Boolean autocomplete = null;
        private String bbox = null;

        public Builder() {
            // Defaults
            mode = GeocodingCriteria.MODE_PLACES;
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
            query = String.format(Locale.US, "%f,%f",
                    position.getLongitude(),
                    position.getLatitude());
            return this;
        }

        public Builder setMode(String mode) {
            this.mode = mode;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setCountries(String[] countries) {
            this.country = TextUtils.join(",", countries);
            return this;
        }

        /**
         * Location around which to bias results.
         *
         * @param position A {@link Position}.
         */
        public Builder setProximity(Position position) {
            if (position == null) return this;
            proximity = String.format(Locale.US, "%f,%f",
                    position.getLongitude(),
                    position.getLatitude());
            return this;
        }

        /**
         * Filter results by one or more type. Options are country, region, postcode, place,
         * locality, neighborhood, address, poi. Multiple options can be comma-separated.
         *
         * @param geocodingType String filtering the geocoder result types.
         */
        public Builder setGeocodingType(String geocodingType) {
            this.geocodingTypes = geocodingType;
            return this;
        }

        public Builder setGeocodingTypes(String[] geocodingType) {
            this.geocodingTypes = TextUtils.join(",", geocodingType);
            return this;
        }

        public Builder setAutocomplete(boolean autocomplete) {
            this.autocomplete = autocomplete;
            return this;
        }

        public Builder setBbox(double minX, double minY, double  maxX, double  maxY) {
            this.bbox = String.format(Locale.US, "%f,%f,%f,%f", minX, minY, maxX, maxY);
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

        public String getMode() {
            return mode;
        }

        public String getCountry() {
            return country;
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
        public String getGeocodingTypes() {
            return geocodingTypes;
        }

        public Boolean getAutocomplete() {
            return autocomplete;
        }

        public String getBbox() {
            return bbox;
        }

        @Override
        public MapboxGeocoding build() throws ServicesException {
            validateAccessToken(accessToken);
            return new MapboxGeocoding(this);
        }
    }
}
