package com.mapbox.api.geocoding.v6;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.Point;

import java.io.Serializable;

/**
 * Object containing coordinate parameters (lat, long) and accuracy.
 */
@AutoValue
public abstract class V6Coordinates implements Serializable {

    /**
     * Longitude of result.
     * @return Longitude of result.
     */
    @Nullable
    @SerializedName("longitude")
    public abstract Double longitude();

    /**
     * Latitude of result.
     * @return Latitude of result.
     */
    @Nullable
    @SerializedName("latitude")
    public abstract Double latitude();

    /**
     * Result coordinate as a {@code Point}.
     * @return Result coordinate as a {@code Point}.
     */
    @Nullable
    public Point point() {
        final Double lat = latitude();
        final Double lon = longitude();
        if (lat != null && lon != null) {
            return Point.fromLngLat(lon, lat);
        }
        return null;
    }

    /**
     * Accuracy metric for a returned address-type result. Possible values are:
     *  - rooftop: Result is for a specific building/entrance
     *  - parcel: Result is derived from a parcel centroid
     *  - point: Result is a known address point but has no specific accuracy
     *  - interpolated: Result has been interpolated from an address range
     *  - intersection: Result is for a block or intersection
     *  - approximate: Result is an approximate location
     *  - street: Result is a street centroid
     *
     * @return Accuracy metric for a returned address-type result.
     */
    @Nullable
    @SerializedName("accuracy")
    public abstract String accuracy();

    /**
     * Gson type adapter for parsing Gson to this class.
     *
     * @param gson the built {@link Gson} object
     * @return the type adapter for this class
     */
    public static TypeAdapter<V6Coordinates> typeAdapter(Gson gson) {
        return new AutoValue_V6Coordinates.GsonTypeAdapter(gson);
    }
}
