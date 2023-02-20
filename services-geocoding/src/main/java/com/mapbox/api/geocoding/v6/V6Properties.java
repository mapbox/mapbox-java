package com.mapbox.api.geocoding.v6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.geocoding.v5.models.GeocodingAdapterFactory;

import java.io.Serializable;

@AutoValue
public abstract class V6Properties implements Serializable {

    /**
     * Feature id.
     * @return Feature id.
     */
    @Nullable
    @SerializedName("mapbox_id")
    public abstract String mapboxId();

    /**
     * A string describing the type of the feature.
     * Options are country, region, postcode, district, place, locality, neighborhood, address.
     * @return A string describing the type of the feature.
     */
    @Nullable
    @SerializedName("feature_type")
    public abstract String featureType();

    /**
     * House number or building number.
     * @return House number or building number.
     */
    @Nullable
    @SerializedName("address_number")
    public abstract String addressNumber();

    /**
     * Name of the street of the address.
     * @return Name of the street of the address.
     */
    @Nullable
    @SerializedName("street")
    public abstract String street();

    /**
     * Formatted string of address_number and street.
     * @return Formatted string of address_number and street.
     */
    @Nullable
    @SerializedName("name")
    public abstract String name();

    /**
     * A canonical or otherwise more common alias for the feature name.
     * @return A canonical or otherwise more common alias for the feature name.
     */
    @Nullable
    @SerializedName("name_preferred")
    public abstract String namePreferred();

    /**
     * Formatted string of result context: place region country postcode. The part of the result which comes after name.
     * @return Formatted string of result context: place region country postcode. The part of the result which comes after name.
     */
    @Nullable
    @SerializedName("place_formatted")
    public abstract String placeFormatted();

    /**
     * An object representing the hierarchy of encompassing parent features.
     * Each parent feature may include any of the above properties. country, region, postcode, district, place, locality, neighborhood
     * @return An object representing the hierarchy of encompassing parent features.
     */
    @Nullable
    @SerializedName("context")
    public abstract JsonObject context();

    /**
     * Object containing coordinate parameters (lat, long) and accuracy.
     * @return Object containing coordinate parameters (lat, long) and accuracy.
     */
    @Nullable
    @SerializedName("coordinates")
    public abstract V6Coordinates coordinates();

    /**
     * Additional metadata indicating how the result components match to the input query.
     * @return Additional metadata indicating how the result components match to the input query.
     */
    @Nullable
    @SerializedName("match_code")
    public abstract V6MatchCode matchCode();

    /**
     * Create a V6Property object from JSON.
     *
     * @param json string of JSON making up a carmen context
     * @return this class using the defined information in the provided JSON string
     */
    @SuppressWarnings("unused")
    public static V6Properties fromJson(@NonNull String json) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(GeocodingAdapterFactory.create())
            .create();
        return gson.fromJson(json, V6Properties.class);
    }

    /**
     * Gson type adapter for parsing Gson to this class.
     *
     * @param gson the built {@link Gson} object
     * @return the type adapter for this class
     */
    public static TypeAdapter<V6Properties> typeAdapter(Gson gson) {
        return new AutoValue_V6Properties.GsonTypeAdapter(gson);
    }
}
