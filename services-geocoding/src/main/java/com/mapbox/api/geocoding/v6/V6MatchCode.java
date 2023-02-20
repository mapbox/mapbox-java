package com.mapbox.api.geocoding.v6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.geocoding.v5.models.GeocodingAdapterFactory;

import java.io.Serializable;

/**
 * The match_code object in the Geocoding API response provides a Confidence score,
 * which indicates how well the result matches the input query.
 * https://docs.mapbox.com/api/search/geocoding-v6/#smart-address-match
 */
@AutoValue
public abstract class V6MatchCode implements Serializable {

    /**
     * Confidence score, which indicates how well the result matches the input query.
     * Possible values are:
     *  - exact: All address components match, allowing for abbreviations, punctuation, and capitalization variations.
     *  - high: One component (excluding house_number or region) may have been corrected.
     *      Additionally, if only house_number, street, and postcode are provided and match, high confidence is returned.
     *  - medium: Two components (excluding house_number or region) may have changed. Allows for minor misspellings.
     *      If house_number, street, place, and postcode are matched the region may be corrected.
     *  - low: House Number, Region, or more than 2 other components have been corrected.
     *
     * @return Confidence score, which indicates how well the result matches the input query.
     */
    @Nullable
    @SerializedName("confidence")
    public abstract String confidence();

    @Nullable
    @SerializedName("exact_match")
    public abstract Boolean exactMatch();

    @Nullable
    @SerializedName("house_number")
    public abstract String houseNumber();

    @Nullable
    @SerializedName("street")
    public abstract String street();

    @Nullable
    @SerializedName("postcode")
    public abstract String postcode();

    @Nullable
    @SerializedName("place")
    public abstract String place();

    @Nullable
    @SerializedName("region")
    public abstract String region();

    @Nullable
    @SerializedName("locality")
    public abstract String locality();

    @Nullable
    @SerializedName("country")
    public abstract String country();

    /**
     * Create a V6MatchCode object from JSON.
     *
     * @param json string of JSON making up a carmen context
     * @return this class using the defined information in the provided JSON string
     */
    @SuppressWarnings("unused")
    public static V6MatchCode fromJson(@NonNull String json) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(GeocodingAdapterFactory.create())
            .create();
        return gson.fromJson(json, V6MatchCode.class);
    }

    /**
     * Gson type adapter for parsing Gson to this class.
     *
     * @param gson the built {@link Gson} object
     * @return the type adapter for this class
     */
    public static TypeAdapter<V6MatchCode> typeAdapter(Gson gson) {
        return new AutoValue_V6MatchCode.GsonTypeAdapter(gson);
    }

    static Builder builder() {
        return new AutoValue_V6MatchCode.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {

        public abstract Builder confidence(@Nullable String confidence);
        public abstract Builder exactMatch(@Nullable Boolean exactMatch);
        public abstract Builder houseNumber(@Nullable String houseNumber);
        public abstract Builder street(@Nullable String street);
        public abstract Builder postcode(@Nullable String postcode);
        public abstract Builder place(@Nullable String place);
        public abstract Builder region(@Nullable String region);
        public abstract Builder locality(@Nullable String locality);
        public abstract Builder country(@Nullable String country);

        public abstract V6MatchCode build();
    }
}
