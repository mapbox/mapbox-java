package com.mapbox.api.geocoding.v6;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.api.geocoding.v5.SingleElementSafeListTypeAdapter;
import com.mapbox.api.geocoding.v5.models.GeocodingAdapterFactory;
import com.mapbox.core.MapboxService;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.core.utils.ApiCallHelper;
import com.mapbox.core.utils.MapboxUtils;
import com.mapbox.core.utils.TextUtils;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.GeometryAdapterFactory;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.gson.BoundingBoxTypeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

@AutoValue
public abstract class MapboxGeocodingV6 extends MapboxService<V6Response, V6GeocodingService> {

    protected MapboxGeocodingV6() {
        super(V6GeocodingService.class);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(GeocodingAdapterFactory.create())
                .registerTypeAdapterFactory(GeometryAdapterFactory.create())
                .registerTypeAdapterFactory(SingleElementSafeListTypeAdapter.FACTORY)
                .registerTypeAdapter(BoundingBox.class, new BoundingBoxTypeAdapter());
    }

    @Override
    protected Call<V6Response> initializeCall() {
        return getService().getCall(
                ApiCallHelper.getHeaderUserAgent(clientAppName()),
                query(),
                accessToken(),
                country(),
                proximity(),
                geocodingTypes(),
                autocomplete(),
                bbox(),
                limit(),
                languages()
        );
    }

    @NonNull
    abstract String query();

    @NonNull
    abstract String accessToken();

    @NonNull
    @Override
    protected abstract String baseUrl();

    @Nullable
    abstract String country();

    @Nullable
    abstract String proximity();

    @Nullable
    abstract String geocodingTypes();

    @Nullable
    abstract Boolean autocomplete();

    @Nullable
    abstract String bbox();

    @Nullable
    abstract String limit();

    @Nullable
    abstract String languages();

    @Nullable
    abstract String clientAppName();


    /**
     * Build a new {@link MapboxGeocodingV6} object with the initial values set for
     * {@link #baseUrl()}}.
     *
     * @return a {@link MapboxGeocodingV6.Builder} object for creating this object
     */
    public static MapboxGeocodingV6.Builder builder() {
        return new AutoValue_MapboxGeocodingV6.Builder()
                .baseUrl(Constants.BASE_API_URL);
    }

    /**
     * This builder is used to create a new request to the Mapbox Geocoding API. At a bare minimum,
     * your request must include an access token and a query of some kind. All other fields can
     * be left alone in order to use the default behaviour of the API.
     * <p>
     * Note to contributors: All optional booleans in this builder use the object {@code Boolean}
     * rather than the primitive to allow for unset (null) values.
     * </p>
     */
    @AutoValue.Builder
    public abstract static class Builder {

        private final List<String> countries = new ArrayList<>();

        /**
         * This method can be used for performing a forward geocode on a string representing an address.
         *
         * @param query a String containing the text you'd like to forward geocode
         * @return this builder for chaining options together
         */
        public abstract MapboxGeocodingV6.Builder query(@NonNull String query);

        /**
         * Bias local results base on a provided {@link Point}. This oftentimes increases accuracy in
         * the returned results.
         *
         * @param proximity a point defining the proximity you'd like to bias the results around
         * @return this builder for chaining options together
         */
        public MapboxGeocodingV6.Builder proximity(@NonNull Point proximity) {
            proximity(String.format(Locale.US, "%s,%s",
                    TextUtils.formatCoordinate(proximity.longitude()), proximity.latitude()));
            return this;
        }

        abstract MapboxGeocodingV6.Builder proximity(String proximity);

        /**
         * This optionally can be set to filter the results returned back after making your forward or
         * reverse geocoding request. A null value can't be passed in and only values defined in
         * {@link V6FeatureType.V6FeatureTypeCriteria} are allowed.
         *
         * @param geocodingTypes optionally filter the result types by one or more defined types inside
         *                       the {@link V6FeatureType.V6FeatureTypeCriteria}
         * @return this builder for chaining options together
         */
        public MapboxGeocodingV6.Builder geocodingTypes(@NonNull @V6FeatureType.V6FeatureTypeCriteria String... geocodingTypes) {
            geocodingTypes(TextUtils.join(",", geocodingTypes));
            return this;
        }

        abstract MapboxGeocodingV6.Builder geocodingTypes(String geocodingTypes);

        /**
         * Add a single country locale to restrict the results. This method can be called as many times
         * as needed inorder to add multiple countries.
         *
         * @param country limit geocoding results to one
         * @return this builder for chaining options together
         */
        public MapboxGeocodingV6.Builder country(Locale country) {
            countries.add(country.getCountry());
            return this;
        }

        /**
         * Limit results to one or more countries. Options are ISO 3166 alpha 2 country codes separated
         * by commas.
         *
         * @param country limit geocoding results to one
         * @return this builder for chaining options together
         */
        public MapboxGeocodingV6.Builder country(String... country) {
            countries.addAll(Arrays.asList(country));
            return this;
        }

        /**
         * Limit results to one or more countries. Options are ISO 3166 alpha 2 country codes separated
         * by commas.
         *
         * @param country limit geocoding results to one
         * @return this builder for chaining options together
         */
        public abstract MapboxGeocodingV6.Builder country(String country);

        /**
         * This controls whether autocomplete results are included. Autocomplete results can partially
         * match the query: for example, searching for {@code washingto} could include washington even
         * though only the prefix matches. Autocomplete is useful for offering fast, type-ahead results
         * in user interfaces.
         * <p>
         * If your queries represent complete addresses or place names, you can disable this behavior
         * and exclude partial matches by setting this to false, the defaults true.
         *
         * @param autocomplete optionally set whether to allow returned results to attempt prediction of
         *                     the full words prior to the user completing the search terms
         * @return this builder for chaining options together
         */
        public abstract MapboxGeocodingV6.Builder autocomplete(Boolean autocomplete);

        /**
         * Limit the results to a defined bounding box. Unlike {@link #proximity()}, this will strictly
         * limit results to within the bounding box only. If simple biasing is desired rather than a
         * strict region, use proximity instead.
         *
         * @param bbox the bounding box
         * @return this builder for chaining options together
         */
        public MapboxGeocodingV6.Builder bbox(BoundingBox bbox) {
            bbox(
                String.format(Locale.US, "%s,%s,%s,%s",
                    TextUtils.formatCoordinate(bbox.southwest().longitude()),
                    TextUtils.formatCoordinate(bbox.southwest().latitude()),
                    TextUtils.formatCoordinate(bbox.northeast().longitude()),
                    TextUtils.formatCoordinate(bbox.northeast().latitude())
                )
            );
            return this;
        }

        abstract MapboxGeocodingV6.Builder bbox(@NonNull String bbox);

        /**
         * This optionally specifies the maximum number of results to return. For forward geocoding, the
         * default is 5 and the maximum is 10. For reverse geocoding, the default is 1 and the maximum
         * is 5. If a limit other than 1 is used for reverse geocoding, a single types option must also
         * be specified.
         *
         * @param limit the number of returned results
         * @return this builder for chaining options together
         */
        public MapboxGeocodingV6.Builder limit(@IntRange(from = 1, to = 10) int limit) {
            limit(String.valueOf(limit));
            return this;
        }

        abstract MapboxGeocodingV6.Builder limit(String limit);

        /**
         * This optionally specifies the desired response language for user queries. For forward
         * geocodes, results that match the requested language are favored over results in other
         * languages. If more than one language tag is supplied, text in all requested languages will be
         * returned. For forward geocodes with more than one language tag, only the first language will
         * be used to weight results.
         * <p>
         * Any valid IETF language tag can be submitted, and a best effort will be made to return
         * results in the requested language or languages, falling back first to similar and then to
         * common languages in the event that text is not available in the requested language. In the
         * event a fallback language is used, the language field will have a different value than the
         * one requested.
         * <p>
         * Translation availability varies by language and region, for a full list of supported regions,
         * see the link provided below.
         *
         * @param languages one or more locale's specifying the language you'd like results to support
         * @return this builder for chaining options together
         * @see <a href="https://docs.mapbox.com/api/search/geocoding-v6/#language-coverage">Supported languages</a>
         */
        public MapboxGeocodingV6.Builder languages(Locale... languages) {
            final String[] languageStrings = new String[languages.length];
            for (int i = 0; i < languages.length; i++) {
                languageStrings[i] = languages[i].getLanguage();
            }
            languages(TextUtils.join(",", languageStrings));
            return this;
        }

        /**
         * This optionally specifies the desired response language for user queries. For forward
         * geocodes, results that match the requested language are favored over results in other
         * languages. If more than one language tag is supplied, text in all requested languages will be
         * returned. For forward geocodes with more than one language tag, only the first language will
         * be used to weight results.
         * <p>
         * Any valid IETF language tag can be submitted, and a best effort will be made to return
         * results in the requested language or languages, falling back first to similar and then to
         * common languages in the event that text is not available in the requested language. In the
         * event a fallback language is used, the language field will have a different value than the
         * one requested.
         * <p>
         * Translation availability varies by language and region, for a full list of supported regions,
         * see the link provided below.
         *
         * @param languages a String specifying the language or languages you'd like results to support
         * @return this builder for chaining options together
         * @see <a href="https://docs.mapbox.com/api/search/geocoding-v6/#language-coverage">Supported languages</a>
         */
        public abstract MapboxGeocodingV6.Builder languages(String languages);

        /**
         * Required to call when this is being built. If no access token provided,
         * {@link ServicesException} will be thrown.
         *
         * @param accessToken Mapbox access token, You must have a Mapbox account inorder to use
         *                    the Geocoding API
         * @return this builder for chaining options together
         */
        public abstract MapboxGeocodingV6.Builder accessToken(@NonNull String accessToken);

        /**
         * Base package name or other simple string identifier. Used inside the calls user agent header.
         *
         * @param clientAppName base package name or other simple string identifier
         * @return this builder for chaining options together
         */
        public abstract MapboxGeocodingV6.Builder clientAppName(@NonNull String clientAppName);

        /**
         * Optionally change the APIs base URL to something other then the default Mapbox one.
         *
         * @param baseUrl base url used as end point
         * @return this builder for chaining options together
         */
        public abstract MapboxGeocodingV6.Builder baseUrl(@NonNull String baseUrl);

        abstract MapboxGeocodingV6 autoBuild();

        /**
         * Build a new {@link MapboxGeocodingV6} object.
         *
         * @return a new {@link MapboxGeocodingV6} using the provided values in this builder
         */
        public MapboxGeocodingV6 build() {
            if (!countries.isEmpty()) {
                country(TextUtils.join(",", countries.toArray()));
            }

            final MapboxGeocodingV6 geocoding = autoBuild();
            if (!MapboxUtils.isAccessTokenValid(geocoding.accessToken())) {
                throw new ServicesException("Using Mapbox Services requires setting a valid access token.");
            }
            if (geocoding.query().isEmpty()) {
                throw new ServicesException("A query with at least one character or digit is required.");
            }
            return geocoding;
        }
    }
}
